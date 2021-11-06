package com.mycompany.inventory.lab.service;

import com.mycompany.inventory.lab.domain.Producer;
import com.mycompany.inventory.lab.repository.ProducerRepository;
import com.mycompany.inventory.lab.service.dto.ProducerDTO;
import com.mycompany.inventory.lab.service.mapper.ProducerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Producer}.
 */
@Service
@Transactional
public class ProducerService {

    private final Logger log = LoggerFactory.getLogger(ProducerService.class);

    private final ProducerRepository producerRepository;

    private final ProducerMapper producerMapper;

    public ProducerService(ProducerRepository producerRepository, ProducerMapper producerMapper) {
        this.producerRepository = producerRepository;
        this.producerMapper = producerMapper;
    }

    /**
     * Save a producer.
     *
     * @param producerDTO the entity to save.
     * @return the persisted entity.
     */
    public ProducerDTO save(ProducerDTO producerDTO) {
        log.debug("Request to save Producer : {}", producerDTO);
        Producer producer = producerMapper.toEntity(producerDTO);
        producer = producerRepository.save(producer);
        return producerMapper.toDto(producer);
    }

    /**
     * Partially update a producer.
     *
     * @param producerDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProducerDTO> partialUpdate(ProducerDTO producerDTO) {
        log.debug("Request to partially update Producer : {}", producerDTO);

        return producerRepository
            .findById(producerDTO.getId())
            .map(existingProducer -> {
                producerMapper.partialUpdate(existingProducer, producerDTO);

                return existingProducer;
            })
            .map(producerRepository::save)
            .map(producerMapper::toDto);
    }

    /**
     * Get all the producers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<ProducerDTO> findAll() {
        log.debug("Request to get all Producers");
        return producerRepository.findAll().stream().map(producerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one producer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProducerDTO> findOne(Long id) {
        log.debug("Request to get Producer : {}", id);
        return producerRepository.findById(id).map(producerMapper::toDto);
    }

    /**
     * Delete the producer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Producer : {}", id);
        producerRepository.deleteById(id);
    }
}
