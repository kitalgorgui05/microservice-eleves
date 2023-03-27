package com.memoire.kital.raph.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class AnneeTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Annee.class);
        Annee annee1 = new Annee();
        annee1.setId(null);
        Annee annee2 = new Annee();
        annee2.setId(annee1.getId());
        assertThat(annee1).isEqualTo(annee2);
        annee2.setId(null);
        assertThat(annee1).isNotEqualTo(annee2);
        annee1.setId(null);
        assertThat(annee1).isNotEqualTo(annee2);
    }
}
