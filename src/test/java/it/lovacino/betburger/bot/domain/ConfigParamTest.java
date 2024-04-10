package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.ConfigParamTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigParamTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigParam.class);
        ConfigParam configParam1 = getConfigParamSample1();
        ConfigParam configParam2 = new ConfigParam();
        assertThat(configParam1).isNotEqualTo(configParam2);

        configParam2.setId(configParam1.getId());
        assertThat(configParam1).isEqualTo(configParam2);

        configParam2 = getConfigParamSample2();
        assertThat(configParam1).isNotEqualTo(configParam2);
    }
}
