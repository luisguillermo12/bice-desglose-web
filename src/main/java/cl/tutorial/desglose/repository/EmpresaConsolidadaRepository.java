package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.EmpresaConsolidada;
import cl.tutorial.desglose.domain.pk.EmpresaConsolidadaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the EmpresaConsolidada entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmpresaConsolidadaRepository extends JpaRepository<EmpresaConsolidada, EmpresaConsolidadaPK> {

}
