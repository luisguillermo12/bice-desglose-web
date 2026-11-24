package cl.tutorial.desglose.conexiones.sfdesglose.domain;

import io.swagger.annotations.ApiModel;
import org.apache.commons.lang.WordUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Objects;

/**
 * Representa la tabla \"TURBINE_USER\" que contiene usuarios del banco. Pertenece al esquema SF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TURBINE_USER\" que contiene usuarios del banco. Pertenece al esquema SF_ADMIN. @author Antonio Marrero Palomino")
@Entity
@Table(name = "TURBINE_USER")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurbineUser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TURBINE_USER_SEQ")
    @SequenceGenerator(name = "TURBINE_USER_SEQ", sequenceName = "TURBINE_USER_SEQ")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @NotNull
    @Column(name = "login_name", nullable = false, unique = true)
    private String loginName;

    @NotNull
    @Column(name = "password_value", nullable = false)
    private String passwordValue;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "confirm_value")
    private String confirmValue;

    @Column(name = "modified")
    private LocalDate modified;

    @Column(name = "created")
    private LocalDate created;

    @Column(name = "last_login")
    private ZonedDateTime lastLogin;

    @Column(name = "expire")
    private LocalDate expire;

    @Column(name = "expire_password")
    private LocalDate expirePassword;

//    @Lob
//    @Column(name = "objectdata")
    @Transient
    private byte[] objectdata;

    // @Column(name = "objectdata_content_type")
    @Transient
    private String objectdataContentType;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getUserId() {
        return userId;
    }

    public TurbineUser userId(Long userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public TurbineUser loginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPasswordValue() {
        return passwordValue;
    }

    public TurbineUser passwordValue(String passwordValue) {
        this.passwordValue = passwordValue;
        return this;
    }

    public void setPasswordValue(String passwordValue) {
        this.passwordValue = passwordValue;
    }

    public String getFirstName() {
        return firstName;
    }

    public TurbineUser firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public TurbineUser lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public TurbineUser email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getConfirmValue() {
        return confirmValue;
    }

    public TurbineUser confirmValue(String confirmValue) {
        this.confirmValue = confirmValue;
        return this;
    }

    public void setConfirmValue(String confirmValue) {
        this.confirmValue = confirmValue;
    }

    public LocalDate getModified() {
        return modified;
    }

    public TurbineUser modified(LocalDate modified) {
        this.modified = modified;
        return this;
    }

    public void setModified(LocalDate modified) {
        this.modified = modified;
    }

    public LocalDate getCreated() {
        return created;
    }

    public TurbineUser created(LocalDate created) {
        this.created = created;
        return this;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public ZonedDateTime getLastLogin() {
        return lastLogin;
    }

    public TurbineUser lastLogin(ZonedDateTime lastLogin) {
        this.lastLogin = lastLogin;
        return this;
    }

    public void setLastLogin(ZonedDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public LocalDate getExpire() {
        return expire;
    }

    public TurbineUser expire(LocalDate expire) {
        this.expire = expire;
        return this;
    }

    public void setExpire(LocalDate expire) {
        this.expire = expire;
    }

    public LocalDate getExpirePassword() {
        return expirePassword;
    }

    public TurbineUser expirePassword(LocalDate expirePassword) {
        this.expirePassword = expirePassword;
        return this;
    }

    public void setExpirePassword(LocalDate expirePassword) {
        this.expirePassword = expirePassword;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public TurbineUser objectdata(byte[] objectdata) {
        this.objectdata = objectdata;
        return this;
    }

    public void setObjectdata(byte[] objectdata) {
        this.objectdata = objectdata;
    }

    public String getObjectdataContentType() {
        return objectdataContentType;
    }

    public TurbineUser objectdataContentType(String objectdataContentType) {
        this.objectdataContentType = objectdataContentType;
        return this;
    }

    public void setObjectdataContentType(String objectdataContentType) {
        this.objectdataContentType = objectdataContentType;
    }

    @Transient
    public String getFullName() {
        return WordUtils.capitalizeFully((this.firstName != null ? this.firstName : "") + (this.lastName != null ? ((this.firstName != null ? " " : "") + this.lastName) : ""));
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
        TurbineUser turbineUser = (TurbineUser) o;
        if (turbineUser.getUserId() == null || getUserId() == null) {
            return false;
        }
        return Objects.equals(getUserId(), turbineUser.getUserId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getUserId());
    }

    @Override
    public String toString() {
        return "TurbineUser{" + "userId=" + userId + ", loginName='" + loginName + '\'' + ", passwordValue='" + passwordValue + '\'' + ", firstName='" + firstName + '\''
            + ", lastName='" + lastName + '\'' + ", email='" + email + '\'' + ", confirmValue='" + confirmValue + '\'' + ", modified=" + modified + ", created=" + created
            + ", lastLogin=" + lastLogin + ", expire=" + expire + ", expirePassword=" + expirePassword + ", objectdata=" + Arrays.toString(objectdata) + '}';
    }
}
