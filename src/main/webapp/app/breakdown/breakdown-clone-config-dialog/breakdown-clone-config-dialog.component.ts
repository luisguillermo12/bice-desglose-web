import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import {
    COD_SUBTIPO_DESG_OTHER1,
    COD_SUBTIPO_DESG_OTHER2, COD_SUBTIPO_DESG_PESOS,
    COD_TIPO_MONEDA_PESOS,
    CODTABLA_SUBTIPO_DESGLOSE,
    CODTABLA_TIPO_DESGLOSE,
    CODTABLA_TIPO_MONEDAS,
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE,
    SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES,
    SUB_TIPO_DESGLOSE_OTRA_MONEDA,
    SUB_TIPO_DESGLOSE_PESOS
} from 'app/app.constants';
import { isNullOrUndefined } from 'util';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiAlertService } from 'ng-jhipster';
import { Principal } from 'app/core';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { SessionStorageService } from 'ngx-webstorage';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { Desglose, IDesglose } from 'app/shared/model/desglose.model';
import { FilterDescripcionCodigoPipe } from 'app/shared/pipes/filter-descripcion-codigo.pipe';
import { TranslateService } from '@ngx-translate/core';
import { IBreakdownFilterResponseDTO } from 'app/entities/desglose/filter-breakdown-response.model';
import { DesgloseService } from 'app/entities/desglose';
import { BreakdownSearchRequestDTO, IBreakdownSearchRequestDTO } from 'app/entities/desglose/search-breakdown-request.model';
import { BreakdownKey, IBreakdownKey } from 'app/shared/model/breakdown-key.model';
import { Sweetalert2Component } from 'app/shared';

@Component({
    selector: 'jhi-breakdown-clone-config-dialog',
    templateUrl: './breakdown-clone-config-dialog.component.html',
    styleUrls: [ './breakdown-clone-config-dialog.scss' ]
})
export class BreakdownCloneConfigDialogComponent implements OnInit {

    public onAcceptCloneEventEmitter: EventEmitter<IBreakdownKey> = new EventEmitter();
    state: { state: string, stateClass: string } = { state: '', stateClass: '' };
    breakdown: IDesglose = new Desglose();
    currentAccount: any;
    sort = 'codGenerico,ASC';
    codTipoDes?: string;
    codSubtipoDes?: string;
    glsSubtipoDes?: string;
    codMoneda?: string;
    codCliente?: string;
    codTipoPlantDes?: string;
    subTipoDesgloseOtraMoneda?: string;
    subTipoDesglosePesos?: string;
    public simpleComboBoxTypes: IDescripcionCodigo[] = [];
    CODTABLA_TIPO_DESGLOSE = CODTABLA_TIPO_DESGLOSE;
    CODTABLA_TIPO_MONEDAS = CODTABLA_TIPO_MONEDAS;
    CODTABLA_SUBTIPO_DESGLOSE = CODTABLA_SUBTIPO_DESGLOSE;
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE = CODTABLA_TIPO_PLANTILLAS_DESGLOSE;
    @ViewChild('fieldBreakdownSubTypeDesc') fieldBreakdownSubTypeDesc: ElementRef;
    @ViewChild('fieldBreakdownSubType') fieldBreakdownSubType: ElementRef;
    @ViewChild('fieldBreakdownCurrency') fieldBreakdownCurrency: ElementRef;
    public existBreakdown = true;
    breakdownSearchFRequestDTO: IBreakdownSearchRequestDTO = new BreakdownSearchRequestDTO();
    subTipoDesgPesos?: string;
    subTipoDesgOtraMoneda?: string;

    constructor(public activeModal: NgbActiveModal, private jhiAlertService: JhiAlertService, private principal: Principal, private sessionStorage: SessionStorageService,
                private descripcionCodigoService: DescripcionCodigoService, private filterDescripcionCodigoPipe: FilterDescripcionCodigoPipe,
                private translateService: TranslateService, private desgloseService: DesgloseService, private sweetAlert2: Sweetalert2Component) {
    }

    ngOnInit() {
        const allComboBoxData = this.sessionStorage.retrieve(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES);
        if (!isNullOrUndefined(allComboBoxData) && allComboBoxData.length > 0) {
            this.simpleComboBoxTypes = allComboBoxData;
        } else {
            const typesParam = this.CODTABLA_SUBTIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_MONEDAS;
            this.loadIDescripcionCodigo(typesParam, this.sort);
        }

        // Iniciar estado clone desglose
        this.state.state = this.translateService.instant('breakdown.state.busy');
        this.state.stateClass = 'state-danger';

        const codesSubTipoDesgl = this.filterDescripcionCodigoPipe.transform(this.simpleComboBoxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        this.subTipoDesglosePesos = codesSubTipoDesgl[ 0 ].codGenerico;
        this.subTipoDesgloseOtraMoneda = codesSubTipoDesgl[ 1 ].codGenerico;

        this.breakdownSearchFRequestDTO.codCliente = this.codCliente;
        this.breakdownSearchFRequestDTO.tipoPlantillaDesglose = this.codTipoPlantDes;
        this.onBreakdownCurrencyChange();
    }

    loadIDescripcionCodigo(codtabla: string, sort: string /*, destinationArray: number*/) {
        this.descripcionCodigoService.findByCodTablasIn(codtabla, sort).subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.assignDescripcionCodigos(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private assignDescripcionCodigos(data: IDescripcionCodigo[], headers: HttpHeaders) {
        this.simpleComboBoxTypes = data;
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    onClickCancel() {
        this.closeModal();
    }

    triggerOnSelectItemEvent() {
        if (!this.existBreakdown) {
            this.onAcceptCloneEventEmitter.emit(new BreakdownKey(this.codTipoDes, this.codSubtipoDes, this.codMoneda));
        } else {
            this.jhiAlertService.error('global.error.breakdownAlreadyExists');
        }
    }

    private onError(errorMessage: string) {
    }

    onBreakdownCurrencyChange() {
        const codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboBoxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        if (codes && codes.length > 0) {
            this.subTipoDesgPesos = codes[ 0 ].codGenerico;
            this.subTipoDesgOtraMoneda = codes[ 1 ].codGenerico;
        }

        this.fieldBreakdownSubTypeDesc.nativeElement.disabled = (!isNullOrUndefined(this.codSubtipoDes) &&
            (this.codSubtipoDes !== COD_SUBTIPO_DESG_OTHER1 && this.codSubtipoDes !== COD_SUBTIPO_DESG_OTHER2));
        if (this.fieldBreakdownSubTypeDesc.nativeElement.disabled) {
            this.glsSubtipoDes = '';
        }
        if (this.codMoneda === COD_TIPO_MONEDA_PESOS) {
            this.codSubtipoDes = this.subTipoDesglosePesos;
            this.fieldBreakdownSubType.nativeElement.value = SUB_TIPO_DESGLOSE_PESOS;
        } else {
            if (this.codSubtipoDes === COD_SUBTIPO_DESG_PESOS) {
                this.codSubtipoDes = this.subTipoDesgloseOtraMoneda;
                this.fieldBreakdownSubType.nativeElement.value = SUB_TIPO_DESGLOSE_OTRA_MONEDA;
            }
        }
        this.onClickSearchBreakdown();
    }

    onClickSearchBreakdown() {
        this.breakdownSearchFRequestDTO.tipoDesglose = Number(this.codTipoDes);
        this.breakdownSearchFRequestDTO.subTipoDesglose = Number(this.codSubtipoDes);
        this.breakdownSearchFRequestDTO.moneda = this.codMoneda;

        if (this.breakdownSearchFRequestDTO) {
            this.desgloseService.breakdownSearch(this.breakdownSearchFRequestDTO).subscribe(
                (res: HttpResponse<IBreakdownFilterResponseDTO[]>) => {
                    this.existBreakdown = !isNullOrUndefined(res.body) && res.body.length > 0;
                    if (this.existBreakdown) {
                        this.state.state = this.translateService.instant('breakdown.state.busy');
                        this.state.stateClass = 'state-danger';
                    } else {
                        this.state.state = this.translateService.instant('breakdown.state.free');
                        this.state.stateClass = 'success';
                    }
                },
                (res: HttpErrorResponse) => this.onError(res.message)
            );
        }
    }

    onBreakdownSubTypeDescChange(event: any) {
        /*if (this.subTipoDesgloseOtraMoneda === this.codSubtipoDes) {
            event.target.value = SUB_TIPO_DESGLOSE_PESOS;
        }*/
    }

    onBreakdownSubTypeChange() {
        let codesSubTipoDesgl: any[] = [];
        let codesMoneda: any[] = [];
        codesSubTipoDesgl = this.filterDescripcionCodigoPipe.transform(this.simpleComboBoxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        codesMoneda = this.filterDescripcionCodigoPipe.transform(this.simpleComboBoxTypes, this.CODTABLA_TIPO_MONEDAS);
        if (codesSubTipoDesgl && codesSubTipoDesgl.length > 0) {
            const monedaPesos = codesMoneda[ 0 ].codGenerico;
            const otraMoneda = codesSubTipoDesgl[ 1 ].codGenerico;
            const monedaSelect1 = this.codMoneda;
            const initValue1: any [] = [ 0 ];
            codesMoneda.forEach((item, index) => {
                if (item.codGenerico === monedaSelect1) {
                    initValue1[ 0 ] = item.codGenerico;
                }
            });
            if (otraMoneda === this.codSubtipoDes && monedaPesos === initValue1[ 0 ]) {
                this.sweetAlert2.mensajeInfo('breakdown.alertSubTipoDesglose.title', 'breakdown.alertSubTipoDesglose.subTipoOtrasMonedas', {});
                this.codSubtipoDes = SUB_TIPO_DESGLOSE_PESOS;
                this.fieldBreakdownSubType.nativeElement.value = SUB_TIPO_DESGLOSE_PESOS;
            }
        }
        if (codesMoneda && codesMoneda.length > 0) {
            const pesosMoneda = codesMoneda[ 0 ].codGenerico;
            const monedaSelect = this.codMoneda;
            const initValue: any [] = [ 0 ];
            codesMoneda.forEach((item, index) => {
                if (item.codGenerico === monedaSelect) {
                    initValue[ 0 ] = item.codGenerico;
                }
            });
            if (codesSubTipoDesgl && codesSubTipoDesgl.length > 0) {
                const otraMoneda2 = codesSubTipoDesgl[ 0 ].codGenerico;
                if (pesosMoneda !== initValue[ 0 ] && this.codSubtipoDes === otraMoneda2) {
                    this.sweetAlert2.mensajeInfo('breakdown.alertSubTipoDesglose.title', 'breakdown.alertSubTipoDesglose.subTipoPesos', {});
                    this.codSubtipoDes = this.subTipoDesgOtraMoneda;
                    this.fieldBreakdownSubType.nativeElement.value = SUB_TIPO_DESGLOSE_OTRA_MONEDA;
                }
            }
        }
        this.onClickSearchBreakdown();
    }

    onCurrencyChange() {
        const codes = this.filterDescripcionCodigoPipe.transform(this.simpleComboBoxTypes, this.CODTABLA_SUBTIPO_DESGLOSE);
        if (codes && codes.length > 0) {
            this.subTipoDesgPesos = codes[ 0 ].codGenerico;
            this.subTipoDesgOtraMoneda = codes[ 1 ].codGenerico;
        }
        if (this.breakdown.codMoneda === COD_TIPO_MONEDA_PESOS) {
            this.breakdown.codSubtipoDes = this.subTipoDesgPesos;
        } else {
            this.breakdown.codSubtipoDes = this.subTipoDesgOtraMoneda;
        }
    }
}
