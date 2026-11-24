package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for SubSector Económico.
 */
public class SubSectorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codSector;
    private String codSubSector;
    private String glsSubSector;

    public SubSectorDTO(String codSector, String codSubSector, String glsSubSector) {
        this.codSector = codSector;
        this.codSubSector = codSubSector;
        this.glsSubSector = glsSubSector;
    }

    public String getCodSector() {
        return codSector;
    }

    public void setCodSector(String codSector) {
        this.codSector = codSector;
    }

    public String getCodSubSector() {
        return codSubSector;
    }

    public void setCodSubSector(String codSubSector) {
        this.codSubSector = codSubSector;
    }

    public String getGlsSubSector() {
        return glsSubSector;
    }

    public void setGlsSubSector(String glsSubSector) {
        this.glsSubSector = glsSubSector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SubSectorDTO))
            return false;
        SubSectorDTO that = (SubSectorDTO) o;
        return Objects.equals(codSector, that.codSector) && Objects.equals(codSubSector, that.codSubSector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codSector, codSubSector);
    }

    @Override
    public String toString() {
        return "SubSectorDTO{" + "codSector='" + codSector + '\'' + ", codSubSector='" + codSubSector + '\'' + ", glsSubSector='" + glsSubSector + '\'' + '}';
    }
}
