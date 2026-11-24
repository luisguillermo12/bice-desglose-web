package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TurbineGroup entity.
 */
public class TurbineGroupDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long groupId;

    @NotNull
    private String groupName;

    @Lob
    private byte[] objectdata;
    private String objectdataContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public byte[] getObjectdata() {
        return objectdata;
    }

    public void setObjectdata(byte[] objectdata) {
        this.objectdata = objectdata;
    }

    public String getObjectdataContentType() {
        return objectdataContentType;
    }

    public void setObjectdataContentType(String objectdataContentType) {
        this.objectdataContentType = objectdataContentType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TurbineGroupDTO turbineGroupDTO = (TurbineGroupDTO) o;
        if (turbineGroupDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turbineGroupDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurbineGroupDTO{" +
            "id=" + getId() +
            ", groupId=" + getGroupId() +
            ", groupName='" + getGroupName() + "'" +
            ", objectdata='" + getObjectdata() + "'" +
            "}";
    }
}
