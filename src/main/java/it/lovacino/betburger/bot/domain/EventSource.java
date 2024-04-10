package it.lovacino.betburger.bot.domain;

import it.lovacino.betburger.bot.domain.enumeration.BetArrow;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A EventSource.
 */
@Entity
@Table(name = "event_source")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class EventSource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "home")
    private String home;

    @Column(name = "away")
    private String away;

    @Column(name = "league")
    private String league;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "bookmaker_event_id")
    private Long bookmakerEventId;

    @Column(name = "bet_type_param")
    private Double betTypeParam;

    @Column(name = "koef_last_modified_at")
    private ZonedDateTime koefLastModifiedAt;

    @Column(name = "scanned_at")
    private ZonedDateTime scannedAt;

    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @Column(name = "bet_burger_id")
    private String betBurgerId;

    @Column(name = "koef")
    private Double koef;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bookmaker bookmaker;

    @ManyToOne(fetch = FetchType.LAZY)
    private Sport sport;

    @ManyToOne(fetch = FetchType.LAZY)
    private Period period;

    @ManyToOne(fetch = FetchType.LAZY)
    private BetType betType;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrow")
    private BetArrow arrow;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public EventSource id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHome() {
        return this.home;
    }

    public EventSource home(String home) {
        this.setHome(home);
        return this;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return this.away;
    }

    public EventSource away(String away) {
        this.setAway(away);
        return this;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public String getLeague() {
        return this.league;
    }

    public EventSource league(String league) {
        this.setLeague(league);
        return this;
    }

    public void setLeague(String league) {
        this.league = league;
    }

    public String getEventName() {
        return this.eventName;
    }

    public EventSource eventName(String eventName) {
        this.setEventName(eventName);
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Long getBookmakerEventId() {
        return this.bookmakerEventId;
    }

    public EventSource bookmakerEventId(Long bookmakerEventId) {
        this.setBookmakerEventId(bookmakerEventId);
        return this;
    }

    public void setBookmakerEventId(Long bookmakerEventId) {
        this.bookmakerEventId = bookmakerEventId;
    }

    public Double getBetTypeParam() {
        return this.betTypeParam;
    }

    public EventSource betTypeParam(Double betTypeParam) {
        this.setBetTypeParam(betTypeParam);
        return this;
    }

    public void setBetTypeParam(Double betTypeParam) {
        this.betTypeParam = betTypeParam;
    }

    public ZonedDateTime getKoefLastModifiedAt() {
        return this.koefLastModifiedAt;
    }

    public EventSource koefLastModifiedAt(ZonedDateTime koefLastModifiedAt) {
        this.setKoefLastModifiedAt(koefLastModifiedAt);
        return this;
    }

    public void setKoefLastModifiedAt(ZonedDateTime koefLastModifiedAt) {
        this.koefLastModifiedAt = koefLastModifiedAt;
    }

    public ZonedDateTime getScannedAt() {
        return this.scannedAt;
    }

    public EventSource scannedAt(ZonedDateTime scannedAt) {
        this.setScannedAt(scannedAt);
        return this;
    }

    public void setScannedAt(ZonedDateTime scannedAt) {
        this.scannedAt = scannedAt;
    }

    public ZonedDateTime getStartedAt() {
        return this.startedAt;
    }

    public EventSource startedAt(ZonedDateTime startedAt) {
        this.setStartedAt(startedAt);
        return this;
    }

    public void setStartedAt(ZonedDateTime startedAt) {
        this.startedAt = startedAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    public EventSource updatedAt(ZonedDateTime updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getBetBurgerId() {
        return this.betBurgerId;
    }

    public EventSource betBurgerId(String betBurgerId) {
        this.setBetBurgerId(betBurgerId);
        return this;
    }

    public void setBetBurgerId(String betBurgerId) {
        this.betBurgerId = betBurgerId;
    }

    public Double getKoef() {
        return this.koef;
    }

    public EventSource koef(Double koef) {
        this.setKoef(koef);
        return this;
    }

    public void setKoef(Double koef) {
        this.koef = koef;
    }

    public Bookmaker getBookmaker() {
        return this.bookmaker;
    }

    public void setBookmaker(Bookmaker bookmaker) {
        this.bookmaker = bookmaker;
    }

    public EventSource bookmaker(Bookmaker bookmaker) {
        this.setBookmaker(bookmaker);
        return this;
    }

    public Sport getSport() {
        return this.sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public EventSource sport(Sport sport) {
        this.setSport(sport);
        return this;
    }

    public Period getPeriod() {
        return this.period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

    public EventSource period(Period period) {
        this.setPeriod(period);
        return this;
    }

    public BetType getBetType() {
        return this.betType;
    }

    public void setBetType(BetType betType) {
        this.betType = betType;
    }

    public EventSource betType(BetType betType) {
        this.setBetType(betType);
        return this;
    }

    public BetArrow getArrow() {
        return arrow;
    }

    public void setArrow(BetArrow arrow) {
        this.arrow = arrow;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof EventSource)) {
            return false;
        }
        return getId() != null && getId().equals(((EventSource) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "EventSource{" +
            "id=" + getId() +
            ", home='" + getHome() + "'" +
            ", away='" + getAway() + "'" +
            ", league='" + getLeague() + "'" +
            ", eventName='" + getEventName() + "'" +
            ", bookmakerEventId=" + getBookmakerEventId() +
            ", betTypeParam=" + getBetTypeParam() +
            ", koefLastModifiedAt='" + getKoefLastModifiedAt() + "'" +
            ", scannedAt='" + getScannedAt() + "'" +
            ", startedAt='" + getStartedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            ", betBurgerId='" + getBetBurgerId() + "'" +
            ", koef=" + getKoef() +
            "}";
    }
}
