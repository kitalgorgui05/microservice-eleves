package com.memoire.kital.raph.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class AnneeDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnneeDTO.class);
        AnneeDTO anneeDTO1 = new AnneeDTO();
        anneeDTO1.setId(null);
        AnneeDTO anneeDTO2 = new AnneeDTO();
        assertThat(anneeDTO1).isNotEqualTo(anneeDTO2);
        anneeDTO2.setId(anneeDTO1.getId());
        assertThat(anneeDTO1).isEqualTo(anneeDTO2);
        anneeDTO2.setId(null);
        assertThat(anneeDTO1).isNotEqualTo(anneeDTO2);
        anneeDTO1.setId(null);
        assertThat(anneeDTO1).isNotEqualTo(anneeDTO2);
    }
}
