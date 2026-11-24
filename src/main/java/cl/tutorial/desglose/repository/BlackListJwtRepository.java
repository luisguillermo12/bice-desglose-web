package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.BlackListJwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the BlackListJwt entity.
 */
@Repository
public interface BlackListJwtRepository extends JpaRepository<BlackListJwt, Long> {

    Optional<BlackListJwt> findOneByJwt(String jwt);

    Long deleteAllByIdIn(List<Long> list);

    List<BlackListJwt> findAllByJwt(String jwt);
}
