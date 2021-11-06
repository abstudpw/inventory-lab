package com.mycompany.inventory.lab.service.mapper;

import com.mycompany.inventory.lab.domain.*;
import com.mycompany.inventory.lab.service.dto.ProducerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Producer} and its DTO {@link ProducerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ProducerMapper extends EntityMapper<ProducerDTO, Producer> {
    @Named("name")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    ProducerDTO toDtoName(Producer producer);
}
