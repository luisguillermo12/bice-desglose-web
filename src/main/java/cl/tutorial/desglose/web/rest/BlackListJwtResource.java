package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.domain.BlackListJwt;
import cl.tutorial.desglose.service.BlackListJwtService;
import cl.tutorial.desglose.web.rest.errors.BadRequestAlertException;
import cl.tutorial.desglose.web.rest.util.HeaderUtil;
import cl.tutorial.desglose.web.rest.util.PaginationUtil;
import com.codahale.metrics.annotation.Timed;
import io.github.jhipster.web.util.ResponseUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing BlackListJwt.
 */
@RestController
@RequestMapping("/api")
public class BlackListJwtResource {

    private final Logger log = LogManager.getLogger(BlackListJwtResource.class);

    private static final String ENTITY_NAME = "blackListJwt";

    private final BlackListJwtService blackListJwtService;

    public BlackListJwtResource(BlackListJwtService blackListJwtService) {
        this.blackListJwtService = blackListJwtService;
    }

    /**
     * POST  /black-list-jwts : Create a new blackListJwt.
     *
     * @param blackListJwt
     * 		the blackListJwt to create
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new blackListJwt, or with status 400 (Bad Request) if the blackListJwt has already an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/black-list-jwts")
    @Timed
    public ResponseEntity<BlackListJwt> createBlackListJwt(@Valid @RequestBody BlackListJwt blackListJwt) throws URISyntaxException {
        log.info("REST request to save BlackListJwt : {}", blackListJwt);
        if (blackListJwt.getId() != null) {
            throw new BadRequestAlertException("A new blackListJwt cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BlackListJwt result = blackListJwtService.save(blackListJwt);
        return ResponseEntity.created(new URI("/api/black-list-jwts/" + result.getId())).headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /black-list-jwts : Updates an existing blackListJwt.
     *
     * @param blackListJwt
     * 		the blackListJwt to update
     *
     * @return the ResponseEntity with status 200 (OK) and with body the updated blackListJwt, or with status 400 (Bad Request) if the blackListJwt is not valid, or with status 500
     * (Internal Server Error) if the blackListJwt couldn't be updated
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PutMapping("/black-list-jwts")
    @Timed
    public ResponseEntity<BlackListJwt> updateBlackListJwt(@Valid @RequestBody BlackListJwt blackListJwt) throws URISyntaxException {
        log.info("REST request to update BlackListJwt : {}", blackListJwt);
        if (blackListJwt.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BlackListJwt result = blackListJwtService.save(blackListJwt);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, blackListJwt.getId().toString())).body(result);
    }

    /**
     * GET  /black-list-jwts : get all the blackListJwts.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the ResponseEntity with status 200 (OK) and the list of blackListJwts in body
     */
    @GetMapping("/black-list-jwts")
    @Timed
    public ResponseEntity<List<BlackListJwt>> getAllBlackListJwts(Pageable pageable) {
        log.info("REST request to get a page of BlackListJwts");
        Page<BlackListJwt> page = blackListJwtService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/black-list-jwts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /black-list-jwts/:id : get the "id" blackListJwt.
     *
     * @param id
     * 		the id of the blackListJwt to retrieve
     *
     * @return the ResponseEntity with status 200 (OK) and with body the blackListJwt, or with status 404 (Not Found)
     */
    @GetMapping("/black-list-jwts/{id}")
    @Timed
    public ResponseEntity<BlackListJwt> getBlackListJwt(@PathVariable Long id) {
        log.info("REST request to get BlackListJwt : {}", id);
        Optional<BlackListJwt> blackListJwt = blackListJwtService.findOne(id);
        return ResponseUtil.wrapOrNotFound(blackListJwt);
    }

    /**
     * DELETE  /black-list-jwts/:id : delete the "id" blackListJwt.
     *
     * @param id
     * 		the id of the blackListJwt to delete
     *
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/black-list-jwts/{id}")
    @Timed
    public ResponseEntity<Void> deleteBlackListJwt(@PathVariable Long id) {
        log.info("REST request to delete BlackListJwt : {}", id);
        blackListJwtService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }
}
