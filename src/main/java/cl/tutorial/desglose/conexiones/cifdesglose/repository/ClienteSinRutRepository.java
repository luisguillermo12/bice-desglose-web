package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteSinRut;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the ClienteSinRut entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteSinRutRepository extends JpaRepository<ClienteSinRut, String> {

    Optional<ClienteSinRut> findOneByCodCliente(String codCliente);

    List<ClienteSinRut> findAllByOrderByNomClienteAsc();

    void deleteByCodCliente(String codCliente);

}
