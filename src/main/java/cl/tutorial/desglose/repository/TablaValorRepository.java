package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.TablaValor;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the TablaValor entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaValorRepository extends JpaRepository<TablaValor, Long> {

    List<TablaValor> findByCodTablaAndCodRegStatus(String codTabla, Integer status);

    TablaValor findByCodTablaAndCodRegStatusAndFecValidezLessThanEqual(String codTabla, Integer status, LocalDate fechaValidez);

    // TablaValor findByCodTablaAndCodRegStatusAndFecValidezBeforeOrFecValidezLessThanEqual(String codTabla, Integer status, LocalDate fechaValidez);
}
