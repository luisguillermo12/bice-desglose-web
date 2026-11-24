package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.PeriodoDesgConsolCif;
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
public interface PeriodoDesgConsolCifRepository extends JpaRepository<PeriodoDesgConsolCif, PeriodoDesgConsolPK> {

    List<PeriodoDesgConsolCif> removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda, LocalDate fecPeriodo, Integer codPlantilla);
}
