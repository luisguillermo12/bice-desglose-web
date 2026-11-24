import { Component, ElementRef, EventEmitter, OnInit, ViewChild } from '@angular/core';
import {
    COD_DEFAULT_TIPO_CAMBIO,
    CODTABLA_SUBTIPO_DESGLOSE,
    CODTABLA_TIPO_DESGLOSE,
    CODTABLA_TIPO_MONEDAS,
    COLUMN_NUMBER_CCC,
    COLUMN_NUMBER_COD_CONCEPT,
    COLUMN_NUMBER_COD_TIPO_REGIS,
    COLUMN_NUMBER_FLG_IS_TOTAL,
    DEFLACTOR_BASE_DEFAULT_VALUE,
    HANDS_ON_TABLE_CUSTOM_CULTURE,
    HANDS_ON_TABLE_LICENSE_KEY,
    HANDS_ON_TABLE_NUMERIC_PATTERN_3,
    HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN,
    MULTI_REGISTRY,
    SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES
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
import { DesgloseService } from 'app/entities/desglose';
import { Moment } from 'moment';
import { DATE_FORMAT_PERIOD_ES_UI_HEADER } from 'app/shared';
import { DatePipe } from '@angular/common';
import { HotTableRegisterer } from '@handsontable/angular';

/* function headerTotalNumericRendererPrint(instance, td, row, col, prop, value, cellProperties) {
    Handsontable.renderers.NumericRenderer.apply(this, arguments);
    td.className = CSS_CLASS_HEADER_TOTAL_NUMBER_RENDERER;
} */
function validNumeric(value, callback) {
    if (!isNaN(Number(value))) {
        callback(true);
    } else {
        callback(false);
    }
}
function hotUpdateSettings(hotInstance: any, lectura:boolean) {
    const FLG_IS_TOTAL_1 = 1;
    const btnEdit1: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit1');
    const btnEdit2: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit2');
    const btnEdit3: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit3');
    const btnEdit4: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit4');
    hotInstance.updateSettings({
        cells: (row, col) => {
            const cellProperties: any = {};
            // Si no tiene un valor es porque es un encabezado de grupo
            if (row === 0) {
                    cellProperties.readOnly = lectura;
            }
            if(row === 1) {
                cellProperties.readOnly = !lectura;
            }
            return cellProperties;
        },
    });
}
function hotUpdateSettings1(hotInstance: any, lectura:boolean) {
    const FLG_IS_TOTAL_1 = 1;
    const btnEdit1: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit1');
    const btnEdit2: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit2');
    const btnEdit3: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit3');
    const btnEdit4: HTMLButtonElement = <HTMLButtonElement>document.getElementById('buttonEdit4');
    hotInstance.updateSettings({
        cells: (row, col) => {
            const cellProperties: any = {};
            // Si no tiene un valor es porque es un encabezado de grupo
            if (row === 1) {
                cellProperties.readOnly = lectura;
            }
            return cellProperties;
        },
    });
}

@Component({
    selector: 'jhi-breakdown-generate-print-dialog',
    templateUrl: './breakdown-generate-print-dialog.component.html',
    styleUrls: [ './breakdown-generate-print-dialog.scss' ]
})
export class BreakdownGeneratePrintDialogComponent implements OnInit {
    state: { state: string, stateClass: string } = { state: '', stateClass: '' };
    breakdown: IDesglose = new Desglose();
    currentAccount: any;
    sort = 'codGenerico,ASC';
    deflactorBase?: number;
    deflactorBaseT?: number;
    fecha?: Moment;
    codMoneda?: string;
    simpleComboBoxTypes: IDescripcionCodigo[] = [];
    CODTABLA_TIPO_DESGLOSE = CODTABLA_TIPO_DESGLOSE;
    CODTABLA_TIPO_MONEDAS = CODTABLA_TIPO_MONEDAS;
    CODTABLA_SUBTIPO_DESGLOSE = CODTABLA_SUBTIPO_DESGLOSE;
    @ViewChild('fieldBreakdownDeflactorBase') fieldBreakdownDeflactorBase: ElementRef;
    public descripMoneda?: IDescripcionCodigo;
    fechaPeriodo: Moment[] = [];
    listaFecPerido: string[] = [];
    newData1: any[];
    newData2: any[];
    newData2Aux: any[];
    public listDeflatorPeriod?: number[];
    public listDeflatorPeriodTemp?: number[] = [];
    public listDeflatorPeriodTempAux?: number[] = [];
    public indicesPeriod?: number[];
    public triggerOnSelectItemEventEmitter: EventEmitter<any> = new EventEmitter();
    handOnInstanceId = 'handOnInstanceIdDetails';
    // Datos de la tabla
    colHeaders: string[];
    columns: any[];
    options: any;
    data: any[];
    checkedImpresion = false;
    public HANDS_ON_TABLE_LICENSE_KEY = HANDS_ON_TABLE_LICENSE_KEY;

    constructor(public activeModal: NgbActiveModal,
                private jhiAlertService: JhiAlertService,
                private principal: Principal,
                private sessionStorage: SessionStorageService,
                private descripcionCodigoService: DescripcionCodigoService,
                private hotRegisterer: HotTableRegisterer,
                private filterDescripcionCodigoPipe: FilterDescripcionCodigoPipe,
                private translateService: TranslateService, private desgloseService: DesgloseService, private datePipe: DatePipe) {
    }

    ngOnInit() {
        const allComboBoxData = this.sessionStorage.retrieve(SESSION_VARIABLE_SIMPLE_COMBOBOX_TYPES);
        if (!isNullOrUndefined(allComboBoxData) && allComboBoxData.length > 0) {
            this.simpleComboBoxTypes = allComboBoxData;
        } else {
            const typesParam = this.CODTABLA_SUBTIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_DESGLOSE + ',' + this.CODTABLA_TIPO_MONEDAS;
            this.loadIDescripcionCodigo(typesParam, this.sort);
        }
        let i = 0;
        this.fechaPeriodo.forEach((elem, j) => {
            if (!isNullOrUndefined(elem)) {
                this.indicesPeriod.forEach(el => {
                    if (j === el) {
                        this.listaFecPerido[ i ] = this.datePipe.transform(elem, DATE_FORMAT_PERIOD_ES_UI_HEADER);
                        i++;
                    }
                });
            }
        });

        if (!isNullOrUndefined(this.listDeflatorPeriod) && this.listDeflatorPeriod.length > 0) {
            this.data = [];
            this.newData1 = new Array(5);
            this.newData2 = new Array(5);
            let c = 0;
            for (let y = 0; y < this.listDeflatorPeriod.length; y++) {
                this.newData1 [ 0 ] = this.translateService.instant('breakdown.printConfig.deflactor');
                this.newData2 [ 0 ] = this.translateService.instant('breakdown.printConfig.tipoCambio');
                this.indicesPeriod.forEach(ind => {
                    if (y === ind) {
                        if (!isNullOrUndefined(this.listDeflatorPeriod [ y ])) {
                            this.listDeflatorPeriodTemp.push(this.listDeflatorPeriod [ y ]);
                            this.newData1 [ c + 1 ] = this.listDeflatorPeriod [ y ];
                            this.newData2 [ c + 1 ] = COD_DEFAULT_TIPO_CAMBIO;
                            c++;
                        }
                    }
                });
            }
            for (let n = 0; n < 4; n++) {
                const indFecPeriodo = this.listaFecPerido.length - 1;
                if (n > indFecPeriodo) {
                    this.listaFecPerido[ n ] = '';
                }
            }
        }
        this.data.push(this.newData1);
        this.data.push(this.newData2);

        this.colHeaders = [ '' ].concat(this.listaFecPerido);
        this.columns = [ { type: 'text' } ];
        // Agrego el formato a todas las columnas de período
        for (let j = 0; j < this.listaFecPerido.length; j++) {
            this.columns.push({
                type: 'numeric',
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE // this is the default culture, set up for USD
                },
                validator: validNumeric, allowInvalid: false
            });
        }
        this.options = {
            // set Spanish language
            language: 'es-MX',
            height: 150,
            colHeaders: true,
            rowHeaders: true,
            stretchH: 'all',
            colWidths: [ '80%', '50%', '50%', '50%', '50%' ],
            autoColumnSize: false,
            columnSorting: false,
            /*allowInsertRow: true,
            allowRemoveRow: true,
            contextMenu: [
                'row_above', 'row_below', 'remove_row'
            ]*/
        };

        setTimeout(() => {
            this.onBreakdownSubTypeChange();
        }, 1000);
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

        for (let l = 1; l < this.data[0].length; l++) {
        this.listDeflatorPeriodTempAux.push(this.data[0][l]);
        }
        console.log('data hason table',this.data);
        /*for (let k = 1; k < this.data[1].length; k++) {
            this.newData2Aux[k]=this.data[1][k];
            }*/

        
        this.triggerOnSelectItemEventEmitter.emit({
            'deflactores': this.listDeflatorPeriodTempAux,
            'fechas': this.listaFecPerido,
            'tiposCambio': this.newData2,
            'monedaImpreso': this.codMoneda,
            'deflactorBase': this.deflactorBase,
            'checkedImpresion': this.checkedImpresion
        });
        console.log('deflactores' , this.listDeflatorPeriodTemp);
        console.log('deflactores modificados' , this.listDeflatorPeriodTempAux);
        console.log('tipo de cambio' , this.newData2);
        console.log('tipo de cambio modificado' , this.newData2);
        console.log('modeda de impresion',this.codMoneda);
        console.log(this.triggerOnSelectItemEventEmitter);
        this.closeModal();
    }

    private onError(errorMessage: string) {
    }

    onBreakdownSubTypeChange() {
        if (!isNullOrUndefined(this.codMoneda) && !isNullOrUndefined(this.descripMoneda.codGenerico)) {
            this.fieldBreakdownDeflactorBase.nativeElement.disabled = this.codMoneda !== this.descripMoneda.codGenerico;
            if(this.codMoneda !== this.descripMoneda.codGenerico) {
                this.deflactorBase = DEFLACTOR_BASE_DEFAULT_VALUE;
                this.listaFecPerido.forEach((elem, j) => {
                    if(!isNullOrUndefined(elem) && elem!=='') {
                        this.data[0][j+1] = 1;
                    }
                });
            } else {
                this.deflactorBase = this.deflactorBaseT;
                this.data[0][1] = this.listDeflatorPeriodTemp[0];
                this.data[0][2] = this.listDeflatorPeriodTemp[1];
                this.data[0][3] = this.listDeflatorPeriodTemp[2];
                this.data[0][4] = this.listDeflatorPeriodTemp[3];
            }
            const hotInstance = this.hotRegisterer.getInstance(this.handOnInstanceId);
            if (hotInstance) {
                hotUpdateSettings(hotInstance, this.codMoneda !== this.descripMoneda.codGenerico);
            }
            hotInstance.render();
        }
        this.onClickSearchBreakdown();
    }

    onClickSearchBreakdown() {
    }

    afterLoadDataMethodGeneratePrint(hotInstancePrint: any) {
        /* try {
            const localData: any = hotInstancePrint.getSourceData();
            if (!isNullOrUndefined(localData) && localData.length > 0) {
                hotInstancePrint.updateSettings({
                    cells: (row, col) => {
                        const cellProperties: any = {};
                        cellProperties.type = 'numeric';
                        cellProperties.forceNumeric = true;
                        cellProperties.format = '0,0.00';
                        cellProperties.language = 'es-CL';
                        cellProperties.renderer = headerTotalNumericRendererPrint;
                        return cellProperties;
                    },
               });
            }
        } catch (e) {
            console.log(e);
        } */
    }
}
