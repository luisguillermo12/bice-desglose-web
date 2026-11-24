package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseCif;
import cl.tutorial.desglose.domain.Desglose;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the Desglose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseCifRepository extends JpaRepository<DesgloseCif, NewCommonPK> {
    void deleteByFlgIsTemporal(Boolean flgIsTemporal);
}
