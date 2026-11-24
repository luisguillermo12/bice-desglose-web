package cl.tutorial.desglose.conexiones.sfdesglose.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"TURBINE_GROUP\" que contiene grupos de usuario. Pertenece al esquema SF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"TURBINE_GROUP\" que contiene grupos de usuario. Pertenece al esquema SF_ADMIN. @author Antonio Marrero Palomino")
@Entity
@Table(name = "TURBINE_GROUP")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class TurbineGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @NotNull
    @Column(name = "group_id", nullable = false)
    private Long groupId;

    @NotNull
    @Column(name = "group_name", nullable = false, unique = true)
    private String groupName;

    //    @Lob
    //    @Column(name = "objectdata")
    @Transient
    private byte[] objectdata;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove

    public Long getGroupId() {
        return groupId;
    }

    public TurbineGroup groupId(Long groupId) {
        this.groupId = groupId;
        return this;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public TurbineGroup groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public TurbineGroup objectdata(byte[] objectdata) {
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
        TurbineGroup turbineGroup = (TurbineGroup) o;
        if (turbineGroup.getGroupId() == null || getGroupId() == null) {
            return false;
        }
        return Objects.equals(getGroupId(), turbineGroup.getGroupId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getGroupId());
    }

    @Override
    public String toString() {
        return "TurbineGroup{" + "groupId=" + getGroupId() + ", groupName='" + getGroupName() + "'" + ", objectdata='" + getObjectdata() + "'" + "}";
    }
}
