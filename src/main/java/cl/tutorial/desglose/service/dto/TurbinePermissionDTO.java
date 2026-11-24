package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Lob;

/**
 * A DTO for the TurbinePermission entity.
 */
public class TurbinePermissionDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Long permissionId;

    @NotNull
    private String permissionName;

    @Lob
    private byte[] objectdata;
    private String objectdataContentType;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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

        TurbinePermissionDTO turbinePermissionDTO = (TurbinePermissionDTO) o;
        if (turbinePermissionDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), turbinePermissionDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TurbinePermissionDTO{" +
            "id=" + getId() +
            ", permissionId=" + getPermissionId() +
            ", permissionName='" + getPermissionName() + "'" +
            ", objectdata='" + getObjectdata() + "'" +
            "}";
    }
}
