package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteBasico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the ClienteBasico entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteBasicoRepository extends JpaRepository<ClienteBasico, CommonClientePK> {

}
