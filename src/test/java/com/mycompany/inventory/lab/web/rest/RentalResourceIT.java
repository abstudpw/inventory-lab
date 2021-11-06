package com.mycompany.inventory.lab.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.inventory.lab.IntegrationTest;
import com.mycompany.inventory.lab.domain.Item;
import com.mycompany.inventory.lab.domain.Rental;
import com.mycompany.inventory.lab.domain.User;
import com.mycompany.inventory.lab.repository.RentalRepository;
import com.mycompany.inventory.lab.service.dto.RentalDTO;
import com.mycompany.inventory.lab.service.mapper.RentalMapper;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link RentalResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RentalResourceIT {

    private static final Instant DEFAULT_FROM = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_FROM = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_TO = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TO = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/rentals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private RentalMapper rentalMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRentalMockMvc;

    private Rental rental;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createEntity(EntityManager em) {
        Rental rental = new Rental().from(DEFAULT_FROM).to(DEFAULT_TO).active(DEFAULT_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        rental.setRentedBy(user);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        rental.setRentedItem(item);
        return rental;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Rental createUpdatedEntity(EntityManager em) {
        Rental rental = new Rental().from(UPDATED_FROM).to(UPDATED_TO).active(UPDATED_ACTIVE);
        // Add required entity
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        rental.setRentedBy(user);
        // Add required entity
        Item item;
        if (TestUtil.findAll(em, Item.class).isEmpty()) {
            item = ItemResourceIT.createUpdatedEntity(em);
            em.persist(item);
            em.flush();
        } else {
            item = TestUtil.findAll(em, Item.class).get(0);
        }
        rental.setRentedItem(item);
        return rental;
    }

    @BeforeEach
    public void initTest() {
        rental = createEntity(em);
    }

    @Test
    @Transactional
    void createRental() throws Exception {
        int databaseSizeBeforeCreate = rentalRepository.findAll().size();
        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);
        restRentalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate + 1);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testRental.getTo()).isEqualTo(DEFAULT_TO);
        assertThat(testRental.getActive()).isEqualTo(DEFAULT_ACTIVE);
    }

    @Test
    @Transactional
    void createRentalWithExistingId() throws Exception {
        // Create the Rental with an existing ID
        rental.setId(1L);
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        int databaseSizeBeforeCreate = rentalRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRentalMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRentals() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get all the rentalList
        restRentalMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(rental.getId().intValue())))
            .andExpect(jsonPath("$.[*].from").value(hasItem(DEFAULT_FROM.toString())))
            .andExpect(jsonPath("$.[*].to").value(hasItem(DEFAULT_TO.toString())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        // Get the rental
        restRentalMockMvc
            .perform(get(ENTITY_API_URL_ID, rental.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(rental.getId().intValue()))
            .andExpect(jsonPath("$.from").value(DEFAULT_FROM.toString()))
            .andExpect(jsonPath("$.to").value(DEFAULT_TO.toString()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingRental() throws Exception {
        // Get the rental
        restRentalMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental
        Rental updatedRental = rentalRepository.findById(rental.getId()).get();
        // Disconnect from session so that the updates on updatedRental are not directly saved in db
        em.detach(updatedRental);
        updatedRental.from(UPDATED_FROM).to(UPDATED_TO).active(UPDATED_ACTIVE);
        RentalDTO rentalDTO = rentalMapper.toDto(updatedRental);

        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testRental.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testRental.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void putNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, rentalDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental.to(UPDATED_TO).active(UPDATED_ACTIVE);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getFrom()).isEqualTo(DEFAULT_FROM);
        assertThat(testRental.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testRental.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void fullUpdateRentalWithPatch() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();

        // Update the rental using partial update
        Rental partialUpdatedRental = new Rental();
        partialUpdatedRental.setId(rental.getId());

        partialUpdatedRental.from(UPDATED_FROM).to(UPDATED_TO).active(UPDATED_ACTIVE);

        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRental.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRental))
            )
            .andExpect(status().isOk());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
        Rental testRental = rentalList.get(rentalList.size() - 1);
        assertThat(testRental.getFrom()).isEqualTo(UPDATED_FROM);
        assertThat(testRental.getTo()).isEqualTo(UPDATED_TO);
        assertThat(testRental.getActive()).isEqualTo(UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void patchNonExistingRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, rentalDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRental() throws Exception {
        int databaseSizeBeforeUpdate = rentalRepository.findAll().size();
        rental.setId(count.incrementAndGet());

        // Create the Rental
        RentalDTO rentalDTO = rentalMapper.toDto(rental);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRentalMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(rentalDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Rental in the database
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRental() throws Exception {
        // Initialize the database
        rentalRepository.saveAndFlush(rental);

        int databaseSizeBeforeDelete = rentalRepository.findAll().size();

        // Delete the rental
        restRentalMockMvc
            .perform(delete(ENTITY_API_URL_ID, rental.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Rental> rentalList = rentalRepository.findAll();
        assertThat(rentalList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
