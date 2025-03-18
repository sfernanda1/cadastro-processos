package com.desafio.tecnico.domain;

import static com.desafio.tecnico.domain.ProcessoTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.desafio.tecnico.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProcessoTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Processo.class);
        Processo processo1 = getProcessoSample1();
        Processo processo2 = new Processo();
        assertThat(processo1).isNotEqualTo(processo2);

        processo2.setId(processo1.getId());
        assertThat(processo1).isEqualTo(processo2);

        processo2 = getProcessoSample2();
        assertThat(processo1).isNotEqualTo(processo2);
    }
}
