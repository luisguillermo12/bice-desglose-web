package cl.tutorial.desglose.conexiones.sfdesglose.repository;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data  repository for the TurbineUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TurbineUserRepository extends JpaRepository<TurbineUser, Long> {

    TurbineUser findByLoginName(String loginName);

    Optional<TurbineUser> findOneByLoginName(String loginName);
}
