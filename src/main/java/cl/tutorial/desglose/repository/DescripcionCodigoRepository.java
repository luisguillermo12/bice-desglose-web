package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.DescripcionCodigo;
import cl.tutorial.desglose.domain.pk.DescripcionCodigoPK;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the DescripcionCodigo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DescripcionCodigoRepository extends JpaRepository<DescripcionCodigo, DescripcionCodigoPK> {

    Page<DescripcionCodigo> findAllByCodTablaInOrderByCodTablaAscCodGenericoAsc(List<String> codtablas,Pageable pageable);

    List<DescripcionCodigo> findDistinctByCodTablaAndCodRegStatus(String codtabla, Integer codRegStatus, Sort orderBy);

    List<DescripcionCodigo> findDistinctByCodTablaInAndCodRegStatus(List<String> codtablas, Integer codRegStatus, Sort orderBy);

    List<DescripcionCodigo> findDistinctByCodTablaInAndCodGenericoStartsWithIgnoreCaseAndCodRegStatus(List<String> codTablas, String codGenerico, Integer codRegStatus,
        Sort orderBy);

    List<DescripcionCodigo> findDistinctByCodTablaAndCodGenericoStartingWithAndCodGenericoNotInAndCodRegStatus(String codTabla, String codGenerico, String codGenerico1,
        Integer codRegStatus);

    DescripcionCodigo findFirstByCodTablaAndCodGenericoStartingWithAndGlsGenericacortaAndCodRegStatus(String codTabla, String codGenerico, String glsGenericacorta,
        Integer codRegStatus);

    DescripcionCodigo findFirstByCodTablaAndCodGenericoAndCodRegStatus(String codTabla, String codGenerico, Integer codRegStatus);
}
