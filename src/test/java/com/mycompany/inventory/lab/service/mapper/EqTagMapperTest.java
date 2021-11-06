package com.mycompany.inventory.lab.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EqTagMapperTest {

    private EqTagMapper eqTagMapper;

    @BeforeEach
    public void setUp() {
        eqTagMapper = new EqTagMapperImpl();
    }
}
