package cl.tutorial.desglose.domain.pk;

import cl.tutorial.desglose.service.dto.DesgloseDTO;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

@MappedSuperclass
public class NewCommonPK extends CommonPK implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Integer codPlantilla;

    public Integer getCodPlantilla() {
        return codPlantilla;
    }

    public void setCodPlantilla(Integer codPlantilla) {
        this.codPlantilla = codPlantilla;
    }

    @org.jetbrains.annotations.NotNull
    public static NewCommonPK newInstance(@NotNull DesgloseDTO desgloseDTO) {
        NewCommonPK id = new NewCommonPK();
        id.setCodCliente(desgloseDTO.getCodCliente());
        id.setCodTipoDes(desgloseDTO.getCodTipoDes() != null ? Integer.valueOf(desgloseDTO.getCodTipoDes()) : null);
        id.setCodSubtipoDes(desgloseDTO.getCodSubtipoDes() != null ? Integer.valueOf(desgloseDTO.getCodSubtipoDes()) : null);
        id.setCodMoneda(desgloseDTO.getCodMoneda());
        id.setCodPlantilla(desgloseDTO.getCodPlantilla() != null ? Integer.valueOf(desgloseDTO.getCodPlantilla()) : null);
        return id;
    }

    @org.jetbrains.annotations.NotNull
    public static NewCommonPK newInstance(@NotNull String codCliente, @NotNull Integer codTipoDes, @NotNull Integer codSubtipoDes, @NotNull String codMoneda,
        @NotNull Integer codPlantilla) {

        NewCommonPK id = new NewCommonPK();
        id.setCodCliente(codCliente);
        id.setCodTipoDes(codTipoDes);
        id.setCodSubtipoDes(codSubtipoDes);
        id.setCodMoneda(codMoneda);
        id.setCodPlantilla(codPlantilla);
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof NewCommonPK))
            return false;
        if (!super.equals(o))
            return false;
        NewCommonPK that = (NewCommonPK) o;
        return Objects.equals(codPlantilla, that.codPlantilla);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), codPlantilla);
    }

    @Override
    public String toString() {
        return "NewCommonPK{" + "codPlantilla=" + codPlantilla + ", codCliente='" + codCliente + '\'' + ", codTipoDes=" + codTipoDes + ", codSubtipoDes=" + codSubtipoDes
            + ", codMoneda='" + codMoneda + '\'' + '}';
    }
}
