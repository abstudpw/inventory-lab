package com.mycompany.inventory.lab.service.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class InventoryDTO implements Serializable {

    private EquipmentDTO equipment;
    private List<ItemDTO> items;
    private List<TagDTO> tags;

    public EquipmentDTO getEquipment() {
        return equipment;
    }

    public void setEquipment(EquipmentDTO equipment) {
        this.equipment = equipment;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InventoryDTO that = (InventoryDTO) o;
        return Objects.equals(equipment, that.equipment) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipment, items);
    }
}
