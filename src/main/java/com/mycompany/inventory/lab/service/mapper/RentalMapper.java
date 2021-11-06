package com.mycompany.inventory.lab.service.mapper;

import com.mycompany.inventory.lab.domain.*;
import com.mycompany.inventory.lab.service.dto.RentalDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Rental} and its DTO {@link RentalDTO}.
 */
@Mapper(componentModel = "spring", uses = { UserMapper.class, ItemMapper.class })
public interface RentalMapper extends EntityMapper<RentalDTO, Rental> {
    @Mapping(target = "rentedBy", source = "rentedBy", qualifiedByName = "login")
    @Mapping(target = "rentedItem", source = "rentedItem", qualifiedByName = "id")
    RentalDTO toDto(Rental s);
}
