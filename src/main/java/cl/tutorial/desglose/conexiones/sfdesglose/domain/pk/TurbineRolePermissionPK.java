package cl.tutorial.desglose.conexiones.sfdesglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class TurbineRolePermissionPK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long roleId;
    private Long permissionId;

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

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TurbineRolePermissionPK))
            return false;
        TurbineRolePermissionPK that = (TurbineRolePermissionPK) o;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }

    @Override
    public String toString() {
        return "TurbineRolePermissionPK{" + "roleId=" + roleId + ", permissionId=" + permissionId + '}';
    }
}
