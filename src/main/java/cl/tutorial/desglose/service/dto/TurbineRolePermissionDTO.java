package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TurbineRolePermission entity.
 */
public class TurbineRolePermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    private Long roleId;

    private Long permissionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long turbineRoleId) {
        this.roleId = turbineRoleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long turbinePermissionId) {
        this.permissionId = turbinePermissionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurbineRolePermissionDTO turbineRolePermissionDTO = (TurbineRolePermissionDTO) o;
        if (turbineRolePermissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turbineRolePermissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurbineRolePermissionDTO{" + "id=" + getId() + ", role=" + getRoleId() + ", permission=" + getPermissionId() + "}";
    }
}
