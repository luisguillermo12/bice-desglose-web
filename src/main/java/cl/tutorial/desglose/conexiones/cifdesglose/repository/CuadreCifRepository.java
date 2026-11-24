package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.CuadreCif;
import cl.tutorial.desglose.domain.Cuadre;
import cl.tutorial.desglose.domain.pk.CuadrePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the Cuadre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CuadreCifRepository extends JpaRepository<CuadreCif, CuadrePK> {

}
