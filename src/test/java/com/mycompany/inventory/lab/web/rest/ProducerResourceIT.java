package com.mycompany.inventory.lab.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.inventory.lab.IntegrationTest;
import com.mycompany.inventory.lab.domain.Producer;
import com.mycompany.inventory.lab.repository.ProducerRepository;
import com.mycompany.inventory.lab.service.criteria.ProducerCriteria;
import com.mycompany.inventory.lab.service.dto.ProducerDTO;
import com.mycompany.inventory.lab.service.mapper.ProducerMapper;
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
 * Integration tests for the {@link ProducerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProducerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/producers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ProducerRepository producerRepository;

    @Autowired
    private ProducerMapper producerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProducerMockMvc;

    private Producer producer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createEntity(EntityManager em) {
        Producer producer = new Producer().name(DEFAULT_NAME);
        return producer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Producer createUpdatedEntity(EntityManager em) {
        Producer producer = new Producer().name(UPDATED_NAME);
        return producer;
    }

    @BeforeEach
    public void initTest() {
        producer = createEntity(em);
    }

    @Test
    @Transactional
    void createProducer() throws Exception {
        int databaseSizeBeforeCreate = producerRepository.findAll().size();
        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);
        restProducerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeCreate + 1);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createProducerWithExistingId() throws Exception {
        // Create the Producer with an existing ID
        producer.setId(1L);
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        int databaseSizeBeforeCreate = producerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProducerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = producerRepository.findAll().size();
        // set the field null
        producer.setName(null);

        // Create the Producer, which fails.
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        restProducerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProducers() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get the producer
        restProducerMockMvc
            .perform(get(ENTITY_API_URL_ID, producer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(producer.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getProducersByIdFiltering() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        Long id = producer.getId();

        defaultProducerShouldBeFound("id.equals=" + id);
        defaultProducerShouldNotBeFound("id.notEquals=" + id);

        defaultProducerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultProducerShouldNotBeFound("id.greaterThan=" + id);

        defaultProducerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultProducerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllProducersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where name equals to DEFAULT_NAME
        defaultProducerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the producerList where name equals to UPDATED_NAME
        defaultProducerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProducersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where name not equals to DEFAULT_NAME
        defaultProducerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the producerList where name not equals to UPDATED_NAME
        defaultProducerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProducersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultProducerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the producerList where name equals to UPDATED_NAME
        defaultProducerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProducersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where name is not null
        defaultProducerShouldBeFound("name.specified=true");

        // Get all the producerList where name is null
        defaultProducerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllProducersByNameContainsSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where name contains DEFAULT_NAME
        defaultProducerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the producerList where name contains UPDATED_NAME
        defaultProducerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllProducersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        // Get all the producerList where name does not contain DEFAULT_NAME
        defaultProducerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the producerList where name does not contain UPDATED_NAME
        defaultProducerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultProducerShouldBeFound(String filter) throws Exception {
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(producer.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultProducerShouldNotBeFound(String filter) throws Exception {
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restProducerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingProducer() throws Exception {
        // Get the producer
        restProducerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        int databaseSizeBeforeUpdate = producerRepository.findAll().size();

        // Update the producer
        Producer updatedProducer = producerRepository.findById(producer.getId()).get();
        // Disconnect from session so that the updates on updatedProducer are not directly saved in db
        em.detach(updatedProducer);
        updatedProducer.name(UPDATED_NAME);
        ProducerDTO producerDTO = producerMapper.toDto(updatedProducer);

        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void putNonExistingProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, producerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProducerWithPatch() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        int databaseSizeBeforeUpdate = producerRepository.findAll().size();

        // Update the producer using partial update
        Producer partialUpdatedProducer = new Producer();
        partialUpdatedProducer.setId(producer.getId());

        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void fullUpdateProducerWithPatch() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        int databaseSizeBeforeUpdate = producerRepository.findAll().size();

        // Update the producer using partial update
        Producer partialUpdatedProducer = new Producer();
        partialUpdatedProducer.setId(producer.getId());

        partialUpdatedProducer.name(UPDATED_NAME);

        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProducer.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedProducer))
            )
            .andExpect(status().isOk());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
        Producer testProducer = producerList.get(producerList.size() - 1);
        assertThat(testProducer.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    void patchNonExistingProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, producerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProducer() throws Exception {
        int databaseSizeBeforeUpdate = producerRepository.findAll().size();
        producer.setId(count.incrementAndGet());

        // Create the Producer
        ProducerDTO producerDTO = producerMapper.toDto(producer);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProducerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(producerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Producer in the database
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProducer() throws Exception {
        // Initialize the database
        producerRepository.saveAndFlush(producer);

        int databaseSizeBeforeDelete = producerRepository.findAll().size();

        // Delete the producer
        restProducerMockMvc
            .perform(delete(ENTITY_API_URL_ID, producer.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Producer> producerList = producerRepository.findAll();
        assertThat(producerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
