package com.hustack.sample.service.impl;

import com.hustack.sample.service.GoodsService;
import com.hustack.sample.domain.Goods;
import com.hustack.sample.repository.GoodsRepository;
import com.hustack.sample.repository.search.GoodsSearchRepository;
import com.hustack.sample.service.dto.GoodsDTO;
import com.hustack.sample.service.mapper.GoodsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Goods.
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    private final Logger log = LoggerFactory.getLogger(GoodsServiceImpl.class);

    private final GoodsRepository goodsRepository;

    private final GoodsMapper goodsMapper;

    private final GoodsSearchRepository goodsSearchRepository;

    public GoodsServiceImpl(GoodsRepository goodsRepository, GoodsMapper goodsMapper, GoodsSearchRepository goodsSearchRepository) {
        this.goodsRepository = goodsRepository;
        this.goodsMapper = goodsMapper;
        this.goodsSearchRepository = goodsSearchRepository;
    }

    /**
     * Save a goods.
     *
     * @param goodsDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public GoodsDTO save(GoodsDTO goodsDTO) {
        log.debug("Request to save Goods : {}", goodsDTO);
        Goods goods = goodsMapper.toEntity(goodsDTO);
        goods = goodsRepository.save(goods);
        GoodsDTO result = goodsMapper.toDto(goods);
        goodsSearchRepository.save(goods);
        return result;
    }

    /**
     * Get all the goods.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Goods");
        return goodsRepository.findAll(pageable)
            .map(goodsMapper::toDto);
    }


    /**
     * Get one goods by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<GoodsDTO> findOne(Long id) {
        log.debug("Request to get Goods : {}", id);
        return goodsRepository.findById(id)
            .map(goodsMapper::toDto);
    }

    /**
     * Delete the goods by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Goods : {}", id);
        goodsRepository.deleteById(id);
        goodsSearchRepository.deleteById(id);
    }

    /**
     * Search for the goods corresponding to the query.
     *
     * @param query the query of the search
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public Page<GoodsDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Goods for query {}", query);
        return goodsSearchRepository.search(queryStringQuery(query), pageable)
            .map(goodsMapper::toDto);
    }
}
