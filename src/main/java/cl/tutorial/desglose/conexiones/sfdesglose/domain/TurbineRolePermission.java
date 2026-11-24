package cl.tutorial.desglose.conexiones.sfdesglose.domain;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.pk.TurbineRolePermissionPK;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"TURBINE_ROLE_PERMISSION\" que contiene permisos asociados al rol. Pertenece al esquema SF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TURBINE_ROLE_PERMISSION\" que contiene permisos asociados al rol. Pertenece al esquema SF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(TurbineRolePermissionPK.class)
@Entity
@Table(name = "TURBINE_ROLE_PERMISSION")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurbineRolePermission implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Long roleId;

    @Id
    private Long permissionId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TurbineRolePermission))
            return false;
        TurbineRolePermission that = (TurbineRolePermission) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }

    @Override
    public String toString() {
        return "TurbineRolePermission{" + "roleId=" + roleId + ", permissionId=" + permissionId + '}';
    }
}
