package it.lovacino.betburger.bot.web.rest;

import it.lovacino.betburger.bot.domain.ConfigParam;
import it.lovacino.betburger.bot.repository.ConfigParamRepository;
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
 * REST controller for managing {@link it.lovacino.betburger.bot.domain.ConfigParam}.
 */
@RestController
@RequestMapping("/api/config-params")
@Transactional
public class ConfigParamResource {

    private final Logger log = LoggerFactory.getLogger(ConfigParamResource.class);

    private static final String ENTITY_NAME = "configParam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigParamRepository configParamRepository;

    public ConfigParamResource(ConfigParamRepository configParamRepository) {
        this.configParamRepository = configParamRepository;
    }

    /**
     * {@code POST  /config-params} : Create a new configParam.
     *
     * @param configParam the configParam to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configParam, or with status {@code 400 (Bad Request)} if the configParam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ConfigParam> createConfigParam(@Valid @RequestBody ConfigParam configParam) throws URISyntaxException {
        log.debug("REST request to save ConfigParam : {}", configParam);
        if (configParam.getId() != null) {
            throw new BadRequestAlertException("A new configParam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigParam result = configParamRepository.save(configParam);
        return ResponseEntity
            .created(new URI("/api/config-params/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-params/:id} : Updates an existing configParam.
     *
     * @param id the id of the configParam to save.
     * @param configParam the configParam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configParam,
     * or with status {@code 400 (Bad Request)} if the configParam is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configParam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ConfigParam> updateConfigParam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConfigParam configParam
    ) throws URISyntaxException {
        log.debug("REST request to update ConfigParam : {}, {}", id, configParam);
        if (configParam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configParam.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configParamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConfigParam result = configParamRepository.save(configParam);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configParam.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /config-params/:id} : Partial updates given fields of an existing configParam, field will ignore if it is null
     *
     * @param id the id of the configParam to save.
     * @param configParam the configParam to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configParam,
     * or with status {@code 400 (Bad Request)} if the configParam is not valid,
     * or with status {@code 404 (Not Found)} if the configParam is not found,
     * or with status {@code 500 (Internal Server Error)} if the configParam couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfigParam> partialUpdateConfigParam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfigParam configParam
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConfigParam partially : {}, {}", id, configParam);
        if (configParam.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configParam.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configParamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfigParam> result = configParamRepository
            .findById(configParam.getId())
            .map(existingConfigParam -> {
                if (configParam.getParamName() != null) {
                    existingConfigParam.setParamName(configParam.getParamName());
                }
                if (configParam.getParamValue() != null) {
                    existingConfigParam.setParamValue(configParam.getParamValue());
                }

                return existingConfigParam;
            })
            .map(configParamRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configParam.getId().toString())
        );
    }

    /**
     * {@code GET  /config-params} : get all the configParams.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configParams in body.
     */
    @GetMapping("")
    public List<ConfigParam> getAllConfigParams() {
        log.debug("REST request to get all ConfigParams");
        return configParamRepository.findAll();
    }

    /**
     * {@code GET  /config-params/:id} : get the "id" configParam.
     *
     * @param id the id of the configParam to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configParam, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ConfigParam> getConfigParam(@PathVariable("id") Long id) {
        log.debug("REST request to get ConfigParam : {}", id);
        Optional<ConfigParam> configParam = configParamRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(configParam);
    }

    /**
     * {@code DELETE  /config-params/:id} : delete the "id" configParam.
     *
     * @param id the id of the configParam to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConfigParam(@PathVariable("id") Long id) {
        log.debug("REST request to delete ConfigParam : {}", id);
        configParamRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
