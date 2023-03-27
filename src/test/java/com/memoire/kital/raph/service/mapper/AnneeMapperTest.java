package com.memoire.kital.raph.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class AnneeMapperTest {

    private AnneeMapper anneeMapper;

    @BeforeEach
    public void setUp() {
        anneeMapper = new AnneeMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = null;
        assertThat(anneeMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(anneeMapper.fromId(null)).isNull();
    }
}
