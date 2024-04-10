package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.BookmakerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookmakerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bookmaker.class);
        Bookmaker bookmaker1 = getBookmakerSample1();
        Bookmaker bookmaker2 = new Bookmaker();
        assertThat(bookmaker1).isNotEqualTo(bookmaker2);

        bookmaker2.setId(bookmaker1.getId());
        assertThat(bookmaker1).isEqualTo(bookmaker2);

        bookmaker2 = getBookmakerSample2();
        assertThat(bookmaker1).isNotEqualTo(bookmaker2);
    }
}
