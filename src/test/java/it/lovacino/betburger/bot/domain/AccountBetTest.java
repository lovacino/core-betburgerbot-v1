package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.AccountBetTestSamples.*;
import static it.lovacino.betburger.bot.domain.BookmakerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccountBetTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccountBet.class);
        AccountBet accountBet1 = getAccountBetSample1();
        AccountBet accountBet2 = new AccountBet();
        assertThat(accountBet1).isNotEqualTo(accountBet2);

        accountBet2.setId(accountBet1.getId());
        assertThat(accountBet1).isEqualTo(accountBet2);

        accountBet2 = getAccountBetSample2();
        assertThat(accountBet1).isNotEqualTo(accountBet2);
    }

    @Test
    void bookmakerTest() throws Exception {
        AccountBet accountBet = getAccountBetRandomSampleGenerator();
        Bookmaker bookmakerBack = getBookmakerRandomSampleGenerator();

        accountBet.setBookmaker(bookmakerBack);
        assertThat(accountBet.getBookmaker()).isEqualTo(bookmakerBack);

        accountBet.bookmaker(null);
        assertThat(accountBet.getBookmaker()).isNull();
    }
}
