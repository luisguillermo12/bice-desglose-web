package cl.tutorial.desglose.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"RELTAB\". Contiene la relación de conceptos definidos en DESCOD. Pertenece al esquema BCT_ADMIN.
 * @author Antonio Marrero Palomino
 */
@ApiModel(description = "Representa la tabla \"RELTAB\". Contiene la relación de conceptos definidos en DESCOD. Pertenece al esquema BCT_ADMIN. @author Antonio Marrero Palomino")
@Entity
@Table(name = "relacion_tabla")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class RelacionTabla implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * Id correlativo.
     */
    @NotNull
    @ApiModelProperty(value = "Id correlativo.", required = true)
    @Column(name = "num_cor_registro", nullable = false)
    private Integer numCorRegistro;

    /**
     * Código de tabla 1 del concepto.
     */
    @ApiModelProperty(value = "Código de tabla 1 del concepto.")
    @Column(name = "cod_tabla_1")
    private String codTabla1;

    /**
     * Código de tabla 2 del concepto.
     */
    @ApiModelProperty(value = "Código de tabla 2 del concepto.")
    @Column(name = "cod_tabla_2")
    private String codTabla2;

    /**
     * Código genérico 1 asociado al código de tabla.
     */
    @ApiModelProperty(value = "Código genérico 1 asociado al código de tabla.")
    @Column(name = "cod_generico_1")
    private String codGenerico1;

    /**
     * Código genérico 2 asociado al código de tabla.
     */
    @ApiModelProperty(value = "Código genérico 2 asociado al código de tabla.")
    @Column(name = "cod_generico_2")
    private String codGenerico2;

    /**
     * Código de vigencia del registro.
     */
    @NotNull
    @ApiModelProperty(value = "Código de vigencia del registro.", required = true)
    @Column(name = "cod_reg_status", nullable = false)
    private Integer codRegStatus;

    /**
     * Código de modificación del registro.
     */
    @ApiModelProperty(value = "Código de modificación del registro.")
    @Column(name = "cod_reg_log")
    private Integer codRegLog;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumCorRegistro() {
        return numCorRegistro;
    }

    public RelacionTabla numCorRegistro(Integer numCorRegistro) {
        this.numCorRegistro = numCorRegistro;
        return this;
    }

    public void setNumCorRegistro(Integer numCorRegistro) {
        this.numCorRegistro = numCorRegistro;
    }

    public String getCodTabla1() {
        return codTabla1;
    }

    public RelacionTabla codTabla1(String codTabla1) {
        this.codTabla1 = codTabla1;
        return this;
    }

    public void setCodTabla1(String codTabla1) {
        this.codTabla1 = codTabla1;
    }

    public String getCodTabla2() {
        return codTabla2;
    }

    public RelacionTabla codTabla2(String codTabla2) {
        this.codTabla2 = codTabla2;
        return this;
    }

    public void setCodTabla2(String codTabla2) {
        this.codTabla2 = codTabla2;
    }

    public String getCodGenerico1() {
        return codGenerico1;
    }

    public RelacionTabla codGenerico1(String codGenerico1) {
        this.codGenerico1 = codGenerico1;
        return this;
    }

    public void setCodGenerico1(String codGenerico1) {
        this.codGenerico1 = codGenerico1;
    }

    public String getCodGenerico2() {
        return codGenerico2;
    }

    public RelacionTabla codGenerico2(String codGenerico2) {
        this.codGenerico2 = codGenerico2;
        return this;
    }

    public void setCodGenerico2(String codGenerico2) {
        this.codGenerico2 = codGenerico2;
    }

    public Integer getCodRegStatus() {
        return codRegStatus;
    }

    public RelacionTabla codRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
        return this;
    }

    public void setCodRegStatus(Integer codRegStatus) {
        this.codRegStatus = codRegStatus;
    }

    public Integer getCodRegLog() {
        return codRegLog;
    }

    public RelacionTabla codRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
        return this;
    }

    public void setCodRegLog(Integer codRegLog) {
        this.codRegLog = codRegLog;
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
        RelacionTabla relacionTabla = (RelacionTabla) o;
        if (relacionTabla.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relacionTabla.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RelacionTabla{" +
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
