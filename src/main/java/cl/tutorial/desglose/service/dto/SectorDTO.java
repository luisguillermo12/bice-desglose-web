package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for sector Económico.
 */
public class SectorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codSector;
    private String glsSector;

    public SectorDTO(String codSector, String glsSector) {
        this.codSector = codSector;
        this.glsSector = glsSector;
    }

    public String getCodSector() {
        return codSector;
    }

    public void setCodSector(String codSector) {
        this.codSector = codSector;
    }

    public String getGlsSector() {
        return glsSector;
    }

    public void setGlsSector(String glsSector) {
        this.glsSector = glsSector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SectorDTO))
            return false;
        SectorDTO sectorDTO = (SectorDTO) o;
        return Objects.equals(codSector, sectorDTO.codSector) && Objects.equals(glsSector, sectorDTO.glsSector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codSector, glsSector);
    }

    @Override
    public String toString() {
        return "SectorDTO{" + "codSector='" + codSector + '\'' + ", glsSector='" + glsSector + '\'' + '}';
    }
}
