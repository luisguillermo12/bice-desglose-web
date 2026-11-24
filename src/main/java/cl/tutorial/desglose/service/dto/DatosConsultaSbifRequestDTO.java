package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class DatosConsultaSbifRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String rut;
    private LocalDate fecConsulta;
    private List<LocalDate> arrPERIODOS;
    private List<Integer> activePeriodsIndexes;

    public DatosConsultaSbifRequestDTO() {
    }

    public DatosConsultaSbifRequestDTO(String rut, LocalDate fecConsulta, List<LocalDate> arrPERIODOS, List<Integer> activePeriodsIndexes) {
        this.rut = rut;
        this.fecConsulta = fecConsulta;
        this.arrPERIODOS = arrPERIODOS;
        this.activePeriodsIndexes = activePeriodsIndexes;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public LocalDate getFecConsulta() {
        return fecConsulta;
    }

    public void setFecConsulta(LocalDate fecConsulta) {
        this.fecConsulta = fecConsulta;
    }

    public List<LocalDate> getArrPERIODOS() {
        return arrPERIODOS;
    }

    public void setArrPERIODOS(List<LocalDate> arrPERIODOS) {
        this.arrPERIODOS = arrPERIODOS;
    }

    public List<Integer> getActivePeriodsIndexes() {
        return activePeriodsIndexes;
    }

    public void setActivePeriodsIndexes(List<Integer> activePeriodsIndexes) {
        this.activePeriodsIndexes = activePeriodsIndexes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DatosConsultaSbifRequestDTO)) return false;
        DatosConsultaSbifRequestDTO that = (DatosConsultaSbifRequestDTO) o;
        return Objects.equals(rut, that.rut) &&
            Objects.equals(fecConsulta, that.fecConsulta) &&
            Objects.equals(arrPERIODOS, that.arrPERIODOS) &&
            Objects.equals(activePeriodsIndexes, that.activePeriodsIndexes);
    }

    @Override
    public int hashCode() {

        return Objects.hash(rut, fecConsulta, arrPERIODOS, activePeriodsIndexes);
    }

    @Override
    public String toString() {
        return "DatosConsultaSbifRequestDTO{" +
            "rut='" + rut + '\'' +
            ", fecConsulta=" + fecConsulta +
            ", arrPERIODOS=" + arrPERIODOS +
            ", activePeriodsIndexes=" + activePeriodsIndexes +
            '}';
    }
}
