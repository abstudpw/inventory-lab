package com.mycompany.inventory.lab.service.mapper;

import com.mycompany.inventory.lab.domain.*;
import com.mycompany.inventory.lab.service.dto.EquipmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Equipment} and its DTO {@link EquipmentDTO}.
 */
@Mapper(componentModel = "spring", uses = { ProducerMapper.class })
public interface EquipmentMapper extends EntityMapper<EquipmentDTO, Equipment> {
    @Mapping(target = "producer", source = "producer", qualifiedByName = "name")
    EquipmentDTO toDto(Equipment s);

    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EquipmentDTO toDtoId(Equipment equipment);

    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    EquipmentDTO toDtoName(Equipment equipment);
}
