package cl.tutorial.desglose.service.dto;

import javafx.fxml.FXMLLoader;

import java.math.BigDecimal;
import java.util.Arrays;

public class LogDTO {

    public String fichero;
    public String nombre;
    public String megaBytes;

    @Override
    public String toString() {
        return "LogDTO{" +
            "fichero='" + fichero + '\'' +
            ", nombre='" + nombre + '\'' +
            ", megaBytes=" + megaBytes +
            '}';
    }
}
