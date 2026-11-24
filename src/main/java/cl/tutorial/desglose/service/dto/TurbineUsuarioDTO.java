package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.util.Objects;

public class TurbineUsuarioDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String loginName;

    private String nombre;

    private String operatorCode;

    private String user_id;

    public TurbineUsuarioDTO(String loginName, String nombre, String operatorCode, String user_id) {
        this.loginName = loginName;
        this.nombre = nombre;
        this.operatorCode = operatorCode;
        this.user_id = user_id;
    }

    public TurbineUsuarioDTO(String nombre, String operatorCode) {
        this.nombre = nombre;
        this.operatorCode = operatorCode;
    }
    public TurbineUsuarioDTO(String nombre) {
        this.nombre = nombre;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(String operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof TurbineUsuarioDTO))
            return false;
        TurbineUsuarioDTO that = (TurbineUsuarioDTO) o;
        return Objects.equals(loginName, that.loginName) && Objects.equals(nombre, that.nombre) && Objects.equals(operatorCode, that.operatorCode) && Objects
            .equals(user_id, that.user_id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loginName, nombre, operatorCode, user_id);
    }

    @Override
    public String toString() {
        return "TurbineUsuarioDTO{" + "loginName='" + loginName + '\'' + ", nombre='" + nombre + '\'' + ", operatorCode='" + operatorCode + '\'' + ", user_id='" + user_id + '\''
            + '}';
    }

}
