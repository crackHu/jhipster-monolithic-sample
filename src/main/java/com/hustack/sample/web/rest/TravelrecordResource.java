package com.hustack.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hustack.sample.service.TravelrecordService;
import com.hustack.sample.web.rest.errors.BadRequestAlertException;
import com.hustack.sample.web.rest.util.HeaderUtil;
import com.hustack.sample.web.rest.util.PaginationUtil;
import com.hustack.sample.service.dto.TravelrecordDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Travelrecord.
 */
@RestController
@RequestMapping("/api")
public class TravelrecordResource {

    private final Logger log = LoggerFactory.getLogger(TravelrecordResource.class);

    private static final String ENTITY_NAME = "travelrecord";

    private final TravelrecordService travelrecordService;

    public TravelrecordResource(TravelrecordService travelrecordService) {
        this.travelrecordService = travelrecordService;
    }

    /**
     * POST  /travelrecords : Create a new travelrecord.
     *
     * @param travelrecordDTO the travelrecordDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new travelrecordDTO, or with status 400 (Bad Request) if the travelrecord has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/travelrecords")
    @Timed
    public ResponseEntity<TravelrecordDTO> createTravelrecord(@Valid @RequestBody TravelrecordDTO travelrecordDTO) throws URISyntaxException {
        log.debug("REST request to save Travelrecord : {}", travelrecordDTO);
        if (travelrecordDTO.getId() != null) {
            throw new BadRequestAlertException("A new travelrecord cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TravelrecordDTO result = travelrecordService.save(travelrecordDTO);
        return ResponseEntity.created(new URI("/api/travelrecords/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /travelrecords : Updates an existing travelrecord.
     *
     * @param travelrecordDTO the travelrecordDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated travelrecordDTO,
     * or with status 400 (Bad Request) if the travelrecordDTO is not valid,
     * or with status 500 (Internal Server Error) if the travelrecordDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/travelrecords")
    @Timed
    public ResponseEntity<TravelrecordDTO> updateTravelrecord(@Valid @RequestBody TravelrecordDTO travelrecordDTO) throws URISyntaxException {
        log.debug("REST request to update Travelrecord : {}", travelrecordDTO);
        if (travelrecordDTO.getId() == null) {
            return createTravelrecord(travelrecordDTO);
        }
        TravelrecordDTO result = travelrecordService.save(travelrecordDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, travelrecordDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /travelrecords : get all the travelrecords.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of travelrecords in body
     */
    @GetMapping("/travelrecords")
    @Timed
    public ResponseEntity<List<TravelrecordDTO>> getAllTravelrecords(Pageable pageable) {
        log.debug("REST request to get a page of Travelrecords");
        Page<TravelrecordDTO> page = travelrecordService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/travelrecords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /travelrecords/:id : get the "id" travelrecord.
     *
     * @param id the id of the travelrecordDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the travelrecordDTO, or with status 404 (Not Found)
     */
    @GetMapping("/travelrecords/{id}")
    @Timed
    public ResponseEntity<TravelrecordDTO> getTravelrecord(@PathVariable Long id) {
        log.debug("REST request to get Travelrecord : {}", id);
        Optional<TravelrecordDTO> travelrecordDTO = travelrecordService.findOne(id);
        return ResponseUtil.wrapOrNotFound(travelrecordDTO);
    }

    /**
     * DELETE  /travelrecords/:id : delete the "id" travelrecord.
     *
     * @param id the id of the travelrecordDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/travelrecords/{id}")
    @Timed
    public ResponseEntity<Void> deleteTravelrecord(@PathVariable Long id) {
        log.debug("REST request to delete Travelrecord : {}", id);
        travelrecordService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/travelrecords?query=:query : search for the travelrecord corresponding
     * to the query.
     *
     * @param query the query of the travelrecord search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/travelrecords")
    @Timed
    public ResponseEntity<List<TravelrecordDTO>> searchTravelrecords(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Travelrecords for query {}", query);
        Page<TravelrecordDTO> page = travelrecordService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/travelrecords");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
