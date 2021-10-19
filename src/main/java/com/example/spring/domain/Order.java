package com.example.spring.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "order", schema="public")
public final class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long orderId;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal amount;

    @Column(nullable = false, columnDefinition = "numeric default 0")
    private BigDecimal discount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, columnDefinition = "timestamp default now()")
    private Date updatedAt;

    @PrePersist
    protected void prePersist() {
        if (this.createdAt == null) createdAt = new Date(System.currentTimeMillis());
        if (this.updatedAt == null) updatedAt = new Date(System.currentTimeMillis());
    }

    @PreUpdate
    protected void preUpdate() {
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    @PreRemove
    protected void preRemove() {
        this.updatedAt = new Date(System.currentTimeMillis());
    }

    public Order() { }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return orderId == order.orderId &&
        Objects.equals(name, order.name) &&
        Objects.equals(amount, order.amount) &&
        Objects.equals(discount, order.discount) &&
        Objects.equals(createdAt, order.createdAt) &&
        Objects.equals(updatedAt, order.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, name, amount, discount,
        createdAt, updatedAt);
    }

    @Override
    public String toString() {
        return "Order{" +
        "orderId=" + orderId +
        ", name='" + name + '\'' +
        ", amount=" + amount +
        ", discount=" + discount +
        ", createdAt=" + createdAt +
        ", updatedAt=" + updatedAt +
        '}';
    }
}
