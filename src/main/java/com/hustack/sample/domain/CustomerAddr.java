package com.hustack.sample.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CustomerAddr.
 */
@Entity
@Table(name = "customer_addr")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "customeraddr")
public class CustomerAddr implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "IdWorker")
    @GenericGenerator(name = "IdWorker", strategy = "com.hustack.sample.config.IdWorker")
    private Long id;

    @Column(name = "customer_id")
    private Integer customerId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public CustomerAddr customerId(Integer customerId) {
        this.customerId = customerId;
        return this;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CustomerAddr customerAddr = (CustomerAddr) o;
        if (customerAddr.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), customerAddr.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CustomerAddr{" +
            "id=" + getId() +
            ", customerId=" + getCustomerId() +
            "}";
    }
}
