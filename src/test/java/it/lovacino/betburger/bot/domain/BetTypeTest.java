package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.BetTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BetTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BetType.class);
        BetType betType1 = getBetTypeSample1();
        BetType betType2 = new BetType();
        assertThat(betType1).isNotEqualTo(betType2);

        betType2.setId(betType1.getId());
        assertThat(betType1).isEqualTo(betType2);

        betType2 = getBetTypeSample2();
        assertThat(betType1).isNotEqualTo(betType2);
    }
}
