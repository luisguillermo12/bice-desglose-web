package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.TablaServMailDest;
import cl.tutorial.desglose.domain.pk.TablaServicioMailDestPK;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the TablaServMailDest entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TablaServMailDestRepository extends JpaRepository<TablaServMailDest, TablaServicioMailDestPK> {

}
