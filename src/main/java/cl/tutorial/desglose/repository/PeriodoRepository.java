package cl.tutorial.desglose.repository;

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
public interface PeriodoRepository extends JpaRepository<Periodo, PeriodoPK> {

    List<Periodo> findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda, Integer codTipoPlantilla, Sort orderBy);

    List<Periodo> findDistinctByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda, LocalDate fecPeriodo, Integer codTipoPlantilla, Sort orderBy);
}
