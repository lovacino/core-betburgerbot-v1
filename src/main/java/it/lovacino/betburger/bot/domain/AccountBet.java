package it.lovacino.betburger.bot.domain;

import it.lovacino.betburger.bot.domain.enumeration.AccountBetState;
import it.lovacino.betburger.bot.domain.enumeration.AccountBetType;
import it.lovacino.betburger.bot.domain.enumeration.BettingRoleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccountBet.
 */
@Entity
@Table(name = "account_bet")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccountBet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private AccountBetState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private AccountBetType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "betting_role_type")
    private BettingRoleType bettingRoleType;

    @Column(name = "betting_role_amount")
    private Double bettingRoleAmount;

    @Column(name = "hour_active_start")
    private Integer hourActiveStart;

    @Column(name = "hour_active_end")
    private Integer hourActiveEnd;

    @Column(name = "flg_active_lun")
    private Boolean flgActiveLun;

    @Column(name = "flg_active_mar")
    private Boolean flgActiveMar;

    @Column(name = "flg_active_mer")
    private Boolean flgActiveMer;

    @Column(name = "flg_active_gio")
    private Boolean flgActiveGio;

    @Column(name = "flg_active_ven")
    private Boolean flgActiveVen;

    @Column(name = "flg_active_sab")
    private Boolean flgActiveSab;

    @Column(name = "flg_active_dom")
    private Boolean flgActiveDom;

    @Column(name = "whats_app_number")
    private String whatsAppNumber;

    @Column(name = "user_account")
    private String userAccount;

    @Column(name = "password_account")
    private String passwordAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    private Bookmaker bookmaker;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccountBet id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public AccountBet name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccountBetState getState() {
        return this.state;
    }

    public AccountBet state(AccountBetState state) {
        this.setState(state);
        return this;
    }

    public void setState(AccountBetState state) {
        this.state = state;
    }

    public AccountBetType getType() {
        return this.type;
    }

    public AccountBet type(AccountBetType type) {
        this.setType(type);
        return this;
    }

    public void setType(AccountBetType type) {
        this.type = type;
    }

    public BettingRoleType getBettingRoleType() {
        return this.bettingRoleType;
    }

    public AccountBet bettingRoleType(BettingRoleType bettingRoleType) {
        this.setBettingRoleType(bettingRoleType);
        return this;
    }

    public void setBettingRoleType(BettingRoleType bettingRoleType) {
        this.bettingRoleType = bettingRoleType;
    }

    public Double getBettingRoleAmount() {
        return this.bettingRoleAmount;
    }

    public AccountBet bettingRoleAmount(Double bettingRoleAmount) {
        this.setBettingRoleAmount(bettingRoleAmount);
        return this;
    }

    public void setBettingRoleAmount(Double bettingRoleAmount) {
        this.bettingRoleAmount = bettingRoleAmount;
    }

    public Integer getHourActiveStart() {
        return this.hourActiveStart;
    }

    public AccountBet hourActiveStart(Integer hourActiveStart) {
        this.setHourActiveStart(hourActiveStart);
        return this;
    }

    public void setHourActiveStart(Integer hourActiveStart) {
        this.hourActiveStart = hourActiveStart;
    }

    public Integer getHourActiveEnd() {
        return this.hourActiveEnd;
    }

    public AccountBet hourActiveEnd(Integer hourActiveEnd) {
        this.setHourActiveEnd(hourActiveEnd);
        return this;
    }

    public void setHourActiveEnd(Integer hourActiveEnd) {
        this.hourActiveEnd = hourActiveEnd;
    }

    public Boolean getFlgActiveLun() {
        return this.flgActiveLun;
    }

    public AccountBet flgActiveLun(Boolean flgActiveLun) {
        this.setFlgActiveLun(flgActiveLun);
        return this;
    }

    public void setFlgActiveLun(Boolean flgActiveLun) {
        this.flgActiveLun = flgActiveLun;
    }

    public Boolean getFlgActiveMar() {
        return this.flgActiveMar;
    }

    public AccountBet flgActiveMar(Boolean flgActiveMar) {
        this.setFlgActiveMar(flgActiveMar);
        return this;
    }

    public void setFlgActiveMar(Boolean flgActiveMar) {
        this.flgActiveMar = flgActiveMar;
    }

    public Boolean getFlgActiveMer() {
        return this.flgActiveMer;
    }

    public AccountBet flgActiveMer(Boolean flgActiveMer) {
        this.setFlgActiveMer(flgActiveMer);
        return this;
    }

    public void setFlgActiveMer(Boolean flgActiveMer) {
        this.flgActiveMer = flgActiveMer;
    }

    public Boolean getFlgActiveGio() {
        return this.flgActiveGio;
    }

    public AccountBet flgActiveGio(Boolean flgActiveGio) {
        this.setFlgActiveGio(flgActiveGio);
        return this;
    }

    public void setFlgActiveGio(Boolean flgActiveGio) {
        this.flgActiveGio = flgActiveGio;
    }

    public Boolean getFlgActiveVen() {
        return this.flgActiveVen;
    }

    public AccountBet flgActiveVen(Boolean flgActiveVen) {
        this.setFlgActiveVen(flgActiveVen);
        return this;
    }

    public void setFlgActiveVen(Boolean flgActiveVen) {
        this.flgActiveVen = flgActiveVen;
    }

    public Boolean getFlgActiveSab() {
        return this.flgActiveSab;
    }

    public AccountBet flgActiveSab(Boolean flgActiveSab) {
        this.setFlgActiveSab(flgActiveSab);
        return this;
    }

    public void setFlgActiveSab(Boolean flgActiveSab) {
        this.flgActiveSab = flgActiveSab;
    }

    public Boolean getFlgActiveDom() {
        return this.flgActiveDom;
    }

    public AccountBet flgActiveDom(Boolean flgActiveDom) {
        this.setFlgActiveDom(flgActiveDom);
        return this;
    }

    public void setFlgActiveDom(Boolean flgActiveDom) {
        this.flgActiveDom = flgActiveDom;
    }

    public String getWhatsAppNumber() {
        return this.whatsAppNumber;
    }

    public AccountBet whatsAppNumber(String whatsAppNumber) {
        this.setWhatsAppNumber(whatsAppNumber);
        return this;
    }

    public void setWhatsAppNumber(String whatsAppNumber) {
        this.whatsAppNumber = whatsAppNumber;
    }

    public String getUserAccount() {
        return this.userAccount;
    }

    public AccountBet userAccount(String userAccount) {
        this.setUserAccount(userAccount);
        return this;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getPasswordAccount() {
        return this.passwordAccount;
    }

    public AccountBet passwordAccount(String passwordAccount) {
        this.setPasswordAccount(passwordAccount);
        return this;
    }

    public void setPasswordAccount(String passwordAccount) {
        this.passwordAccount = passwordAccount;
    }

    public Bookmaker getBookmaker() {
        return this.bookmaker;
    }

    public void setBookmaker(Bookmaker bookmaker) {
        this.bookmaker = bookmaker;
    }

    public AccountBet bookmaker(Bookmaker bookmaker) {
        this.setBookmaker(bookmaker);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccountBet)) {
            return false;
        }
        return getId() != null && getId().equals(((AccountBet) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccountBet{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", state='" + getState() + "'" +
            ", type='" + getType() + "'" +
            ", bettingRoleType='" + getBettingRoleType() + "'" +
            ", bettingRoleAmount=" + getBettingRoleAmount() +
            ", hourActiveStart=" + getHourActiveStart() +
            ", hourActiveEnd=" + getHourActiveEnd() +
            ", flgActiveLun='" + getFlgActiveLun() + "'" +
            ", flgActiveMar='" + getFlgActiveMar() + "'" +
            ", flgActiveMer='" + getFlgActiveMer() + "'" +
            ", flgActiveGio='" + getFlgActiveGio() + "'" +
            ", flgActiveVen='" + getFlgActiveVen() + "'" +
            ", flgActiveSab='" + getFlgActiveSab() + "'" +
            ", flgActiveDom='" + getFlgActiveDom() + "'" +
            ", whatsAppNumber='" + getWhatsAppNumber() + "'" +
            ", userAccount='" + getUserAccount() + "'" +
            ", passwordAccount='" + getPasswordAccount() + "'" +
            "}";
    }
}
