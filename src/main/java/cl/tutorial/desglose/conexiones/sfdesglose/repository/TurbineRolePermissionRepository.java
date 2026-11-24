package cl.tutorial.desglose.conexiones.sfdesglose.repository;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRolePermission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the TurbineRolePermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurbineRolePermissionRepository extends JpaRepository<TurbineRolePermission, Long> {

}
