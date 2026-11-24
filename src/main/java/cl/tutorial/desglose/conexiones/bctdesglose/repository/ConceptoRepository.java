package cl.tutorial.desglose.conexiones.bctdesglose.repository;

import cl.tutorial.desglose.conexiones.bctdesglose.domain.Concepto;
import cl.tutorial.desglose.conexiones.bctdesglose.domain.pk.ConceptoPK;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Concepto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ConceptoRepository extends JpaRepository<Concepto, ConceptoPK> {

    List<Concepto> findAllByCodTipoPlantillaAndCodConceptoNotLike(Integer codTipoPlantilla, String codConcepto);

    List<Concepto> findAllByCodTipoPlantillaAndCodConceptoNotLikeAndFlgConActivo(Integer codTipoPlantilla, String codConcepto, Integer flgConActivo);

    List<Concepto> findAllByCodTipoPlantilla(Integer codTipoPlantilla);

    List<Concepto> findAllByCodTipoPlantillaAndFlgConActivo(Integer codTipoPlantilla, Integer flgConActivo);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepIn(Integer codTipoPlantilla, List<String> conceptTypeCodes, Sort orderBy);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepInAndFlgConActivo(Integer codTipoPlantilla, List<String> conceptTypeCodes, Integer flgConActivo, Sort orderBy);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepIn(Integer codTipoPlantilla, List<String> conceptTypeCodes);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepInAndFlgConActivo(Integer codTipoPlantilla, List<String> conceptTypeCodes, Integer flgConActivo);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepAndCodConceptoLikeOrderByCodConceptoAsc(Integer codTipoPlantilla, String codTipoConcepto, String codConcepto);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepAndCodConceptoLikeAndFlgConActivoOrderByNumSecuenciaAsc(Integer codTipoPlantilla, String codTipoConcepto,
        String codConcepto, Integer flgConActivo);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepAndCodConcepto(Integer codTipoPlantilla, String codTipoConcepto, String codConcepto);

    List<Concepto> findAllByCodTipoPlantillaAndCodTipoConcepAndCodConceptoAndFlgConActivo(Integer codTipoPlantilla, String codTipoConcepto, String codConcepto,
        Integer flgConActivo);
}
