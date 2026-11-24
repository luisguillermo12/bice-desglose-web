package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseReemplazoCif;
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
public interface DesgloseReemplazoCifRepository extends JpaRepository<DesgloseReemplazoCif, DesgloseReemplazoPK> {

    Integer countByNumRolEmpleadoAndDesNumRolEmpleado(String numRolEmpleado, String desNumRolEmpleado);

    DesgloseReemplazoCif findFirstByDesNumRolEmpleado(String desNumRolEmpleado);

    DesgloseReemplazoCif findFirstByCodCargoAndNumRolEmpleadoAndCodRegStatus(String codCargo,String NumRolEmpleado,Integer codRegStatus);

}
