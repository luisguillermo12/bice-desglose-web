package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseChequeoInfoCif;
import cl.tutorial.desglose.domain.DesgloseChequeoInfo;
import cl.tutorial.desglose.domain.pk.DesgloseChequeoInfoPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the DesgloseChequeoInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseChequeoInfoCifRepository extends JpaRepository<DesgloseChequeoInfoCif, DesgloseChequeoInfoPK> {

}
