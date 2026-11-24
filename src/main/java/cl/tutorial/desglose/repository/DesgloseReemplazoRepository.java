package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.DesgloseReemplazo;
import cl.tutorial.desglose.domain.pk.DesgloseReemplazoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the DesgloseReemplazo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseReemplazoRepository extends JpaRepository<DesgloseReemplazo, DesgloseReemplazoPK> {

    Integer countByNumRolEmpleadoAndDesNumRolEmpleado(String numRolEmpleado, String desNumRolEmpleado);

    DesgloseReemplazo findFirstByDesNumRolEmpleado(String desNumRolEmpleado);

    DesgloseReemplazo findFirstByCodCargoAndNumRolEmpleadoAndCodRegStatus(String codCargo,String NumRolEmpleado,Integer codRegStatus);

}
