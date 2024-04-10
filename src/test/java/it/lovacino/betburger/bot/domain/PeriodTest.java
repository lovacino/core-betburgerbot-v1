package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.PeriodTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeriodTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Period.class);
        Period period1 = getPeriodSample1();
        Period period2 = new Period();
        assertThat(period1).isNotEqualTo(period2);

        period2.setId(period1.getId());
        assertThat(period1).isEqualTo(period2);

        period2 = getPeriodSample2();
        assertThat(period1).isNotEqualTo(period2);
    }
}
