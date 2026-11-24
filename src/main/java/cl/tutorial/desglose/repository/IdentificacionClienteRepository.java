package cl.tutorial.desglose.repository;

import cl.tutorial.desglose.domain.IdentificacionCliente;
import cl.tutorial.desglose.domain.pk.IdentificacionClientePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the IdentificacionCliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IdentificacionClienteRepository extends JpaRepository<IdentificacionCliente, IdentificacionClientePK> {

}
