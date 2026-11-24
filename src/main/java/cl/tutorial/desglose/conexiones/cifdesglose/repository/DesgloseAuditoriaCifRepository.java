package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseAuditoriaCif;
import cl.tutorial.desglose.domain.DesgloseAuditoria;
import cl.tutorial.desglose.domain.pk.DesgloseAuditoriaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the DesgloseAuditoria entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseAuditoriaCifRepository extends JpaRepository<DesgloseAuditoriaCif, DesgloseAuditoriaPK> {

    List<DesgloseAuditoriaCif> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(String CodCliente,
        Integer CodTipoDes,Integer CodSubtipoDes, String CodMoneda,Integer CodPlantilla );
}
