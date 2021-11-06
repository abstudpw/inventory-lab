package com.mycompany.inventory.lab.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.inventory.lab.domain.EqTag} entity.
 */
public class EqTagDTO implements Serializable {

    private Long id;

    private EquipmentDTO equipment;

    private TagDTO tag;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EquipmentDTO getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    public TagDTO getTag() {
        return tag;
    }

    public void setTag(TagDTO tag) {
        this.tag = tag;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EqTagDTO)) {
            return false;
        }

        EqTagDTO eqTagDTO = (EqTagDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, eqTagDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EqTagDTO{" +
            "id=" + getId() +
            ", equipment=" + getEquipment() +
            ", tag=" + getTag() +
            "}";
    }
}
