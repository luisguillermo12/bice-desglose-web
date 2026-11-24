package cl.tutorial.desglose.repository;

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
public interface DesgloseAuditoriaRepository extends JpaRepository<DesgloseAuditoria, DesgloseAuditoriaPK> {

    List<DesgloseAuditoria> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(String CodCliente,
        Integer CodTipoDes,Integer CodSubtipoDes, String CodMoneda,Integer CodPlantilla );
}
