package com.desafio.tecnico.service.mapper;

import static com.desafio.tecnico.domain.ProcessoAsserts.*;
import static com.desafio.tecnico.domain.ProcessoTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProcessoMapperTest {

    private ProcessoMapper processoMapper;

    @BeforeEach
    void setUp() {
        processoMapper = new ProcessoMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProcessoSample1();
        var actual = processoMapper.toEntity(processoMapper.toDto(expected));
        assertProcessoAllPropertiesEquals(expected, actual);
    }
}
