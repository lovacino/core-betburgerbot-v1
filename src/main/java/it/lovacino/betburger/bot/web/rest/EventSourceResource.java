package it.lovacino.betburger.bot.web.rest;

import it.lovacino.betburger.bot.domain.EventSource;
import it.lovacino.betburger.bot.repository.EventSourceRepository;
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
 * REST controller for managing {@link it.lovacino.betburger.bot.domain.EventSource}.
 */
@RestController
@RequestMapping("/api/event-sources")
@Transactional
public class EventSourceResource {

    private final Logger log = LoggerFactory.getLogger(EventSourceResource.class);

    private static final String ENTITY_NAME = "eventSource";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EventSourceRepository eventSourceRepository;

    public EventSourceResource(EventSourceRepository eventSourceRepository) {
        this.eventSourceRepository = eventSourceRepository;
    }

    /**
     * {@code POST  /event-sources} : Create a new eventSource.
     *
     * @param eventSource the eventSource to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eventSource, or with status {@code 400 (Bad Request)} if the eventSource has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<EventSource> createEventSource(@RequestBody EventSource eventSource) throws URISyntaxException {
        log.debug("REST request to save EventSource : {}", eventSource);
        if (eventSource.getId() != null) {
            throw new BadRequestAlertException("A new eventSource cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EventSource result = eventSourceRepository.save(eventSource);
        return ResponseEntity
            .created(new URI("/api/event-sources/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /event-sources/:id} : Updates an existing eventSource.
     *
     * @param id the id of the eventSource to save.
     * @param eventSource the eventSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventSource,
     * or with status {@code 400 (Bad Request)} if the eventSource is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eventSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EventSource> updateEventSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventSource eventSource
    ) throws URISyntaxException {
        log.debug("REST request to update EventSource : {}, {}", id, eventSource);
        if (eventSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EventSource result = eventSourceRepository.save(eventSource);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventSource.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /event-sources/:id} : Partial updates given fields of an existing eventSource, field will ignore if it is null
     *
     * @param id the id of the eventSource to save.
     * @param eventSource the eventSource to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eventSource,
     * or with status {@code 400 (Bad Request)} if the eventSource is not valid,
     * or with status {@code 404 (Not Found)} if the eventSource is not found,
     * or with status {@code 500 (Internal Server Error)} if the eventSource couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EventSource> partialUpdateEventSource(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody EventSource eventSource
    ) throws URISyntaxException {
        log.debug("REST request to partial update EventSource partially : {}, {}", id, eventSource);
        if (eventSource.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eventSource.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eventSourceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EventSource> result = eventSourceRepository
            .findById(eventSource.getId())
            .map(existingEventSource -> {
                if (eventSource.getHome() != null) {
                    existingEventSource.setHome(eventSource.getHome());
                }
                if (eventSource.getAway() != null) {
                    existingEventSource.setAway(eventSource.getAway());
                }
                if (eventSource.getLeague() != null) {
                    existingEventSource.setLeague(eventSource.getLeague());
                }
                if (eventSource.getEventName() != null) {
                    existingEventSource.setEventName(eventSource.getEventName());
                }
                if (eventSource.getBookmakerEventId() != null) {
                    existingEventSource.setBookmakerEventId(eventSource.getBookmakerEventId());
                }
                if (eventSource.getBetTypeParam() != null) {
                    existingEventSource.setBetTypeParam(eventSource.getBetTypeParam());
                }
                if (eventSource.getKoefLastModifiedAt() != null) {
                    existingEventSource.setKoefLastModifiedAt(eventSource.getKoefLastModifiedAt());
                }
                if (eventSource.getScannedAt() != null) {
                    existingEventSource.setScannedAt(eventSource.getScannedAt());
                }
                if (eventSource.getStartedAt() != null) {
                    existingEventSource.setStartedAt(eventSource.getStartedAt());
                }
                if (eventSource.getUpdatedAt() != null) {
                    existingEventSource.setUpdatedAt(eventSource.getUpdatedAt());
                }
                if (eventSource.getBetBurgerId() != null) {
                    existingEventSource.setBetBurgerId(eventSource.getBetBurgerId());
                }
                if (eventSource.getKoef() != null) {
                    existingEventSource.setKoef(eventSource.getKoef());
                }

                return existingEventSource;
            })
            .map(eventSourceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eventSource.getId().toString())
        );
    }

    /**
     * {@code GET  /event-sources} : get all the eventSources.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eventSources in body.
     */
    @GetMapping("")
    public List<EventSource> getAllEventSources() {
        log.debug("REST request to get all EventSources");
        return eventSourceRepository.findAll();
    }

    /**
     * {@code GET  /event-sources/:id} : get the "id" eventSource.
     *
     * @param id the id of the eventSource to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eventSource, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventSource> getEventSource(@PathVariable("id") Long id) {
        log.debug("REST request to get EventSource : {}", id);
        Optional<EventSource> eventSource = eventSourceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(eventSource);
    }

    /**
     * {@code DELETE  /event-sources/:id} : delete the "id" eventSource.
     *
     * @param id the id of the eventSource to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEventSource(@PathVariable("id") Long id) {
        log.debug("REST request to delete EventSource : {}", id);
        eventSourceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
