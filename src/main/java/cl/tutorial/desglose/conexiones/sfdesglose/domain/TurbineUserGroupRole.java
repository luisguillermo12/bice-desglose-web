package cl.tutorial.desglose.conexiones.sfdesglose.domain;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.pk.TurbineUserGroupRolePK;
import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"TURBINE_USER_GROUP_ROLE\" que relacion usuario, grupo y rol. Pertenece al esquema SF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TURBINE_USER_GROUP_ROLE\" que relacion usuario, grupo y rol. Pertenece al esquema SF_ADMIN. @author Antonio Marrero Palomino")
@IdClass(TurbineUserGroupRolePK.class)
@Entity
@Table(name = "TURBINE_USER_GROUP_ROLE")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurbineUserGroupRole implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "user_id")
    private Long userId;

    @Id
    @Column(name = "group_id")
    private Long groupId;

    @Id
    @Column(name = "role_id")
    private Long roleId;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

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

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TurbineUserGroupRole))
            return false;
        TurbineUserGroupRole that = (TurbineUserGroupRole) o;
        return Objects.equals(userId, that.userId) && Objects.equals(groupId, that.groupId) && Objects.equals(roleId, that.roleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, groupId, roleId);
    }

    @Override
    public String toString() {
        return "TurbineUserGroupRole{" + "userId=" + userId + ", groupId=" + groupId + ", roleId=" + roleId + '}';
    }
}
