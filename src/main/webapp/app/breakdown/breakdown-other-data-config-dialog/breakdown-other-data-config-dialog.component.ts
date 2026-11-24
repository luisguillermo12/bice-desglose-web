import { Component, EventEmitter, OnInit } from '@angular/core';
import { ConceptoImpresionService } from 'app/entities/concepto-impresion';
import { ConceptoImpresion, IConceptoImpresion } from 'app/shared/model/concepto-impresion.model';
import { JhiAlertService } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { COD_TIPO_CONCEP_BALANCE,
         COD_TIPO_CONCEP_EST_RESULT,
         INICIALIZE_CHECKOTHER_CARACTER,
         MAX_CANT_CONTADOR,
         INICIALIZE_CONTADOR,
         MAXCARACTERESVALIDATION,
        } from 'app/app.constants';
import { DATE_FORMAT, DATE_FORMAT_PERIOD_ES_HEADER_CORTA, DATE_FORMAT_PERIOD_ES_UI_HEADER } from 'app/shared';
import { DatePipe } from '@angular/common';
import { IConcepto } from 'app/shared/model/concepto.model';
import { ValPeriodoDesgloseService } from 'app/entities/val-periodo-desglose';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IValPeriodoDesglose } from 'app/shared/model/val-periodo-desglose.model';
import { isNullOrUndefined } from 'util';

@Component({
    selector: 'jhi-breakdown-other-data-config-dialog',
    templateUrl: './breakdown-other-data-config-dialog.component.html',
    styleUrls: [ './breakdown-other-data-config-dialog.scss' ]
})
export class BreakdownOtherDataConfigDialogComponent implements OnInit {
    rows = [];
    selected: IConceptoImpresion[] = [];
    selectedAnterior: IConceptoImpresion[] = [];
    observations: string;
    periods: any[] = [];
    fechaPeriodo: string;
    fechaPeriodo1: string;
    fechaPeriodoStr: string;
    fechaPeriodo1Str: string;
    conceptos: IConcepto[];
    // filtros de busqueda de concepto
    codCliente: string;
    codTipoPlantilla: number;
    codTipoDesglose: number;
    codSubTipoDesglose: number;
    codMoneda: number;
    isIndicePeriodos = [ 0, 1, 2, 3 ];
    public contador = INICIALIZE_CONTADOR;
    public MaxCaracteres = MAX_CANT_CONTADOR;
    public checkCaractOther = INICIALIZE_CHECKOTHER_CARACTER;
    public caractValidationMessage = MAXCARACTERESVALIDATION;
    public saveOtherDataConfigEmit: EventEmitter<any> = new EventEmitter();

    constructor(public activeModal: NgbActiveModal, private datePipe: DatePipe, private conceptoImpresionService: ConceptoImpresionService,
                private valPeriodoDesgloseService: ValPeriodoDesgloseService, private jhiAlertService: JhiAlertService) {
    }

    ngOnInit() {
        this.conceptos.filter(concepto => concepto.codTipoConcep === COD_TIPO_CONCEP_BALANCE
        ).forEach((concept, index) => {
            if (!isNullOrUndefined(concept.codConcepto) && concept.codConcepto.toString().split('.').length === 1) {
                if (!isNullOrUndefined(concept.codConcepto) && concept.codConcepto !== '') {
                    const filtro = this.conceptos.filter(data => !isNullOrUndefined(data.codConcepto) && data.codTipoConcep === COD_TIPO_CONCEP_BALANCE &&
                        data.codConcepto.toString().indexOf(concept.codConcepto + '.') === 0);
                    if (filtro.length > 1) {
                        const concepto = new ConceptoImpresion(
                            this.codCliente, Number(this.codTipoPlantilla), Number(this.codTipoDesglose), Number(this.codSubTipoDesglose), this.codMoneda.toString(),
                            concept.glsConcepto, concept.codTipoConcep, concept.corConcepto.toString(), null, null, concept.codConcepto);
                        this.rows.push(concepto);
                    }
                }
            }
        });
        this.conceptos.filter(concepto => concepto.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT
        ).forEach((concept, index) => {
            if (!isNullOrUndefined(concept.codConcepto)  && concept.codConcepto.toString().split('.').length === 1) {
                if (!isNullOrUndefined(concept.codConcepto) && concept.codConcepto !== '') {
                    const filtro = this.conceptos.filter(data => !isNullOrUndefined(data.codConcepto) && data.codTipoConcep === COD_TIPO_CONCEP_EST_RESULT &&
                        data.codConcepto.toString().indexOf(concept.codConcepto + '.') === 0);
                    if (filtro.length > 1) {
                        const concepto = new ConceptoImpresion(
                            this.codCliente, Number(this.codTipoPlantilla), Number(this.codTipoDesglose), Number(this.codSubTipoDesglose), this.codMoneda.toString(),
                            concept.glsConcepto, concept.codTipoConcep, concept.corConcepto.toString(), null, null, concept.codConcepto);
                        this.rows.push(concepto);
                    }
                }
            }
        });
        if (!isNullOrUndefined(this.periods[ this.isIndicePeriodos[ 3 ] ])) {
            this.fechaPeriodo = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
            this.fechaPeriodo1 = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 3 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
            this.fechaPeriodoStr = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 2 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
            this.fechaPeriodo1Str = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 3 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
        } else {
            if (!isNullOrUndefined(this.periods[ this.isIndicePeriodos[ 2 ] ])) {
                this.fechaPeriodo = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
                this.fechaPeriodo1 = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 2 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
                this.fechaPeriodoStr = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 1 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
                this.fechaPeriodo1Str = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 2 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
            } else {
                if (!isNullOrUndefined(this.periods[ this.isIndicePeriodos[ 1 ] ])) {
                    this.fechaPeriodo = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
                    this.fechaPeriodo1 = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 1 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
                    this.fechaPeriodoStr = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 0 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
                    this.fechaPeriodo1Str = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 1 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
                } else {
                    if (!isNullOrUndefined(this.periods[ this.isIndicePeriodos[ 0 ] ])) {
                        this.fechaPeriodo = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 0 ] ], DATE_FORMAT_PERIOD_ES_UI_HEADER);
                        this.fechaPeriodo1 = '';
                        this.fechaPeriodoStr = this.datePipe.transform(this.periods[ this.isIndicePeriodos[ 0 ] ], DATE_FORMAT_PERIOD_ES_HEADER_CORTA);
                        this.fechaPeriodo1Str = '';
                    }
                }
            }
        }
        if(!isNullOrUndefined(this.fechaPeriodoStr)) {
            this.fechaPeriodoStr = this.fechaPeriodoStr.replace('.', '');
        }
        if(!isNullOrUndefined(this.fechaPeriodo1Str)) {
            this.fechaPeriodo1Str = this.fechaPeriodo1Str.replace('.', '');
        }
        this.valPeriodoDesgloseService.findByBusquedaValores(this.codCliente, this.codTipoDesglose, this.codSubTipoDesglose, this.codMoneda, this.codTipoPlantilla).subscribe(
            (response: HttpResponse<IValPeriodoDesglose[]>) => {
                response.body.forEach((datos, indice) => {
                    this.rows.forEach((concept, index) => {
                        if (datos.codTipoConcep === concept.codTipoConcep && datos.corConcepto.toString() === concept.corConcepto) {
                            if (this.datePipe.transform(datos.fecPeriodo, DATE_FORMAT_PERIOD_ES_UI_HEADER) === this.fechaPeriodo) {
                                this.rows[ index ].prc = !isNullOrUndefined(datos.valPrcConcep) && datos.valPrcConcep!==0  ? Math.round(datos.valPrcConcep * 100) : '';
                            }
                            if (this.datePipe.transform(datos.fecPeriodo, DATE_FORMAT_PERIOD_ES_UI_HEADER) === this.fechaPeriodo1) {
                                this.rows[ index ].prc1 = !isNullOrUndefined(datos.valPrcConcep) && datos.valPrcConcep!==0 ? Math.round(datos.valPrcConcep * 100) : '';
                            }
                        }
                    });
                });
            },
            (response: HttpErrorResponse) => this.onError(response.message)
        );
        this.rows.forEach((concept, index) => {
            this.selectedAnterior.forEach((concepto, indice) => {
                if (this.rows[ index ].corConcepto === concepto.corConcepto) {
                    this.selected.push(this.rows[ index ]);
                }
            });
        });
    }

    onSelectTableItem() {
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    onClickCancel() {
        this.closeModal();
    }

    onActivate(event) {

    }

    onClickSaveOtherDataConfig() {
        this.saveOtherDataConfigEmit.emit(
            {
                'conceptos': this.selected,
                'observacion': this.observations,
            });
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    onSelect({ selected }) {
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }

    showSecondPeriodColumn() {
        return this.fechaPeriodo1 !== 'undefined' && this.fechaPeriodo1 !== null && this.fechaPeriodo1 !== undefined;
    }

    onKey(event) {
        if(event.target.value.length <= this.MaxCaracteres) {
            this.contador = this.MaxCaracteres - event.target.value.length;
            if(this.contador < 0 ) {
                this.checkCaractOther = false;
            } else {
                this.checkCaractOther = true;
            }
        } else {
            this.contador = 0;
            this.checkCaractOther = false;
        }
    }
}
