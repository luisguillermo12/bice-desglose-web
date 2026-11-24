package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.DesgloseRevisionLog;
import cl.tutorial.desglose.domain.pk.DesgloseRevisionLogPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the DesgloseRevisionLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseRevisionLogRepository extends JpaRepository<DesgloseRevisionLog, DesgloseRevisionLogPK> {

}
