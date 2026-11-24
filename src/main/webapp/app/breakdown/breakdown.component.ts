import {AfterViewInit, Component, ElementRef, HostListener, OnInit, Renderer2, ViewChild} from '@angular/core';
import {Desglose, IDesglose} from 'app/shared/model/desglose.model';
import {NgbAccordionConfig, NgbModal, NgbModalRef, NgbTabChangeEvent} from '@ng-bootstrap/ng-bootstrap';
import {isNullOrUndefined} from 'util';
import {ActivatedRoute, Router} from '@angular/router';
import {BreakdownSearchPopupComponent} from 'app/breakdown/breakdown-search-popup/breakdown-search-popup.component';
import {DescripcionCodigoService} from 'app/entities/descripcion-codigo';
import {IDescripcionCodigo} from 'app/shared/model/descripcion-codigo.model';
import {HttpErrorResponse, HttpHeaders, HttpResponse} from '@angular/common/http';
import * as jQuery from 'jquery';
import {
    CANT_MESES_PER,
    CANT_MESES_PER0,
    CANT_MESES_PER00,
    CANT_MESES_PER01,
    CANT_MESES_PER1,
    CANT_MESES_PER2,
    CANT_MESES_PER3,
    CANT_MESES_PER4,
    CNPT,
    COD_GENERICO_PROYECTAR,
    COD_REG_STATUS_ENVIA_REE,
    COD_REG_STATUS_VIGENTE_REE,
    COD_SUBTIPO_DESG_OTHER1,
    COD_SUBTIPO_DESG_OTHER2,
    COD_TIPO_CONCEP_BALANCE,
    COD_TIPO_CONCEP_EST_RESULT,
    COD_TIPO_CONCEP_GEN_FONDOS,
    COD_TIPO_CONCEP_IND_FINAN,
    COD_TIPO_DESG_BASE_NUEVA,
    COD_TIPO_DESG_CONSOLIDADO,
    COD_TIPO_MONEDA_PESOS,
    CODTABLA_AUDITORES,
    CODTABLA_ESTADO_DESGLOSE,
    CODTABLA_SUBTIPO_DESGLOSE,
    CODTABLA_TIPO_CLIENTE,
    CODTABLA_TIPO_DESGLOSE,
    CODTABLA_TIPO_MONEDAS,
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE,
    CODTABLA_TIPO_RESPUESTA_AUDITOR,
    CODTABLA_UNIDAD_MONETARIA,
    COL_NUMBER_COMPANY_NAME,
    COLUMN_LETTER_GLS_FORMULA_DEF1,
    COLUMN_LETTER_GLS_FORMULA_DEF2,
    COLUMN_LETTER_GLS_FORMULA_DEF3,
    COLUMN_LETTER_GLS_FORMULA_DEF4,
    COLUMN_LETTER_GLS_FORMULA_VAL1,
    COLUMN_LETTER_GLS_FORMULA_VAL2,
    COLUMN_LETTER_GLS_FORMULA_VAL3,
    COLUMN_LETTER_GLS_FORMULA_VAL4,
    COLUMN_NUMBER_CCC,
    COLUMN_NUMBER_COD_CONCEPT,
    COLUMN_NUMBER_COD_TIPO_CONCEP,
    COLUMN_NUMBER_COD_TIPO_REGIS,
    COLUMN_NUMBER_FLG_IS_TOTAL,
    COLUMN_NUMBER_GLS_FORMULA_DEF,
    COLUMN_NUMBER_GLS_FORMULA_DEF_LETTERS,
    COLUMN_NUMBER_GLS_FORMULA_PRC,
    COLUMN_NUMBER_GLS_FORMULA_VAL,
    COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS,
    COMENTARIO_AUDITORIA_CAMBIO_ESTADO,
    CSS_CLASS_HEADER_GROUP_RENDERER, CSS_CLASS_HEADER_GROUP_RENDERER_ZEROS,
    CSS_CLASS_HEADER_INFO_RENDERER,
    CSS_CLASS_HEADER_TOTAL_NUMBER_RENDERER,
    CSS_CLASS_HEADER_TOTAL_RENDERER,
    CSS_CLASS_HEADER_TOTAL_RENDERER_NUMERIC,
    CSS_CLASS_HEADER_TOTAL_RENDERER_ZERO,
    CSS_CLASS_HEREDO,
    CSS_CLASS_ZERO,
    DEBUG_INFO_ENABLED,
    DEF_PER1,
    DEF_PER2,
    DEF_PER3,
    DEF_PER4,
    DEFLACT_BASE_IVA,
    DEFLACT_PERIODS,
    DEFLACTOR_BASE_DEFAULT_VALUE,
    ESTADO_BORRADOR_EN_PROCESO,
    ESTADO_BORRADOR_TERMINADO,
    ESTADO_BORRADOR_VALIDADO,
    ESTADO_CORREGIDO,
    ESTADO_DEFINITIVO,
    ESTADO_EN_EDICION,
    EVENTO_AUDITORIA_CAMBIO_ESTADO,
    FIELD_NAME_COD_TIPO_CONCEP,
    FIELD_NAME_NUM_SECUENCIA,
    HANDS_ON_TABLE_CUSTOM_CULTURE,
    HANDS_ON_TABLE_LICENSE_KEY,
    HANDS_ON_TABLE_NUMERIC_PATTERN_3,
    HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
    ICON_CARD_CLOSE,
    ICON_CARD_OPEN,
    INFINITY,
    INFINITY_STR,
    MULTI_REGISTRY,
    NAME_VALIDATION_PATTERN,
    P_ANT,
    REGEX_NUMBER,
    RUT_VALIDATION_PATTERN,
    RUT_VERIFICATION_NUMBER_VALIDATION_PATTERN,
    SESSION_REEMPLAZO_AUTENTICATE,
    SESSION_VARIABLE_BUSQUEDA_DESGLO,
    SESSION_VARIABLE_CLOSE,
    SESSION_VARIABLE_ECONOMIC_ACTIVITIES,
    SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES,
    SESSION_VARIABLE_USUARIO,
    SESSION_VARIABLE_USUARIO_NAME,
    SUB_TIPO_DESGLOSE_OTRA_MONEDA,
    SUB_TIPO_DESGLOSE_PESOS,
    UNIDADMONETARIADEFAULT,
    USER_ROLE_PLAT_DESGLOSE,
    USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR,
    USER_ROLE_PLAT_DESGLOSE_CUSTOM_CLONE_ROLE,
    USER_ROLE_PLAT_DESGLOSE_EJECUTOR,
    USER_ROLE_PLAT_DESGLOSE_JEFE,
    USER_ROLE_PLAT_DESGLOSE_REVISOR,
    VALUE_IS_ACTIVE_CONCEPT_TRUE
} from 'app/app.constants';
import * as moment from 'moment';
import {JhiAlertService} from 'ng-jhipster';
import {IEconomicActivity} from 'app/shared/model/economic-activity.model';
import {CustomSqlService} from 'app/shared/services/custom-sql.service';
import {SessionStorageService} from 'ngx-webstorage';
import {ClienteService} from 'app/shared/services/cliente.service';
import {Cliente, ClientType, ICliente} from 'app/shared/model/cliente.model';
import {NgForm} from '@angular/forms';
import {BreakdownClientsDialogComponent} from 'app/breakdown/breakdown-clients-dialog/breakdown-clients-dialog.component';
import {
    DATE_FORMAT_CREATE_MOMENT_OBJECT,
    DATE_FORMAT_PERIOD_ES_UI_HEADER,
    DATE_TIME_FORMAT,
    Sweetalert2Component
} from 'app/shared';
import {Observable, Subscription} from 'rxjs';
import {BreakdownCreateClientDialogComponent} from 'app/breakdown/breakdown-create-client-dialog/breakdown-create-client-dialog.component';
import {IBreakdownFilterResponseDTO} from 'app/entities/desglose/filter-breakdown-response.model';
import {TablaValorService} from 'app/entities/tabla-valor';
import {ITablaValor, TablaValor} from 'app/shared/model/tabla-valor.model';
import {FilterDescripcionCodigoPipe} from 'app/shared/pipes/filter-descripcion-codigo.pipe';
import {TransformIvaPipe} from 'app/shared/pipes/transform-iva.pipe';
import {ValidationUtils} from 'app/shared/util/validation-utils';
import {BreakdownConsolidateCompaniesDialogComponent} from 'app/breakdown/breakdown-consolidate-companies-dialog/breakdown-consolidate-companies-dialog.component';
import {IPeriodo} from 'app/shared/model/periodo.model';
import {IEmpresaConsolidada} from 'app/shared/model/empresa-consolidada.model';
import {BreakdownSelectAuditorDialogComponent} from 'app/breakdown/breakdown-select-auditor-dialog/breakdown-select-auditor-dialog.component';
import {BreakdownOtherDataConfigDialogComponent} from 'app/breakdown/breakdown-other-data-config-dialog/breakdown-other-data-config-dialog.component';
import {DesgloseService} from 'app/entities/desglose';
import {BreakdownEditRequestDTO, IBreakdownEditRequestDTO} from 'app/entities/desglose/edit-breakdown-request.model';
import {TranslateService} from '@ngx-translate/core';
import {BreakdownAuditor} from 'app/entities/desglose/breakdown-auditor.model';
import {Concepto, IConcepto} from 'app/shared/model/concepto.model';
import {
    ConceptRequestDTO,
    IConceptRequestDTO,
    OrderByDirection
} from 'app/entities/concepto/filter-concept-request.model';
import {ConceptoService} from 'app/entities/concepto';
import {BreakdownCloneConfigDialogComponent} from 'app/breakdown/breakdown-clone-config-dialog/breakdown-clone-config-dialog.component';
import {DatePipe} from '@angular/common';
import {HotTableRegisterer} from '@handsontable/angular';
import {ExtraDataDesglose, IExtraDataDesglose} from 'app/shared/model/extra-data-desglose.model';
import {
    BreakdownSearchRequestDTO,
    IBreakdownSearchRequestDTO
} from 'app/entities/desglose/search-breakdown-request.model';
import {IBreakdownRecoverResponseDTO} from 'app/entities/desglose/recover-breakdown-response.model';
import {IValPeriodoDesglose} from 'app/shared/model/val-periodo-desglose.model';
import {BreakdownCloneRequestDTO, IBreakdownCloneRequestDTO} from 'app/entities/desglose/clone-breakdown-request.model';
import {BreakdownGeneratePrintDialogComponent} from 'app/breakdown/breakdown-generate-print-dialog/breakdown-generate-print-dialog.component';
import {EmprConsolPeriodoDesgConsol} from 'app/shared/model/empr-consol-periodo-desg-consol.model';
import {PeriodoService} from 'app/entities/periodo';
import {BreakdownSelectionPrintDialogComponent} from 'app/breakdown/breakdown-selection-print-dialog/breakdown-selection-print-dialog.component';
import {IConceptoImpresion} from 'app/shared/model/concepto-impresion.model';
import {IPdfIngressReport} from 'app/shared/model/pdf-ingress-report.model';
import {ReportService} from 'app/shared/services/report.service';
import {IPdfSummaryReport} from 'app/shared/model/pdf-summary-report.model';
import {ISector} from 'app/shared/model/sector.model';
import {ISubSector} from 'app/shared/model/sub-sector.model';
import {ITurbineUser} from 'app/shared/model/turbine-user.model';
import {ValPeriodoDesgImpr} from 'app/shared/model/val-periodo-desg-impr.model';
import {ValPeriodoDesgImprService} from 'app/entities/val-periodo-desg-impr';
import {PeriodoDesgImpresionService} from 'app/entities/periodo-desg-impresion';
import {DesgloseImpresionService} from 'app/entities/desglose-impresion';
import {IBreakdownValidationError} from 'app/entities/desglose/breakdown-validation-error.model';
import {Account, LoginService, Principal} from 'app/core';
import {BreakdownErrorsDialogComponent} from 'app/breakdown/breakdown-errors-dialog/breakdown-errors-dialog.component';
import {DesgloseFirmaService} from 'app/entities/desglose-firma';
import {DesgloseGrupoService} from 'app/entities/desglose-grupo';
import {IDesgloseFirma} from 'app/shared/model/desglose-firma.model';
import {IDesgloseGrupo} from 'app/shared/model/desglose-grupo.model';
import {SfStfOperatorService} from 'app/entities/sf-stf-operator';
import {SfStfOperator} from 'app/shared/model/sf-stf-operator.model';
import {BreakdownShippingReviewComponent} from 'app/breakdown/breakdown-shipping-review/breakdown-shipping-review.component';
import {DesgloseRevisionService} from 'app/entities/desglose-revision';
import {DesgloseRevision, IDesgloseRevision} from 'app/shared/model/desglose-revision.model';
import {DesgloseMensajeService} from 'app/entities/desglose-mensaje';
import {DesgloseMensaje, IDesgloseMensaje} from 'app/shared/model/desglose-mensaje.model';
import {BreakdownMessagesComponent} from 'app/breakdown/breakdown-messages/breakdown-messages.component';
import {BreakdownSendRequest, IBreakdownSendRequest} from 'app/entities/desglose/send-breakdown-request.model';
import {DesgloseAuditoria, IDesgloseAuditoria} from 'app/shared/model/desglose-auditoria.model';
import Handsontable from 'handsontable';
import {TurbineUserService} from 'app/entities/turbine-user';
import * as numbro from 'numbro';
import * as spanishChileNumberCulture from 'numbro/languages/es-CL';
import {ITurbineUsuarios} from 'app/shared/model/turbine-usuarios.model';
import {LoaderService} from 'app/core/load/loader.service';
import {ComponentCanDeactivate} from 'app/shared/guards/pending-changes-guard';
import {DesgloseImpresionDTO} from 'app/entities/desglose/desglose-impresion.model';
import {ILoadDeudaSbifDTO, LoadDeudaSbifDTO} from 'app/shared/model/load-deuda-sbif-deudasbif.model';
import {
    DatosConsultaSbifRequestDTO,
    IDatosConsultaSbifRequestDTO
} from 'app/shared/model/load-deuda-sbif-datos-request';
import {LoadDeudaSbifService} from 'app/shared/services/load-deuda-sbif-service';
import {LocalStorage} from '@ng-idle/core';
import {ValPeriodoDesgloseService} from 'app/entities/val-periodo-desglose';
import {IBreakdownFilterConceptDto} from 'app/shared/model/breakdown-filter-concept-dto';
import {ProjectionsComponent} from 'app/projections';
// import { FloatingActionButton } from 'ng-floating-action-menu';
// import { HotTableComponent, HotTableRegisterer } from '@handsontable/angular';
// import { HotRegisterer } from 'angular-handsontable';
// @ts-ignore
numbro.registerLanguage(spanishChileNumberCulture);
function headerGroupTextRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    if(col === COLUMN_NUMBER_COD_CONCEPT || col === COLUMN_NUMBER_CCC) {
        td.className = CSS_CLASS_HEADER_GROUP_RENDERER;
    } else {
        td.className = CSS_CLASS_HEADER_GROUP_RENDERER_ZEROS;
    }
}

function headerTotalNumericRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.NumericRenderer.apply(this, arguments);
    td.className = CSS_CLASS_HEADER_TOTAL_NUMBER_RENDERER;
    if (value !== INFINITY && value !== INFINITY_STR) {
        if (!isNaN(Number(value))) {
            if (Number(value) === 0) {
                td.className = CSS_CLASS_HEADER_TOTAL_RENDERER_ZERO;
            } else {
                td.className = CSS_CLASS_HEADER_TOTAL_RENDERER_NUMERIC;
            }
        } else {
            td.className = CSS_CLASS_HEADER_TOTAL_RENDERER_ZERO;
        }
    } else {
        td.className = CSS_CLASS_HEADER_TOTAL_RENDERER_ZERO;
    }
}

function validNumeric(value, callback) {
    if (!isNaN(Number(value))) {
        if (Number(value).toString().length > 1) {
            callback(false);
        }
        callback(true);
    } else {
        callback(false);
    }
}
/*function validText(value, callback) {
    const btnSave = <HTMLButtonElement> document.getElementById('buttonSave');
    const btnProcess = <HTMLButtonElement> document.getElementById('buttonInProcess');
    const btnPrevius = <HTMLButtonElement> document.getElementById('agregarMultiregistro');
    const btnNext = <HTMLButtonElement> document.getElementById('quitarMultiregistro');
    if(repetidoMultiregistro <= 1) {
        if ((value.toString().trim() !== '' && value.toString().trim() !== '0')) {
            btnSave.disabled = false;
            btnProcess.disabled = false;
            btnPrevius.disabled = false;
            btnNext.disabled = false;
            callback(true);
        } else {
            btnSave.disabled = true;
            btnProcess.disabled = true;
            btnPrevius.disabled = true;
            btnNext.disabled = true;
            callback(false);
        }
    } else {
        btnSave.disabled = true;
        btnProcess.disabled = true;
        btnPrevius.disabled = true;
        btnNext.disabled = true;
        callback(true);
    }
}*/

function initColumnas(lectura: boolean, period?: number) {
    if (isNullOrUndefined(period)) {
        return [
            { type: 'text', readOnly: true },   // 0 A - COD_TIPO_CONCEP
            { type: 'text', readOnly: true },   // 1 B - COD_TIPO_REGIS
            { type: 'text', readOnly: true },   // 2 C - FLG_IS_TOTAL
            { type: 'text', readOnly: true },   // 3 D - Código
            { type: 'text', readOnly: true , renderer: 'zeroValueRenderer1'},   // 4 E - Concepto cuenta contable
            {                                   // 5 F - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
                // validator: validNumeric, allowInvalid: false
            },
            {                                   // 6 G - valor porciento período 1
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 7 H - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 8 I - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 9 J - valor porciento período 2
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 10 K - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 11 L - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 12 M - valor porciento período 3
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 13 N - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 14 O - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 15 P - valor porciento período 4
                type: 'text',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 16 Q - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            }
        ];
    } else {
        let lectura_per1 = true;
        let lectura_per2 = true;
        let lectura_per3 = true;
        let lectura_per4 = true;
        switch (period) {
            case 0: {
                lectura_per1 = false;
                break;
            }
            case 1: {
                lectura_per2 = false;
                break;
            }
            case 2: {
                lectura_per3 = false;
                break;
            }
            case 3: {
                lectura_per4 = false;
                break;
            }
        }
        return [
            { type: 'text', readOnly: true },   // 0 A - COD_TIPO_CONCEP
            { type: 'text', readOnly: true },   // 1 B - COD_TIPO_REGIS
            { type: 'text', readOnly: true },   // 2 C - FLG_IS_TOTAL
            { type: 'text', readOnly: true },   // 3 D - Código
            { type: 'text', readOnly: true,renderer: 'zeroValueRenderer1' },   // 4 E - Concepto cuenta contable
            {                                   // 5 F - valor fórmula
                type: 'numeric',
                readOnly: lectura_per1,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
                // validator: validNumeric, allowInvalid: false
            },
            {                                   // 6 G - valor porciento período 1
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 7 H - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 8 I - valor fórmula
                type: 'numeric',
                readOnly: lectura_per2,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 9 J - valor porciento período 2
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 10 K - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 11 L - valor fórmula
                type: 'numeric',
                readOnly: lectura_per3,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 12 M - valor porciento período 3
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 13 N - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 14 O - valor fórmula
                type: 'numeric',
                readOnly: lectura_per4,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 15 P - valor porciento período 4
                type: 'text',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
            },
            {                                   // 16 Q - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            }
        ];
    }
}
function initColumnas1(lectura: boolean) {
        return [
            { type: 'text', readOnly: true },   // 0 A - COD_TIPO_CONCEP
            { type: 'text', readOnly: true },   // 1 B - COD_TIPO_REGIS
            { type: 'text', readOnly: true },   // 2 C - FLG_IS_TOTAL
            { type: 'text', readOnly: true },   // 3 D - Código
            { type: 'text', readOnly: true },   // 4 E - Concepto cuenta contable
            {                                   // 5 F - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
                // validator: validNumeric, allowInvalid: false
            },
            {                                   // 6 G - valor porciento período 1
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 7 H - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 8 I - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 9 J - valor porciento período 2
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 10 K - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 11 L - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 12 M - valor porciento período 3
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 13 N - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 14 O - valor fórmula
                type: 'numeric',
                readOnly: lectura,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 15 P - valor porciento período 4
                type: 'text',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            },
            {                                   // 16 Q - valor deflactor
                type: 'numeric',
                readOnly: true,
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE
                },
                renderer: 'zeroValueRenderer'
            }
        ];
}

Handsontable.renderers.registerRenderer('zeroValueRenderer', function(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.NumericRenderer.apply(this, arguments);
    if (value !== INFINITY && value !== INFINITY_STR) {
        if (!isNaN(Number(value))) {
            if (Number(value) === 0) {
                td.className = CSS_CLASS_ZERO;
            } else {
                td.className = CSS_CLASS_HEREDO;
            }
        } else {
            td.className = CSS_CLASS_ZERO;
        }
    } else {
        td.className = CSS_CLASS_ZERO;
    }
});
Handsontable.renderers.registerRenderer('zeroValueRenderer1', function(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    if (value === '0' || value === 0) {
        td.className = CSS_CLASS_ZERO;
    }
});
let setter = false;
Handsontable.hooks.add('beforeChange', function(changes, source) {
    const hotInstance = this;
    changes.forEach(function(item) {
        const prop: any = item[ 3 ];
        if ((isNullOrUndefined(item[ 3 ]) || item[ 3 ] === '') && ( item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_VAL[0] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_VAL[1] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_VAL[2] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_VAL[3]) ) {
            item[ 3 ] = 0;
            setter = true;
        }
        if ((isNullOrUndefined(item[ 3 ]) || item[ 3 ] === '') && ( item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_DEF[0] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_DEF[1] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_DEF[2] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_DEF[3]) ) {
            item[ 3 ] = 0;
            setter = true;
        }
        if ((isNullOrUndefined(item[ 3 ]) || item[ 3 ] === '' ) && ( item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_PRC[0] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_PRC[1] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_PRC[2] || item[ 1 ] === COLUMN_NUMBER_GLS_FORMULA_PRC[3]) ) {
            item[ 3 ] = 0;
            setter = true;
        }
        if (!isNullOrUndefined(prop) && prop.toString().includes(',')) {
            item[ 3 ] = prop.replace(new RegExp(',', 'g'), '.');
        }
    });
});
Handsontable.hooks.add('afterCreateRow', function(index, amount) {
    const hotInstance = this;
    if(automatic) {
        if (!isNullOrUndefined(hotInstance)) {
            if (!isNullOrUndefined(conceptos[ index ])) {
                if (!isNullOrUndefined(hotInstance.container.parentNode.id) && hotInstance.container.parentNode.id === 'handOnTableInstanceIdDetails') {
                    hotInstance.setDataAtCell(index, 0, conceptos[ index ].codTipoConcep);
                    hotInstance.setDataAtCell(index, 1, conceptos[ index ].codTipoRegis);
                    hotInstance.setDataAtCell(index, 2, conceptos[ index ].flgIsTotal);
                    hotInstance.setDataAtCell(index, 3, conceptos[ index ].codConcepto);
                    hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_VAL[0], 0);
                    hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_VAL[1], 0);
                    hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_VAL[2], 0);
                    hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_VAL[3], 0);
                    hotInstance.selectCell(index, 4);
                    automatic = false;
                }
            }
        }
    }
});
Handsontable.hooks.add('afterChange', function(changes, source) {
    const hotInstance = this;
    if (!isNullOrUndefined(hotInstance)) {
        if (source === 'edit') {
            changes.forEach(function(item) {
                const fila = item[ 0 ];
                const prop = item[ 1 ];
                let newValue = item[ 3 ];
                if (setter) {
                    setter = false;
                    hotInstance.setDataAtCell(fila + 0, prop + 0, newValue + 0.00001);
                    setTimeout(() => {
                        hotInstance.setDataAtCell(fila + 0, prop + 0, newValue + 0.00000);
                        hotInstance.render();
                    }, 50);
                }
                if (hotInstance.getSourceData()[ fila ][ COLUMN_NUMBER_COD_TIPO_REGIS ] === MULTI_REGISTRY) {
                    if (!isNullOrUndefined(item[ 3 ]) && prop === COLUMN_NUMBER_CCC) {
                        let cuenta = 0;
                        for (let i = 0; i < hotInstance.getSourceData().length; i++) {
                            if (hotInstance.getSourceData()[ i ][ COLUMN_NUMBER_COD_TIPO_CONCEP ] === hotInstance.getSourceData()[ fila ][ COLUMN_NUMBER_COD_TIPO_CONCEP ]
                                && hotInstance.getSourceData()[ i ][ COLUMN_NUMBER_COD_CONCEPT ] === hotInstance.getSourceData()[ fila ][ COLUMN_NUMBER_COD_CONCEPT ]) {
                                if (!isNullOrUndefined(hotInstance.getSourceData()[ i ][ COLUMN_NUMBER_CCC ]) && hotInstance.getSourceData()[ i ][ COLUMN_NUMBER_CCC ]!==''
                                    && hotInstance.getSourceData()[ i ][ COLUMN_NUMBER_CCC ] === hotInstance.getSourceData()[ fila ][ prop ]) {
                                    cuenta++;
                                }
                            }
                        }
                        if (cuenta > 1) {
                            newValue = '';
                            item[ 3 ] = '';
                            setTimeout(() => {
                                hotInstance.setDataAtCell(fila + 0, prop + 0, '');
                            }, 10);
                        }
                    }
                }
            });
        }
    }
});
Handsontable.hooks.add('afterRemoveRow', function(index, amount) {
    const hotInstance = this;
    if(!isNullOrUndefined(hotInstance)) {
        if (!isNullOrUndefined(hotInstance.container.parentNode.id) && hotInstance.container.parentNode.id === 'handOnTableInstanceIdDetails') {
            setTimeout(() => {

            }, 10);
        }
    }
});

function headerTotalTextRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    td.className = CSS_CLASS_HEADER_TOTAL_RENDERER;
}

function headerInfoRenderer(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.TextRenderer.apply(this, arguments);
    td.className = CSS_CLASS_HEADER_INFO_RENDERER;
}

function formatNumber(input: string): string {
    if (!isNaN(Number(input))) {
        return new Intl.NumberFormat('es-CL', { style: 'decimal', maximumFractionDigits: 3 }).format(Number(input));
    }
}

function hotUpdateSettings(hotInstance: any) {
    const FLG_IS_TOTAL_1 = 1;
    const btnEdit1: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit1');
    const btnEdit2: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit2');
    const btnEdit3: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit3');
    const btnEdit4: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit4');
    if(!isNullOrUndefined(hotInstance) && !isNullOrUndefined(btnEdit1) && !isNullOrUndefined(btnEdit2) && !isNullOrUndefined(btnEdit3) && !isNullOrUndefined(btnEdit4)) {
        hotInstance.updateSettings({
            cells: (row, col) => {
                const cellProperties: any = {};
                // Si no tiene un valor es porque es un encabezado de grupo
                if (!isNullOrUndefined(hotInstance.getSourceData()[ row ])) {
                    if (col !== COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] && col !== COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] && col !== COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] && col !== COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ]) {
                        if ((isNullOrUndefined(hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_TIPO_REGIS ])   || (hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_TIPO_REGIS ] === '')  || hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_CONCEPT ] === ' ')
                            && (!isNullOrUndefined(hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_CCC ]))) {
                            cellProperties.renderer = headerGroupTextRenderer;
                            cellProperties.readOnly = true;
                        } else if (col > COLUMN_NUMBER_CCC && hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_FLG_IS_TOTAL ] === FLG_IS_TOTAL_1 &&
                            hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_CONCEPT ].split('.').length === 1 ) { // Es un
                            // encabezado de totales numérico
                            cellProperties.renderer = headerTotalNumericRenderer;
                            cellProperties.readOnly = true;
                        } else if (hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_FLG_IS_TOTAL ] === FLG_IS_TOTAL_1 &&
                            hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_CONCEPT ].split('.').length === 1 ) { // Es un encabezado de totales letra
                            cellProperties.renderer = headerTotalTextRenderer;
                            cellProperties.readOnly = true;
                        }
                        if (hotInstance.getSourceData()[ row ][ col - 3 ] === MULTI_REGISTRY && (btnEdit1.disabled || btnEdit2.disabled || btnEdit3.disabled || btnEdit4.disabled)) {
                            cellProperties.readOnly = false;
                        }
                    }
                    if (col === COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] || col === COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] || col === COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] || col === COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ]) {
                        if ((isNullOrUndefined(hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_TIPO_REGIS ]) || hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_TIPO_REGIS ] === '' || hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_COD_CONCEPT ]=== ' ' )
                            && (!isNullOrUndefined(hotInstance.getSourceData()[ row ][ COLUMN_NUMBER_CCC ]))) {
                            cellProperties.renderer = headerGroupTextRenderer;
                            cellProperties.readOnly = true;
                        }
                        if (Number(hotInstance.getDataAtCell(row, COLUMN_NUMBER_FLG_IS_TOTAL)) === 1 ) {
                            // totales
                            cellProperties.className = 'header-no-total-renderer-numeric';
                            cellProperties.readOnly = true;
                            if (isNullOrUndefined(hotInstance.getDataAtCell(row, col)) ||
                                isNaN(Number(hotInstance.getDataAtCell(row, col))) || Number(hotInstance.getDataAtCell(row, col)) === 0) {
                                cellProperties.className = 'header-total-renderer_zeros';
                                cellProperties.readOnly = true;
                            }
                            // condicion de si tiene valor distinto de cero la celda de valor y si la celda de porcentaje es cero se visualise
                            if (!isNullOrUndefined(hotInstance.getDataAtCell(row, col - 1))
                                && Number(hotInstance.getDataAtCell(row, col - 1)) !== 0 && Number(hotInstance.getDataAtCell(row, col)) === 0) {
                                cellProperties.className = 'header-no-total-renderer-numeric';
                                cellProperties.readOnly = true;
                            }
                        }
                        // oculta los ceros de las columnas de porciento que no son totales
                        if (Number(hotInstance.getDataAtCell(row, COLUMN_NUMBER_FLG_IS_TOTAL)) === 0) {
                            cellProperties.className = 'header-no-total-number-renderer';
                            cellProperties.readOnly = true;
                        }
                    }
                    if (!isNullOrUndefined(hotInstance)) {
                        if (!isNullOrUndefined(hotInstance.container)) {
                            if (!isNullOrUndefined(hotInstance.container.parentNode)) {
                                if (!isNullOrUndefined(hotInstance.container.parentNode.id)) {
                                    const id = hotInstance.container.parentNode.id;
                                    if (id !== 'handOnTableInstanceIdDetails') {
                                        if (col === COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] || col === COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] || col === COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] || col === COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ]) {
                                            const dataAtCell = hotInstance.getSourceDataAtCell(row, col);
                                            const hasComputedCellValue: boolean = hotInstance.getPlugin('formulas').hasComputedCellValue(row, col);
                                            const span: HTMLSpanElement = document.getElementById('selectedCellValueId');
                                            if (hasComputedCellValue) {
                                                cellProperties.readOnly = true;
                                            } else {
                                                cellProperties.readOnly = false;
                                            }
                                        } else {
                                            cellProperties.readOnly = true;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return cellProperties;
            },
        });
    }
}
function updateFormula(formula:string,codTipoConcepto:string,fila:string,filaAnterior:string,posicionFila?:number,filaVal?:string) {
    const regex = new RegExp('\\[(.*?)\\]');
    let match:any;
    while ((match = regex.exec(formula)) !== null) {
        let isPeriodoAnterior = 0;
        match[0] = match[0].replace('[','');
        match[0] = match[0].replace(']','');
        if (match[0].includes(P_ANT)) {
            isPeriodoAnterior = 1;
        }
        if(match[0].includes(CNPT)) {
            const regexProfundoCntp = new RegExp('\\['+match[0]+'\\]');
            const reemplazo  = filaVal+posicionFila;
            formula = formula.replace(regexProfundoCntp, reemplazo);
        } else {
            const regexProfundo = new RegExp('\\['+match[0]+'\\]');
            if (!match[ 0 ].includes(codTipoConcepto)) {
                if (!match[ 0 ].includes(COD_TIPO_CONCEP_BALANCE) && !match[ 0 ].includes(COD_TIPO_CONCEP_EST_RESULT)
                    && !match[ 0 ].includes(COD_TIPO_CONCEP_GEN_FONDOS) && !match[ 0 ].includes(COD_TIPO_CONCEP_IND_FINAN)) {
                    match[ 0 ] = codTipoConcepto + '.' + match[ 0 ];
                }
            }
            let reemplazo;
            if (isPeriodoAnterior === 0) {
                reemplazo = fila + diccionario.get(match[ 0 ]);
            } else {
                if (filaAnterior === '') {
                    match[ 0 ] = match[ 0 ].replace(P_ANT, '');
                    reemplazo =  P_ANT + diccionario.get(match[ 0 ]);
                } else {
                    match[ 0 ] = match[ 0 ].replace(P_ANT, '');
                    reemplazo = filaAnterior + diccionario.get(match[ 0 ]);
                }
            }
            formula = formula.replace(regexProfundo, reemplazo);
        }
    }
    return formula;
}
function updateConceptosV2(hotInstance:any,hotInstance1:any,posicion:number,isUpdatePosicion:boolean) {
    conceptos.forEach((item, index) => {
            if(!isNullOrUndefined(hotInstance.getSourceData()[ index ]) && !isNullOrUndefined(hotInstance.getSourceData()[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ posicion ] ])) {
                if(isUpdatePosicion) {
                    if(!isNullOrUndefined(item.glsFormulaVal) && item.glsFormulaVal!== '') {
                        hotInstance.getSourceData()[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ posicion ] ] = updateFormula(item.glsFormulaValOriginal, item.codTipoConcep, COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS[ posicion ], posicion > 0 ? COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS[ posicion - 1 ] : '');
                    }
                    if(!isNullOrUndefined(item.glsFormulaPrc) && item.glsFormulaPrc!== '') {
                        hotInstance.getSourceData()[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ posicion ] ] = updateFormula(item.glsFormulaPrcOriginal, item.codTipoConcep, COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS[ posicion ], posicion > 0 ? COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS[ posicion - 1 ] : '');
                    }
                }
                if(!isNullOrUndefined(item.glsFormulaVal) && item.glsFormulaVal!== '') {
                    item.glsFormulaVal = updateFormula(item.glsFormulaValOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL1, '');
                    item.glsFormulaVal2 = updateFormula(item.glsFormulaValOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL2, COLUMN_LETTER_GLS_FORMULA_VAL1);
                    item.glsFormulaVal3 = updateFormula(item.glsFormulaValOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL3, COLUMN_LETTER_GLS_FORMULA_VAL2);
                    item.glsFormulaVal4 = updateFormula(item.glsFormulaValOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL4, COLUMN_LETTER_GLS_FORMULA_VAL3);
                }
                if(!isNullOrUndefined(item.glsFormulaPrc) && item.glsFormulaPrc!== '') {
                    item.glsFormulaPrc = updateFormula(item.glsFormulaPrcOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL1, '');
                    item.glsFormulaPrc2 = updateFormula(item.glsFormulaPrcOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL2, COLUMN_LETTER_GLS_FORMULA_VAL1);
                    item.glsFormulaPrc3 = updateFormula(item.glsFormulaPrcOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL3, COLUMN_LETTER_GLS_FORMULA_VAL2);
                    item.glsFormulaPrc4 = updateFormula(item.glsFormulaPrcOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_VAL4, COLUMN_LETTER_GLS_FORMULA_VAL3);
                }
            }
                if(isUpdatePosicion) {
                    if(!isNullOrUndefined(item.glsFormulaDef) && item.glsFormulaDef!== '') {
                        hotInstance1.getSourceData()[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ posicion ] ] =
                            updateFormula(item.glsFormulaDefOriginal, item.codTipoConcep, COLUMN_NUMBER_GLS_FORMULA_DEF_LETTERS[ posicion ], posicion > 0 ? COLUMN_NUMBER_GLS_FORMULA_DEF_LETTERS[ posicion - 1 ] : '', index + 1, COLUMN_NUMBER_GLS_FORMULA_VAL_LETTERS[ posicion - 1 ]);
                    }
                }
        if(!isNullOrUndefined(item.glsFormulaDef) && item.glsFormulaDef!== '') {
            item.glsFormulaDef = updateFormula(item.glsFormulaDefOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_DEF1, '', index + 1, COLUMN_LETTER_GLS_FORMULA_VAL1);
            item.glsFormulaDef2 = updateFormula(item.glsFormulaDefOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_DEF2, COLUMN_LETTER_GLS_FORMULA_DEF1, index + 1, COLUMN_LETTER_GLS_FORMULA_VAL2);
            item.glsFormulaDef3 = updateFormula(item.glsFormulaDefOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_DEF3, COLUMN_LETTER_GLS_FORMULA_DEF2, index + 1, COLUMN_LETTER_GLS_FORMULA_VAL3);
            item.glsFormulaDef4 = updateFormula(item.glsFormulaDefOriginal, item.codTipoConcep, COLUMN_LETTER_GLS_FORMULA_DEF4, COLUMN_LETTER_GLS_FORMULA_DEF3, index + 1, COLUMN_LETTER_GLS_FORMULA_VAL4);
        }
        if (!isNullOrUndefined(item.glsFormulaDef)) {
            if (item.glsFormulaDef.toString().includes(CANT_MESES_PER)) {
                item.glsFormulaDef = item.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER, 'g'), CANT_MESES_PER0);
                item.glsFormulaDef = item.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER00, 'g'), CANT_MESES_PER0);
                item.glsFormulaDef = item.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER01, 'g'), CANT_MESES_PER1);
            }
        }
        if (!isNullOrUndefined(item.glsFormulaDef2)) {
            item.glsFormulaDef2 = item.glsFormulaDef2.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER2);
            item.glsFormulaDef2 = item.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER2);
            item.glsFormulaDef2 = item.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER1);
        }
        if (!isNullOrUndefined(item.glsFormulaDef3)) {
            item.glsFormulaDef3 = item.glsFormulaDef3.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER3);
            item.glsFormulaDef3 = item.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER3);
            item.glsFormulaDef3 = item.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER2);
        }
        if (!isNullOrUndefined(item.glsFormulaDef4)) {
            item.glsFormulaDef4 = item.glsFormulaDef4.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER4);
            item.glsFormulaDef4 = item.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER4);
            item.glsFormulaDef4 = item.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER3);
        }
    });
}
function updateConceptos(datosMaestros: any, datosMaestros1: any) {
    conceptos.forEach((concept, index, currentArray) => {
        if (concept.codTipoRegis === MULTI_REGISTRY) {
            currentArray[ index ].glsFormulaDef = concept.glsFormulaDef.replace(new RegExp(REGEX_NUMBER, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL1 + (index + 1));
            currentArray[ index ].glsFormulaDef2 = concept.glsFormulaDef2.replace(new RegExp(REGEX_NUMBER, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL2 + (index + 1));
            currentArray[ index ].glsFormulaDef3 = concept.glsFormulaDef3.replace(new RegExp(REGEX_NUMBER, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL3 + (index + 1));
            currentArray[ index ].glsFormulaDef4 = concept.glsFormulaDef4.replace(new RegExp(REGEX_NUMBER, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL4 + (index + 1));
        }
    });
    conceptos.forEach((concept, index) => {
        if (!isNullOrUndefined(concept.codTipoRegis) && concept.codTipoRegis !== '') {
            if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                if (!isNullOrUndefined(datosMaestros)) {
                    // actualiza las formulas de los totales de la valprd
                    if ((!isNullOrUndefined(datosMaestros[ index ])
                        && !isNullOrUndefined(datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])
                        && datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] !== ''
                        && isNaN(Number(datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])))) {
                        const formula1 = datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                        if (formula1.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL1)) {
                            concept.glsFormulaVal = formula1;
                            concept.glsFormulaVal2 = concept.glsFormulaVal;
                            concept.glsFormulaVal3 = concept.glsFormulaVal;
                            concept.glsFormulaVal4 = concept.glsFormulaVal;
                            const regex = COLUMN_LETTER_GLS_FORMULA_VAL1;
                            if (!isNullOrUndefined(concept.glsFormulaVal2)) {
                                concept.glsFormulaVal2 = concept.glsFormulaVal2.toString().replace(new RegExp(regex, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL2);
                            }
                            if (!isNullOrUndefined(concept.glsFormulaVal3)) {
                                concept.glsFormulaVal3 = concept.glsFormulaVal3.toString().replace(new RegExp(regex, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL3);
                            }
                            if (!isNullOrUndefined(concept.glsFormulaVal4)) {
                                concept.glsFormulaVal4 = concept.glsFormulaVal4.toString().replace(new RegExp(regex, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL4);
                            }
                        }
                    }
                    // actualiza las formulas de los prc
                    if (!isNullOrUndefined(datosMaestros[ index ])
                        && !isNullOrUndefined(datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ])
                        && datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ] !== ''
                        && isNaN(Number(datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ]))) {
                        const formula2 = datosMaestros[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ];
                        if (formula2.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL1)) {
                            concept.glsFormulaPrc = formula2;
                            concept.glsFormulaPrc2 = concept.glsFormulaPrc;
                            concept.glsFormulaPrc3 = concept.glsFormulaPrc;
                            concept.glsFormulaPrc4 = concept.glsFormulaPrc;
                            const regex = COLUMN_LETTER_GLS_FORMULA_VAL1;
                            if (!isNullOrUndefined(concept.glsFormulaPrc2)) {
                                concept.glsFormulaPrc2 = concept.glsFormulaPrc2.toString().replace(new RegExp(regex, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL2);
                            }
                            if (!isNullOrUndefined(concept.glsFormulaPrc3)) {
                                concept.glsFormulaPrc3 = concept.glsFormulaPrc3.toString().replace(new RegExp(regex, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL3);
                            }
                            if (!isNullOrUndefined(concept.glsFormulaPrc4)) {
                                concept.glsFormulaPrc4 = concept.glsFormulaPrc4.toString().replace(new RegExp(regex, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL4);
                            }
                        }
                    }
                }
            }
            // actualiza las formulas de totales de deflactor
            if (!isNullOrUndefined(datosMaestros1)) {
                if (!isNullOrUndefined(datosMaestros1[ index ])
                    && !isNullOrUndefined(datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ])
                    && datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] !== ''
                    && isNaN(Number(datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ]))) {
                    const formula3 = datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ];
                    if (formula3.toString().includes(COLUMN_LETTER_GLS_FORMULA_DEF1)) {
                        concept.glsFormulaDef = formula3;
                        if(concept.glsFormulaDef.toString().includes(CANT_MESES_PER)) {
                                concept.glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER, 'g'), CANT_MESES_PER0);
                                concept.glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER00, 'g'), CANT_MESES_PER0);
                                concept.glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER01, 'g'), CANT_MESES_PER1);
                        }
                        concept.glsFormulaDef2 = concept.glsFormulaDef;
                        concept.glsFormulaDef3 = concept.glsFormulaDef;
                        concept.glsFormulaDef4 = concept.glsFormulaDef;
                        if (!isNullOrUndefined(concept.glsFormulaDef2)) {
                            concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_DEF1, 'g'), COLUMN_LETTER_GLS_FORMULA_DEF2);
                            concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER2);
                            concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER2);
                            concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER1);
                        }
                        if (!isNullOrUndefined(concept.glsFormulaDef3)) {
                            concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_DEF1, 'g'), COLUMN_LETTER_GLS_FORMULA_DEF3);
                            concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER3);
                            concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER3);
                            concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER2);
                        }
                        if (!isNullOrUndefined(concept.glsFormulaDef4)) {
                            concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_DEF1, 'g'), COLUMN_LETTER_GLS_FORMULA_DEF4);
                            concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER4);
                            concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER4);
                            concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER3);
                        }
                    }
                    // actualiza las formulas de deflactores los elementos hijos.
                    if (!isNullOrUndefined(datosMaestros1[ index ])
                        && !isNullOrUndefined(datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ])
                        && datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] !== ''
                        && isNaN(Number(datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ]))) {
                        const formula4 = datosMaestros1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ];
                        if (formula4.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL1)) {
                            concept.glsFormulaDef = formula4;
                            if(concept.glsFormulaDef.toString().includes(CANT_MESES_PER)) {
                                concept.glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER, 'g'), CANT_MESES_PER0);
                                concept.glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER00, 'g'), CANT_MESES_PER0);
                                concept.glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(CANT_MESES_PER01, 'g'), CANT_MESES_PER1);
                            }
                            concept.glsFormulaDef2 = concept.glsFormulaDef;
                            concept.glsFormulaDef3 = concept.glsFormulaDef;
                            concept.glsFormulaDef4 = concept.glsFormulaDef;
                            if (!isNullOrUndefined(concept.glsFormulaDef2)) {
                                concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL1, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL2);
                                concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER2);
                                concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER2);
                                concept.glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER1);
                            }
                            if (!isNullOrUndefined(concept.glsFormulaDef3)) {
                                concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL1, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL3);
                                concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER3);
                                concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER3);
                                concept.glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER2);
                            }
                            if (!isNullOrUndefined(concept.glsFormulaDef4)) {
                                concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL1, 'g'), COLUMN_LETTER_GLS_FORMULA_VAL4);
                                concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER4);
                                concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER4);
                                concept.glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER3);
                            }
                        }
                    }
                }
            }
        }
    });
}
let breakdownFilterConceptDto:IBreakdownFilterConceptDto;
let conceptos: IConcepto[];
let conceptosOriginal: IConcepto[];
let conceptosEliminar: IConcepto[] = [];
let desglose: IDesglose;
let valPeriodoService;
let automatic = false;
const diccionario:any = new Map();
@Component({
    selector: 'jhi-breakdown',
    templateUrl: './breakdown.component.html',
    styleUrls: [ 'breakdown.scss' ],
    providers: [ NgbAccordionConfig ] // add the NgbAccordionConfig to the component providers
})
export class BreakdownComponent implements OnInit, AfterViewInit, ComponentCanDeactivate {
    usuario: ITurbineUser;
    operatorCode: number;
    role: string;
    currentAccount: Account;
    ALL_ROLES = [ USER_ROLE_PLAT_DESGLOSE, USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR, USER_ROLE_PLAT_DESGLOSE_EJECUTOR ];
    ROLE_JEFE_ROLE_REVISOR_ROLE_ADMIN = [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR ];
    ROLE_PLAT_DESGLOSE_ADMINISTRADOR = [ USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR ];
    USER_ROLE_PLAT_DESGLOSE_JEFE = USER_ROLE_PLAT_DESGLOSE_JEFE;
    USER_ROLE_PLAT_DESGLOSE_REVISOR = USER_ROLE_PLAT_DESGLOSE_REVISOR;
    USER_ROLE_PLAT_DESGLOSE_EJECUTOR = USER_ROLE_PLAT_DESGLOSE_EJECUTOR;
    USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR = USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR;
    COD_TIPO_MONEDA_PESOS = COD_TIPO_MONEDA_PESOS;
    COD_TIPO_DESG_BASE_NUEVA = COD_TIPO_DESG_BASE_NUEVA;
    COD_TIPO_DESG_CONSOLIDADO = COD_TIPO_DESG_CONSOLIDADO;
    message: string;
    url: string;
    breakdown: IDesglose = new Desglose();
    extraDataDesglose: IExtraDataDesglose = new ExtraDataDesglose();
    breakdownRecoverResponseDTO: IBreakdownRecoverResponseDTO;
    client: ICliente = new Cliente(null);
    previousRut: string;
    simpleComboboxTypes: IDescripcionCodigo[] = [];
    economicActivities: IEconomicActivity[] = [];
    handOnTableDetailInstanceId = 'handOnTableInstanceIdDetails';
    handOnTableGenFonInstanceId = 'handOnTableGenFonInstanceId';
    handOnTableImpreInstanceId = 'handOnTableImpreInstanceId';
    public data: any = [ [] ];
    public data1: any = [ [] ];
    public data2: any = [ [] ];
    public dataGlobal: any = [[]];
    public isCollapsedDataPanel = false;
    public isCollapsedClientDataPanel = false;
    public isCollapsedBreakdownDataPanel = false;
    public isCollapsedActionsPanel = false;
    public isCollapsedPeriodsPanel = false;
    auditorNames: string[] = [];
    private ngbModalRef: NgbModalRef;
    private ngbModalRefClients: NgbModalRef;
    private ngbModalRefConsol: NgbModalRef;
    private ngbModalRefCreateClient: NgbModalRef;
    private ngbModalRefClone: NgbModalRef;
    private ngbModalRefGeneratePrint: NgbModalRef;
    private ngbModalSelectionPrint: NgbModalRef;
    private ngbModalProject: NgbModalRef;
    private ngbModalRefErrors: NgbModalRef;
    private ngbModalShippingReview: NgbModalRef;
    private ngbModalMessages: NgbModalRef;
    public selectedBreakdown: IBreakdownFilterResponseDTO;
    public selectedClient: ICliente;
    CODTABLA_TIPO_MONEDAS = CODTABLA_TIPO_MONEDAS;
    CODTABLA_AUDITORES = CODTABLA_AUDITORES;
    CODTABLA_SUBTIPO_DESGLOSE = CODTABLA_SUBTIPO_DESGLOSE;
    CODTABLA_TIPO_RESPUESTA_AUDITOR = CODTABLA_TIPO_RESPUESTA_AUDITOR;
    CODTABLA_UNIDAD_MONETARIA = CODTABLA_UNIDAD_MONETARIA;
    CODTABLA_TIPO_DESGLOSE = CODTABLA_TIPO_DESGLOSE;
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE = CODTABLA_TIPO_PLANTILLAS_DESGLOSE;
    CODTABLA_TIPO_CLIENTE = CODTABLA_TIPO_CLIENTE;
    CODTABLA_ESTADO_DESGLOSE = CODTABLA_ESTADO_DESGLOSE;
    ESTADO_DEFINITIVO = ESTADO_DEFINITIVO;
    ESTADO_CORREGIDO = ESTADO_CORREGIDO;
    sort = 'codGenerico,ASC';
    breakdownTemplateType: IDescripcionCodigo;
    NAME_VALIDATION_PATTERN = NAME_VALIDATION_PATTERN;
    RUT_VALIDATION_PATTERN = RUT_VALIDATION_PATTERN;
    RUT_VERIFICATION_NUMBER_VALIDATION_PATTERN = RUT_VERIFICATION_NUMBER_VALIDATION_PATTERN;
    // Elementos de la interfaz fields
    @ViewChild('fieldRut') fieldRut: ElementRef;
    @ViewChild('fieldRutVerificationNumber') fieldRutVerificationNumber: ElementRef;
    @ViewChild('fieldName') fieldName: ElementRef;
    @ViewChild('fieldActivity') fieldActivity: ElementRef;
    @ViewChild('fieldBreakdownTemplateType') fieldBreakdownTemplateType: ElementRef;
    @ViewChild('field_baseDeflator') field_baseDeflator: ElementRef;
    @ViewChild('field_fecDeflacBase') field_fecDeflacBase: ElementRef;
    @ViewChild('field_fecDeflacBaseBtn') field_fecDeflacBaseBtn: ElementRef;
    @ViewChild('fieldBreakdownSubTypeDesc') fieldBreakdownSubTypeDesc: ElementRef;
    // Elementos de la interfaz botones
    @ViewChild('clientDataDiv') clientDataDivContainer: ElementRef;
    @ViewChild('ApproveBreakdown') ApproveBreakdown: ElementRef;
    @ViewChild('otherData') otherData: ElementRef;
    @ViewChild('selectedMonetaryUnit') selectedMonetaryUnit: ElementRef;
    @ViewChild('SendToReview') SendToReview: ElementRef;
    @ViewChild('SendToExecutor') SendToExecutor: ElementRef;
    // Elementos de la interfaz botones
    @ViewChild('buttonConsolidated') buttonConsolidated: ElementRef;
    @ViewChild('buttonSearchClient') buttonSearchClient: ElementRef;
    @ViewChild('buttonRecover') buttonRecover: ElementRef;
    @ViewChild('buttonClear') buttonClear: ElementRef;
    @ViewChild('buttonProcess') buttonProcess: ElementRef;
    @ViewChild('buttonGeneratePrint') buttonGeneratePrint: ElementRef;
    @ViewChild('buttonPrint') buttonPrint: ElementRef;
    @ViewChild('buttonClone') buttonClone: ElementRef;
    @ViewChild('buttonDelete') buttonDelete: ElementRef;
    @ViewChild('buttonApproveBreakdown') buttonApproveBreakdown: ElementRef;
    @ViewChild('buttonSendToReview') buttonSendToReview: ElementRef;
    @ViewChild('buttonSendToExecutor') buttonSendToExecutor: ElementRef;
    @ViewChild('buttonLiberar') buttonLiberar: ElementRef;
    @ViewChild('buttonSave') buttonSave: ElementRef;
    @ViewChild('buttonEdit1') buttonEdit1: ElementRef;
    @ViewChild('buttonEdit2') buttonEdit2: ElementRef;
    @ViewChild('buttonEdit3') buttonEdit3: ElementRef;
    @ViewChild('buttonEdit4') buttonEdit4: ElementRef;
    btnEdit1: HTMLButtonElement;
    btnEdit2: HTMLButtonElement;
    btnEdit3: HTMLButtonElement;
    btnEdit4: HTMLButtonElement;
    private canOpenConsolidatedCompanies = false;
    private clients: ICliente[];
    disableAll = false;
    isBlockPeriodsDataDisabled = true;
    alwaysDisabled = true;
    private questionMessageSubscription: Subscription;
    private tablaValorIva: ITablaValor = new TablaValor();
    private numPeriodosConInfo = 0;
    periods: IPeriodo[] = [];
    consolidatedCompanies: IEmpresaConsolidada[] = [];
    // Por si se está editando uno que está ya aprobado.
    isNewApprobationProcess = false;
    state: { state: string, stateClass: string } = { state: '', stateClass: '' };
    initialDisabledIds = [ 'buttonRecover', 'buttonInProcess', 'buttonSave', 'buttonGeneratePrint', 'buttonPrint', 'buttonClone',
        'buttonDelete', 'field_baseDeflator', 'field_fecDeflacBase', 'field_fecDeflacBaseBtn', 'field_iva', 'field_breakdownSubTypeDesc', 'buttonConsolidated',
        'field_executor', 'buttonOtherData' ];
    reseatedDisabledIds = [ 'buttonRecover', 'buttonInProcess', 'buttonSave', 'buttonGeneratePrint', 'buttonPrint', 'buttonClone',
        'buttonDelete', 'field_baseDeflator', 'field_fecDeflacBase', 'field_fecDeflacBaseBtn', 'field_iva', 'field_breakdownSubTypeDesc', 'buttonConsolidated', 'field_executor',
        'field_breakdownTemplateType', 'field_breakdownType', 'buttonConsolidated', 'field_breakdownSubType', 'field_breakdownSubTypeDesc', 'field_currency', 'buttonOtherData' ];
    // Configuración de las tabulaciones
    public currentJustify = 'justified';
    currentType = 'pills';
    public currentDestroyOnHide = false;
    // Configuración de hot-table
    colHeaders: string[];
    colHeaders1: string[];
    colWidths: string[];
    columns: any[];
    columns1: any[];
    options: Handsontable.GridSettings;
    options1: any;
    COLUMN_LETTER_GLS_FORMULA_VAL_1 = 'F';
    COLUMN_LETTER_GLS_FORMULA_PRC_1 = 'F';
    COLUMN_LETTER_GLS_FORMULA_DEF_1 = 'H';
    COLUMN_LETTER_GLS_FORMULA_VAL_2 = 'I';
    COLUMN_LETTER_GLS_FORMULA_PRC_2 = 'I';
    COLUMN_LETTER_GLS_FORMULA_DEF_2 = 'K';
    COLUMN_LETTER_GLS_FORMULA_VAL_3 = 'L';
    COLUMN_LETTER_GLS_FORMULA_PRC_3 = 'L';
    COLUMN_LETTER_GLS_FORMULA_DEF_3 = 'N';
    COLUMN_LETTER_GLS_FORMULA_VAL_4 = 'O';
    COLUMN_LETTER_GLS_FORMULA_PRC_4 = 'O';
    COLUMN_LETTER_GLS_FORMULA_DEF_4 = 'Q';
    FLG_IS_TOTAL_0 = 0;
    FLG_IS_TOTAL_1 = 1;
    hiddenRows: number[] = [];
    hiddenRows1: number[] = [];
    hiddenMultiregistro: number[] = [];
    ROW_NUMBER_DEFLAC_BASE = 1;
    ROW_NUMBER_IVA = 2;
    ROW_NUMBER_DEFLAC_PERIOD = [ 1, 2, 3, 4 ];
    ROW_NUMBER_CANT_MESES = [ 1, 2, 3, 4 ];

    tabBreakdownDetailId = 'tab-breakdown-detail';
    tabGenFondIndFinId = 'tab-gen-fond-ind-fin';
    tabPrintId = 'tab-print';
    public isDivFormulaValueHidden = false;
    public isButtonMultiregistroDisabled = true;
    public isButtonPreviousDisabled = true;
    public isButtonNextDisabled = true;
    public nextPeriodIndex = 0;
    public activePeriodsIndexes = [ 0, 1, 2, 3 ];
    public activePeriodsEdit = [];
    subTipoDesgOtraMoneda?: string;
    subTipoDesgPesos?: string;
    public objetoImpresion?: IPdfIngressReport;
    public isOcultaDivPrint = true;
    public isButtonImprime = true;
    private mensaje: IDesgloseMensaje[] = [];
    private mensajeEnvio: string;
    private gDesgloseEnRevision = false;
    private isNotificacion = false;
    private isEdicion = false;
    public objetoResumen?: IPdfSummaryReport;
    sectorTypes: ISector[] = [];
    subSectorTypes: ISubSector[] = [];
    revisorAsociadoRevisor: IDesgloseFirma[] = [];
    jefeAsociadoDeEjecutor: IDesgloseGrupo;
    isHiddenMensajes = true;
    configF;
    isReplacement = false;
    roleEnviaRevision = 0;
    /*buttons: Array<FloatingActionButton> = [
        {
            iconClass: 'fa fa-eye',
            onClick: () => {
                this.router.navigate([ '/revision' ], {
                    queryParams: {
                        isExternal: true,
                    }
                });
            }
        }
    ];*/
    placements = [
        {
            value: 'br',
            key: 'bottom right'
        },
        {
            value: 'bl',
            key: 'bottom left'
        },
        {
            value: 'tr',
            key: 'top right'
        },
        {
            value: 'tl',
            key: 'top left'
        }
    ];
    effects = [
        {
            value: 'mfb-zoomin',
            key: 'Zoom In'
        },
        {
            value: 'mfb-slidein',
            key: 'Slide In + Fade'
        },
        {
            value: 'mfb-fountain',
            key: 'Fountain'
        },
        {
            value: 'mfb-slidein-spring',
            key: 'Slide In (Spring)'
        }
    ];
    toggles = [ 'click', 'hover' ];
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;
    public HANDS_ON_TABLE_LICENSE_KEY = HANDS_ON_TABLE_LICENSE_KEY;
    private editaRevisor = false;
    private currentTabId = this.tabBreakdownDetailId;
    private currentTableId = this.handOnTableDetailInstanceId;
    private dataEmpresasConsolidan: any = [ [] ];
    private contMultiregistro = 0;
    isDivEditor = false;
    PLAT_DESGLOSE_CUSTOM_CLONE_ROLE = USER_ROLE_PLAT_DESGLOSE_CUSTOM_CLONE_ROLE;
    isRecover = false;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_PROCESS = 0;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SAVE = 1;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_GENERATE_PRINT = 2;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_PRINT = 3;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_APPROVE = 4;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SEND_TO_REVIEW = 5;
    CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SEND_BACK_TO_EXECUTOR = 6;
    reviewerNameOnSuccessValidationsReview: any;
    reviewerNumRolOnSuccessValidationsReview: any;
    datosConfImpresion:any;
    dataDeudaSbifRequest: IDatosConsultaSbifRequestDTO = new DatosConsultaSbifRequestDTO();
    loadDeudaSbif: ILoadDeudaSbifDTO = new LoadDeudaSbifDTO();
    private listLoadDeudaSbif: ILoadDeudaSbifDTO[];
    private cambioPestana = false;
    constructor(private sweetalert2Component:Sweetalert2Component,private localStorage:LocalStorage,private loginService: LoginService,private loaderService: LoaderService, private config: NgbAccordionConfig, private modalService: NgbModal, private router: Router,
                private jhiAlertService: JhiAlertService,private customSqlService: CustomSqlService, private sessionStorage: SessionStorageService,
                private clienteService: ClienteService, private sweetAlert2: Sweetalert2Component, private renderer: Renderer2, private tablaValorService: TablaValorService,
                private filterDescripcionCodigoPipe: FilterDescripcionCodigoPipe, private transformIvaPipe: TransformIvaPipe, private validationUtils: ValidationUtils,
                private desgloseService: DesgloseService, private translateService: TranslateService, private conceptoService: ConceptoService, private datePipe: DatePipe,
                private hotRegisterer: HotTableRegisterer, private periodoService: PeriodoService, private rutaActiva: ActivatedRoute, private reportService: ReportService,
                private valPeriodoDesgImprService: ValPeriodoDesgImprService, private periodoDesgImpresionService: PeriodoDesgImpresionService,
                private desgloseImpresionService: DesgloseImpresionService, private principal: Principal, private desgloseFirmaService: DesgloseFirmaService,
                private desgloseGrupoService: DesgloseGrupoService, private sfStfOperatorService: SfStfOperatorService, private desgloseRevisionService: DesgloseRevisionService,
                private desgloseMensajeService: DesgloseMensajeService, private turbineUserService: TurbineUserService, private descripcionCodigoService: DescripcionCodigoService,
                private loadDeudaSbifService: LoadDeudaSbifService, private valPeriodoDesgloseService:ValPeriodoDesgloseService
    ) {
        valPeriodoService = valPeriodoDesgloseService;
        // customize default values of accordions used by this component tree
        config.closeOthers = false;
        // config.type = 'custom-panel-header-bg';
        this.message = 'BreakdownComponent message';
        this.columns = initColumnas(true);
        this.columns1 = initColumnas1(true);
    }

    initTableConfigData() {
        // Configuración de hot-table
        this.colHeadersConfig();
        this.colWidths = [ '0%', '0%', '0%', '35%', '200%', '200%', '35%', '0%', '170%', '35%', '0%', '170%', '35%', '0%', '170%', '35%', '0%' ];
        // columnas
        this.options = {
            allowInvalid: false,
            // enterBeginsEditing: false,
            // validator: TODO: Aplicar para hacer validaciones más específicas en las columnas
            renderAllRows: false,
            hiddenRows: {
                indicators: true// DEBUG_INFO_ENABLED,
            },
            stretchH: 'none',
            hiddenColumns: {
                // set columns that are hidden by default
                columns: [ COLUMN_NUMBER_COD_TIPO_CONCEP, COLUMN_NUMBER_COD_TIPO_REGIS, COLUMN_NUMBER_FLG_IS_TOTAL, COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ],
                    COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ], COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ], COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ],
                // show where are hidden columns
                indicators: true// DEBUG_INFO_ENABLED
            },
            autoColumnSize: false,
            autoRowSize: false,
            width: '100%',
            height: screen.height  - 100 - (document.getElementById('bloque1').clientHeight + document.getElementById('bloque2').clientHeight),
            colHeaders: true,
            rowHeaders: true,
            colWidths: this.colWidths,
            allowInsertRow: true,
            allowRemoveRow: true,
            /*viewportRowRenderingOffset: 350,
            viewportColumnRenderingOffset: 30,*/
            allowEmpty: true
        };
        this.options1 = {
            allowInvalid: false,
            // enterBeginsEditing: false,
            // validator: TODO: Aplicar para hacer validaciones más específicas en las columnas
            renderAllRows: false,
            hiddenRows: {
                indicators: true// DEBUG_INFO_ENABLED,
            },
            stretchH: 'none',
            hiddenColumns: {
                // set columns that are hidden by default
                columns: [ COLUMN_NUMBER_COD_TIPO_CONCEP, COLUMN_NUMBER_COD_TIPO_REGIS, COLUMN_NUMBER_FLG_IS_TOTAL, COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ],
                    COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ], COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ], COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ],
                    COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ], COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ], COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ], COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ] ],
                // show where are hidden columns
                indicators: true// DEBUG_INFO_ENABLED
            },
            autoColumnSize: false,
            autoRowSize: false,
            height: screen.height - 100 - (document.getElementById('bloque1').clientHeight + document.getElementById('bloque2').clientHeight),
            colHeaders: true,
            rowHeaders: true,
            colWidths: this.colWidths,
            allowInsertRow: true,
            allowRemoveRow: true,
            /*viewportRowRenderingOffset: 350,
            viewportColumnRenderingOffset: 30,*/
            allowEmpty: true
        };
        // if(DEBUG_INFO_ENABLED) { // TODO: Cambiar los true y este if para dejarlos con valor DEBUG_INFO_ENABLED
        this.options.contextMenu = [ 'hidden_rows_show', 'hidden_rows_hide', 'hidden_columns_hide', 'hidden_columns_show' ];
        this.options1.contextMenu = [ 'hidden_rows_show', 'hidden_rows_hide', 'hidden_columns_hide', 'hidden_columns_show' ];
        // }
    }

    ngOnInit() {
        this.isRecover = false;
        this.initTableConfigData();
        const ANCHO = document.getElementById('divSizeReference').clientWidth;
        const ANCHO1 = document.getElementById('divSizeReference1').clientWidth;
        this.colWidths = [ '0%', '0%', '0%', '40%', `${ ANCHO*2 - 145 }%`, `${ ANCHO1*2 - 65 }%`, '40%', `${ ANCHO1*2 - 20 }%`, `${ ANCHO1*2 - 65 }%`, '40%', `${ ANCHO1*2 - 20 }%`,
            `${ ANCHO1*2 - 65 }%`, '40%', `${ ANCHO1*2 - 20 }%`, `${ ANCHO1*2 - 65 }%`, '40%', `${ ANCHO1*2 - 20 }%` ];
        this.isReplacement = Boolean(this.localStorage.getItem(SESSION_REEMPLAZO_AUTENTICATE));
        this.usuario = this.sessionStorage.retrieve(SESSION_VARIABLE_USUARIO);
        if(isNullOrUndefined(this.usuario)) {
                this.turbineUserService.findByLoginNameSfStf(this.localStorage.getItem(SESSION_VARIABLE_USUARIO_NAME)).subscribe(
                (data: HttpResponse<ITurbineUser>) => {
                    this.usuario = data.body;
                    this.sessionStorage.store(SESSION_VARIABLE_USUARIO, data.body);
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        }
        this.indicadorNotificaciones();
        this.principal.identity().then(account => {
            this.currentAccount = account;
            if (this.currentAccount.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_EJECUTOR).length > 0) {
                this.role = USER_ROLE_PLAT_DESGLOSE_EJECUTOR;
                this.buttonSendToReview.nativeElement.disabled = true;
                this.ApproveBreakdown.nativeElement.remove();
                this.SendToExecutor.nativeElement.remove();
            }
            if (this.currentAccount.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_REVISOR).length > 0) {
                this.role = USER_ROLE_PLAT_DESGLOSE_REVISOR;
                this.buttonApproveBreakdown.nativeElement.disabled = true;
                this.buttonSendToReview.nativeElement.disabled = true;
                this.buttonSendToExecutor.nativeElement.disabled = true;
            }
            if (this.currentAccount.authorities.filter(data => data === USER_ROLE_PLAT_DESGLOSE_JEFE).length > 0) {
                this.role = USER_ROLE_PLAT_DESGLOSE_JEFE;
                this.buttonApproveBreakdown.nativeElement.disabled = true;
                this.buttonSendToReview.nativeElement.disabled = true;
                this.buttonSendToExecutor.nativeElement.disabled = true;
            }
            this.otherData.nativeElement.disabled = true;
            this.selectedMonetaryUnit.nativeElement.disabled = true;
            this.buttonLiberar.nativeElement.disabled = true;
            this.field_baseDeflator.nativeElement.disabled = true;
            this.field_fecDeflacBase.nativeElement.disabled = true;
            this.field_fecDeflacBaseBtn.nativeElement.disabled = true;
            this.buttonEdit1.nativeElement.disabled = true;
            this.buttonEdit2.nativeElement.disabled = true;
            this.buttonEdit3.nativeElement.disabled = true;
            this.buttonEdit4.nativeElement.disabled = true;
        });
        this.sfStfOperatorService.findByLoginName(this.localStorage.getItem(SESSION_VARIABLE_USUARIO_NAME)).subscribe(
            (data: HttpResponse<SfStfOperator>) => {
                this.operatorCode = data.body.operatorCode;
                if (this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR) {
                    this.desgloseFirmaService.empleadoAsociado2Firma(this.operatorCode.toString(10)).subscribe(
                        (rpt: HttpResponse<IDesgloseFirma[]>) => {
                            this.revisorAsociadoRevisor = rpt.body;
                        },
                        (rpt: HttpErrorResponse) => this.onError(rpt.message)
                    );
                }
                if (this.role === USER_ROLE_PLAT_DESGLOSE_EJECUTOR) {
                    this.desgloseGrupoService.findRoleEjecutor(this.operatorCode.toString(10)).subscribe(
                        (rpt: HttpResponse<IDesgloseGrupo>) => {
                            this.jefeAsociadoDeEjecutor = rpt.body;
                        },
                        (rpt: HttpErrorResponse) => this.onError(rpt.message)
                    );
                }
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );
        this.loadIvaValue();
        const typesParam =
            this.CODTABLA_SUBTIPO_DESGLOSE +
            ',' +
            this.CODTABLA_TIPO_DESGLOSE +
            ',' +
            this.CODTABLA_UNIDAD_MONETARIA +
            ',' +
            this.CODTABLA_TIPO_MONEDAS +
            ',' +
            this.CODTABLA_TIPO_RESPUESTA_AUDITOR +
            ',' +
            this.CODTABLA_TIPO_PLANTILLAS_DESGLOSE +
            ',' +
            this.CODTABLA_TIPO_CLIENTE +
            ',' +
            this.CODTABLA_AUDITORES;
        this.loadIDescripcionCodigo(typesParam, this.sort);
        this.loadSectors();
        this.loadSubSectors();
        this.disableElementsByIds(this.initialDisabledIds, true);
        this.btnEdit1 = this.buttonEdit1.nativeElement;
        this.btnEdit2 = this.buttonEdit2.nativeElement;
        this.btnEdit3 = this.buttonEdit3.nativeElement;
        this.btnEdit4 = this.buttonEdit4.nativeElement;
        setTimeout(() => {
            document.getElementById('field_baseDeflator').classList.remove('ng-invalid');
            this.buttonSave.nativeElement.disabled = true;
            this.buttonProcess.nativeElement.disabled = true;
        }, 1000);
    }
    public hasUnsavedData() {
        return !isNullOrUndefined(this.breakdown) && !isNullOrUndefined(this.breakdown.codCliente);
    }

    // @HostListener allows us to also guard against browser refresh, close, etc.
    @HostListener('window:beforeunload')
    canDeactivate(): Observable<boolean> | boolean {
        this.cambioPestana= true;
        if (this.breakdown.codEstado === ESTADO_EN_EDICION &&
            (this.breakdown.nomUserOpen === this.usuario.loginName)) {
            this.freeBreakdown(true);
        }
        if (this.breakdown.codEstado !== ESTADO_EN_EDICION &&
            (this.breakdown.nomUserOpen === this.usuario.loginName)) {
            this.freeBreakdown(false);
        }
        if (this.breakdown.codEstado !== ESTADO_EN_EDICION && (isNullOrUndefined(this.breakdown.nomUserOpen) || this.breakdown.nomUserOpen === '')) {
            this.freeBreakdown(false);
        } else {
            if (!isNullOrUndefined(this.sessionStorage.retrieve(SESSION_VARIABLE_CLOSE))) {
                this.loginService.logout();
            }
        }

        // Solo puede liberarse si es el mismo usuario el que lo bloqueó y de la sesión.
        // return !this.hasUnsavedData();
        return true;
        // insert logic to check if there are pending changes here;
        // returning true will navigate without confirmation
        // returning false will show a confirm dialog before navigating away
    }

    private hasPermissionToClone() {
        this.principal.hasAuthority(this.PLAT_DESGLOSE_CUSTOM_CLONE_ROLE).then(result => {
            if (result) {
                this.disableElementsByIds([ 'buttonClone' ], false);
            } else {
                this.disableElementsByIds([ 'buttonClone' ], true);
            }
        });
    }

    indicadorNotificaciones() {
        this.desgloseRevisionService.indicadorNotificaciones().subscribe(
            (data: HttpResponse<number>) => {
                /*this.configF = {
                    placment: 'br',
                    effect: 'mfb-zoomin',
                    label: data.body,
                    toggle: 'hover'
                };*/
                if (data.body > 0) {
                    this.isNotificacion = true;
                } else {
                    this.isNotificacion = false;
                }
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );
    }

    loadSectors() {
        this.customSqlService.getAllSectors().subscribe(
            (res: HttpResponse<ISector[]>) => {
                this.sectorTypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadSubSectors() {
        this.customSqlService.getAllSubSectors().subscribe(
            (res: HttpResponse<ISubSector[]>) => {
                this.subSectorTypes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    colHeadersConfig() {
        this.colHeaders = [
            'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
            'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
            'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
            this.translateService.instant('breakdown.hotTable.code'),
            this.translateService.instant('breakdown.hotTable.ccc'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
        ];
    }
    colHeadersConfig1() {
        this.colHeaders1 = [
            'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
            'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
            'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
            this.translateService.instant('breakdown.hotTable.code'),
            this.translateService.instant('breakdown.hotTable.ccc'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
            this.translateService.instant('breakdown.hotTable.percentSymbol'),
            !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ]) ? this.datePipe.transform(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER) : '',
        ];
    }

    loadAllTemplateData(templateCode: number) {
        const conceptRequestDTO: IConceptRequestDTO = new ConceptRequestDTO(templateCode);
        conceptRequestDTO.conceptTypeCodesIn = [ COD_TIPO_CONCEP_BALANCE, COD_TIPO_CONCEP_EST_RESULT, COD_TIPO_CONCEP_GEN_FONDOS, COD_TIPO_CONCEP_IND_FINAN ];
        conceptRequestDTO.orderByDirection = OrderByDirection.ASC;
        conceptRequestDTO.orderByFields = [ FIELD_NAME_COD_TIPO_CONCEP, FIELD_NAME_NUM_SECUENCIA ];
        conceptRequestDTO.columnLetterGlsFormulaVal1 = this.COLUMN_LETTER_GLS_FORMULA_VAL_1;
        conceptRequestDTO.columnLetterGlsFormulaVal2 = this.COLUMN_LETTER_GLS_FORMULA_VAL_2;
        conceptRequestDTO.columnLetterGlsFormulaVal3 = this.COLUMN_LETTER_GLS_FORMULA_VAL_3;
        conceptRequestDTO.columnLetterGlsFormulaVal4 = this.COLUMN_LETTER_GLS_FORMULA_VAL_4;
        conceptRequestDTO.columnLetterGlsFormulaPrc1 = this.COLUMN_LETTER_GLS_FORMULA_PRC_1;
        conceptRequestDTO.columnLetterGlsFormulaPrc2 = this.COLUMN_LETTER_GLS_FORMULA_PRC_2;
        conceptRequestDTO.columnLetterGlsFormulaPrc3 = this.COLUMN_LETTER_GLS_FORMULA_PRC_3;
        conceptRequestDTO.columnLetterGlsFormulaPrc4 = this.COLUMN_LETTER_GLS_FORMULA_PRC_4;
        conceptRequestDTO.columnLetterGlsFormulaDef1 = this.COLUMN_LETTER_GLS_FORMULA_DEF_1;
        conceptRequestDTO.columnLetterGlsFormulaDef2 = this.COLUMN_LETTER_GLS_FORMULA_DEF_2;
        conceptRequestDTO.columnLetterGlsFormulaDef3 = this.COLUMN_LETTER_GLS_FORMULA_DEF_3;
        conceptRequestDTO.columnLetterGlsFormulaDef4 = this.COLUMN_LETTER_GLS_FORMULA_DEF_4;
        conceptRequestDTO.flgConActivo = VALUE_IS_ACTIVE_CONCEPT_TRUE;
        /* conceptRequestDTO.cellNumMeses1 = this.COLUMN_LETTER_CANT_MESES + '' + this.ROW_NUMBER_CANT_MESES[ 0 ];
         conceptRequestDTO.cellIva = this.COLUMN_LETTER_IVA + '' + this.ROW_NUMBER_IVA;
         conceptRequestDTO.cellDeflacBase = this.COLUMN_LETTER_DEFLAC_BASE + '' + this.ROW_NUMBER_DEFLAC_BASE;
         conceptRequestDTO.cellDeflacPeriod1 = this.COLUMN_LETTER_DEFLAC_PERIOD + this.ROW_NUMBER_DEFLAC_PERIOD[ 0 ];
         conceptRequestDTO.cellDeflacPeriod2 = this.COLUMN_LETTER_DEFLAC_PERIOD + this.ROW_NUMBER_DEFLAC_PERIOD[ 1 ];
         conceptRequestDTO.cellDeflacPeriod3 = this.COLUMN_LETTER_DEFLAC_PERIOD + this.ROW_NUMBER_DEFLAC_PERIOD[ 2 ];
         conceptRequestDTO.cellDeflacPeriod4 = this.COLUMN_LETTER_DEFLAC_PERIOD + this.ROW_NUMBER_DEFLAC_PERIOD[ 3 ];*/
        this.cargaConceptos(conceptRequestDTO);
    }

    private cargaConceptos(conceptRequestDTO: IConceptRequestDTO) {
        this.conceptoService.findByTemplateConceptsCodesIn(conceptRequestDTO).subscribe(
            (res: HttpResponse<IBreakdownFilterConceptDto>) => {
                breakdownFilterConceptDto = res.body;
                conceptos = breakdownFilterConceptDto.conceptoDTOList;
                this.assignConcepts(breakdownFilterConceptDto.conceptoDTOList, res.headers);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    ngAfterViewInit(): void {
        this.hideEvaluationMessage();
        setTimeout(() => {
            this.fieldRut.nativeElement.focus();
        }, 5000);
    }

    loadIvaValue() {
        this.tablaValorService.getIvaValue().subscribe(
            (res: HttpResponse<ITablaValor>) => {
                this.tablaValorIva = res.body;
                this.onIvaChange();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadIDescripcionCodigo(codtabla: string, sort: string /*, destinationArray: number*/) {
        this.descripcionCodigoService.findByCodTablasIn(codtabla, sort).subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigos(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    loadActividadEconomica() {
        this.customSqlService.getAllEconomicActivities().subscribe(
            (res: HttpResponse<IEconomicActivity[]>) => this.assignEconomicActivities(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    /**
     * Ocultar/eliminar información de licencia del componente.
     */
    private hideEvaluationMessage() {
        const divElement: HTMLElement = document.getElementById('hot-display-license-info');
        if (!isNullOrUndefined(divElement)) {
            divElement.remove();
        }
        // divElement.hidden = true;
    }

    onClickSearchBreakdown() {
        const that = this;
        this.ngbModalRef = this.modalService.open(BreakdownSearchPopupComponent as Component, {
            windowClass: 'search-breakdown-dialog',
            // size: 'lg',
            backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.onSelectItem.subscribe(data => {
            that.selectedBreakdown = data;
            this.loadBreakdownDataIntoPage();
            that.ngbModalRef.close();
            that.ngbModalRef = null;
        });
        this.ngbModalRef.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            }
        );
    }

    onClickOtherData() {
        const that = this;
        const hotInstance = that.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        this.ngbModalRef = this.modalService.open(BreakdownOtherDataConfigDialogComponent as Component, {
            windowClass: 'other-data-config-dialog',
            backdrop: 'static'
        });
        this.ngbModalRef.componentInstance.conceptos = conceptos;
        this.ngbModalRef.componentInstance.periods = that.extraDataDesglose.fecPeriodo;
        this.ngbModalRef.componentInstance.codCliente = that.breakdown.codCliente;
        this.ngbModalRef.componentInstance.codTipoDesglose = that.breakdown.codTipoDes;
        this.ngbModalRef.componentInstance.codSubTipoDesglose = that.breakdown.codSubtipoDes;
        this.ngbModalRef.componentInstance.codTipoPlantilla = that.breakdown.codPlantilla;
        this.ngbModalRef.componentInstance.codMoneda = that.breakdown.codMoneda;
        this.ngbModalRef.componentInstance.isIndicePeriodos = that.activePeriodsIndexes;
        this.ngbModalRef.componentInstance.selectedAnterior = that.extraDataDesglose.matrizConImp;
        this.ngbModalRef.componentInstance.observations = that.breakdown.glsObservacion;
        this.ngbModalRef.componentInstance.saveOtherDataConfigEmit.subscribe(data => {
            this.extraDataDesglose.matrizConImp = data.conceptos;
            this.breakdown.glsObservacion = data.observacion;
            that.ngbModalRef.close();
            that.ngbModalRef = null;
        });
        this.ngbModalRef.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRef = null;
            }
        );
    }

     afterSelectionMethod(hotInstance: any, r: number, c: number, r2: number, c2: number, selectionLayerLevel: number) {
        const btnAddMultiRegistry: HTMLButtonElement = <HTMLButtonElement>document.getElementById('agregarMultiregistro');
        const btnRemoveMultiRegistry: HTMLButtonElement = <HTMLButtonElement>document.getElementById('quitarMultiregistro');
        const btnEdit1: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit1');
        const btnEdit2: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit2');
        const btnEdit3: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit3');
        const btnEdit4: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit4');
        const FLG_IS_TOTAL_0 = 0;
        const FLG_IS_TOTAL_1 = 1;
        const hotRegisterer = new HotTableRegisterer();
        const dataHotInstance = hotInstance.getSourceData();
        const hotInstance1 = hotRegisterer.getInstance('handOnTableGenFonInstanceId');
        if (!isNullOrUndefined(dataHotInstance[ r ]) && (dataHotInstance[ r ][ c - 2 ] === MULTI_REGISTRY || dataHotInstance[ r ][ c - 3 ] === MULTI_REGISTRY)
            && (btnEdit1.disabled || btnEdit2.disabled || btnEdit3.disabled || btnEdit4.disabled)) {
            btnAddMultiRegistry.disabled = false;
            btnRemoveMultiRegistry.disabled = false;
            btnAddMultiRegistry.onclick = () => {
                let posicion = 0;
                if ((btnEdit1.disabled || btnEdit2.disabled || btnEdit3.disabled || btnEdit4.disabled)) {
                    if(btnEdit1.disabled) {
                        posicion = 0;
                    }
                    if(btnEdit2.disabled) {
                        posicion = 1;
                    }
                    if(btnEdit3.disabled) {
                        posicion = 2;
                    }
                    if(btnEdit4.disabled) {
                        posicion = 3;
                    }
                    const newConcept: IConcepto = new Concepto(
                        conceptos[ r ].codTipoPlantilla, conceptos[ r ].codTipoConcep, conceptos[ r ].corConcepto, conceptos[ r ].codConcepto, '',
                        conceptos[ r ].numSecuencia, conceptos[ r ].numLvlIndent, conceptos[ r ].codTipoRegis,conceptos[ r ].glsFormulaValOriginal, conceptos[ r ].glsFormulaVal, conceptos[ r ].glsFormulaVal2,
                        conceptos[ r ].glsFormulaVal3, conceptos[ r ].glsFormulaVal4, conceptos[ r ].glsFormulaPrcOriginal,conceptos[ r ].glsFormulaPrc, conceptos[ r ].glsFormulaPrc2, conceptos[ r ].glsFormulaPrc3,
                        conceptos[ r ].glsFormulaPrc4, conceptos[ r ].glsFormulaDefOriginal,conceptos[ r ].glsFormulaDef, conceptos[ r ].glsFormulaDef2, conceptos[ r ].glsFormulaDef3, conceptos[ r ].glsFormulaDef4,
                        conceptos[ r ].flgIsTotal, conceptos[ r ].flgUserModif, conceptos[ r ].flgConActivo, conceptos[ r ].numDecimales, conceptos[ r ].flgHaceAjust,
                        conceptos[ r ].flgIsBold, conceptos[ r ].flgIsUcase, conceptos[ r ].flgIsEnclosed, conceptos[ r ].flgIsPrinted, conceptos[ r ].numDecPrint,
                        conceptos[ r ].flgNonegativ, conceptos[ r ].flgAfterspace, conceptos[ r ].concepto, conceptos[ r ].corValor, conceptos[ r ].newValue,
                        conceptos[ r ].newFormula, conceptos[ r ].newGlosa, conceptos[ r ].newPorcent, conceptos[ r ].expValidacionVal, conceptos[ r ].expValidacionPrc,
                        conceptos[ r ].expValidacionDef, conceptos[ r ].msgErrValidacionVal, conceptos[ r ].msgErrValidacionPrc, conceptos[ r ].msgErrValidacionDef,
                        conceptos[ r ].unidad, conceptos[ r ].updateFormula);
                    conceptos.splice(r, 0, newConcept);
                    automatic = true;
                    hotInstance.getPlugin('formulas').disablePlugin();
                    hotInstance1.getPlugin('formulas').disablePlugin();
                    hotInstance.alter('insert_row', r, 1);
                    hotInstance1.alter('insert_row', r, 1);
                    let actualizarIndices = 0;
                    diccionario.forEach((item, index) => {
                        if(index === newConcept.codTipoConcep + '.'+ newConcept.codConcepto) {
                            actualizarIndices = 1;
                        }
                        if(actualizarIndices === 1) {
                            diccionario.set(index, item + 1);
                        }
                    });
                    updateConceptosV2(hotInstance,hotInstance1,posicion,true);
                    hotInstance.getPlugin('formulas').enablePlugin ();
                    hotInstance1.getPlugin('formulas').enablePlugin ();
                    hotInstance.getPlugin('formulas').recalculateFull();
                    setTimeout(() => {
                        /*const datosMaestros = hotInstance.getSourceData();
                        const datosMaestros1 = hotInstance1.getSourceData();*/
                        // updateConceptos(datosMaestros, null);
                        // updateConceptos(null, datosMaestros1);
                        hotUpdateSettings(hotInstance);
                        hotUpdateSettings(hotInstance1);
                        hotInstance.render();
                        hotInstance1.render();
                        btnAddMultiRegistry.onclick = null;
                    }, 500);
                }
            };
            const tableData = hotInstance.getSourceData();
            const filter = tableData.filter(data => data[ COLUMN_NUMBER_COD_CONCEPT ] === hotInstance.getSourceData()[ r ][ COLUMN_NUMBER_COD_CONCEPT ]);
            if (filter.length === 1) {
                btnRemoveMultiRegistry.disabled = true;
            } else {
                btnRemoveMultiRegistry.onclick = () => {
                    let posicion = 0;
                    if ((btnEdit1.disabled || btnEdit2.disabled || btnEdit3.disabled || btnEdit4.disabled)) {
                        if (btnEdit1.disabled) {
                            posicion = 0;
                        }
                        if (btnEdit2.disabled) {
                            posicion = 1;
                        }
                        if (btnEdit3.disabled) {
                            posicion = 2;
                        }
                        if (btnEdit4.disabled) {
                            posicion = 3;
                        }
                        const corValorEliminado = conceptos[ r ].corValor;
                        conceptosEliminar.push(conceptos[ r ]);
                        conceptos.splice(r, 1);
                        hotInstance.getPlugin('formulas').disablePlugin();
                        hotInstance1.getPlugin('formulas').disablePlugin();
                        hotInstance.alter('remove_row', r, 1);
                        hotInstance1.alter('remove_row', r, 1);
                        let actualizarIndices = 0;
                        diccionario.forEach((item, index) => {
                            if (index === conceptos[ r ].codTipoConcep + '.' + conceptos[ r ].codConcepto) {
                                actualizarIndices = 1;
                            }
                            if (actualizarIndices === 1) {
                                diccionario.set(index, item - 1);
                            }
                        });
                        updateConceptosV2(hotInstance,hotInstance1,posicion,true);
                        hotInstance.getPlugin('formulas').enablePlugin ();
                        hotInstance1.getPlugin('formulas').enablePlugin ();
                        hotInstance.getPlugin('formulas').recalculateFull();
                        setTimeout(() => {
                            const datosMaestros = hotInstance.getSourceData();
                            const datosMaestros1 = hotInstance1.getSourceData();
                            // updateConceptos(datosMaestros, null);
                            // updateConceptos(null, datosMaestros1);
                            hotUpdateSettings(hotInstance);
                            hotUpdateSettings(hotInstance1);
                            hotInstance.render();
                            hotInstance1.render();
                            btnRemoveMultiRegistry.onclick = null;
                        }, 500);
                    }

                };
            }
        } else {
            btnAddMultiRegistry.disabled = true;
            btnRemoveMultiRegistry.disabled = true;
        }
        const spanContainer: HTMLSpanElement = document.getElementById('selectedCellValueContainerId');
        if (c !== COLUMN_NUMBER_COD_TIPO_CONCEP && c !== COLUMN_NUMBER_COD_TIPO_REGIS && c !== COLUMN_NUMBER_FLG_IS_TOTAL &&
            c !== COLUMN_NUMBER_COD_CONCEPT && c !== COLUMN_NUMBER_CCC) {
            const dataAtCell = hotInstance.getSourceDataAtCell(r, c);
            const hasComputedCellValue: boolean = hotInstance.getPlugin('formulas').hasComputedCellValue(r, c);
            const span: HTMLSpanElement = document.getElementById('selectedCellValueId');
            if (!hasComputedCellValue) {
                span.innerHTML = '&emsp;';
            } else {
                span.textContent = dataAtCell;
            }
        }
    }

    afterSelectionMethod2(hotInstance: any, r: number, c: number, r2: number, c2: number, selectionLayerLevel: number) {

    }

    afterValidateMethod(hotInstance: any, isValid: boolean, value: any, row: number, col: number) {
        /* if (!isValid) {
            hotInstance.setDataAtCell(row, col, null);
        } */
    }

    /**
     * Método que se ejecuta después de cargar la metadata de la tabla. Se formatean las filas.
     * @param hotInstance
     */
    afterLoadDataMethod(hotInstance: any) {
        try {
            const localData: any = hotInstance.getSourceData();
            if (!isNullOrUndefined(localData) && localData.length > 0 && localData[ 0 ].length > 0) {
                hotUpdateSettings(hotInstance);
            }
        } catch (e) {
            if (DEBUG_INFO_ENABLED) {
                console.log(e);
            }
        }
    }

    afterLoadDataMethod2(hotInstance: any) {
        const FLG_IS_TOTAL_0 = 0;
        const FLG_IS_TOTAL_1 = 1;
        try {
            const localData: any = hotInstance.getSourceData();
            if (!isNullOrUndefined(localData) && localData.length > 0 && localData[ 0 ].length > 0) {
                hotInstance.updateSettings({
                    cells: (row, col) => {
                        const cellProperties: any = {};
                        // Si no tiene un valor es porque es un encabezado de grupo
                        if ((isNullOrUndefined(localData[ row ][ COLUMN_NUMBER_COD_TIPO_CONCEP ])) || (isNullOrUndefined(localData[ row ][ COLUMN_NUMBER_COD_TIPO_REGIS ]) ||
                            localData[ row ][ COLUMN_NUMBER_COD_TIPO_REGIS ] === '')) {
                            cellProperties.renderer = headerGroupTextRenderer;
                        }
                        if (localData[ row ][ COLUMN_NUMBER_COD_TIPO_CONCEP ] === 3) {
                            cellProperties.renderer = headerInfoRenderer;
                        }
                        if (localData[ row ][ COLUMN_NUMBER_COD_TIPO_CONCEP ] === 2) {
                            cellProperties.renderer = headerTotalTextRenderer;
                        }
                        return cellProperties;
                    },
                });

            }
        } catch (e) {
            if (DEBUG_INFO_ENABLED) {
                console.log(e);
            }
        }
    }

    deletePeriod(periodIndex: number, columnIndex: number) {
        const fecPeriodoElement = this.extraDataDesglose.fecPeriodo[ periodIndex ];
        if (!isNullOrUndefined(fecPeriodoElement)) {
            if (fecPeriodoElement.isValid()) {
                const fecPeriodo = this.datePipe.transform(fecPeriodoElement, DATE_FORMAT_PERIOD_ES_UI_HEADER);
                this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                    'breakdown.confirmation',
                    'breakdown.deletePeriodConfirmation',
                    'question',
                    'breakdown.yes',
                    'breakdown.no',
                    { periodDate: fecPeriodo }
                ).subscribe(res => {
                    if (res) {
                        this.periodoService.delete(this.breakdown.codCliente, this.breakdown.codTipoDes, this.breakdown.codSubtipoDes, this.breakdown.codMoneda,
                            fecPeriodoElement, this.breakdown.codPlantilla).subscribe(
                            () => this.recoverButtonOnClick(),
                            (err: HttpErrorResponse) => this.onError(err.message)
                        );
                    }
                    this.questionMessageSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeError('breakdown.error', 'breakdown.periodDateFormatError', {});
            }
        } else {
            this.sweetAlert2.mensajeError('breakdown.error', 'breakdown.periodDateError', {});
        }
    }

    deleteDataPeriod(periodIndex: number, columnIndex: number) {
        this.extraDataDesglose.fecPeriodo.forEach((item, index) => {
            if (periodIndex === index) {
                this.extraDataDesglose.fecPeriodo [ index ] = null;
            }
        });
        this.extraDataDesglose.monthsPeriod.forEach((item2, index2) => {
            if (periodIndex === index2) {
                this.extraDataDesglose.monthsPeriod [ index2 ] = null;
            }
        });
        this.extraDataDesglose.deflatorPeriod.forEach((item3, index3) => {
            if (periodIndex === index3) {
                this.extraDataDesglose.deflatorPeriod [ index3 ] = null;
                this.extraDataDesglose.deflatorPeriodStr[ index3 ] = null;
            }
        });
        this.extraDataDesglose.periodRating [ periodIndex ] = '';
        this.auditorNames [ periodIndex ] = '';

        // const period = this.extraDataDesglose.periodos [ periodIndex ];
        this.extraDataDesglose.periodos.splice(periodIndex, 1);
        // todo: carlos --> esto lo comento me altera logica de programacion.
        // conceptos.filter(c => ((c.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (c.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)))
        /*.forEach((concept, ind) => {
            // Sea o no una fórmula lo pongo en null
            this.data[ ind ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ columnIndex ] ] = null;
            this.data1[ ind ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ columnIndex ] ] = null;
        });*/
        // Actualizar el header de la tabla
        this.colHeadersConfig();
    }

    clientNameKeyUp(event) {
        this.client.rut = null;
        this.client.rutVerificationNumber = null;
        this.client.activity = '-1';
        this.client.clientCode = null;
        if (this.client.name && this.client.name.length > 0) {
            this.fieldRut.nativeElement.disabled = true;
            this.fieldActivity.nativeElement.disabled = true;
            this.buttonSearchClient.nativeElement.disabled = true;
        } else {
            this.fieldRut.nativeElement.disabled = false;
            this.fieldActivity.nativeElement.disabled = false;
            this.buttonSearchClient.nativeElement.disabled = false;
        }
    }

    clientNameKeyDownOnEnter(event) {
        // if (!this.fieldActivity.nativeElement.disabled) {
        //     this.fieldActivity.nativeElement.focus();
        // } else {
        //     this.fieldBreakdownTemplateType.nativeElement.focus();
        // }
    }

    clientNameOnBlur(clientDataForm: NgForm) {
        if (!this.modalService.hasOpenModals() && clientDataForm.valid && (this.client.activity === '-1' || isNullOrUndefined(this.client.activity))) {
            if (!isNullOrUndefined(this.client.name) && this.client.name.trim().length > 0) {
                this.client.name = this.client.name.trim();
                this.clienteService.findByNameContains(this.client.name, this.breakdown.codTipoDes,
                    this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                    (res: HttpResponse<ICliente[]>) => this.onSuccessFindByName(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        }
    }

    clientRutKeyUp(event) {
        if (this.client.rut && this.client.rut.length > 0 && !this.fieldRut.nativeElement.disabled) {
            this.fieldRutVerificationNumber.nativeElement.disabled = false;
        } else if (!this.fieldRut.nativeElement.disabled) {
            this.client.rutVerificationNumber = '';
            this.fieldRutVerificationNumber.nativeElement.disabled = true;
        }

        if ((this.client.rut && this.client.rut.length > 0) || (this.client.rutVerificationNumber && this.client.rutVerificationNumber.length > 0)) {
            this.fieldName.nativeElement.disabled = true;
            this.fieldActivity.nativeElement.disabled = true;
            this.buttonSearchClient.nativeElement.disabled = true;
        } else {
            this.fieldName.nativeElement.disabled = false;
            this.fieldActivity.nativeElement.disabled = false;
            this.buttonSearchClient.nativeElement.disabled = false;
        }
    }

    clientRutKeyDownOnEnter(event) {
         if (!this.client.rutVerificationNumber || this.client.rutVerificationNumber.length === 0) {
            this.fieldRutVerificationNumber.nativeElement.focus();
         } else {
             this.fieldBreakdownTemplateType.nativeElement.focus();
         }
    }

    clientRutOnBlur(clientDataForm: NgForm) {
        if (!this.modalService.hasOpenModals() && clientDataForm.valid && (this.client.activity === '-1' || isNullOrUndefined(this.client.activity))) {
            this.searchClientByRut();
        }
    }

    private searchClientByRut() {
        if (!isNullOrUndefined(this.client.rut) && this.client.rut.trim().length > 0 && !isNullOrUndefined(this.client.rutVerificationNumber) &&
            this.client.rutVerificationNumber.trim().length > 0) {

            this.client.rut = this.client.rut.trim();
            this.client.rutVerificationNumber = this.client.rutVerificationNumber.trim().toUpperCase();

            const rutValue = this.client.rut + '' + this.client.rutVerificationNumber;
            // if ((!isNullOrUndefined(this.previousRut) && this.previousRut !== rutValue) || (isNullOrUndefined(this.previousRut) || this.previousRut.trim().length === 0)) {
            this.previousRut = rutValue;
            if (this.validationUtils.rutValidate(rutValue)) {
                this.clienteService.findByRut(rutValue, this.breakdown.codTipoDes,
                    this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                    (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            } else {
                this.jhiAlertService.error('error.invalidRut');
            }
            // }
        }
    }

    onClickConsolidated() {
        // TODO: Esta lógica es al revés
        this.canOpenConsolidatedCompanies = true;
       /* if (this.breakdown.fecPeriodo1Rpt || this.breakdown.fecPeriodo2Rpt || this.breakdown.fecPeriodo3Rpt || this.breakdown.fecPeriodo4Rpt) {
            canOpenConsolidatedCompanies = false;
        }
        if (this.breakdown.fecPerConsol1Rpt || this.breakdown.fecPerConsol2Rpt) {
            canOpenConsolidatedCompanies = false;
        }*/
        if (this.canOpenConsolidatedCompanies) {
            this.openBreakdownConsolidateCompaniesDialog();
        }
     /*   } else {
            this.jhiAlertService.error('error.noPeriodsToConsolidate', null, null);
        }
*/
    }

    private openBreakdownConsolidateCompaniesDialog() {
        const that = this;
        if (that.extraDataDesglose.fecPeriodo.includes(null) || isNullOrUndefined(that.extraDataDesglose.fecPeriodo) || that.extraDataDesglose.fecPeriodo.length <= 0) {
            this.sweetAlert2.mensajeInfo('charges.aviso', 'charges.alertPeriodoConsol', {});
        } else {
            this.ngbModalRefConsol = this.modalService.open(BreakdownConsolidateCompaniesDialogComponent as Component, {
                windowClass: 'breakdown-consolidate-companies-dialog',
                backdrop: 'static'
            });
            this.ngbModalRefConsol.componentInstance.fechaPeriodo = that.extraDataDesglose.fecPeriodo;
            this.ngbModalRefConsol.componentInstance.empConsolPeriodoDesgConsolList = that.extraDataDesglose.empConsolPeriodoDesgConsolList;
            this.ngbModalRefConsol.componentInstance.onClickButtonSave.subscribe(items => {
                const consolidateCompaniesItems: any[] = items;
                this.dataEmpresasConsolidan = items;
                that.extraDataDesglose.empConsolPeriodoDesgConsolList = [];
                let y = 0;
                that.extraDataDesglose.fecPeriodo.forEach(elem => {
                    consolidateCompaniesItems.forEach(consol => {
                        // Guardo solo las filas que tengan un nombre de empresa.
                        if (!isNullOrUndefined(consol) && !isNullOrUndefined(consol[ COL_NUMBER_COMPANY_NAME ]) && consol[ COL_NUMBER_COMPANY_NAME ].trim().length > 0) {
                            const consolidateCompany = new EmprConsolPeriodoDesgConsol();
                            consolidateCompany.fecPeriodo = elem;
                            consolidateCompany.nomEmpresa = consol[ COL_NUMBER_COMPANY_NAME ];
                            consolidateCompany.valPrcConsol = consol[ y + 1 ];
                            that.extraDataDesglose.empConsolPeriodoDesgConsolList.push(consolidateCompany);
                        }
                    });
                    y = y + 1;
                });
                that.ngbModalRefConsol.close();
                that.ngbModalRefConsol = null;
                that.canOpenConsolidatedCompanies = false;
            });
            this.ngbModalRefConsol.result.then(
                () => {
                    this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                    this.ngbModalRefConsol = null;
                    this.canOpenConsolidatedCompanies = false;
                },
                () => {
                    this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                    this.ngbModalRefConsol = null;
                    this.canOpenConsolidatedCompanies = false;
                }
            );
        }
    }

    buttonSelectAuditorOnClick(index: number) {
        this.openBreakdownSelectAuditorDialog(index);
    }

    private openBreakdownSelectAuditorDialog(index: number) {
        const that = this;
        this.ngbModalRefClients = this.modalService.open(BreakdownSelectAuditorDialogComponent as Component, {
            windowClass: 'select-auditor-dialog',
            backdrop: 'static'
        });
        this.ngbModalRefClients.componentInstance.breakdownAuditor = isNullOrUndefined(that.extraDataDesglose.auditors[ index ]) ? new BreakdownAuditor() :
            that.extraDataDesglose.auditors[ index ];
        this.ngbModalRefClients.componentInstance.isOtherAuditorNameDisabled = (isNullOrUndefined(this.ngbModalRefClients.componentInstance.breakdownAuditor.otherAuditorName) ||
            this.ngbModalRefClients.componentInstance.breakdownAuditor.otherAuditorName <= 0);
        this.ngbModalRefClients.componentInstance.onAcceptEventEmitter.subscribe(breakdownAuditor => {
            that.extraDataDesglose.auditors[ index ] = breakdownAuditor;
            that.auditorNames [ index ] = (!isNullOrUndefined(breakdownAuditor.otherAuditorName) && breakdownAuditor.otherAuditorName.length > 0) ?
                breakdownAuditor.otherAuditorName : breakdownAuditor.auditor.glsGenericalarga;
            that.ngbModalRefClients.close();
            that.ngbModalRefClients = null;
        });
        this.ngbModalRefClients.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefClients = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefClients = null;
            }
        );
    }

    private assignDescripcionCodigos(data: IDescripcionCodigo[], headers: HttpHeaders) {
        this.simpleComboboxTypes = data;
        this.initBreakdownComboBoxDefaultData();
        this.sessionStorage.store(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES, this.simpleComboboxTypes);
        this.loadActividadEconomica();
        this.breakdown.codUnidMoneda = UNIDADMONETARIADEFAULT;
    }

    private assignEconomicActivities(data: IEconomicActivity[], headers: HttpHeaders) {
        this.economicActivities = data;
        this.sessionStorage.store(SESSION_VARIABLE_ECONOMIC_ACTIVITIES, this.economicActivities);
        this.selectedBreakdown = this.sessionStorage.retrieve(SESSION_VARIABLE_BUSQUEDA_DESGLO);
        this.sessionStorage.clear(SESSION_VARIABLE_BUSQUEDA_DESGLO);
        if (!isNullOrUndefined(this.selectedBreakdown)) {
            this.loadBreakdownDataIntoPage();
        }
        this.loadAllTemplateData(parseInt(this.breakdown.codPlantilla, 10));
    }

    private initBreakdownComboBoxDefaultData() {
        let codes: any[] = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_PLANTILLAS_DESGLOSE);
        if (codes && codes.length > 0) {
            this.breakdown.codPlantilla = codes[ 0 ].codGenerico;
        }
        codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_DESGLOSE);
        if (codes && codes.length > 0) {
            this.breakdown.codTipoDes = codes[ 0 ].codGenerico;
        }
        codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        if (codes && codes.length > 0) {
            this.breakdown.codSubtipoDes = codes[ 0 ].codGenerico;
        }
        codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_MONEDAS);
        if (codes && codes.length > 0) {
            this.breakdown.codMoneda = COD_TIPO_MONEDA_PESOS;
        }
        codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_UNIDAD_MONETARIA);
        if (codes && codes.length > 0) {
            this.breakdown.codUnidMoneda = codes[ 0 ].codGenerico;
        }
        /*if (this.tablaValorIva) {
            this.breakdown.valIva = this.transformIvaPipe.transform(this.tablaValorIva.valor);
        }*/
    }

    private onError(errorMessage: string) {
        // this.jhiAlertService.error(errorMessage, null, null);
    }

    private onSuccessFindByRut(data: ICliente[], headers: HttpHeaders) {
        this.clients = data;
        if (this.clients.length === 1) {
            this.fieldRut.nativeElement.disabled = true;
            this.fieldRutVerificationNumber.nativeElement.disabled = true;
            this.client = this.clients[ 0 ];
            // Si existe algún warning, mostrarlo.
            if (!isNullOrUndefined(this.client.warnings) && this.client.warnings.length > 0) {
                // this.jhiAlertService.warning(this.client.warnings[ 0 ], null, null);
                this.sweetAlert2.mensajeWarning('breakdown.warning', this.client.warnings[ 0 ], {});
                this.client.warnings = [];
            }
            // Encuentre o no el desglose con estado, el código de cliente va a existir.
            this.breakdown.codCliente = this.client.clientCode;

            // Call checkForFieldChange
            // Cargar los datos del desglose existente del cliente y no es un dato temporal.
            if (!isNullOrUndefined(this.client.breakdownSearchResponseDTO) /* && !this.client.breakdownSearchResponseDTO.flgIsTemporal*/) {
                this.disableElementsByIds([ 'buttonRecover' ], false);
                this.breakdown.nomUserOpen = this.client.breakdownSearchResponseDTO.userOpen;
                this.breakdown.flgIsTemporal = this.client.breakdownSearchResponseDTO.flgIsTemporal;
                this.breakdown.nomUserCrea = this.client.breakdownSearchResponseDTO.nomUserCrea;
                this.breakdown.codEstado = this.client.breakdownSearchResponseDTO.codEstado;
                this.breakdown.glsEstado = this.client.breakdownSearchResponseDTO.glsEstado;
                this.breakdown.valDeflacBase = this.client.breakdownSearchResponseDTO.valDeflacBase;
                this.breakdown.valDeflacBaseStr = this.breakdown.valDeflacBase.toString(10).replace('.', ',');
                this.breakdown.fecDeflacBase = this.client.breakdownSearchResponseDTO.fecDeflacBase;
                this.breakdown.flgReq2firma = this.client.breakdownSearchResponseDTO.flgReq2Firma;
                this.breakdown.glsObservacion = this.client.breakdownSearchResponseDTO.glsObservacion;
                this.breakdown.nomUserAnalis = this.client.breakdownSearchResponseDTO.userModif;
                this.breakdown.valIva = this.client.breakdownSearchResponseDTO.valIva;
                // TODO: Antonio -> Llenar todos los datos del desglose temporal. En este caso la query tiene un JOIN con DESCOD por estado, pero cuando se crea temporal no
                // tiene estado por lo que no devuelve nada.
                // objCheckDesglo.userOpen = varStatusDesglo.userOpen
                // objCheckDesglo.nomUserOpen = varStatusDesglo.nomUserOpen
                // objCheckDesglo.enProceso = varStatusDesglo.enProceso
                // objCheckDesglo.codEstado = varStatusDesglo.codEstado
                // objCheckDesglo.glsEstado = varStatusDesglo.glsEstado
                // objCheckDesglo.validado = varStatusDesglo.validado
                // objCheckDesglo.isFoundOnDB = True
                // objCheckDesglo.rolEjecutor = varStatusDesglo.rolEjecutor
                // objCheckDesglo.rolRevisor = varStatusDesglo.rolRevisor
                // objCheckDesglo.rolReemplazo = varStatusDesglo.rolReemplazo
                // this.disableElementsByIds([ 'buttonEdit', 'buttonApproveBreakdown', 'buttonSendToReview' ], true);
                // this.disableElementsByIds([ 'buttonRecover', 'buttonInProcess', 'buttonSave', 'buttonGeneratePrint', 'buttonPrint', 'buttonClone', 'buttonDelete',
                //     'field_baseDeflator', 'field_fecDeflacBase' ], false);
            } else {
                this.breakdown.nomUserOpen = null;
                this.breakdown.flgIsTemporal = true;
                this.breakdown.codEstado = null;
            }
            // ModoSoloLectorDeObjetos
            this.isBlockPeriodsDataDisabled = true;
            this.buttonRecover.nativeElement.disabled = false;
            this.desactivarButonRecuperar(this.breakdown.nomUserOpen);
            // this.disableElementsByIds([ 'buttonEdit' ], false);
            // this.onBreakdownSubTypeChange(any);

            this.fieldBreakdownSubTypeDesc.nativeElement.disabled = (!isNullOrUndefined(this.breakdown.codSubtipoDes) &&
                (this.breakdown.codSubtipoDes !== COD_SUBTIPO_DESG_OTHER1 && this.breakdown.codSubtipoDes !== COD_SUBTIPO_DESG_OTHER2));
            if (this.breakdown.codMoneda === COD_TIPO_MONEDA_PESOS) {
                this.disableElementsByIds([ 'field_baseDeflator', 'field_fecDeflacBase', 'field_fecDeflacBaseBtn' ], false);
            } else {
                this.disableElementsByIds([ 'field_baseDeflator', 'field_fecDeflacBase', 'field_fecDeflacBaseBtn' ], true);
            }
            this.disableElementsByIds(['field_monetaryUnit','field_iva' ], false);
        } else if (this.clients.length <= 0) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.newClientRutConfirmation',
                'question',
                'breakdown.yes',
                'breakdown.no'
            ).subscribe(res => {
                if (res) {
                    this.openCreateClientDialog(true);
                } else {
                    this.resetAllElementsToInitialState(false);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        } else {
            this.openMoreThanOneClientDialog();
        }
    }

    private onSuccessFindByName(data: ICliente[], headers: HttpHeaders) {
        this.clients = data;
        if (this.clients.length === 1) {
            this.client = this.clients[ 0 ];
            this.breakdown.codCliente = this.client.clientCode;
            // Call checkForFieldChange

            // ModoSoloLectorDeObjetos
            this.setDisableAll(true);
            this.isBlockPeriodsDataDisabled = true;
            this.buttonRecover.nativeElement.disabled = false;
            this.desactivarButonRecuperar(this.breakdown.nomUserOpen);
            if (!isNullOrUndefined(this.client.breakdownSearchResponseDTO) /* && !this.client.breakdownSearchResponseDTO.flgIsTemporal*/) {
                this.disableElementsByIds([ 'buttonRecover' ], false);
                this.breakdown.flgIsTemporal = this.client.breakdownSearchResponseDTO.flgIsTemporal;
                this.breakdown.nomUserCrea = this.client.breakdownSearchResponseDTO.nomUserCrea;
                this.breakdown.codEstado = this.client.breakdownSearchResponseDTO.codEstado;
                this.breakdown.glsEstado = this.client.breakdownSearchResponseDTO.glsEstado;
                this.breakdown.valDeflacBase = this.client.breakdownSearchResponseDTO.valDeflacBase;
                this.breakdown.fecDeflacBase = this.client.breakdownSearchResponseDTO.fecDeflacBase;
                this.breakdown.flgReq2firma = this.client.breakdownSearchResponseDTO.flgReq2Firma;
            }
        } else if (this.clients.length <= 0) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.newClientNoRutConfirmation',
                'question',
                'breakdown.yes',
                'breakdown.no'
            ).subscribe(res => {
                if (res) {
                    this.openCreateClientDialog(false);
                } else {
                    this.resetAllElementsToInitialState(false);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        } else {
            this.openMoreThanOneClientDialog();
        }
    }

    private desactivarButonRecuperar(nomUserOpen:string) {
        if((this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === USER_ROLE_PLAT_DESGLOSE_JEFE ) && !isNullOrUndefined(nomUserOpen)) {
            this.buttonLiberar.nativeElement.disabled = false;
        }
        if((this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === USER_ROLE_PLAT_DESGLOSE_JEFE) &&
            (isNullOrUndefined(nomUserOpen) || nomUserOpen === '' || nomUserOpen === this.usuario.loginName)) {
            this.buttonLiberar.nativeElement.disabled = true;
        }
    }
    private desactivarButonEliminar(nomUserOpen:string) {
        if(!isNullOrUndefined(nomUserOpen) && nomUserOpen !== this.usuario.loginName ) {
            this.buttonDelete.nativeElement.disabled = true;
        } else {
            this.buttonDelete.nativeElement.disabled = false;
        }
    }
    private onSuccessFindLoadDeudaSbif(data: ILoadDeudaSbifDTO[], headers: HttpHeaders) {
        this.listLoadDeudaSbif = data;
        if (this.listLoadDeudaSbif.length === 1) {
            this.loadDeudaSbif = this.listLoadDeudaSbif[ 0 ];

        }
    }

    private openMoreThanOneClientDialog() {
        const that = this;
        this.ngbModalRefClients = this.modalService.open(BreakdownClientsDialogComponent as Component, {
            windowClass: 'breakdown-clients-dialog',
            // size: 'lg',
            backdrop: 'static'
        });
        this.ngbModalRefClients.componentInstance.clients = that.clients;
        this.ngbModalRefClients.componentInstance.onSelectItem.subscribe(selectedItem => {
            that.client = selectedItem;
            that.ngbModalRefClients.close();
            that.ngbModalRefClients = null;
            const rutValue = this.client.rut + '' + this.client.rutVerificationNumber;
            if (this.validationUtils.rutValidate(rutValue)) {
                that.searchClientByRut();
            } else {
                this.clienteService.findByRut(that.client.clientCode, this.breakdown.codTipoDes,
                    this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                    (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            }
        });
        this.ngbModalRefClients.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefClients = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefClients = null;
            }
        );
    }

    private resetAllElementsToInitialState(eliminar: boolean) {
        if (eliminar === false) {
            // if (!isNullOrUndefined(this.breakdown.nomUserOpen) && this.breakdown.nomUserOpen === this.usuario.loginName) {
            this.freeBreakdown(false);
            // }
        }
        this.isRecover = false;
        this.buttonEdit1.nativeElement.disabled = true;
        this.buttonEdit2.nativeElement.disabled = true;
        this.buttonEdit3.nativeElement.disabled = true;
        this.buttonEdit4.nativeElement.disabled = true;
        this.recargarDesactiveConcepts();
        this.client = new Cliente(null);
        this.breakdown = new Desglose();
        this.extraDataDesglose = new ExtraDataDesglose();
        this.auditorNames = [];
        this.state.state = '';
        this.state.stateClass = '';
        this.previousRut = null;
        const clientDataForm: any = document.getElementById('clientDataForm');
        clientDataForm.reset();
        const periodsForm: any = document.getElementById('periodsForm');
        periodsForm.reset();
        this.setDisableAll(false);
        this.btnEdit1.disabled = true;
        this.btnEdit2.disabled = true;
        this.btnEdit3.disabled = true;
        this.btnEdit4.disabled = true;
        this.fieldRut.nativeElement.disabled = false;
        this.fieldRutVerificationNumber.nativeElement.disabled = true;
        this.fieldActivity.nativeElement.disabled = false;
        this.fieldName.nativeElement.disabled = false;
        this.buttonSearchClient.nativeElement.disabled = false;
        this.fieldBreakdownSubTypeDesc.nativeElement.disabled = true;
        this.buttonRecover.nativeElement.disabled = true;
        this.otherData.nativeElement.disabled = true;
        this.selectedMonetaryUnit.nativeElement.disabled = true;
        this.isButtonNextDisabled = true;
        this.isButtonPreviousDisabled = true;
        this.isButtonMultiregistroDisabled = true;
        this.buttonLiberar.nativeElement.disabled = true;
        this.field_baseDeflator.nativeElement.disabled = true;
        this.field_fecDeflacBase.nativeElement.disabled = true;
        this.field_fecDeflacBaseBtn.nativeElement.disabled = true;
        this.nextPeriodIndex = 0;
        this.activePeriodsIndexes = [ 0, 1, 2, 3 ];
        this.data2 = [];
        this.isHiddenMensajes = true;
        this.mensaje = [];
        this.gDesgloseEnRevision = false;
        this.disableElementsByIds(this.reseatedDisabledIds, true);
        this.disableElementsByIds([ 'field_breakdownTemplateType', 'field_breakdownType', 'field_breakdownSubType', 'field_currency' ], false);
        this.initBreakdownComboBoxDefaultData();
        // Reiniciar los datos de la tabla
        // this.hotInstance.clear(); // Clears the data from the table (the table settings remain intact).
        this.columns = initColumnas(true);
        this.colHeadersConfig();
        this.loadAllTemplateData(parseInt(this.breakdown.codPlantilla, 10));
        this.objetoImpresion = null;
        this.isOcultaDivPrint = true;
        this.isEdicion = false;
        if (this.currentAccount.authorities.filter(data => data === this.USER_ROLE_PLAT_DESGLOSE_EJECUTOR).length > 0) {
            this.buttonSendToReview.nativeElement.disabled = true;
        }
        if (this.currentAccount.authorities.filter(data => data === this.USER_ROLE_PLAT_DESGLOSE_REVISOR).length > 0) {
            this.buttonApproveBreakdown.nativeElement.disabled = true;
            this.buttonSendToReview.nativeElement.disabled = true;
            this.buttonSendToExecutor.nativeElement.disabled = true;
        }
        this.breakdown.codUnidMoneda = UNIDADMONETARIADEFAULT;
        setTimeout(() => {
            document.getElementById('field_baseDeflator').classList.remove('ng-invalid');
        }, 1000);
    }
    private setDisableAll(disable: boolean) {
        this.disableAll = disable;
    }

    private openCreateClientDialog(withRut: boolean) {
        const that = this;
        this.ngbModalRefCreateClient = this.modalService.open(BreakdownCreateClientDialogComponent as Component, {
            windowClass: 'breakdown-create-client-dialog',
            // size: 'lg',
            backdrop: 'static'
        });
        this.ngbModalRefCreateClient.componentInstance.withRut = withRut;
        this.ngbModalRefCreateClient.componentInstance.newClient = new Cliente();
        if (withRut) {
            this.ngbModalRefCreateClient.componentInstance.newClient.rut = this.client.rut;
            this.ngbModalRefCreateClient.componentInstance.newClient.rutVerificationNumber = this.client.rutVerificationNumber;
        } else {
            this.ngbModalRefCreateClient.componentInstance.newClient.name = this.client.name;
        }
        this.ngbModalRefCreateClient.componentInstance.newClient.clientType = ClientType.EMPRESA;
        this.ngbModalRefCreateClient.componentInstance.onSaveSuccessEmitter.subscribe(selectedItem => {
            that.client = selectedItem;
            if (!that.client.isCompany) {
                that.client.name += !isNullOrUndefined(that.client.fatherLastName) ? ' ' + that.client.fatherLastName : '';
                that.client.name += !isNullOrUndefined(that.client.motherLastName) ? ' ' + that.client.motherLastName : '';
            }
            that.ngbModalRefCreateClient.close();
            that.ngbModalRefCreateClient = null;
            this.setDisableAll(true);
            this.clienteService.findByRut(that.client.clientCode, this.breakdown.codTipoDes,
                this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        });
        this.ngbModalRefCreateClient.componentInstance.onCancelEmitter.subscribe(() => {
            that.ngbModalRefCreateClient.close();
            this.resetAllElementsToInitialState(false);
        });
        this.ngbModalRefCreateClient.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefCreateClient = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefCreateClient = null;
            }
        );
    }

    clearButtonOnClick() {
        this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
            'breakdown.confirmation',
            'breakdown.clearAllDataConfirmation',
            'question',
            'breakdown.accept',
            'breakdown.cancel'
        ).subscribe(res => {
            if (res) {
                this.resetAllElementsToInitialState(false);
            }
            this.questionMessageSubscription.unsubscribe();
        });
    }

    /**
     * Evento cuando se hace click en el botón editar. Edita un desglose actualizando estado, fecha de midif y usuario de modif.
     */
    onClickEditButton(period: number) {
        // TODO: If gRolOperador <> ROL_DIGITADOR And (estOrig = ESTADO_DEFINITIVO Or estOrig = ESTADO_CORREGIDO) Then
        let canEdit = true;
        this.isEdicion = true;
        this.isNewApprobationProcess = false;
        let gReqSegundaFirma = false;
        this.breakdown.rolEmpleadoRevMod = null;
        if (!this.gDesgloseEnRevision && (this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === USER_ROLE_PLAT_DESGLOSE_JEFE)) {
            gReqSegundaFirma = true;
        }
        if (this.gDesgloseEnRevision && (this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === USER_ROLE_PLAT_DESGLOSE_JEFE)) {
            gReqSegundaFirma = false;
            this.breakdown.rolEmpleadoRevMod = this.operatorCode.toString();
        }
        if ((this.role === USER_ROLE_PLAT_DESGLOSE_EJECUTOR)) {
            gReqSegundaFirma = true;
        }
        if (!(isNullOrUndefined(this.breakdown.codEstado) || (!isNullOrUndefined(this.breakdown.codEstado) && this.breakdown.codEstado !== ESTADO_DEFINITIVO &&
            this.breakdown.codEstado !== ESTADO_CORREGIDO))) {
            canEdit = false;
            this.isEdicion = false;
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.editAnsStartNewApprovalProcessConfirmation',
                'question',
                'breakdown.yes',
                'breakdown.no'
            ).subscribe(res => {
                if (res) {
                    canEdit = true;
                    this.isEdicion = true;
                    this.isNewApprobationProcess = true; // Solo si es un estado aprobado (estado final) y selecciona SI.
                    gReqSegundaFirma = true;
                     const btn: HTMLButtonElement = <HTMLButtonElement>document.getElementById('button_verMensaje');
                    btn.disabled = true;
                }
                this.questionMessageSubscription.unsubscribe();
                this.editActionPerform(canEdit, gReqSegundaFirma, period);
            });
        } else {
            this.editActionPerform(canEdit, gReqSegundaFirma, period);
        }
    }

    private editActionPerform(canEdit: boolean, gReqSegundaFirma: boolean, period: number) {
        if (canEdit) {
            this.isBlockPeriodsDataDisabled = false;
            this.breakdown.codEstado = ESTADO_EN_EDICION;
            this.breakdown.nomUserOpen = this.usuario.loginName;
            this.breakdown.nomUser = this.usuario.loginName;
            this.breakdown.nomUserAnalis = this.usuario.fullName;
            this.extraDataDesglose.userOpen = this.usuario.loginName;
            this.extraDataDesglose.nomUserOpen = this.usuario.loginName;
            const breakdownEditRequestDTO: IBreakdownEditRequestDTO = new BreakdownEditRequestDTO(this.breakdown, gReqSegundaFirma, this.isNewApprobationProcess);
            this.desgloseService.editBreakdown(breakdownEditRequestDTO).subscribe(
                (res: HttpResponse<IDesglose>) => this.onSuccessEditBreakdown(res.body, gReqSegundaFirma, res.headers, period),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    private loadBreakdownDataIntoPage() {
        // Datos del cliente
        if (this.selectedBreakdown.hasRut) {
            this.client.rut = this.selectedBreakdown.codCliente.slice(0, -1);
            this.client.rutVerificationNumber = this.selectedBreakdown.codCliente.slice(-1);
        }
        this.client.name = this.selectedBreakdown.nomCliente;
        this.client.activity = this.selectedBreakdown.codActividad;
        // Datos del desglose
        this.client.clientCode = this.selectedBreakdown.codCliente;
        this.breakdown.codPlantilla = this.selectedBreakdown.codPlantillaDesglose;
        this.breakdown.codTipoDes = this.selectedBreakdown.codTipoDes;
        this.breakdown.codSubtipoDes = this.selectedBreakdown.codSubTipoDes;
        this.breakdown.codMoneda = this.selectedBreakdown.codMoneda;
        this.breakdown.codUnidMoneda = this.selectedBreakdown.codUnidadMon;
        this.breakdown.nomUserAnalis = this.selectedBreakdown.nombreAnalista;
        this.breakdown.nomUserOpen = this.selectedBreakdown.userOpen;
        this.breakdown.nomUser = this.selectedBreakdown.userAnalista;
        this.clienteService.findByRut(this.selectedBreakdown.codCliente, this.breakdown.codTipoDes,
            this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
            (res: HttpResponse<ICliente[]>) => {
                this.onSuccessFindByRut(res.body, res.headers);
                this.recoverButtonOnClick();
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.clientFormDisableElements(this.clientDataDivContainer, true, [ 'input', 'button', 'select' ]);
        this.onBreakdownTypeChange();
    }

    onBreakdownTypeChange() {
        this.buttonConsolidated.nativeElement.disabled = (!isNullOrUndefined(this.breakdown.codTipoDes) && this.breakdown.codTipoDes !== COD_TIPO_DESG_CONSOLIDADO);
        if (!isNullOrUndefined(this.client.clientCode) && (this.client.clientCode !== '')) {
            this.clienteService.findByRut(this.client.clientCode, this.breakdown.codTipoDes,
                this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    onBreakdownTemplateTypeChange() {
        this.loadAllTemplateData(parseInt(this.breakdown.codPlantilla, 10));
        if (!isNullOrUndefined(this.client.clientCode) && (this.client.clientCode !== '')) {
            this.clienteService.findByRut(this.client.clientCode, this.breakdown.codTipoDes,
                this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    onBreakdownSubTypeChange(event: any) {
        let codesSubTipoDesgl: any[] = [];
        let codesMoneda: any[] = [];
        codesSubTipoDesgl = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        codesMoneda = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_MONEDAS);
        if (codesSubTipoDesgl && codesSubTipoDesgl.length > 0) {
            const monedaPesos = codesMoneda[ 0 ].codGenerico;
            const otraMoneda = codesSubTipoDesgl[ 1 ].codGenerico;
            const monedaSelect1 = this.breakdown.codMoneda;
            const initValue1: any [] = [ 0 ];
            codesMoneda.forEach((item, index) => {
                if (item.codGenerico === monedaSelect1) {
                    initValue1[ 0 ] = item.codGenerico;
                }
            });
            if (otraMoneda === this.breakdown.codSubtipoDes && monedaPesos === initValue1[ 0 ]) {
                this.sweetAlert2.mensajeInfo('breakdown.alertSubTipoDesglose.title', 'breakdown.alertSubTipoDesglose.subTipoOtrasMonedas', {});
                event.target.value = SUB_TIPO_DESGLOSE_PESOS;
            }
        }
        if (codesMoneda && codesMoneda.length > 0) {
            const pesosMoneda = codesMoneda[ 0 ].codGenerico;
            const monedaSelect = this.breakdown.codMoneda;
            const initValue: any [] = [ 0 ];
            codesMoneda.forEach((item, index) => {
                if (item.codGenerico === monedaSelect) {
                    initValue[ 0 ] = item.codGenerico;
                }
            });
            if (codesSubTipoDesgl && codesSubTipoDesgl.length > 0) {
                const otraMoneda2 = codesSubTipoDesgl[ 0 ].codGenerico;
                if (pesosMoneda !== initValue[ 0 ] && this.breakdown.codSubtipoDes === otraMoneda2) {
                    this.sweetAlert2.mensajeInfo('breakdown.alertSubTipoDesglose.title', 'breakdown.alertSubTipoDesglose.subTipoPesos', {});
                    event.target.value = SUB_TIPO_DESGLOSE_OTRA_MONEDA;
                }
            }
        }
        if (!isNullOrUndefined(this.client.clientCode) && (this.client.clientCode !== '')) {
            this.clienteService.findByRut(this.client.clientCode, this.breakdown.codTipoDes,
                this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    onCurrencyChange() {
        const codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        if (codes && codes.length > 0) {
            this.subTipoDesgPesos = codes[ 0 ].codGenerico;
            this.subTipoDesgOtraMoneda = codes[ 1 ].codGenerico;
        }
        if (this.breakdown.codMoneda === COD_TIPO_MONEDA_PESOS) {
            this.breakdown.valDeflacBase = null;
            this.breakdown.codSubtipoDes = this.subTipoDesgPesos;
            this.disableElementsByIds([ 'field_baseDeflator', 'field_fecDeflacBase', 'field_fecDeflacBaseBtn' ], false);
        } else {
            this.disableElementsByIds([ 'field_baseDeflator', 'field_fecDeflacBase', 'field_fecDeflacBaseBtn' ], true);
            this.breakdown.valDeflacBase = DEFLACTOR_BASE_DEFAULT_VALUE;
            this.breakdown.codSubtipoDes = this.subTipoDesgOtraMoneda;
        }
        if (!isNullOrUndefined(this.client.clientCode) && (this.client.clientCode !== '')) {
            this.clienteService.findByRut(this.client.clientCode, this.breakdown.codTipoDes,
                this.breakdown.codSubtipoDes, this.breakdown.codMoneda, this.breakdown.codPlantilla).subscribe(
                (res: HttpResponse<ICliente[]>) => this.onSuccessFindByRut(res.body, res.headers),
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    /**
     * Método para cambiar el valor de la propiedad disabled de todos los elementos de un div que contengan los tags dados.
     * @param containerDiv
     * @param disabled
     * @param tagNames
     */
    private clientFormDisableElements(containerDiv: ElementRef, disabled: boolean, tagNames: string[]) {
        tagNames.forEach(entry => {
            const elements = containerDiv.nativeElement.getElementsByTagName(entry);
            for (let i = 0; i < elements.length; i++) {
                elements.item(i).disabled = disabled;
            }
        });
    }

    /**
     * Método para cambiar el valor de la propiedad disabled de todos los elementos dados sus ID. Estos elementos deben poseer la propiedad disabled (Ej: Buttons, Inputs, etc...).
     * @param ids
     */
    private disableElementsByIds(ids: string[], disabled: boolean) {
        ids.forEach(entry => {
            const element: any = document.getElementById(entry);
            if (!isNullOrUndefined(element)) {
                element.disabled = disabled;
            }
        });
    }

    private canLoadSBIF(): boolean {
        let result = true;
        // TIPO = Consolidado
        // SUBTIPO = {OtraMoneda | Matriz | Combinado | Otros1 | Otros2}
        if (this.breakdown.codTipoDes === '1' || this.breakdown.codSubtipoDes === '2' || this.breakdown.codSubtipoDes === '4' || this.breakdown.codSubtipoDes === '5'
            || this.breakdown.codSubtipoDes === '6' || this.breakdown.codSubtipoDes === '7') {

            result = false;
        }
        return result;
    }

    private onSuccessEditBreakdown(body: IDesglose, flgReq2firma: boolean, headers: HttpHeaders, period: number) {
        const input1: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo1Rpt');
        const input2: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo2Rpt');
        const input3: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo3Rpt');
        const input4: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo4Rpt');
        const unidMone = this.breakdown.codUnidMoneda;
        const iva = this.breakdown.valIva;
        this.breakdown = body;
        this.field_fecDeflacBase.nativeElement.disabled = false;
        this.field_fecDeflacBaseBtn.nativeElement.disabled = false;
        this.breakdown.codUnidMoneda = unidMone;
        this.breakdown.valIva = iva;
        if (this.gDesgloseEnRevision && (this.role === USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === USER_ROLE_PLAT_DESGLOSE_JEFE)) {
            this.breakdown.rolEmpleadoRevMod = this.operatorCode.toString();
        }
        this.breakdown.valDeflacBaseStr = this.breakdown.valDeflacBase.toString(10).replace('.', ',');
        this.breakdown.flgReq2firma = flgReq2firma;
        let isReadOnly = false;
        if (!isNullOrUndefined(this.extraDataDesglose.nomUserOpen) && this.usuario.loginName !== this.extraDataDesglose.nomUserOpen) {
            isReadOnly = true;
        }
        this.columns = initColumnas(isReadOnly, period);
        this.otherData.nativeElement.disabled = false;
        if (!isNullOrUndefined(this.extraDataDesglose.nomUserOpen)) {
            if (this.extraDataDesglose.nomUserOpen !== this.usuario.loginName) {
                this.state.state = this.translateService.instant('breakdown.state.busy');
                this.state.stateClass = 'danger';
            } else {
                this.state.state = this.translateService.instant('breakdown.state.free');
                this.state.stateClass = 'success';
            }
        } else {
            this.state.state = this.translateService.instant('breakdown.state.free');
            this.state.stateClass = 'success';
        }
        this.fieldBreakdownSubTypeDesc.nativeElement.disabled = (!isNullOrUndefined(this.breakdown.codSubtipoDes) &&
            (this.breakdown.codSubtipoDes !== COD_SUBTIPO_DESG_OTHER1 && this.breakdown.codSubtipoDes !== COD_SUBTIPO_DESG_OTHER2));
        if (this.role === this.USER_ROLE_PLAT_DESGLOSE_EJECUTOR) {
            this.buttonSendToReview.nativeElement.disabled = true;
        } else {
            if (this.gDesgloseEnRevision === true) {
                this.editaRevisor = true;
            }
            this.buttonSendToReview.nativeElement.disabled = true;
            this.buttonSendToExecutor.nativeElement.disabled = true;
            this.buttonApproveBreakdown.nativeElement.disabled = true;
        }
        this.buttonSave.nativeElement.disabled = false;
        this.buttonProcess.nativeElement.disabled = false;
        this.onChangueCargarPeriodos(period);
        let existePeriod = 0;
        this.activePeriodsEdit.forEach((number, index) => {
            if(number === period) {
                existePeriod++;
            }
        });
        if(existePeriod === 0) {
            this.activePeriodsEdit.push(period);
        }
        if (this.breakdown.codMoneda !== COD_TIPO_MONEDA_PESOS) {
            this.extraDataDesglose.deflatorPeriodStr[this.activePeriodsIndexes[period]] = '1';
        }
        this.beforeChangeClick();
        if(isNullOrUndefined(this.extraDataDesglose.fecPeriodo[period]) ) {
            this.disableElementsByIds(['buttonSave','buttonInProcess'],true);
        }
        switch (period) {
            case 0: {
                if(input1.value === '') {
                    this.disableElementsByIds(['buttonSave','buttonInProcess'],true);
                }
                    break;
            }
            case 1: {
                if(input2.value === '') {
                    this.disableElementsByIds(['buttonSave','buttonInProcess'],true);
                }
                    break;
            }
            case 2: {
                if(input3.value === '') {
                    this.disableElementsByIds(['buttonSave','buttonInProcess'],true);
                }
                    break;
            }
            case 3: {
                if(input4.value === '') {
                    this.disableElementsByIds(['buttonSave','buttonInProcess'],true);
                }
                    break;
            }
        }
        this.disableElementsByIds(['field_breakdownTemplateType','field_breakdownType','field_breakdownSubType', 'field_breakdownSubTypeDesc', 'field_currency' ], true);
        this.disableElementsByIds([ 'buttonRecover', 'buttonGeneratePrint', 'buttonPrint', 'buttonClone', 'buttonDelete'], false);
    }

    private assignConcepts(body: IConcepto[], headers: HttpHeaders) {
        if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
            this.data = [];
            this.data1 = [];
            this.dataGlobal = [];
            this.hiddenRows = [];
            // let lastGroup = '';
            let i = 0;
            conceptos.forEach((concept, index) => {
                if (isNullOrUndefined(concept.codTipoRegis) || concept.codTipoRegis === '' || concept.codConcepto === ' ') { // Si es un encabezado de grupo
                    // El encabezado se agrega uno solo en todos los períodos
                    if (concept.codTipoRegis === '' || isNullOrUndefined(concept.codTipoRegis)) {
                        this.data.push([
                            concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                            '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                            concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                            '', // G
                            this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                            null, null, null, null, null, null, null, null, null, null, null, null
                        ]);
                        if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                            this.data1.push([
                                concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                '', //
                                this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                null, null, null, null, null, null, null, null, null, null, null, null
                            ]);
                        }
                    } else {
                        this.data.push([
                            concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                            '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                            concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                            '', // G
                            concept.glsConcepto,
                            null, null, null, null, null, null, null, null, null, null, null, null
                        ]);
                        if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                            this.data1.push([
                                concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                '', //
                                concept.glsConcepto,
                                null, null, null, null, null, null, null, null, null, null, null, null
                            ]);
                        }
                    }
                } else {
                    if (concept.flgIsTotal === 0) {
                        this.data.push([
                            concept.codTipoConcep,
                            concept.codTipoRegis,
                            concept.flgIsTotal,
                            concept.codConcepto,
                            concept.glsConcepto,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0,
                            0
                        ]);
                    } else {
                        this.data.push([
                            concept.codTipoConcep,
                            concept.codTipoRegis,
                            concept.flgIsTotal,
                            concept.codConcepto,
                            concept.glsConcepto,
                            '',
                            '',
                            '',
                            '',
                            '',
                            '',
                            '',
                            '',
                            '',
                            '',
                            '',
                            ''
                        ]);
                    }
                    if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                        if (concept.flgIsTotal === 0) {
                            this.data1.push([
                                concept.codTipoConcep,
                                concept.codTipoRegis,
                                concept.flgIsTotal,
                                concept.codConcepto,
                                concept.glsConcepto,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0,
                                0
                            ]);
                        } else {
                            this.data1.push([
                                concept.codTipoConcep,
                                concept.codTipoRegis,
                                concept.flgIsTotal,
                                concept.codConcepto,
                                concept.glsConcepto,
                                '',
                                '',
                                '',
                                '',
                                '',
                                '',
                                '',
                                '',
                                '',
                                '',
                                '',
                                ''
                            ]);
                        }
                    }
                }
                // update hidden columns...
                if (concept.codTipoConcep !== COD_TIPO_CONCEP_EST_RESULT && concept.codTipoConcep !== COD_TIPO_CONCEP_BALANCE) {
                    this.hiddenRows.push(i);
                }
                if (concept.codTipoConcep !== COD_TIPO_CONCEP_IND_FINAN && concept.codTipoConcep !== COD_TIPO_CONCEP_GEN_FONDOS) {
                    this.hiddenRows1.push(i);
                }
                i = i + 1;
            });
        } else {
            this.data = [ [] ];
            this.data1 = [ [] ];
            this.dataGlobal = [[]];
        }
        // Se usa para que se vea completa la tabla + linea 2121
        setTimeout(() => {
            document.getElementById(this.tabGenFondIndFinId).click();
            document.getElementById(this.tabBreakdownDetailId).click();
        }, 200);
    }

    onPeriodBaseDateChange(event: any) {
        const fecha: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecDeflacBase');
        if (fecha.value !== '') {
            if (moment(fecha.value, 'dd-MM-YYYY').isValid()) {
                // this.buttonProcess.nativeElement.disabled = false;
                // this.buttonSave.nativeElement.disabled = false;
                this.colHeadersConfig();
                this.btnEdit1.disabled = true;
            } else {
                this.buttonProcess.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = true;
                // deflactorbase
                document.getElementById('field_fecDeflacBase').focus();
            }
        } else {
            document.getElementById('field_fecDeflacBase').focus();
            this.buttonProcess.nativeElement.disabled = true;
            this.buttonSave.nativeElement.disabled = true;
        }
    }

    onPeriodDateChange1(event: any) {
        // todo: ejemplo de periodos visibles por posicion visbles al usuario, falta validadar existencia de estos

        const fecha: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo1Rpt');
        if (fecha.value !== '') {
            if (moment(fecha.value, 'DD-MM-YYYY', true).isValid()) {
                // this.buttonProcess.nativeElement.disabled = false;
                // this.buttonSave.nativeElement.disabled = false;
                let existeCount = 0;
                for(let i = 0; i< this.extraDataDesglose.fecPeriodo.length; i++) {
                    const fechaExtra = this.extraDataDesglose.fecPeriodo[i].format('DD-MM-YYYY').toString();
                    if(moment(fechaExtra, 'DD-MM-YYYY').isSame(moment(fecha.value, 'DD-MM-YYYY'))) {
                            existeCount++;
                    }
                }
                if (existeCount === 1) {
                    this.colHeadersConfig();
                    this.btnEdit1.disabled = true;
                    if(jQuery('#field_monthsPeriod1').val().length > 0 && jQuery('#field_deflatorPeriod1').val().length > 0) {
                        this.buttonProcess.nativeElement.disabled = false;
                        this.buttonSave.nativeElement.disabled = false;
                    } else {
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                    }
                } else {
                        this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechas');
                        const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo1Rpt');
                        campo.value = '';
                        document.getElementById('field_fecPeriodo1Rpt').focus();
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                }
            } else {
                this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechasInvalid');
                const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo1Rpt');
                campo.value = '';
                document.getElementById('field_fecPeriodo1Rpt').focus();
                this.buttonProcess.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = true;
            }
        } else {
            document.getElementById('field_fecPeriodo1Rpt').focus();
            this.buttonProcess.nativeElement.disabled = true;
            this.buttonSave.nativeElement.disabled = true;
        }
    }

    onPeriodDateChange2(event: any) {
        const fecha: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo2Rpt');
        if (fecha.value !== '') {
            if (moment(fecha.value, 'DD-MM-YYYY', true).isValid()) {
                let existeCount = 0;
                for(let i = 0; i< this.extraDataDesglose.fecPeriodo.length; i++) {
                    const fechaExtra = this.extraDataDesglose.fecPeriodo[i].format('DD-MM-YYYY').toString();
                    if(moment(fechaExtra, 'DD-MM-YYYY').isSame(moment(fecha.value, 'DD-MM-YYYY'))) {
                            existeCount++;
                    }
                }
                if (existeCount === 1) {
                    this.colHeadersConfig();
                    this.btnEdit2.disabled = true;
                    if(jQuery('#field_monthsPeriod2').val().length > 0 && jQuery('#field_deflatorPeriod2').val().length > 0) {
                        this.buttonProcess.nativeElement.disabled = false;
                        this.buttonSave.nativeElement.disabled = false;
                    } else {
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                    }
                } else {
                        this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechas');
                        const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo2Rpt');
                        campo.value = '';
                        document.getElementById('field_fecPeriodo2Rpt').focus();
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                }
            } else {
                this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechasInvalid');
                const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo2Rpt');
                campo.value = '';
                document.getElementById('field_fecPeriodo2Rpt').focus();
                this.buttonProcess.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = true;
            }
        } else {
            document.getElementById('field_fecPeriodo2Rpt').focus();
            this.buttonProcess.nativeElement.disabled = true;
            this.buttonSave.nativeElement.disabled = true;
        }

    }

    onPeriodDateChange3(event: any) {
        const fecha: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo3Rpt');
        if (fecha.value !== '') {
            if (moment(fecha.value, 'DD-MM-YYYY', true).isValid()) {
                let existeCount = 0;
                for(let i = 0; i< this.extraDataDesglose.fecPeriodo.length; i++) {
                    const fechaExtra = this.extraDataDesglose.fecPeriodo[i].format('DD-MM-YYYY').toString();
                    if(moment(fechaExtra, 'DD-MM-YYYY').isSame(moment(fecha.value, 'DD-MM-YYYY'))) {
                            existeCount++;
                    }
                }
                if (existeCount === 1) {
                    this.colHeadersConfig();
                    this.btnEdit3.disabled = true;
                    if(jQuery('#field_monthsPeriod3').val().length > 0 && jQuery('#field_deflatorPeriod3').val().length > 0) {
                        this.buttonProcess.nativeElement.disabled = false;
                        this.buttonSave.nativeElement.disabled = false;
                    } else {
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                    }
                } else {
                        this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechas');
                        const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo3Rpt');
                        campo.value = '';
                        document.getElementById('field_fecPeriodo3Rpt').focus();
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                }
            } else {
                this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechasInvalid');
                const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo3Rpt');
                campo.value = '';
                document.getElementById('field_fecPeriodo3Rpt').focus();
                this.buttonProcess.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = true;
            }
        } else {
            document.getElementById('field_fecPeriodo3Rpt').focus();
            this.buttonProcess.nativeElement.disabled = true;
            this.buttonSave.nativeElement.disabled = true;
        }
    }

    onPeriodDateChange4(event: any) {
        const fecha: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo4Rpt');
        if (fecha.value !== '') {
            if (moment(fecha.value, 'DD-MM-YYYY', true).isValid()) {
                let existeCount = 0;
                for(let i = 0; i< this.extraDataDesglose.fecPeriodo.length; i++) {
                        const fechaExtra = this.extraDataDesglose.fecPeriodo[i].format('DD-MM-YYYY').toString();
                        if(moment(fechaExtra, 'DD-MM-YYYY').isSame(moment(fecha.value, 'DD-MM-YYYY'))) {
                                existeCount++;
                        }
                }
                if (existeCount === 1) {
                    this.colHeadersConfig();
                    this.btnEdit4.disabled = true;
                    if(jQuery('#field_monthsPeriod4').val().length > 0 && jQuery('#field_deflatorPeriod4').val().length > 0) {
                        this.buttonProcess.nativeElement.disabled = false;
                        this.buttonSave.nativeElement.disabled = false;
                    } else {
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                    }
                } else {
                        this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechas');
                        const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo4Rpt');
                        campo.value = '';
                        document.getElementById('field_fecPeriodo4Rpt').focus();
                        this.buttonProcess.nativeElement.disabled = true;
                        this.buttonSave.nativeElement.disabled = true;
                }
            } else {
                this.sweetAlert2.mensajeQuestion('breakdown.warning','breakdown.warningBreakdownFechasInvalid');
                const campo:HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo4Rpt');
                campo.value = '';
                document.getElementById('field_fecPeriodo4Rpt').focus();
                this.buttonProcess.nativeElement.disabled = true;
                this.buttonSave.nativeElement.disabled = true;
            }
        } else {
            document.getElementById('field_fecPeriodo4Rpt').focus();
            this.buttonProcess.nativeElement.disabled = true;
            this.buttonSave.nativeElement.disabled = true;
        }
    }

    // deflactor
    onDeflactorChange(number: number) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        if (hotInstance && hotInstance1) {
            switch (number) {
                case 0: // Deflactor bases
                    if (!isNullOrUndefined(this.breakdown.valDeflacBaseStr) && this.breakdown.valDeflacBaseStr.length > 0) {
                        this.breakdown.valDeflacBase = Number(this.breakdown.valDeflacBaseStr.replace(',', '.'));
                        if (this.breakdown.valDeflacBase > 0) {
                            if (this.breakdown.valDeflacBaseStr.split(',').length > 1 && this.breakdown.valDeflacBaseStr.split(',')[1].length < 1) {
                                // document.getElementById('field_baseDeflator').focus();
                                // this.buttonSave.nativeElement.disabled = true;
                            }
                        } else {
                            this.breakdown.valDeflacBase = 1;
                            this.breakdown.valDeflacBaseStr = '1';
                        }
                    } else {
                        this.breakdown.valDeflacBase = null;
                    }
                    hotInstance.getPlugin('formulas').setVariable(DEFLACT_PERIODS, this.breakdown.valDeflacBase);
                    hotInstance1.getPlugin('formulas').setVariable(DEFLACT_PERIODS, this.breakdown.valDeflacBase);
                    this.onIvaChange();
                    break;
                case 1: // Deflactor período 1
                    if (!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ])
                        && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ].length > 0) {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 0 ] ] =
                            Number(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ].replace(',', '.'));
                    } else {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 0 ] ] = null;
                    }
                    hotInstance.getPlugin('formulas').setVariable(DEF_PER1, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 0 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(DEF_PER1, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 0 ] ]);
                    break;
                case 2: // Deflactor período 2
                    if (!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ])
                        && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ].length > 0) {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 1 ] ] =
                            Number(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ].replace(',', '.'));
                    } else {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 1 ] ] = null;
                    }
                    hotInstance.getPlugin('formulas').setVariable(DEF_PER2, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 1 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(DEF_PER2, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 1 ] ]);
                    break;
                case 3: // Deflactor período 3
                    if (!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ])
                        && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ].length > 0) {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 2 ] ] =
                            Number(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ].replace(',', '.'));
                    } else {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 2 ] ] = null;
                    }
                    hotInstance.getPlugin('formulas').setVariable(DEF_PER3, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 2 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(DEF_PER3, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 2 ] ]);
                    break;
                case 4: // Deflactor período 4
                    if (!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ])
                        && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ].length > 0) {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 3 ] ] =
                            Number(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ].replace(',', '.'));
                    } else {
                        this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 3 ] ] = null;
                    }
                    hotInstance.getPlugin('formulas').setVariable(DEF_PER4, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 3 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(DEF_PER4, this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 3 ] ]);
                    break;
                default:
                    break;
            }
        }
    }

    onMonthNumberChange(number: number) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        if (hotInstance && hotInstance1) {
            switch (number) {
                case 1: // Número de meses período 1
                    if (!isNullOrUndefined(this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] - 1 ])) {
                        hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER0, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] - 1 ]);
                        hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER0, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] - 1 ]);
                        hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] - 1 ]);
                        hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] - 1 ]);
                    } else {
                        hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER0, 0);
                        hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER0, 0);
                        hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER, 0);
                        hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER, 0);
                    }
                    hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER1, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER1, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 0 ] ]);
                    break;
                case 2: // Número de meses período 2
                    hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER2, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 1 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER2, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 1 ] ]);
                    break;
                case 3: // Número de meses período 3
                    hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER3, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 2 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER3, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 2 ] ]);
                    break;
                case 4: // Número de meses período 4
                    hotInstance.getPlugin('formulas').setVariable(CANT_MESES_PER4, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 3 ] ]);
                    hotInstance1.getPlugin('formulas').setVariable(CANT_MESES_PER4, this.extraDataDesglose.monthsPeriod[ this.activePeriodsIndexes[ 3 ] ]);
                    break;
                default:
                    break;
            }
        }
    }

    onIvaChange() {
        if (!isNullOrUndefined(this.breakdown.valIva) && this.breakdown.valIva.toString().trim()!=='') {
            if (!isNaN(Number(this.breakdown.valIva))) {
                if (this.breakdown.valIva / 100 < 0 || this.breakdown.valIva / 100 > 1) {
                    this.breakdown.valIva = 0;
                    this.sweetAlert2.mensajeError('breakdown.warning', 'breakdown.ivaNoPermitido');
                    document.getElementById('field_iva').focus();
                } else {
                    const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
                    if (hotInstance) {
                        hotInstance.getPlugin('formulas').setVariable(DEFLACT_BASE_IVA, this.breakdown.valIva / 100);
                    }
                    const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
                    if (hotInstance1) {
                        hotInstance1.getPlugin('formulas').setVariable(DEFLACT_BASE_IVA, this.breakdown.valIva / 100);
                    }
                }
            } else {
                this.breakdown.valIva = 0;
                this.sweetAlert2.mensajeError('breakdown.warning', 'breakdown.ivaNoPermitido');
                document.getElementById('field_iva').focus();
            }
        } else {
            this.breakdown.valIva = 0;
        }
    }
    onIvaKeyDownOnEnter(event) {
        this.canOpenConsolidatedCompanies = false;
    }
    beforeChange(event: NgbTabChangeEvent) {
        this.loaderService.isLoading.next(true);
        setTimeout(() => {
            this.isDivFormulaValueHidden = event.nextId === this.tabPrintId;
            this.currentTabId = event.nextId;
            let timeout = 100;
            const row = 2;
            const col = 1;
            let hideExtraColumns = false;
            if (event.nextId === this.tabBreakdownDetailId) {
                this.currentTableId = this.handOnTableDetailInstanceId;
                this.isDivEditor = false;
                timeout = 200;

            }
            if (event.nextId === this.tabGenFondIndFinId) {
                if (this.isRecover) {
                    this.recargarActiveConcepts();
                    const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
                    if (hotInstance1) {
                        setTimeout(() => {
                            hotInstance1.getPlugin('formulas').recalculateFull();
                            // this.anularValoresIndefinidosTablas(null, hotInstance1);
                        }, 500);
                    }
                }
                this.isDivEditor = true;
                this.currentTableId = this.handOnTableGenFonInstanceId;
                hideExtraColumns = true;
            }
            if (event.nextId === this.tabPrintId) {
                this.isDivEditor = true;
                this.currentTableId = null;
                const hotInstance = this.hotRegisterer.getInstance(this.handOnTableImpreInstanceId);
                if (hotInstance) {
                    setTimeout(() => {
                        hotInstance.render();
                        this.loaderService.isLoading.next(false);
                    }, timeout);
                    hotInstance.selectCell(row, col);
                }
            }
            if (!isNullOrUndefined(this.currentTableId)) {
                this.updateSelectedTabTableView(timeout, row, col, hideExtraColumns);
            }
        }, 200);
        this.hideEvaluationMessage();
    }
    beforeChangeClick() {
        this.loaderService.isLoading.next(true);
        setTimeout(() => {
            this.isDivFormulaValueHidden = this.currentTabId === this.tabPrintId;
            let timeout = 100;
            const row = 2;
            const col = 1;
            let hideExtraColumns = false;
            if (this.currentTabId === this.tabBreakdownDetailId) {
                this.currentTableId = this.handOnTableDetailInstanceId;
                this.isDivEditor = false;
                timeout = 200;
            }
            if (this.currentTabId === this.tabGenFondIndFinId) {
                if (this.isRecover) {
                    this.recargarActiveConcepts();
                    const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
                    if (hotInstance1) {
                        setTimeout(() => {
                            hotInstance1.getPlugin('formulas').recalculateFull();
                            // this.anularValoresIndefinidosTablas(null, hotInstance1);
                        }, 500);
                    }
                }
                this.isDivEditor = true;
                this.currentTableId = this.handOnTableGenFonInstanceId;
                hideExtraColumns = true;
            }
            if (this.currentTabId === this.tabPrintId) {
                this.isDivEditor = true;
                this.currentTableId = null;
                const hotInstance = this.hotRegisterer.getInstance(this.handOnTableImpreInstanceId);
                if (hotInstance) {
                    setTimeout(() => {
                        hotInstance.render();
                        this.loaderService.isLoading.next(false);
                    }, timeout);
                    hotInstance.selectCell(row, col);
                }
            }
            if (!isNullOrUndefined(this.currentTableId)) {
                this.updateSelectedTabTableView(timeout, row, col, hideExtraColumns);
            }
        }, 200);
        this.hideEvaluationMessage();
    }

    /**
     * Se ejecuta al hacer clic en el botón 'En proceso'.
     */
    buttonInProcessOnClick() {
        this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
            'breakdown.confirmation',
            'breakdown.saveWithoutValidateConfirmation',
            'question',
            'breakdown.yes',
            'breakdown.no'
        ).subscribe(res => {
            if (res) {
                this.loaderService.isLoading.next(true);
                setTimeout(() => {
                    this.saveInProcess(false,false);
                }, 200);
            }
            this.questionMessageSubscription.unsubscribe();
        });
    }

    private saveInProcess(isRapid:boolean,skipNotRequiredValidations?:boolean) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        if (hotInstance && hotInstance1) {
            if(!isRapid) {
                this.recargarActiveConcepts();
                if (hotInstance1) {
                    hotInstance1.getPlugin('formulas').recalculateFull();
                    hotInstance1.render();
                }
            }
            // const localData: any = hotInstance.getSourceData(); // Estos son los datos que contiene las fórmulas tipo Excel.
            const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
            this.extraDataDesglose.enProceso = true;
            this.extraDataDesglose.validado = false;
            this.extraDataDesglose.canValidate = false;
            this.extraDataDesglose.requireValidation = false;
            this.extraDataDesglose.isTemporal = false;
            this.extraDataDesglose.conceptos = conceptosOriginal.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE ||
                el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
            this.extraDataDesglose.data = hotInstance.getSourceData();
            this.extraDataDesglose.visualData = visualData;
            this.extraDataDesglose.conceptos2 = conceptosEliminar;
            if(!isRapid) {
                const visualData1: any = hotInstance1.getData();
                this.extraDataDesglose.conceptos1 = conceptosOriginal;
                this.extraDataDesglose.data1 = hotInstance1.getSourceData();
                this.extraDataDesglose.visualData1 = visualData1;
                this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes, skipNotRequiredValidations).subscribe(
                    (data: HttpResponse<IBreakdownValidationError[]>) => {
                        if (!isNullOrUndefined(data.body)) {
                            if (!skipNotRequiredValidations) {
                                if (data.body.length > 0) {
                                    const optionalQuestionMessage = 'breakdown.errors.questionMessageSave';
                                    this.displayValidationErrors(data.body, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_PROCESS, optionalQuestionMessage);
                                } else {
                                    this.processSave(isRapid, skipNotRequiredValidations);
                                }
                            } else {
                                // Llamamos al servicio grabar datos de desglose pero indicando que no realice validaciones
                                this.processSave(isRapid, skipNotRequiredValidations);
                            }
                        }
                    },
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            } else {
                this.processSave(isRapid, skipNotRequiredValidations);
            }
        }
    }

    /**
     *
     * @param isRapid
     * @param skipNotRequiredValidations
     */
    processSave(isRapid:boolean,skipNotRequiredValidations:boolean) {
        this.desgloseService.save(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes,this.activePeriodsEdit, skipNotRequiredValidations,isRapid).subscribe(
            (resp: HttpResponse<IDesglose>) => {
                if(!this.cambioPestana) {
                    this.onSuccessSave(resp.body, resp.headers);
                }
                if(isRapid) {
                    this.desgloseService.clearBreakdownUserOpenAndSetStatusInProcess(this.breakdown).subscribe(
                        (datos: HttpResponse<IDesglose>) => {
                            if(!isNullOrUndefined(this.sessionStorage.retrieve(SESSION_VARIABLE_CLOSE)) ) {
                                this.loginService.logout();
                            }
                        },
                    );
                } else {
                    if(!isNullOrUndefined(this.sessionStorage.retrieve(SESSION_VARIABLE_CLOSE))) {
                        this.loginService.logout();
                    }
                }
            },
            (resp: HttpErrorResponse) => {
                    this.onError(resp.message);
            });
    }
    /**
     * Se ejecuta al hacer clic en el botón 'Grabar'.
     */
    public buttonSaveOnClick() {
        this.save();
    }

    /**
     * Lógica de negocio para guardar un desglose realizando validaciones de cuadratura.
     *
     * @param askConfirmation - Pide confirmación de si desea guardar o no.
     */
    private save(askConfirmation?: boolean,skipNotRequiredValidations?:boolean) {
        if (isNullOrUndefined(askConfirmation)) {
            askConfirmation = true;
        }
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (hotInstance && hotInstance1) {
            if (askConfirmation) {
                this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                    'breakdown.confirmation',
                    'breakdown.saveWithoutConfirmation',
                    'question',
                    'breakdown.yes',
                    'breakdown.no'
                ).subscribe(res => {
                    if (res) {
                        this.invokeSaveBreakdown(hotInstance1, hotInstance, false);
                    }
                    this.questionMessageSubscription.unsubscribe();
                });
            } else { // Si no se pide confirmación viene de la acción "Si" del popup de errores
                this.invokeSaveBreakdown(hotInstance1, hotInstance, true);
            }
        }
    }

    private invokeSaveBreakdown(hotInstance1, hotInstance, skipNotRequiredValidations: boolean) {
        this.loaderService.isLoading.next(true);
        this.extraDataDesglose.canLoadSBIF = this.canLoadSBIF();
        setTimeout(() => {
            this.recargarActiveConcepts();
            if (hotInstance1) {
                hotInstance1.getPlugin('formulas').recalculateFull();
                hotInstance1.render();
            }
            const visualData: any = hotInstance.getData();
            this.extraDataDesglose.canValidate = true;
            this.extraDataDesglose.conceptos = conceptosOriginal.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE ||
                el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
            this.extraDataDesglose.data = hotInstance.getSourceData();
            this.extraDataDesglose.visualData = visualData;

            const visualData1: any = hotInstance1.getData();
            this.extraDataDesglose.conceptos1 = conceptosOriginal;
            this.extraDataDesglose.conceptos2 = conceptosEliminar;
            this.extraDataDesglose.data1 = hotInstance1.getSourceData();
            this.extraDataDesglose.visualData1 = visualData1;
            this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes, skipNotRequiredValidations).subscribe(
                (data: HttpResponse<IBreakdownValidationError[]>) => {
                    this.onSuccessValidationsSave(data.body, skipNotRequiredValidations);
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        }, 200);
    }

    /**
     * Se ejecuta al guardar un desglose luego de llamar al servicio de validaciones de cuadratura.
     *
     * @param data - Array de elementos con errores de validación de cuadratura.
     */
    private onSuccessValidationsSave(data: IBreakdownValidationError[], skipNotRequiredValidations: boolean) {
        if (!isNullOrUndefined(data)) {
            if(!skipNotRequiredValidations) {
                if (data.length > 0) {
                    const optionalQuestionMessage = 'breakdown.errors.questionMessageSave';
                    this.displayValidationErrors(data, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SAVE, optionalQuestionMessage);
                } else {
                    this.confirmSave(skipNotRequiredValidations);
                }
            } else {
                this.confirmSave(skipNotRequiredValidations);
            }
        }
    }
    confirmSave(skipNotRequiredValidations: boolean) {
        this.desgloseService.save(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes, this.activePeriodsEdit, skipNotRequiredValidations).subscribe(
            (rpt: HttpResponse<IDesglose>) => {
                if (this.editaRevisor && (!this.breakdown.flgReq2firma)) {
                    this.buttonSave.nativeElement.disabled = true;
                    this.buttonApproveBreakdown.nativeElement.disabled = false;
                    this.buttonProcess.nativeElement.disabled = true;
                } else {
                    this.buttonSave.nativeElement.disabled = true;
                    this.buttonSendToReview.nativeElement.disabled = false;
                    this.buttonProcess.nativeElement.disabled = true;
                }
                this.onSuccessSave(rpt.body, rpt.headers);
            },
            (rpt: HttpErrorResponse) => this.onError(rpt.message)
        );
    }

    /**
     * Se ejecuta al hacer clic en el botón 'Aprobar'.
     */
    buttonApproveOnClick() {
        this.recargarActiveConcepts();
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        if (hotInstance1) {
            hotInstance1.getPlugin('formulas').recalculateFull();
        }
        this.approve();
    }

    /**
     * Lógica de negocio para aprobar un desglose realizando validaciones de cuadratura.
     */
    private approve() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (hotInstance) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.approveWithoutConfirmation',
                'question',
                'breakdown.yes',
                'breakdown.no'
            ).subscribe(res => {
                if (res) {
                    this.loaderService.isLoading.next(true);
                    setTimeout(() => {
                        const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
                        this.extraDataDesglose.numPeriodosLoaded = !isNullOrUndefined(this.extraDataDesglose.periodos) ? this.extraDataDesglose.periodos.length : 0;
                        this.extraDataDesglose.canValidate = true;
                        this.extraDataDesglose.conceptos = conceptos.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE ||
                            el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
                        this.extraDataDesglose.data = this.data1;
                        this.extraDataDesglose.visualData = visualData;
                        this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes).subscribe(
                            (data: HttpResponse<IBreakdownValidationError[]>) => {
                                this.onSuccessValidationsApprove(data.body);
                            },
                            (data: HttpErrorResponse) => this.onError(data.message)
                        );
                    }, 200);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        }
    }

    /**
     * Se ejecuta al aprobar un desglose luego de llamar al servicio de validaciones de cuadratura.
     *
     * @param data - Array de elementos con errores de validación de cuadratura.
     */
    private onSuccessValidationsApprove(data: IBreakdownValidationError[]) {
        if (!isNullOrUndefined(data)) {
            if (data.length > 0) {
                const optionalQuestionMessage = 'breakdown.errors.questionMessageApprove';
                this.displayValidationErrors(data, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_APPROVE, optionalQuestionMessage);
            } else {
                // ACTUALIZAMOS EL DESGLOSE
                this.breakdown.nomUserOpen = null;
                this.breakdown.fecDesgloDef = moment(new Date(), DATE_TIME_FORMAT);
                this.breakdown.rolEmpleadoDesDef = this.operatorCode.toString(10);
                this.breakdown.nomRevisorDef = this.usuario.loginName;
                this.breakdown.numRolChgEstado = this.operatorCode.toString(10);
                // GENERAMOS AUDITORIA
                const auditoria: IDesgloseAuditoria = new DesgloseAuditoria();
                auditoria.codEvento = EVENTO_AUDITORIA_CAMBIO_ESTADO;
                auditoria.codEstadoNvo = ESTADO_DEFINITIVO;
                auditoria.glsComentario = COMENTARIO_AUDITORIA_CAMBIO_ESTADO;
                // GENERAMOS DESGLOSE REVISION
                const desgloseRevision: IDesgloseRevision = new DesgloseRevision();
                desgloseRevision.codCliente = this.breakdown.codCliente;
                desgloseRevision.codTipoDes = Number(this.breakdown.codTipoDes);
                desgloseRevision.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
                desgloseRevision.codMoneda = this.breakdown.codMoneda;
                desgloseRevision.codPlantilla = this.breakdown.codPlantilla;
                desgloseRevision.fecTerminoRevision = moment(new Date(), DATE_TIME_FORMAT);
                // GENERAMOS EL MENSAJE A ENVIAR
                const desgloseMensaje: IDesgloseMensaje = new DesgloseMensaje();
                desgloseMensaje.codCliente = this.breakdown.codCliente;
                desgloseMensaje.codPlantilla = Number(this.breakdown.codPlantilla);
                desgloseMensaje.codTipoDes = Number(this.breakdown.codTipoDes);
                desgloseMensaje.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
                desgloseMensaje.codMoneda = this.breakdown.codMoneda;
                desgloseMensaje.numRolEmpleado = this.operatorCode.toString(10);
                desgloseMensaje.fecEvent = moment(new Date(), DATE_TIME_FORMAT);
                // CREAMOS LOS DATOS Y APROBAMOS DESGLOSE
                const breakdownSendRequest: IBreakdownSendRequest = new BreakdownSendRequest(this.breakdown, auditoria, desgloseRevision, desgloseMensaje, this.extraDataDesglose);
                this.desgloseService.sendForApproval(breakdownSendRequest).subscribe(
                    (datos: HttpResponse<IDesglose>) => {
                        /*this.recargarConceptosFormulasDigitadas();*/
                        this.breakdown.glsEstado = datos.body.glsEstado;
                        this.breakdown.codEstado = datos.body.codEstado;
                        this.buttonSendToExecutor.nativeElement.disabled = true;
                        this.buttonApproveBreakdown.nativeElement.disabled = true;
                        this.sweetAlert2.mensajeSuccess('breakdown.warning', 'breakdown.approveConfirmation');
                        this.resetAllElementsToInitialState(false);
                    },
                    (datos: HttpErrorResponse) => this.onError(datos.message)
                );
            }
        }
    }

    /**
     * Se ejecuta al enviar a revisión un desglose luego de llamar al servicio de validaciones de cuadratura.
     *
     * @param data - Array de elementos con errores de validación de cuadratura.
     */
    private onSuccessValidationsReview(data: IBreakdownValidationError[]) {
        const that = this;
        if (!isNullOrUndefined(data)) {
            if (data.length > 0) {
                const optionalQuestionMessage = 'breakdown.errors.questionMessageSendToReview';
                this.displayValidationErrors(data, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SEND_TO_REVIEW, optionalQuestionMessage);
            } else {
                this.ngbModalShippingReview = this.modalService.open(BreakdownShippingReviewComponent as Component, {
                    windowClass: 'shipping-review',
                    backdrop: 'static'
                });
                this.ngbModalShippingReview.componentInstance.codeRevisor = this.reviewerNumRolOnSuccessValidationsReview;
                this.ngbModalShippingReview.componentInstance.breakdown = that.breakdown;
                this.ngbModalShippingReview.componentInstance.hayNotificacion = this.isNotificacion;
                this.ngbModalShippingReview.componentInstance.extraDataDesglose = that.extraDataDesglose;
                this.ngbModalShippingReview.componentInstance.mensajeSoloLectura = that.mensaje;
                this.ngbModalShippingReview.componentInstance.mensajeEnvio = that.mensajeEnvio;
                this.ngbModalShippingReview.componentInstance.nameCliente = that.client.name;
                this.ngbModalShippingReview.componentInstance.codeCliente = that.client.clientCode;
                this.ngbModalShippingReview.componentInstance.codeUser = that.operatorCode;
                this.ngbModalShippingReview.componentInstance.roleUser = that.role;
                this.ngbModalShippingReview.componentInstance.enviarShippingReviewEmit.subscribe(datos => {
                    this.breakdown.glsEstado = datos.glsEstado;
                    this.breakdown.codEstado = datos.codEstado;
                    this.buttonSendToReview.nativeElement.disabled = true;
                    const messageTittle = 'breakdown.warning';
                    let messageTextContent = 'breakdown.successRevisor';
                    this.turbineUserService.findByRoleisReemplazo(this.reviewerNumRolOnSuccessValidationsReview).subscribe(
                        (rpt: HttpResponse<ITurbineUsuarios>) => {
                            let messageTranslateObject;
                            if (isNullOrUndefined(rpt.body)) {
                                messageTextContent = 'breakdown.successRevisor';
                                messageTranslateObject = { revisor: this.reviewerNameOnSuccessValidationsReview };
                            } else {
                                messageTextContent = 'breakdown.successRevisorReemplazo';
                                messageTranslateObject = { revisor: this.reviewerNameOnSuccessValidationsReview, reemplazo: rpt.body.nombre };
                            }
                            this.sweetAlert2.mensajeSuccess(messageTittle, messageTextContent, messageTranslateObject);
                        },
                        (rpt: HttpErrorResponse) => {
                            this.sweetAlert2.mensajeSuccess(messageTittle, messageTextContent, { revisor: this.reviewerNameOnSuccessValidationsReview });
                        }
                    );
                    this.resetAllElementsToInitialState(false);
                });
                this.ngbModalShippingReview.result.then(
                    () => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalShippingReview = null;
                    },
                    () => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalShippingReview = null;
                    }
                );
            }
        }
    }

    /**
     * Se ejecuta al devolver un desglose a un ejecutor luego de llamar al servicio de validaciones de cuadratura.
     *
     * @param data - Array de elementos con errores de validación de cuadratura.
     */
    private onSuccessValidationsSendBackToExecutor(data: IBreakdownValidationError[]) {
        const that = this;
        if (!isNullOrUndefined(data)) {
            if (data.length > 0) {
                const optionalQuestionMessage = 'breakdown.errors.questionMessageSendBackToExecutor';
                this.displayValidationErrors(data, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SEND_BACK_TO_EXECUTOR, optionalQuestionMessage);
            } else {
                this.ngbModalShippingReview = this.modalService.open(BreakdownShippingReviewComponent as Component, {
                    windowClass: 'shipping-review',
                    backdrop: 'static'
                });

                this.ngbModalShippingReview.componentInstance.codeUser = that.operatorCode;
                this.ngbModalShippingReview.componentInstance.roleUser = that.role;
                this.ngbModalShippingReview.componentInstance.isEnviarRevisor = false;
                this.ngbModalShippingReview.componentInstance.breakdown = that.breakdown;
                this.ngbModalShippingReview.componentInstance.hayNotificacion = this.isNotificacion;
                this.ngbModalShippingReview.componentInstance.extraDataDesglose = that.extraDataDesglose;
                this.ngbModalShippingReview.componentInstance.mensajeSoloLectura = that.mensaje;
                this.ngbModalShippingReview.componentInstance.nameCliente = that.client.name;
                this.ngbModalShippingReview.componentInstance.codeCliente = that.client.clientCode;
                this.ngbModalShippingReview.componentInstance.codeEnviaRevision = that.roleEnviaRevision ;
                this.ngbModalShippingReview.componentInstance.devolverShippingEmit.subscribe(datos => {
                    this.breakdown.glsEstado = datos.glsEstado;
                    this.breakdown.codEstado = datos.codEstado;
                    this.buttonSendToExecutor.nativeElement.disabled = true;
                    this.buttonApproveBreakdown.nativeElement.disabled = true;
                    this.sweetAlert2.mensajeSuccess('breakdown.warning', 'breakdown.successEjecutor', { ejecutor: datos.nomUserModif });
                    this.resetAllElementsToInitialState(false);
                });
                this.ngbModalShippingReview.result.then(
                    () => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalShippingReview = null;
                    },
                    () => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalShippingReview = null;
                    }
                );
            }
        }
    }

    private displayValidationErrors(data: IBreakdownValidationError[], callMethod: number, optionalQuestionMessage?: string) {
        if (!isNullOrUndefined(data)) {
            let isAnyValidationRequired = false;
            for (let i = 0, needContinue = true; i < data.length && needContinue; i++) {
                if (data[ i ].isRequired) {
                    isAnyValidationRequired = true;
                    needContinue = false;
                }
            }
            const that = this;
            this.ngbModalRefErrors = this.modalService.open(BreakdownErrorsDialogComponent as Component, {
                windowClass: 'quadrature-validation-errors-dialog',
                backdrop: 'static'
            });
            this.ngbModalRefErrors.componentInstance.iBreakdownValidationErrorList = data;
            this.ngbModalRefErrors.componentInstance.isAnyValidationRequired = isAnyValidationRequired;
            if (isNullOrUndefined(optionalQuestionMessage)) {
                optionalQuestionMessage = 'breakdown.errors.questionMessageSave';
            }
            this.ngbModalRefErrors.componentInstance.questionMessage = optionalQuestionMessage;

            this.ngbModalRefErrors.componentInstance.onClickAcceptEvent.subscribe(response => {
                if (response === true) {
                    switch (callMethod) {
                        case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_PROCESS:
                            this.saveInProcess(false,true);
                            break;
                       case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SAVE:
                            this.save(false,true);
                            break;
                        case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_GENERATE_PRINT:
                            this.generatePrintData();
                            break;
                        case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_APPROVE:
                            this.onSuccessValidationsApprove([]); // Se envía con los datos de errores de validación como array vacío para que ejecute la lógica.
                            break;
                        case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SEND_BACK_TO_EXECUTOR:
                            this.onSuccessValidationsSendBackToExecutor([]);
                            break;
                        case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SEND_TO_REVIEW:
                            this.onSuccessValidationsReview([]);
                            break;
                        case this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_PRINT:
                            this.onSuccessValidationsPrint();
                            break;
                        default:
                            break;
                    }
                }
                that.ngbModalRefErrors.close();
                // that.ngbModalRefErrors = null;
            });

            this.ngbModalRefErrors.result.then(
                () => {
                    this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                    this.ngbModalRefErrors = null;
                },
                () => {
                    this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                    this.ngbModalRefErrors = null;
                }
            );
        }
    }

    private onSuccessSave(body: IDesglose, headers: HttpHeaders) {
        this.breakdown.codEstado = body.codEstado;
        this.breakdown.glsEstado = body.glsEstado;
        this.isEdicion = false;
        this.btnEdit1.disabled = false;
        this.btnEdit2.disabled = false;
        this.btnEdit3.disabled = false;
        this.btnEdit4.disabled = false;
        this.buttonProcess.nativeElement.disabled = true;
        this.columns = initColumnas(true);
        const alerts = this.jhiAlertService.get();
        this.recoverButtonOnClick();
    }
    onClickClone() {
        this.clone();
    }

    private clone() {
        const that = this;
        this.ngbModalRefClone = this.modalService.open(BreakdownCloneConfigDialogComponent as Component, {
            backdrop: 'static'
        });
        this.ngbModalRefClone.componentInstance.codCliente = this.breakdown.codCliente;
        this.ngbModalRefClone.componentInstance.codTipoPlantDes = this.breakdown.codPlantilla;
        this.ngbModalRefClone.componentInstance.codTipoDes = this.breakdown.codTipoDes;
        this.ngbModalRefClone.componentInstance.codSubtipoDes = this.breakdown.codSubtipoDes;
        this.ngbModalRefClone.componentInstance.codMoneda = this.breakdown.codMoneda;

        this.ngbModalRefClone.componentInstance.onAcceptCloneEventEmitter.subscribe(breakdownKey => {
            const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
            if (hotInstance) {
                const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
                const breakdownCloneRequestDTO: IBreakdownCloneRequestDTO = new BreakdownCloneRequestDTO(that.breakdown.codCliente, that.breakdown.codTipoDes,
                    breakdownKey.codTipoDes, that.breakdown.codSubtipoDes, breakdownKey.codSubtipoDes, that.breakdown.codMoneda, breakdownKey.codMoneda,
                    that.breakdown.codPlantilla);

                // Llamamos al servicio clonar
                this.desgloseService.clone(breakdownCloneRequestDTO).subscribe(
                    (resp: HttpResponse<IBreakdownRecoverResponseDTO>) => {
                        this.onSuccessRecoverBreakdown(resp.body, resp.headers, true);
                    },
                    (resp: HttpErrorResponse) => this.onError(resp.message)
                );
            }
            that.ngbModalRefClone.close();
            that.ngbModalRefClone = null;
            this.setDisableAll(true);
        });

        this.ngbModalRefClone.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefClone = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalRefClone = null;
            }
        );
    }

    recoverButtonOnClick() {
        const breakdownSearchFRequestDTO: IBreakdownSearchRequestDTO = new BreakdownSearchRequestDTO(this.breakdown.codCliente, Number(this.breakdown.codTipoDes),
            this.breakdown.codPlantilla, Number(this.breakdown.codSubtipoDes), this.breakdown.codMoneda);
        const conceptRequestDTO: IConceptRequestDTO = new ConceptRequestDTO(parseInt(this.breakdown.codPlantilla, 10));
        conceptRequestDTO.conceptTypeCodesIn = [ COD_TIPO_CONCEP_BALANCE, COD_TIPO_CONCEP_EST_RESULT, COD_TIPO_CONCEP_GEN_FONDOS, COD_TIPO_CONCEP_IND_FINAN ];
        conceptRequestDTO.orderByDirection = OrderByDirection.ASC;
        conceptRequestDTO.orderByFields = [ FIELD_NAME_COD_TIPO_CONCEP, FIELD_NAME_NUM_SECUENCIA ];
        conceptRequestDTO.columnLetterGlsFormulaVal1 = this.COLUMN_LETTER_GLS_FORMULA_VAL_1;
        conceptRequestDTO.columnLetterGlsFormulaVal2 = this.COLUMN_LETTER_GLS_FORMULA_VAL_2;
        conceptRequestDTO.columnLetterGlsFormulaVal3 = this.COLUMN_LETTER_GLS_FORMULA_VAL_3;
        conceptRequestDTO.columnLetterGlsFormulaVal4 = this.COLUMN_LETTER_GLS_FORMULA_VAL_4;
        conceptRequestDTO.columnLetterGlsFormulaPrc1 = this.COLUMN_LETTER_GLS_FORMULA_PRC_1;
        conceptRequestDTO.columnLetterGlsFormulaPrc2 = this.COLUMN_LETTER_GLS_FORMULA_PRC_2;
        conceptRequestDTO.columnLetterGlsFormulaPrc3 = this.COLUMN_LETTER_GLS_FORMULA_PRC_3;
        conceptRequestDTO.columnLetterGlsFormulaPrc4 = this.COLUMN_LETTER_GLS_FORMULA_PRC_4;
        conceptRequestDTO.columnLetterGlsFormulaDef1 = this.COLUMN_LETTER_GLS_FORMULA_DEF_1;
        conceptRequestDTO.columnLetterGlsFormulaDef2 = this.COLUMN_LETTER_GLS_FORMULA_DEF_2;
        conceptRequestDTO.columnLetterGlsFormulaDef3 = this.COLUMN_LETTER_GLS_FORMULA_DEF_3;
        conceptRequestDTO.columnLetterGlsFormulaDef4 = this.COLUMN_LETTER_GLS_FORMULA_DEF_4;
        conceptRequestDTO.flgConActivo = VALUE_IS_ACTIVE_CONCEPT_TRUE;
        this.conceptoService.findByTemplateConceptsCodesIn(conceptRequestDTO).subscribe(
            (res: HttpResponse<IBreakdownFilterConceptDto>) => {
                breakdownFilterConceptDto = res.body;
                conceptos = breakdownFilterConceptDto.conceptoDTOList;
                conceptosOriginal = breakdownFilterConceptDto.conceptoDTOList;
                if (breakdownFilterConceptDto.validationErrorDTOS.length === 0) {
                    if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
                        this.data = [];
                        this.data1 = [];
                        this.dataGlobal = [];
                        this.hiddenRows = [];
                        // let lastGroup = '';
                        let i = 0;
                        conceptos.forEach((concept, index) => {
                            if (isNullOrUndefined(concept.codTipoRegis) || concept.codTipoRegis === '' || concept.codConcepto=== ' ') { // Si es un encabezado de grupo
                                if (concept.codTipoRegis === '' || isNullOrUndefined(concept.codTipoRegis)) {
                                    // El encabezado se agrega uno solo en todos los períodos
                                    this.data.push([
                                        concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                        '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                        concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                        '', // G
                                        this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                        null, null, null, null, null, null, null, null, null, null, null, null
                                    ]);
                                    this.dataGlobal.push([
                                        concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                        '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                        concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                        '', // G
                                        this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                        null, null, null, null, null, null, null, null, null, null, null, null
                                    ]);
                                    if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                                        this.data1.push([
                                            concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                            '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                            concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                            '', //
                                            this.translateService.instant('breakdown.hotTable.' + concept.glsConcepto),
                                            null, null, null, null, null, null, null, null, null, null, null, null
                                        ]);
                                    }
                                } else {
                                    this.data.push([
                                        concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                        '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                        concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                        '', // G
                                        concept.glsConcepto,
                                        null, null, null, null, null, null, null, null, null, null, null, null
                                    ]);
                                    this.dataGlobal.push([
                                        concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                        '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                        concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                        '', // G
                                        concept.glsConcepto,
                                        null, null, null, null, null, null, null, null, null, null, null, null
                                    ]);
                                    if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                                        this.data1.push([
                                            concept.codTipoConcep, // 'COD_TIPO_CONCEP', // BALANCE, EST_RESULT, GEN_FONDOS, IND_FINAN
                                            '', // 'COD_TIPO_REGIS', // NORMAL, TITULO, MULTIREGISTRO
                                            concept.flgIsTotal, // 'FLG_IS_TOTAL', // Si es un total (no es editable) 1 sino 0
                                            '', //
                                            concept.glsConcepto,
                                            null, null, null, null, null, null, null, null, null, null, null, null
                                        ]);
                                    }
                                }
                            } else {
                                if (concept.flgIsTotal === 0) {
                                    this.data.push([
                                        concept.codTipoConcep,
                                        concept.codTipoRegis,
                                        concept.flgIsTotal,
                                        concept.codConcepto,
                                        concept.glsConcepto,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0
                                    ]);
                                    this.dataGlobal.push([
                                        concept.codTipoConcep,
                                        concept.codTipoRegis,
                                        concept.flgIsTotal,
                                        concept.codConcepto,
                                        concept.glsConcepto,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0,
                                        0
                                    ]);
                                } else {
                                    this.data.push([
                                        concept.codTipoConcep,
                                        concept.codTipoRegis,
                                        concept.flgIsTotal,
                                        concept.codConcepto,
                                        concept.glsConcepto,
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        ''
                                    ]);
                                    this.dataGlobal.push([
                                        concept.codTipoConcep,
                                        concept.codTipoRegis,
                                        concept.flgIsTotal,
                                        concept.codConcepto,
                                        concept.glsConcepto,
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        '',
                                        ''
                                    ]);
                                }
                                if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                                    if (concept.flgIsTotal === 0) {
                                        this.data1.push([
                                            concept.codTipoConcep,
                                            concept.codTipoRegis,
                                            concept.flgIsTotal,
                                            concept.codConcepto,
                                            concept.glsConcepto,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0,
                                            0
                                        ]);
                                    } else {
                                        this.data1.push([
                                            concept.codTipoConcep,
                                            concept.codTipoRegis,
                                            concept.flgIsTotal,
                                            concept.codConcepto,
                                            concept.glsConcepto,
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            '',
                                            ''
                                        ]);
                                    }
                                }
                            }
                            // update hidden columns...
                            if (concept.codTipoConcep !== COD_TIPO_CONCEP_EST_RESULT && concept.codTipoConcep !== COD_TIPO_CONCEP_BALANCE) {
                                this.hiddenRows.push(i);
                            }
                            if (concept.codTipoConcep !== COD_TIPO_CONCEP_IND_FINAN && concept.codTipoConcep !== COD_TIPO_CONCEP_GEN_FONDOS) {
                                this.hiddenRows1.push(i);
                            }
                            i = i + 1;
                        });
                        conceptos.forEach((concept, index) => {
                            diccionario.set(concept.codTipoConcep + '.'+concept.codConcepto,index+1);
                        });
                    } else {
                        this.data = [[]];
                        this.data1 = [[]];
                        this.dataGlobal = [[]];
                    }
                    // Llamamos al servicio de recuperar todos los datos del desglose
                    this.desgloseService.recover(breakdownSearchFRequestDTO).subscribe(
                        (resp: HttpResponse<IBreakdownRecoverResponseDTO>) => this.onSuccessRecoverBreakdown(resp.body, resp.headers, false),
                        (resp: HttpErrorResponse) => this.onError(resp.message)
                    );
                } else {
                    this.displayValidationErrors(breakdownFilterConceptDto.validationErrorDTOS, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_SAVE);
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private onSuccessRecoverBreakdown(body: IBreakdownRecoverResponseDTO, headers: HttpHeaders, isCloneAction: boolean) {
        this.isRecover = true;
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        this.breakdownRecoverResponseDTO = body;
        conceptosEliminar = [];
        if (isCloneAction) {
            this.breakdown.codTipoDes = this.breakdownRecoverResponseDTO.codTipoDes;
            this.breakdown.codSubtipoDes = this.breakdownRecoverResponseDTO.codSubtipoDes;
            this.breakdown.glsSubtipoDes = this.breakdownRecoverResponseDTO.glsSubtipoDes;
            this.breakdown.codMoneda = this.breakdownRecoverResponseDTO.codMoneda;
            this.breakdown.codEstado = this.breakdownRecoverResponseDTO.codEstado;
            this.breakdown.glsEstado = this.breakdownRecoverResponseDTO.glsEstado;
        }
        this.contMultiregistro = 0;
        this.breakdown.glsSubtipoDes = this.breakdownRecoverResponseDTO.glsSubtipoDes;
        this.breakdown.nomUserOpen =  this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen;
        this.isEdicion = false;
        this.isButtonPreviousDisabled = true;
        this.isButtonNextDisabled = true;
        this.nextPeriodIndex = 0;
        this.activePeriodsIndexes = [ 0, 1, 2, 3 ];
        this.activePeriodsEdit = [];
        this.buttonEdit1.nativeElement.disabled = false;
        this.buttonEdit2.nativeElement.disabled = false;
        this.buttonEdit3.nativeElement.disabled = false;
        this.buttonEdit4.nativeElement.disabled = false;
        if (this.breakdownRecoverResponseDTO && this.breakdownRecoverResponseDTO.extraDataDTO) {
            this.controlBotones(body);
            this.extraDataDesglose = body.extraDataDTO;
            this.isDesgloseMensajes();
            this.isDesgloseEnRevision();
            desglose = this.breakdown;
            if (!this.extraDataDesglose.monthsPeriod) {
                this.extraDataDesglose.monthsPeriod = [];
            }
            if (!this.extraDataDesglose.deflatorPeriod) {
                this.extraDataDesglose.deflatorPeriod = [];
            }
            if (!this.extraDataDesglose.deflatorPeriodStr) {
                this.extraDataDesglose.deflatorPeriodStr = [];
            }
            if (!this.extraDataDesglose.periodRating) {
                this.extraDataDesglose.periodRating = [];
            }
            if (!this.extraDataDesglose.fecPeriodo) {
                this.extraDataDesglose.fecPeriodo = [];
            }
            if (!this.extraDataDesglose.matrizConImp) {
                this.extraDataDesglose.matrizConImp = [];
            }
            if (!this.extraDataDesglose.periodos) {
                this.extraDataDesglose.periodos = [];
            } else {
                if (hotInstance) {
                    setTimeout(() => {
                        this.recargarActiveConceptsPeriod(0, null);
                        this.asignMultiregistro(this.extraDataDesglose);
                        this.recargarDesactiveConcepts();
                        if (this.breakdownRecoverResponseDTO.extraDataDTO.periodos.length <= 4) {
                            this.extraDataDesglose.periodos.forEach((period, index) => {
                                this.assignValPrdToTable(period.valPeriodoDesgloseDTOList, index);
                            });
                        } else {
                                this.extraDataDesglose.periodos.forEach((period, index) => {
                                    if(index < 4) {
                                        this.assignValPrdToTable(period.valPeriodoDesgloseDTOList, index);
                                    }
                                });
                        }
                        if (this.breakdownRecoverResponseDTO.extraDataDTO.periodos.length > 4) {
                            for (let i = 0; i < (this.breakdownRecoverResponseDTO.extraDataDTO.periodos.length); i++) {
                                if (i <= (this.breakdownRecoverResponseDTO.extraDataDTO.periodos.length - 4) - 2) {
                                    // this.nextPeriodIndex = this.nextPeriodIndex + 1;
                                    this.executeNextPeriodAction();
                                }
                            }
                            this.executeNextPeriodAction();
                        }
                        if (isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ])) {
                            this.isButtonNextDisabled = true;
                        } else {
                            this.isButtonNextDisabled = false;
                        }
                        this.recargarActiveConcepts();
                        setTimeout(() => {
                            this.columns = initColumnas(true);
                            setTimeout(() => {
                                hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                                setTimeout(() => {
                                    hotInstance.selectCell(this.data1.length-1, COLUMN_NUMBER_CCC);
                                    setTimeout(() => {
                                        hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                                        hotInstance.getPlugin('formulas').recalculateFull();
                                        hotInstance.getPlugin('hiddenRows').hideRows(this.hiddenMultiregistro);
                                        hotInstance.render();
                                        this.beforeChangeClick();
                                        const typesParam = this.CODTABLA_ESTADO_DESGLOSE;
                                        jQuery('#project1').hide();
                                        jQuery('#project2').hide();
                                        jQuery('#project3').hide();
                                        jQuery('#project4').hide();
                                        this.descripcionCodigoService.findByCodTabla(typesParam, this.sort).subscribe(
                                            (res: HttpResponse<IDescripcionCodigo[]>) => {
                                                if ((this.breakdown.codPlantilla === COD_TIPO_DESG_BASE_NUEVA) && (this.breakdown.codTipoDes !== COD_TIPO_DESG_CONSOLIDADO)) {
                                                    res.body.forEach((item, index) => {
                                                        if(!isNullOrUndefined(item) && !isNullOrUndefined(item.codTabla)) {
                                                            if (item.codTabla === CODTABLA_ESTADO_DESGLOSE && item.codGenerico === COD_GENERICO_PROYECTAR) {
                                                                if (item.codRegStatus === 1) {
                                                                    jQuery('#project1').hide();
                                                                    jQuery('#project2').hide();
                                                                    jQuery('#project3').hide();
                                                                    jQuery('#project4').hide();
                                                                } else {
                                                                    jQuery('#project1').show();
                                                                    jQuery('#project2').show();
                                                                    jQuery('#project3').show();
                                                                    jQuery('#project4').show();
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            },
                                            (res: HttpErrorResponse) => this.onError(res.message)
                                        );
                                    }, 50);
                                }, 50);
                            }, 50);
                        }, 10);
                    }, 200);
                }
            }
            if (!this.extraDataDesglose.conceptos) {
                this.extraDataDesglose.conceptos = [];
            }
            if (!this.extraDataDesglose.empresaConsolidadas) {
                this.extraDataDesglose.empresaConsolidadas = [];
            }
            if (!this.extraDataDesglose.periodoDesgConsols) {
                this.extraDataDesglose.periodoDesgConsols = [];
            }
            if (!this.extraDataDesglose.empConsolPeriodoDesgConsolList) {
                this.extraDataDesglose.empConsolPeriodoDesgConsolList = [];
            }
            if (!this.extraDataDesglose.auditors) {
                this.extraDataDesglose.auditors = [];
            } else {
                for (let i = 0; i < this.extraDataDesglose.auditors.length; i++) {
                    this.auditorNames [ i ] = !isNullOrUndefined(this.extraDataDesglose.auditors[ i ].otherAuditorName) ? this.extraDataDesglose.auditors[ i ].otherAuditorName :
                        (!isNullOrUndefined(this.extraDataDesglose.auditors[ i ].auditor) ? this.extraDataDesglose.auditors[ i ].auditor.glsGenericalarga : null);
                }
            }
            this.isBlockPeriodsDataDisabled = true;
            this.colHeadersConfig();
            this.controlEstadoDesglose();
            if (this.extraDataDesglose.periodos.length >= 4) {
                this.extraDataDesglose.numPeriodosLoaded = 4;
            } else {
                this.extraDataDesglose.numPeriodosLoaded = this.extraDataDesglose.periodos.length;
            }
            this.indicadorNotificaciones();
        } else {
            this.btnEdit1.disabled = false;
            this.btnEdit2.disabled = false;
            this.btnEdit3.disabled = false;
            this.btnEdit4.disabled = false;
        }
        this.hasPermissionToClone();
    }

    private asignMultiregistro(extraDataDesglose: IExtraDataDesglose) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        let number = 0;
        let numberTotal = 100;
        let cuentaMultiregistros = 0;
        let posicion = 0;
        conceptos.filter(concepto => ((concepto.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concepto.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)))
        .forEach((concept, index) => {
            if (concept.codTipoRegis === MULTI_REGISTRY) {
                this.extraDataDesglose.periodos.forEach(period => {
                    const values = period.valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                        && v.codPlantilla === concept.codTipoPlantilla);
                    for(let i = 0; i< values.length; i++) {
                        if(values[i].corValor > number) {
                            number = values[i].corValor;
                        }
                    }
                });
                numberTotal = number;
                if (numberTotal > 1) {
                    let contador = 1;
                    for(let i = posicion; i<(posicion+ numberTotal);i++) {
                        if(contador > number) {
                            this.hiddenMultiregistro.push(i);
                        }
                        contador++;
                    }
                    hotInstance.getPlugin('formulas').disablePlugin();
                    hotInstance1.getPlugin('formulas').disablePlugin();
                    hotInstance.alter('insert_row', posicion, numberTotal-1 );
                    hotInstance1.alter('insert_row', posicion, numberTotal-1 );
                }
                let actualizarIndices = 0;
                diccionario.forEach((item, index1) => {
                    if(index1 === concept.codTipoConcep + '.'+ concept.codConcepto) {
                        actualizarIndices = 1;
                    }
                    if(actualizarIndices === 1) {
                        if (numberTotal > 1) {
                            diccionario.set(index1, item + (numberTotal - 1));
                        }
                    }
                });
                concept.corValor = number;
                for (let i = 1; i < numberTotal ; i++) {
                    cuentaMultiregistros++;
                    this.contMultiregistro++;
                    const newConcept: IConcepto = new Concepto(
                        concept.codTipoPlantilla, concept.codTipoConcep, concept.corConcepto, concept.codConcepto, '',
                        concept.numSecuencia, concept.numLvlIndent, concept.codTipoRegis,concept.glsFormulaValOriginal,concept.glsFormulaVal, concept.glsFormulaVal2,
                        concept.glsFormulaVal3, concept.glsFormulaVal4, concept.glsFormulaPrcOriginal,concept.glsFormulaPrc, concept.glsFormulaPrc2, concept.glsFormulaPrc3,
                        concept.glsFormulaPrc4, concept.glsFormulaDefOriginal,concept.glsFormulaDef, concept.glsFormulaDef2, concept.glsFormulaDef3, concept.glsFormulaDef4,
                        concept.flgIsTotal, concept.flgUserModif, concept.flgConActivo, concept.numDecimales, concept.flgHaceAjust,
                        concept.flgIsBold, concept.flgIsUcase, concept.flgIsEnclosed, concept.flgIsPrinted, concept.numDecPrint,
                        concept.flgNonegativ, concept.flgAfterspace, concept.concepto, i, concept.newValue,
                        concept.newFormula, concept.newGlosa, concept.newPorcent, concept.expValidacionVal, concept.expValidacionPrc,
                        concept.expValidacionDef, concept.msgErrValidacionVal, concept.msgErrValidacionPrc, concept.msgErrValidacionDef,
                        concept.unidad, concept.updateFormula);
                    conceptos.splice(posicion, 0, newConcept);
                    this.data[ posicion ] = [
                        concept.codTipoConcep,
                        concept.codTipoRegis,
                        concept.flgIsTotal,
                        concept.codConcepto,
                        concept.glsConcepto,
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0'
                    ];
                    this.dataGlobal.splice(posicion, 0, [
                        concept.codTipoConcep,
                        concept.codTipoRegis,
                        concept.flgIsTotal,
                        concept.codConcepto,
                        concept.glsConcepto,
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0'
                    ]);
                    this.data1[ posicion ] = [
                        concept.codTipoConcep,
                        concept.codTipoRegis,
                        concept.flgIsTotal,
                        concept.codConcepto,
                        concept.glsConcepto,
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0',
                        '0'
                    ];
                    posicion++;
                }
                number = 1;
            }
            posicion++;
        });
        const datosMaestros = hotInstance.getSourceData();
        const datosMaestros1 = hotInstance1.getSourceData();
        if (cuentaMultiregistros > 0) {
            hotUpdateSettings(hotInstance);
            hotUpdateSettings(hotInstance1);
            this.columns = initColumnas(true);
            // updateConceptos(datosMaestros, datosMaestros1);
            hotInstance.getPlugin('formulas').enablePlugin ();
            hotInstance1.getPlugin('formulas').enablePlugin ();
        }
        updateConceptosV2(hotInstance,hotInstance1,0,true);
        updateConceptosV2(hotInstance,hotInstance1,1,true);
        updateConceptosV2(hotInstance,hotInstance1,2,true);
        updateConceptosV2(hotInstance,hotInstance1,3,true);
    }

    private limpiarDatosMultiregistro(extraDataDesglose: IExtraDataDesglose) {
        let number = 0;
        let cuentaMultiregistros = 0;
        let posicion = 0;
        conceptos.filter(concepto => ((concepto.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concepto.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)))
        .forEach((concept, index) => {
            if (concept.codTipoRegis === MULTI_REGISTRY) {
                this.extraDataDesglose.periodos.forEach((period, indice) => {
                    const values = period.valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                        && v.codPlantilla === concept.codTipoPlantilla);
                    if (values.length > number) {
                        number = values.length;
                    }
                });
                for (let i = 0; i < number; i++) {
                    cuentaMultiregistros++;
                    if (!isNullOrUndefined(this.data[ posicion ])) {
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] ] = '0';
                        this.data[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ] ] = '0';
                    }
                    if (!isNullOrUndefined(this.data1[ posicion ])) {
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] ] = '0';
                        this.data1[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ] ] = '0';
                        // posicion++;
                    }
                    if (!isNullOrUndefined(this.dataGlobal[ posicion ])) {
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] ] = '0';
                        this.dataGlobal[ posicion ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ] ] = '0';
                        // posicion++;
                    }
                }
                number = 0;
            }
            posicion++;
            number = 0;
        });
    }

    private assignValPrdToTable(valPeriodoDesgloseDTOList: IValPeriodoDesglose[], periodNumber: number) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        if (!isNullOrUndefined(valPeriodoDesgloseDTOList) && valPeriodoDesgloseDTOList.length > 0) {
            this.recorreConceptosAsignaData(valPeriodoDesgloseDTOList, periodNumber);
            this.recorreConceptosAsignGeneracionFondosIndicadoresFinancieros(valPeriodoDesgloseDTOList, periodNumber);
        } else {
            for (let k = 0; k < conceptos.length; k++) {
                const concept = conceptos[ k ];
                if ((concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)) {
                    if (!isNullOrUndefined(concept.codTipoRegis)) {
                        if(!isNullOrUndefined(this.data[ k ])) {
                            this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = 0;
                        }
                        if(!isNullOrUndefined(this.data1[ k ])) {
                            this.data1[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = 0;
                        }
                    }
                }
                if(!isNullOrUndefined(concept.codTipoRegis)) {
                    if(!isNullOrUndefined(this.dataGlobal[ k ])) {
                        this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = 0;
                    }
                }
            }
        }
    }

    private recorreConceptosAsignaData(valPeriodoDesgloseDTOList: IValPeriodoDesglose[], periodNumber: number) {
        let k = 0;
        for (; k < conceptos.length; k++) {
            let setToNull = null;
            const concept = conceptos[ k ];
            if ((concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)) {
                // todo: comento para inyectar todos los valores
                if (!isNullOrUndefined(valPeriodoDesgloseDTOList)) {
                    const values = valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                        && v.codPlantilla === concept.codTipoPlantilla);
                    if (!isNullOrUndefined(values) && values.length > 0 && !isNullOrUndefined(values[ 0 ])) {
                        if (concept.codTipoRegis !== MULTI_REGISTRY && concept.codConcepto!== ' ') {
                            if(!isNullOrUndefined(this.data[ k ])) {
                                this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valConcepto) ? values[ 0 ].valConcepto : 0;
                                this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valPrcConcep) ? values[ 0 ].valPrcConcep : 0;
                            }
                            if(!isNullOrUndefined(this.dataGlobal[ k ])) {
                                this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valConcepto) ? values[ 0 ].valConcepto : 0;
                                this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valPrcConcep) ? values[ 0 ].valPrcConcep : 0;
                            }
                            if(!isNullOrUndefined(this.data1[ k ])) {
                                this.data1[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valConcepto) ? values[ 0 ].valConcepto : 0;
                                this.data1[ k ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valPrcConcep) ? values[ 0 ].valPrcConcep : 0;
                            }
                            if (concept.flgIsTotal === 0) {
                                let formula = values[ 0 ].glsFrmlConcep;
                                if (!isNullOrUndefined(formula) && !formula.toString().includes('=')) {
                                    formula = '=' + formula;
                                }
                                if (!isNullOrUndefined(formula) && formula.toString() !== '' && formula.toString().includes('=')) {
                                    this.data1[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = formula;
                                   /* switch (periodNumber) {
                                        case 0: {
                                            concept.glsFormulaVal = formula;
                                            break;
                                        }
                                        case 1: {
                                            concept.glsFormulaVal2 = formula;
                                            break;
                                        }
                                        case 2: {
                                            concept.glsFormulaVal3 = formula;
                                            break;
                                        }
                                        case 3: {
                                            concept.glsFormulaVal4 = formula;
                                            break;
                                        }
                                    }*/
                                }
                            }
                        }
                        if (concept.codTipoRegis === MULTI_REGISTRY) {
                            for (let i = 0; i < values.length; i++) {
                                if (this.data[ k - 1 ][ COLUMN_NUMBER_COD_TIPO_REGIS ] !== MULTI_REGISTRY) {
                                    if (this.data[ k + i ][ COLUMN_NUMBER_COD_TIPO_REGIS ] === MULTI_REGISTRY) {
                                        if(!isNullOrUndefined(values[ i ].corValor)) {
                                            if(!isNullOrUndefined(this.data[  k + values[ i ].corValor -1])) {
                                                this.data[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = !isNullOrUndefined(values[ i ].valConcepto) ? values[ i ].valConcepto : 0;
                                                this.data[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = !isNullOrUndefined(values[ i ].valPrcConcep) ? values[ i ].valPrcConcep : 0;
                                                this.data[ k + values[ i ].corValor -1][ COLUMN_NUMBER_CCC ] = values[ i ].glsConcepto;
                                            }
                                            if(!isNullOrUndefined(this.dataGlobal[  k + values[ i ].corValor ])) {
                                                this.dataGlobal[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = !isNullOrUndefined(values[ i ].valConcepto) ? values[ i ].valConcepto : 0;
                                                this.dataGlobal[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = !isNullOrUndefined(values[ i ].valPrcConcep) ? values[ i ].valPrcConcep : 0;
                                                this.dataGlobal[ k + values[ i ].corValor -1][ COLUMN_NUMBER_CCC ] = values[ i ].glsConcepto;
                                            }
                                            if(!isNullOrUndefined(this.data1[  k + values[ i ].corValor ])) {
                                                this.data1[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = !isNullOrUndefined(values[ i ].valConcepto) ? values[ i ].valConcepto : 0;
                                                this.data1[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = !isNullOrUndefined(values[ i ].valPrcConcep) ? values[ i ].valPrcConcep : 0;
                                                this.data1[ k + values[ i ].corValor -1][ COLUMN_NUMBER_CCC ] = values[ i ].glsConcepto;
                                            }
                                        }
                                        if (!concept.flgIsTotal) {
                                            let formula = values[ i ].glsFrmlConcep;
                                            if (!isNullOrUndefined(formula) && !formula.toString().includes('=')) {
                                                formula = '=' + formula;
                                            }
                                            if (!isNullOrUndefined(formula) && formula.toString() !== '' && formula.toString().includes('=')) {
                                                if(!isNullOrUndefined(values[ i ].corValor)) {
                                                    this.data1[ k + values[ i ].corValor -1][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] =formula;
                                                    // this.data1[ k + values[ i ].corValor -1 ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = formula;
                                                    /*switch (periodNumber) {
                                                        case 0: {
                                                            conceptos[  k + values[ i ].corValor -1 ].glsFormulaVal = formula;
                                                            break;
                                                        }
                                                        case 1: {
                                                            conceptos[  k + values[ i ].corValor -1 ].glsFormulaVal2 = formula;
                                                            break;
                                                        }
                                                        case 2: {
                                                            conceptos[  k + values[ i ].corValor -1 ].glsFormulaVal3 = formula;
                                                            break;
                                                        }
                                                        case 3: {
                                                            conceptos[  k + values[ i ].corValor -1 ].glsFormulaVal4 = formula;
                                                            break;
                                                        }
                                                    }*/
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        setToNull = true;
                    }
                } else {

                }
                if (setToNull) {
                    if (!isNullOrUndefined(concept.codTipoRegis) && concept.codConcepto!== ' ') {
                        if(!isNullOrUndefined(this.data[ k ])) {
                            this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = 0;
                            this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = 0;
                        }
                        if(!isNullOrUndefined(this.dataGlobal[ k ])) {
                            this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = 0;
                            this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = 0;
                        }
                        if(!isNullOrUndefined(this.data1[ k ])) {
                            this.data1[ k ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ periodNumber ] ] = 0;
                            this.data1[ k ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ periodNumber ] ] = 0;
                        }
                    }
                }
            }
        }
    }

    private recorreConceptosAsignGeneracionFondosIndicadoresFinancieros(valPeriodoDesgloseDTOList: IValPeriodoDesglose[], periodNumber: number) {
        let k = 0;
        for (; k < conceptos.length; k++) {
            let setToNull = null;
            const concept = conceptos[ k ];
            if ((concept.codTipoConcep === COD_TIPO_CONCEP_GEN_FONDOS) || (concept.codTipoConcep === COD_TIPO_CONCEP_IND_FINAN)) {
                if (!isNullOrUndefined(valPeriodoDesgloseDTOList)) {
                    const values = valPeriodoDesgloseDTOList.filter(v => v.codTipoConcep === concept.codTipoConcep && v.corConcepto === concept.corConcepto
                        && v.codPlantilla === concept.codTipoPlantilla);
                    if (!isNullOrUndefined(values) && values.length > 0 && !isNullOrUndefined(values[ 0 ])) {
                        if (!isNullOrUndefined(this.dataGlobal[ k ])) {
                            if (!isNullOrUndefined(this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ periodNumber ] ])) {
                                this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valConcepto) ? values[ 0 ].valConcepto : 0;
                                this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ periodNumber ] ] = !isNullOrUndefined(values[ 0 ].valConcepto) ? values[ 0 ].valConcepto : 0;
                            }
                        }
                    } else {
                        setToNull = true;
                    }
                } else {

                }
                if (setToNull) {
                    if (!isNullOrUndefined(concept.codTipoRegis)) {
                        if (!isNullOrUndefined(this.dataGlobal[ k ])) {
                            if (!isNullOrUndefined(this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ periodNumber ] ])) {
                                this.dataGlobal[ k ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ periodNumber ] ] = 0;
                                this.data[ k ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ periodNumber ] ] = 0;
                            }
                        }
                    }
                }
            }
        }
    }

    private controlBotones(body: IBreakdownRecoverResponseDTO) {
        this.buttonSave.nativeElement.disabled = true;
        this.buttonProcess.nativeElement.disabled = true;
        this.disableElementsByIds([ 'buttonInProcess', 'field_breakdownTemplateType', 'field_breakdownType', 'field_breakdownSubType',
            'field_breakdownSubTypeDesc', 'field_currency' ], true);
        this.disableElementsByIds([ 'buttonRecover', 'buttonGeneratePrint', 'buttonPrint', 'buttonClone', 'buttonDelete' ], false);
        this.btnEdit1.disabled = true;
        this.btnEdit2.disabled = true;
        this.btnEdit3.disabled = true;
        this.btnEdit4.disabled = true;
        if (this.breakdownRecoverResponseDTO.extraDataDTO.isTemporal || (this.breakdownRecoverResponseDTO.extraDataDTO.isAutoriced && this.breakdownRecoverResponseDTO.extraDataDTO.userOpen === '')) {
            this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningBreakdown', '');
            return;
        } else if (!this.breakdownRecoverResponseDTO.extraDataDTO.isAutoriced && this.breakdownRecoverResponseDTO.extraDataDTO.userOpen !== this.usuario.loginName && this.breakdownRecoverResponseDTO.extraDataDTO.enProceso) {
            this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningInProcess', { usuario: body.extraDataDTO.userOpen });
        } else if (!this.breakdownRecoverResponseDTO.extraDataDTO.isAutoriced && this.breakdownRecoverResponseDTO.extraDataDTO.userOpen !== this.usuario.loginName && !this.breakdownRecoverResponseDTO.extraDataDTO.enProceso) {
            this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningInUse', { usuario: body.extraDataDTO.userOpen });
        }
        this.desactivarButonRecuperar(this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen);
        this.desactivarButonEliminar(this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen);
        if (!isNullOrUndefined(this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen)) {
            if (this.usuario.loginName !== this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen) {
            } else {
                this.btnEdit1.disabled = false;
                this.btnEdit2.disabled = false;
                this.btnEdit3.disabled = false;
                this.btnEdit4.disabled = false;
                if (!isNullOrUndefined(this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen) && this.breakdownRecoverResponseDTO.extraDataDTO.nomUserOpen === this.usuario.loginName) {
                    if (this.breakdownRecoverResponseDTO.codEstado === ESTADO_BORRADOR_EN_PROCESO || this.breakdownRecoverResponseDTO.codEstado === ESTADO_EN_EDICION) {
                        this.buttonProcess.nativeElement.disabled = false;
                        this.buttonSave.nativeElement.disabled = false;
                    }
                }
            }
        } else {
            this.btnEdit1.disabled = false;
            this.btnEdit2.disabled = false;
            this.btnEdit3.disabled = false;
            this.btnEdit4.disabled = false;
        }
    }

    private controlEstadoDesglose() {
        if (!isNullOrUndefined(this.extraDataDesglose.nomUserOpen)) {
            if (this.extraDataDesglose.nomUserOpen !== this.usuario.loginName) {
                this.state.state = this.translateService.instant('breakdown.state.busy');
                this.state.stateClass = 'danger';
                this.turbineUserService.findLoginName(this.extraDataDesglose.nomUserOpen).subscribe(
                    (data: HttpResponse<ITurbineUser>) => {
                        this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningOccupied', { usuario: data.body.firstName + ' ' + data.body.lastName });
                    },
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            } else {
                this.state.state = this.translateService.instant('breakdown.state.free');
                this.state.stateClass = 'success';
            }
        } else {
            this.state.state = this.translateService.instant('breakdown.state.free');
            this.state.stateClass = 'success';
        }
    }

    private async isDesgloseEnRevision() {
        this.desgloseRevisionService.find(this.breakdown.codCliente, Number(this.breakdown.codTipoDes),
            Number(this.breakdown.codSubtipoDes), this.breakdown.codMoneda, Number(this.breakdown.codPlantilla)).subscribe(
            async (res: HttpResponse<IDesgloseRevision[]>) => {
                const DESGLOSANDO: IDesgloseRevision[] = await res.body;
                if (!isNullOrUndefined(DESGLOSANDO)) {
                    if (DESGLOSANDO.length > 0) {
                        let DESGLO: IDesgloseRevision = null;
                        let DESGLO1: IDesgloseRevision = null;
                        DESGLOSANDO.forEach((item, index) => {
                            if (item.codRegStatus === COD_REG_STATUS_ENVIA_REE) {
                                DESGLO = item;
                                this.roleEnviaRevision = Number(DESGLO.rolDigitador);
                            }
                            if (item.codRegStatus === COD_REG_STATUS_VIGENTE_REE) {
                                if (item.rolRevisor === this.operatorCode.toString(10)) {
                                    DESGLO1 = item;
                                }
                            }
                        });
                        if (!isNullOrUndefined(DESGLO)) {
                            if (DESGLO.codRegStatus === COD_REG_STATUS_ENVIA_REE) {
                                if (DESGLO.rolRevisor === this.operatorCode.toString(10) || (!isNullOrUndefined(DESGLO.rolRevisorReem) && DESGLO.rolRevisorReem === this.operatorCode.toString(10))) {
                                    this.gDesgloseEnRevision = true;
                                    if (this.role === this.USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === this.USER_ROLE_PLAT_DESGLOSE_JEFE) {
                                        if (this.breakdown.codEstado === ESTADO_BORRADOR_TERMINADO) {
                                            this.buttonApproveBreakdown.nativeElement.disabled = false;
                                            this.buttonSendToExecutor.nativeElement.disabled = false;
                                        }
                                        if (this.breakdown.codEstado === ESTADO_BORRADOR_VALIDADO && this.extraDataDesglose.nomUserOpen === this.usuario.loginName) {
                                            this.buttonSendToReview.nativeElement.disabled = false;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (this.breakdown.flgReq2firma) {
                                if ((this.role === this.USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === this.USER_ROLE_PLAT_DESGLOSE_JEFE || this.role === this.USER_ROLE_PLAT_DESGLOSE_EJECUTOR) /*&& this.extraDataDesglose.nomUserOpen === this.usuario.loginName*/) {
                                    if (this.breakdown.codEstado === ESTADO_BORRADOR_VALIDADO) {
                                        this.buttonSendToReview.nativeElement.disabled = false;
                                    }
                                }
                            } else {
                                if (!isNullOrUndefined(DESGLO1)) {
                                    if ((this.role === this.USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === this.USER_ROLE_PLAT_DESGLOSE_JEFE) /*this.extraDataDesglose.nomUserOpen === this.usuario.loginName*/ && DESGLO1.rolRevisor === this.operatorCode.toString(10)) {
                                        if (this.breakdown.codEstado === ESTADO_BORRADOR_VALIDADO) {
                                            this.buttonApproveBreakdown.nativeElement.disabled = false;
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        if (this.role === this.USER_ROLE_PLAT_DESGLOSE_REVISOR || this.role === this.USER_ROLE_PLAT_DESGLOSE_JEFE || this.role === this.USER_ROLE_PLAT_DESGLOSE_EJECUTOR) {
                            if (this.breakdown.codEstado === ESTADO_BORRADOR_VALIDADO) {
                                this.buttonSendToReview.nativeElement.disabled = false;
                            }
                        }
                    }
                }
            },
            (res: HttpErrorResponse) => {
            }
        );
    }

    private async isDesgloseMensajes() {
        this.desgloseMensajeService.find(this.breakdown.codCliente, Number(this.breakdown.codTipoDes),
            Number(this.breakdown.codSubtipoDes), this.breakdown.codMoneda, Number(this.breakdown.codPlantilla)).subscribe(
            async (res: HttpResponse<IDesgloseMensaje[]>) => {
                const MESSAGES: IDesgloseMensaje[] = await res.body;
                if (MESSAGES.length > 0) {
                    this.isHiddenMensajes = false;
                    this.mensaje = MESSAGES;
                    const btn: HTMLButtonElement = <HTMLButtonElement>document.getElementById('button_verMensaje');
                    btn.disabled = false;
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    /**
     * Método ejecutado al hacer clic en el botón 'Generar impreso'. Genera los datos en la pestaña de impresión.
     */
    onClickGeneratePrint() {
        // TODO: falta agregar validacion de impresion al momento de marcar el check del dialogo
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
        this.extraDataDesglose.numPeriodosLoaded = !isNullOrUndefined(this.extraDataDesglose.periodos) ? this.extraDataDesglose.periodos.length : 0;
        this.extraDataDesglose.canValidate = true;
        this.extraDataDesglose.conceptos = conceptos.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE || el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
        this.extraDataDesglose.data = this.data1;
        this.extraDataDesglose.visualData = visualData;
        this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes).subscribe(
            (respuesta: HttpResponse<IBreakdownValidationError[]>) => {
                if (respuesta.body.length > 0) {
                    const optionalQuestionMessage = 'breakdown.errors.questionMessageGeneratePrintData';
                    this.displayValidationErrors(respuesta.body, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_GENERATE_PRINT, optionalQuestionMessage);
                } else {
                    this.generatePrintData();
                }
            },
            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
        );
    }

    onClicktoProject(peridoOption: number) {
         const that = this;
        this.ngbModalProject = this.modalService.open(ProjectionsComponent as Component, {
            windowClass: 'generate-dialogo',
            backdrop: 'static'
        });
        this.ngbModalProject.componentInstance.codCliente = that.breakdown.codCliente;
        this.ngbModalProject.componentInstance.nameCliente = that.client.name;
        this.ngbModalProject.componentInstance.codTipoDesglose = that.breakdown.codTipoDes;
        this.ngbModalProject.componentInstance.codSubtipoDesglose = that.breakdown.codSubtipoDes;
        this.ngbModalProject.componentInstance.codMoneda = that.breakdown.codMoneda;
        this.ngbModalProject.componentInstance.codPlantilla = that.breakdown.codPlantilla;
        this.ngbModalProject.componentInstance.codPlantillaProyect = '50000';
        this.ngbModalProject.componentInstance.fecPeriodo = this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ peridoOption ] ];
        this.ngbModalProject.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalProject = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalProject = null;
            }
        );

    }
    private getAbsolutePath() {
        const loc = window.location;
        const pathName = loc.pathname.substring(0, loc.pathname.lastIndexOf('/') + 1);
        return loc.href.substring(0, loc.href.length - ((loc.pathname + loc.search + loc.hash).length - pathName.length));
    }
    private generarEstructuraMatrizConImp() {
        conceptos.forEach((concep, index) => {
            this.extraDataDesglose.matrizConImp.forEach((matrix, i) => {
                if (!isNullOrUndefined(concep.codTipoConcep) && !isNullOrUndefined(concep.corConcepto)) {
                    if (concep.codTipoConcep === matrix.codTipoConcep && concep.corConcepto.toString() === matrix.corConcepto.toString()) {
                        matrix.codConcepto = concep.codConcepto;
                    }
                }
            });
        });
    }

    /**
     * Genera los datos de impresión.
     */
    private generatePrintData() {
        const that = this;
        const HIDDEN_ROWS: number[] = [];
        const CODE_MONEDA = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_MONEDAS);
        const CODE_UNIDAD_MONETARIA = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_UNIDAD_MONETARIA);
        const CODE_TIPO_PLANTILLA_DESGLOSE = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_PLANTILLAS_DESGLOSE);
        const CODE_TIPO_DESGLOSE = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_TIPO_DESGLOSE);
        const CODE_SUBTIPO_DESGLOSE = this.filterDescripcionCodigoPipe.transform(this.simpleComboboxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        const DATE = new Date();
        if (CODE_MONEDA && CODE_MONEDA.length > 0 && !isNullOrUndefined(this.breakdown.codMoneda)) {
            const descripcionCodigo = this.validationUtils.getDescripcionCodigoByCodGenerico(CODE_MONEDA, this.breakdown.codMoneda);
            this.ngbModalRefGeneratePrint = this.modalService.open(BreakdownGeneratePrintDialogComponent as Component, {
                windowClass: 'generate-print-dialog',
                backdrop: 'static'
            });
            this.onSuccessPressGeneratePrint();
            this.ngbModalRefGeneratePrint.componentInstance.descripMoneda = descripcionCodigo;
            this.ngbModalRefGeneratePrint.componentInstance.codMoneda = that.breakdown.codMoneda;
            this.ngbModalRefGeneratePrint.componentInstance.fecha = that.breakdown.fecDeflacBase;
            this.ngbModalRefGeneratePrint.componentInstance.deflactorBase = that.breakdown.valDeflacBase;
            this.ngbModalRefGeneratePrint.componentInstance.deflactorBaseT = that.breakdown.valDeflacBase;
            this.ngbModalRefGeneratePrint.componentInstance.fechaPeriodo = that.extraDataDesglose.fecPeriodo;
            this.ngbModalRefGeneratePrint.componentInstance.listDeflatorPeriod = that.extraDataDesglose.deflatorPeriod;
            this.ngbModalRefGeneratePrint.componentInstance.indicesPeriod = that.activePeriodsIndexes;
            this.ngbModalRefGeneratePrint.componentInstance.triggerOnSelectItemEventEmitter.subscribe(data => {
                const hotInstance = this.hotRegisterer.getInstance(this.handOnTableImpreInstanceId);
                this.loaderService.isLoading.next(true);
                this.datosConfImpresion = data;
                setTimeout(() => {
                this.colHeadersConfig1();
                let actividadSelected = '';
                const MONEDA_IMPRESO: string = data.monedaImpreso;
                const DEFLACTOR_BASE: number = this.datosConfImpresion.deflactorBase;
                this.generarEstructuraMatrizConImp();
                this.economicActivities.forEach((activities, index) => {
                    if (activities.codActividad === that.client.activity) {
                        actividadSelected = activities.glsActividad;
                    }
                });
                this.isButtonImprime = data.checkedImpresion ? false : true;
                this.data2 = [];
                //  datos basicos del impreso
                this.data2.push([ null, '', '', '',
                    this.translateService.instant('breakdown.hotTable.datos_basicos'),
                    '',
                    '',
                    null,
                    '',
                    '',
                    null,
                    '',
                    '',
                    null,
                    '',
                    '',
                    null
                ]);
                this.data2.push([ 2, 2, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_persona.title'),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([
                    1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_persona.rut', { rut: this.client.rut + '-' + this.client.rutVerificationNumber }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([
                    1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_persona.nombre', { nombre: this.client.name }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([
                    1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_persona.actividad', { actividad: actividadSelected }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                //  datos de desglose
                this.data2.push([ 2, 2, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.title'),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                let input = <HTMLSelectElement>document.getElementById('field_breakdownTemplateType');
                this.data2.push([ 1, 2, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.plantilla',
                        {
                            plantilla: input.options[ input.selectedIndex ].text
                        }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                input = <HTMLSelectElement>document.getElementById('field_breakdownType');
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.tipo', {
                        tipo: input.options[ input.selectedIndex ].text
                    }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                input = <HTMLSelectElement>document.getElementById('field_breakdownSubType');
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.subTipo', {
                        subtipo: input.options[ input.selectedIndex ].text
                    }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                input = <HTMLSelectElement>document.getElementById('field_currency');
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.moneda', {
                        moneda: input.options[ input.selectedIndex ].text
                    }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                input = <HTMLSelectElement>document.getElementById('field_monetaryUnit');
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.montos', {
                        montos: input.options[ input.selectedIndex ].text
                    }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_desgloses.deflactorBase', { deflactorBase: this.breakdown.valDeflacBase }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                // datos de impreso
                this.data2.push([ 2, 2, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_impreso.title'),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_impreso.fechaActualizacion', {
                        fechaActualizacion: this.datePipe.transform(DATE,
                            DATE_FORMAT_PERIOD_ES_UI_HEADER)
                    }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_impreso.monedaImpreso', {
                        moneda: !isNullOrUndefined(data.monedaImpreso) ?
                            this.validationUtils.getDescripcionCodigoByCodGenerico(CODE_MONEDA, data.monedaImpreso).glsGenericalarga : ''
                    }),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_impreso.deflactorPeriodo'),
                    !isNullOrUndefined(data.deflactores[ 0 ]) ? data.deflactores[ 0 ] : null,
                    null,
                    null,
                    !isNullOrUndefined(data.deflactores[ 1 ]) ? data.deflactores[ 1 ] : null,
                    null,
                    null,
                    !isNullOrUndefined(data.deflactores[ 2 ]) ? data.deflactores[ 2 ] : null,
                    null,
                    null,
                    !isNullOrUndefined(data.deflactores[ 3 ]) ? data.deflactores[ 3 ] : null,
                    null
                ]);
                this.data2.push([ 1, 1, '', '',
                    this.translateService.instant('breakdown.hotTable.datos_impreso.tipoCambioPeriodo'),
                    !isNullOrUndefined(data.tiposCambio[ 1 ]) ? data.tiposCambio[ 1 ] : null,
                    null,
                    null,
                    !isNullOrUndefined(data.tiposCambio[ 2 ]) ? data.tiposCambio[ 2 ] : null,
                    null,
                    null,
                    !isNullOrUndefined(data.tiposCambio[ 3 ]) ? data.tiposCambio[ 3 ] : null,
                    null,
                    null,
                    !isNullOrUndefined(data.tiposCambio[ 4 ]) ? data.tiposCambio[ 4 ] : null,
                    null
                ]);
                this.data2.push([ 1, 1, '', '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.dataGlobal.forEach((concept, index) => {
                        if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] === COD_TIPO_CONCEP_BALANCE ) {
                                if (concept[ COLUMN_NUMBER_COD_CONCEPT ].split('.').length > 1) {
                                   // HIDDEN_ROWS.push(index);
                                } else {
                                    if (conceptos[ index ].flgIsPrinted === 1) {
                                        let glsFormulaVal0: number = null;
                                        let glsFormulaVal1: number = null;
                                        let glsFormulaVal2: number = null;
                                        let glsFormulaVal3: number = null;
                                        if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])) {
                                                glsFormulaVal0 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])) {
                                                glsFormulaVal1 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])) {
                                                glsFormulaVal2 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])) {
                                                glsFormulaVal3 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] : null;
                                            }
                                            if (MONEDA_IMPRESO === that.breakdown.codMoneda) {
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = !isNaN(glsFormulaVal0 * (DEFLACTOR_BASE / data.deflactores[ 0 ])) ?
                                                    glsFormulaVal0 * (DEFLACTOR_BASE / data.deflactores[ 0 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = !isNaN(glsFormulaVal1 * (DEFLACTOR_BASE / data.deflactores[ 1 ])) ?
                                                    glsFormulaVal1 * (DEFLACTOR_BASE / data.deflactores[ 1 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = !isNaN(glsFormulaVal2 * (DEFLACTOR_BASE / data.deflactores[ 2 ])) ?
                                                    glsFormulaVal2 * (DEFLACTOR_BASE / data.deflactores[ 2 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = !isNaN(glsFormulaVal3 * (DEFLACTOR_BASE / data.deflactores[ 3 ])) ?
                                                    glsFormulaVal3 * (DEFLACTOR_BASE / data.deflactores[ 3 ]) : '';
                                            } else {
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = !isNaN(glsFormulaVal0 * data.tiposCambio[ 1 ]) ?
                                                    glsFormulaVal0 * data.tiposCambio[ 1 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = !isNaN(glsFormulaVal1 * data.tiposCambio[ 2 ]) ?
                                                    glsFormulaVal1 * data.tiposCambio[ 2 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = !isNaN(glsFormulaVal2 * data.tiposCambio[ 3 ]) ?
                                                    glsFormulaVal2 * data.tiposCambio[ 3 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = !isNaN(glsFormulaVal3 * data.tiposCambio[ 4 ]) ?
                                                    glsFormulaVal3 * data.tiposCambio[ 4 ] : '';
                                            }
                                            that.data2.push(concept);
                                        } else {
                                            this.data2.push([ null, '', '', '',concept[COLUMN_NUMBER_CCC],
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                                null,
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                                null,
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                                null,
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                            ]);
                                        }
                                    }
                                }
                        }
                    }
                );
                this.data2.push([ 1, 1, '', '', '', null, '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.dataGlobal.forEach((concept, index) => {
                        if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] === COD_TIPO_CONCEP_EST_RESULT) {
                                if (concept[ COLUMN_NUMBER_COD_CONCEPT ].split('.').length > 1) {
                                    // HIDDEN_ROWS.push(index);
                                } else {
                                    if (conceptos[index].flgIsPrinted === 1) {
                                        let glsFormulaVal0: number = null;
                                        let glsFormulaVal1: number = null;
                                        let glsFormulaVal2: number = null;
                                        let glsFormulaVal3: number = null;
                                        if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])) {
                                                glsFormulaVal0 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])) {
                                                glsFormulaVal1 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])) {
                                                glsFormulaVal2 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])) {
                                                glsFormulaVal3 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] : null;
                                            }
                                            if (MONEDA_IMPRESO === that.breakdown.codMoneda) {
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = !isNaN(glsFormulaVal0 * (DEFLACTOR_BASE / data.deflactores[ 0 ])) ?
                                                    glsFormulaVal0 * (DEFLACTOR_BASE / data.deflactores[ 0 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = !isNaN(glsFormulaVal1 * (DEFLACTOR_BASE / data.deflactores[ 1 ])) ?
                                                    glsFormulaVal1 * (DEFLACTOR_BASE / data.deflactores[ 1 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = !isNaN(glsFormulaVal2 * (DEFLACTOR_BASE / data.deflactores[ 2 ])) ?
                                                    glsFormulaVal2 * (DEFLACTOR_BASE / data.deflactores[ 2 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = !isNaN(glsFormulaVal3 * (DEFLACTOR_BASE / data.deflactores[ 3 ])) ?
                                                    glsFormulaVal3 * (DEFLACTOR_BASE / data.deflactores[ 3 ]) : '';
                                            } else {
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = !isNaN(glsFormulaVal0 * data.tiposCambio[ 1 ]) ?
                                                    glsFormulaVal0 * data.tiposCambio[ 1 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = !isNaN(glsFormulaVal1 * data.tiposCambio[ 2 ]) ?
                                                    glsFormulaVal1 * data.tiposCambio[ 2 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = !isNaN(glsFormulaVal2 * data.tiposCambio[ 3 ]) ?
                                                    glsFormulaVal2 * data.tiposCambio[ 3 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = !isNaN(glsFormulaVal3 * data.tiposCambio[ 4 ]) ?
                                                    glsFormulaVal3 * data.tiposCambio[ 4 ] : '';
                                            }
                                            that.data2.push(concept);
                                        } else {
                                            this.data2.push([ null, '', '', '',concept[COLUMN_NUMBER_CCC],
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                                null,
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                                null,
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                                null,
                                                this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                                this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                            ]);
                                        }
                                    }
                                }
                            }
                    }
                );
                this.data2.push([ 1, 1, '', '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.dataGlobal.forEach((concept, index) => {
                        if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] === COD_TIPO_CONCEP_GEN_FONDOS) {
                            if ( conceptos[index].flgIsPrinted === 1) {
                                let glsFormulaVal0: number = null;
                                let glsFormulaVal1: number = null;
                                let glsFormulaVal2: number = null;
                                let glsFormulaVal3: number = null;
                                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ])) {
                                        glsFormulaVal0 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] : null;
                                    }
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ])) {
                                        glsFormulaVal1 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] : null;
                                    }
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ])) {
                                        glsFormulaVal2 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] : null;
                                    }
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ])) {
                                        glsFormulaVal3 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ] : null;
                                    }
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = glsFormulaVal0;
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = glsFormulaVal1;
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = glsFormulaVal2;
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = glsFormulaVal3;
                                    that.data2.push(concept);
                                } else {
                                    this.data2.push([ null, '', '', '',concept[COLUMN_NUMBER_CCC],
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                        null,
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                        null,
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                        null,
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                    ]);
                                }
                            }
                        }
                    }
                );
                this.data2.push([ 1, 1, '', '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.dataGlobal.forEach((concept, index) => {
                        if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] === COD_TIPO_CONCEP_IND_FINAN) {
                            if (conceptos[index].flgIsPrinted === 1) {
                                let glsFormulaVal0: number = null;
                                let glsFormulaVal1: number = null;
                                let glsFormulaVal2: number = null;
                                let glsFormulaVal3: number = null;
                                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ])) {
                                        glsFormulaVal0 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] : null;
                                    }
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ])) {
                                        glsFormulaVal1 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] : null;
                                    }
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ])) {
                                        glsFormulaVal2 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] : null;
                                    }
                                    if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ])) {
                                        glsFormulaVal3 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ] : null;
                                    }
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = glsFormulaVal0;
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = glsFormulaVal1;
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = glsFormulaVal2;
                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = glsFormulaVal3;
                                    that.data2.push(concept);
                                } else {
                                    this.data2.push([ null, '', '', '',concept[COLUMN_NUMBER_CCC],
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                        null,
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                        null,
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                        null,
                                        this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                                        this.translateService.instant('breakdown.hotTable.percentSymbol'),
                                    ]);
                                }
                            }
                        }
                    }
                );
                this.data2.push([ 1, 1, '', '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ 3, 3, '', '', this.translateService.instant('breakdown.hotTable.info'),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ null, '', '', '', this.translateService.instant('breakdown.hotTable.detalle_cuentas'),
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null,
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null,
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null,
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                ]);
                if (!isNullOrUndefined(this.extraDataDesglose.matrizConImp)) {
                    this.extraDataDesglose.matrizConImp.forEach((config: IConceptoImpresion, indice) => {
                        this.dataGlobal.forEach((concept, index) => {
                                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_CONCEPT ])) {
                                    if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] === config.codTipoConcep &&
                                        (concept[ COLUMN_NUMBER_COD_CONCEPT ] === config.codConcepto
                                            || concept[ COLUMN_NUMBER_COD_CONCEPT ].toString().indexOf(config.codConcepto + '.') === 0) && conceptos[index].flgIsPrinted === 1) {
                                        if (concept[ COLUMN_NUMBER_COD_CONCEPT ].split('.')[ 0 ] === config.codConcepto) {
                                                let glsFormulaVal0: number = null;
                                                let glsFormulaVal1: number = null;
                                                let glsFormulaVal2: number = null;
                                                let glsFormulaVal3: number = null;
                                                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])) {
                                                    glsFormulaVal0 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])) ?
                                                        concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] : null;
                                                }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])) {
                                                glsFormulaVal1 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])) ?
                                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])) {
                                                glsFormulaVal2 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])) ?
                                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] : null;
                                            }
                                            if (!isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])) {
                                                glsFormulaVal3 = !isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])) ?
                                                    concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] : null;
                                            }
                                            if (MONEDA_IMPRESO === that.breakdown.codMoneda) {
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = !isNaN(glsFormulaVal0 * (DEFLACTOR_BASE / data.deflactores[ 0 ])) ?
                                                    glsFormulaVal0 * (DEFLACTOR_BASE / data.deflactores[ 0 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = !isNaN(glsFormulaVal1 * (DEFLACTOR_BASE / data.deflactores[ 1 ])) ?
                                                    glsFormulaVal1 * (DEFLACTOR_BASE / data.deflactores[ 1 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = !isNaN(glsFormulaVal2 * (DEFLACTOR_BASE / data.deflactores[ 2 ])) ?
                                                    glsFormulaVal2 * (DEFLACTOR_BASE / data.deflactores[ 2 ]) : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = !isNaN(glsFormulaVal3 * (DEFLACTOR_BASE / data.deflactores[ 3 ])) ?
                                                    glsFormulaVal3 * (DEFLACTOR_BASE / data.deflactores[ 3 ]) : '';
                                            } else {
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = !isNaN(glsFormulaVal0 * data.tiposCambio[ 1 ]) ?
                                                    glsFormulaVal0 * data.tiposCambio[ 1 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = !isNaN(glsFormulaVal1 * data.tiposCambio[ 2 ]) ?
                                                    glsFormulaVal1 * data.tiposCambio[ 2 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = !isNaN(glsFormulaVal2 * data.tiposCambio[ 3 ]) ?
                                                    glsFormulaVal2 * data.tiposCambio[ 3 ] : '';
                                                concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = !isNaN(glsFormulaVal3 * data.tiposCambio[ 4 ]) ?
                                                    glsFormulaVal3 * data.tiposCambio[ 4 ] : '';
                                            }
                                            let cuentaVacios = 0;
                                            if ((isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ]) || concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] === '' || Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ])=== 0)) {
                                                cuentaVacios++;
                                            }
                                            if ((isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ]) || concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] === '' || Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ])=== 0)) {
                                                cuentaVacios++;
                                            }
                                            if ((isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ]) || concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] === '' || Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ])=== 0)) {
                                                cuentaVacios++;
                                            }
                                            if ((isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ]) || concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] === '' || Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ])=== 0)) {
                                                cuentaVacios++;
                                            }
                                            if (cuentaVacios !== 4) {
                                                that.data2.push(concept);
                                            }
                                        }
                                    }
                                }
                        });
                        }
                    );
                }
                this.data2.push([ 1, 1, '', '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ null, '', '', '',
                    this.translateService.instant('breakdown.hotTable.empresas_consolidan'),
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null,
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null,
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null,
                    this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER),
                    this.translateService.instant('breakdown.hotTable.percentSymbol'),
                    null
                ]);
                this.data2.push([ 1, 1, '', '', '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                // Empresas que consolidan
                const CONSOLIDADOS: string[] = [];
                that.extraDataDesglose.empConsolPeriodoDesgConsolList.forEach((consol, index) => {
                    if (CONSOLIDADOS.filter(c => c === consol.nomEmpresa).length === 0) {
                        CONSOLIDADOS.push(consol.nomEmpresa);
                    }
                });
                CONSOLIDADOS.forEach((consol, index) => {
                    const FILTRO: EmprConsolPeriodoDesgConsol[] = that.extraDataDesglose.empConsolPeriodoDesgConsolList.filter(c => (c.nomEmpresa === consol) &&
                        (this.datePipe.transform(c.fecPeriodo, DATE_FORMAT_PERIOD_ES_UI_HEADER) ===
                            this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER)
                            || this.datePipe.transform(c.fecPeriodo, DATE_FORMAT_PERIOD_ES_UI_HEADER) ===
                            this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER)
                            || this.datePipe.transform(c.fecPeriodo, DATE_FORMAT_PERIOD_ES_UI_HEADER) ===
                            this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER)
                            || this.datePipe.transform(c.fecPeriodo, DATE_FORMAT_PERIOD_ES_UI_HEADER) ===
                            this.datePipe.transform(that.extraDataDesglose.fecPeriodo[ that.activePeriodsIndexes[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER)));
                    let glsFormulaPrc0: number = null;
                    let glsFormulaPrc1: number = null;
                    let glsFormulaPrc2: number = null;
                    let glsFormulaPrc3: number = null;
                    if (!isNullOrUndefined(FILTRO[ 0 ])) {
                        glsFormulaPrc0 = !isNaN(Number(FILTRO[ 0 ].valPrcConsol)) ? FILTRO[ 0 ].valPrcConsol : null;
                    }
                    if (!isNullOrUndefined(FILTRO[ 1 ])) {
                        glsFormulaPrc1 = !isNaN(Number(FILTRO[ 1 ].valPrcConsol)) ? FILTRO[ 1 ].valPrcConsol : null;
                    }
                    if (!isNullOrUndefined(FILTRO[ 2 ])) {
                        glsFormulaPrc2 = !isNaN(Number(FILTRO[ 2 ].valPrcConsol)) ? FILTRO[ 2 ].valPrcConsol : null;
                    }
                    if (!isNullOrUndefined(FILTRO[ 3 ])) {
                        glsFormulaPrc3 = !isNaN(Number(FILTRO[ 3 ].valPrcConsol)) ? FILTRO[ 3 ].valPrcConsol : null;
                    }
                    if (MONEDA_IMPRESO === that.breakdown.codMoneda) {
                        this.data2.push([ 1, 1, '', '', consol,
                            glsFormulaPrc0,
                            null,
                            null,
                            glsFormulaPrc1,
                            null,
                            null,
                            glsFormulaPrc2,
                            null,
                            null,
                            glsFormulaPrc3,
                            null,
                        ]);
                    } else {
                        this.data2.push([ 1, 1, '', '', consol,
                            glsFormulaPrc0,
                            null,
                            null,
                            glsFormulaPrc1,
                            null,
                            null,
                            glsFormulaPrc2,
                            null,
                            null,
                            glsFormulaPrc3,
                            null,
                        ]);
                    }
                });
                this.data2.push([ null, '', '', '',
                    this.translateService.instant('breakdown.hotTable.observaciones'),
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                this.data2.push([ 1, 1, '', '', !isNullOrUndefined(this.breakdown.glsObservacion) ? this.breakdown.glsObservacion : '',
                    null, null, null, null, null, null, null, null, null, null, null
                ]);
                // hotInstance.getPlugin('hiddenRows').hideRows(HIDDEN_ROWS);
                hotInstance.render();
                that.isOcultaDivPrint = false;
                this.loaderService.isLoading.next(false);
                }, 200);
                setTimeout(() => {
                    hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                    setTimeout(() => {
                        hotInstance.selectCell(this.data2.length-1, COLUMN_NUMBER_CCC);
                        setTimeout(() => {
                            hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                        }, 200);
                    }, 200);
                }, 200);
            });
            this.ngbModalRefGeneratePrint.result.then(
                () => {
                    this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                    this.ngbModalRefGeneratePrint = null;
                },
                () => {
                    this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                    this.ngbModalRefGeneratePrint = null;
                }
            );
        } else {
            // TODO: Error. No hay moneda seleccionada ni codigos de moneda cargados en la página.
        }
    }

    onSuccessPressGeneratePrint() {
        if (isNullOrUndefined(this.extraDataDesglose.deflatorPeriod[ 0 ]) && !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ 0 ])) {
            this.extraDataDesglose.deflatorPeriod[ 0 ] = this.breakdown.valDeflacBase;
        }
        if (isNullOrUndefined(this.extraDataDesglose.deflatorPeriod[ 1 ]) && !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ 1 ])) {
            this.extraDataDesglose.deflatorPeriod[ 1 ] = this.breakdown.valDeflacBase;
        }
        if (isNullOrUndefined(this.extraDataDesglose.deflatorPeriod[ 2 ]) && !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ 2 ])) {
            this.extraDataDesglose.deflatorPeriod[ 2 ] = this.breakdown.valDeflacBase;
        }
        if (isNullOrUndefined(this.extraDataDesglose.deflatorPeriod[ 3 ]) && !isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ 3 ])) {
            this.extraDataDesglose.deflatorPeriod[ 3 ] = this.breakdown.valDeflacBase;
        }
    }

    onClickPreviousPeriod() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (this.isEdicion) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.warningPrevious',
                'question',
                'breakdown.yes',
                'breakdown.no'
            ).subscribe(res => {
                if (res) {
                    this.loaderService.isLoading.next(true);
                    setTimeout(() => {
                        this.btnEdit1.disabled = false;
                        this.btnEdit2.disabled = false;
                        this.btnEdit3.disabled = false;
                        this.btnEdit4.disabled = false;
                        this.recargarDesactiveConceptsPeriod(0,true);
                        this.recargarDesactiveConceptsPeriod(1,true);
                        this.recargarDesactiveConceptsPeriod(2,true);
                        this.recargarDesactiveConceptsPeriod(3,true);
                        if (hotInstance) {
                            setTimeout(() => {
                                hotInstance.getPlugin('formulas').recalculateFull();
                                hotInstance.render();
                            }, 200);
                        }
                        this.executePreviousPeriodAction();
                        hotInstance.getPlugin('formulas').recalculateFull();
                        hotInstance.render();
                        this.clickOnActiveTabTable(200);
                    }, 200);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        } else {
            this.loaderService.isLoading.next(true);
            setTimeout(() => {
                this.executePreviousPeriodAction();
                hotInstance.getPlugin('formulas').recalculateFull();
                hotInstance.render();
                this.clickOnActiveTabTable(200);
            }, 200);
        }
    }

    private executePreviousPeriodAction() {
        this.isEdicion = false;
        let position = 0;
        this.activePeriodsIndexes = [];
        /*this.recargarConceptosFormulasDigitadasDesactivate();*/
        this.limpiarDatosMultiregistro(this.extraDataDesglose);
        for (let i = 0; i < this.extraDataDesglose.periodos.length; i++) {
            if (i >= this.nextPeriodIndex - 1) {
                if (this.activePeriodsIndexes.length < 4) {
                    this.activePeriodsIndexes.push(i);
                }
                if (!isNullOrUndefined(this.extraDataDesglose.periodos[ i ].valPeriodoDesgloseDTOList) &&
                    this.extraDataDesglose.periodos[ i ].valPeriodoDesgloseDTOList.length > 0) {
                    if (!isNullOrUndefined(this.extraDataDesglose.periodos[ i ])) {
                        this.recorreConceptosAsignaData(this.extraDataDesglose.periodos[ i ].valPeriodoDesgloseDTOList, position);
                        this.recorreConceptosAsignGeneracionFondosIndicadoresFinancieros(this.extraDataDesglose.periodos[ i ].valPeriodoDesgloseDTOList, position);
                    }
                    position = position + 1;
                }
            }
        }
        this.nextPeriodIndex = this.nextPeriodIndex - 1;
        if (this.nextPeriodIndex === 0) {
            this.isButtonPreviousDisabled = true;
        }
        this.isButtonNextDisabled = false;
        this.colHeadersConfig();
    }

    onClickNextPeriod() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (this.isEdicion) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.warningNext',
                'question',
                'breakdown.yes',
                'breakdown.no'
            ).subscribe(res => {
                if (res) {
                    this.loaderService.isLoading.next(true);
                    setTimeout(() => {
                        this.btnEdit1.disabled = false;
                        this.btnEdit2.disabled = false;
                        this.btnEdit3.disabled = false;
                        this.btnEdit4.disabled = false;
                        this.recargarDesactiveConceptsPeriod(0,true);
                        this.recargarDesactiveConceptsPeriod(1,true);
                        this.recargarDesactiveConceptsPeriod(2,true);
                        this.recargarDesactiveConceptsPeriod(3,true);
                        if (hotInstance) {
                            setTimeout(() => {
                                hotInstance.getPlugin('formulas').recalculateFull();
                                hotInstance.render();
                            }, 200);
                        }
                        this.executeNextPeriodAction();
                        hotInstance.getPlugin('formulas').recalculateFull();
                        hotInstance.render();
                        this.clickOnActiveTabTable(200);
                    }, 200);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        } else {
            this.loaderService.isLoading.next(true);
            setTimeout(() => {
                this.executeNextPeriodAction();
                hotInstance.getPlugin('formulas').recalculateFull();
                hotInstance.render();
                this.clickOnActiveTabTable(200);
            }, 200);
        }
    }

    private executeNextPeriodAction() {
        this.isEdicion = false;
        let position = 0;
        this.activePeriodsIndexes = [];
        /*this.recargarConceptosFormulasDigitadasDesactivate();*/
        this.limpiarDatosMultiregistro(this.extraDataDesglose);
        this.extraDataDesglose.periodos.forEach((period, index) => {
            if (index >= this.nextPeriodIndex) {
                if (this.activePeriodsIndexes.length < 4) {
                    this.activePeriodsIndexes.push(index + 1);
                }
                if (!isNullOrUndefined(period.valPeriodoDesgloseDTOList) && period.valPeriodoDesgloseDTOList.length > 0) {
                    if (!isNullOrUndefined(this.extraDataDesglose.periodos[ index + 1 ])) {
                        this.recorreConceptosAsignaData(this.extraDataDesglose.periodos[ index + 1 ].valPeriodoDesgloseDTOList, position);
                        this.recorreConceptosAsignGeneracionFondosIndicadoresFinancieros(this.extraDataDesglose.periodos[ index + 1 ].valPeriodoDesgloseDTOList, position);
                    }
                    position = position + 1;
                }
            }
        });
        this.nextPeriodIndex = this.nextPeriodIndex + 1;
        this.isButtonPreviousDisabled = false;
        this.colHeadersConfig();
        if (isNullOrUndefined(this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ])) {
            this.isButtonNextDisabled = true;
            // Poner en blanco todos los campos de la columna ya que es un período nuevo.
            conceptos.filter(concepto => ((concepto.codTipoConcep === COD_TIPO_CONCEP_BALANCE) || (concepto.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)))
            .forEach((concept, ind) => {
                /* if (isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal === '') {*/
                if (!isNullOrUndefined(concept.codTipoRegis) && concept.codConcepto !== ' ') {
                    this.data[ ind ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ position - 1 ] ] = 0;
                    this.data1[ ind ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ position - 1 ] ] = 0;
                    this.data[ ind ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ position - 1 ] ] = 0;
                    this.data1[ ind ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ position - 1 ] ] = 0;
                }
                /*}*/
            });
        }
    }

    onClickPrintTabPrint() {
        this.onClickSelectionPrint();
    }

    compareFnPeriodRating(item1: string, item2: string): boolean {
        if (!isNullOrUndefined(item2)) {
            item2 = item2 + ''; // Se convierte a string si no es null o undefined
        }
        return item1 === item2;
    }

    onClickSelectionPrint() {
        this.ngbModalSelectionPrint = this.modalService.open(BreakdownSelectionPrintDialogComponent as Component, {
            windowClass: 'selection-print-dialog',
            backdrop: 'static'
        });
        this.ngbModalSelectionPrint.componentInstance.saveSelectionPrintDialogEmit.subscribe(data => {
            if(isNaN(this.datosConfImpresion.tiposCambio[0])) {
                this.datosConfImpresion.tiposCambio.splice(0, 1);
            }
            const desgloseImpresionDTO = new DesgloseImpresionDTO(this.breakdown.codCliente,Number(this.breakdown.codPlantilla),
                Number(this.breakdown.codTipoDes),Number(this.breakdown.codSubtipoDes),this.breakdown.codMoneda,
                this.activePeriodsIndexes,false,this.datosConfImpresion.monedaImpreso,
                this.datosConfImpresion.deflactorBase,this.datosConfImpresion.deflactores,this.datosConfImpresion.tiposCambio,
                this.breakdown.fecDeflacBase);

                console.log('LO QUE SE MANDA AL SERVICIO DESDE LA PARTE DE ABAJO',desgloseImpresionDTO );
            if (data === '1') {
                this.reportService.InformeIngresoServerPdf(desgloseImpresionDTO).subscribe(
                    (datos: HttpResponse<any>) => {
                        const file = new Blob([ datos ], { type: 'application/pdf' });
                        const fileURL = URL.createObjectURL(file);
                        window.open(fileURL);
                    },
                    (datos: HttpErrorResponse) => this.onError(datos.message)
                );
            } else {
                this.reportService.InformeResumenServerPdf(desgloseImpresionDTO).subscribe(
                    (datos: HttpResponse<any>) => {
                        const file = new Blob([ datos ], { type: 'application/pdf' });
                        const fileURL = URL.createObjectURL(file);
                        window.open(fileURL);
                    },
                    (datos: HttpErrorResponse) => this.onError(datos.message)
                );
            }
        });
        this.ngbModalSelectionPrint.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalSelectionPrint = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalSelectionPrint = null;
            }
        );
    }
    onClickDeleteBreakdown() {
        this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
            'breakdown.confirmation',
            'breakdown.deleteDesglose',
            'question',
            'breakdown.yes',
            'breakdown.no'
        ).subscribe(res => {
            if (res) {
                this.desgloseService.deleteDesglo(this.breakdown).subscribe(
                    (resp: HttpResponse<any>) => {
                        this.resetAllElementsToInitialState(true);
                    },
                    (resp: HttpErrorResponse) => this.onError(resp.message)
                );
            }
            this.questionMessageSubscription.unsubscribe();
        });
    }

    /**
     * Método que se ejecuta al hacer clic en el botón imprimir.
     */
    onClickPrint() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
        this.extraDataDesglose.numPeriodosLoaded = !isNullOrUndefined(this.extraDataDesglose.periodos) ? this.extraDataDesglose.periodos.length : 0;
        this.extraDataDesglose.canValidate = true;
        this.extraDataDesglose.conceptos = conceptos.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE ||
            el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
        this.extraDataDesglose.data = this.data1;
        this.extraDataDesglose.visualData = visualData;
        this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes).subscribe(
            (data: HttpResponse<IBreakdownValidationError[]>) => {
                if (data.body.length > 0) {
                    const optionalQuestionMessage = 'breakdown.errors.questionMessagePrint';
                    this.displayValidationErrors(data.body, this.CALL_METHOD_AFTER_OPTIONAL_VALIDATION_PRINT, optionalQuestionMessage);
                } else {
                    this.loaderService.isLoading.next(true);
                    setTimeout(() => {
                        this.onSuccessValidationsPrint();
                    }, 200);
                }
            },
            (data: HttpErrorResponse) => {
                this.jhiAlertService.error(data.message, null, null);
            }
        );
    }

    /**
     * Se ejecuta si al imprimir un desglose luego de llamar al servicio de validaciones de cuadratura no existen errores de validación.
     */
    private onSuccessValidationsPrint() {
        const desgloseImpresionDTO = new DesgloseImpresionDTO(this.breakdown.codCliente,Number(this.breakdown.codPlantilla),
            Number(this.breakdown.codTipoDes),Number(this.breakdown.codSubtipoDes),this.breakdown.codMoneda,
            this.activePeriodsIndexes,false,this.datosConfImpresion.monedaImpreso,
            this.breakdown.valDeflacBase,
            [this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 0 ] ],
                this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 1 ] ],
                this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 2 ] ],
                this.extraDataDesglose.deflatorPeriod[ this.activePeriodsIndexes[ 3 ] ]],
            [1,1,1,1],
            this.breakdown.fecDeflacBase);

            console.log('LO QUE SE MANDA AL SERVICIO 1', desgloseImpresionDTO);
        this.reportService.InformeResumenServerPdf(desgloseImpresionDTO).subscribe(
            (datos: HttpResponse<any>) => {
                const file = new Blob([ datos ], { type: 'application/pdf' });
                const fileURL = URL.createObjectURL(file);
                window.open(fileURL);
            },
            (datos: HttpErrorResponse) => {
                this.jhiAlertService.error('error.impresion', null, null);
            }
        );
    }
    private grabarValImp(valPeriodoDesgImpr: ValPeriodoDesgImpr) {
        this.valPeriodoDesgImprService.CifSpInsValImp(valPeriodoDesgImpr).subscribe(
            (data: HttpResponse<any>) => {
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );
    }

    sendToReviewOnClick() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (hotInstance) {
            const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
            this.extraDataDesglose.numPeriodosLoaded = !isNullOrUndefined(this.extraDataDesglose.periodos) ? this.extraDataDesglose.periodos.length : 0;
            this.extraDataDesglose.canValidate = true;
            this.extraDataDesglose.conceptos = conceptos.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE ||
                el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
            this.extraDataDesglose.data = this.data1;
            this.extraDataDesglose.visualData = visualData;
            if (this.currentAccount.authorities.filter(data => data === this.USER_ROLE_PLAT_DESGLOSE_EJECUTOR).length > 0) {
                if (!isNullOrUndefined(this.jefeAsociadoDeEjecutor)) {
                    if (!isNullOrUndefined(this.jefeAsociadoDeEjecutor.numRolEmpleado)) {
                        const numRolRevisor = this.jefeAsociadoDeEjecutor.numRolEmpleado.toString();
                        this.aperturaEnviarRevision(numRolRevisor);
                    } else {
                        this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningAssociatedEjecutor');
                    }
                } else {
                    this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningAssociatedEjecutor');
                }
            }
            if (this.currentAccount.authorities.filter(data => data === this.USER_ROLE_PLAT_DESGLOSE_REVISOR).length > 0) {
                if (!isNullOrUndefined(this.revisorAsociadoRevisor[ 0 ])) {
                    if (!isNullOrUndefined(this.revisorAsociadoRevisor[ 0 ].numRolEmpleado)) {
                        const numRolRevisor = this.revisorAsociadoRevisor[ 0 ].numRolEmpleado.toString();
                        this.aperturaEnviarRevision(numRolRevisor);
                    } else {
                        this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningAssociatedReviewer');
                    }
                } else {
                    this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningAssociatedReviewer');
                }
            }
        }
    }

    private aperturaEnviarRevision(numRolRevisor: string) {
        this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes).subscribe(
            (data: HttpResponse<IBreakdownValidationError[]>) => {
                this.turbineUserService.findByRole(numRolRevisor).subscribe(
                    (datos: HttpResponse<ITurbineUsuarios>) => {
                        this.reviewerNameOnSuccessValidationsReview = datos.body[ 0 ].nombre;
                        this.reviewerNumRolOnSuccessValidationsReview = numRolRevisor;
                        this.onSuccessValidationsReview(data.body);
                    },
                    (datos: HttpErrorResponse) => this.onError(datos.message)
                );
            },
            (data: HttpErrorResponse) => this.sweetAlert2.mensajeWarning('breakdown.warning', 'breakdown.warningAssociatedReviewer'));
    }

    /**
     * Se ejecuta al hacer clic en el botón 'Dev. ejecutor'.
     */
    sendBackToExecutorOnClick() {
        if (this.currentAccount.authorities.filter(data => data === this.USER_ROLE_PLAT_DESGLOSE_REVISOR).length > 0) {
            const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
            if (hotInstance) {
                const visualData: any = hotInstance.getData(); // Estos son los datos que contiene los valores que se muestran en pantalla.
                this.extraDataDesglose.numPeriodosLoaded = !isNullOrUndefined(this.extraDataDesglose.periodos) ? this.extraDataDesglose.periodos.length : 0;
                this.extraDataDesglose.canValidate = true;
                this.extraDataDesglose.conceptos = conceptos.filter(el => el.codTipoConcep === COD_TIPO_CONCEP_BALANCE ||
                    el.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT);
                this.extraDataDesglose.data = this.data1;
                this.extraDataDesglose.visualData = visualData;
                this.desgloseService.validate(this.breakdown, this.extraDataDesglose, this.activePeriodsIndexes).subscribe(
                    (data: HttpResponse<IBreakdownValidationError[]>) => {
                        this.onSuccessValidationsSendBackToExecutor(data.body);
                    },
                    (data: HttpErrorResponse) => this.onError(data.message));
            }
        }
    }

    /**
     * Se ejecuta al hacer clic en el botón 'Ver mensajes'.
     */
    viewMessagesOnClick() {
        const that = this;
        this.ngbModalMessages = this.modalService.open(BreakdownMessagesComponent as Component, {
            windowClass: 'messages',
            backdrop: 'static'
        });
        this.ngbModalMessages.componentInstance.mensajeSoloLectura = that.mensaje;
        this.ngbModalMessages.componentInstance.isHiddenEscritura = true;
        this.ngbModalMessages.componentInstance.enviarMessagesEmit.subscribe(data => {
            this.mensajeEnvio = data;
        });
        this.ngbModalMessages.result.then(
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalMessages = null;
            },
            () => {
                this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                this.ngbModalMessages = null;
            }
        );
    }

    /**
     * Se ejecuta al hacer clic en el botón 'Liberar'.
     */
    freeBreakdownOnClick() {
        this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
            'breakdown.confirmation',
            'breakdown.breakFreeDesglose',
            'question',
            'breakdown.yes',
            'breakdown.no'
        ).subscribe(res => {
            if (res) {
                this.resetAllElementsToInitialState(false);
            }
            this.questionMessageSubscription.unsubscribe();
        });
    }

    /**
     * Libera un desglose si el estado actual es en edición.
     */
    private freeBreakdown(isProces:boolean) {
        if(isProces) {
            this.desgloseService.clearBreakdownUserOpenAndSetStatusInProcessTemporal().subscribe(
                    (data: HttpResponse<IDesglose>) => {},
                    (data: HttpErrorResponse) => this.onError(data.message)
            );
            if (this.breakdown.codEstado === ESTADO_EN_EDICION &&
                (this.breakdown.nomUserOpen === this.usuario.loginName)) {
                this.saveInProcess(true,false);
            } else {
                if(!isNullOrUndefined(this.sessionStorage.retrieve(SESSION_VARIABLE_CLOSE))) {
                    this.loginService.logout();
                }
            }
        } else {
            if(!isNullOrUndefined(this.breakdown.codEstado)) {
                this.desgloseService.clearBreakdownUserOpenAndSetStatusInProcess(this.breakdown).subscribe(
                    (data: HttpResponse<IDesglose>) => {
                        if (!isNullOrUndefined(this.sessionStorage.retrieve(SESSION_VARIABLE_CLOSE))) {
                            this.loginService.logout();
                        }
                    },
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            } else {
                this.desgloseService.clearBreakdownUserOpenAndSetStatusInProcessTemporal().subscribe(
                    (data: HttpResponse<IDesglose>) => {},
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            }
        }
    }
    private onChangueCargarPeriodos(period: number) {
        this.onDeflactorChange(0);
        this.onDeflactorChange(1);
        this.onDeflactorChange(2);
        this.onDeflactorChange(3);
        this.onDeflactorChange(4);
        this.onMonthNumberChange(1);
        this.onMonthNumberChange(2);
        this.onMonthNumberChange(3);
        this.onMonthNumberChange(4);
        const input1: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo1Rpt');
        const input2: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo2Rpt');
        const input3: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo3Rpt');
        const input4: HTMLInputElement = <HTMLInputElement>document.getElementById('field_fecPeriodo4Rpt');
        switch (period) {
            case 0: {
                if (input1.value === '') {
                    // this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 0 ] ] = moment(new Date(), DATE_FORMAT_CREATE_MOMENT_OBJECT);
                    document.getElementById('field_fecPeriodo1Rpt').focus();
                }
                this.btnEdit1.disabled = true;
                this.btnEdit2.disabled = false;
                this.btnEdit3.disabled = false;
                this.btnEdit4.disabled = false;
                this.recargarDesactiveConceptsPeriod(1,false);
                this.recargarDesactiveConceptsPeriod(2,false);
                this.recargarDesactiveConceptsPeriod(3,false);
                break;
            }
            case 1: {
                if (input2.value === '') {
                    // this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 1 ] ] = moment(new Date(), DATE_FORMAT_CREATE_MOMENT_OBJECT);
                    document.getElementById('field_fecPeriodo2Rpt').focus();
                }
                this.btnEdit1.disabled = false;
                this.btnEdit2.disabled = true;
                this.btnEdit3.disabled = false;
                this.btnEdit4.disabled = false;
                this.recargarDesactiveConceptsPeriod(0,false);
                this.recargarDesactiveConceptsPeriod(2,false);
                this.recargarDesactiveConceptsPeriod(3,false);
                break;
            }
            case 2: {
                if (input3.value === '') {
                    // this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 2 ] ] = moment(new Date(), DATE_FORMAT_CREATE_MOMENT_OBJECT);
                    document.getElementById('field_fecPeriodo3Rpt').focus();
                }
                this.btnEdit1.disabled = false;
                this.btnEdit2.disabled = false;
                this.btnEdit3.disabled = true;
                this.btnEdit4.disabled = false;
                this.recargarDesactiveConceptsPeriod(0,false);
                this.recargarDesactiveConceptsPeriod(1,false);
                this.recargarDesactiveConceptsPeriod(3,false);
                break;
            }
            case 3: {
                if (input4.value === '') {
                    // this.extraDataDesglose.fecPeriodo[ this.activePeriodsIndexes[ 3 ] ] = moment(new Date(), DATE_FORMAT_CREATE_MOMENT_OBJECT);
                    document.getElementById('field_fecPeriodo4Rpt').focus();
                }
                this.btnEdit1.disabled = false;
                this.btnEdit2.disabled = false;
                this.btnEdit3.disabled = false;
                this.btnEdit4.disabled = true;
                this.recargarDesactiveConceptsPeriod(0,false);
                this.recargarDesactiveConceptsPeriod(1,false);
                this.recargarDesactiveConceptsPeriod(2,false);
                break;
            }
        }
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (hotInstance) {
            hotInstance.getPlugin('formulas').recalculateFull();
        }

        this.recargarActiveConceptsPeriod(period, true);
        setTimeout(() => {
            hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
            setTimeout(() => {
                hotInstance.selectCell(this.data1.length-1, COLUMN_NUMBER_CCC);
                setTimeout(() => {
                    hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                }, 50);
            }, 50);
        }, 50);
    }

    private recargarActiveConceptsPeriod(period: number, verificarErrores?: boolean) {
        if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
            conceptos.forEach((concept, index) => {
                if(!isNullOrUndefined(this.data[index])) {
                    if (concept.codConcepto === this.data[ index ][ COLUMN_NUMBER_COD_CONCEPT ] && concept.codTipoConcep === this.data[ index ][ COLUMN_NUMBER_COD_TIPO_CONCEP ]) {
                        switch (period) {
                            case 0: {
                                if ((!isNullOrUndefined(concept.glsFormulaVal) && concept.glsFormulaVal !== '' && concept.glsFormulaVal !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = concept.glsFormulaVal;
                                }
                                break;
                            }
                            case 1: {
                                if ((!isNullOrUndefined(concept.glsFormulaVal2) && concept.glsFormulaVal2 !== '' && concept.glsFormulaVal2 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = concept.glsFormulaVal2;
                                }
                                break;
                            }
                            case 2: {
                                if ((!isNullOrUndefined(concept.glsFormulaVal3) && concept.glsFormulaVal3 !== '' && concept.glsFormulaVal3 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = concept.glsFormulaVal3;
                                }
                                break;
                            }
                            case 3: {
                                if ((!isNullOrUndefined(concept.glsFormulaVal4) && concept.glsFormulaVal4 !== '' && concept.glsFormulaVal4 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = concept.glsFormulaVal4;
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        switch (period) {
                            case 0: {
                                if ((!isNullOrUndefined(concept.glsFormulaPrc) && concept.glsFormulaPrc !== '' && concept.glsFormulaPrc !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ] = concept.glsFormulaPrc;
                                }
                                break;
                            }
                            case 1: {
                                if ((!isNullOrUndefined(concept.glsFormulaPrc2) && concept.glsFormulaPrc2 !== '' && concept.glsFormulaPrc2 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] ] = concept.glsFormulaPrc2;
                                }
                                break;
                            }
                            case 2: {
                                if ((!isNullOrUndefined(concept.glsFormulaPrc3) && concept.glsFormulaPrc3 !== '' && concept.glsFormulaPrc3 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] ] = concept.glsFormulaPrc3;
                                }
                                break;
                            }
                            case 3: {
                                if ((!isNullOrUndefined(concept.glsFormulaPrc4) && concept.glsFormulaPrc4 !== '' && concept.glsFormulaPrc4 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ] ] = concept.glsFormulaPrc4;
                                }
                                break;
                            }
                            default: {
                                break;
                            }
                        }
                        switch (period) {
                            case 0: {
                                if ((!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef !== '' && concept.glsFormulaDef !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                                }
                                break;
                            }
                            case 1: {
                                if ((!isNullOrUndefined(concept.glsFormulaDef2) && concept.glsFormulaDef2 !== '' && concept.glsFormulaDef2 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = concept.glsFormulaDef2;
                                }
                                break;
                            }
                            case 2: {
                                if ((!isNullOrUndefined(concept.glsFormulaDef3) && concept.glsFormulaDef3 !== '' && concept.glsFormulaDef3 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] = concept.glsFormulaDef3;
                                }
                                break;
                            }
                            case 3: {
                                if ((!isNullOrUndefined(concept.glsFormulaDef4) && concept.glsFormulaDef4 !== '' && concept.glsFormulaDef4 !== '0')) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ] = concept.glsFormulaDef4;
                                }
                                break;
                            }
                            default : {
                                break;
                            }
                        }
                        if(!isNullOrUndefined(this.data1[index])) {
                            if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                                switch (period) {
                                    case 0: {
                                        if ((!isNullOrUndefined(concept.glsFormulaVal) && concept.glsFormulaVal !== '' && concept.glsFormulaVal !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ] = concept.glsFormulaVal;
                                        }
                                        break;
                                    }
                                    case 1: {
                                        if ((!isNullOrUndefined(concept.glsFormulaVal2) && concept.glsFormulaVal2 !== '' && concept.glsFormulaVal2 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ] = concept.glsFormulaVal2;
                                        }
                                        break;
                                    }
                                    case 2: {
                                        if ((!isNullOrUndefined(concept.glsFormulaVal3) && concept.glsFormulaVal3 !== '' && concept.glsFormulaVal3 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ] = concept.glsFormulaVal3;
                                        }
                                        break;
                                    }
                                    case 3: {
                                        if ((!isNullOrUndefined(concept.glsFormulaVal4) && concept.glsFormulaVal4 !== '' && concept.glsFormulaVal4 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ] = concept.glsFormulaVal4;
                                        }
                                        break;
                                    }
                                    default : {
                                        break;
                                    }
                                }
                                switch (period) {
                                    case 0: {
                                        if ((!isNullOrUndefined(concept.glsFormulaPrc) && concept.glsFormulaPrc !== '' && concept.glsFormulaPrc !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 0 ] ] = concept.glsFormulaPrc;
                                        }
                                        break;
                                    }
                                    case 1: {
                                        if ((!isNullOrUndefined(concept.glsFormulaPrc2) && concept.glsFormulaPrc2 !== '' && concept.glsFormulaPrc2 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 1 ] ] = concept.glsFormulaPrc2;
                                        }
                                        break;
                                    }
                                    case 2: {
                                        if ((!isNullOrUndefined(concept.glsFormulaPrc3) && concept.glsFormulaPrc3 !== '' && concept.glsFormulaPrc3 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 2 ] ] = concept.glsFormulaPrc3;
                                        }
                                        break;
                                    }
                                    case 3: {
                                        if ((!isNullOrUndefined(concept.glsFormulaPrc4) && concept.glsFormulaPrc4 !== '' && concept.glsFormulaPrc4 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ 3 ] ] = concept.glsFormulaPrc4;
                                        }
                                        break;
                                    }
                                    default : {
                                        break;
                                    }
                                }
                                switch (period) {
                                    case 0: {
                                        if ((!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef !== '' && concept.glsFormulaDef !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                                        }
                                        break;
                                    }
                                    case 1: {
                                        if ((!isNullOrUndefined(concept.glsFormulaDef2) && concept.glsFormulaDef2 !== '' && concept.glsFormulaDef2 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = concept.glsFormulaDef2;
                                        }
                                        break;
                                    }
                                    case 2: {
                                        if ((!isNullOrUndefined(concept.glsFormulaDef3) && concept.glsFormulaDef3 !== '' && concept.glsFormulaDef3 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] = concept.glsFormulaDef3;
                                        }
                                        break;
                                    }
                                    case 3: {
                                        if ((!isNullOrUndefined(concept.glsFormulaDef4) && concept.glsFormulaDef4 !== '' && concept.glsFormulaDef4 !== '0')) {
                                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ] = concept.glsFormulaDef4;
                                        }
                                        break;
                                    }
                                    default : {
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            });
        }
            const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
            if (hotInstance) {
                hotInstance.getPlugin('formulas').recalculateFull();
            }
            const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
            if (hotInstance1) {
                hotInstance1.getPlugin('formulas').recalculateFull();
            }
            if (!isNullOrUndefined(verificarErrores)) {
                // this.anularValoresIndefinidosTablas(hotInstance,hotInstance1);
            }
    }

/*    private recargarConceptosFormulasDigitadas() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
            if (hotInstance) {
                const data = hotInstance.getSourceData();
                conceptos.forEach((concept, index) => {
                    for (let period = 0; period < 4; period++) {
                        if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                            switch (period) {
                                case 0: {
                                    if (!isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) && concept.flgIsTotal === 0 && data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ].toString().includes('=')) {
                                        concept.glsFormulaVal = !isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0;
                                    }
                                    break;
                                }
                                case 1: {
                                    if (!isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) && concept.flgIsTotal === 0 && data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ].toString().includes('=')) {
                                        concept.glsFormulaVal2 = !isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0;
                                    }
                                    break;
                                }
                                case 2: {
                                    if (!isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) && concept.flgIsTotal === 0 && data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ].toString().includes('=')) {
                                        concept.glsFormulaVal3 = !isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0;
                                    }
                                    break;
                                }
                                case 3: {
                                    if (!isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) && concept.flgIsTotal === 0 && data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ].toString().includes('=')) {
                                        concept.glsFormulaVal4 = !isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
    }*/

    /*private recargarConceptosFormulasDigitadasDesactivate() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
            if (hotInstance) {
                // const data = hotInstance.getSourceData();
                conceptos.forEach((concept, index) => {
                    for (let period = 0; period < 4; period++) {
                        if (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT) {
                            switch (period) {
                                case 0: {
                                    if (!isNullOrUndefined(concept.glsFormulaVal) && concept.glsFormulaVal.toString().includes('=') && concept.codConcepto.toString().includes('.')) {
                                        conceptos[ index ].glsFormulaVal = null;
                                    }
                                    break;
                                }
                                case 1: {
                                    if (!isNullOrUndefined(concept.glsFormulaVal2) && concept.glsFormulaVal2.toString().includes('=') && concept.codConcepto.toString().includes('.')) {
                                        conceptos[ index ].glsFormulaVal2 = null;
                                    }
                                    break;
                                }
                                case 2: {
                                    if (!isNullOrUndefined(concept.glsFormulaVal3) && concept.glsFormulaVal3.toString().includes('=') && concept.codConcepto.toString().includes('.')) {
                                        conceptos[ index ].glsFormulaVal3 = null;
                                    }
                                    break;
                                }
                                case 3: {
                                    if (!isNullOrUndefined(concept.glsFormulaVal4) && concept.glsFormulaVal4.toString().includes('=') && concept.codConcepto.toString().includes('.')) {
                                        conceptos[ index ].glsFormulaVal4 = null;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                });
            }
        }
    }*/

    private recargarActiveConcepts() {
        this.onDeflactorChange(0);
        this.onDeflactorChange(1);
        this.onDeflactorChange(2);
        this.onDeflactorChange(3);
        this.onDeflactorChange(4);
        this.onMonthNumberChange(1);
        this.onMonthNumberChange(2);
        this.onMonthNumberChange(3);
        this.onMonthNumberChange(4);
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (!isNullOrUndefined(conceptos) && conceptos.length > 0) {
            if (hotInstance) {
                const data = hotInstance.getData();
                // inyecto las formulas de totales de val a totales de deflactores
                conceptos.forEach((concept, index) => {
                    for (let period = 0; period < 4; period++) {
                        if (!isNullOrUndefined(concept) && (concept.codTipoConcep === COD_TIPO_CONCEP_BALANCE || concept.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT)) {
                            if(!isNullOrUndefined( data[ index ]) && !isNullOrUndefined( data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) {
                                if(!isNullOrUndefined(this.data[ index ])) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = !isNullOrUndefined(data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0;
                                    switch (period) {
                                        case 0: {
                                            if (!isNullOrUndefined(concept.glsFormulaVal) && concept.glsFormulaVal.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL1) && concept.glsFormulaVal !== '' && concept.glsFormulaVal !== '0' && isNaN(Number(concept.glsFormulaVal))) {
                                                conceptos[ index ].glsFormulaDef = concept.glsFormulaVal.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL1, 'g'), this.COLUMN_LETTER_GLS_FORMULA_DEF_1);
                                            }
                                            break;
                                        }
                                        case 1: {
                                            if (!isNullOrUndefined(concept.glsFormulaVal2) && concept.glsFormulaVal2.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL2) && concept.glsFormulaVal2 !== '' && concept.glsFormulaVal2 !== '0' && isNaN(Number(concept.glsFormulaVal2))) {
                                                conceptos[ index ].glsFormulaDef2 = concept.glsFormulaVal2.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL2, 'g'), this.COLUMN_LETTER_GLS_FORMULA_DEF_2);
                                                conceptos[ index ].glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(DEF_PER1, 'g'),DEF_PER2);
                                                conceptos[ index ].glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER1, 'g'),CANT_MESES_PER2);
                                                conceptos[ index ].glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(CANT_MESES_PER0, 'g'),CANT_MESES_PER1);
                                            }
                                            break;
                                        }
                                        case 2: {
                                            if (!isNullOrUndefined(concept.glsFormulaVal3) && concept.glsFormulaVal3.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL3) && concept.glsFormulaVal3 !== '' && concept.glsFormulaVal3 !== '0' && isNaN(Number(concept.glsFormulaVal3))) {
                                                conceptos[ index ].glsFormulaDef3 = concept.glsFormulaVal3.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL3, 'g'), this.COLUMN_LETTER_GLS_FORMULA_DEF_3);
                                                conceptos[ index ].glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(DEF_PER1, 'g'), DEF_PER3);
                                                conceptos[ index ].glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER1, 'g'), CANT_MESES_PER3);
                                                conceptos[ index ].glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(CANT_MESES_PER0, 'g'), CANT_MESES_PER2);
                                            }
                                            break;
                                        }
                                        case 3: {
                                            if (!isNullOrUndefined(concept.glsFormulaVal4) && concept.glsFormulaVal4.toString().includes(COLUMN_LETTER_GLS_FORMULA_VAL4) && concept.glsFormulaVal4 !== '' && concept.glsFormulaVal4 !== '0' && isNaN(Number(concept.glsFormulaVal4))) {
                                                conceptos[ index ].glsFormulaDef4 = concept.glsFormulaVal4.toString().replace(new RegExp(COLUMN_LETTER_GLS_FORMULA_VAL4, 'g'), this.COLUMN_LETTER_GLS_FORMULA_DEF_4);
                                                conceptos[ index ].glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(DEF_PER1, 'g'), DEF_PER4);
                                                conceptos[ index ].glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER1, 'g'), CANT_MESES_PER4);
                                                conceptos[ index ].glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(CANT_MESES_PER0, 'g'), CANT_MESES_PER3);
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        } else {
                            if (!isNullOrUndefined(this.data[ index ]) && isNullOrUndefined(this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) {
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = 0;
                            }
                        }
                    }
                });
            }
            conceptos.forEach((concept, index) => {
                if(!isNullOrUndefined(this.data[index])) {
                    if (!isNullOrUndefined(this.extraDataDesglose.periodos)) {
                        if (this.extraDataDesglose.periodos.length > 0) {
                            if (!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef !== '' && concept.glsFormulaDef !== '0') {
                                if (concept.glsFormulaDef.toString().includes(P_ANT)) {
                                    const regex = '(P_ANT.\\d+)';
                                    const CONCIDENCIAS = concept.glsFormulaDef.toString().match(new RegExp(regex));
                                    if (!isNullOrUndefined(CONCIDENCIAS)) {
                                        CONCIDENCIAS.forEach((concidencia, indice) => {
                                            const PERIODOANT = concidencia.split('.');
                                            const NEWREGEX = '(P_ANT.' + PERIODOANT[ 1 ] + ')';
                                            let valconcep1 = '0';
                                            let valconcep2 = '0';
                                            let valconcep3 = '0';
                                            let valconcep4 = '0';
                                            if (!isNullOrUndefined(this.extraDataDesglose.periodos[ this.activePeriodsIndexes[ 0 ] - 1 ])) {
                                                let lista: IValPeriodoDesglose[] = this.extraDataDesglose.periodos[ this.activePeriodsIndexes[ 0 ] - 1 ].valPeriodoDesgloseDTOList;
                                                lista = lista.filter(v => (v.codTipoConcep === conceptos[ Number(PERIODOANT[ 1 ])].codTipoConcep &&
                                                    v.corConcepto === conceptos[ Number(PERIODOANT[ 1 ])].corConcepto));
                                                if (!isNullOrUndefined(lista) && lista.length > 0) {
                                                    valconcep1 = !isNullOrUndefined(lista[ 0 ].valConcepto) ? lista[ 0 ].valConcepto.toString() : '0';
                                                }
                                            }
                                            if (!isNullOrUndefined(this.extraDataDesglose.periodos[ this.activePeriodsIndexes[ 0 ] ])) {
                                                    if(this.contMultiregistro> 0) {
                                                        valconcep2 = 'H' + (Number(PERIODOANT[ 1 ]) + (this.contMultiregistro - 1));
                                                    } else {
                                                        valconcep2 = 'H' + (Number(PERIODOANT[ 1 ]));
                                                    }
                                            }
                                            if (!isNullOrUndefined(this.extraDataDesglose.periodos[ this.activePeriodsIndexes[ 1 ] ])) {
                                                    if(this.contMultiregistro> 0) {
                                                        valconcep3 = 'K' + (Number(PERIODOANT[ 1 ]) + (this.contMultiregistro - 1));
                                                    } else {
                                                        valconcep3 = 'K' + (Number(PERIODOANT[ 1 ]));
                                                    }
                                            }
                                            if (!isNullOrUndefined(this.extraDataDesglose.periodos[ this.activePeriodsIndexes[ 2 ] ])) {
                                                    if(this.contMultiregistro> 0) {
                                                        valconcep4 = 'N' + (Number(PERIODOANT[ 1 ]) + (this.contMultiregistro - 1));
                                                    } else {
                                                        valconcep4 = 'N' + (Number(PERIODOANT[ 1 ]));
                                                    }
                                            }
                                            conceptos[ index ].glsFormulaDef = concept.glsFormulaDef.toString().replace(new RegExp(NEWREGEX, 'g'), !isNullOrUndefined(valconcep1.toString()) ? valconcep1.toString() : '0');
                                            conceptos[ index ].glsFormulaDef2 = concept.glsFormulaDef2.toString().replace(new RegExp(NEWREGEX, 'g'), !isNullOrUndefined(valconcep2.toString()) ? valconcep2.toString() : '0');
                                            conceptos[ index ].glsFormulaDef3 = concept.glsFormulaDef3.toString().replace(new RegExp(NEWREGEX, 'g'), !isNullOrUndefined(valconcep3.toString()) ? valconcep3.toString() : '0');
                                            conceptos[ index ].glsFormulaDef4 = concept.glsFormulaDef4.toString().replace(new RegExp(NEWREGEX, 'g'), !isNullOrUndefined(valconcep4.toString()) ? valconcep4.toString() : '0');
                                        });
                                    }
                                }
                            }
                            switch (this.extraDataDesglose.periodos.length) {
                                case 1: {
                                    if(!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                                    } else  {
                                        if(isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal==='') {
                                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] =  this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                                        }
                                    }
                                    break;
                                }
                                case 2: {
                                    if(!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal==='') {
                                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] =  this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                                        }
                                    }
                                    if(!isNullOrUndefined(concept.glsFormulaDef2) && concept.glsFormulaDef2!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = concept.glsFormulaDef2;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal2) || concept.glsFormulaVal2==='') {
                                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] =  this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ];
                                        }
                                    }
                                    break;
                                }
                                case 3: {
                                    if(!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                                            }
                                        }
                                    }
                                    if(!isNullOrUndefined(concept.glsFormulaDef2) && concept.glsFormulaDef2!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = concept.glsFormulaDef2;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal2) || concept.glsFormulaVal2==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ];
                                            }
                                        }
                                    }
                                    if(!isNullOrUndefined(concept.glsFormulaDef3) && concept.glsFormulaDef3!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] = concept.glsFormulaDef3;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal3) || concept.glsFormulaVal3==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ];
                                            }
                                        }
                                    }
                                    break;
                                }
                                default : {
                                    if(!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                                            }
                                        }
                                    }
                                    if(!isNullOrUndefined(concept.glsFormulaDef2) && concept.glsFormulaDef2!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = concept.glsFormulaDef2;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal2) || concept.glsFormulaVal2==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 1 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 1 ] ];
                                            }
                                        }
                                    }
                                    if(!isNullOrUndefined(concept.glsFormulaDef3) && concept.glsFormulaDef3!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] = concept.glsFormulaDef3;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal2) || concept.glsFormulaVal2==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 2 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 2 ] ];
                                            }
                                        }
                                    }
                                    if(!isNullOrUndefined(concept.glsFormulaDef4) && concept.glsFormulaDef4!=='') {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ] = concept.glsFormulaDef4;
                                    } else {
                                        if(isNullOrUndefined(concept.glsFormulaVal4) || concept.glsFormulaVal4==='') {
                                            if(!isNullOrUndefined(concept.codTipoRegis)) {
                                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 3 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 3 ] ];
                                            }
                                        }
                                    }
                                    break;
                                }
                            }
                        } else {
                            if(!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef!=='') {
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                            } else {
                                if(isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal==='') {
                                    if(!isNullOrUndefined(concept.codTipoRegis)) {
                                        this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                                    }
                                }
                            }
                        }
                    } else {
                        if(!isNullOrUndefined(concept.glsFormulaDef) && concept.glsFormulaDef!=='') {
                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = concept.glsFormulaDef;
                        } else {
                            if(isNullOrUndefined(concept.glsFormulaVal) || concept.glsFormulaVal==='') {
                                if(!isNullOrUndefined(concept.codTipoRegis)) {
                                    this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ 0 ] ] = this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ 0 ] ];
                                }
                            }
                        }
                    }
                }
            });
        }
    }

    private recargarDesactiveConceptsPeriod(period: number,limpiarNoTotales:boolean) {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        if (hotInstance) {
            hotInstance.getData().forEach((concept, index) => {
                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                    if (concept[ COLUMN_NUMBER_FLG_IS_TOTAL ] === 0) {
                        if(limpiarNoTotales) {
                            if(!isNullOrUndefined(this.data[ index ])) {
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0) : 0;
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] : 0) : 0;
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] : 0) : 0;
                            }
                            if(!isNullOrUndefined(this.data1[ index ])) {
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : 0) : 0;
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] : 0) : 0;
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] : 0) : 0;
                            }
                        }
                    } else {
                        if(!isNullOrUndefined(this.data[ index ])) {
                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : '') : '';
                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] : '') : '';
                            this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] : '') : '';
                        }
                        if(!isNullOrUndefined(this.data1[ index ])) {
                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] : '') : '';
                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] : '') : '';
                            this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = !isNullOrUndefined(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ]) ? (!isNaN(Number(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ])) ? concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] : '') : '';
                        }
                    }
                }
            });
        }
    }

    private recargarDesactiveConcepts() {
        const hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
        const hotInstance1 = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
        if (hotInstance) {
            hotInstance.getData().forEach((concept, index) => {
                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                    for (let period = 0; period < 4; period++) {
                        if (concept[ COLUMN_NUMBER_FLG_IS_TOTAL ] === 0) {
                            if(!isNullOrUndefined(this.data[ index ])) {
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = 0;
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = 0;
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = 0;
                            }
                            if(!isNullOrUndefined(this.data1[ index ])) {
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = 0;
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = 0;
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = 0;
                            }
                        } else {
                            if(!isNullOrUndefined(this.data[ index ])) {
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = '';
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = '';
                                this.data[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = '';
                            }
                            if(!isNullOrUndefined(this.data1[ index ])) {
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] = '';
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] = '';
                                this.data1[ index ][ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] = '';
                            }
                        }
                    }
                }
            });
            hotInstance.getPlugin('formulas').recalculateFull();
            hotInstance1.getPlugin('formulas').recalculateFull();
        }
    }

    private anularValoresIndefinidosTablas(hotInstance: any, hotInstance1: any) {
        if (!isNullOrUndefined(hotInstance) && hotInstance) {
            const data = hotInstance.getData();
            data.forEach((concept, index) => {
                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                    for (let period = 0; period < 4; period++) {
                        if (concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ] !== '' && !isFinite(concept[ COLUMN_NUMBER_GLS_FORMULA_VAL[ period ] ])) {
                            hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_VAL[ period ], 0);
                        }
                        if (concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ] !== '' && !isFinite(concept[ COLUMN_NUMBER_GLS_FORMULA_PRC[ period ] ])) {
                            hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_PRC[ period ], 0);
                        }
                    }
                }
            });
            hotInstance.getPlugin('formulas').recalculateFull();
        }
        if (!isNullOrUndefined(hotInstance1) && hotInstance1) {
            const data = hotInstance1.getData();
            data.forEach((concept, index) => {
                if (!isNullOrUndefined(concept[ COLUMN_NUMBER_COD_TIPO_REGIS ]) && concept[ COLUMN_NUMBER_COD_TIPO_REGIS ] !== '') {
                    for (let period = 0; period < 4; period++) {
                        if (concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ] !== '' && !isFinite(concept[ COLUMN_NUMBER_GLS_FORMULA_DEF[ period ] ])) {
                            hotInstance.setDataAtCell(index, COLUMN_NUMBER_GLS_FORMULA_DEF[ period ], 0);
                        }
                    }
                }
            });
            hotInstance1.getPlugin('formulas').recalculateFull();

        }
    }

    private updateSelectedTabTableView(timeout: number, row: number, col: number, hideExtraColumns: boolean) {
        const hotInstance1 = this.hotRegisterer.getInstance(this.currentTableId);
        let i = 0;
        this.hiddenRows = [];
        this.hiddenRows1 = [];
        hotInstance1.getData().forEach((concept, index) => {
            if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] !== COD_TIPO_CONCEP_EST_RESULT && concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] !== COD_TIPO_CONCEP_BALANCE) {
                this.hiddenRows.push(i);
            }
            if (concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] !== COD_TIPO_CONCEP_IND_FINAN && concept[ COLUMN_NUMBER_COD_TIPO_CONCEP ] !== COD_TIPO_CONCEP_GEN_FONDOS) {
                this.hiddenRows1.push(i);
            }
            i = i + 1;
        });
        const hotInstance = this.hotRegisterer.getInstance(this.currentTableId);
        if (hotInstance) {
            if (hideExtraColumns) {
                hotInstance.getPlugin('hiddenRows').hideRows(this.hiddenRows1);
                hotInstance.getPlugin('hiddenColumns').hideColumns(COLUMN_NUMBER_GLS_FORMULA_VAL);
                hotInstance.getPlugin('hiddenColumns').hideColumns(COLUMN_NUMBER_GLS_FORMULA_PRC);
                hotInstance.getPlugin('hiddenColumns').showColumns(COLUMN_NUMBER_GLS_FORMULA_DEF);
            } else {
                hotInstance.getPlugin('hiddenRows').hideRows(this.hiddenRows);
                hotInstance.getPlugin('hiddenColumns').hideColumns(COLUMN_NUMBER_GLS_FORMULA_DEF);
            }
            setTimeout(() => {
                hotInstance.render();
                this.loaderService.isLoading.next(false);
            }, timeout);
            setTimeout(() => {
                hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                setTimeout(() => {
                    hotInstance.selectCell(this.data1.length-1, COLUMN_NUMBER_CCC);
                    setTimeout(() => {
                        hotInstance.selectCell(0, COLUMN_NUMBER_CCC);
                    }, 200);
                }, 200);
            }, 200);
        }
    }

    /**
     * Chequea por la tab activa y aplicarle el selectCell a la tabla de esa tab.
     */
    private clickOnActiveTabTable(timeout: number) {
        let hotInstance;
        const row = 2;
        const col = 1;
        let hideExtraColumns = false;
        if (!isNullOrUndefined(this.currentTableId)) {
            if (this.currentTableId === this.handOnTableDetailInstanceId) {
                hotInstance = this.hotRegisterer.getInstance(this.handOnTableDetailInstanceId);
            } else if (this.currentTableId === this.handOnTableGenFonInstanceId) {
                hideExtraColumns = true;
                hotInstance = this.hotRegisterer.getInstance(this.handOnTableGenFonInstanceId);
                hotInstance.getPlugin('formulas').recalculateFull();
                // this.anularValoresIndefinidosTablas(null,hotInstance);

            }
            if (hotInstance) {
                setTimeout(() => {
                    this.updateSelectedTabTableView(timeout, row, col, hideExtraColumns);
                }, timeout);
                hotInstance.selectCell(2, 1);
            }
        }
    }

    replaceDotByComma(event, number: number) {
        switch (number) {
            case 0: // Deflactor base
                if(!isNullOrUndefined(this.breakdown.valDeflacBaseStr) && this.breakdown.valDeflacBaseStr.includes('.')) {
                    this.breakdown.valDeflacBaseStr = this.breakdown.valDeflacBaseStr.replace('.', ',');
                }
                break;
            case 1: // Deflactor período 1
                if(!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ]) && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ].includes('.')) {
                    this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ]
                        = this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 0 ] ].replace('.', ',');
                }
                break;
            case 2: // Deflactor período 2
                if(!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ]) && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ].includes('.') ) {
                    this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ]
                        = this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 1 ] ].replace('.', ',');
                }
                break;
            case 3: // Deflactor período 3
                if(!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ]) && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ].includes('.') ) {
                    this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ]
                        = this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 2 ] ].replace('.', ',');
                }
                break;
            case 4: // Deflactor período 4
                if(!isNullOrUndefined(this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ]) && this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ].includes('.')) {
                    this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ]
                        = this.extraDataDesglose.deflatorPeriodStr[ this.activePeriodsIndexes[ 3 ] ].replace('.', ',');
                }
                break;
            default:
                break;
        }
    }
}
