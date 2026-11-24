import { Component, EventEmitter, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { Subscription } from 'rxjs';
import { DATE_FORMAT_CREATE_MOMENT_OBJECT, DATE_TIME_FORMAT, Sweetalert2Component } from 'app/shared';
import { TurbineUserService } from 'app/entities/turbine-user';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { JhiAlertService } from 'ng-jhipster';
import { DesgloseRevisionService } from 'app/entities/desglose-revision';
import * as moment from 'moment';
import {
    COMENTARIO_AUDITORIA_CAMBIO_ESTADO_BORRADOR_OBSERVACION,
    COMENTARIO_AUDITORIA_CAMBIO_ESTADO_BORRADOR_TERMINADO,
    ESTADO_BORRADOR_OBSERVACION,
    ESTADO_BORRADOR_TERMINADO,
    EVENTO_AUDITORIA_CAMBIO_ESTADO,
    INICIALIZE_CHECKOTHER_CARACTER,
    MAX_CANT_CONTADOR_ENVREV,
    MAXCARACTERESVALIDATION,
    INICIALIZE_CONTADOR_ENVREV
} from 'app/app.constants';
import { DesgloseService } from 'app/entities/desglose';
import { IDesglose } from 'app/shared/model/desglose.model';
import { Router } from '@angular/router';
import { DesgloseMensaje, IDesgloseMensaje } from 'app/shared/model/desglose-mensaje.model';
import { DesgloseAuditoria, IDesgloseAuditoria } from 'app/shared/model/desglose-auditoria.model';
import { DesgloseRevision, IDesgloseRevision } from 'app/shared/model/desglose-revision.model';
import { ISfStfOperator } from 'app/shared/model/sf-stf-operator.model';
import { SfStfOperatorService } from 'app/entities/sf-stf-operator';
import { BreakdownSendRequest, IBreakdownSendRequest } from 'app/entities/desglose/send-breakdown-request.model';
import { IExtraDataDesglose } from 'app/shared/model/extra-data-desglose.model';
import { isNullOrUndefined } from 'util';

@Component({
    selector: 'jhi-breakdown-shipping-review',
    templateUrl: './breakdown-shipping-review.component.html',
    styleUrls: [ './breakdown-shipping-review.component.scss' ]
})
export class BreakdownShippingReviewComponent implements OnInit {
    private ngbModalMessages: NgbModalRef;
    hayNotificacion = false;
    codeAnalista: number;
    breakdown: IDesglose;
    extraDataDesglose: IExtraDataDesglose;
    commentary: string;
    isEnviarRevisor = true;
    nameCliente: string;
    codeCliente: string;
    codeUser: string;
    roleUser: string;
    codeRevisor: string;
    nameRevisor: string;
    mensajeEnvio: string;
    codeEnviaRevision:number;
    mensajeSoloLectura: IDesgloseMensaje[];
    listaAnalistas: ISfStfOperator[];
    public enviarShippingReviewEmit: EventEmitter<any> = new EventEmitter();
    public devolverShippingEmit: EventEmitter<any> = new EventEmitter();
    private questionMessageSubscription: Subscription;
    public contador = INICIALIZE_CONTADOR_ENVREV;
    public MaxCaracteres = MAX_CANT_CONTADOR_ENVREV;
    public checkCaractOther = INICIALIZE_CHECKOTHER_CARACTER;
    public caractValidationMessage = MAXCARACTERESVALIDATION;

    constructor(public activeModal: NgbActiveModal,
                private sweetAlert2: Sweetalert2Component,
                private turbineUserService: TurbineUserService,
                private jhiAlertService: JhiAlertService,
                private desgloseRevisionService: DesgloseRevisionService,
                private desgloseService: DesgloseService,
                private modalService: NgbModal,
                private router: Router,
                private sfStfOperatorService: SfStfOperatorService) {
    }

    ngOnInit() {
        this.commentary = this.mensajeEnvio;
        if (this.isEnviarRevisor) {
            this.turbineUserService.findByRole(this.codeRevisor).subscribe(
                (data: HttpResponse<ITurbineUsuarios>) => {
                    this.nameRevisor = data.body[ 0 ].nombre;
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        } else {
            this.sfStfOperatorService.listarAnalistas().subscribe(
                (data: HttpResponse<ISfStfOperator[]>) => {
                    this.listaAnalistas = data.body;
                    this.listaAnalistas.forEach((stf,index)=> {
                        if(!isNullOrUndefined(this.codeEnviaRevision)) {
                            if (stf.operatorCode === this.codeEnviaRevision) {
                                this.codeAnalista = stf.operatorCode;
                            }
                        }
                    });
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        }
    }

    onClickCancel() {
        this.closeModal();
    }

    onClickEnviar() {
        if (this.isEnviarRevisor) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.shipping.sendMessage',
                'question',
                'breakdown.yes',
                'breakdown.no',
                {
                    revisor: this.nameRevisor
                }
            ).subscribe(res => {
                if (res) {
                    this.enviarDesgloseParaRevision(this.commentary);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        }
        this.closeModal();
    }

    onClickDevolver() {
        const input = <HTMLSelectElement>document.getElementById('field_executer');
        if (!this.isEnviarRevisor) {
            this.questionMessageSubscription = this.sweetAlert2.sweetAlertQuestionMessage(
                'breakdown.confirmation',
                'breakdown.shipping.devolverMessage',
                'question',
                'breakdown.yes',
                'breakdown.no',
                {
                    ejecutor: input.options[ input.selectedIndex ].text
                }
            ).subscribe(res => {
                if (res) {
                    this.devolverDesglose(this.commentary);
                }
                this.questionMessageSubscription.unsubscribe();
            });
        }
        this.closeModal();
    }

    closeModal() {
        this.activeModal.dismiss('cancel');
    }

    private enviarDesgloseParaRevision(mensaje?: string) {

        // ACTUALIZAMOS EL DESGLOSE
        this.breakdown.nomUserOpen = null;
        this.breakdown.codEstado = ESTADO_BORRADOR_TERMINADO;
        this.breakdown.flgReq2firma = true;
        this.breakdown.numRolChgEstado = this.codeUser;
        // GENERAMOS AUDITORIA
        const auditoria: IDesgloseAuditoria = new DesgloseAuditoria();
        auditoria.codEvento = EVENTO_AUDITORIA_CAMBIO_ESTADO;
        auditoria.codEstadoNvo = ESTADO_BORRADOR_TERMINADO;
        auditoria.glsComentario = COMENTARIO_AUDITORIA_CAMBIO_ESTADO_BORRADOR_TERMINADO;
        auditoria.codCliente = this.codeCliente;
        auditoria.codTipoDes = Number(this.breakdown.codTipoDes);
        auditoria.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
        auditoria.codMoneda = this.breakdown.codMoneda;
        auditoria.cod_plantilla = Number(this.breakdown.codPlantilla);

        // GENERAMOS DESGLOSE REVISION
        const desgloseRevision: IDesgloseRevision = new DesgloseRevision();
        desgloseRevision.codCliente = this.breakdown.codCliente;
        desgloseRevision.codTipoDes = Number(this.breakdown.codTipoDes);
        desgloseRevision.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
        desgloseRevision.codMoneda = this.breakdown.codMoneda;
        desgloseRevision.codPlantilla = this.breakdown.codPlantilla;
        desgloseRevision.rolDigitador = this.codeUser;
        desgloseRevision.rolRevisor = this.codeRevisor;
        desgloseRevision.glsNotificacion = this.commentary;
        desgloseRevision.rolRevisorReem = null;
        // GENERAMOS EL MENSAJE A ENVIAR
        const desgloseMensaje: IDesgloseMensaje = new DesgloseMensaje();
        desgloseMensaje.codCliente = this.breakdown.codCliente;
        desgloseMensaje.codPlantilla = Number(this.breakdown.codPlantilla);
        desgloseMensaje.codTipoDes = Number(this.breakdown.codTipoDes);
        desgloseMensaje.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
        desgloseMensaje.codMoneda = this.breakdown.codMoneda;
        desgloseMensaje.numRolEmpleado = this.codeUser;
        desgloseMensaje.fecEvent = moment(new Date(), DATE_TIME_FORMAT);
        desgloseMensaje.glsComentarios = mensaje;

        const breakdownToApprove: IBreakdownSendRequest = new BreakdownSendRequest(
            this.breakdown,
            auditoria,
            desgloseRevision,
            desgloseMensaje,
            this.extraDataDesglose);
        this.desgloseService.sendReview(breakdownToApprove).subscribe(
            (data: HttpResponse<IDesglose>) => {
                this.breakdown = data.body;
                this.enviarShippingReviewEmit.emit(this.breakdown);
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );

    }

    private devolverDesglose(mensaje?: string) {
        // ACTUALIZAMOS EL DESGLOSE
        this.breakdown.nomUserOpen = null;
        this.breakdown.codEstado = ESTADO_BORRADOR_OBSERVACION;
        this.breakdown.flgReq2firma = null; // todo: aqui falta
        this.breakdown.numDevolEjecutor = null;
        this.breakdown.numRolChgEstado = this.codeUser;
        // GENERAMOS AUDITORIA
        const auditoria: IDesgloseAuditoria = new DesgloseAuditoria();
        auditoria.codEvento = EVENTO_AUDITORIA_CAMBIO_ESTADO;
        auditoria.codEstadoNvo = ESTADO_BORRADOR_OBSERVACION;
        auditoria.glsComentario = COMENTARIO_AUDITORIA_CAMBIO_ESTADO_BORRADOR_OBSERVACION;
        auditoria.codCliente = this.codeCliente;
        auditoria.codTipoDes = Number(this.breakdown.codTipoDes);
        auditoria.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
        auditoria.codMoneda = this.breakdown.codMoneda;
        auditoria.cod_plantilla = Number(this.breakdown.codPlantilla);

        // GENERAMOS DESGLOSE REVISION
        let desgloseRevision: IDesgloseRevision;
        // esto para crear
        desgloseRevision = new DesgloseRevision();
        desgloseRevision.codCliente = this.breakdown.codCliente;
        desgloseRevision.codTipoDes = Number(this.breakdown.codTipoDes);
        desgloseRevision.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
        desgloseRevision.codMoneda = this.breakdown.codMoneda;
        desgloseRevision.codPlantilla = this.breakdown.codPlantilla;
        desgloseRevision.rolDigitador = this.codeUser;
        desgloseRevision.rolRevisor = this.codeAnalista.toString(10);
        desgloseRevision.glsNotificacion = this.commentary;
        // GENERAMOS EL MENSAJE A ENVIAR
        const desgloseMensaje: IDesgloseMensaje = new DesgloseMensaje();
        desgloseMensaje.codCliente = this.breakdown.codCliente;
        desgloseMensaje.codPlantilla = Number(this.breakdown.codPlantilla);
        desgloseMensaje.codTipoDes = Number(this.breakdown.codTipoDes);
        desgloseMensaje.codSubtipoDes = Number(this.breakdown.codSubtipoDes);
        desgloseMensaje.codMoneda = this.breakdown.codMoneda;
        desgloseMensaje.numRolEmpleado = this.codeUser;
        desgloseMensaje.fecEvent = moment(new Date(), DATE_FORMAT_CREATE_MOMENT_OBJECT);
        desgloseMensaje.glsComentarios = mensaje;

        const breakdownSendRequest: IBreakdownSendRequest = new BreakdownSendRequest(this.breakdown, auditoria, desgloseRevision, desgloseMensaje, this.extraDataDesglose);
        this.desgloseService.sendExecutor(breakdownSendRequest).subscribe(
            (data: HttpResponse<IDesglose>) => {
                this.breakdown = data.body;
                this.turbineUserService.findByRole(this.codeAnalista.toString()).subscribe(
                    (datos: HttpResponse<ITurbineUsuarios>) => {
                        this.breakdown.nomUserModif = datos.body[ 0 ].nombre;
                        this.devolverShippingEmit.emit(this.breakdown);
                    },
                    (datos: HttpErrorResponse) => this.onError(datos.message)
                );
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );

    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
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
