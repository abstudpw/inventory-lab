package com.mycompany.inventory.lab.service;

import com.mycompany.inventory.lab.domain.EqTag;
import com.mycompany.inventory.lab.repository.EqTagRepository;
import com.mycompany.inventory.lab.service.dto.EqTagDTO;
import com.mycompany.inventory.lab.service.mapper.EqTagMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link EqTag}.
 */
@Service
@Transactional
public class EqTagService {

    private final Logger log = LoggerFactory.getLogger(EqTagService.class);

    private final EqTagRepository eqTagRepository;

    private final EqTagMapper eqTagMapper;

    public EqTagService(EqTagRepository eqTagRepository, EqTagMapper eqTagMapper) {
        this.eqTagRepository = eqTagRepository;
        this.eqTagMapper = eqTagMapper;
    }

    /**
     * Save a eqTag.
     *
     * @param eqTagDTO the entity to save.
     * @return the persisted entity.
     */
    public EqTagDTO save(EqTagDTO eqTagDTO) {
        log.debug("Request to save EqTag : {}", eqTagDTO);
        EqTag eqTag = eqTagMapper.toEntity(eqTagDTO);
        eqTag = eqTagRepository.save(eqTag);
        return eqTagMapper.toDto(eqTag);
    }

    /**
     * Partially update a eqTag.
     *
     * @param eqTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<EqTagDTO> partialUpdate(EqTagDTO eqTagDTO) {
        log.debug("Request to partially update EqTag : {}", eqTagDTO);

        return eqTagRepository
            .findById(eqTagDTO.getId())
            .map(existingEqTag -> {
                eqTagMapper.partialUpdate(existingEqTag, eqTagDTO);

                return existingEqTag;
            })
            .map(eqTagRepository::save)
            .map(eqTagMapper::toDto);
    }

    /**
     * Get all the eqTags.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<EqTagDTO> findAll() {
        log.debug("Request to get all EqTags");
        return eqTagRepository.findAll().stream().map(eqTagMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one eqTag by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<EqTagDTO> findOne(Long id) {
        log.debug("Request to get EqTag : {}", id);
        return eqTagRepository.findById(id).map(eqTagMapper::toDto);
    }

    /**
     * Delete the eqTag by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete EqTag : {}", id);
        eqTagRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EqTagDTO> findAllByEquipmentId(Long eqId) {
        return eqTagRepository.findAllByEquipmentId(eqId).stream().map(eqTagMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

}
