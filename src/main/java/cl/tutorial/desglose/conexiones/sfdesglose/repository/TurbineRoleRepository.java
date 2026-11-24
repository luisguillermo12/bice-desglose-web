package cl.tutorial.desglose.conexiones.sfdesglose.repository;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data  repository for the TurbineRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurbineRoleRepository extends JpaRepository<TurbineRole, Long> {

    Optional<TurbineRole> findByRoleName(String roleName);

    List<TurbineRole> findAllByRoleIdIn(Set<Long> rolesId);

    List<TurbineRole> findAllByRoleIdInAndRoleNameStartingWithIgnoreCase(Set<Long> rolesId, String roleNameDesglo);
}
