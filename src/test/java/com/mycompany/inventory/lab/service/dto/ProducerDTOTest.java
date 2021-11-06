package com.mycompany.inventory.lab.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.inventory.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProducerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProducerDTO.class);
        ProducerDTO producerDTO1 = new ProducerDTO();
        producerDTO1.setId(1L);
        ProducerDTO producerDTO2 = new ProducerDTO();
        assertThat(producerDTO1).isNotEqualTo(producerDTO2);
        producerDTO2.setId(producerDTO1.getId());
        assertThat(producerDTO1).isEqualTo(producerDTO2);
        producerDTO2.setId(2L);
        assertThat(producerDTO1).isNotEqualTo(producerDTO2);
        producerDTO1.setId(null);
        assertThat(producerDTO1).isNotEqualTo(producerDTO2);
    }
}
