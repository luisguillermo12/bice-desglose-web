package cl.tutorial.desglose.repository;

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
public interface DesgloseMensajeRepository extends JpaRepository<DesgloseMensaje, DesgloseMensajePK> {

    List<DesgloseMensaje> findAllByCodClienteAndCodPlantillaAndCodTipoDesAndCodSubtipoDesAndCodMonedaOrderByFecEventAsc(String codCliente, Integer codPlantilla, Integer codTipoDes,
        Integer codSubTipoDes, String codMoneda);

    List<DesgloseMensaje> deleteAllByCodClienteAndCodPlantillaAndCodTipoDesAndCodSubtipoDesAndCodMoneda(String codCliente, Integer codPlantilla, Integer codTipoDes,
        Integer codSubTipoDes, String codMoneda);
}
