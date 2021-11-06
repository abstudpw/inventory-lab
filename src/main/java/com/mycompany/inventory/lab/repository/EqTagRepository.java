package com.mycompany.inventory.lab.repository;

import java.util.List;

import com.mycompany.inventory.lab.domain.EqTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the EqTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EqTagRepository extends JpaRepository<EqTag, Long> {

    List<EqTag> findAllByEquipmentId(Long eqId);

}
