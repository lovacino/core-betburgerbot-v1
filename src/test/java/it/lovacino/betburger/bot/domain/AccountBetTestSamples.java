package it.lovacino.betburger.bot.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AccountBetTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AccountBet getAccountBetSample1() {
        return new AccountBet()
            .id(1L)
            .name("name1")
            .hourActiveActive(1)
            .hourActiveEnd(1)
            .whatsAppNumber("whatsAppNumber1")
            .userAccount("userAccount1")
            .passwordAccount("passwordAccount1");
    }

    public static AccountBet getAccountBetSample2() {
        return new AccountBet()
            .id(2L)
            .name("name2")
            .hourActiveActive(2)
            .hourActiveEnd(2)
            .whatsAppNumber("whatsAppNumber2")
            .userAccount("userAccount2")
            .passwordAccount("passwordAccount2");
    }

    public static AccountBet getAccountBetRandomSampleGenerator() {
        return new AccountBet()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .hourActiveActive(intCount.incrementAndGet())
            .hourActiveEnd(intCount.incrementAndGet())
            .whatsAppNumber(UUID.randomUUID().toString())
            .userAccount(UUID.randomUUID().toString())
            .passwordAccount(UUID.randomUUID().toString());
    }
}
