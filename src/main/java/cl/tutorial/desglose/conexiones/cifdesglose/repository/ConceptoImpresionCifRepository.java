package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ConceptoImpresionCif;
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
public interface ConceptoImpresionCifRepository extends JpaRepository<ConceptoImpresionCif, ConceptoImpresionPK> {

    List<ConceptoImpresionCif> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaOrderByCodTipoConcepAscCorConceptoAsc(String codCliente, Integer codTipoDes, Integer codSubtipoDes,
        String codMoneda,Integer codPlantilla);
}

