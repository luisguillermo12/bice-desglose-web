package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ValPeriodoDesgloseCif;
import cl.tutorial.desglose.domain.ValPeriodoDesglose;
import cl.tutorial.desglose.domain.pk.ValPeriodoDesglosePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.List;

/**
 * Spring Data  repository for the ValPeriodoDesglose entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ValPeriodoDesgloseCifRepository extends JpaRepository<ValPeriodoDesgloseCif, ValPeriodoDesglosePK> {

    List<ValPeriodoDesgloseCif> removeAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndFecPeriodoAndCodPlantilla(String codCliente, Integer codTipoDes,
        Integer codSubtipoDes, String codMoneda, LocalDate fecPeriodo, Integer codPlantilla);

    List<ValPeriodoDesgloseCif> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantilla(String codCliente, Integer codTipoDesglose, Integer codSubtipoDesglose,
        String codMoneda, Integer codPlantilla);

    List<ValPeriodoDesgloseCif> findAllByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndFecPeriodo(String codCliente, Integer codTipoDesglose, Integer codSubtipoDesglose,
        String codMoneda, Integer codPlantilla,LocalDate FecPeriodo);

    @Query(value = "SELECT COUNT(*) AS CUENTA FROM (select COD_CLIENTE,COD_MONEDA,COD_TIPO_DES,COD_SUBTIPO_DES,COD_PLANTILLA,COR_VALOR, \n"
        + "count(*) as cuenta from VALPRD where\n" + "COD_CLIENTE =:codCliente and COD_TIPO_DES =:codTipoDes \n"
        + "and COD_SUBTIPO_DES =:codSubTipo and COD_MONEDA =:codMoneda \n" + "and COD_PLANTILLA =:codPlantilla and COR_CONCEPTO =:corConcepto\n"
        + "GROUP by COD_CLIENTE,COD_MONEDA,COD_TIPO_DES,COD_SUBTIPO_DES,COD_PLANTILLA,COR_VALOR)", nativeQuery = true)
    Integer numberMultiregistros(@Param("codCliente") String codCliente,@Param("codTipoDes") Integer codTipoDes, @Param("codSubTipo") Integer codSubTipo,
        @Param("codMoneda") String codMoneda,@Param("codPlantilla") Integer codPlantilla,@Param("corConcepto") Integer corConcepto);

    void deleteByCodClienteAndCodTipoDesAndCodSubtipoDesAndCodMonedaAndCodPlantillaAndCorConceptoAndCorValor(String codCliente,Integer codTipoDes, Integer codSubTipo,
       String codMoneda,Integer codPlantilla,Integer corConcepto,Integer corValor);
}
