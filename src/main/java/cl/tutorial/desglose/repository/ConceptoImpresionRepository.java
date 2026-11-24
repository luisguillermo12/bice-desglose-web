package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.ConceptoImpresion;
import cl.tutorial.desglose.domain.pk.ConceptoImpresionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the ConceptoImpresion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConceptoImpresionRepository extends JpaRepository<ConceptoImpresion, ConceptoImpresionPK> {

    List<ConceptoImpresion> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaOrderByCodTipoConcepAscCorConceptoAsc(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda,Integer codPlantilla);
}

