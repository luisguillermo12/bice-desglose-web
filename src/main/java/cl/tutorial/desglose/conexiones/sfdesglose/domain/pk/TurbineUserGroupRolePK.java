package cl.tutorial.desglose.conexiones.sfdesglose.domain.pk;

import java.io.Serializable;
import java.util.Objects;

public class TurbineUserGroupRolePK implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long userId;
    private Long groupId;
    private Long roleId;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TurbineUserGroupRolePK))
            return false;
        TurbineUserGroupRolePK that = (TurbineUserGroupRolePK) o;
        return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId, roleId);
    }

    @Override
    public String toString() {
        return "TurbineUserGroupRolePK{" + "userId=" + userId + ", groupId=" + groupId + ", roleId=" + roleId + '}';
    }
}
