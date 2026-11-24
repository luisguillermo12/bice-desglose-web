package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.DesgloseChequeoLog;
import cl.tutorial.desglose.domain.pk.DesgloseChequeoLogPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the DesgloseChequeoLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseChequeoLogRepository extends JpaRepository<DesgloseChequeoLog, DesgloseChequeoLogPK> {

}
