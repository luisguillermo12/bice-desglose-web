package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.PeriodoDesgConsol;
import cl.tutorial.desglose.domain.pk.PeriodoDesgConsolPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the PeriodoDesgConsol entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeriodoDesgConsolRepository extends JpaRepository<PeriodoDesgConsol, PeriodoDesgConsolPK> {

    List<PeriodoDesgConsol> removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda, LocalDate fecPeriodo, Integer codPlantilla);
}
