package com.hustack.sample.service.mapper;

import com.hustack.sample.domain.*;
import com.hustack.sample.service.dto.CustomerAddrDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity CustomerAddr and its DTO CustomerAddrDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CustomerAddrMapper extends EntityMapper<CustomerAddrDTO, CustomerAddr> {



    default CustomerAddr fromId(Long id) {
        if (id == null) {
            return null;
        }
        CustomerAddr customerAddr = new CustomerAddr();
        customerAddr.setId(id);
        return customerAddr;
    }
}
