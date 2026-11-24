package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.ValPeriodoProyeccionDesglose;
import cl.tutorial.desglose.domain.pk.ValPeriodoProyeccionDesglosePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

/**
 * @author  carlos eduardo rodriguez perez
 */
@SuppressWarnings("unused")
@Repository
public interface ValPeriodoProyeccionDesgloseRepository extends JpaRepository<ValPeriodoProyeccionDesglose, ValPeriodoProyeccionDesglosePK> {
    @Modifying
    void deleteByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndCodPlantillaPry(String codCliente,Integer codTipoDes, Integer codSubTipo,
        String codMoneda,Integer codPlantilla,Integer codPlantillaPry);
}
