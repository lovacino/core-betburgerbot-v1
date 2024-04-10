package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.AccountBetTestSamples.*;
import static it.lovacino.betburger.bot.domain.BetTypeTestSamples.*;
import static it.lovacino.betburger.bot.domain.BettingTestSamples.*;
import static it.lovacino.betburger.bot.domain.PeriodTestSamples.*;
import static it.lovacino.betburger.bot.domain.SportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Betting.class);
        Betting betting1 = getBettingSample1();
        Betting betting2 = new Betting();
        assertThat(betting1).isNotEqualTo(betting2);

        betting2.setId(betting1.getId());
        assertThat(betting1).isEqualTo(betting2);

        betting2 = getBettingSample2();
        assertThat(betting1).isNotEqualTo(betting2);
    }

    @Test
    void accountTest() throws Exception {
        Betting betting = getBettingRandomSampleGenerator();
        AccountBet accountBetBack = getAccountBetRandomSampleGenerator();

        betting.setAccount(accountBetBack);
        assertThat(betting.getAccount()).isEqualTo(accountBetBack);

        betting.account(null);
        assertThat(betting.getAccount()).isNull();
    }

    @Test
    void sportTest() throws Exception {
        Betting betting = getBettingRandomSampleGenerator();
        Sport sportBack = getSportRandomSampleGenerator();

        betting.setSport(sportBack);
        assertThat(betting.getSport()).isEqualTo(sportBack);

        betting.sport(null);
        assertThat(betting.getSport()).isNull();
    }

    @Test
    void periodTest() throws Exception {
        Betting betting = getBettingRandomSampleGenerator();
        Period periodBack = getPeriodRandomSampleGenerator();

        betting.setPeriod(periodBack);
        assertThat(betting.getPeriod()).isEqualTo(periodBack);

        betting.period(null);
        assertThat(betting.getPeriod()).isNull();
    }

    @Test
    void betTypeTest() throws Exception {
        Betting betting = getBettingRandomSampleGenerator();
        BetType betTypeBack = getBetTypeRandomSampleGenerator();

        betting.setBetType(betTypeBack);
        assertThat(betting.getBetType()).isEqualTo(betTypeBack);

        betting.betType(null);
        assertThat(betting.getBetType()).isNull();
    }
}
