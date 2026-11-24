package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteJuridico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClienteJuridico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteJuridicoRepository extends JpaRepository<ClienteJuridico, CommonClientePK> {

}
