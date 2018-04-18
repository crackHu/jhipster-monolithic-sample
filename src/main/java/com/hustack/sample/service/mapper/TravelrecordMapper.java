package com.hustack.sample.service.mapper;

import com.hustack.sample.domain.*;
import com.hustack.sample.service.dto.TravelrecordDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity Travelrecord and its DTO TravelrecordDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface TravelrecordMapper extends EntityMapper<TravelrecordDTO, Travelrecord> {



    default Travelrecord fromId(Long id) {
        if (id == null) {
            return null;
        }
        Travelrecord travelrecord = new Travelrecord();
        travelrecord.setId(id);
        return travelrecord;
    }
}
