package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.Desglose;
import cl.tutorial.desglose.domain.pk.NewCommonPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the Desglose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseRepository extends JpaRepository<Desglose, NewCommonPK> {
    void deleteByFlgIsTemporalAndNomUserOpen(Boolean flgIsTemporal,String nomUserOpen);

    List<Desglose>  findAllByFlgIsTemporal(Boolean flgIsTemporal);

}
