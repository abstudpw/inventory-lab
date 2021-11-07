package com.mycompany.inventory.lab.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Rental.
 */
@Entity
@Table(name = "rental")
public class Rental implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "jhi_from")
    private Instant from;

    @Column(name = "jhi_to")
    private Instant to;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(optional = false)
    @NotNull
    private User rentedBy;

    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @NotNull
    @JsonIgnoreProperties(value = { "equipment" }, allowSetters = true)
    private Item rentedItem;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Rental id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFrom() {
        return this.from;
    }

    public Rental from(Instant from) {
        this.setFrom(from);
        return this;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return this.to;
    }

    public Rental to(Instant to) {
        this.setTo(to);
        return this;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Rental active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public User getRentedBy() {
        return this.rentedBy;
    }

    public void setRentedBy(User user) {
        this.rentedBy = user;
    }

    public Rental rentedBy(User user) {
        this.setRentedBy(user);
        return this;
    }

    public Item getRentedItem() {
        return this.rentedItem;
    }

    public void setRentedItem(Item item) {
        this.rentedItem = item;
    }

    public Rental rentedItem(Item item) {
        this.setRentedItem(item);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Rental)) {
            return false;
        }
        return id != null && id.equals(((Rental) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Rental{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
