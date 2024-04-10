package it.lovacino.betburger.bot.web.rest;

import static it.lovacino.betburger.bot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.lovacino.betburger.bot.IntegrationTest;
import it.lovacino.betburger.bot.domain.Betting;
import it.lovacino.betburger.bot.domain.enumeration.BetResultType;
import it.lovacino.betburger.bot.domain.enumeration.BettingState;
import it.lovacino.betburger.bot.repository.BettingRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BettingResourceIT {

    private static final Long DEFAULT_EVENT_SOURCE_ID = 1L;
    private static final Long UPDATED_EVENT_SOURCE_ID = 2L;

    private static final Double DEFAULT_BET_TYPE_PARAM = 1D;
    private static final Double UPDATED_BET_TYPE_PARAM = 2D;

    private static final Double DEFAULT_KOEF = 1D;
    private static final Double UPDATED_KOEF = 2D;

    private static final String DEFAULT_HOME = "AAAAAAAAAA";
    private static final String UPDATED_HOME = "BBBBBBBBBB";

    private static final String DEFAULT_AWAY = "AAAAAAAAAA";
    private static final String UPDATED_AWAY = "BBBBBBBBBB";

    private static final String DEFAULT_LEAGUE = "AAAAAAAAAA";
    private static final String UPDATED_LEAGUE = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_STARTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_BET_BURGER_ID = "AAAAAAAAAA";
    private static final String UPDATED_BET_BURGER_ID = "BBBBBBBBBB";

    private static final BettingState DEFAULT_STATE = BettingState.CREATED;
    private static final BettingState UPDATED_STATE = BettingState.SENDING;

    private static final Double DEFAULT_AMOUNT_BET = 1D;
    private static final Double UPDATED_AMOUNT_BET = 2D;

    private static final Double DEFAULT_AMOUNT_BET_WIN = 1D;
    private static final Double UPDATED_AMOUNT_BET_WIN = 2D;

    private static final BetResultType DEFAULT_BET_RESULT_TYPE = BetResultType.WIN;
    private static final BetResultType UPDATED_BET_RESULT_TYPE = BetResultType.LOSE;

    private static final String ENTITY_API_URL = "/api/bettings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BettingRepository bettingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBettingMockMvc;

    private Betting betting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Betting createEntity(EntityManager em) {
        Betting betting = new Betting()
            .eventSourceId(DEFAULT_EVENT_SOURCE_ID)
            .betTypeParam(DEFAULT_BET_TYPE_PARAM)
            .koef(DEFAULT_KOEF)
            .home(DEFAULT_HOME)
            .away(DEFAULT_AWAY)
            .league(DEFAULT_LEAGUE)
            .eventName(DEFAULT_EVENT_NAME)
            .startedAt(DEFAULT_STARTED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .betBurgerId(DEFAULT_BET_BURGER_ID)
            .state(DEFAULT_STATE)
            .amountBet(DEFAULT_AMOUNT_BET)
            .amountBetWin(DEFAULT_AMOUNT_BET_WIN)
            .betResultType(DEFAULT_BET_RESULT_TYPE);
        return betting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Betting createUpdatedEntity(EntityManager em) {
        Betting betting = new Betting()
            .eventSourceId(UPDATED_EVENT_SOURCE_ID)
            .betTypeParam(UPDATED_BET_TYPE_PARAM)
            .koef(UPDATED_KOEF)
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .state(UPDATED_STATE)
            .amountBet(UPDATED_AMOUNT_BET)
            .amountBetWin(UPDATED_AMOUNT_BET_WIN)
            .betResultType(UPDATED_BET_RESULT_TYPE);
        return betting;
    }

    @BeforeEach
    public void initTest() {
        betting = createEntity(em);
    }

    @Test
    @Transactional
    void createBetting() throws Exception {
        int databaseSizeBeforeCreate = bettingRepository.findAll().size();
        // Create the Betting
        restBettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(betting)))
            .andExpect(status().isCreated());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeCreate + 1);
        Betting testBetting = bettingList.get(bettingList.size() - 1);
        assertThat(testBetting.getEventSourceId()).isEqualTo(DEFAULT_EVENT_SOURCE_ID);
        assertThat(testBetting.getBetTypeParam()).isEqualTo(DEFAULT_BET_TYPE_PARAM);
        assertThat(testBetting.getKoef()).isEqualTo(DEFAULT_KOEF);
        assertThat(testBetting.getHome()).isEqualTo(DEFAULT_HOME);
        assertThat(testBetting.getAway()).isEqualTo(DEFAULT_AWAY);
        assertThat(testBetting.getLeague()).isEqualTo(DEFAULT_LEAGUE);
        assertThat(testBetting.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testBetting.getStartedAt()).isEqualTo(DEFAULT_STARTED_AT);
        assertThat(testBetting.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testBetting.getBetBurgerId()).isEqualTo(DEFAULT_BET_BURGER_ID);
        assertThat(testBetting.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBetting.getAmountBet()).isEqualTo(DEFAULT_AMOUNT_BET);
        assertThat(testBetting.getAmountBetWin()).isEqualTo(DEFAULT_AMOUNT_BET_WIN);
        assertThat(testBetting.getBetResultType()).isEqualTo(DEFAULT_BET_RESULT_TYPE);
    }

    @Test
    @Transactional
    void createBettingWithExistingId() throws Exception {
        // Create the Betting with an existing ID
        betting.setId(1L);

        int databaseSizeBeforeCreate = bettingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(betting)))
            .andExpect(status().isBadRequest());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBettings() throws Exception {
        // Initialize the database
        bettingRepository.saveAndFlush(betting);

        // Get all the bettingList
        restBettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betting.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventSourceId").value(hasItem(DEFAULT_EVENT_SOURCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].betTypeParam").value(hasItem(DEFAULT_BET_TYPE_PARAM.doubleValue())))
            .andExpect(jsonPath("$.[*].koef").value(hasItem(DEFAULT_KOEF.doubleValue())))
            .andExpect(jsonPath("$.[*].home").value(hasItem(DEFAULT_HOME)))
            .andExpect(jsonPath("$.[*].away").value(hasItem(DEFAULT_AWAY)))
            .andExpect(jsonPath("$.[*].league").value(hasItem(DEFAULT_LEAGUE)))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(sameInstant(DEFAULT_STARTED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].betBurgerId").value(hasItem(DEFAULT_BET_BURGER_ID)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].amountBet").value(hasItem(DEFAULT_AMOUNT_BET.doubleValue())))
            .andExpect(jsonPath("$.[*].amountBetWin").value(hasItem(DEFAULT_AMOUNT_BET_WIN.doubleValue())))
            .andExpect(jsonPath("$.[*].betResultType").value(hasItem(DEFAULT_BET_RESULT_TYPE.toString())));
    }

    @Test
    @Transactional
    void getBetting() throws Exception {
        // Initialize the database
        bettingRepository.saveAndFlush(betting);

        // Get the betting
        restBettingMockMvc
            .perform(get(ENTITY_API_URL_ID, betting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(betting.getId().intValue()))
            .andExpect(jsonPath("$.eventSourceId").value(DEFAULT_EVENT_SOURCE_ID.intValue()))
            .andExpect(jsonPath("$.betTypeParam").value(DEFAULT_BET_TYPE_PARAM.doubleValue()))
            .andExpect(jsonPath("$.koef").value(DEFAULT_KOEF.doubleValue()))
            .andExpect(jsonPath("$.home").value(DEFAULT_HOME))
            .andExpect(jsonPath("$.away").value(DEFAULT_AWAY))
            .andExpect(jsonPath("$.league").value(DEFAULT_LEAGUE))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.startedAt").value(sameInstant(DEFAULT_STARTED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.betBurgerId").value(DEFAULT_BET_BURGER_ID))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.amountBet").value(DEFAULT_AMOUNT_BET.doubleValue()))
            .andExpect(jsonPath("$.amountBetWin").value(DEFAULT_AMOUNT_BET_WIN.doubleValue()))
            .andExpect(jsonPath("$.betResultType").value(DEFAULT_BET_RESULT_TYPE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBetting() throws Exception {
        // Get the betting
        restBettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBetting() throws Exception {
        // Initialize the database
        bettingRepository.saveAndFlush(betting);

        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();

        // Update the betting
        Betting updatedBetting = bettingRepository.findById(betting.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBetting are not directly saved in db
        em.detach(updatedBetting);
        updatedBetting
            .eventSourceId(UPDATED_EVENT_SOURCE_ID)
            .betTypeParam(UPDATED_BET_TYPE_PARAM)
            .koef(UPDATED_KOEF)
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .state(UPDATED_STATE)
            .amountBet(UPDATED_AMOUNT_BET)
            .amountBetWin(UPDATED_AMOUNT_BET_WIN)
            .betResultType(UPDATED_BET_RESULT_TYPE);

        restBettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBetting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBetting))
            )
            .andExpect(status().isOk());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
        Betting testBetting = bettingList.get(bettingList.size() - 1);
        assertThat(testBetting.getEventSourceId()).isEqualTo(UPDATED_EVENT_SOURCE_ID);
        assertThat(testBetting.getBetTypeParam()).isEqualTo(UPDATED_BET_TYPE_PARAM);
        assertThat(testBetting.getKoef()).isEqualTo(UPDATED_KOEF);
        assertThat(testBetting.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testBetting.getAway()).isEqualTo(UPDATED_AWAY);
        assertThat(testBetting.getLeague()).isEqualTo(UPDATED_LEAGUE);
        assertThat(testBetting.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testBetting.getStartedAt()).isEqualTo(UPDATED_STARTED_AT);
        assertThat(testBetting.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testBetting.getBetBurgerId()).isEqualTo(UPDATED_BET_BURGER_ID);
        assertThat(testBetting.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBetting.getAmountBet()).isEqualTo(UPDATED_AMOUNT_BET);
        assertThat(testBetting.getAmountBetWin()).isEqualTo(UPDATED_AMOUNT_BET_WIN);
        assertThat(testBetting.getBetResultType()).isEqualTo(UPDATED_BET_RESULT_TYPE);
    }

    @Test
    @Transactional
    void putNonExistingBetting() throws Exception {
        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();
        betting.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, betting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(betting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBetting() throws Exception {
        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();
        betting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(betting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBetting() throws Exception {
        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();
        betting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBettingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(betting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBettingWithPatch() throws Exception {
        // Initialize the database
        bettingRepository.saveAndFlush(betting);

        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();

        // Update the betting using partial update
        Betting partialUpdatedBetting = new Betting();
        partialUpdatedBetting.setId(betting.getId());

        partialUpdatedBetting
            .eventSourceId(UPDATED_EVENT_SOURCE_ID)
            .koef(UPDATED_KOEF)
            .home(UPDATED_HOME)
            .startedAt(UPDATED_STARTED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .amountBet(UPDATED_AMOUNT_BET)
            .amountBetWin(UPDATED_AMOUNT_BET_WIN)
            .betResultType(UPDATED_BET_RESULT_TYPE);

        restBettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBetting))
            )
            .andExpect(status().isOk());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
        Betting testBetting = bettingList.get(bettingList.size() - 1);
        assertThat(testBetting.getEventSourceId()).isEqualTo(UPDATED_EVENT_SOURCE_ID);
        assertThat(testBetting.getBetTypeParam()).isEqualTo(DEFAULT_BET_TYPE_PARAM);
        assertThat(testBetting.getKoef()).isEqualTo(UPDATED_KOEF);
        assertThat(testBetting.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testBetting.getAway()).isEqualTo(DEFAULT_AWAY);
        assertThat(testBetting.getLeague()).isEqualTo(DEFAULT_LEAGUE);
        assertThat(testBetting.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testBetting.getStartedAt()).isEqualTo(UPDATED_STARTED_AT);
        assertThat(testBetting.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testBetting.getBetBurgerId()).isEqualTo(UPDATED_BET_BURGER_ID);
        assertThat(testBetting.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testBetting.getAmountBet()).isEqualTo(UPDATED_AMOUNT_BET);
        assertThat(testBetting.getAmountBetWin()).isEqualTo(UPDATED_AMOUNT_BET_WIN);
        assertThat(testBetting.getBetResultType()).isEqualTo(UPDATED_BET_RESULT_TYPE);
    }

    @Test
    @Transactional
    void fullUpdateBettingWithPatch() throws Exception {
        // Initialize the database
        bettingRepository.saveAndFlush(betting);

        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();

        // Update the betting using partial update
        Betting partialUpdatedBetting = new Betting();
        partialUpdatedBetting.setId(betting.getId());

        partialUpdatedBetting
            .eventSourceId(UPDATED_EVENT_SOURCE_ID)
            .betTypeParam(UPDATED_BET_TYPE_PARAM)
            .koef(UPDATED_KOEF)
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .state(UPDATED_STATE)
            .amountBet(UPDATED_AMOUNT_BET)
            .amountBetWin(UPDATED_AMOUNT_BET_WIN)
            .betResultType(UPDATED_BET_RESULT_TYPE);

        restBettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBetting))
            )
            .andExpect(status().isOk());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
        Betting testBetting = bettingList.get(bettingList.size() - 1);
        assertThat(testBetting.getEventSourceId()).isEqualTo(UPDATED_EVENT_SOURCE_ID);
        assertThat(testBetting.getBetTypeParam()).isEqualTo(UPDATED_BET_TYPE_PARAM);
        assertThat(testBetting.getKoef()).isEqualTo(UPDATED_KOEF);
        assertThat(testBetting.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testBetting.getAway()).isEqualTo(UPDATED_AWAY);
        assertThat(testBetting.getLeague()).isEqualTo(UPDATED_LEAGUE);
        assertThat(testBetting.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testBetting.getStartedAt()).isEqualTo(UPDATED_STARTED_AT);
        assertThat(testBetting.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testBetting.getBetBurgerId()).isEqualTo(UPDATED_BET_BURGER_ID);
        assertThat(testBetting.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testBetting.getAmountBet()).isEqualTo(UPDATED_AMOUNT_BET);
        assertThat(testBetting.getAmountBetWin()).isEqualTo(UPDATED_AMOUNT_BET_WIN);
        assertThat(testBetting.getBetResultType()).isEqualTo(UPDATED_BET_RESULT_TYPE);
    }

    @Test
    @Transactional
    void patchNonExistingBetting() throws Exception {
        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();
        betting.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, betting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(betting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBetting() throws Exception {
        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();
        betting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(betting))
            )
            .andExpect(status().isBadRequest());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBetting() throws Exception {
        int databaseSizeBeforeUpdate = bettingRepository.findAll().size();
        betting.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBettingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(betting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Betting in the database
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBetting() throws Exception {
        // Initialize the database
        bettingRepository.saveAndFlush(betting);

        int databaseSizeBeforeDelete = bettingRepository.findAll().size();

        // Delete the betting
        restBettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, betting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Betting> bettingList = bettingRepository.findAll();
        assertThat(bettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
