import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { DesgloseDesgloseModule } from './desglose/desglose.module';
import { DesglosePeriodoModule } from './periodo/periodo.module';
import { DesgloseValPeriodoDesgloseModule } from './val-periodo-desglose/val-periodo-desglose.module';
import { DesgloseValPeriodoDesgImprModule } from './val-periodo-desg-impr/val-periodo-desg-impr.module';
import { DesgloseEmpresaConsolidadaModule } from './empresa-consolidada/empresa-consolidada.module';
import { DesgloseConceptoImpresionModule } from './concepto-impresion/concepto-impresion.module';
import { DesglosePeriodoDesgConsolModule } from './periodo-desg-consol/periodo-desg-consol.module';
import { DesglosePeriodoDesgImpresionModule } from './periodo-desg-impresion/periodo-desg-impresion.module';
import { DesgloseDesgloseImpresionModule } from './desglose-impresion/desglose-impresion.module';
import { DesgloseDesgloseRevisionModule } from './desglose-revision/desglose-revision.module';
import { DesgloseDesgloseFirmaModule } from './desglose-firma/desglose-firma.module';
import { DesgloseDesgloseGrupoModule } from './desglose-grupo/desglose-grupo.module';
import { DesgloseDesgloseReemplazoModule } from './desglose-reemplazo/desglose-reemplazo.module';
import { DesgloseDesgloseMensajeModule } from './desglose-mensaje/desglose-mensaje.module';
import { DesgloseDesgloseAuditoriaModule } from './desglose-auditoria/desglose-auditoria.module';
import { DesgloseClienteSinRutModule } from './cliente-sin-rut/cliente-sin-rut.module';
import { DesgloseDesgloseChequeoLogModule } from './desglose-chequeo-log/desglose-chequeo-log.module';
import { DesgloseDesgloseChequeoInfoModule } from './desglose-chequeo-info/desglose-chequeo-info.module';
import { DesgloseDesgloseRevisionLogModule } from './desglose-revision-log/desglose-revision-log.module';
import { DesgloseCuadreModule } from './cuadre/cuadre.module';
import { DesgloseConceptoModule } from './concepto/concepto.module';
import { DesgloseClienteBasicoModule } from './cliente-basico/cliente-basico.module';
import { DesgloseClienteNaturalModule } from './cliente-natural/cliente-natural.module';
import { DesgloseIdentificacionClienteModule } from './identificacion-cliente/identificacion-cliente.module';
import { DesgloseClienteJuridicoModule } from './cliente-juridico/cliente-juridico.module';
import { DesgloseTurbineRoleModule } from './turbine-role/turbine-role.module';
import { DesgloseTurbineGroupModule } from './turbine-group/turbine-group.module';
import { DesgloseTurbinePermissionModule } from './turbine-permission/turbine-permission.module';
import { DesgloseTurbineUserModule } from './turbine-user/turbine-user.module';
import { DesgloseTurbineRolePermissionModule } from './turbine-role-permission/turbine-role-permission.module';
import { DesgloseTurbineUserGroupRoleModule } from './turbine-user-group-role/turbine-user-group-role.module';
import { DesgloseDescripcionCodigoModule } from './descripcion-codigo/descripcion-codigo.module';
import { DesgloseRelacionTablaModule } from './relacion-tabla/relacion-tabla.module';
import { DesgloseTablaValorModule } from './tabla-valor/tabla-valor.module';
import { DesgloseTablaServicioMailModule } from './tabla-servicio-mail/tabla-servicio-mail.module';
import { DesgloseTablaServMailDestModule } from './tabla-serv-mail-dest/tabla-serv-mail-dest.module';
import { DesgloseSfStfOperatorModule } from './sf-stf-operator/sf-stf-operator.module';
import { DesgloseUniqueSessionControlModule } from './unique-session-control/unique-session-control.module';
import { DesgloseBlackListJwtModule } from './black-list-jwt/black-list-jwt.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        DesgloseDesgloseModule,
        DesglosePeriodoModule,
        DesgloseValPeriodoDesgloseModule,
        DesgloseValPeriodoDesgImprModule,
        DesgloseEmpresaConsolidadaModule,
        DesgloseConceptoImpresionModule,
        DesglosePeriodoDesgConsolModule,
        DesglosePeriodoDesgImpresionModule,
        DesgloseDesgloseImpresionModule,
        DesgloseDesgloseRevisionModule,
        DesgloseDesgloseFirmaModule,
        DesgloseDesgloseGrupoModule,
        DesgloseDesgloseReemplazoModule,
        DesgloseDesgloseMensajeModule,
        DesgloseDesgloseAuditoriaModule,
        DesgloseClienteSinRutModule,
        DesgloseDesgloseChequeoLogModule,
        DesgloseDesgloseChequeoInfoModule,
        DesgloseDesgloseRevisionLogModule,
        DesgloseCuadreModule,
        DesgloseConceptoModule,
        DesgloseClienteBasicoModule,
        DesgloseClienteNaturalModule,
        DesgloseIdentificacionClienteModule,
        DesgloseClienteJuridicoModule,
        DesgloseTurbineRoleModule,
        DesgloseTurbineGroupModule,
        DesgloseTurbinePermissionModule,
        DesgloseTurbineUserModule,
        DesgloseTurbineRolePermissionModule,
        DesgloseTurbineUserGroupRoleModule,
        DesgloseDescripcionCodigoModule,
        DesgloseRelacionTablaModule,
        DesgloseTablaValorModule,
        DesgloseTablaServicioMailModule,
        DesgloseTablaServMailDestModule,
        DesgloseSfStfOperatorModule,
        DesgloseUniqueSessionControlModule,
        DesgloseBlackListJwtModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class DesgloseEntityModule {}
