package com.memoire.kital.raph.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class TuteurDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(TuteurDTO.class);
        TuteurDTO tuteurDTO1 = new TuteurDTO();
        tuteurDTO1.setId(null);
        TuteurDTO tuteurDTO2 = new TuteurDTO();
        assertThat(tuteurDTO1).isNotEqualTo(tuteurDTO2);
        tuteurDTO2.setId(tuteurDTO1.getId());
        assertThat(tuteurDTO1).isEqualTo(tuteurDTO2);
        tuteurDTO2.setId(null);
        assertThat(tuteurDTO1).isNotEqualTo(tuteurDTO2);
        tuteurDTO1.setId(null);
        assertThat(tuteurDTO1).isNotEqualTo(tuteurDTO2);
    }
}
