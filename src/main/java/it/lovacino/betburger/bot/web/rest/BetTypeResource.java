package it.lovacino.betburger.bot.web.rest;

import it.lovacino.betburger.bot.domain.BetType;
import it.lovacino.betburger.bot.repository.BetTypeRepository;
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
 * REST controller for managing {@link it.lovacino.betburger.bot.domain.BetType}.
 */
@RestController
@RequestMapping("/api/bet-types")
@Transactional
public class BetTypeResource {

    private final Logger log = LoggerFactory.getLogger(BetTypeResource.class);

    private static final String ENTITY_NAME = "betType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BetTypeRepository betTypeRepository;

    public BetTypeResource(BetTypeRepository betTypeRepository) {
        this.betTypeRepository = betTypeRepository;
    }

    /**
     * {@code POST  /bet-types} : Create a new betType.
     *
     * @param betType the betType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new betType, or with status {@code 400 (Bad Request)} if the betType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BetType> createBetType(@Valid @RequestBody BetType betType) throws URISyntaxException {
        log.debug("REST request to save BetType : {}", betType);
        if (betType.getId() != null) {
            throw new BadRequestAlertException("A new betType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BetType result = betTypeRepository.save(betType);
        return ResponseEntity
            .created(new URI("/api/bet-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bet-types/:id} : Updates an existing betType.
     *
     * @param id the id of the betType to save.
     * @param betType the betType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated betType,
     * or with status {@code 400 (Bad Request)} if the betType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the betType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BetType> updateBetType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BetType betType
    ) throws URISyntaxException {
        log.debug("REST request to update BetType : {}, {}", id, betType);
        if (betType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, betType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!betTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BetType result = betTypeRepository.save(betType);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, betType.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bet-types/:id} : Partial updates given fields of an existing betType, field will ignore if it is null
     *
     * @param id the id of the betType to save.
     * @param betType the betType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated betType,
     * or with status {@code 400 (Bad Request)} if the betType is not valid,
     * or with status {@code 404 (Not Found)} if the betType is not found,
     * or with status {@code 500 (Internal Server Error)} if the betType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BetType> partialUpdateBetType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BetType betType
    ) throws URISyntaxException {
        log.debug("REST request to partial update BetType partially : {}, {}", id, betType);
        if (betType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, betType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!betTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BetType> result = betTypeRepository
            .findById(betType.getId())
            .map(existingBetType -> {
                if (betType.getName() != null) {
                    existingBetType.setName(betType.getName());
                }

                return existingBetType;
            })
            .map(betTypeRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, betType.getId().toString())
        );
    }

    /**
     * {@code GET  /bet-types} : get all the betTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of betTypes in body.
     */
    @GetMapping("")
    public List<BetType> getAllBetTypes() {
        log.debug("REST request to get all BetTypes");
        return betTypeRepository.findAll();
    }

    /**
     * {@code GET  /bet-types/:id} : get the "id" betType.
     *
     * @param id the id of the betType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the betType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BetType> getBetType(@PathVariable("id") Long id) {
        log.debug("REST request to get BetType : {}", id);
        Optional<BetType> betType = betTypeRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(betType);
    }

    /**
     * {@code DELETE  /bet-types/:id} : delete the "id" betType.
     *
     * @param id the id of the betType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBetType(@PathVariable("id") Long id) {
        log.debug("REST request to delete BetType : {}", id);
        betTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
