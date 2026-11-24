package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.ValPeriodoDesgImpr;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesgImprPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the ValPeriodoDesgImpr entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValPeriodoDesgImprRepository extends JpaRepository<ValPeriodoDesgImpr, ValPeriodoDesgImprPK> {

    List<ValPeriodoDesgImpr> removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes,
        Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla);
    void deleteByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndCorConceptoAndCorValor(String codCliente,Integer codTipoDes, Integer codSubTipo,
        String codMoneda,Integer codPlantilla,Integer corConcepto,Integer corValor);
}
