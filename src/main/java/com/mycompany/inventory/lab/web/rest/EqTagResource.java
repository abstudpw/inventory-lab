package com.mycompany.inventory.lab.web.rest;

import com.mycompany.inventory.lab.repository.EqTagRepository;
import com.mycompany.inventory.lab.service.EqTagService;
import com.mycompany.inventory.lab.service.dto.EqTagDTO;
import com.mycompany.inventory.lab.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.inventory.lab.domain.EqTag}.
 */
@RestController
@RequestMapping("/api")
public class EqTagResource {

    private final Logger log = LoggerFactory.getLogger(EqTagResource.class);

    private static final String ENTITY_NAME = "eqTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EqTagService eqTagService;

    private final EqTagRepository eqTagRepository;

    public EqTagResource(EqTagService eqTagService, EqTagRepository eqTagRepository) {
        this.eqTagService = eqTagService;
        this.eqTagRepository = eqTagRepository;
    }

    /**
     * {@code POST  /eq-tags} : Create a new eqTag.
     *
     * @param eqTagDTO the eqTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new eqTagDTO, or with status {@code 400 (Bad Request)} if the eqTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/eq-tags")
    public ResponseEntity<EqTagDTO> createEqTag(@Valid @RequestBody EqTagDTO eqTagDTO) throws URISyntaxException {
        log.debug("REST request to save EqTag : {}", eqTagDTO);
        if (eqTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new eqTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EqTagDTO result = eqTagService.save(eqTagDTO);
        return ResponseEntity
            .created(new URI("/api/eq-tags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /eq-tags/:id} : Updates an existing eqTag.
     *
     * @param id the id of the eqTagDTO to save.
     * @param eqTagDTO the eqTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eqTagDTO,
     * or with status {@code 400 (Bad Request)} if the eqTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the eqTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/eq-tags/{id}")
    public ResponseEntity<EqTagDTO> updateEqTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EqTagDTO eqTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EqTag : {}, {}", id, eqTagDTO);
        if (eqTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eqTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eqTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EqTagDTO result = eqTagService.save(eqTagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eqTagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /eq-tags/:id} : Partial updates given fields of an existing eqTag, field will ignore if it is null
     *
     * @param id the id of the eqTagDTO to save.
     * @param eqTagDTO the eqTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated eqTagDTO,
     * or with status {@code 400 (Bad Request)} if the eqTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the eqTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the eqTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/eq-tags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EqTagDTO> partialUpdateEqTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EqTagDTO eqTagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EqTag partially : {}, {}", id, eqTagDTO);
        if (eqTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, eqTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!eqTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EqTagDTO> result = eqTagService.partialUpdate(eqTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, eqTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /eq-tags} : get all the eqTags.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of eqTags in body.
     */
    @GetMapping("/eq-tags")
    public List<EqTagDTO> getAllEqTags() {
        log.debug("REST request to get all EqTags");
        return eqTagService.findAll();
    }

    /**
     * {@code GET  /eq-tags/:id} : get the "id" eqTag.
     *
     * @param id the id of the eqTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the eqTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/eq-tags/{id}")
    public ResponseEntity<EqTagDTO> getEqTag(@PathVariable Long id) {
        log.debug("REST request to get EqTag : {}", id);
        Optional<EqTagDTO> eqTagDTO = eqTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(eqTagDTO);
    }

    /**
     * {@code DELETE  /eq-tags/:id} : delete the "id" eqTag.
     *
     * @param id the id of the eqTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/eq-tags/{id}")
    public ResponseEntity<Void> deleteEqTag(@PathVariable Long id) {
        log.debug("REST request to delete EqTag : {}", id);
        eqTagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
