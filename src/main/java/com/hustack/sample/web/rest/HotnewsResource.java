package com.hustack.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hustack.sample.service.HotnewsService;
import com.hustack.sample.web.rest.errors.BadRequestAlertException;
import com.hustack.sample.web.rest.util.HeaderUtil;
import com.hustack.sample.web.rest.util.PaginationUtil;
import com.hustack.sample.service.dto.HotnewsDTO;
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
 * REST controller for managing Hotnews.
 */
@RestController
@RequestMapping("/api")
public class HotnewsResource {

    private final Logger log = LoggerFactory.getLogger(HotnewsResource.class);

    private static final String ENTITY_NAME = "hotnews";

    private final HotnewsService hotnewsService;

    public HotnewsResource(HotnewsService hotnewsService) {
        this.hotnewsService = hotnewsService;
    }

    /**
     * POST  /hotnews : Create a new hotnews.
     *
     * @param hotnewsDTO the hotnewsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new hotnewsDTO, or with status 400 (Bad Request) if the hotnews has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/hotnews")
    @Timed
    public ResponseEntity<HotnewsDTO> createHotnews(@Valid @RequestBody HotnewsDTO hotnewsDTO) throws URISyntaxException {
        log.debug("REST request to save Hotnews : {}", hotnewsDTO);
        if (hotnewsDTO.getId() != null) {
            throw new BadRequestAlertException("A new hotnews cannot already have an ID", ENTITY_NAME, "idexists");
        }
        HotnewsDTO result = hotnewsService.save(hotnewsDTO);
        return ResponseEntity.created(new URI("/api/hotnews/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /hotnews : Updates an existing hotnews.
     *
     * @param hotnewsDTO the hotnewsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated hotnewsDTO,
     * or with status 400 (Bad Request) if the hotnewsDTO is not valid,
     * or with status 500 (Internal Server Error) if the hotnewsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/hotnews")
    @Timed
    public ResponseEntity<HotnewsDTO> updateHotnews(@Valid @RequestBody HotnewsDTO hotnewsDTO) throws URISyntaxException {
        log.debug("REST request to update Hotnews : {}", hotnewsDTO);
        if (hotnewsDTO.getId() == null) {
            return createHotnews(hotnewsDTO);
        }
        HotnewsDTO result = hotnewsService.save(hotnewsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, hotnewsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /hotnews : get all the hotnews.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of hotnews in body
     */
    @GetMapping("/hotnews")
    @Timed
    public ResponseEntity<List<HotnewsDTO>> getAllHotnews(Pageable pageable) {
        log.debug("REST request to get a page of Hotnews");
        Page<HotnewsDTO> page = hotnewsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/hotnews");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /hotnews/:id : get the "id" hotnews.
     *
     * @param id the id of the hotnewsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the hotnewsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/hotnews/{id}")
    @Timed
    public ResponseEntity<HotnewsDTO> getHotnews(@PathVariable Long id) {
        log.debug("REST request to get Hotnews : {}", id);
        Optional<HotnewsDTO> hotnewsDTO = hotnewsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hotnewsDTO);
    }

    /**
     * DELETE  /hotnews/:id : delete the "id" hotnews.
     *
     * @param id the id of the hotnewsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/hotnews/{id}")
    @Timed
    public ResponseEntity<Void> deleteHotnews(@PathVariable Long id) {
        log.debug("REST request to delete Hotnews : {}", id);
        hotnewsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/hotnews?query=:query : search for the hotnews corresponding
     * to the query.
     *
     * @param query the query of the hotnews search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/hotnews")
    @Timed
    public ResponseEntity<List<HotnewsDTO>> searchHotnews(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Hotnews for query {}", query);
        Page<HotnewsDTO> page = hotnewsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/hotnews");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
