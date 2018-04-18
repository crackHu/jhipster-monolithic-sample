package com.hustack.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hustack.sample.service.CustomerAddrService;
import com.hustack.sample.web.rest.errors.BadRequestAlertException;
import com.hustack.sample.web.rest.util.HeaderUtil;
import com.hustack.sample.web.rest.util.PaginationUtil;
import com.hustack.sample.service.dto.CustomerAddrDTO;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing CustomerAddr.
 */
@RestController
@RequestMapping("/api")
public class CustomerAddrResource {

    private final Logger log = LoggerFactory.getLogger(CustomerAddrResource.class);

    private static final String ENTITY_NAME = "customerAddr";

    private final CustomerAddrService customerAddrService;

    public CustomerAddrResource(CustomerAddrService customerAddrService) {
        this.customerAddrService = customerAddrService;
    }

    /**
     * POST  /customer-addrs : Create a new customerAddr.
     *
     * @param customerAddrDTO the customerAddrDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new customerAddrDTO, or with status 400 (Bad Request) if the customerAddr has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/customer-addrs")
    @Timed
    public ResponseEntity<CustomerAddrDTO> createCustomerAddr(@RequestBody CustomerAddrDTO customerAddrDTO) throws URISyntaxException {
        log.debug("REST request to save CustomerAddr : {}", customerAddrDTO);
        if (customerAddrDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerAddr cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerAddrDTO result = customerAddrService.save(customerAddrDTO);
        return ResponseEntity.created(new URI("/api/customer-addrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /customer-addrs : Updates an existing customerAddr.
     *
     * @param customerAddrDTO the customerAddrDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated customerAddrDTO,
     * or with status 400 (Bad Request) if the customerAddrDTO is not valid,
     * or with status 500 (Internal Server Error) if the customerAddrDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/customer-addrs")
    @Timed
    public ResponseEntity<CustomerAddrDTO> updateCustomerAddr(@RequestBody CustomerAddrDTO customerAddrDTO) throws URISyntaxException {
        log.debug("REST request to update CustomerAddr : {}", customerAddrDTO);
        if (customerAddrDTO.getId() == null) {
            return createCustomerAddr(customerAddrDTO);
        }
        CustomerAddrDTO result = customerAddrService.save(customerAddrDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, customerAddrDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /customer-addrs : get all the customerAddrs.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of customerAddrs in body
     */
    @GetMapping("/customer-addrs")
    @Timed
    public ResponseEntity<List<CustomerAddrDTO>> getAllCustomerAddrs(Pageable pageable) {
        log.debug("REST request to get a page of CustomerAddrs");
        Page<CustomerAddrDTO> page = customerAddrService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/customer-addrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /customer-addrs/:id : get the "id" customerAddr.
     *
     * @param id the id of the customerAddrDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the customerAddrDTO, or with status 404 (Not Found)
     */
    @GetMapping("/customer-addrs/{id}")
    @Timed
    public ResponseEntity<CustomerAddrDTO> getCustomerAddr(@PathVariable Long id) {
        log.debug("REST request to get CustomerAddr : {}", id);
        Optional<CustomerAddrDTO> customerAddrDTO = customerAddrService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerAddrDTO);
    }

    /**
     * DELETE  /customer-addrs/:id : delete the "id" customerAddr.
     *
     * @param id the id of the customerAddrDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/customer-addrs/{id}")
    @Timed
    public ResponseEntity<Void> deleteCustomerAddr(@PathVariable Long id) {
        log.debug("REST request to delete CustomerAddr : {}", id);
        customerAddrService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/customer-addrs?query=:query : search for the customerAddr corresponding
     * to the query.
     *
     * @param query the query of the customerAddr search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/customer-addrs")
    @Timed
    public ResponseEntity<List<CustomerAddrDTO>> searchCustomerAddrs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CustomerAddrs for query {}", query);
        Page<CustomerAddrDTO> page = customerAddrService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/customer-addrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
