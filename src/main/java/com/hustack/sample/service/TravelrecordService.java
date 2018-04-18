package com.hustack.sample.service;

import com.hustack.sample.service.dto.TravelrecordDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Travelrecord.
 */
public interface TravelrecordService {

    /**
     * Save a travelrecord.
     *
     * @param travelrecordDTO the entity to save
     * @return the persisted entity
     */
    TravelrecordDTO save(TravelrecordDTO travelrecordDTO);

    /**
     * Get all the travelrecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TravelrecordDTO> findAll(Pageable pageable);


    /**
     * Get the "id" travelrecord.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<TravelrecordDTO> findOne(Long id);

    /**
     * Delete the "id" travelrecord.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the travelrecord corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<TravelrecordDTO> search(String query, Pageable pageable);
}
