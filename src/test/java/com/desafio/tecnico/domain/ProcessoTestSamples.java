package com.desafio.tecnico.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProcessoTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Processo getProcessoSample1() {
        return new Processo().id(1L).npu("npu1").municipio("municipio1").uf("uf1");
    }

    public static Processo getProcessoSample2() {
        return new Processo().id(2L).npu("npu2").municipio("municipio2").uf("uf2");
    }

    public static Processo getProcessoRandomSampleGenerator() {
        return new Processo()
            .id(longCount.incrementAndGet())
            .npu(UUID.randomUUID().toString())
            .municipio(UUID.randomUUID().toString())
            .uf(UUID.randomUUID().toString());
    }
}
