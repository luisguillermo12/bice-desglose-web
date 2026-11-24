package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteNatural;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClienteNatural entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteNaturalRepository extends JpaRepository<ClienteNatural, CommonClientePK> {

}
