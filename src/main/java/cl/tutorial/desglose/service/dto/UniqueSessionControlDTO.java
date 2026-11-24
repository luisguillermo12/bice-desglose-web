package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the UniqueSessionControl entity.
 */
public class UniqueSessionControlDTO implements Serializable {

    private Long id;

    @NotNull
    private String loginName;

    private ZonedDateTime sessionCheckTime;

    private ZonedDateTime sessionEndTime;

    @NotNull
    private String jwt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public ZonedDateTime getSessionCheckTime() {
        return sessionCheckTime;
    }

    public void setSessionCheckTime(ZonedDateTime sessionCheckTime) {
        this.sessionCheckTime = sessionCheckTime;
    }

    public ZonedDateTime getSessionEndTime() {
        return sessionEndTime;
    }

    public void setSessionEndTime(ZonedDateTime sessionEndTime) {
        this.sessionEndTime = sessionEndTime;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        UniqueSessionControlDTO uniqueSessionControlDTO = (UniqueSessionControlDTO) o;
        if (uniqueSessionControlDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), uniqueSessionControlDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UniqueSessionControlDTO{" + "id=" + getId() + ", loginName='" + getLoginName() + "'" + ", sessionCheckTime='" + getSessionCheckTime() + "'" + ", sessionEndTime='"
            + getSessionEndTime() + "'" + ", jwt='" + getJwt() + "'" + "}";
    }
}
