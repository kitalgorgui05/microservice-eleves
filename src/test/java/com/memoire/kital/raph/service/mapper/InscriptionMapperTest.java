package com.memoire.kital.raph.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class InscriptionMapperTest {

    private InscriptionMapper inscriptionMapper;

    @BeforeEach
    public void setUp() {
        inscriptionMapper = new InscriptionMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = null;
        assertThat(inscriptionMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(inscriptionMapper.fromId(null)).isNull();
    }
}
