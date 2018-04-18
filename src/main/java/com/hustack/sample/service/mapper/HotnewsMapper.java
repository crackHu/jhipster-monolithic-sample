package com.hustack.sample.service.mapper;

import com.hustack.sample.domain.*;
import com.hustack.sample.service.dto.HotnewsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Hotnews and its DTO HotnewsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface HotnewsMapper extends EntityMapper<HotnewsDTO, Hotnews> {



    default Hotnews fromId(Long id) {
        if (id == null) {
            return null;
        }
        Hotnews hotnews = new Hotnews();
        hotnews.setId(id);
        return hotnews;
    }
}
