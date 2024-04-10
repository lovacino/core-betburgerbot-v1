package it.lovacino.betburger.bot.web.rest;

import static it.lovacino.betburger.bot.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.lovacino.betburger.bot.IntegrationTest;
import it.lovacino.betburger.bot.domain.EventSource;
import it.lovacino.betburger.bot.repository.EventSourceRepository;
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
 * Integration tests for the {@link EventSourceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EventSourceResourceIT {

    private static final String DEFAULT_HOME = "AAAAAAAAAA";
    private static final String UPDATED_HOME = "BBBBBBBBBB";

    private static final String DEFAULT_AWAY = "AAAAAAAAAA";
    private static final String UPDATED_AWAY = "BBBBBBBBBB";

    private static final String DEFAULT_LEAGUE = "AAAAAAAAAA";
    private static final String UPDATED_LEAGUE = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_BOOKMAKER_EVENT_ID = 1L;
    private static final Long UPDATED_BOOKMAKER_EVENT_ID = 2L;

    private static final Double DEFAULT_BET_TYPE_PARAM = 1D;
    private static final Double UPDATED_BET_TYPE_PARAM = 2D;

    private static final ZonedDateTime DEFAULT_KOEF_LAST_MODIFIED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_KOEF_LAST_MODIFIED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_SCANNED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SCANNED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_STARTED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_STARTED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_UPDATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_UPDATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_BET_BURGER_ID = "AAAAAAAAAA";
    private static final String UPDATED_BET_BURGER_ID = "BBBBBBBBBB";

    private static final Double DEFAULT_KOEF = 1D;
    private static final Double UPDATED_KOEF = 2D;

    private static final String ENTITY_API_URL = "/api/event-sources";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EventSourceRepository eventSourceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventSourceMockMvc;

    private EventSource eventSource;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventSource createEntity(EntityManager em) {
        EventSource eventSource = new EventSource()
            .home(DEFAULT_HOME)
            .away(DEFAULT_AWAY)
            .league(DEFAULT_LEAGUE)
            .eventName(DEFAULT_EVENT_NAME)
            .bookmakerEventId(DEFAULT_BOOKMAKER_EVENT_ID)
            .betTypeParam(DEFAULT_BET_TYPE_PARAM)
            .koefLastModifiedAt(DEFAULT_KOEF_LAST_MODIFIED_AT)
            .scannedAt(DEFAULT_SCANNED_AT)
            .startedAt(DEFAULT_STARTED_AT)
            .updatedAt(DEFAULT_UPDATED_AT)
            .betBurgerId(DEFAULT_BET_BURGER_ID)
            .koef(DEFAULT_KOEF);
        return eventSource;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EventSource createUpdatedEntity(EntityManager em) {
        EventSource eventSource = new EventSource()
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .bookmakerEventId(UPDATED_BOOKMAKER_EVENT_ID)
            .betTypeParam(UPDATED_BET_TYPE_PARAM)
            .koefLastModifiedAt(UPDATED_KOEF_LAST_MODIFIED_AT)
            .scannedAt(UPDATED_SCANNED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .koef(UPDATED_KOEF);
        return eventSource;
    }

    @BeforeEach
    public void initTest() {
        eventSource = createEntity(em);
    }

    @Test
    @Transactional
    void createEventSource() throws Exception {
        int databaseSizeBeforeCreate = eventSourceRepository.findAll().size();
        // Create the EventSource
        restEventSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventSource)))
            .andExpect(status().isCreated());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeCreate + 1);
        EventSource testEventSource = eventSourceList.get(eventSourceList.size() - 1);
        assertThat(testEventSource.getHome()).isEqualTo(DEFAULT_HOME);
        assertThat(testEventSource.getAway()).isEqualTo(DEFAULT_AWAY);
        assertThat(testEventSource.getLeague()).isEqualTo(DEFAULT_LEAGUE);
        assertThat(testEventSource.getEventName()).isEqualTo(DEFAULT_EVENT_NAME);
        assertThat(testEventSource.getBookmakerEventId()).isEqualTo(DEFAULT_BOOKMAKER_EVENT_ID);
        assertThat(testEventSource.getBetTypeParam()).isEqualTo(DEFAULT_BET_TYPE_PARAM);
        assertThat(testEventSource.getKoefLastModifiedAt()).isEqualTo(DEFAULT_KOEF_LAST_MODIFIED_AT);
        assertThat(testEventSource.getScannedAt()).isEqualTo(DEFAULT_SCANNED_AT);
        assertThat(testEventSource.getStartedAt()).isEqualTo(DEFAULT_STARTED_AT);
        assertThat(testEventSource.getUpdatedAt()).isEqualTo(DEFAULT_UPDATED_AT);
        assertThat(testEventSource.getBetBurgerId()).isEqualTo(DEFAULT_BET_BURGER_ID);
        assertThat(testEventSource.getKoef()).isEqualTo(DEFAULT_KOEF);
    }

    @Test
    @Transactional
    void createEventSourceWithExistingId() throws Exception {
        // Create the EventSource with an existing ID
        eventSource.setId(1L);

        int databaseSizeBeforeCreate = eventSourceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventSourceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventSource)))
            .andExpect(status().isBadRequest());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEventSources() throws Exception {
        // Initialize the database
        eventSourceRepository.saveAndFlush(eventSource);

        // Get all the eventSourceList
        restEventSourceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eventSource.getId().intValue())))
            .andExpect(jsonPath("$.[*].home").value(hasItem(DEFAULT_HOME)))
            .andExpect(jsonPath("$.[*].away").value(hasItem(DEFAULT_AWAY)))
            .andExpect(jsonPath("$.[*].league").value(hasItem(DEFAULT_LEAGUE)))
            .andExpect(jsonPath("$.[*].eventName").value(hasItem(DEFAULT_EVENT_NAME)))
            .andExpect(jsonPath("$.[*].bookmakerEventId").value(hasItem(DEFAULT_BOOKMAKER_EVENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].betTypeParam").value(hasItem(DEFAULT_BET_TYPE_PARAM.doubleValue())))
            .andExpect(jsonPath("$.[*].koefLastModifiedAt").value(hasItem(sameInstant(DEFAULT_KOEF_LAST_MODIFIED_AT))))
            .andExpect(jsonPath("$.[*].scannedAt").value(hasItem(sameInstant(DEFAULT_SCANNED_AT))))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(sameInstant(DEFAULT_STARTED_AT))))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(sameInstant(DEFAULT_UPDATED_AT))))
            .andExpect(jsonPath("$.[*].betBurgerId").value(hasItem(DEFAULT_BET_BURGER_ID)))
            .andExpect(jsonPath("$.[*].koef").value(hasItem(DEFAULT_KOEF.doubleValue())));
    }

    @Test
    @Transactional
    void getEventSource() throws Exception {
        // Initialize the database
        eventSourceRepository.saveAndFlush(eventSource);

        // Get the eventSource
        restEventSourceMockMvc
            .perform(get(ENTITY_API_URL_ID, eventSource.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eventSource.getId().intValue()))
            .andExpect(jsonPath("$.home").value(DEFAULT_HOME))
            .andExpect(jsonPath("$.away").value(DEFAULT_AWAY))
            .andExpect(jsonPath("$.league").value(DEFAULT_LEAGUE))
            .andExpect(jsonPath("$.eventName").value(DEFAULT_EVENT_NAME))
            .andExpect(jsonPath("$.bookmakerEventId").value(DEFAULT_BOOKMAKER_EVENT_ID.intValue()))
            .andExpect(jsonPath("$.betTypeParam").value(DEFAULT_BET_TYPE_PARAM.doubleValue()))
            .andExpect(jsonPath("$.koefLastModifiedAt").value(sameInstant(DEFAULT_KOEF_LAST_MODIFIED_AT)))
            .andExpect(jsonPath("$.scannedAt").value(sameInstant(DEFAULT_SCANNED_AT)))
            .andExpect(jsonPath("$.startedAt").value(sameInstant(DEFAULT_STARTED_AT)))
            .andExpect(jsonPath("$.updatedAt").value(sameInstant(DEFAULT_UPDATED_AT)))
            .andExpect(jsonPath("$.betBurgerId").value(DEFAULT_BET_BURGER_ID))
            .andExpect(jsonPath("$.koef").value(DEFAULT_KOEF.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingEventSource() throws Exception {
        // Get the eventSource
        restEventSourceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEventSource() throws Exception {
        // Initialize the database
        eventSourceRepository.saveAndFlush(eventSource);

        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();

        // Update the eventSource
        EventSource updatedEventSource = eventSourceRepository.findById(eventSource.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEventSource are not directly saved in db
        em.detach(updatedEventSource);
        updatedEventSource
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .bookmakerEventId(UPDATED_BOOKMAKER_EVENT_ID)
            .betTypeParam(UPDATED_BET_TYPE_PARAM)
            .koefLastModifiedAt(UPDATED_KOEF_LAST_MODIFIED_AT)
            .scannedAt(UPDATED_SCANNED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .koef(UPDATED_KOEF);

        restEventSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedEventSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedEventSource))
            )
            .andExpect(status().isOk());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
        EventSource testEventSource = eventSourceList.get(eventSourceList.size() - 1);
        assertThat(testEventSource.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testEventSource.getAway()).isEqualTo(UPDATED_AWAY);
        assertThat(testEventSource.getLeague()).isEqualTo(UPDATED_LEAGUE);
        assertThat(testEventSource.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEventSource.getBookmakerEventId()).isEqualTo(UPDATED_BOOKMAKER_EVENT_ID);
        assertThat(testEventSource.getBetTypeParam()).isEqualTo(UPDATED_BET_TYPE_PARAM);
        assertThat(testEventSource.getKoefLastModifiedAt()).isEqualTo(UPDATED_KOEF_LAST_MODIFIED_AT);
        assertThat(testEventSource.getScannedAt()).isEqualTo(UPDATED_SCANNED_AT);
        assertThat(testEventSource.getStartedAt()).isEqualTo(UPDATED_STARTED_AT);
        assertThat(testEventSource.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEventSource.getBetBurgerId()).isEqualTo(UPDATED_BET_BURGER_ID);
        assertThat(testEventSource.getKoef()).isEqualTo(UPDATED_KOEF);
    }

    @Test
    @Transactional
    void putNonExistingEventSource() throws Exception {
        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();
        eventSource.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eventSource.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEventSource() throws Exception {
        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();
        eventSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventSourceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eventSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEventSource() throws Exception {
        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();
        eventSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventSourceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(eventSource)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEventSourceWithPatch() throws Exception {
        // Initialize the database
        eventSourceRepository.saveAndFlush(eventSource);

        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();

        // Update the eventSource using partial update
        EventSource partialUpdatedEventSource = new EventSource();
        partialUpdatedEventSource.setId(eventSource.getId());

        partialUpdatedEventSource
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .scannedAt(UPDATED_SCANNED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .koef(UPDATED_KOEF);

        restEventSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventSource))
            )
            .andExpect(status().isOk());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
        EventSource testEventSource = eventSourceList.get(eventSourceList.size() - 1);
        assertThat(testEventSource.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testEventSource.getAway()).isEqualTo(UPDATED_AWAY);
        assertThat(testEventSource.getLeague()).isEqualTo(UPDATED_LEAGUE);
        assertThat(testEventSource.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEventSource.getBookmakerEventId()).isEqualTo(DEFAULT_BOOKMAKER_EVENT_ID);
        assertThat(testEventSource.getBetTypeParam()).isEqualTo(DEFAULT_BET_TYPE_PARAM);
        assertThat(testEventSource.getKoefLastModifiedAt()).isEqualTo(DEFAULT_KOEF_LAST_MODIFIED_AT);
        assertThat(testEventSource.getScannedAt()).isEqualTo(UPDATED_SCANNED_AT);
        assertThat(testEventSource.getStartedAt()).isEqualTo(UPDATED_STARTED_AT);
        assertThat(testEventSource.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEventSource.getBetBurgerId()).isEqualTo(DEFAULT_BET_BURGER_ID);
        assertThat(testEventSource.getKoef()).isEqualTo(UPDATED_KOEF);
    }

    @Test
    @Transactional
    void fullUpdateEventSourceWithPatch() throws Exception {
        // Initialize the database
        eventSourceRepository.saveAndFlush(eventSource);

        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();

        // Update the eventSource using partial update
        EventSource partialUpdatedEventSource = new EventSource();
        partialUpdatedEventSource.setId(eventSource.getId());

        partialUpdatedEventSource
            .home(UPDATED_HOME)
            .away(UPDATED_AWAY)
            .league(UPDATED_LEAGUE)
            .eventName(UPDATED_EVENT_NAME)
            .bookmakerEventId(UPDATED_BOOKMAKER_EVENT_ID)
            .betTypeParam(UPDATED_BET_TYPE_PARAM)
            .koefLastModifiedAt(UPDATED_KOEF_LAST_MODIFIED_AT)
            .scannedAt(UPDATED_SCANNED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .updatedAt(UPDATED_UPDATED_AT)
            .betBurgerId(UPDATED_BET_BURGER_ID)
            .koef(UPDATED_KOEF);

        restEventSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEventSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEventSource))
            )
            .andExpect(status().isOk());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
        EventSource testEventSource = eventSourceList.get(eventSourceList.size() - 1);
        assertThat(testEventSource.getHome()).isEqualTo(UPDATED_HOME);
        assertThat(testEventSource.getAway()).isEqualTo(UPDATED_AWAY);
        assertThat(testEventSource.getLeague()).isEqualTo(UPDATED_LEAGUE);
        assertThat(testEventSource.getEventName()).isEqualTo(UPDATED_EVENT_NAME);
        assertThat(testEventSource.getBookmakerEventId()).isEqualTo(UPDATED_BOOKMAKER_EVENT_ID);
        assertThat(testEventSource.getBetTypeParam()).isEqualTo(UPDATED_BET_TYPE_PARAM);
        assertThat(testEventSource.getKoefLastModifiedAt()).isEqualTo(UPDATED_KOEF_LAST_MODIFIED_AT);
        assertThat(testEventSource.getScannedAt()).isEqualTo(UPDATED_SCANNED_AT);
        assertThat(testEventSource.getStartedAt()).isEqualTo(UPDATED_STARTED_AT);
        assertThat(testEventSource.getUpdatedAt()).isEqualTo(UPDATED_UPDATED_AT);
        assertThat(testEventSource.getBetBurgerId()).isEqualTo(UPDATED_BET_BURGER_ID);
        assertThat(testEventSource.getKoef()).isEqualTo(UPDATED_KOEF);
    }

    @Test
    @Transactional
    void patchNonExistingEventSource() throws Exception {
        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();
        eventSource.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eventSource.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEventSource() throws Exception {
        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();
        eventSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventSourceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eventSource))
            )
            .andExpect(status().isBadRequest());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEventSource() throws Exception {
        int databaseSizeBeforeUpdate = eventSourceRepository.findAll().size();
        eventSource.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEventSourceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(eventSource))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EventSource in the database
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEventSource() throws Exception {
        // Initialize the database
        eventSourceRepository.saveAndFlush(eventSource);

        int databaseSizeBeforeDelete = eventSourceRepository.findAll().size();

        // Delete the eventSource
        restEventSourceMockMvc
            .perform(delete(ENTITY_API_URL_ID, eventSource.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EventSource> eventSourceList = eventSourceRepository.findAll();
        assertThat(eventSourceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
