package com.hustack.sample.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Goods.
 */
@Entity
@Table(name = "goods")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "goods")
public class Goods implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "IdWorker")
    @GenericGenerator(name = "IdWorker", strategy = "com.hustack.sample.config.IdWorker")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Goods name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
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
        Goods goods = (Goods) o;
        if (goods.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), goods.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Goods{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
