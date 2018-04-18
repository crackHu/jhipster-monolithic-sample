package com.hustack.sample.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CustomerAddr entity.
 */
public class CustomerAddrDTO implements Serializable {

    private Long id;

    private Integer customerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CustomerAddrDTO customerAddrDTO = (CustomerAddrDTO) o;
        if (customerAddrDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerAddrDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerAddrDTO{" +
            "id=" + getId() +
            ", customerId=" + getCustomerId() +
            "}";
    }
}
