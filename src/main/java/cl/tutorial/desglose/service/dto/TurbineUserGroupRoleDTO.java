package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TurbineUserGroupRole entity.
 */
public class TurbineUserGroupRoleDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long groupId;

    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long turbineUserId) {
        this.userId = turbineUserId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long turbineGroupId) {
        this.groupId = turbineGroupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long turbineRoleId) {
        this.roleId = turbineRoleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        TurbineUserGroupRoleDTO that = (TurbineUserGroupRoleDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId, roleId);
    }

    @Override
    public String toString() {
        return "TurbineUserGroupRoleDTO{" + ", user=" + getUserId() + ", group=" + getGroupId() + ", role=" + getRoleId() + "}";
    }
}
