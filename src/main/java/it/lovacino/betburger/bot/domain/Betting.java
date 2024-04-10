package it.lovacino.betburger.bot.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.lovacino.betburger.bot.domain.enumeration.BetResultType;
import it.lovacino.betburger.bot.domain.enumeration.BettingState;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Betting.
 */
@Entity
@Table(name = "betting")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Betting implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "event_source_id")
    private Long eventSourceId;

    @Column(name = "bet_type_param")
    private Double betTypeParam;

    @Column(name = "koef")
    private Double koef;

    @Column(name = "home")
    private String home;

    @Column(name = "away")
    private String away;

    @Column(name = "league")
    private String league;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "bet_burger_id")
    private String betBurgerId;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private BettingState state;

    @Column(name = "amount_bet")
    private Double amountBet;

    @Column(name = "amount_bet_win")
    private Double amountBetWin;

    @Enumerated(EnumType.STRING)
    @Column(name = "bet_result_type")
    private BetResultType betResultType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bookmaker" }, allowSetters = true)
    private AccountBet account;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY)
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    private BetType betType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Betting id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEventSourceId() {
        return this.eventSourceId;
    }

    public Betting eventSourceId(Long eventSourceId) {
        this.setEventSourceId(eventSourceId);
        return this;
    }

    public void setEventSourceId(Long eventSourceId) {
        this.eventSourceId = eventSourceId;
    }

    public Double getBetTypeParam() {
        return this.betTypeParam;
    }

    public Betting betTypeParam(Double betTypeParam) {
        this.setBetTypeParam(betTypeParam);
        return this;
    }

    public void setBetTypeParam(Double betTypeParam) {
        this.betTypeParam = betTypeParam;
    }

    public Double getKoef() {
        return this.koef;
    }

    public Betting koef(Double koef) {
        this.setKoef(koef);
        return this;
    }

    public void setKoef(Double koef) {
        this.koef = koef;
    }

    public String getHome() {
        return this.home;
    }

    public Betting home(String home) {
        this.setHome(home);
        return this;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return this.away;
    }

    public Betting away(String away) {
        this.setAway(away);
        return this;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getLeague() {
        return this.league;
    }

    public Betting league(String league) {
        this.setLeague(league);
        return this;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getEventName() {
        return this.eventName;
    }

    public Betting eventName(String eventName) {
        this.setEventName(eventName);
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public ZonedDateTime getStartedAt() {
        return this.startedAt;
    }

    public Betting startedAt(ZonedDateTime startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public Betting updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getBetBurgerId() {
        return this.betBurgerId;
    }

    public Betting betBurgerId(String betBurgerId) {
        this.setBetBurgerId(betBurgerId);
        return this;
    }

    public void setBetBurgerId(String betBurgerId) {
        this.betBurgerId = betBurgerId;
    }

    public BettingState getState() {
        return this.state;
    }

    public Betting state(BettingState state) {
        this.setState(state);
        return this;
    }

    public void setState(BettingState state) {
        this.state = state;
    }

    public Double getAmountBet() {
        return this.amountBet;
    }

    public Betting amountBet(Double amountBet) {
        this.setAmountBet(amountBet);
        return this;
    }

    public void setAmountBet(Double amountBet) {
        this.amountBet = amountBet;
    }

    public Double getAmountBetWin() {
        return this.amountBetWin;
    }

    public Betting amountBetWin(Double amountBetWin) {
        this.setAmountBetWin(amountBetWin);
        return this;
    }

    public void setAmountBetWin(Double amountBetWin) {
        this.amountBetWin = amountBetWin;
    }

    public BetResultType getBetResultType() {
        return this.betResultType;
    }

    public Betting betResultType(BetResultType betResultType) {
        this.setBetResultType(betResultType);
        return this;
    }

    public void setBetResultType(BetResultType betResultType) {
        this.betResultType = betResultType;
    }

    public AccountBet getAccount() {
        return this.account;
    }

    public void setAccount(AccountBet accountBet) {
        this.account = accountBet;
    }

    public Betting account(AccountBet accountBet) {
        this.setAccount(accountBet);
        return this;
    }

    public Sport getSport() {
        return this.sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public Betting sport(Sport sport) {
        this.setSport(sport);
        return this;
    }

    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public Betting period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public BetType getBetType() {
        return this.betType;
    }

    public void setBetType(BetType betType) {
        this.betType = betType;
    }

    public Betting betType(BetType betType) {
        this.setBetType(betType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Betting)) {
            return false;
        }
        return getId() != null && getId().equals(((Betting) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Betting{" +
            "id=" + getId() +
            ", eventSourceId=" + getEventSourceId() +
            ", betTypeParam=" + getBetTypeParam() +
            ", koef=" + getKoef() +
            ", home='" + getHome() + "'" +
            ", away='" + getAway() + "'" +
            ", league='" + getLeague() + "'" +
            ", eventName='" + getEventName() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", betBurgerId='" + getBetBurgerId() + "'" +
            ", state='" + getState() + "'" +
            ", amountBet=" + getAmountBet() +
            ", amountBetWin=" + getAmountBetWin() +
            ", betResultType='" + getBetResultType() + "'" +
            "}";
    }
}
