package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.PeriodoDesgImpresionCif;
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
public interface PeriodoDesgImpresionCifRepository extends JpaRepository<PeriodoDesgImpresionCif, PeriodoDesgImpresionPK> {

    List<PeriodoDesgImpresionCif> removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes,
        Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla);
}
