package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BettingTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Betting getBettingSample1() {
        return new Betting()
            .id(1L)
            .eventSourceId(1L)
            .home("home1")
            .away("away1")
            .league("league1")
            .eventName("eventName1")
            .betBurgerId("betBurgerId1");
    }

    public static Betting getBettingSample2() {
        return new Betting()
            .id(2L)
            .eventSourceId(2L)
            .home("home2")
            .away("away2")
            .league("league2")
            .eventName("eventName2")
            .betBurgerId("betBurgerId2");
    }

    public static Betting getBettingRandomSampleGenerator() {
        return new Betting()
            .id(longCount.incrementAndGet())
            .eventSourceId(longCount.incrementAndGet())
            .home(UUID.randomUUID().toString())
            .away(UUID.randomUUID().toString())
            .league(UUID.randomUUID().toString())
            .eventName(UUID.randomUUID().toString())
            .betBurgerId(UUID.randomUUID().toString());
    }
}
