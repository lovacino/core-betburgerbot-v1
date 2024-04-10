package it.lovacino.betburger.bot.domain;

import static it.lovacino.betburger.bot.domain.BetTypeTestSamples.*;
import static it.lovacino.betburger.bot.domain.BookmakerTestSamples.*;
import static it.lovacino.betburger.bot.domain.EventSourceTestSamples.*;
import static it.lovacino.betburger.bot.domain.PeriodTestSamples.*;
import static it.lovacino.betburger.bot.domain.SportTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import it.lovacino.betburger.bot.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class EventSourceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(EventSource.class);
        EventSource eventSource1 = getEventSourceSample1();
        EventSource eventSource2 = new EventSource();
        assertThat(eventSource1).isNotEqualTo(eventSource2);

        eventSource2.setId(eventSource1.getId());
        assertThat(eventSource1).isEqualTo(eventSource2);

        eventSource2 = getEventSourceSample2();
        assertThat(eventSource1).isNotEqualTo(eventSource2);
    }

    @Test
    void bookmakerTest() throws Exception {
        EventSource eventSource = getEventSourceRandomSampleGenerator();
        Bookmaker bookmakerBack = getBookmakerRandomSampleGenerator();

        eventSource.setBookmaker(bookmakerBack);
        assertThat(eventSource.getBookmaker()).isEqualTo(bookmakerBack);

        eventSource.bookmaker(null);
        assertThat(eventSource.getBookmaker()).isNull();
    }

    @Test
    void sportTest() throws Exception {
        EventSource eventSource = getEventSourceRandomSampleGenerator();
        Sport sportBack = getSportRandomSampleGenerator();

        eventSource.setSport(sportBack);
        assertThat(eventSource.getSport()).isEqualTo(sportBack);

        eventSource.sport(null);
        assertThat(eventSource.getSport()).isNull();
    }

    @Test
    void periodTest() throws Exception {
        EventSource eventSource = getEventSourceRandomSampleGenerator();
        Period periodBack = getPeriodRandomSampleGenerator();

        eventSource.setPeriod(periodBack);
        assertThat(eventSource.getPeriod()).isEqualTo(periodBack);

        eventSource.period(null);
        assertThat(eventSource.getPeriod()).isNull();
    }

    @Test
    void betTypeTest() throws Exception {
        EventSource eventSource = getEventSourceRandomSampleGenerator();
        BetType betTypeBack = getBetTypeRandomSampleGenerator();

        eventSource.setBetType(betTypeBack);
        assertThat(eventSource.getBetType()).isEqualTo(betTypeBack);

        eventSource.betType(null);
        assertThat(eventSource.getBetType()).isNull();
    }
}
