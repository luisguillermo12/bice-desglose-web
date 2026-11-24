import { AfterViewInit, Component, ElementRef, Input, OnDestroy, OnInit, Renderer, ViewChild } from '@angular/core';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs';
import { JhiAlertService, JhiEventManager, JhiParseLinks } from 'ng-jhipster';

import {Concepto, IConcepto} from 'app/shared/model/concepto.model';
import { Principal } from 'app/core';
import { ConceptoService } from './concepto.service';
import { CODTABLA_TIPO_PLANTILLAS_DESGLOSE, CODTABLA_TIPO_PLANTILLAS_PROYECCION, NOT_LIKE_EXPRESSION_ONLY_FATHERS, VALUE_IS_ACTIVE_CONCEPT_TRUE } from 'app/app.constants';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { isNullOrUndefined } from 'util';
import { DatatableComponent } from '@swimlane/ngx-datatable';
import { ConceptRequestDTO, IConceptRequestDTO } from 'app/entities/concepto/filter-concept-request.model';
import { Sweetalert2Component } from 'app/shared';
import {IBreakdownValidationError} from 'app/entities/desglose/breakdown-validation-error.model';
import {BreakdownErrorsDialogComponent} from 'app/breakdown/breakdown-errors-dialog/breakdown-errors-dialog.component';
import {NgbModal, NgbModalRef} from '@ng-bootstrap/ng-bootstrap';

@Component({
    selector: 'jhi-concepto',
    templateUrl: './concepto.component.html'
})
export class ConceptoComponent implements OnInit, OnDestroy, AfterViewInit {
    currentAccount: any;
    conceptos: IConcepto[];
    conceptosT: IConcepto[];
    error: any;
    success: any;
    eventSubscriber: Subscription;
    routeData: any;
    pagina: any = 0;
    private idCodPlantilla = 0;
    listaPlantillas: IDescripcionCodigo[];
    expanded: any = {};
    @ViewChild(DatatableComponent) table: DatatableComponent;
    private notLikeExpression = NOT_LIKE_EXPRESSION_ONLY_FATHERS;
    sort = 'codGenerico,ASC';
    private questionMessageSubscription: Subscription;
    @Input() isProyec? = false;
    private ngbModalRefErrors: NgbModalRef;
    constructor(private conceptoService: ConceptoService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService, private principal: Principal,
                private activatedRoute: ActivatedRoute, private router: Router, private eventManager: JhiEventManager, private descripcionCodigoService: DescripcionCodigoService,
                private renderer: Renderer,private modalService:NgbModal, private elementRef: ElementRef,private sweetAlert2: Sweetalert2Component) {
    }

    loadAll(templateCode: number) {
        const conceptRequestDTO: IConceptRequestDTO = new ConceptRequestDTO(templateCode, this.notLikeExpression);
        this.conceptoService.resecuenciar(this.idCodPlantilla).subscribe(
            (datos: HttpResponse<any>) => {
                this.conceptoService.queryConcepts(conceptRequestDTO).subscribe(
                    (res: HttpResponse<IConcepto[]>) => {
                        this.conceptos = res.body;
                        this.conceptosT = res.body;
                        this.updateFilter(null);
                        this.ngAfterViewInit();
                        this.conceptoService.validationsAll(new Concepto(this.idCodPlantilla)).subscribe((data:HttpResponse<IBreakdownValidationError[]>)=> {
                            this.displayValidationErrors(data.body);
                        },(data:HttpErrorResponse)=> this.onError(data.message));
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            },
            (datos: HttpErrorResponse) => this.onError(datos.message)
        );
    }

    onChange(id) {
        this.idCodPlantilla = id;
        this.loadAll(this.idCodPlantilla);
    }

    ngOnInit() {
        if (isNullOrUndefined(this.listaPlantillas)) {
            let typesParam;
            if(!this.isProyec) {
                typesParam = CODTABLA_TIPO_PLANTILLAS_DESGLOSE;
            } else {
                typesParam = CODTABLA_TIPO_PLANTILLAS_PROYECCION;
            }
            this.descripcionCodigoService.findByCodTablasIn(typesParam, this.sort).subscribe(
                (data: HttpResponse<IDescripcionCodigo[]>) => {
                    this.listaPlantillas = data.body;
                    this.idCodPlantilla = Number(this.listaPlantillas[0].codGenerico);
                    this.loadAll(Number(this.listaPlantillas[0].codGenerico));
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        }

        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.registerChangeInConceptos();
    }

    ngAfterViewInit() {
        setTimeout(() => this.renderer.invokeElementMethod(this.elementRef.nativeElement.querySelector('#conceptTypeFilter'), 'focus', []), 10);
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConceptos() {
        this.eventSubscriber = this.eventManager.subscribe('conceptoListModification', response => this.loadAll(this.idCodPlantilla));
    }

    updateFilter(event) {
        const elementById: any = document.getElementById('conceptTypeFilter');
        const val = !isNullOrUndefined(elementById) && !isNullOrUndefined(elementById.value) ? elementById.value.toLowerCase() : '';
        // filter our data
        const temp = this.conceptosT.filter(d => {
            return d.codTipoConcep.toLowerCase().indexOf(val) !== -1 || !val;
        });
        // update the rows
        this.conceptos = temp;
        // Whenever the filter changes, always go back to the first page
        this.table.offset = this.pagina;
    }
    loadPage(pageEvent = {offset: 0}) {
       this.pagina = pageEvent.offset;
    }
    toggleExpandRow(row) {
        const concepto: IConcepto = row;
        this.conceptoService.queryTipoPlantillalike(this.idCodPlantilla, concepto.codTipoConcep, concepto.codConcepto, concepto.flgConActivo).subscribe(
            (data: HttpResponse<IConcepto[]>) => {
                row.concepto = data.body;
                this.table.rowDetail.toggleExpandRow(row);
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );
    }
    resecuenciar() {
        this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
            'desgloseApp.concepto.confirmation',
            'desgloseApp.concepto.resencuenciar',
            'question',
            'breakdown.yes',
            'breakdown.no'
        ).subscribe(res => {
            if (res) {

                this.conceptoService.resecuenciar(this.idCodPlantilla).subscribe(
                    (data: HttpResponse<any>) => { },
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            }
            this.questionMessageSubscription.unsubscribe();
        });

    }

    onDetailToggle(event) {
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
    private displayValidationErrors(data: IBreakdownValidationError[], optionalQuestionMessage?: string) {
        if (!isNullOrUndefined(data) && data.length> 0) {
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
            this.ngbModalRefErrors.componentInstance.mostrarColumnas = false;
            if (isNullOrUndefined(optionalQuestionMessage)) {
                optionalQuestionMessage = 'breakdown.errors.questionMessageSave';
            }
            this.ngbModalRefErrors.componentInstance.questionMessage = optionalQuestionMessage;

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
}
