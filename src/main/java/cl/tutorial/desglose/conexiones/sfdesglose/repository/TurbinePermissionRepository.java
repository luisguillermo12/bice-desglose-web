package cl.tutorial.desglose.conexiones.sfdesglose.repository;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbinePermission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TurbinePermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurbinePermissionRepository extends JpaRepository<TurbinePermission, Long> {

}
