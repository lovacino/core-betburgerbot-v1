package it.lovacino.betburger.bot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.lovacino.betburger.bot.IntegrationTest;
import it.lovacino.betburger.bot.domain.Bookmaker;
import it.lovacino.betburger.bot.domain.enumeration.BookmakerState;
import it.lovacino.betburger.bot.repository.BookmakerRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link BookmakerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookmakerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final BookmakerState DEFAULT_STATE = BookmakerState.ACTIVE;
    private static final BookmakerState UPDATED_STATE = BookmakerState.INACTIVE;

    private static final String ENTITY_API_URL = "/api/bookmakers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookmakerRepository bookmakerRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookmakerMockMvc;

    private Bookmaker bookmaker;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bookmaker createEntity(EntityManager em) {
        Bookmaker bookmaker = new Bookmaker().name(DEFAULT_NAME).state(DEFAULT_STATE);
        return bookmaker;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bookmaker createUpdatedEntity(EntityManager em) {
        Bookmaker bookmaker = new Bookmaker().name(UPDATED_NAME).state(UPDATED_STATE);
        return bookmaker;
    }

    @BeforeEach
    public void initTest() {
        bookmaker = createEntity(em);
    }

    @Test
    @Transactional
    void createBookmaker() throws Exception {
        int databaseSizeBeforeCreate = bookmakerRepository.findAll().size();
        // Create the Bookmaker
        restBookmakerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookmaker)))
            .andExpect(status().isCreated());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeCreate + 1);
        Bookmaker testBookmaker = bookmakerList.get(bookmakerList.size() - 1);
        assertThat(testBookmaker.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBookmaker.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void createBookmakerWithExistingId() throws Exception {
        // Create the Bookmaker with an existing ID
        bookmaker.setId(1L);

        int databaseSizeBeforeCreate = bookmakerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookmakerMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookmaker)))
            .andExpect(status().isBadRequest());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookmakers() throws Exception {
        // Initialize the database
        bookmakerRepository.saveAndFlush(bookmaker);

        // Get all the bookmakerList
        restBookmakerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookmaker.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())));
    }

    @Test
    @Transactional
    void getBookmaker() throws Exception {
        // Initialize the database
        bookmakerRepository.saveAndFlush(bookmaker);

        // Get the bookmaker
        restBookmakerMockMvc
            .perform(get(ENTITY_API_URL_ID, bookmaker.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookmaker.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookmaker() throws Exception {
        // Get the bookmaker
        restBookmakerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookmaker() throws Exception {
        // Initialize the database
        bookmakerRepository.saveAndFlush(bookmaker);

        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();

        // Update the bookmaker
        Bookmaker updatedBookmaker = bookmakerRepository.findById(bookmaker.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookmaker are not directly saved in db
        em.detach(updatedBookmaker);
        updatedBookmaker.name(UPDATED_NAME).state(UPDATED_STATE);

        restBookmakerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookmaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookmaker))
            )
            .andExpect(status().isOk());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
        Bookmaker testBookmaker = bookmakerList.get(bookmakerList.size() - 1);
        assertThat(testBookmaker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBookmaker.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void putNonExistingBookmaker() throws Exception {
        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();
        bookmaker.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookmakerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookmaker.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookmaker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookmaker() throws Exception {
        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();
        bookmaker.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmakerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookmaker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookmaker() throws Exception {
        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();
        bookmaker.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmakerMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookmaker)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookmakerWithPatch() throws Exception {
        // Initialize the database
        bookmakerRepository.saveAndFlush(bookmaker);

        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();

        // Update the bookmaker using partial update
        Bookmaker partialUpdatedBookmaker = new Bookmaker();
        partialUpdatedBookmaker.setId(bookmaker.getId());

        partialUpdatedBookmaker.name(UPDATED_NAME);

        restBookmakerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookmaker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookmaker))
            )
            .andExpect(status().isOk());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
        Bookmaker testBookmaker = bookmakerList.get(bookmakerList.size() - 1);
        assertThat(testBookmaker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBookmaker.getState()).isEqualTo(DEFAULT_STATE);
    }

    @Test
    @Transactional
    void fullUpdateBookmakerWithPatch() throws Exception {
        // Initialize the database
        bookmakerRepository.saveAndFlush(bookmaker);

        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();

        // Update the bookmaker using partial update
        Bookmaker partialUpdatedBookmaker = new Bookmaker();
        partialUpdatedBookmaker.setId(bookmaker.getId());

        partialUpdatedBookmaker.name(UPDATED_NAME).state(UPDATED_STATE);

        restBookmakerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookmaker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookmaker))
            )
            .andExpect(status().isOk());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
        Bookmaker testBookmaker = bookmakerList.get(bookmakerList.size() - 1);
        assertThat(testBookmaker.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBookmaker.getState()).isEqualTo(UPDATED_STATE);
    }

    @Test
    @Transactional
    void patchNonExistingBookmaker() throws Exception {
        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();
        bookmaker.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookmakerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookmaker.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookmaker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookmaker() throws Exception {
        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();
        bookmaker.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmakerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookmaker))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookmaker() throws Exception {
        int databaseSizeBeforeUpdate = bookmakerRepository.findAll().size();
        bookmaker.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmakerMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(bookmaker))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bookmaker in the database
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookmaker() throws Exception {
        // Initialize the database
        bookmakerRepository.saveAndFlush(bookmaker);

        int databaseSizeBeforeDelete = bookmakerRepository.findAll().size();

        // Delete the bookmaker
        restBookmakerMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookmaker.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bookmaker> bookmakerList = bookmakerRepository.findAll();
        assertThat(bookmakerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
