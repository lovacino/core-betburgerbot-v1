package it.lovacino.betburger.bot.web.rest;

import it.lovacino.betburger.bot.domain.Bookmaker;
import it.lovacino.betburger.bot.repository.BookmakerRepository;
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
 * REST controller for managing {@link it.lovacino.betburger.bot.domain.Bookmaker}.
 */
@RestController
@RequestMapping("/api/bookmakers")
@Transactional
public class BookmakerResource {

    private final Logger log = LoggerFactory.getLogger(BookmakerResource.class);

    private static final String ENTITY_NAME = "bookmaker";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookmakerRepository bookmakerRepository;

    public BookmakerResource(BookmakerRepository bookmakerRepository) {
        this.bookmakerRepository = bookmakerRepository;
    }

    /**
     * {@code POST  /bookmakers} : Create a new bookmaker.
     *
     * @param bookmaker the bookmaker to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookmaker, or with status {@code 400 (Bad Request)} if the bookmaker has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Bookmaker> createBookmaker(@Valid @RequestBody Bookmaker bookmaker) throws URISyntaxException {
        log.debug("REST request to save Bookmaker : {}", bookmaker);
        if (bookmaker.getId() != null) {
            throw new BadRequestAlertException("A new bookmaker cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Bookmaker result = bookmakerRepository.save(bookmaker);
        return ResponseEntity
            .created(new URI("/api/bookmakers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bookmakers/:id} : Updates an existing bookmaker.
     *
     * @param id the id of the bookmaker to save.
     * @param bookmaker the bookmaker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookmaker,
     * or with status {@code 400 (Bad Request)} if the bookmaker is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookmaker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Bookmaker> updateBookmaker(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Bookmaker bookmaker
    ) throws URISyntaxException {
        log.debug("REST request to update Bookmaker : {}, {}", id, bookmaker);
        if (bookmaker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookmaker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookmakerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Bookmaker result = bookmakerRepository.save(bookmaker);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookmaker.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bookmakers/:id} : Partial updates given fields of an existing bookmaker, field will ignore if it is null
     *
     * @param id the id of the bookmaker to save.
     * @param bookmaker the bookmaker to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookmaker,
     * or with status {@code 400 (Bad Request)} if the bookmaker is not valid,
     * or with status {@code 404 (Not Found)} if the bookmaker is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookmaker couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Bookmaker> partialUpdateBookmaker(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Bookmaker bookmaker
    ) throws URISyntaxException {
        log.debug("REST request to partial update Bookmaker partially : {}, {}", id, bookmaker);
        if (bookmaker.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookmaker.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookmakerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Bookmaker> result = bookmakerRepository
            .findById(bookmaker.getId())
            .map(existingBookmaker -> {
                if (bookmaker.getName() != null) {
                    existingBookmaker.setName(bookmaker.getName());
                }
                if (bookmaker.getState() != null) {
                    existingBookmaker.setState(bookmaker.getState());
                }

                return existingBookmaker;
            })
            .map(bookmakerRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookmaker.getId().toString())
        );
    }

    /**
     * {@code GET  /bookmakers} : get all the bookmakers.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookmakers in body.
     */
    @GetMapping("")
    public List<Bookmaker> getAllBookmakers() {
        log.debug("REST request to get all Bookmakers");
        return bookmakerRepository.findAll();
    }

    /**
     * {@code GET  /bookmakers/:id} : get the "id" bookmaker.
     *
     * @param id the id of the bookmaker to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookmaker, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Bookmaker> getBookmaker(@PathVariable("id") Long id) {
        log.debug("REST request to get Bookmaker : {}", id);
        Optional<Bookmaker> bookmaker = bookmakerRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(bookmaker);
    }

    /**
     * {@code DELETE  /bookmakers/:id} : delete the "id" bookmaker.
     *
     * @param id the id of the bookmaker to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmaker(@PathVariable("id") Long id) {
        log.debug("REST request to delete Bookmaker : {}", id);
        bookmakerRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
