package cl.tutorial.desglose.service.util;

import javax.persistence.ParameterMode;
import java.util.Objects;

public class Param<T> {

    public String        name;
    public Class         type;
    public T             value;
    public ParameterMode parameterMode;

    public Param(String name, Class type, T value, ParameterMode parameterMode) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.parameterMode = parameterMode;
    }

    public Param(String name, T value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Param))
            return false;
        Param<?> param = (Param<?>) o;
        return Objects.equals(name, param.name) && Objects.equals(type, param.type) && Objects.equals(value, param.value) && parameterMode == param.parameterMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value, parameterMode);
    }

    @Override
    public String toString() {
        return "Param{" + "name='" + name + '\'' + ", type=" + type + ", value=" + value + ", parameterMode=" + parameterMode + '}';
    }
}
