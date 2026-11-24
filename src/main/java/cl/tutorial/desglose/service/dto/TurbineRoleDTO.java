package cl.tutorial.desglose.service.dto;

import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TurbineRole entity.
 */
public class TurbineRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long roleId;

    @NotNull
    private String roleName;

    @Lob
    private byte[] objectdata;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public void setObjectdata(byte[] objectdata) {
        this.objectdata = objectdata;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurbineRoleDTO turbineRoleDTO = (TurbineRoleDTO) o;
        if (turbineRoleDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turbineRoleDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurbineRoleDTO{" + "id=" + getId() + ", roleId=" + getRoleId() + ", roleName='" + getRoleName() + "'" + ", objectdata='" + getObjectdata() + "'" + "}";
    }
}
