package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.TablaServicioMail;
import cl.tutorial.desglose.domain.pk.TablaServicioMailPK;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the TablaServicioMail entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaServicioMailRepository extends JpaRepository<TablaServicioMail, TablaServicioMailPK> {

}
