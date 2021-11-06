package com.mycompany.inventory.lab.service.mapper;

import com.mycompany.inventory.lab.domain.*;
import com.mycompany.inventory.lab.service.dto.EqTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EqTag} and its DTO {@link EqTagDTO}.
 */
@Mapper(componentModel = "spring", uses = { EquipmentMapper.class, TagMapper.class })
public interface EqTagMapper extends EntityMapper<EqTagDTO, EqTag> {
    @Mapping(target = "equipment", source = "equipment", qualifiedByName = "name")
    @Mapping(target = "tag", source = "tag", qualifiedByName = "name")
    EqTagDTO toDto(EqTag s);
}
