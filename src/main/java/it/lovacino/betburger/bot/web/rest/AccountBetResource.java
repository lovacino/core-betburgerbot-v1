package it.lovacino.betburger.bot.web.rest;

import it.lovacino.betburger.bot.domain.AccountBet;
import it.lovacino.betburger.bot.repository.AccountBetRepository;
import it.lovacino.betburger.bot.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link it.lovacino.betburger.bot.domain.AccountBet}.
 */
@RestController
@RequestMapping("/api/account-bets")
@Transactional
public class AccountBetResource {

    private final Logger log = LoggerFactory.getLogger(AccountBetResource.class);

    private static final String ENTITY_NAME = "accountBet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountBetRepository accountBetRepository;

    public AccountBetResource(AccountBetRepository accountBetRepository) {
        this.accountBetRepository = accountBetRepository;
    }

    /**
     * {@code POST  /account-bets} : Create a new accountBet.
     *
     * @param accountBet the accountBet to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountBet, or with status {@code 400 (Bad Request)} if the accountBet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccountBet> createAccountBet(@Valid @RequestBody AccountBet accountBet) throws URISyntaxException {
        log.debug("REST request to save AccountBet : {}", accountBet);
        if (accountBet.getId() != null) {
            throw new BadRequestAlertException("A new accountBet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountBet result = accountBetRepository.save(accountBet);
        return ResponseEntity
            .created(new URI("/api/account-bets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-bets/:id} : Updates an existing accountBet.
     *
     * @param id the id of the accountBet to save.
     * @param accountBet the accountBet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBet,
     * or with status {@code 400 (Bad Request)} if the accountBet is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountBet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccountBet> updateAccountBet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountBet accountBet
    ) throws URISyntaxException {
        log.debug("REST request to update AccountBet : {}, {}", id, accountBet);
        if (accountBet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountBet result = accountBetRepository.save(accountBet);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountBet.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-bets/:id} : Partial updates given fields of an existing accountBet, field will ignore if it is null
     *
     * @param id the id of the accountBet to save.
     * @param accountBet the accountBet to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBet,
     * or with status {@code 400 (Bad Request)} if the accountBet is not valid,
     * or with status {@code 404 (Not Found)} if the accountBet is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountBet couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountBet> partialUpdateAccountBet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountBet accountBet
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountBet partially : {}, {}", id, accountBet);
        if (accountBet.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBet.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountBet> result = accountBetRepository
            .findById(accountBet.getId())
            .map(existingAccountBet -> {
                if (accountBet.getName() != null) {
                    existingAccountBet.setName(accountBet.getName());
                }
                if (accountBet.getState() != null) {
                    existingAccountBet.setState(accountBet.getState());
                }
                if (accountBet.getType() != null) {
                    existingAccountBet.setType(accountBet.getType());
                }
                if (accountBet.getBettingRoleType() != null) {
                    existingAccountBet.setBettingRoleType(accountBet.getBettingRoleType());
                }
                if (accountBet.getBettingRoleAmount() != null) {
                    existingAccountBet.setBettingRoleAmount(accountBet.getBettingRoleAmount());
                }
                if (accountBet.getHourActiveActive() != null) {
                    existingAccountBet.setHourActiveActive(accountBet.getHourActiveActive());
                }
                if (accountBet.getHourActiveEnd() != null) {
                    existingAccountBet.setHourActiveEnd(accountBet.getHourActiveEnd());
                }
                if (accountBet.getFlgActiveLun() != null) {
                    existingAccountBet.setFlgActiveLun(accountBet.getFlgActiveLun());
                }
                if (accountBet.getFlgActiveMar() != null) {
                    existingAccountBet.setFlgActiveMar(accountBet.getFlgActiveMar());
                }
                if (accountBet.getFlgActiveMer() != null) {
                    existingAccountBet.setFlgActiveMer(accountBet.getFlgActiveMer());
                }
                if (accountBet.getFlgActiveGio() != null) {
                    existingAccountBet.setFlgActiveGio(accountBet.getFlgActiveGio());
                }
                if (accountBet.getFlgActiveVen() != null) {
                    existingAccountBet.setFlgActiveVen(accountBet.getFlgActiveVen());
                }
                if (accountBet.getFlgActiveSab() != null) {
                    existingAccountBet.setFlgActiveSab(accountBet.getFlgActiveSab());
                }
                if (accountBet.getFlgActiveDom() != null) {
                    existingAccountBet.setFlgActiveDom(accountBet.getFlgActiveDom());
                }
                if (accountBet.getWhatsAppNumber() != null) {
                    existingAccountBet.setWhatsAppNumber(accountBet.getWhatsAppNumber());
                }
                if (accountBet.getUserAccount() != null) {
                    existingAccountBet.setUserAccount(accountBet.getUserAccount());
                }
                if (accountBet.getPasswordAccount() != null) {
                    existingAccountBet.setPasswordAccount(accountBet.getPasswordAccount());
                }

                return existingAccountBet;
            })
            .map(accountBetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountBet.getId().toString())
        );
    }

    /**
     * {@code GET  /account-bets} : get all the accountBets.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountBets in body.
     */
    @GetMapping("")
    public List<AccountBet> getAllAccountBets() {
        log.debug("REST request to get all AccountBets");
        return accountBetRepository.findAll();
    }

    /**
     * {@code GET  /account-bets/:id} : get the "id" accountBet.
     *
     * @param id the id of the accountBet to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountBet, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccountBet> getAccountBet(@PathVariable("id") Long id) {
        log.debug("REST request to get AccountBet : {}", id);
        Optional<AccountBet> accountBet = accountBetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(accountBet);
    }

    /**
     * {@code DELETE  /account-bets/:id} : delete the "id" accountBet.
     *
     * @param id the id of the accountBet to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountBet(@PathVariable("id") Long id) {
        log.debug("REST request to delete AccountBet : {}", id);
        accountBetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
