package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.SfStfOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.Optional;

/**
 * Spring Data  repository for the SfStfOperator entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SfStfOperatorRepository extends JpaRepository<SfStfOperator, Long> {

    SfStfOperator findFirstByLoginName(String loginName);

    Optional<SfStfOperator> findOneByOperatorCode(Integer operatorCode);
}
