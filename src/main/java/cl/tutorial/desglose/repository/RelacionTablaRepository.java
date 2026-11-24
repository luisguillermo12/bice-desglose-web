package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.RelacionTabla;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the RelacionTabla entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelacionTablaRepository extends JpaRepository<RelacionTabla, Long> {

}
