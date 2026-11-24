package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.PeriodoCif;
import cl.tutorial.desglose.domain.Periodo;
import cl.tutorial.desglose.domain.pk.PeriodoPK;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the Periodo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodoCifRepository extends JpaRepository<PeriodoCif, PeriodoPK> {

    List<PeriodoCif> findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda, Integer codTipoPlantilla, Sort orderBy);

    List<PeriodoCif> findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda, LocalDate fecPeriodo, Integer codTipoPlantilla, Sort orderBy);
}
