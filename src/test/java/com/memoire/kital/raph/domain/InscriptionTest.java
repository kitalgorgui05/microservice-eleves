package com.memoire.kital.raph.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.memoire.kital.raph.web.rest.TestUtil;

public class InscriptionTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Inscription.class);
        Inscription inscription1 = new Inscription();
        inscription1.setId(null);
        Inscription inscription2 = new Inscription();
        inscription2.setId(inscription1.getId());
        assertThat(inscription1).isEqualTo(inscription2);
        inscription2.setId(null);
        assertThat(inscription1).isNotEqualTo(inscription2);
        inscription1.setId(null);
        assertThat(inscription1).isNotEqualTo(inscription2);
    }
}
