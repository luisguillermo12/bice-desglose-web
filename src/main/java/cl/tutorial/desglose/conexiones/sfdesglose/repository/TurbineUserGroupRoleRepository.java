package cl.tutorial.desglose.conexiones.sfdesglose.repository;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUserGroupRole;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.pk.TurbineUserGroupRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Spring Data  repository for the TurbineUserGroupRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurbineUserGroupRoleRepository extends JpaRepository<TurbineUserGroupRole, TurbineUserGroupRolePK> {

    @Modifying
    @Transactional
    @Query(value = "delete from TURBINE_USER_GROUP_ROLE where USER_ID=:userId and GROUP_ID=:groupId and ROLE_ID=:roleId", nativeQuery = true)
    void eliminaUserIdAndGroupIdAndRoleId(@Param("userId") Long userId, @Param("groupId") Long groupId, @Param("roleId") Long roleId);

    List<TurbineUserGroupRole> findAllByUserId(Long userId);
}
