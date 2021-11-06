package com.mycompany.inventory.lab.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.inventory.lab.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EqTagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EqTag.class);
        EqTag eqTag1 = new EqTag();
        eqTag1.setId(1L);
        EqTag eqTag2 = new EqTag();
        eqTag2.setId(eqTag1.getId());
        assertThat(eqTag1).isEqualTo(eqTag2);
        eqTag2.setId(2L);
        assertThat(eqTag1).isNotEqualTo(eqTag2);
        eqTag1.setId(null);
        assertThat(eqTag1).isNotEqualTo(eqTag2);
    }
}
