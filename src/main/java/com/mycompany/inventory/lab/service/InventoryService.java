package com.mycompany.inventory.lab.service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mycompany.inventory.lab.service.dto.EqTagDTO;
import com.mycompany.inventory.lab.service.dto.EquipmentDTO;
import com.mycompany.inventory.lab.service.dto.InventoryDTO;
import com.mycompany.inventory.lab.service.dto.ItemDTO;
import com.mycompany.inventory.lab.service.dto.RentalDTO;
import com.mycompany.inventory.lab.service.dto.TagDTO;

@Service
@Transactional
public class InventoryService {

    private EquipmentService equipmentService;
    private ItemService itemService;
    private RentalService rentalService;
    private EqTagService eqTagService;

    public InventoryService(EquipmentService equipmentService, ItemService itemService, RentalService rentalService, EqTagService eqTagService) {
        this.equipmentService = equipmentService;
        this.itemService = itemService;
        this.rentalService = rentalService;
        this.eqTagService = eqTagService;
    }

    public List<InventoryDTO> getAllInventory() {
        List<EquipmentDTO> equipments = equipmentService.findAll();
        return equipments.stream().map(this::mapToInventoryList).collect(Collectors.toList());
    }

    private InventoryDTO mapToInventoryList(EquipmentDTO equipmentDTO) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        inventoryDTO.setEquipment(equipmentDTO);
        inventoryDTO.setItems(setItems(equipmentDTO));
        inventoryDTO.setTags(setTags(equipmentDTO));
        return inventoryDTO;
    }

    private List<TagDTO> setTags(EquipmentDTO equipmentDTO) {
        List<EqTagDTO> allByEquipmentId = eqTagService.findAllByEquipmentId(equipmentDTO.getId());
        return allByEquipmentId.stream()
            .map(EqTagDTO::getTag)
            .collect(Collectors.toList());
    }

    private List<ItemDTO> setItems(EquipmentDTO equipmentDTO) {
        List<ItemDTO> itemDTOList = itemService.findByEquipmentId(equipmentDTO.getId());
        itemDTOList.forEach(this::getCurrentRenter);
        return itemDTOList;
    }

    private void getCurrentRenter(ItemDTO itemDTO) {
        if (Objects.nonNull(itemDTO) && Boolean.TRUE.equals(itemDTO.getRented())) {
            List<RentalDTO> rentalDTOS = rentalService.findByRentedItemId(itemDTO.getId());
            if (!rentalDTOS.isEmpty()) {
                RentalDTO rentalDTO = rentalDTOS.get(0);
                itemDTO.setRental(rentalDTO);
            }
        }
    }

}
