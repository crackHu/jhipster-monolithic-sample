package com.hustack.sample.service.mapper;

import com.hustack.sample.domain.*;
import com.hustack.sample.service.dto.GoodsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Goods and its DTO GoodsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface GoodsMapper extends EntityMapper<GoodsDTO, Goods> {



    default Goods fromId(Long id) {
        if (id == null) {
            return null;
        }
        Goods goods = new Goods();
        goods.setId(id);
        return goods;
    }
}
