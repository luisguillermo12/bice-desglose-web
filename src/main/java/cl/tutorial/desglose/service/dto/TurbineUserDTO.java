package cl.tutorial.desglose.service.dto;

import org.apache.commons.lang.WordUtils;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A DTO for the TurbineUser entity.
 */
public class TurbineUserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private String loginName;

    @NotNull
    private String passwordValue;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    private String email;

    private String confirmValue;

    private LocalDate modified;

    private LocalDate created;

    private ZonedDateTime lastLogin;

    private LocalDate expire;

    private LocalDate expirePassword;

    @Lob
    private byte[] objectdata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmValue() {
        return confirmValue;
    }

    public void setConfirmValue(String confirmValue) {
        this.confirmValue = confirmValue;
    }

    public LocalDate getModified() {
        return modified;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public ZonedDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(ZonedDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    public LocalDate getExpirePassword() {
        return expirePassword;
    }

    public void setExpirePassword(LocalDate expirePassword) {
        this.expirePassword = expirePassword;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public void setObjectdata(byte[] objectdata) {
        this.objectdata = objectdata;
    }

    public String getFullName() {
        return WordUtils.capitalizeFully((this.firstName != null ? this.firstName : "") + (this.lastName != null ? ((this.firstName != null ? " " : "") + this.lastName) : ""));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurbineUserDTO turbineUserDTO = (TurbineUserDTO) o;
        if (turbineUserDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turbineUserDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurbineUserDTO{" + "id=" + id + ", userId=" + userId + ", loginName='" + loginName + '\'' + ", passwordValue='" + passwordValue + '\'' + ", firstName='" + firstName
            + '\'' + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", confirmValue='" + confirmValue + '\'' + ", modified=" + modified + ", created=" + created
            + ", lastLogin=" + lastLogin + ", expire=" + expire + ", expirePassword=" + expirePassword + '}';
    }

}
