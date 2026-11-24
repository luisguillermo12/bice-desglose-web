package cl.tutorial.desglose.service.dto;

import java.io.Serializable;

public class ReemplazosVistaDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String cargo01;
    private Long   rol1;
    private String nombre1;
    private String cargo02;
    private Long   rol2;
    private String nombre2;

    public ReemplazosVistaDTO(String cargo01, Long rol1, String nombre1, String cargo02, Long rol2, String nombre2) {
        this.cargo01 = cargo01;
        this.rol1 = rol1;
        this.nombre1 = nombre1;
        this.cargo02 = cargo02;
        this.rol2 = rol2;
        this.nombre2 = nombre2;
    }

    public String getCargo01() {
        return cargo01;
    }

    public void setCargo01(String cargo01) {
        this.cargo01 = cargo01;
    }

    public Long getRol1() {
        return rol1;
    }

    public void setRol1(Long rol1) {
        this.rol1 = rol1;
    }

    public String getNombre1() {
        return nombre1;
    }

    public void setNombre1(String nombre1) {
        this.nombre1 = nombre1;
    }

    public String getCargo02() {
        return cargo02;
    }

    public void setCargo02(String cargo02) {
        this.cargo02 = cargo02;
    }

    public Long getRol2() {
        return rol2;
    }

    public void setRol2(Long rol2) {
        this.rol2 = rol2;
    }

    public String getNombre2() {
        return nombre2;
    }

    public void setNombre2(String nombre2) {
        this.nombre2 = nombre2;
    }
}
