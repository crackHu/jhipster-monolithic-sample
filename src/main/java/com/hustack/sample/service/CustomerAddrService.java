package com.hustack.sample.service;

import com.hustack.sample.service.dto.CustomerAddrDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing CustomerAddr.
 */
public interface CustomerAddrService {

    /**
     * Save a customerAddr.
     *
     * @param customerAddrDTO the entity to save
     * @return the persisted entity
     */
    CustomerAddrDTO save(CustomerAddrDTO customerAddrDTO);

    /**
     * Get all the customerAddrs.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomerAddrDTO> findAll(Pageable pageable);


    /**
     * Get the "id" customerAddr.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<CustomerAddrDTO> findOne(Long id);

    /**
     * Delete the "id" customerAddr.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the customerAddr corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<CustomerAddrDTO> search(String query, Pageable pageable);
}
