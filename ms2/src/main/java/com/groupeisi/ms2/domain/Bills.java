package com.groupeisi.ms2.domain;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * A Bills.
 */
@Entity
@Table(name = "bills")
public class Bills implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "bill_date")
    private LocalDate billDate;

    @Column(name = "customer_id")
    private Long customerId;

    @Transient private Customer customer;

    public Long getId() {
        return this.id;
    }

    public Bills id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return this.amount;
    }

    public Bills amount(Double amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDate getBillDate() {
        return this.billDate;
    }

    public Bills billDate(LocalDate billDate) {
        this.setBillDate(billDate);
        return this;
    }

    public void setBillDate(LocalDate billDate) {
        this.billDate = billDate;
    }

    public Long getCustomerId() {
        return this.customerId;
    }

    public Bills customerId(Long customerId) {
        this.setCustomerId(customerId);
        return this;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bills)) {
            return false;
        }
        return getId() != null && getId().equals(((Bills) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Bills{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", billDate='" + getBillDate() + "'" +
            ", customerId=" + getCustomerId() +
            "}";
    }
}
