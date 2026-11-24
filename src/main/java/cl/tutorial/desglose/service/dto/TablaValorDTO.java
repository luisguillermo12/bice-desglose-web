package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the TablaValor entity.
 */
public class TablaValorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotNull
    private String codTabla;

    @NotNull
    private String codGenerico;

    @NotNull
    private LocalDate fecValidez;

    private BigDecimal valor;

    private Integer codTipovalor;

    @NotNull
    private Integer codRegStatus;

    private Integer codRegLog;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodTabla() {
        return codTabla;
    }

    public void setCodTabla(String codTabla) {
        this.codTabla = codTabla;
    }

    public String getCodGenerico() {
        return codGenerico;
    }

    public void setCodGenerico(String codGenerico) {
        this.codGenerico = codGenerico;
    }

    public LocalDate getFecValidez() {
        return fecValidez;
    }

    public void setFecValidez(LocalDate fecValidez) {
        this.fecValidez = fecValidez;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getCodTipovalor() {
        return codTipovalor;
    }

    public void setCodTipovalor(Integer codTipovalor) {
        this.codTipovalor = codTipovalor;
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

        TablaValorDTO tablaValorDTO = (TablaValorDTO) o;
        if (tablaValorDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tablaValorDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TablaValorDTO{" + "id=" + getId() + ", codTabla='" + getCodTabla() + "'" + ", codGenerico='" + getCodGenerico() + "'" + ", fecValidez='" + getFecValidez() + "'"
            + ", valor=" + getValor() + ", codTipovalor=" + getCodTipovalor() + ", codRegStatus=" + getCodRegStatus() + ", codRegLog=" + getCodRegLog() + "}";
    }
}
