package com.memoire.kital.raph.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class EleveMapperTest {

    private EleveMapper eleveMapper;

    @BeforeEach
    public void setUp() {
        eleveMapper = new EleveMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = null;
        assertThat(eleveMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(eleveMapper.fromId(null)).isNull();
    }
}
