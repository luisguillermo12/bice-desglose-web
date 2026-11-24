package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for Actividad Económica.
 */
public class ActividadEconomicaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codSector;
    private String codSubsector;
    private String codActividad;
    private String glsActividad;

    public ActividadEconomicaDTO(String codSector, String codSubsector, String codActividad, String glsActividad) {
        this.codSector = codSector;
        this.codSubsector = codSubsector;
        this.codActividad = codActividad;
        this.glsActividad = glsActividad;
    }

    public String getCodSector() {
        return codSector;
    }

    public void setCodSector(String codSector) {
        this.codSector = codSector;
    }

    public String getCodSubsector() {
        return codSubsector;
    }

    public void setCodSubsector(String codSubsector) {
        this.codSubsector = codSubsector;
    }

    public String getCodActividad() {
        return codActividad;
    }

    public void setCodActividad(String codActividad) {
        this.codActividad = codActividad;
    }

    public String getGlsActividad() {
        return glsActividad;
    }

    public void setGlsActividad(String glsActividad) {
        this.glsActividad = glsActividad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ActividadEconomicaDTO))
            return false;
        ActividadEconomicaDTO that = (ActividadEconomicaDTO) o;
        return Objects.equals(codSector, that.codSector) && Objects.equals(codSubsector, that.codSubsector) && Objects.equals(codActividad, that.codActividad) && Objects
            .equals(glsActividad, that.glsActividad);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codSector, codSubsector, codActividad, glsActividad);
    }

    @Override
    public String toString() {
        return "ActividadEconomicaDTO{" + "codSector='" + codSector + '\'' + ", codSubsector='" + codSubsector + '\'' + ", codActividad='" + codActividad + '\''
            + ", glsActividad='" + glsActividad + '\'' + '}';
    }
}
