package it.lovacino.betburger.bot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.lovacino.betburger.bot.IntegrationTest;
import it.lovacino.betburger.bot.domain.BetType;
import it.lovacino.betburger.bot.repository.BetTypeRepository;
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
 * Integration tests for the {@link BetTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BetTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/bet-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BetTypeRepository betTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBetTypeMockMvc;

    private BetType betType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BetType createEntity(EntityManager em) {
        BetType betType = new BetType().name(DEFAULT_NAME);
        return betType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BetType createUpdatedEntity(EntityManager em) {
        BetType betType = new BetType().name(UPDATED_NAME);
        return betType;
    }

    @BeforeEach
    public void initTest() {
        betType = createEntity(em);
    }

    @Test
    @Transactional
    void createBetType() throws Exception {
        int databaseSizeBeforeCreate = betTypeRepository.findAll().size();
        // Create the BetType
        restBetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(betType)))
            .andExpect(status().isCreated());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeCreate + 1);
        BetType testBetType = betTypeList.get(betTypeList.size() - 1);
        assertThat(testBetType.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createBetTypeWithExistingId() throws Exception {
        // Create the BetType with an existing ID
        betType.setId(1L);

        int databaseSizeBeforeCreate = betTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBetTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(betType)))
            .andExpect(status().isBadRequest());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBetTypes() throws Exception {
        // Initialize the database
        betTypeRepository.saveAndFlush(betType);

        // Get all the betTypeList
        restBetTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(betType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getBetType() throws Exception {
        // Initialize the database
        betTypeRepository.saveAndFlush(betType);

        // Get the betType
        restBetTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, betType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(betType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getNonExistingBetType() throws Exception {
        // Get the betType
        restBetTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBetType() throws Exception {
        // Initialize the database
        betTypeRepository.saveAndFlush(betType);

        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();

        // Update the betType
        BetType updatedBetType = betTypeRepository.findById(betType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBetType are not directly saved in db
        em.detach(updatedBetType);
        updatedBetType.name(UPDATED_NAME);

        restBetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBetType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBetType))
            )
            .andExpect(status().isOk());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
        BetType testBetType = betTypeList.get(betTypeList.size() - 1);
        assertThat(testBetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingBetType() throws Exception {
        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();
        betType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, betType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(betType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBetType() throws Exception {
        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();
        betType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBetTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(betType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBetType() throws Exception {
        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();
        betType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBetTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(betType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBetTypeWithPatch() throws Exception {
        // Initialize the database
        betTypeRepository.saveAndFlush(betType);

        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();

        // Update the betType using partial update
        BetType partialUpdatedBetType = new BetType();
        partialUpdatedBetType.setId(betType.getId());

        partialUpdatedBetType.name(UPDATED_NAME);

        restBetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBetType))
            )
            .andExpect(status().isOk());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
        BetType testBetType = betTypeList.get(betTypeList.size() - 1);
        assertThat(testBetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void fullUpdateBetTypeWithPatch() throws Exception {
        // Initialize the database
        betTypeRepository.saveAndFlush(betType);

        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();

        // Update the betType using partial update
        BetType partialUpdatedBetType = new BetType();
        partialUpdatedBetType.setId(betType.getId());

        partialUpdatedBetType.name(UPDATED_NAME);

        restBetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBetType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBetType))
            )
            .andExpect(status().isOk());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
        BetType testBetType = betTypeList.get(betTypeList.size() - 1);
        assertThat(testBetType.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingBetType() throws Exception {
        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();
        betType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, betType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(betType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBetType() throws Exception {
        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();
        betType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBetTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(betType))
            )
            .andExpect(status().isBadRequest());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBetType() throws Exception {
        int databaseSizeBeforeUpdate = betTypeRepository.findAll().size();
        betType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBetTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(betType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BetType in the database
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBetType() throws Exception {
        // Initialize the database
        betTypeRepository.saveAndFlush(betType);

        int databaseSizeBeforeDelete = betTypeRepository.findAll().size();

        // Delete the betType
        restBetTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, betType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BetType> betTypeList = betTypeRepository.findAll();
        assertThat(betTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
