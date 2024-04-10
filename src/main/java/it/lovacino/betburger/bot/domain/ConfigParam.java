package it.lovacino.betburger.bot.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ConfigParam.
 */
@Entity
@Table(name = "config_param")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ConfigParam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "param_name", unique = true)
    private String paramName;

    @Column(name = "param_value")
    private String paramValue;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ConfigParam id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getParamName() {
        return this.paramName;
    }

    public ConfigParam paramName(String paramName) {
        this.setParamName(paramName);
        return this;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return this.paramValue;
    }

    public ConfigParam paramValue(String paramValue) {
        this.setParamValue(paramValue);
        return this;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConfigParam)) {
            return false;
        }
        return getId() != null && getId().equals(((ConfigParam) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ConfigParam{" +
            "id=" + getId() +
            ", paramName='" + getParamName() + "'" +
            ", paramValue='" + getParamValue() + "'" +
            "}";
    }
}
