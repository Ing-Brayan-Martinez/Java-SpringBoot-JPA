package com.example.spring.domain;

import lombok.Setter;
import lombok.Getter;
import lombok.ToString;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;


@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "order", schema = "public")
public final class Order {

    /**
     * Variable con el id de la orden.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    /**
     * Variable con el nombre de la orden.
     */
    @Column(nullable = false, length = 100)
    private String name;

    /**
     * Variable con el monto de la orden.
     */
    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal amount;

    /**
     * Variable con el descuento de la orden.
     */
    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal discount;

    /**
     * Variable con la fecha de creacion.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private Date createdAt;

    /**
     * Variable con la fecha de actualización.
     */
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private Date updatedAt;

    @PrePersist
    private void prePersist() {
        if (this.createdAt == null) {
            createdAt = new Date(System.currentTimeMillis());
        }

        if (this.updatedAt == null) {
            updatedAt = new Date(System.currentTimeMillis());
        }
    }

    @PreUpdate
    private void preUpdate() {
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    @PreRemove
    private void preRemove() {
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Order order = (Order) o;
        return orderId != null && Objects.equals(orderId, order.orderId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, name, amount, discount, createdAt, updatedAt);
    }
}
