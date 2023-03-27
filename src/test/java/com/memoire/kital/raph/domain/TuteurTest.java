package com.memoire.kital.raph.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class TuteurTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tuteur.class);
        Tuteur tuteur1 = new Tuteur();
        tuteur1.setId(null);
        Tuteur tuteur2 = new Tuteur();
        tuteur2.setId(tuteur1.getId());
        assertThat(tuteur1).isEqualTo(tuteur2);
        tuteur2.setId(null);
        assertThat(tuteur1).isNotEqualTo(tuteur2);
        tuteur1.setId(null);
        assertThat(tuteur1).isNotEqualTo(tuteur2);
    }
}
