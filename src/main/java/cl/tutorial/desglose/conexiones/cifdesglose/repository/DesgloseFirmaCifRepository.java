package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseFirmaCif;
import cl.tutorial.desglose.domain.DesgloseFirma;
import cl.tutorial.desglose.domain.pk.DesgloseFirmaPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the DesgloseFirma entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseFirmaCifRepository extends JpaRepository<DesgloseFirmaCif, DesgloseFirmaPK> {

    @Modifying
    void deleteByCodCargoAndNumRolEmpleadoAndNumRolEmpleado2fir(@Param("codCargo") String codCargo, @Param("numRolEmpleado") String numRolEmpleado,
        @Param("numRolEmpleado2fir") String numRolEmpleado2Fir);

    List<DesgloseFirmaCif> findAllByNumRolEmpleadoAndNumRolEmpleado2fir(String numRolEmpleado, String numRolEmpleado2Fir);

    List<DesgloseFirmaCif> findAllByNumRolEmpleado2fir(String numRolEmpleado2Fir);

    List<DesgloseFirmaCif> findAllByNumRolEmpleado(String numRolEmpleado);
}
