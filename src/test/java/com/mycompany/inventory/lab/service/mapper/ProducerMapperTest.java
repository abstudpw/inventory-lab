package com.mycompany.inventory.lab.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProducerMapperTest {

    private ProducerMapper producerMapper;

    @BeforeEach
    public void setUp() {
        producerMapper = new ProducerMapperImpl();
    }
}
