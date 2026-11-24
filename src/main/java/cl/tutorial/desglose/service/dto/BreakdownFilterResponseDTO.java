package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO for the BreakdownFilterResponseDTO.
 */
public class BreakdownFilterResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String        codCliente;
    public String        nomCliente;
    public Boolean       isEmpresa;
    public Boolean       hasRut;
    public String        codActividad;
    public String        codSector;
    public String        glsSector;
    public String        codTipoDes;
    public String        glsTipoDes;
    public String        codSubTipoDes;
    public String        glsSubTipoDes;
    public String        codMoneda;
    public String        glsMoneda;
    public String        codMonedaImp;
    public String        glsMonedaImp;
    public String        codUnidadMon;
    public String        glsUnidadMon;
    public LocalDateTime fechaEnvioRevision;
    public LocalDateTime fechaRevision;
    public String        nombreRevisor;
    public String        userAnalista;
    public String        userOpen;
    public String        nombreAnalista;
    public String        nombreUserOpen;
    public String        codEstado;
    public String        glsEstado;
    public String        codPlantillaDesglose;
    public String        glsPlantillaDesglose;
    public String        rolEmpleadoDefEnvPdf;

    public List<LocalDate> fechaPeriodos;

    public String        codClienteFormatted;
    public String        flgTipoCliente;

    public LocalDateTime fechaDesglose;
    public String userCreacion;
    public String nombreUserCreacion;

    public LocalDateTime fechaLastUpdate;
    public String userUpdate;
    public String nombreUserUpdate;

    public LocalDateTime fechaDesgloDef;
    public String nombreRevisorDef;
    public String nombreRevisorModif;

    public String glsObservacion;

    @Override
    public String toString() {
        return "BreakdownFilterResponseDTO{" +
            "codCliente='" + codCliente + '\'' +
            ", nomCliente='" + nomCliente + '\'' +
            ", isEmpresa=" + isEmpresa +
            ", hasRut=" + hasRut +
            ", codActividad='" + codActividad + '\'' +
            ", codSector='" + codSector + '\'' +
            ", glsSector='" + glsSector + '\'' +
            ", codTipoDes='" + codTipoDes + '\'' +
            ", glsTipoDes='" + glsTipoDes + '\'' +
            ", codSubTipoDes='" + codSubTipoDes + '\'' +
            ", glsSubTipoDes='" + glsSubTipoDes + '\'' +
            ", codMoneda='" + codMoneda + '\'' +
            ", glsMoneda='" + glsMoneda + '\'' +
            ", codMonedaImp='" + codMonedaImp + '\'' +
            ", glsMonedaImp='" + glsMonedaImp + '\'' +
            ", codUnidadMon='" + codUnidadMon + '\'' +
            ", glsUnidadMon='" + glsUnidadMon + '\'' +
            ", fechaEnvioRevision=" + fechaEnvioRevision +
            ", fechaRevision=" + fechaRevision +
            ", nombreRevisor='" + nombreRevisor + '\'' +
            ", userAnalista='" + userAnalista + '\'' +
            ", userOpen='" + userOpen + '\'' +
            ", nombreAnalista='" + nombreAnalista + '\'' +
            ", nombreUserOpen='" + nombreUserOpen + '\'' +
            ", codEstado='" + codEstado + '\'' +
            ", glsEstado='" + glsEstado + '\'' +
            ", codPlantillaDesglose='" + codPlantillaDesglose + '\'' +
            ", glsPlantillaDesglose='" + glsPlantillaDesglose + '\'' +
            ", rolEmpleadoDefEnvPdf='" + rolEmpleadoDefEnvPdf + '\'' +
            ", fechaPeriodos=" + fechaPeriodos +
            ", codClienteFormatted='" + codClienteFormatted + '\'' +
            ", flgTipoCliente='" + flgTipoCliente + '\'' +
            ", fechaDesglose=" + fechaDesglose +
            ", userCreacion='" + userCreacion + '\'' +
            ", nombreUserCreacion='" + nombreUserCreacion + '\'' +
            ", fechaLastUpdate=" + fechaLastUpdate +
            ", userUpdate='" + userUpdate + '\'' +
            ", nombreUserUpdate='" + nombreUserUpdate + '\'' +
            ", fechaDesgloDef=" + fechaDesgloDef +
            ", nombreRevisorDef='" + nombreRevisorDef + '\'' +
            ", nombreRevisorModif='" + nombreRevisorModif + '\'' +
            ", glsObservacion='" + glsObservacion + '\'' +
            '}';
    }

/*
    @Override
    public String toString() {
        return "BreakdownFilterResponseDTO{" + "codCliente='" + codCliente + '\'' + ", nomCliente='" + nomCliente + '\'' + ", isEmpresa=" + isEmpresa + ", hasRut=" + hasRut
            + ", codActividad='" + codActividad + '\'' + ", codSector='" + codSector + '\'' + ", glsSector='" + glsSector + '\'' + ", codTipoDes='" + codTipoDes + '\''
            + ", glsTipoDes='" + glsTipoDes + '\'' + ", codSubTipoDes='" + codSubTipoDes + '\'' + ", glsSubTipoDes='" + glsSubTipoDes + '\'' + ", codMoneda='" + codMoneda + '\''
            + ", glsMoneda='" + glsMoneda + '\'' + ", codMonedaImp='" + codMonedaImp + '\'' + ", glsMonedaImp='" + glsMonedaImp + '\'' + ", codUnidadMon='" + codUnidadMon + '\''
            + ", glsUnidadMon='" + glsUnidadMon + '\'' + ", fechaDesglose=" + fechaDesglose + ", fechaLastUpdate=" + fechaLastUpdate + ", fechaEnvioRevision=" + fechaEnvioRevision
            + ", fechaRevision=" + fechaRevision + ", nombreRevisor='" + nombreRevisor + '\'' + ", userAnalista='" + userAnalista + '\'' + ", nombreAnalista='" + nombreAnalista
            + '\'' + ", codEstado='" + codEstado + '\'' + ", glsEstado='" + glsEstado + '\'' + ", codPlantillaDesglose='" + codPlantillaDesglose + '\'' + ", glsPlantillaDesglose='"
            + glsPlantillaDesglose + '\'' + ", rolEmpleadoDefEnvPdf='" + rolEmpleadoDefEnvPdf + '\'' + ", fechaPeriodos=" + fechaPeriodos + '}';
    }
*/
}
