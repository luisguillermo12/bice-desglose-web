package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseChequeoLogCif;
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
public interface DesgloseChequeoLogCifRepository extends JpaRepository<DesgloseChequeoLogCif, DesgloseChequeoLogPK> {

}
