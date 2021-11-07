package com.mycompany.inventory.lab.service;

import com.mycompany.inventory.lab.domain.Item;
import com.mycompany.inventory.lab.domain.Rental;
import com.mycompany.inventory.lab.repository.ItemRepository;
import com.mycompany.inventory.lab.repository.RentalRepository;
import com.mycompany.inventory.lab.service.dto.RentalDTO;
import com.mycompany.inventory.lab.service.mapper.RentalMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Rental}.
 */
@Service
@Transactional
public class RentalService {

    private final Logger log = LoggerFactory.getLogger(RentalService.class);

    private final RentalRepository rentalRepository;

    private final RentalMapper rentalMapper;

    private final ItemRepository itemRepository;

    public RentalService(RentalRepository rentalRepository, RentalMapper rentalMapper, ItemRepository itemRepository) {
        this.rentalRepository = rentalRepository;
        this.rentalMapper = rentalMapper;
        this.itemRepository = itemRepository;
    }

    /**
     * Save a rental.
     *
     * @param rentalDTO the entity to save.
     * @return the persisted entity.
     */
    public RentalDTO save(RentalDTO rentalDTO) {
        log.debug("Request to save Rental : {}", rentalDTO);
        Rental rental = rentalMapper.toEntity(rentalDTO);
        setRented(rental);
        rental = rentalRepository.save(rental);
        return rentalMapper.toDto(rental);
    }

    private void setRented(Rental rental) {
        Optional<Item> fetched = itemRepository.findById(rental.getRentedItem().getId());
        if (fetched.isPresent()) {
            Item item = fetched.get();
            item.setRented(rental.getActive());
            itemRepository.save(item);
        }
    }

    /**
     * Partially update a rental.
     *
     * @param rentalDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<RentalDTO> partialUpdate(RentalDTO rentalDTO) {
        log.debug("Request to partially update Rental : {}", rentalDTO);

        return rentalRepository
            .findById(rentalDTO.getId())
            .map(existingRental -> {
                rentalMapper.partialUpdate(existingRental, rentalDTO);

                return existingRental;
            })
            .map(rentalRepository::save)
            .map(rentalMapper::toDto);
    }

    /**
     * Get all the rentals.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<RentalDTO> findAll() {
        log.debug("Request to get all Rentals");
        return rentalRepository.findAll().stream().map(rentalMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Transactional(readOnly = true)
    public List<RentalDTO> findByRentedItemId(Long itemId) {
        log.debug("Request to get all Rentals");
        return rentalRepository.findByRentedItemIdAndActiveIsTrue(itemId).stream().map(rentalMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one rental by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<RentalDTO> findOne(Long id) {
        log.debug("Request to get Rental : {}", id);
        return rentalRepository.findById(id).map(rentalMapper::toDto);
    }

    /**
     * Delete the rental by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Rental : {}", id);
        rentalRepository.deleteById(id);
    }
}
