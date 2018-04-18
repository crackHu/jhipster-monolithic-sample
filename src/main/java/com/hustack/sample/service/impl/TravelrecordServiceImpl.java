package com.hustack.sample.service.impl;

import com.hustack.sample.service.TravelrecordService;
import com.hustack.sample.domain.Travelrecord;
import com.hustack.sample.repository.TravelrecordRepository;
import com.hustack.sample.repository.search.TravelrecordSearchRepository;
import com.hustack.sample.service.dto.TravelrecordDTO;
import com.hustack.sample.service.mapper.TravelrecordMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Travelrecord.
 */
@Service
@Transactional
public class TravelrecordServiceImpl implements TravelrecordService {

    private final Logger log = LoggerFactory.getLogger(TravelrecordServiceImpl.class);

    private final TravelrecordRepository travelrecordRepository;

    private final TravelrecordMapper travelrecordMapper;

    private final TravelrecordSearchRepository travelrecordSearchRepository;

    public TravelrecordServiceImpl(TravelrecordRepository travelrecordRepository, TravelrecordMapper travelrecordMapper, TravelrecordSearchRepository travelrecordSearchRepository) {
        this.travelrecordRepository = travelrecordRepository;
        this.travelrecordMapper = travelrecordMapper;
        this.travelrecordSearchRepository = travelrecordSearchRepository;
    }

    /**
     * Save a travelrecord.
     *
     * @param travelrecordDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public TravelrecordDTO save(TravelrecordDTO travelrecordDTO) {
        log.debug("Request to save Travelrecord : {}", travelrecordDTO);
        Travelrecord travelrecord = travelrecordMapper.toEntity(travelrecordDTO);
        travelrecord = travelrecordRepository.save(travelrecord);
        TravelrecordDTO result = travelrecordMapper.toDto(travelrecord);
        travelrecordSearchRepository.save(travelrecord);
        return result;
    }

    /**
     * Get all the travelrecords.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TravelrecordDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Travelrecords");
        return travelrecordRepository.findAll(pageable)
            .map(travelrecordMapper::toDto);
    }


    /**
     * Get one travelrecord by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<TravelrecordDTO> findOne(Long id) {
        log.debug("Request to get Travelrecord : {}", id);
        return travelrecordRepository.findById(id)
            .map(travelrecordMapper::toDto);
    }

    /**
     * Delete the travelrecord by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Travelrecord : {}", id);
        travelrecordRepository.deleteById(id);
        travelrecordSearchRepository.deleteById(id);
    }

    /**
     * Search for the travelrecord corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<TravelrecordDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Travelrecords for query {}", query);
        return travelrecordSearchRepository.search(queryStringQuery(query), pageable)
            .map(travelrecordMapper::toDto);
    }
}
