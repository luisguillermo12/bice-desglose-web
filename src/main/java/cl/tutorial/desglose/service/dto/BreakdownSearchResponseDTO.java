package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * A DTO for the BreakdownSearchResponseDTO.
 */
public class BreakdownSearchResponseDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public String        codUnidadMon;
    public String        glsTipoDes;
    public String        glsSubtipoDes;
    public BigDecimal    valDeflacBase;
    public LocalDateTime fecDeflacBase;
    public BigDecimal    valIva;
    public String        glsObservacion;
    public String        nomUserCrea;
    public String        userModif;
    public LocalDateTime fechaModif;
    public String        userOpen;
    public Boolean       flgIsTemporal;
    public String        codMonedaImp;
    public String        nomUser;
    public String        glsEjecutor;
    public Boolean       flgReq2Firma;
    public String        usrEjecutor;
    public String        codEstado;
    public String        glsEstado;
    public String        numRol1Firm;
    public String        numRol2Firm;
    public String        numRolRee;
    public String        nomUserOpen;

    @Override
    public String toString() {
        return "BreakdownSearchResponseDTO{" + "codUnidadMon='" + codUnidadMon + '\'' + ", glsTipoDes='" + glsTipoDes + '\'' + ", valDeflacBase=" + valDeflacBase
            + ", fecDeflacBase=" + fecDeflacBase + ", valIva=" + valIva + ", glsObservacion='" + glsObservacion + '\'' + ", nomUserCrea='" + nomUserCrea + '\'' + ", userModif='"
            + userModif + '\'' + ", fechaModif=" + fechaModif + ", userOpen='" + userOpen + '\'' + ", flgIsTemporal=" + flgIsTemporal + ", codMonedaImp='" + codMonedaImp + '\''
            + ", nomUser='" + nomUser + '\'' + ", glsEjecutor='" + glsEjecutor + '\'' + ", flgReq2Firma=" + flgReq2Firma + ", usrEjecutor='" + usrEjecutor + '\'' + ", codEstado='"
            + codEstado + '\'' + ", glsEstado='" + glsEstado + '\'' + ", numRol1Firm='" + numRol1Firm + '\'' + ", numRol2Firm='" + numRol2Firm + '\'' + ", numRolRee='" + numRolRee
            + '\'' + ", nomUserOpen='" + nomUserOpen + '\'' + '}';
    }
}
