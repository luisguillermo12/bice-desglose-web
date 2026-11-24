package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.UniqueSessionControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the UniqueSessionControl entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UniqueSessionControlRepository extends JpaRepository<UniqueSessionControl, Long> {

    Optional<UniqueSessionControl> findOneByLoginName(String loginName);

    Long deleteAllByIdIn(List<Long> selectedItemsIds);


    // List<UniqueSessionControl> findAllBy(String loginName);
}
