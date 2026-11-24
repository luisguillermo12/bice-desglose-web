package cl.tutorial.desglose.conexiones.cifdesglose.repository;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.DesgloseRevisionCif;
import cl.tutorial.desglose.domain.DesgloseRevision;
import cl.tutorial.desglose.domain.pk.DesgloseRevisionPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

/**
 * Spring Data  repository for the DesgloseRevision entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DesgloseRevisionCifRepository extends JpaRepository<DesgloseRevisionCif, DesgloseRevisionPK> {

    @Query(value = "select cod_cliente,cod_tipo_des,cod_subtipo_des,cod_moneda,rol_digitador,rol_revisor,rol_revisor_reem,gls_notificacion,flg_notificado,fec_envio_revision,fec_en_revision,fec_termino_revision,cod_reg_status,cod_plantilla"
        + " from desglo_rev where cod_cliente=:codCliente and cod_tipo_des=:codTipoDes and cod_subtipo_des=:codSubTipo and cod_moneda=:codMoneda and "
        + "cod_plantilla=:codPlantilla and cod_reg_status=:codRegStatus",nativeQuery = true)
        DesgloseRevisionCif findFirstByConsulta(@Param("codCliente") String codCliente,@Param("codTipoDes") Integer codTipoDes, @Param("codSubTipo") Integer codSubTipo,
        @Param("codMoneda") String codMoneda,@Param("codPlantilla") Integer codPlantilla,@Param("codRegStatus") Integer codRegStatus);

    @Query(value = "select count(*) cont from DESGLO_REV where (rol_revisor=:rolRevisor or rol_revisor in (select num_rol_empleado from desglo_ree where cod_reg_status = 0"
        + " and des_num_rol_empleado =:rolRevisor)) and flg_notificado = 0 and cod_reg_status = 0", nativeQuery = true)
    Integer countAllByNotificacionesRevisorOrJefe(@Param("rolRevisor") String rolRevisor);

    @Query(value = "select count(*) cont from DESGLO_REV where rol_digitador=:rolDigitador and cod_reg_status = 2 "
        + " and fec_envio_revision is not null and flg_notificado = 0", nativeQuery = true)
    Integer countAllByNotificacionesDigitador(@Param("rolDigitador") String rolDigitador);

}
