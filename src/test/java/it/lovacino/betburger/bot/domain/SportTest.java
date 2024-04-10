package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.SportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Sport.class);
        Sport sport1 = getSportSample1();
        Sport sport2 = new Sport();
        assertThat(sport1).isNotEqualTo(sport2);

        sport2.setId(sport1.getId());
        assertThat(sport1).isEqualTo(sport2);

        sport2 = getSportSample2();
        assertThat(sport1).isNotEqualTo(sport2);
    }
}
