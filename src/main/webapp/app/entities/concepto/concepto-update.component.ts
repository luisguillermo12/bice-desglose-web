import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IConcepto } from 'app/shared/model/concepto.model';
import { ConceptoService } from './concepto.service';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { CODTABLA_TIPO_PLANTILLAS_DESGLOSE, CODTABLA_TIPO_PLANTILLAS_PROYECCION, CODTABLA_TIPO_REGISTRO } from 'app/app.constants';
import { JhiAlertService, JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { isNullOrUndefined } from 'util';
import {IBreakdownValidationError} from 'app/entities/desglose/breakdown-validation-error.model';
import {BreakdownErrorsDialogComponent} from 'app/breakdown/breakdown-errors-dialog/breakdown-errors-dialog.component';
import * as jQuery from 'jquery';
@Component({
    selector: 'jhi-concepto-update',
    templateUrl: './concepto-update.component.html',
    styleUrls: [ './concepto-update.component.scss' ]
})
export class ConceptoUpdateComponent implements OnInit {
    concepto: IConcepto;
    isSaving: boolean;
    simpleComboBoxTypes: IDescripcionCodigo[];
    sort = 'codGenerico,ASC';
    CODTABLA_TIPO_PLANTILLAS_DESGLOSE = CODTABLA_TIPO_PLANTILLAS_DESGLOSE;
    CODTABLA_TIPO_REGISTRO = CODTABLA_TIPO_REGISTRO;
    CODTABLA_TIPO_PLANTILLAS_PROYECCION = CODTABLA_TIPO_PLANTILLAS_PROYECCION;
    @Input() isProyec? = false;
    private ngbModalRefErrors: NgbModalRef;
    constructor(private router: Router,private route: ActivatedRoute, private modalService: NgbModal,private conceptoService: ConceptoService, private activatedRoute: ActivatedRoute, private descripcionCodigoService: DescripcionCodigoService,
                private jhiAlertService: JhiAlertService, public activeModal: NgbActiveModal, private eventManager: JhiEventManager) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    ngOnInit() {
        let typesParam = this.CODTABLA_TIPO_PLANTILLAS_DESGLOSE + ',' + this.CODTABLA_TIPO_REGISTRO;
        this.route.queryParams.subscribe(params => {
            this.isProyec =  Boolean(JSON.parse(params['isProyec']));
            if(this.isProyec) {
                typesParam = this.CODTABLA_TIPO_REGISTRO + ',' + CODTABLA_TIPO_PLANTILLAS_PROYECCION;
            }
        });
        this.isSaving = false;
        this.descripcionCodigoService.findByCodTablasIn(typesParam, this.sort).subscribe(
            (data: HttpResponse<IDescripcionCodigo[]>) => {
                this.simpleComboBoxTypes = data.body;
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );

        if (!isNullOrUndefined(this.concepto.corConcepto)) {
             jQuery('#field_tipoPlantilla').prop('disabled', true);
             jQuery('#field_codTipoConcep').prop('disabled', true);
             jQuery('#field_codConcepto').prop('disabled', true);
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    previousState() {
        window.history.back();
    }

    save() {

        this.concepto.flgIsTotal = Number(this.concepto.flgIsTotal);
        this.concepto.flgUserModif = Number(this.concepto.flgUserModif);
        this.concepto.flgConActivo = Number(this.concepto.flgConActivo);
        this.concepto.numDecimales = Number(this.concepto.numDecimales);
        this.concepto.flgHaceAjust = Number(this.concepto.flgHaceAjust);
        this.concepto.flgIsBold = Number(this.concepto.flgIsBold);
        this.concepto.flgIsUcase = Number(this.concepto.flgIsUcase);
        this.concepto.flgIsEnclosed = Number(this.concepto.flgIsEnclosed);
        this.concepto.flgIsPrinted = Number(this.concepto.flgIsPrinted);
        this.concepto.numDecPrint = Number(this.concepto.numDecPrint);
        this.concepto.flgNonegativ = Number(this.concepto.flgNonegativ);
        this.concepto.flgAfterspace = Number(this.concepto.flgAfterspace);
        if ( !isNullOrUndefined(this.concepto.corConcepto) /*|| this.concepto.corConcepto !== 0*/ ) {
            this.conceptoService.validations(this.concepto).subscribe((res:HttpResponse<IBreakdownValidationError[]>) => {
                this.displayValidationErrors(res.body,'breakdown.errors.questionMessageSave');
                if(res.body.length === 0) {
                    this.isSaving = true;
                    this.subscribeToSaveResponse(this.conceptoService.update(this.concepto));
                }
            }, () => {});

        } else {
            this.concepto.corConcepto = 0;
            this.conceptoService.validations(this.concepto).subscribe((res:HttpResponse<IBreakdownValidationError[]>) => {
                this.displayValidationErrors(res.body,'breakdown.errors.questionMessageSave');
                if(res.body.length === 0) {
                    this.subscribeToSaveResponse(this.conceptoService.create(this.concepto));
                } else {
                    this.concepto.corConcepto = null;
                }
            }, () => {});
        }
    }
    private subscribeToSaveResponse(result: Observable<HttpResponse<IConcepto>>) {
        result.subscribe((res: HttpResponse<IConcepto>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    private onSaveSuccess() {
        this.isSaving = false;
        this.eventManager.broadcast({ name: 'conceptoListModification', content: 'OK' });
        this.activeModal.dismiss(true);
        this.concepto.corConcepto = null;
    }

    private onSaveError() {
        this.isSaving = false;
        this.concepto.corConcepto = null;
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

@Component({
    selector: 'jhi-concepto-create-popup',
    template: ''
})
export class ConceptoUpdatePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {
    }

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ concepto }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(ConceptoUpdateComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.concepto = concepto;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([ { outlets: { popup: null } } ], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }

}
