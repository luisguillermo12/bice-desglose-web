package cl.tutorial.desglose.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A BlackListJwt.
 */
@Entity
@Table(name = "BLACK_LIST_JWT_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BlackListJwt implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(name = "INVALIDATE_TIME", nullable = false)
    private ZonedDateTime invalidateTime;

    @NotNull
    @Column(name = "JWT", nullable = false)
    private String jwt;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getInvalidateTime() {
        return invalidateTime;
    }

    public BlackListJwt invalidateTime(ZonedDateTime invalidateTime) {
        this.invalidateTime = invalidateTime;
        return this;
    }

    public void setInvalidateTime(ZonedDateTime invalidateTime) {
        this.invalidateTime = invalidateTime;
    }

    public String getJwt() {
        return jwt;
    }

    public BlackListJwt jwt(String jwt) {
        this.jwt = jwt;
        return this;
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
        BlackListJwt blackListJwt = (BlackListJwt) o;
        if (blackListJwt.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), blackListJwt.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BlackListJwt{" + "id=" + getId() + ", invalidateTime='" + getInvalidateTime() + "'" + ", jwt='" + getJwt() + "'" + "}";
    }
}
