package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseMensajeCif;
import cl.tutorial.desglose.domain.DesgloseMensaje;
import cl.tutorial.desglose.domain.pk.DesgloseMensajePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Spring Data  repository for the DesgloseMensaje entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseMensajeCifRepository extends JpaRepository<DesgloseMensajeCif, DesgloseMensajePK> {

    List<DesgloseMensajeCif> findAllByCodClienteAndCodPlantillaAndCodTipoDesAndCodSubtipoDesAndCodMonedaOrderByFecEventAsc(String codCliente, Integer codPlantilla, Integer codTipoDes,
        Integer codSubTipoDes, String codMoneda);

    List<DesgloseMensajeCif> deleteAllByCodClienteAndCodPlantillaAndCodTipoDesAndCodSubtipoDesAndCodMoneda(String codCliente, Integer codPlantilla, Integer codTipoDes,
        Integer codSubTipoDes, String codMoneda);
}
