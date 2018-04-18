package com.hustack.sample.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the OrderItems entity.
 */
public class OrderItemsDTO implements Serializable {

    private Long id;

    private Integer orderId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderItemsDTO orderItemsDTO = (OrderItemsDTO) o;
        if (orderItemsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orderItemsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrderItemsDTO{" +
            "id=" + getId() +
            ", orderId=" + getOrderId() +
            "}";
    }
}
