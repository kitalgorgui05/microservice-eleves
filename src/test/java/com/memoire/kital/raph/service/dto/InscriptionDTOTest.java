package com.memoire.kital.raph.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class InscriptionDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InscriptionDTO.class);
        InscriptionDTO inscriptionDTO1 = new InscriptionDTO();
        inscriptionDTO1.setId(null);
        InscriptionDTO inscriptionDTO2 = new InscriptionDTO();
        assertThat(inscriptionDTO1).isNotEqualTo(inscriptionDTO2);
        inscriptionDTO2.setId(inscriptionDTO1.getId());
        assertThat(inscriptionDTO1).isEqualTo(inscriptionDTO2);
        inscriptionDTO2.setId(null);
        assertThat(inscriptionDTO1).isNotEqualTo(inscriptionDTO2);
        inscriptionDTO1.setId(null);
        assertThat(inscriptionDTO1).isNotEqualTo(inscriptionDTO2);
    }
}
