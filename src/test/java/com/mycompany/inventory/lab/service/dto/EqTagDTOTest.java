package com.mycompany.inventory.lab.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.inventory.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EqTagDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EqTagDTO.class);
        EqTagDTO eqTagDTO1 = new EqTagDTO();
        eqTagDTO1.setId(1L);
        EqTagDTO eqTagDTO2 = new EqTagDTO();
        assertThat(eqTagDTO1).isNotEqualTo(eqTagDTO2);
        eqTagDTO2.setId(eqTagDTO1.getId());
        assertThat(eqTagDTO1).isEqualTo(eqTagDTO2);
        eqTagDTO2.setId(2L);
        assertThat(eqTagDTO1).isNotEqualTo(eqTagDTO2);
        eqTagDTO1.setId(null);
        assertThat(eqTagDTO1).isNotEqualTo(eqTagDTO2);
    }
}
