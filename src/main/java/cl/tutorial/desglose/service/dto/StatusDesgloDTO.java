package cl.tutorial.desglose.service.dto;

import java.io.Serializable;

public class StatusDesgloDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    public Boolean isFoundOnDB = false;
    public String  userOpen;
    public String  nomUserOpen = "";
    public Boolean isTemporal  = false;
    public Boolean enProceso   = false;
    public String  codEstado;
    public String  glsEstado;
    public Boolean validado;
    public String  rolEjecutor;
    public String  rolRevisor;
    public String  rolReemplazo;
}
