package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.PeriodoDesgImpresion;
import cl.tutorial.desglose.domain.pk.PeriodoDesgImpresionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the PeriodoDesgImpresion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodoDesgImpresionRepository extends JpaRepository<PeriodoDesgImpresion, PeriodoDesgImpresionPK> {

    List<PeriodoDesgImpresion> removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes,
        Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla);
}
