package cl.tutorial.desglose.security;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Objects;

//@Component
//@Scope("session")
public class UserDetailsCustom implements Serializable {

    private Integer operatorCode;

    public UserDetailsCustom() {
    }

    public Integer getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(Integer operatorCode) {
        this.operatorCode = operatorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserDetailsCustom))
            return false;
        UserDetailsCustom that = (UserDetailsCustom) o;
        return Objects.equals(operatorCode, that.operatorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operatorCode);
    }

    @Override
    public String toString() {
        return "UserDetailsCustom{" + "operatorCode='" + operatorCode + '\'' + '}';
    }
}
