package com.mycompany.inventory.lab.repository;

import java.util.List;

import com.mycompany.inventory.lab.domain.Item;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Item entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByEquipmentId(Long equipmentId);
}
