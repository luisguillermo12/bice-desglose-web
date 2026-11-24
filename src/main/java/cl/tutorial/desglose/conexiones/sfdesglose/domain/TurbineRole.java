package cl.tutorial.desglose.conexiones.sfdesglose.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"TURBINE_ROLE\" que contiene roles de usuario. Pertenece al esquema SF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TURBINE_ROLE\" que contiene roles de usuario. Pertenece al esquema SF_ADMIN. @author Antonio Marrero Palomino")
@Entity
@Table(name = "TURBINE_ROLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurbineRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "role_id", nullable = false)
    private Long roleId;

    @NotNull
    @Column(name = "role_name", nullable = false, unique = true)
    private String roleName;

    //    @Lob
    //    @Column(name = "objectdata")
    @Transient
    private byte[] objectdata;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getRoleId() {
        return roleId;
    }

    public TurbineRole roleId(Long roleId) {
        this.roleId = roleId;
        return this;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public TurbineRole roleName(String roleName) {
        this.roleName = roleName;
        return this;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public TurbineRole objectdata(byte[] objectdata) {
        this.objectdata = objectdata;
        return this;
    }

    public void setObjectdata(byte[] objectdata) {
        this.objectdata = objectdata;
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
        TurbineRole turbineRole = (TurbineRole) o;
        if (turbineRole.getRoleId() == null || getRoleId() == null) {
            return false;
        }
        return Objects.equals(getRoleId(), turbineRole.getRoleId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getRoleId());
    }

    @Override
    public String toString() {
        return "TurbineRole{" + "roleId=" + getRoleId() + ", roleName='" + getRoleName() + "'" + ", objectdata='" + getObjectdata() + "'" + "}";
    }
}
