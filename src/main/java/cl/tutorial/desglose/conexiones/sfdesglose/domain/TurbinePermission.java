package cl.tutorial.desglose.conexiones.sfdesglose.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"TURBINE_PERMISSION\" que contiene permisos. Pertenece al esquema SF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TURBINE_PERMISSION\" que contiene permisos. Pertenece al esquema SF_ADMIN. @author Antonio Marrero Palomino")
@Entity
@Table(name = "TURBINE_PERMISSION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurbinePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "permission_id", nullable = false)
    private Long permissionId;

    @NotNull
    @Column(name = "permission_name", nullable = false, unique = true)
    private String permissionName;

    //    @Lob
    //    @Column(name = "objectdata")
    @Transient
    private byte[] objectdata;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getPermissionId() {
        return permissionId;
    }

    public TurbinePermission permissionId(Long permissionId) {
        this.permissionId = permissionId;
        return this;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public TurbinePermission permissionName(String permissionName) {
        this.permissionName = permissionName;
        return this;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public TurbinePermission objectdata(byte[] objectdata) {
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
        TurbinePermission turbinePermission = (TurbinePermission) o;
        if (turbinePermission.getPermissionId() == null || getPermissionId() == null) {
            return false;
        }
        return Objects.equals(getPermissionId(), turbinePermission.getPermissionId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getPermissionId());
    }

    @Override
    public String toString() {
        return "TurbinePermission{" + "permissionId=" + getPermissionId() + ", permissionName='" + getPermissionName() + "'" + ", objectdata='" + getObjectdata() + "'" + "}";
    }
}
