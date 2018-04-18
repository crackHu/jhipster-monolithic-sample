package com.hustack.sample.service.impl;

import com.hustack.sample.service.HotnewsService;
import com.hustack.sample.domain.Hotnews;
import com.hustack.sample.repository.HotnewsRepository;
import com.hustack.sample.repository.search.HotnewsSearchRepository;
import com.hustack.sample.service.dto.HotnewsDTO;
import com.hustack.sample.service.mapper.HotnewsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Hotnews.
 */
@Service
@Transactional
public class HotnewsServiceImpl implements HotnewsService {

    private final Logger log = LoggerFactory.getLogger(HotnewsServiceImpl.class);

    private final HotnewsRepository hotnewsRepository;

    private final HotnewsMapper hotnewsMapper;

    private final HotnewsSearchRepository hotnewsSearchRepository;

    public HotnewsServiceImpl(HotnewsRepository hotnewsRepository, HotnewsMapper hotnewsMapper, HotnewsSearchRepository hotnewsSearchRepository) {
        this.hotnewsRepository = hotnewsRepository;
        this.hotnewsMapper = hotnewsMapper;
        this.hotnewsSearchRepository = hotnewsSearchRepository;
    }

    /**
     * Save a hotnews.
     *
     * @param hotnewsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public HotnewsDTO save(HotnewsDTO hotnewsDTO) {
        log.debug("Request to save Hotnews : {}", hotnewsDTO);
        Hotnews hotnews = hotnewsMapper.toEntity(hotnewsDTO);
        hotnews = hotnewsRepository.save(hotnews);
        HotnewsDTO result = hotnewsMapper.toDto(hotnews);
        hotnewsSearchRepository.save(hotnews);
        return result;
    }

    /**
     * Get all the hotnews.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HotnewsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Hotnews");
        return hotnewsRepository.findAll(pageable)
            .map(hotnewsMapper::toDto);
    }


    /**
     * Get one hotnews by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<HotnewsDTO> findOne(Long id) {
        log.debug("Request to get Hotnews : {}", id);
        return hotnewsRepository.findById(id)
            .map(hotnewsMapper::toDto);
    }

    /**
     * Delete the hotnews by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Hotnews : {}", id);
        hotnewsRepository.deleteById(id);
        hotnewsSearchRepository.deleteById(id);
    }

    /**
     * Search for the hotnews corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<HotnewsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Hotnews for query {}", query);
        return hotnewsSearchRepository.search(queryStringQuery(query), pageable)
            .map(hotnewsMapper::toDto);
    }
}
