package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * A DTO for the FiltroDesglose.
 */
public class BreakdownFilterRequestDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String    tipoFiltro;
    public String    rut;
    public String    rutVerificationNumber;
    public String    nombre;
    public Integer   tipoDesglose;
    public String    tipoPlantillaDesglose;
    public Integer   subTipoDesglose;
    public String    subTipoDesgloseDesc;
    public Integer   moneda;
    public LocalDate fecCrea1;
    public LocalDate fecCrea2;
    public LocalDate fecRevi1;
    public LocalDate fecRevi2;
    public LocalDate fecEnvRevi1;
    public LocalDate fecEnvRevi2;
    public String    nomUserModification;
    public LocalDate fecDef1;
    public LocalDate fecDef2;
    public String    nomAnalista;
    public Integer   sector;
    public String    codEstado;
    public String    codStatus;
    public Boolean   loadPeriodsDates = false;

    @Override
    public String toString() {
        return "BreakdownFilterRequestDTO{" + "tipoFiltro='" + tipoFiltro + '\'' + ", rut='" + rut + '\'' + ", rutVerificationNumber='" + rutVerificationNumber + '\''
            + ", nombre='" + nombre + '\'' + ", tipoDesglose=" + tipoDesglose + ", tipoPlantillaDesglose='" + tipoPlantillaDesglose + '\'' + ", subTipoDesglose=" + subTipoDesglose
            + ", subTipoDesgloseDesc='" + subTipoDesgloseDesc + '\'' + ", moneda=" + moneda + ", fecCrea1=" + fecCrea1 + ", fecCrea2=" + fecCrea2 + ", fecRevi1=" + fecRevi1
            + ", fecRevi2=" + fecRevi2 + ", fecEnvRevi1=" + fecEnvRevi1 + ", fecEnvRevi2=" + fecEnvRevi2 + ", nomUserModification='" + nomUserModification + '\'' + ", fecDef1="
            + fecDef1 + ", fecDef2=" + fecDef2 + ", nomAnalista='" + nomAnalista + '\'' + ", sector=" + sector + ", codEstado='" + codEstado + '\'' + ", codStatus='" + codStatus
            + '\'' + ", loadPeriodsDates=" + loadPeriodsDates + '}';
    }
}
