package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PeriodTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Period getPeriodSample1() {
        return new Period().id(1L).name("name1");
    }

    public static Period getPeriodSample2() {
        return new Period().id(2L).name("name2");
    }

    public static Period getPeriodRandomSampleGenerator() {
        return new Period().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
