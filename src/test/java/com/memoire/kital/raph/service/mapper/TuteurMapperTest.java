package com.memoire.kital.raph.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class TuteurMapperTest {

    private TuteurMapper tuteurMapper;

    @BeforeEach
    public void setUp() {
        tuteurMapper = new TuteurMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        String id = null;
        assertThat(tuteurMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(tuteurMapper.fromId(null)).isNull();
    }
}
