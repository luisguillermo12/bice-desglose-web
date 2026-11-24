package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseGrupoCif;
import cl.tutorial.desglose.domain.DesgloseGrupo;
import cl.tutorial.desglose.domain.pk.DesgloseGrupoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the DesgloseGrupo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseGrupoCifRepository extends JpaRepository<DesgloseGrupoCif, DesgloseGrupoPK> {

    List<DesgloseGrupoCif> findAllByNumRolEmpleado(String numRolEmpleado);

    DesgloseGrupoCif findFirstByDesNumRolEmpleado(String numRolEmpleado);
}
