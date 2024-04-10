package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventSourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static EventSource getEventSourceSample1() {
        return new EventSource()
            .id(1L)
            .home("home1")
            .away("away1")
            .league("league1")
            .eventName("eventName1")
            .bookmakerEventId(1L)
            .betBurgerId("betBurgerId1");
    }

    public static EventSource getEventSourceSample2() {
        return new EventSource()
            .id(2L)
            .home("home2")
            .away("away2")
            .league("league2")
            .eventName("eventName2")
            .bookmakerEventId(2L)
            .betBurgerId("betBurgerId2");
    }

    public static EventSource getEventSourceRandomSampleGenerator() {
        return new EventSource()
            .id(longCount.incrementAndGet())
            .home(UUID.randomUUID().toString())
            .away(UUID.randomUUID().toString())
            .league(UUID.randomUUID().toString())
            .eventName(UUID.randomUUID().toString())
            .bookmakerEventId(longCount.incrementAndGet())
            .betBurgerId(UUID.randomUUID().toString());
    }
}
