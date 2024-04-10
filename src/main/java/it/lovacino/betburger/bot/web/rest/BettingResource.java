package it.lovacino.betburger.bot.web.rest;

import it.lovacino.betburger.bot.domain.Betting;
import it.lovacino.betburger.bot.repository.BettingRepository;
import it.lovacino.betburger.bot.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link it.lovacino.betburger.bot.domain.Betting}.
 */
@RestController
@RequestMapping("/api/bettings")
@Transactional
public class BettingResource {

    private final Logger log = LoggerFactory.getLogger(BettingResource.class);

    private static final String ENTITY_NAME = "betting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BettingRepository bettingRepository;

    public BettingResource(BettingRepository bettingRepository) {
        this.bettingRepository = bettingRepository;
    }

    /**
     * {@code POST  /bettings} : Create a new betting.
     *
     * @param betting the betting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new betting, or with status {@code 400 (Bad Request)} if the betting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Betting> createBetting(@RequestBody Betting betting) throws URISyntaxException {
        log.debug("REST request to save Betting : {}", betting);
        if (betting.getId() != null) {
            throw new BadRequestAlertException("A new betting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Betting result = bettingRepository.save(betting);
        return ResponseEntity
            .created(new URI("/api/bettings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bettings/:id} : Updates an existing betting.
     *
     * @param id the id of the betting to save.
     * @param betting the betting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated betting,
     * or with status {@code 400 (Bad Request)} if the betting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the betting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Betting> updateBetting(@PathVariable(value = "id", required = false) final Long id, @RequestBody Betting betting)
        throws URISyntaxException {
        log.debug("REST request to update Betting : {}, {}", id, betting);
        if (betting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, betting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Betting result = bettingRepository.save(betting);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, betting.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bettings/:id} : Partial updates given fields of an existing betting, field will ignore if it is null
     *
     * @param id the id of the betting to save.
     * @param betting the betting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated betting,
     * or with status {@code 400 (Bad Request)} if the betting is not valid,
     * or with status {@code 404 (Not Found)} if the betting is not found,
     * or with status {@code 500 (Internal Server Error)} if the betting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Betting> partialUpdateBetting(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Betting betting
    ) throws URISyntaxException {
        log.debug("REST request to partial update Betting partially : {}, {}", id, betting);
        if (betting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, betting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Betting> result = bettingRepository
            .findById(betting.getId())
            .map(existingBetting -> {
                if (betting.getEventSourceId() != null) {
                    existingBetting.setEventSourceId(betting.getEventSourceId());
                }
                if (betting.getBetTypeParam() != null) {
                    existingBetting.setBetTypeParam(betting.getBetTypeParam());
                }
                if (betting.getKoef() != null) {
                    existingBetting.setKoef(betting.getKoef());
                }
                if (betting.getHome() != null) {
                    existingBetting.setHome(betting.getHome());
                }
                if (betting.getAway() != null) {
                    existingBetting.setAway(betting.getAway());
                }
                if (betting.getLeague() != null) {
                    existingBetting.setLeague(betting.getLeague());
                }
                if (betting.getEventName() != null) {
                    existingBetting.setEventName(betting.getEventName());
                }
                if (betting.getStartedAt() != null) {
                    existingBetting.setStartedAt(betting.getStartedAt());
                }
                if (betting.getUpdatedAt() != null) {
                    existingBetting.setUpdatedAt(betting.getUpdatedAt());
                }
                if (betting.getBetBurgerId() != null) {
                    existingBetting.setBetBurgerId(betting.getBetBurgerId());
                }
                if (betting.getState() != null) {
                    existingBetting.setState(betting.getState());
                }
                if (betting.getAmountBet() != null) {
                    existingBetting.setAmountBet(betting.getAmountBet());
                }
                if (betting.getAmountBetWin() != null) {
                    existingBetting.setAmountBetWin(betting.getAmountBetWin());
                }
                if (betting.getBetResultType() != null) {
                    existingBetting.setBetResultType(betting.getBetResultType());
                }

                return existingBetting;
            })
            .map(bettingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, betting.getId().toString())
        );
    }

    /**
     * {@code GET  /bettings} : get all the bettings.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bettings in body.
     */
    @GetMapping("")
    public List<Betting> getAllBettings() {
        log.debug("REST request to get all Bettings");
        return bettingRepository.findAll();
    }

    /**
     * {@code GET  /bettings/:id} : get the "id" betting.
     *
     * @param id the id of the betting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the betting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Betting> getBetting(@PathVariable("id") Long id) {
        log.debug("REST request to get Betting : {}", id);
        Optional<Betting> betting = bettingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(betting);
    }

    /**
     * {@code DELETE  /bettings/:id} : delete the "id" betting.
     *
     * @param id the id of the betting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBetting(@PathVariable("id") Long id) {
        log.debug("REST request to delete Betting : {}", id);
        bettingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
