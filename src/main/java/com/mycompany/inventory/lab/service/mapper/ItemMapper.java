package com.mycompany.inventory.lab.service.mapper;

import com.mycompany.inventory.lab.domain.*;
import com.mycompany.inventory.lab.service.dto.ItemDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Item} and its DTO {@link ItemDTO}.
 */
@Mapper(componentModel = "spring", uses = { EquipmentMapper.class })
public interface ItemMapper extends EntityMapper<ItemDTO, Item> {
    @Mapping(target = "equipment", source = "equipment", qualifiedByName = "name")
    ItemDTO toDto(Item s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ItemDTO toDtoId(Item item);
}
