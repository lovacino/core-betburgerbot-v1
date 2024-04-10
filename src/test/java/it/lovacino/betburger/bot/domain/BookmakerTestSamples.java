package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookmakerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Bookmaker getBookmakerSample1() {
        return new Bookmaker().id(1L).name("name1");
    }

    public static Bookmaker getBookmakerSample2() {
        return new Bookmaker().id(2L).name("name2");
    }

    public static Bookmaker getBookmakerRandomSampleGenerator() {
        return new Bookmaker().id(longCount.incrementAndGet()).name(UUID.randomUUID().toString());
    }
}
