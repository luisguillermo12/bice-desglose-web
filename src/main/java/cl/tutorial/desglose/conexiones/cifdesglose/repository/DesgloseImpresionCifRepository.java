package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseImpresionCif;
import cl.tutorial.desglose.domain.DesgloseImpresion;
import cl.tutorial.desglose.domain.pk.DesgloseImpresionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the DesgloseImpresion entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseImpresionCifRepository extends JpaRepository<DesgloseImpresionCif, DesgloseImpresionPK> {

}
