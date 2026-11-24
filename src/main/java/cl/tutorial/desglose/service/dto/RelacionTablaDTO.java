package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the RelacionTabla entity.
 */
public class RelacionTablaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private Integer numCorRegistro;

    private String codTabla1;

    private String codTabla2;

    private String codGenerico1;

    private String codGenerico2;

    @NotNull
    private Integer codRegStatus;

    private Integer codRegLog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumCorRegistro() {
        return numCorRegistro;
    }

    public void setNumCorRegistro(Integer numCorRegistro) {
        this.numCorRegistro = numCorRegistro;
    }

    public String getCodTabla1() {
        return codTabla1;
    }

    public void setCodTabla1(String codTabla1) {
        this.codTabla1 = codTabla1;
    }

    public String getCodTabla2() {
        return codTabla2;
    }

    public void setCodTabla2(String codTabla2) {
        this.codTabla2 = codTabla2;
    }

    public String getCodGenerico1() {
        return codGenerico1;
    }

    public void setCodGenerico1(String codGenerico1) {
        this.codGenerico1 = codGenerico1;
    }

    public String getCodGenerico2() {
        return codGenerico2;
    }

    public void setCodGenerico2(String codGenerico2) {
        this.codGenerico2 = codGenerico2;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelacionTablaDTO relacionTablaDTO = (RelacionTablaDTO) o;
        if (relacionTablaDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relacionTablaDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RelacionTablaDTO{" +
            "id=" + getId() +
            ", numCorRegistro=" + getNumCorRegistro() +
            ", codTabla1='" + getCodTabla1() + "'" +
            ", codTabla2='" + getCodTabla2() + "'" +
            ", codGenerico1='" + getCodGenerico1() + "'" +
            ", codGenerico2='" + getCodGenerico2() + "'" +
            ", codRegStatus=" + getCodRegStatus() +
            ", codRegLog=" + getCodRegLog() +
            "}";
    }
}
