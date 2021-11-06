package com.mycompany.inventory.lab.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.inventory.lab.domain.Rental} entity.
 */
public class RentalDTO implements Serializable {

    private Long id;

    private Instant from;

    private Instant to;

    private Boolean active;

    private UserDTO rentedBy;

    private ItemDTO rentedItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getFrom() {
        return from;
    }

    public void setFrom(Instant from) {
        this.from = from;
    }

    public Instant getTo() {
        return to;
    }

    public void setTo(Instant to) {
        this.to = to;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDTO getRentedBy() {
        return rentedBy;
    }

    public void setRentedBy(UserDTO rentedBy) {
        this.rentedBy = rentedBy;
    }

    public ItemDTO getRentedItem() {
        return rentedItem;
    }

    public void setRentedItem(ItemDTO rentedItem) {
        this.rentedItem = rentedItem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RentalDTO)) {
            return false;
        }

        RentalDTO rentalDTO = (RentalDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, rentalDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "RentalDTO{" +
            "id=" + getId() +
            ", from='" + getFrom() + "'" +
            ", to='" + getTo() + "'" +
            ", active='" + getActive() + "'" +
            ", rentedBy=" + getRentedBy() +
            ", rentedItem=" + getRentedItem() +
            "}";
    }
}
