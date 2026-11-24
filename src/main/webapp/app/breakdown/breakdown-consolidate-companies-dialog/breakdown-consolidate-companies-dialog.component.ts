import { AfterViewInit, Component, EventEmitter, OnInit } from '@angular/core';
import { DATE_FORMAT, DATE_FORMAT_PERIOD_ES_HEADER_CORTA, DATE_FORMAT_PERIOD_ES_UI_HEADER } from 'app/shared';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { DesgloseService } from 'app/entities/desglose';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { ActivatedRoute, Router } from '@angular/router';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { CustomSqlService } from 'app/shared/services/custom-sql.service';
import { ValidationUtils } from 'app/shared/util/validation-utils';
import { isNullOrUndefined } from 'util';
import { SessionStorageService } from 'ngx-webstorage';
import { TranslateService } from '@ngx-translate/core';
import { IEmpresaConsolidada } from 'app/shared/model/empresa-consolidada.model';
import 'handsontable/languages/es-MX';
import { Moment } from 'moment';
import { IBreakdownSearchResponseDTO } from 'app/entities/desglose/search-breakdown-response.model';
import { IEmprConsolPeriodoDesgConsol } from 'app/shared/model/empr-consol-periodo-desg-consol.model';
import {
    COL_NUMBER_COMPANY_NAME, CSS_CLASS_HEREDO, CSS_CLASS_ZERO,
    HANDS_ON_TABLE_CUSTOM_CULTURE,
    HANDS_ON_TABLE_LICENSE_KEY,
    HANDS_ON_TABLE_NUMERIC_PATTERN, HANDS_ON_TABLE_NUMERIC_PATTERN_3,
    HANDS_ON_TABLE_NUMERIC_PERCENT_PATTERN, INFINITY, INFINITY_STR
} from 'app/app.constants';
import { DatePipe } from '@angular/common';
import Handsontable from 'handsontable';
import { forEach } from '@angular/router/src/utils/collection';

function validNumeric(value, callback) {
    if (!isNaN(Number(value))) {
        callback(true);
    } else {
        callback(false);
    }
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
@Component({
    selector: 'jhi-breakdown-consolidate-companies-dialog',
    templateUrl: './breakdown-consolidate-companies-dialog.component.html',
    styleUrls: [ './breakdown-consolidate-companies-dialog.scss' ]
})
export class BreakdownConsolidateCompaniesDialogComponent implements OnInit, AfterViewInit {

    public onClickButtonSave: EventEmitter<IEmpresaConsolidada[]> = new EventEmitter();
    currentAccount: any;
    private hotInstance: Handsontable;
    listConsolidateCompSearchResponseDTO: IBreakdownSearchResponseDTO[] = [];

    // Datos de la tabla
    colHeaders: string[];
    colWidths:number[];
    columns: any[];
    options: Handsontable.GridSettings = {
        language: 'es-MX', // set Spanish language
        height: 396,
        colHeaders: true,
        rowHeaders: true,
        stretchH: 'none',
        columnSorting: false,
        formulas: true,
        allowInsertRow: true,
        allowRemoveRow: true,
        // contextMenu: [
        //     'row_above', 'row_below', 'remove_row'
        // ],
        minSpareRows: 1,// always add 3 empty rows at the table end
        fixedColumnsLeft: 1,
        // class: 'hot',
        // bindRowsWithHeaders: true,
    };
    numPeriodosConInfo = 0;
    fechaPeriodo: Moment[] = [];
    listaFecPerido: string[] = [];
    data: any = [ [] ];
    isFormDataValid = true;
    ultimoPeriodo: number;
    dataConsolidateCompanies: any[] = [];
    /**
     * Unifica los datos de empresaConsolidadas y periodoDesgConsols
     */
    public empConsolPeriodoDesgConsolList: IEmprConsolPeriodoDesgConsol[] = [];
    public HANDS_ON_TABLE_LICENSE_KEY = HANDS_ON_TABLE_LICENSE_KEY;

    constructor(public activeModal: NgbActiveModal, private desgloseService: DesgloseService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService,
                private principal: Principal, private activatedRoute: ActivatedRoute, private router: Router, private descripcionCodigoService: DescripcionCodigoService,
                private customSqlService: CustomSqlService, private sessionStorage: SessionStorageService, private validationUtils: ValidationUtils,
                private translateService: TranslateService,private datePipe: DatePipe) {

    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.fechaPeriodo.forEach((elem, i) => {
            if (!isNullOrUndefined(elem)) {
                this.listaFecPerido.push(this.datePipe.transform(elem, DATE_FORMAT_PERIOD_ES_HEADER_CORTA));
            }
        });
        this.colHeaders = [ this.translateService.instant('breakdown.consolidated.company') ].concat(this.listaFecPerido);
        this.colWidths = [520];
        this.columns = [ { type: 'text' }];
        for (let i = 0; i < this.listaFecPerido.length; i++) {
            this.columns.push({
                type: 'numeric',
                numericFormat: {
                    pattern: HANDS_ON_TABLE_NUMERIC_PATTERN_3,
                    culture: HANDS_ON_TABLE_CUSTOM_CULTURE // this is the default culture, set up for USD
                },
                renderer: 'zeroValueRenderer',
                validator: validNumeric, allowInvalid: false
            });
            this.colWidths.push(100);
        }
        this.data = [];
        this.empConsolPeriodoDesgConsolList.forEach(
            (companyData, rowIndex) => {
                let contador = 0;
                for(let i = 0;i<this.data.length;i++) {
                    if(this.data[i][COL_NUMBER_COMPANY_NAME] === companyData.nomEmpresa.trim()) {
                        contador++;
                    }
                }
                if(contador === 0) {
                    this.data.push([companyData.nomEmpresa.trim(),null,null,null,null]);
                }
            });
        // Después que las columnas estén cargadas
        if (!isNullOrUndefined(this.empConsolPeriodoDesgConsolList) && this.empConsolPeriodoDesgConsolList.length > 0) { // Cargar los datos de los períodos
            this.colHeaders.forEach((columnName, i) => {
                if (i > 0) { // Si es una columna de período
                    // filtro la data por el período y asigno los valores
                    this.empConsolPeriodoDesgConsolList.filter(el => !isNullOrUndefined(el.fecPeriodo)
                        && this.datePipe.transform(el.fecPeriodo, DATE_FORMAT_PERIOD_ES_HEADER_CORTA) === columnName).forEach(
                        (companyData, rowIndex) => {
                            for(let j = 0;j<this.data.length;j++) {
                                if (companyData.nomEmpresa.trim() === this.data[ j ][ COL_NUMBER_COMPANY_NAME ]) {
                                    this.data[ j ][ i ] = companyData.valPrcConsol;
                                    if(this.data[ j ][ i ] !== 0) {
                                        this.ultimoPeriodo = i;
                                    }
                                }
                            }
                        });
                }
            });
        }
        setTimeout(() => {
            this.hotInstance.render();
            this.hotInstance.selectCell(1, this.ultimoPeriodo);
        }, 200);

    }

    ngAfterViewInit(): void {
        this.hideEvaluationMessage();
    }

    /**
     * Ocultar/eliminar información de licencia del componente.
     */
    private hideEvaluationMessage() {
        const divElement: HTMLElement = document.getElementById('hot-display-license-info');
        if (!isNullOrUndefined(divElement)) {
            divElement.remove();
        }
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    triggerOnSelectItemEvent(items) {
        this.onClickButtonSave.emit(this.data);
    }

    onClickCancel() {
        this.closeModal();
    }

    onClickSaveConsolidatedCompanies() {
        if (this.hotInstance) {
            this.data = this.hotInstance.getData();
            this.triggerOnSelectItemEvent(this.data);
        }
    }

    onClickNewRow() {
        this.hotInstance.alter('insert_row', this.data.length);
    }

    public afterInitConsolidateCompanies = hotInstance => {
        if (!isNullOrUndefined(hotInstance)) {
            this.hotInstance = hotInstance;
        }
    };
}
