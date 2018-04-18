package com.hustack.sample.service.mapper;

import com.hustack.sample.domain.*;
import com.hustack.sample.service.dto.OrderItemsDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity OrderItems and its DTO OrderItemsDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface OrderItemsMapper extends EntityMapper<OrderItemsDTO, OrderItems> {



    default OrderItems fromId(Long id) {
        if (id == null) {
            return null;
        }
        OrderItems orderItems = new OrderItems();
        orderItems.setId(id);
        return orderItems;
    }
}
