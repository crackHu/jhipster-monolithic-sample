package com.hustack.sample.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.hustack.sample.service.OrderItemsService;
import com.hustack.sample.web.rest.errors.BadRequestAlertException;
import com.hustack.sample.web.rest.util.HeaderUtil;
import com.hustack.sample.web.rest.util.PaginationUtil;
import com.hustack.sample.service.dto.OrderItemsDTO;
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
 * REST controller for managing OrderItems.
 */
@RestController
@RequestMapping("/api")
public class OrderItemsResource {

    private final Logger log = LoggerFactory.getLogger(OrderItemsResource.class);

    private static final String ENTITY_NAME = "orderItems";

    private final OrderItemsService orderItemsService;

    public OrderItemsResource(OrderItemsService orderItemsService) {
        this.orderItemsService = orderItemsService;
    }

    /**
     * POST  /order-items : Create a new orderItems.
     *
     * @param orderItemsDTO the orderItemsDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new orderItemsDTO, or with status 400 (Bad Request) if the orderItems has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/order-items")
    @Timed
    public ResponseEntity<OrderItemsDTO> createOrderItems(@RequestBody OrderItemsDTO orderItemsDTO) throws URISyntaxException {
        log.debug("REST request to save OrderItems : {}", orderItemsDTO);
        if (orderItemsDTO.getId() != null) {
            throw new BadRequestAlertException("A new orderItems cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OrderItemsDTO result = orderItemsService.save(orderItemsDTO);
        return ResponseEntity.created(new URI("/api/order-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /order-items : Updates an existing orderItems.
     *
     * @param orderItemsDTO the orderItemsDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated orderItemsDTO,
     * or with status 400 (Bad Request) if the orderItemsDTO is not valid,
     * or with status 500 (Internal Server Error) if the orderItemsDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/order-items")
    @Timed
    public ResponseEntity<OrderItemsDTO> updateOrderItems(@RequestBody OrderItemsDTO orderItemsDTO) throws URISyntaxException {
        log.debug("REST request to update OrderItems : {}", orderItemsDTO);
        if (orderItemsDTO.getId() == null) {
            return createOrderItems(orderItemsDTO);
        }
        OrderItemsDTO result = orderItemsService.save(orderItemsDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, orderItemsDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /order-items : get all the orderItems.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of orderItems in body
     */
    @GetMapping("/order-items")
    @Timed
    public ResponseEntity<List<OrderItemsDTO>> getAllOrderItems(Pageable pageable) {
        log.debug("REST request to get a page of OrderItems");
        Page<OrderItemsDTO> page = orderItemsService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/order-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /order-items/:id : get the "id" orderItems.
     *
     * @param id the id of the orderItemsDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the orderItemsDTO, or with status 404 (Not Found)
     */
    @GetMapping("/order-items/{id}")
    @Timed
    public ResponseEntity<OrderItemsDTO> getOrderItems(@PathVariable Long id) {
        log.debug("REST request to get OrderItems : {}", id);
        Optional<OrderItemsDTO> orderItemsDTO = orderItemsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orderItemsDTO);
    }

    /**
     * DELETE  /order-items/:id : delete the "id" orderItems.
     *
     * @param id the id of the orderItemsDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/order-items/{id}")
    @Timed
    public ResponseEntity<Void> deleteOrderItems(@PathVariable Long id) {
        log.debug("REST request to delete OrderItems : {}", id);
        orderItemsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/order-items?query=:query : search for the orderItems corresponding
     * to the query.
     *
     * @param query the query of the orderItems search
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/order-items")
    @Timed
    public ResponseEntity<List<OrderItemsDTO>> searchOrderItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OrderItems for query {}", query);
        Page<OrderItemsDTO> page = orderItemsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/order-items");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

}
