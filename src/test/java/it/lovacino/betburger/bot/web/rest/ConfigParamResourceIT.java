package it.lovacino.betburger.bot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.lovacino.betburger.bot.IntegrationTest;
import it.lovacino.betburger.bot.domain.ConfigParam;
import it.lovacino.betburger.bot.repository.ConfigParamRepository;
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
 * Integration tests for the {@link ConfigParamResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigParamResourceIT {

    private static final String DEFAULT_PARAM_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PARAM_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_PARAM_VALUE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/config-params";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigParamRepository configParamRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigParamMockMvc;

    private ConfigParam configParam;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigParam createEntity(EntityManager em) {
        ConfigParam configParam = new ConfigParam().paramName(DEFAULT_PARAM_NAME).paramValue(DEFAULT_PARAM_VALUE);
        return configParam;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigParam createUpdatedEntity(EntityManager em) {
        ConfigParam configParam = new ConfigParam().paramName(UPDATED_PARAM_NAME).paramValue(UPDATED_PARAM_VALUE);
        return configParam;
    }

    @BeforeEach
    public void initTest() {
        configParam = createEntity(em);
    }

    @Test
    @Transactional
    void createConfigParam() throws Exception {
        int databaseSizeBeforeCreate = configParamRepository.findAll().size();
        // Create the ConfigParam
        restConfigParamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configParam)))
            .andExpect(status().isCreated());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigParam testConfigParam = configParamList.get(configParamList.size() - 1);
        assertThat(testConfigParam.getParamName()).isEqualTo(DEFAULT_PARAM_NAME);
        assertThat(testConfigParam.getParamValue()).isEqualTo(DEFAULT_PARAM_VALUE);
    }

    @Test
    @Transactional
    void createConfigParamWithExistingId() throws Exception {
        // Create the ConfigParam with an existing ID
        configParam.setId(1L);

        int databaseSizeBeforeCreate = configParamRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigParamMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configParam)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllConfigParams() throws Exception {
        // Initialize the database
        configParamRepository.saveAndFlush(configParam);

        // Get all the configParamList
        restConfigParamMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configParam.getId().intValue())))
            .andExpect(jsonPath("$.[*].paramName").value(hasItem(DEFAULT_PARAM_NAME)))
            .andExpect(jsonPath("$.[*].paramValue").value(hasItem(DEFAULT_PARAM_VALUE)));
    }

    @Test
    @Transactional
    void getConfigParam() throws Exception {
        // Initialize the database
        configParamRepository.saveAndFlush(configParam);

        // Get the configParam
        restConfigParamMockMvc
            .perform(get(ENTITY_API_URL_ID, configParam.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configParam.getId().intValue()))
            .andExpect(jsonPath("$.paramName").value(DEFAULT_PARAM_NAME))
            .andExpect(jsonPath("$.paramValue").value(DEFAULT_PARAM_VALUE));
    }

    @Test
    @Transactional
    void getNonExistingConfigParam() throws Exception {
        // Get the configParam
        restConfigParamMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConfigParam() throws Exception {
        // Initialize the database
        configParamRepository.saveAndFlush(configParam);

        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();

        // Update the configParam
        ConfigParam updatedConfigParam = configParamRepository.findById(configParam.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConfigParam are not directly saved in db
        em.detach(updatedConfigParam);
        updatedConfigParam.paramName(UPDATED_PARAM_NAME).paramValue(UPDATED_PARAM_VALUE);

        restConfigParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConfigParam.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConfigParam))
            )
            .andExpect(status().isOk());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
        ConfigParam testConfigParam = configParamList.get(configParamList.size() - 1);
        assertThat(testConfigParam.getParamName()).isEqualTo(UPDATED_PARAM_NAME);
        assertThat(testConfigParam.getParamValue()).isEqualTo(UPDATED_PARAM_VALUE);
    }

    @Test
    @Transactional
    void putNonExistingConfigParam() throws Exception {
        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();
        configParam.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configParam.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configParam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigParam() throws Exception {
        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();
        configParam.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigParamMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configParam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigParam() throws Exception {
        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();
        configParam.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigParamMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configParam)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigParamWithPatch() throws Exception {
        // Initialize the database
        configParamRepository.saveAndFlush(configParam);

        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();

        // Update the configParam using partial update
        ConfigParam partialUpdatedConfigParam = new ConfigParam();
        partialUpdatedConfigParam.setId(configParam.getId());

        partialUpdatedConfigParam.paramValue(UPDATED_PARAM_VALUE);

        restConfigParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigParam))
            )
            .andExpect(status().isOk());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
        ConfigParam testConfigParam = configParamList.get(configParamList.size() - 1);
        assertThat(testConfigParam.getParamName()).isEqualTo(DEFAULT_PARAM_NAME);
        assertThat(testConfigParam.getParamValue()).isEqualTo(UPDATED_PARAM_VALUE);
    }

    @Test
    @Transactional
    void fullUpdateConfigParamWithPatch() throws Exception {
        // Initialize the database
        configParamRepository.saveAndFlush(configParam);

        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();

        // Update the configParam using partial update
        ConfigParam partialUpdatedConfigParam = new ConfigParam();
        partialUpdatedConfigParam.setId(configParam.getId());

        partialUpdatedConfigParam.paramName(UPDATED_PARAM_NAME).paramValue(UPDATED_PARAM_VALUE);

        restConfigParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigParam))
            )
            .andExpect(status().isOk());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
        ConfigParam testConfigParam = configParamList.get(configParamList.size() - 1);
        assertThat(testConfigParam.getParamName()).isEqualTo(UPDATED_PARAM_NAME);
        assertThat(testConfigParam.getParamValue()).isEqualTo(UPDATED_PARAM_VALUE);
    }

    @Test
    @Transactional
    void patchNonExistingConfigParam() throws Exception {
        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();
        configParam.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configParam.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configParam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigParam() throws Exception {
        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();
        configParam.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigParamMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configParam))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigParam() throws Exception {
        int databaseSizeBeforeUpdate = configParamRepository.findAll().size();
        configParam.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigParamMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(configParam))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigParam in the database
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigParam() throws Exception {
        // Initialize the database
        configParamRepository.saveAndFlush(configParam);

        int databaseSizeBeforeDelete = configParamRepository.findAll().size();

        // Delete the configParam
        restConfigParamMockMvc
            .perform(delete(ENTITY_API_URL_ID, configParam.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigParam> configParamList = configParamRepository.findAll();
        assertThat(configParamList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
