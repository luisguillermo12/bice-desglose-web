package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.EmpresaConsolidadaCif;
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
public interface EmpresaConsolidadaCifRepository extends JpaRepository<EmpresaConsolidadaCif, EmpresaConsolidadaPK> {

}
