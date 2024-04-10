package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ConfigParamTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ConfigParam getConfigParamSample1() {
        return new ConfigParam().id(1L).paramName("paramName1").paramValue("paramValue1");
    }

    public static ConfigParam getConfigParamSample2() {
        return new ConfigParam().id(2L).paramName("paramName2").paramValue("paramValue2");
    }

    public static ConfigParam getConfigParamRandomSampleGenerator() {
        return new ConfigParam()
            .id(longCount.incrementAndGet())
            .paramName(UUID.randomUUID().toString())
            .paramValue(UUID.randomUUID().toString());
    }
}
