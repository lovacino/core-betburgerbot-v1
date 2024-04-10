package it.lovacino.betburger.bot.batch;

import it.lovacino.betburger.bot.domain.AccountBet;
import it.lovacino.betburger.bot.domain.Betting;
import it.lovacino.betburger.bot.domain.Bookmaker;
import it.lovacino.betburger.bot.domain.EventSource;
import it.lovacino.betburger.bot.domain.enumeration.*;
import it.lovacino.betburger.bot.repository.*;
import it.lovacino.betburger.bot.service.WhatsAppService;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class BetBurgerBot {

    private boolean inProgress = false;
    private static final Logger logger = LoggerFactory.getLogger("BOT");
    private static final long TEN_MINUTE_MILLIS = 600000;
    private String accessToken = "9623e250f3fade7a48bd1a97132e5a02"; //BET BURGER ACCESS TOKEN TODO: DA SALVARE E RECUPERARE DAL DB
    private Integer[] filtersId = new Integer[] { 402817 }; //BET BURGER FILTERS ID TODO: DA SALVARE E RECUPERARE DAL DB

    @Autowired
    private AccountBetRepository accountBetRepository;

    @Autowired
    private BettingRepository bettingRepository;

    @Autowired
    private BookmakerRepository bookmakerRepository;

    @Autowired
    private SportRepository sportRepository;

    @Autowired
    private PeriodRepository periodRepository;

    @Autowired
    private BetTypeRepository betTypeRepository;

    @Autowired
    private EventSourceRepository eventSourceRepository;

    @Autowired
    private WhatsAppService whatsAppService;

    @Scheduled(cron = "0/5 * * ? * *")
    public void pollingBot() {
        logger.info("[BOT] pollingBot START -> inProgress: {}", inProgress);
        try {
            if (!inProgress) {
                logger.info("[BOT] pollingBot IN PROGRESS");
                inProgress = true;
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
                for (Integer id : filtersId) {
                    map.add("search_filter", id.toString());
                }
                map.add("access_token", accessToken);
                HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
                JSONObject responseJson = new JSONObject(
                    restTemplate.postForObject("https://rest-api-pr.betburger.com/api/v1/valuebets/bot_pro_search", request, String.class)
                );
                List<EventSource> events = parseResponse(responseJson);
                if (!events.isEmpty()) {
                    events.forEach(event -> {
                        EventSource eventToBetting = processEvent(event);
                        //se eventToBetting è diverso da null e arrow è HIDDEN e la data di update koef di bet burger deve essere distante non più di 10 minuti dalla data corrente devo effettuare la scommessa
                        //TODO: DA VERIFICARE aggiungo 2 ore a update kouef date per la differenza tra i 2 sistemi
                        if (
                            eventToBetting != null &&
                            eventToBetting.getArrow().equals(BetArrow.HIDDEN) &&
                            (ZonedDateTime.now().toInstant().toEpochMilli() -
                                    eventToBetting.getKoefLastModifiedAt().plus(2, ChronoUnit.HOURS).toInstant().toEpochMilli()) <=
                                TEN_MINUTE_MILLIS
                        ) {
                            prepareAndSendBetting(eventToBetting);
                        }
                    });
                }
            }
        } catch (Exception e) {
            logger.error("[BOT] pollingBot ERROR", e);
        } finally {
            logger.info("[BOT] pollingBot END");
            inProgress = false;
        }
    }

    private void prepareAndSendBetting(EventSource eventToBetting) {
        logger.info("[BOT] prepareBetting START event: {}", eventToBetting);
        try {
            AccountBet filter = new AccountBet();
            Bookmaker bookmaker = new Bookmaker();
            bookmaker.setId(eventToBetting.getBookmaker().getId());
            filter.setBookmaker(bookmaker);
            filter.setState(AccountBetState.ACTIVE);
            List<AccountBet> accountBets = accountBetRepository.findAll(Example.of(filter));

            if (!CollectionUtils.isEmpty(accountBets)) {
                for (AccountBet accountBet : accountBets) {
                    try {
                        if (_checkDayAndHour(accountBet)) {
                            Betting betting = new Betting();
                            betting.setBetBurgerId(eventToBetting.getBetBurgerId());
                            betting.setBetType(eventToBetting.getBetType());
                            betting.setBetTypeParam(eventToBetting.getBetTypeParam());
                            betting.setLeague(eventToBetting.getLeague());
                            betting.setHome(eventToBetting.getHome());
                            betting.setAway(eventToBetting.getAway());
                            betting.setEventName(eventToBetting.getEventName());
                            betting.setKoef(eventToBetting.getKoef());
                            betting.setAccount(accountBet);
                            betting.setAmountBet(_calculateBet(accountBet, eventToBetting.getKoef()));
                            betting.setAmountBetWin(null);
                            betting.setEventSourceId(eventToBetting.getId());
                            betting.setPeriod(eventToBetting.getPeriod());
                            betting.setSport(eventToBetting.getSport());
                            betting.setState(BettingState.CREATED);
                            betting.setStartedAt(ZonedDateTime.now());
                            betting.setUpdatedAt(ZonedDateTime.now());
                            betting = bettingRepository.save(betting);
                            sendBetting(betting);
                        }
                    } catch (Exception ex) {
                        logger.error(
                            "[BOT] prepareBetting ERROR process event " +
                            eventToBetting.getEventName() +
                            " to account " +
                            accountBet.getName(),
                            ex
                        );
                    }
                }
            }
        } catch (Exception e) {
            logger.error("[BOT] prepareBetting ERROR", e);
        }
    }

    private boolean _checkDayAndHour(AccountBet accountBet) {
        logger.info(
            "[BOT] _checkDayAndHour account bet: {}, hour start: {}, hour end: {} START",
            accountBet.getName(),
            accountBet.getHourActiveStart(),
            accountBet.getHourActiveEnd()
        );
        if (
            accountBet.getHourActiveStart() <= ZonedDateTime.now().getHour() &&
            accountBet.getHourActiveEnd() >= ZonedDateTime.now().getHour()
        ) {
            logger.info("[BOT] _checkDayAndHour Hour OK");
            if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.MONDAY) && accountBet.getFlgActiveLun()) {
                logger.info("[BOT] _checkDayAndHour DAY LUB OK");
                return true;
            } else if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.TUESDAY) && accountBet.getFlgActiveMar()) {
                logger.info("[BOT] _checkDayAndHour DAY MAR OK");
                return true;
            } else if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.WEDNESDAY) && accountBet.getFlgActiveMer()) {
                logger.info("[BOT] _checkDayAndHour DAY MER OK");
                return true;
            } else if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.THURSDAY) && accountBet.getFlgActiveGio()) {
                logger.info("[BOT] _checkDayAndHour DAY GIO OK");
                return true;
            } else if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.FRIDAY) && accountBet.getFlgActiveVen()) {
                logger.info("[BOT] _checkDayAndHour DAY VEN OK");
                return true;
            } else if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.SATURDAY) && accountBet.getFlgActiveSab()) {
                logger.info("[BOT] _checkDayAndHour DAY SAB OK");
                return true;
            } else if (ZonedDateTime.now().getDayOfWeek().equals(DayOfWeek.SUNDAY) && accountBet.getFlgActiveDom()) {
                logger.info("[BOT] _checkDayAndHour DAY DOM OK");
                return true;
            }
        }
        logger.info("[BOT] _checkDayAndHour return false -> SKIPPED");
        return false;
    }

    private void sendBetting(Betting betting) {
        logger.info("[BOT] sendBetting START betting: {}", betting);
        try {
            betting.setUpdatedAt(ZonedDateTime.now());
            betting.setState(BettingState.SENDING);
            betting = bettingRepository.save(betting);
            AccountBet accountBet = accountBetRepository.findById(betting.getAccount().getId()).get();
            AccountBetType accountBetType = accountBet.getType();
            if (accountBetType.equals(AccountBetType.WHATSAPP)) {
                String whatsAppNumber = accountBet.getWhatsAppNumber();
                String message = _buildMessage(betting);
                boolean sended = whatsAppService.sendMessage(whatsAppNumber, message);
                if (sended) {
                    betting.setUpdatedAt(ZonedDateTime.now());
                    betting.setState(BettingState.SENDED);
                    bettingRepository.save(betting);
                } else {
                    betting.setUpdatedAt(ZonedDateTime.now());
                    betting.setState(BettingState.ERROR);
                    betting.setErrorMessage("Impossibile inviare il messaggio tremite whatsApp");
                    bettingRepository.save(betting);
                }
            } else if (accountBetType.equals(AccountBetType.ONLINEBOT)) {
                //TODO: DA IMPLEMENTARE IN FUTUTRO CON SELENIUM
            }
        } catch (Exception e) {
            logger.error("[BOT] sendBetting ERROR", e);
            betting.setUpdatedAt(ZonedDateTime.now());
            betting.setState(BettingState.ERROR);
            betting.setErrorMessage("Impossibile inviare scommessa: " + e.getMessage());
            bettingRepository.save(betting);
        }
    }

    private String _buildMessage(Betting betting) {
        String message = "";
        return message;
    }

    private Double _calculateBet(AccountBet accountBet, Double koef) {
        logger.info(
            "[BOT] _calculateBet START accountBetRoleType: {}, accountBetRoleAmount: {}, koef: {}",
            accountBet.getBettingRoleType(),
            accountBet.getBettingRoleAmount(),
            koef
        );
        Double betAmount = accountBet.getBettingRoleType().equals(BettingRoleType.FIXED_BET)
            ? accountBet.getBettingRoleAmount()
            : accountBet.getBettingRoleAmount() / koef;
        logger.info("[BOT] _calculateBet END RETURN betAmount: {}", accountBet.getBettingRoleType(), koef);
        return betAmount;
    }

    private EventSource processEvent(EventSource event) {
        logger.info("[BOT] processEvent START event: {}", event);
        try {
            if (event.getBetBurgerId() != null) {
                EventSource filter = new EventSource();
                filter.setBetBurgerId(event.getBetBurgerId());
                filter.setKoefLastModifiedAt(event.getKoefLastModifiedAt());
                List<EventSource> eventsDb = eventSourceRepository.findAll(Example.of(filter));
                if (eventsDb.isEmpty()) {
                    event = eventSourceRepository.save(event);
                }
            }

            if (event.getId() != null && event.getBookmakerEventId() != null && event.getBetBurgerId() != null) {
                EventSource filter = new EventSource();
                filter.setBookmakerEventId(event.getBookmakerEventId());
                filter.setBetBurgerId(event.getBetBurgerId());
                List<EventSource> eventsDb = eventSourceRepository.findAll(Example.of(filter), Sort.by("koefLastModifiedAt"));
                BetArrow arrow = calculateArrow(event, eventsDb);
                event.setArrow(arrow);
                event = eventSourceRepository.save(event);
                logger.info("[BOT] processEvent event: {}, ARROW: {}", event.getEventName(), arrow);
                return event;
            }
        } catch (Exception e) {
            logger.error("[BOT] processEvent ERROR", e);
        }

        return null;
    }

    /**
     *
     //GREEN UP  If over the last 10 minutes bookmaker odds increased
     //RED DOWN  If over the last 10 minutes bookmaker odds dropped
     //GRAY UP  If over the more then 10 minutes bookmaker odds increased
     //GRAY DOWN  If over the more then 10 minutes bookmaker odds dropped
     * */
    private BetArrow calculateArrow(EventSource event, List<EventSource> eventsDb) {
        BetArrow arrow = BetArrow.HIDDEN;
        Double koef = 0.0;
        ZonedDateTime lastDateInversion = null;
        if (!eventsDb.isEmpty()) {
            eventsDb.add(event);
            for (EventSource eventSource : eventsDb) {
                if (lastDateInversion == null) {
                    lastDateInversion = eventSource.getKoefLastModifiedAt();
                    koef = eventSource.getKoef();
                } else {
                    ZonedDateTime currentKoefLastModifiedAt = eventSource.getKoefLastModifiedAt();
                    Double currentKoef = eventSource.getKoef();
                    long differenceMillis =
                        currentKoefLastModifiedAt.toInstant().toEpochMilli() - lastDateInversion.toInstant().toEpochMilli();
                    if (currentKoef > koef) {
                        if (arrow != BetArrow.UP_GRAY && arrow != BetArrow.UP_GREEN) {
                            lastDateInversion = currentKoefLastModifiedAt;
                            differenceMillis =
                                currentKoefLastModifiedAt.toInstant().toEpochMilli() - lastDateInversion.toInstant().toEpochMilli();
                        }
                        koef = currentKoef;
                        if (differenceMillis > TEN_MINUTE_MILLIS) {
                            arrow = BetArrow.UP_GRAY;
                        } else {
                            arrow = BetArrow.UP_GREEN;
                        }
                    } else if (currentKoef < koef) {
                        if (arrow != BetArrow.DOWN_GREY && arrow != BetArrow.DOWN_RED) {
                            lastDateInversion = currentKoefLastModifiedAt;
                            differenceMillis =
                                currentKoefLastModifiedAt.toInstant().toEpochMilli() - lastDateInversion.toInstant().toEpochMilli();
                        }
                        koef = currentKoef;
                        if (differenceMillis > TEN_MINUTE_MILLIS) {
                            arrow = BetArrow.DOWN_GREY;
                        } else {
                            arrow = BetArrow.DOWN_RED;
                        }
                    }
                }
            }
        }

        return arrow;
    }

    private List<EventSource> parseResponse(JSONObject responseJson) {
        logger.info("[BOT] parseResponse START responseJson: {}", responseJson);
        List<EventSource> events = new ArrayList<>();
        JSONArray bets = responseJson.getJSONArray("bets");
        if (!bets.isEmpty()) {
            bets.forEach(betObj -> {
                try {
                    JSONObject bet = (JSONObject) betObj;
                    Bookmaker bookmaker = bookmakerRepository.findById(bet.getLong("bookmaker_id")).get();
                    logger.info(
                        "[BOT] parseResponse bookmaker: {} check status: {} - " +
                        (bookmaker.getState().equals(BookmakerState.INACTIVE) ? "SKIPPED" : "TO PROCESS"),
                        bookmaker.getName(),
                        bookmaker.getState()
                    );
                    if (bookmaker.getState().equals(BookmakerState.ACTIVE)) {
                        EventSource eventSource = new EventSource();
                        eventSource.setBetBurgerId(bet.getString("id"));
                        eventSource.setBookmakerEventId(bet.getLong("event_id"));
                        eventSource.setEventName(new String(bet.getString("event_name").getBytes(StandardCharsets.UTF_8), "UTF-8"));
                        eventSource.setBookmaker(bookmaker);
                        eventSource.setSport(sportRepository.findById(bet.getLong("sport_id")).get());
                        eventSource.setPeriod(periodRepository.findById(bet.getLong("period_id")).get());
                        eventSource.setLeague(new String(bet.getString("league").getBytes(StandardCharsets.UTF_8), "UTF-8"));
                        eventSource.setBetType(betTypeRepository.findById(bet.getLong("market_and_bet_type")).get());
                        eventSource.setBetTypeParam(bet.getDouble("market_and_bet_type_param"));
                        eventSource.setBookmakerEventId(bet.getLong("bookmaker_event_id"));
                        eventSource.setHome(new String(bet.getString("home").getBytes(StandardCharsets.UTF_8), "UTF-8"));
                        eventSource.setAway(new String(bet.getString("away").getBytes(StandardCharsets.UTF_8), "UTF-8"));
                        eventSource.setKoef(bet.getDouble("koef"));
                        eventSource.setKoefLastModifiedAt(
                            ZonedDateTime.ofInstant(Instant.ofEpochMilli(bet.getLong("koef_last_modified_at")), ZoneId.systemDefault())
                        );
                        eventSource.setScannedAt(
                            ZonedDateTime.ofInstant(Instant.ofEpochMilli(bet.getLong("scanned_at")), ZoneId.systemDefault())
                        );
                        eventSource.setUpdatedAt(
                            ZonedDateTime.ofInstant(Instant.ofEpochMilli(bet.getLong("updated_at")), ZoneId.systemDefault())
                        );
                        events.add(eventSource);
                    }
                } catch (Exception e) {
                    logger.error("[BOT] parseResponse ERROR", e);
                }
            });
        }

        logger.info("[BOT] parseResponse RETURN events size: {}", events.size());
        return events;
    }

    public static void main(String[] args) {
        BetBurgerBot bot = new BetBurgerBot();
        EventSource event = new EventSource();
        event.setKoef(1.45);
        event.setKoefLastModifiedAt(ZonedDateTime.now());
        List<EventSource> eventsDb = new ArrayList<>();

        EventSource e1 = new EventSource();
        e1.setKoef(1.5);
        e1.setKoefLastModifiedAt(
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(ZonedDateTime.now().toInstant().toEpochMilli() - (TEN_MINUTE_MILLIS + 10000)),
                ZoneId.systemDefault()
            )
        );
        eventsDb.add(e1);

        EventSource e2 = new EventSource();
        e2.setKoef(1.6);
        e2.setKoefLastModifiedAt(
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(ZonedDateTime.now().toInstant().toEpochMilli() - (TEN_MINUTE_MILLIS - 5000)),
                ZoneId.systemDefault()
            )
        );
        eventsDb.add(e2);

        EventSource e3 = new EventSource();
        e3.setKoef(1.5);
        e3.setKoefLastModifiedAt(
            ZonedDateTime.ofInstant(
                Instant.ofEpochMilli(ZonedDateTime.now().toInstant().toEpochMilli() - (TEN_MINUTE_MILLIS - 500)),
                ZoneId.systemDefault()
            )
        );
        eventsDb.add(e3);

        bot.calculateArrow(event, eventsDb);
    }
}
