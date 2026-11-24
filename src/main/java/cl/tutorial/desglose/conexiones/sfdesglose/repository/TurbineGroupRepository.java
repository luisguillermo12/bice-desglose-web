package cl.tutorial.desglose.conexiones.sfdesglose.repository;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TurbineGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurbineGroupRepository extends JpaRepository<TurbineGroup, Long> {

}
