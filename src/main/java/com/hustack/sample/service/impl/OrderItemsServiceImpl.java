package com.hustack.sample.service.impl;

import com.hustack.sample.service.OrderItemsService;
import com.hustack.sample.domain.OrderItems;
import com.hustack.sample.repository.OrderItemsRepository;
import com.hustack.sample.repository.search.OrderItemsSearchRepository;
import com.hustack.sample.service.dto.OrderItemsDTO;
import com.hustack.sample.service.mapper.OrderItemsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing OrderItems.
 */
@Service
@Transactional
public class OrderItemsServiceImpl implements OrderItemsService {

    private final Logger log = LoggerFactory.getLogger(OrderItemsServiceImpl.class);

    private final OrderItemsRepository orderItemsRepository;

    private final OrderItemsMapper orderItemsMapper;

    private final OrderItemsSearchRepository orderItemsSearchRepository;

    public OrderItemsServiceImpl(OrderItemsRepository orderItemsRepository, OrderItemsMapper orderItemsMapper, OrderItemsSearchRepository orderItemsSearchRepository) {
        this.orderItemsRepository = orderItemsRepository;
        this.orderItemsMapper = orderItemsMapper;
        this.orderItemsSearchRepository = orderItemsSearchRepository;
    }

    /**
     * Save a orderItems.
     *
     * @param orderItemsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrderItemsDTO save(OrderItemsDTO orderItemsDTO) {
        log.debug("Request to save OrderItems : {}", orderItemsDTO);
        OrderItems orderItems = orderItemsMapper.toEntity(orderItemsDTO);
        orderItems = orderItemsRepository.save(orderItems);
        OrderItemsDTO result = orderItemsMapper.toDto(orderItems);
        orderItemsSearchRepository.save(orderItems);
        return result;
    }

    /**
     * Get all the orderItems.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all OrderItems");
        return orderItemsRepository.findAll(pageable)
            .map(orderItemsMapper::toDto);
    }


    /**
     * Get one orderItems by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrderItemsDTO> findOne(Long id) {
        log.debug("Request to get OrderItems : {}", id);
        return orderItemsRepository.findById(id)
            .map(orderItemsMapper::toDto);
    }

    /**
     * Delete the orderItems by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete OrderItems : {}", id);
        orderItemsRepository.deleteById(id);
        orderItemsSearchRepository.deleteById(id);
    }

    /**
     * Search for the orderItems corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<OrderItemsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of OrderItems for query {}", query);
        return orderItemsSearchRepository.search(queryStringQuery(query), pageable)
            .map(orderItemsMapper::toDto);
    }
}
