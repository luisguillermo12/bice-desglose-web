package cl.tutorial.desglose.domain;

import cl.tutorial.desglose.domain.pk.CuadrePK;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"CUADRE\", en donde se definición condiciones de cuadratura a realizar previo al grabado de un desglose. Pertenece al esquema CIF_ADMIN.
 *
 * @author Antonio Marrero Palomino
 */
@ApiModel(
    description = "Representa la tabla \"CUADRE\", en donde se definición condiciones de cuadratura a realizar previo al grabado de un desglose. Pertenece al esquema CIF_ADMIN. "
        + "@author Antonio Marrero Palomino")
@IdClass(CuadrePK.class)
@Entity
@Table(name = "CUADRE_WK")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Cuadre implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * Tipo de concepto 1.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de copm startncepto 1.", required = true)
    @Column(name = "cod_tpo_concep1", nullable = false)
    private String codTpoConcep1;

    /**
     * Correlativo del concepto 1.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo del concepto 1.", required = true)
    @Column(name = "cor_concepto1", nullable = false)
    private Integer corConcepto1;

    /**
     * Tipo de concepto 2.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Tipo de concepto 2.", required = true)
    @Column(name = "cod_tpo_concep2", nullable = false)
    private String codTpoConcep2;

    /**
     * Correlativo del concepto 2.
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "Correlativo del concepto 2.", required = true)
    @Column(name = "cor_concepto2", nullable = false)
    private Integer corConcepto2;

    /**
     * Mensaje a mostrar.
     */
    @ApiModelProperty(value = "Mensaje a mostrar.")
    @Column(name = "gls_mensaje")
    private String glsMensaje;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getCodTpoConcep1() {
        return codTpoConcep1;
    }

    public Cuadre codTpoConcep1(String codTpoConcep1) {
        this.codTpoConcep1 = codTpoConcep1;
        return this;
    }

    public void setCodTpoConcep1(String codTpoConcep1) {
        this.codTpoConcep1 = codTpoConcep1;
    }

    public Integer getCorConcepto1() {
        return corConcepto1;
    }

    public Cuadre corConcepto1(Integer corConcepto1) {
        this.corConcepto1 = corConcepto1;
        return this;
    }

    public void setCorConcepto1(Integer corConcepto1) {
        this.corConcepto1 = corConcepto1;
    }

    public String getCodTpoConcep2() {
        return codTpoConcep2;
    }

    public Cuadre codTpoConcep2(String codTpoConcep2) {
        this.codTpoConcep2 = codTpoConcep2;
        return this;
    }

    public void setCodTpoConcep2(String codTpoConcep2) {
        this.codTpoConcep2 = codTpoConcep2;
    }

    public Integer getCorConcepto2() {
        return corConcepto2;
    }

    public Cuadre corConcepto2(Integer corConcepto2) {
        this.corConcepto2 = corConcepto2;
        return this;
    }

    public void setCorConcepto2(Integer corConcepto2) {
        this.corConcepto2 = corConcepto2;
    }

    public String getGlsMensaje() {
        return glsMensaje;
    }

    public Cuadre glsMensaje(String glsMensaje) {
        this.glsMensaje = glsMensaje;
        return this;
    }

    public void setGlsMensaje(String glsMensaje) {
        this.glsMensaje = glsMensaje;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Cuadre))
            return false;
        Cuadre cuadre = (Cuadre) o;
        return Objects.equals(codTpoConcep1, cuadre.codTpoConcep1) && Objects.equals(corConcepto1, cuadre.corConcepto1) && Objects.equals(codTpoConcep2, cuadre.codTpoConcep2)
            && Objects.equals(corConcepto2, cuadre.corConcepto2) && Objects.equals(glsMensaje, cuadre.glsMensaje);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codTpoConcep1, corConcepto1, codTpoConcep2, corConcepto2, glsMensaje);
    }

    @Override
    public String toString() {
        return "Cuadre{" + "codTpoConcep1='" + getCodTpoConcep1() + "'" + ", corConcepto1=" + getCorConcepto1() + ", codTpoConcep2='" + getCodTpoConcep2() + "'" + ", corConcepto2="
            + getCorConcepto2() + ", glsMensaje='" + getGlsMensaje() + "'" + "}";
    }
}
