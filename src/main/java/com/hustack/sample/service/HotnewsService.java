package com.hustack.sample.service;

import com.hustack.sample.service.dto.HotnewsDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing Hotnews.
 */
public interface HotnewsService {

    /**
     * Save a hotnews.
     *
     * @param hotnewsDTO the entity to save
     * @return the persisted entity
     */
    HotnewsDTO save(HotnewsDTO hotnewsDTO);

    /**
     * Get all the hotnews.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HotnewsDTO> findAll(Pageable pageable);


    /**
     * Get the "id" hotnews.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<HotnewsDTO> findOne(Long id);

    /**
     * Delete the "id" hotnews.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the hotnews corresponding to the query.
     *
     * @param query the query of the search
     * 
     * @param pageable the pagination information
     * @return the list of entities
     */
    Page<HotnewsDTO> search(String query, Pageable pageable);
}
