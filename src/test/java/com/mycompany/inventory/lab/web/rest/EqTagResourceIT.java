package com.mycompany.inventory.lab.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.inventory.lab.IntegrationTest;
import com.mycompany.inventory.lab.domain.EqTag;
import com.mycompany.inventory.lab.domain.Equipment;
import com.mycompany.inventory.lab.domain.Tag;
import com.mycompany.inventory.lab.repository.EqTagRepository;
import com.mycompany.inventory.lab.service.dto.EqTagDTO;
import com.mycompany.inventory.lab.service.mapper.EqTagMapper;
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
 * Integration tests for the {@link EqTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EqTagResourceIT {

    private static final String ENTITY_API_URL = "/api/eq-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private EqTagRepository eqTagRepository;

    @Autowired
    private EqTagMapper eqTagMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEqTagMockMvc;

    private EqTag eqTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EqTag createEntity(EntityManager em) {
        EqTag eqTag = new EqTag();
        // Add required entity
        Equipment equipment;
        if (TestUtil.findAll(em, Equipment.class).isEmpty()) {
            equipment = EquipmentResourceIT.createEntity(em);
            em.persist(equipment);
            em.flush();
        } else {
            equipment = TestUtil.findAll(em, Equipment.class).get(0);
        }
        eqTag.setEquipment(equipment);
        // Add required entity
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tag = TagResourceIT.createEntity(em);
            em.persist(tag);
            em.flush();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        eqTag.setTag(tag);
        return eqTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EqTag createUpdatedEntity(EntityManager em) {
        EqTag eqTag = new EqTag();
        // Add required entity
        Equipment equipment;
        if (TestUtil.findAll(em, Equipment.class).isEmpty()) {
            equipment = EquipmentResourceIT.createUpdatedEntity(em);
            em.persist(equipment);
            em.flush();
        } else {
            equipment = TestUtil.findAll(em, Equipment.class).get(0);
        }
        eqTag.setEquipment(equipment);
        // Add required entity
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tag = TagResourceIT.createUpdatedEntity(em);
            em.persist(tag);
            em.flush();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        eqTag.setTag(tag);
        return eqTag;
    }

    @BeforeEach
    public void initTest() {
        eqTag = createEntity(em);
    }

    @Test
    @Transactional
    void createEqTag() throws Exception {
        int databaseSizeBeforeCreate = eqTagRepository.findAll().size();
        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);
        restEqTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isCreated());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeCreate + 1);
        EqTag testEqTag = eqTagList.get(eqTagList.size() - 1);
    }

    @Test
    @Transactional
    void createEqTagWithExistingId() throws Exception {
        // Create the EqTag with an existing ID
        eqTag.setId(1L);
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        int databaseSizeBeforeCreate = eqTagRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEqTagMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllEqTags() throws Exception {
        // Initialize the database
        eqTagRepository.saveAndFlush(eqTag);

        // Get all the eqTagList
        restEqTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(eqTag.getId().intValue())));
    }

    @Test
    @Transactional
    void getEqTag() throws Exception {
        // Initialize the database
        eqTagRepository.saveAndFlush(eqTag);

        // Get the eqTag
        restEqTagMockMvc
            .perform(get(ENTITY_API_URL_ID, eqTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(eqTag.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingEqTag() throws Exception {
        // Get the eqTag
        restEqTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewEqTag() throws Exception {
        // Initialize the database
        eqTagRepository.saveAndFlush(eqTag);

        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();

        // Update the eqTag
        EqTag updatedEqTag = eqTagRepository.findById(eqTag.getId()).get();
        // Disconnect from session so that the updates on updatedEqTag are not directly saved in db
        em.detach(updatedEqTag);
        EqTagDTO eqTagDTO = eqTagMapper.toDto(updatedEqTag);

        restEqTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eqTagDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
        EqTag testEqTag = eqTagList.get(eqTagList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingEqTag() throws Exception {
        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();
        eqTag.setId(count.incrementAndGet());

        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEqTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, eqTagDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEqTag() throws Exception {
        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();
        eqTag.setId(count.incrementAndGet());

        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEqTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEqTag() throws Exception {
        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();
        eqTag.setId(count.incrementAndGet());

        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEqTagMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEqTagWithPatch() throws Exception {
        // Initialize the database
        eqTagRepository.saveAndFlush(eqTag);

        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();

        // Update the eqTag using partial update
        EqTag partialUpdatedEqTag = new EqTag();
        partialUpdatedEqTag.setId(eqTag.getId());

        restEqTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEqTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEqTag))
            )
            .andExpect(status().isOk());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
        EqTag testEqTag = eqTagList.get(eqTagList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateEqTagWithPatch() throws Exception {
        // Initialize the database
        eqTagRepository.saveAndFlush(eqTag);

        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();

        // Update the eqTag using partial update
        EqTag partialUpdatedEqTag = new EqTag();
        partialUpdatedEqTag.setId(eqTag.getId());

        restEqTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEqTag.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedEqTag))
            )
            .andExpect(status().isOk());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
        EqTag testEqTag = eqTagList.get(eqTagList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingEqTag() throws Exception {
        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();
        eqTag.setId(count.incrementAndGet());

        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEqTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, eqTagDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEqTag() throws Exception {
        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();
        eqTag.setId(count.incrementAndGet());

        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEqTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEqTag() throws Exception {
        int databaseSizeBeforeUpdate = eqTagRepository.findAll().size();
        eqTag.setId(count.incrementAndGet());

        // Create the EqTag
        EqTagDTO eqTagDTO = eqTagMapper.toDto(eqTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEqTagMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(eqTagDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EqTag in the database
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEqTag() throws Exception {
        // Initialize the database
        eqTagRepository.saveAndFlush(eqTag);

        int databaseSizeBeforeDelete = eqTagRepository.findAll().size();

        // Delete the eqTag
        restEqTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, eqTag.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<EqTag> eqTagList = eqTagRepository.findAll();
        assertThat(eqTagList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
