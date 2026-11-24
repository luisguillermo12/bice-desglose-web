package cl.tutorial.desglose.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A UniqueSessionControl.
 */
@Entity
@Table(name = "UNIQUE_SESSION_CONTROL_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class UniqueSessionControl implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "LOGIN_NAME", nullable = false)
    private String loginName;

    @Column(name = "SESSION_CHECK_TIME")
    private ZonedDateTime sessionCheckTime;

    @Column(name = "SESSION_END_TIME")
    private ZonedDateTime sessionEndTime;

    @NotNull
    @Column(name = "JWT", nullable = false)
    private String jwt;

    @Column(name = "SESSION_LAST")
    private ZonedDateTime sessionLast;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public UniqueSessionControl loginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public ZonedDateTime getSessionCheckTime() {
        return sessionCheckTime;
    }

    public UniqueSessionControl sessionCheckTime(ZonedDateTime sessionCheckTime) {
        this.sessionCheckTime = sessionCheckTime;
        return this;
    }

    public void setSessionCheckTime(ZonedDateTime sessionCheckTime) {
        this.sessionCheckTime = sessionCheckTime;
    }

    public ZonedDateTime getSessionEndTime() {
        return sessionEndTime;
    }

    public UniqueSessionControl sessionEndTime(ZonedDateTime sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
        return this;
    }

    public void setSessionEndTime(ZonedDateTime sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getJwt() {
        return jwt;
    }

    public UniqueSessionControl jwt(String jwt) {
        this.jwt = jwt;
        return this;
    }

    public ZonedDateTime getSessionLast() {
        return sessionLast;
    }

    public void setSessionLast(ZonedDateTime sessionLast) {
        this.sessionLast = sessionLast;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UniqueSessionControl uniqueSessionControl = (UniqueSessionControl) o;
        if (uniqueSessionControl.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), uniqueSessionControl.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UniqueSessionControl{" + "id=" + id + ", loginName='" + loginName + '\'' + ", sessionCheckTime=" + sessionCheckTime + ", sessionEndTime=" + sessionEndTime
            + ", jwt='" + jwt + '\'' + ", sessionLast='" + sessionLast + '\'' + '}';
    }
}
