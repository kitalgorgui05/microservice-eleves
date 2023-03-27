package com.memoire.kital.raph.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class EleveDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(EleveDTO.class);
        EleveDTO eleveDTO1 = new EleveDTO();
        eleveDTO1.setId(null);
        EleveDTO eleveDTO2 = new EleveDTO();
        assertThat(eleveDTO1).isNotEqualTo(eleveDTO2);
        eleveDTO2.setId(eleveDTO1.getId());
        assertThat(eleveDTO1).isEqualTo(eleveDTO2);
        eleveDTO2.setId(null);
        assertThat(eleveDTO1).isNotEqualTo(eleveDTO2);
        eleveDTO1.setId(null);
        assertThat(eleveDTO1).isNotEqualTo(eleveDTO2);
    }
}
