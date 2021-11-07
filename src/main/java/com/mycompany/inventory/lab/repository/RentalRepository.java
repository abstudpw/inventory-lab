package com.mycompany.inventory.lab.repository;

import com.mycompany.inventory.lab.domain.Rental;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Rental entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RentalRepository extends JpaRepository<Rental, Long> {
    @Query("select rental from Rental rental where rental.rentedBy.login = ?#{principal.username}")
    List<Rental> findByRentedByIsCurrentUser();

    List<Rental> findByRentedItemIdAndActiveIsTrue(Long itemId);

}
