package com.hustack.sample.service.impl;

import com.hustack.sample.service.CustomerAddrService;
import com.hustack.sample.domain.CustomerAddr;
import com.hustack.sample.repository.CustomerAddrRepository;
import com.hustack.sample.repository.search.CustomerAddrSearchRepository;
import com.hustack.sample.service.dto.CustomerAddrDTO;
import com.hustack.sample.service.mapper.CustomerAddrMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing CustomerAddr.
 */
@Service
@Transactional
public class CustomerAddrServiceImpl implements CustomerAddrService {

    private final Logger log = LoggerFactory.getLogger(CustomerAddrServiceImpl.class);

    private final CustomerAddrRepository customerAddrRepository;

    private final CustomerAddrMapper customerAddrMapper;

    private final CustomerAddrSearchRepository customerAddrSearchRepository;

    public CustomerAddrServiceImpl(CustomerAddrRepository customerAddrRepository, CustomerAddrMapper customerAddrMapper, CustomerAddrSearchRepository customerAddrSearchRepository) {
        this.customerAddrRepository = customerAddrRepository;
        this.customerAddrMapper = customerAddrMapper;
        this.customerAddrSearchRepository = customerAddrSearchRepository;
    }

    /**
     * Save a customerAddr.
     *
     * @param customerAddrDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public CustomerAddrDTO save(CustomerAddrDTO customerAddrDTO) {
        log.debug("Request to save CustomerAddr : {}", customerAddrDTO);
        CustomerAddr customerAddr = customerAddrMapper.toEntity(customerAddrDTO);
        customerAddr = customerAddrRepository.save(customerAddr);
        CustomerAddrDTO result = customerAddrMapper.toDto(customerAddr);
        customerAddrSearchRepository.save(customerAddr);
        return result;
    }

    /**
     * Get all the customerAddrs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerAddrDTO> findAll(Pageable pageable) {
        log.debug("Request to get all CustomerAddrs");
        return customerAddrRepository.findAll(pageable)
            .map(customerAddrMapper::toDto);
    }


    /**
     * Get one customerAddr by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<CustomerAddrDTO> findOne(Long id) {
        log.debug("Request to get CustomerAddr : {}", id);
        return customerAddrRepository.findById(id)
            .map(customerAddrMapper::toDto);
    }

    /**
     * Delete the customerAddr by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete CustomerAddr : {}", id);
        customerAddrRepository.deleteById(id);
        customerAddrSearchRepository.deleteById(id);
    }

    /**
     * Search for the customerAddr corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CustomerAddrDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of CustomerAddrs for query {}", query);
        return customerAddrSearchRepository.search(queryStringQuery(query), pageable)
            .map(customerAddrMapper::toDto);
    }
}
