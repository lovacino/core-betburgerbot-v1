package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BetTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BetType getBetTypeSample1() {
        return new BetType().id(1L).name("name1");
    }

    public static BetType getBetTypeSample2() {
        return new BetType().id(2L).name("name2");
    }

    public static BetType getBetTypeRandomSampleGenerator() {
        return new BetType().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
