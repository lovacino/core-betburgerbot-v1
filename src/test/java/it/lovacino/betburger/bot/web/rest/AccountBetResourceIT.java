package it.lovacino.betburger.bot.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import it.lovacino.betburger.bot.IntegrationTest;
import it.lovacino.betburger.bot.domain.AccountBet;
import it.lovacino.betburger.bot.domain.enumeration.AccountBetState;
import it.lovacino.betburger.bot.domain.enumeration.AccountBetType;
import it.lovacino.betburger.bot.domain.enumeration.BettingRoleType;
import it.lovacino.betburger.bot.repository.AccountBetRepository;
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
 * Integration tests for the {@link AccountBetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccountBetResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final AccountBetState DEFAULT_STATE = AccountBetState.ACTIVE;
    private static final AccountBetState UPDATED_STATE = AccountBetState.INACTIVE;

    private static final AccountBetType DEFAULT_TYPE = AccountBetType.WHATSAPP;
    private static final AccountBetType UPDATED_TYPE = AccountBetType.ONLINEBOT;

    private static final BettingRoleType DEFAULT_BETTING_ROLE_TYPE = BettingRoleType.FIXED_BET;
    private static final BettingRoleType UPDATED_BETTING_ROLE_TYPE = BettingRoleType.FIXED_WIN;

    private static final Double DEFAULT_BETTING_ROLE_AMOUNT = 1D;
    private static final Double UPDATED_BETTING_ROLE_AMOUNT = 2D;

    private static final Integer DEFAULT_HOUR_ACTIVE_ACTIVE = 1;
    private static final Integer UPDATED_HOUR_ACTIVE_ACTIVE = 2;

    private static final Integer DEFAULT_HOUR_ACTIVE_END = 1;
    private static final Integer UPDATED_HOUR_ACTIVE_END = 2;

    private static final Boolean DEFAULT_FLG_ACTIVE_LUN = false;
    private static final Boolean UPDATED_FLG_ACTIVE_LUN = true;

    private static final Boolean DEFAULT_FLG_ACTIVE_MAR = false;
    private static final Boolean UPDATED_FLG_ACTIVE_MAR = true;

    private static final Boolean DEFAULT_FLG_ACTIVE_MER = false;
    private static final Boolean UPDATED_FLG_ACTIVE_MER = true;

    private static final Boolean DEFAULT_FLG_ACTIVE_GIO = false;
    private static final Boolean UPDATED_FLG_ACTIVE_GIO = true;

    private static final Boolean DEFAULT_FLG_ACTIVE_VEN = false;
    private static final Boolean UPDATED_FLG_ACTIVE_VEN = true;

    private static final Boolean DEFAULT_FLG_ACTIVE_SAB = false;
    private static final Boolean UPDATED_FLG_ACTIVE_SAB = true;

    private static final Boolean DEFAULT_FLG_ACTIVE_DOM = false;
    private static final Boolean UPDATED_FLG_ACTIVE_DOM = true;

    private static final String DEFAULT_WHATS_APP_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_WHATS_APP_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_USER_ACCOUNT = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD_ACCOUNT = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD_ACCOUNT = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/account-bets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AccountBetRepository accountBetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccountBetMockMvc;

    private AccountBet accountBet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBet createEntity(EntityManager em) {
        AccountBet accountBet = new AccountBet()
            .name(DEFAULT_NAME)
            .state(DEFAULT_STATE)
            .type(DEFAULT_TYPE)
            .bettingRoleType(DEFAULT_BETTING_ROLE_TYPE)
            .bettingRoleAmount(DEFAULT_BETTING_ROLE_AMOUNT)
            .hourActiveActive(DEFAULT_HOUR_ACTIVE_ACTIVE)
            .hourActiveEnd(DEFAULT_HOUR_ACTIVE_END)
            .flgActiveLun(DEFAULT_FLG_ACTIVE_LUN)
            .flgActiveMar(DEFAULT_FLG_ACTIVE_MAR)
            .flgActiveMer(DEFAULT_FLG_ACTIVE_MER)
            .flgActiveGio(DEFAULT_FLG_ACTIVE_GIO)
            .flgActiveVen(DEFAULT_FLG_ACTIVE_VEN)
            .flgActiveSab(DEFAULT_FLG_ACTIVE_SAB)
            .flgActiveDom(DEFAULT_FLG_ACTIVE_DOM)
            .whatsAppNumber(DEFAULT_WHATS_APP_NUMBER)
            .userAccount(DEFAULT_USER_ACCOUNT)
            .passwordAccount(DEFAULT_PASSWORD_ACCOUNT);
        return accountBet;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccountBet createUpdatedEntity(EntityManager em) {
        AccountBet accountBet = new AccountBet()
            .name(UPDATED_NAME)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .bettingRoleType(UPDATED_BETTING_ROLE_TYPE)
            .bettingRoleAmount(UPDATED_BETTING_ROLE_AMOUNT)
            .hourActiveActive(UPDATED_HOUR_ACTIVE_ACTIVE)
            .hourActiveEnd(UPDATED_HOUR_ACTIVE_END)
            .flgActiveLun(UPDATED_FLG_ACTIVE_LUN)
            .flgActiveMar(UPDATED_FLG_ACTIVE_MAR)
            .flgActiveMer(UPDATED_FLG_ACTIVE_MER)
            .flgActiveGio(UPDATED_FLG_ACTIVE_GIO)
            .flgActiveVen(UPDATED_FLG_ACTIVE_VEN)
            .flgActiveSab(UPDATED_FLG_ACTIVE_SAB)
            .flgActiveDom(UPDATED_FLG_ACTIVE_DOM)
            .whatsAppNumber(UPDATED_WHATS_APP_NUMBER)
            .userAccount(UPDATED_USER_ACCOUNT)
            .passwordAccount(UPDATED_PASSWORD_ACCOUNT);
        return accountBet;
    }

    @BeforeEach
    public void initTest() {
        accountBet = createEntity(em);
    }

    @Test
    @Transactional
    void createAccountBet() throws Exception {
        int databaseSizeBeforeCreate = accountBetRepository.findAll().size();
        // Create the AccountBet
        restAccountBetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBet)))
            .andExpect(status().isCreated());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeCreate + 1);
        AccountBet testAccountBet = accountBetList.get(accountBetList.size() - 1);
        assertThat(testAccountBet.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAccountBet.getState()).isEqualTo(DEFAULT_STATE);
        assertThat(testAccountBet.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testAccountBet.getBettingRoleType()).isEqualTo(DEFAULT_BETTING_ROLE_TYPE);
        assertThat(testAccountBet.getBettingRoleAmount()).isEqualTo(DEFAULT_BETTING_ROLE_AMOUNT);
        assertThat(testAccountBet.getHourActiveActive()).isEqualTo(DEFAULT_HOUR_ACTIVE_ACTIVE);
        assertThat(testAccountBet.getHourActiveEnd()).isEqualTo(DEFAULT_HOUR_ACTIVE_END);
        assertThat(testAccountBet.getFlgActiveLun()).isEqualTo(DEFAULT_FLG_ACTIVE_LUN);
        assertThat(testAccountBet.getFlgActiveMar()).isEqualTo(DEFAULT_FLG_ACTIVE_MAR);
        assertThat(testAccountBet.getFlgActiveMer()).isEqualTo(DEFAULT_FLG_ACTIVE_MER);
        assertThat(testAccountBet.getFlgActiveGio()).isEqualTo(DEFAULT_FLG_ACTIVE_GIO);
        assertThat(testAccountBet.getFlgActiveVen()).isEqualTo(DEFAULT_FLG_ACTIVE_VEN);
        assertThat(testAccountBet.getFlgActiveSab()).isEqualTo(DEFAULT_FLG_ACTIVE_SAB);
        assertThat(testAccountBet.getFlgActiveDom()).isEqualTo(DEFAULT_FLG_ACTIVE_DOM);
        assertThat(testAccountBet.getWhatsAppNumber()).isEqualTo(DEFAULT_WHATS_APP_NUMBER);
        assertThat(testAccountBet.getUserAccount()).isEqualTo(DEFAULT_USER_ACCOUNT);
        assertThat(testAccountBet.getPasswordAccount()).isEqualTo(DEFAULT_PASSWORD_ACCOUNT);
    }

    @Test
    @Transactional
    void createAccountBetWithExistingId() throws Exception {
        // Create the AccountBet with an existing ID
        accountBet.setId(1L);

        int databaseSizeBeforeCreate = accountBetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccountBetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBet)))
            .andExpect(status().isBadRequest());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAccountBets() throws Exception {
        // Initialize the database
        accountBetRepository.saveAndFlush(accountBet);

        // Get all the accountBetList
        restAccountBetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accountBet.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].state").value(hasItem(DEFAULT_STATE.toString())))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].bettingRoleType").value(hasItem(DEFAULT_BETTING_ROLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].bettingRoleAmount").value(hasItem(DEFAULT_BETTING_ROLE_AMOUNT.doubleValue())))
            .andExpect(jsonPath("$.[*].hourActiveActive").value(hasItem(DEFAULT_HOUR_ACTIVE_ACTIVE)))
            .andExpect(jsonPath("$.[*].hourActiveEnd").value(hasItem(DEFAULT_HOUR_ACTIVE_END)))
            .andExpect(jsonPath("$.[*].flgActiveLun").value(hasItem(DEFAULT_FLG_ACTIVE_LUN.booleanValue())))
            .andExpect(jsonPath("$.[*].flgActiveMar").value(hasItem(DEFAULT_FLG_ACTIVE_MAR.booleanValue())))
            .andExpect(jsonPath("$.[*].flgActiveMer").value(hasItem(DEFAULT_FLG_ACTIVE_MER.booleanValue())))
            .andExpect(jsonPath("$.[*].flgActiveGio").value(hasItem(DEFAULT_FLG_ACTIVE_GIO.booleanValue())))
            .andExpect(jsonPath("$.[*].flgActiveVen").value(hasItem(DEFAULT_FLG_ACTIVE_VEN.booleanValue())))
            .andExpect(jsonPath("$.[*].flgActiveSab").value(hasItem(DEFAULT_FLG_ACTIVE_SAB.booleanValue())))
            .andExpect(jsonPath("$.[*].flgActiveDom").value(hasItem(DEFAULT_FLG_ACTIVE_DOM.booleanValue())))
            .andExpect(jsonPath("$.[*].whatsAppNumber").value(hasItem(DEFAULT_WHATS_APP_NUMBER)))
            .andExpect(jsonPath("$.[*].userAccount").value(hasItem(DEFAULT_USER_ACCOUNT)))
            .andExpect(jsonPath("$.[*].passwordAccount").value(hasItem(DEFAULT_PASSWORD_ACCOUNT)));
    }

    @Test
    @Transactional
    void getAccountBet() throws Exception {
        // Initialize the database
        accountBetRepository.saveAndFlush(accountBet);

        // Get the accountBet
        restAccountBetMockMvc
            .perform(get(ENTITY_API_URL_ID, accountBet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accountBet.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.state").value(DEFAULT_STATE.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.bettingRoleType").value(DEFAULT_BETTING_ROLE_TYPE.toString()))
            .andExpect(jsonPath("$.bettingRoleAmount").value(DEFAULT_BETTING_ROLE_AMOUNT.doubleValue()))
            .andExpect(jsonPath("$.hourActiveActive").value(DEFAULT_HOUR_ACTIVE_ACTIVE))
            .andExpect(jsonPath("$.hourActiveEnd").value(DEFAULT_HOUR_ACTIVE_END))
            .andExpect(jsonPath("$.flgActiveLun").value(DEFAULT_FLG_ACTIVE_LUN.booleanValue()))
            .andExpect(jsonPath("$.flgActiveMar").value(DEFAULT_FLG_ACTIVE_MAR.booleanValue()))
            .andExpect(jsonPath("$.flgActiveMer").value(DEFAULT_FLG_ACTIVE_MER.booleanValue()))
            .andExpect(jsonPath("$.flgActiveGio").value(DEFAULT_FLG_ACTIVE_GIO.booleanValue()))
            .andExpect(jsonPath("$.flgActiveVen").value(DEFAULT_FLG_ACTIVE_VEN.booleanValue()))
            .andExpect(jsonPath("$.flgActiveSab").value(DEFAULT_FLG_ACTIVE_SAB.booleanValue()))
            .andExpect(jsonPath("$.flgActiveDom").value(DEFAULT_FLG_ACTIVE_DOM.booleanValue()))
            .andExpect(jsonPath("$.whatsAppNumber").value(DEFAULT_WHATS_APP_NUMBER))
            .andExpect(jsonPath("$.userAccount").value(DEFAULT_USER_ACCOUNT))
            .andExpect(jsonPath("$.passwordAccount").value(DEFAULT_PASSWORD_ACCOUNT));
    }

    @Test
    @Transactional
    void getNonExistingAccountBet() throws Exception {
        // Get the accountBet
        restAccountBetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccountBet() throws Exception {
        // Initialize the database
        accountBetRepository.saveAndFlush(accountBet);

        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();

        // Update the accountBet
        AccountBet updatedAccountBet = accountBetRepository.findById(accountBet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAccountBet are not directly saved in db
        em.detach(updatedAccountBet);
        updatedAccountBet
            .name(UPDATED_NAME)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .bettingRoleType(UPDATED_BETTING_ROLE_TYPE)
            .bettingRoleAmount(UPDATED_BETTING_ROLE_AMOUNT)
            .hourActiveActive(UPDATED_HOUR_ACTIVE_ACTIVE)
            .hourActiveEnd(UPDATED_HOUR_ACTIVE_END)
            .flgActiveLun(UPDATED_FLG_ACTIVE_LUN)
            .flgActiveMar(UPDATED_FLG_ACTIVE_MAR)
            .flgActiveMer(UPDATED_FLG_ACTIVE_MER)
            .flgActiveGio(UPDATED_FLG_ACTIVE_GIO)
            .flgActiveVen(UPDATED_FLG_ACTIVE_VEN)
            .flgActiveSab(UPDATED_FLG_ACTIVE_SAB)
            .flgActiveDom(UPDATED_FLG_ACTIVE_DOM)
            .whatsAppNumber(UPDATED_WHATS_APP_NUMBER)
            .userAccount(UPDATED_USER_ACCOUNT)
            .passwordAccount(UPDATED_PASSWORD_ACCOUNT);

        restAccountBetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAccountBet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedAccountBet))
            )
            .andExpect(status().isOk());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
        AccountBet testAccountBet = accountBetList.get(accountBetList.size() - 1);
        assertThat(testAccountBet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAccountBet.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAccountBet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccountBet.getBettingRoleType()).isEqualTo(UPDATED_BETTING_ROLE_TYPE);
        assertThat(testAccountBet.getBettingRoleAmount()).isEqualTo(UPDATED_BETTING_ROLE_AMOUNT);
        assertThat(testAccountBet.getHourActiveActive()).isEqualTo(UPDATED_HOUR_ACTIVE_ACTIVE);
        assertThat(testAccountBet.getHourActiveEnd()).isEqualTo(UPDATED_HOUR_ACTIVE_END);
        assertThat(testAccountBet.getFlgActiveLun()).isEqualTo(UPDATED_FLG_ACTIVE_LUN);
        assertThat(testAccountBet.getFlgActiveMar()).isEqualTo(UPDATED_FLG_ACTIVE_MAR);
        assertThat(testAccountBet.getFlgActiveMer()).isEqualTo(UPDATED_FLG_ACTIVE_MER);
        assertThat(testAccountBet.getFlgActiveGio()).isEqualTo(UPDATED_FLG_ACTIVE_GIO);
        assertThat(testAccountBet.getFlgActiveVen()).isEqualTo(UPDATED_FLG_ACTIVE_VEN);
        assertThat(testAccountBet.getFlgActiveSab()).isEqualTo(UPDATED_FLG_ACTIVE_SAB);
        assertThat(testAccountBet.getFlgActiveDom()).isEqualTo(UPDATED_FLG_ACTIVE_DOM);
        assertThat(testAccountBet.getWhatsAppNumber()).isEqualTo(UPDATED_WHATS_APP_NUMBER);
        assertThat(testAccountBet.getUserAccount()).isEqualTo(UPDATED_USER_ACCOUNT);
        assertThat(testAccountBet.getPasswordAccount()).isEqualTo(UPDATED_PASSWORD_ACCOUNT);
    }

    @Test
    @Transactional
    void putNonExistingAccountBet() throws Exception {
        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();
        accountBet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accountBet.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccountBet() throws Exception {
        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();
        accountBet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(accountBet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccountBet() throws Exception {
        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();
        accountBet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(accountBet)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccountBetWithPatch() throws Exception {
        // Initialize the database
        accountBetRepository.saveAndFlush(accountBet);

        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();

        // Update the accountBet using partial update
        AccountBet partialUpdatedAccountBet = new AccountBet();
        partialUpdatedAccountBet.setId(accountBet.getId());

        partialUpdatedAccountBet
            .name(UPDATED_NAME)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .hourActiveActive(UPDATED_HOUR_ACTIVE_ACTIVE)
            .flgActiveLun(UPDATED_FLG_ACTIVE_LUN)
            .flgActiveMer(UPDATED_FLG_ACTIVE_MER)
            .flgActiveGio(UPDATED_FLG_ACTIVE_GIO)
            .flgActiveDom(UPDATED_FLG_ACTIVE_DOM)
            .whatsAppNumber(UPDATED_WHATS_APP_NUMBER)
            .passwordAccount(UPDATED_PASSWORD_ACCOUNT);

        restAccountBetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountBet))
            )
            .andExpect(status().isOk());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
        AccountBet testAccountBet = accountBetList.get(accountBetList.size() - 1);
        assertThat(testAccountBet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAccountBet.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAccountBet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccountBet.getBettingRoleType()).isEqualTo(DEFAULT_BETTING_ROLE_TYPE);
        assertThat(testAccountBet.getBettingRoleAmount()).isEqualTo(DEFAULT_BETTING_ROLE_AMOUNT);
        assertThat(testAccountBet.getHourActiveActive()).isEqualTo(UPDATED_HOUR_ACTIVE_ACTIVE);
        assertThat(testAccountBet.getHourActiveEnd()).isEqualTo(DEFAULT_HOUR_ACTIVE_END);
        assertThat(testAccountBet.getFlgActiveLun()).isEqualTo(UPDATED_FLG_ACTIVE_LUN);
        assertThat(testAccountBet.getFlgActiveMar()).isEqualTo(DEFAULT_FLG_ACTIVE_MAR);
        assertThat(testAccountBet.getFlgActiveMer()).isEqualTo(UPDATED_FLG_ACTIVE_MER);
        assertThat(testAccountBet.getFlgActiveGio()).isEqualTo(UPDATED_FLG_ACTIVE_GIO);
        assertThat(testAccountBet.getFlgActiveVen()).isEqualTo(DEFAULT_FLG_ACTIVE_VEN);
        assertThat(testAccountBet.getFlgActiveSab()).isEqualTo(DEFAULT_FLG_ACTIVE_SAB);
        assertThat(testAccountBet.getFlgActiveDom()).isEqualTo(UPDATED_FLG_ACTIVE_DOM);
        assertThat(testAccountBet.getWhatsAppNumber()).isEqualTo(UPDATED_WHATS_APP_NUMBER);
        assertThat(testAccountBet.getUserAccount()).isEqualTo(DEFAULT_USER_ACCOUNT);
        assertThat(testAccountBet.getPasswordAccount()).isEqualTo(UPDATED_PASSWORD_ACCOUNT);
    }

    @Test
    @Transactional
    void fullUpdateAccountBetWithPatch() throws Exception {
        // Initialize the database
        accountBetRepository.saveAndFlush(accountBet);

        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();

        // Update the accountBet using partial update
        AccountBet partialUpdatedAccountBet = new AccountBet();
        partialUpdatedAccountBet.setId(accountBet.getId());

        partialUpdatedAccountBet
            .name(UPDATED_NAME)
            .state(UPDATED_STATE)
            .type(UPDATED_TYPE)
            .bettingRoleType(UPDATED_BETTING_ROLE_TYPE)
            .bettingRoleAmount(UPDATED_BETTING_ROLE_AMOUNT)
            .hourActiveActive(UPDATED_HOUR_ACTIVE_ACTIVE)
            .hourActiveEnd(UPDATED_HOUR_ACTIVE_END)
            .flgActiveLun(UPDATED_FLG_ACTIVE_LUN)
            .flgActiveMar(UPDATED_FLG_ACTIVE_MAR)
            .flgActiveMer(UPDATED_FLG_ACTIVE_MER)
            .flgActiveGio(UPDATED_FLG_ACTIVE_GIO)
            .flgActiveVen(UPDATED_FLG_ACTIVE_VEN)
            .flgActiveSab(UPDATED_FLG_ACTIVE_SAB)
            .flgActiveDom(UPDATED_FLG_ACTIVE_DOM)
            .whatsAppNumber(UPDATED_WHATS_APP_NUMBER)
            .userAccount(UPDATED_USER_ACCOUNT)
            .passwordAccount(UPDATED_PASSWORD_ACCOUNT);

        restAccountBetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccountBet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAccountBet))
            )
            .andExpect(status().isOk());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
        AccountBet testAccountBet = accountBetList.get(accountBetList.size() - 1);
        assertThat(testAccountBet.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAccountBet.getState()).isEqualTo(UPDATED_STATE);
        assertThat(testAccountBet.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testAccountBet.getBettingRoleType()).isEqualTo(UPDATED_BETTING_ROLE_TYPE);
        assertThat(testAccountBet.getBettingRoleAmount()).isEqualTo(UPDATED_BETTING_ROLE_AMOUNT);
        assertThat(testAccountBet.getHourActiveActive()).isEqualTo(UPDATED_HOUR_ACTIVE_ACTIVE);
        assertThat(testAccountBet.getHourActiveEnd()).isEqualTo(UPDATED_HOUR_ACTIVE_END);
        assertThat(testAccountBet.getFlgActiveLun()).isEqualTo(UPDATED_FLG_ACTIVE_LUN);
        assertThat(testAccountBet.getFlgActiveMar()).isEqualTo(UPDATED_FLG_ACTIVE_MAR);
        assertThat(testAccountBet.getFlgActiveMer()).isEqualTo(UPDATED_FLG_ACTIVE_MER);
        assertThat(testAccountBet.getFlgActiveGio()).isEqualTo(UPDATED_FLG_ACTIVE_GIO);
        assertThat(testAccountBet.getFlgActiveVen()).isEqualTo(UPDATED_FLG_ACTIVE_VEN);
        assertThat(testAccountBet.getFlgActiveSab()).isEqualTo(UPDATED_FLG_ACTIVE_SAB);
        assertThat(testAccountBet.getFlgActiveDom()).isEqualTo(UPDATED_FLG_ACTIVE_DOM);
        assertThat(testAccountBet.getWhatsAppNumber()).isEqualTo(UPDATED_WHATS_APP_NUMBER);
        assertThat(testAccountBet.getUserAccount()).isEqualTo(UPDATED_USER_ACCOUNT);
        assertThat(testAccountBet.getPasswordAccount()).isEqualTo(UPDATED_PASSWORD_ACCOUNT);
    }

    @Test
    @Transactional
    void patchNonExistingAccountBet() throws Exception {
        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();
        accountBet.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccountBetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accountBet.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccountBet() throws Exception {
        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();
        accountBet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(accountBet))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccountBet() throws Exception {
        int databaseSizeBeforeUpdate = accountBetRepository.findAll().size();
        accountBet.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccountBetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(accountBet))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccountBet in the database
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccountBet() throws Exception {
        // Initialize the database
        accountBetRepository.saveAndFlush(accountBet);

        int databaseSizeBeforeDelete = accountBetRepository.findAll().size();

        // Delete the accountBet
        restAccountBetMockMvc
            .perform(delete(ENTITY_API_URL_ID, accountBet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AccountBet> accountBetList = accountBetRepository.findAll();
        assertThat(accountBetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
