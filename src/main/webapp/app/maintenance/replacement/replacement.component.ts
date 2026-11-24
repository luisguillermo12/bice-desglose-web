import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { CODTABLA_TIPO_CARGOS, ICON_CARD_CLOSE, ICON_CARD_OPEN, PLAT_CODEJECUTOR, SESSION_VARIABLE_CARGOS } from 'app/app.constants';
import { Principal } from 'app/core';
import { JhiAlertService } from 'ng-jhipster';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { SessionStorageService } from 'ngx-webstorage';
import { ReplacementService } from 'app/maintenance/replacement/replacement.service';
import { IReemplazosVista } from 'app/shared/model/reemplazos-vista.model';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { Sweetalert2Component } from 'app/shared';
import { Subscription } from 'rxjs';
import { isNullOrUndefined } from 'util';
import { DesgloseReemplazo, IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';
import { DesgloseReemplazoService } from 'app/entities/desglose-reemplazo';
import { forEach } from '@angular/router/src/utils/collection';

@Component({
    selector: 'jhi-replacement',
    templateUrl: './replacement.component.html',
    styleUrls: [ 'replacement.scss' ]
})
export class ReplacementComponent implements OnInit {
    private clienteSubscription: Subscription;
    listaCargos: IDescripcionCodigo[] = [];
    listaReemplazar: ITurbineUsuarios[] = [];
    listaReemplazos: ITurbineUsuarios[] = [];
    listaEliminar: IDesgloseReemplazo[] = [];
    currentAccount: any;
    public isCollapsedDataPanel = false;
    public isCollapsedClientDataPanel = false;
    public PLAT_CODEJECUTOR = PLAT_CODEJECUTOR;
    public CODTABLA_TIPO_CARGOS = CODTABLA_TIPO_CARGOS;
    rows = [];
    selected = [];
    forma: FormGroup;
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;

    constructor(private descripcionCodigoService: DescripcionCodigoService, private principal: Principal, private jhiAlertService: JhiAlertService,
                private sessionStorage: SessionStorageService, private service: ReplacementService, private sweetalert: Sweetalert2Component,
                private desgloseReemplazoService: DesgloseReemplazoService) {
    }

    ngOnInit() {
        this.forma = new FormGroup({
            cargo: new FormControl(this.listaCargos[ 0 ], [ Validators.required ]),
            miembroReemplazar: new FormControl(this.listaReemplazar[ 0 ], [ Validators.required ]),
            miembroReemplazo: new FormControl(this.listaReemplazos[ 0 ], [ Validators.required ])
        });

        this.principal.identity().then(account => {
            this.currentAccount = account;
        });

        this.loadAll();
    }

    private loadAll() {
        this.selected = [];
        this.listaCargos = [];
        this.listaReemplazos = [];
        this.listaReemplazar = [];
        this.listaEliminar = [];
        const typesParam = CODTABLA_TIPO_CARGOS;
        this.loadIDescripcionCodigo(typesParam);
        this.service.findvistaReemplazos().subscribe(
            (resp: HttpResponse<IReemplazosVista[]>) => this.loadReemplazos(resp.body, resp.headers),
            (resp: HttpErrorResponse) => this.onError(resp.message)
        );
    }

    loadReemplazos(data: IReemplazosVista[], headers: HttpHeaders) {
        this.rows = data;
    }

    loadIDescripcionCodigo(codtabla: string) {
        this.descripcionCodigoService
        .findAllByCodTablaAndCodGenericoLikeAndCodGenericoNot(codtabla, 'CARGO-', this.PLAT_CODEJECUTOR)
        .subscribe(
            (res: HttpResponse<IDescripcionCodigo[]>) => this.asignDescripcionCodigos(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    agregarReemplazo() {
        if (this.forma.status === 'VALID') {
            const cargoSel: IDescripcionCodigo = this.forma.value.cargo;
            const miembroReemplazar: ITurbineUsuarios = this.forma.value.miembroReemplazar;
            const miembroReemplazo: ITurbineUsuarios = this.forma.value.miembroReemplazo;
            this.service.findEs2Firma(miembroReemplazar.operatorCode, miembroReemplazo.operatorCode).subscribe(
                (data: HttpResponse<number>) => {
                    if (data.body === 0) {
                        this.clienteSubscription = this.sweetalert
                        .solicitarAccion(
                            'replacement.mensaje',
                            'replacement.reemplazar',
                            'replacement.aceptar',
                            'replacement.cancelar',
                            { persona1: miembroReemplazar.nombre, persona2: miembroReemplazo.nombre }
                        )
                        .subscribe(rpt => {
                            if (rpt) {
                                this.grabarReemplazo(
                                    cargoSel.codGenerico,
                                    miembroReemplazar.operatorCode,
                                    miembroReemplazo.operatorCode,
                                    miembroReemplazo.operatorCode
                                );
                            }
                            this.clienteSubscription.unsubscribe();
                        });
                    } else {
                        this.sweetalert.mensajeError('replacement.mensaje', 'replacement.valida2Firma', {});
                    }
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        } else {
            this.sweetalert.mensajeWarning('replacement.mensaje', 'replacement.alertaGrabar');
        }
    }

    grabarReemplazo(cargo: string, rolReemplazar: string, rolReemplazo: string, operatorCode) {
        const desgloseReemplazo = new DesgloseReemplazo(0, cargo, rolReemplazar, '', rolReemplazo, null, null, operatorCode, null, null);
        this.service.create(desgloseReemplazo).subscribe(
            (data: HttpResponse<void>) => {
                this.loadAll();
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );
    }

    eliminarReemplazo() {
        if (this.selected.length > 0) {
            this.clienteSubscription = this.sweetalert
            .solicitarAccion('replacement.mensaje', 'replacement.alertaEliminar', 'replacement.aceptar', 'replacement.cancelar')
            .subscribe(res => {
                if (res) {
                    for (const item of this.selected) {
                        const data: IReemplazosVista = item;
                        const desgloseReemplazo = new DesgloseReemplazo(
                            0,
                            data.cargo01,
                            data.rol1.toString(),
                            data.cargo02,
                            data.rol2.toString(),
                            null,
                            null,
                            null,
                            data.rol2.toString(),
                            null
                        );
                        this.listaEliminar.push(desgloseReemplazo);
                    }
                    this.desgloseReemplazoService.updateNativo(this.listaEliminar).subscribe(
                        (data: HttpResponse<IDesgloseReemplazo>) => {
                            this.loadAll();
                        },
                        (data: HttpErrorResponse) => this.onError(data.message)
                    );
                }
                this.clienteSubscription.unsubscribe();
            });
        } else {
            this.sweetalert.mensajeWarning('replacement.mensaje', 'replacement.alertaSeleccionEliminar');
        }
    }

    onSelectCargos(event) {
        if (!isNullOrUndefined(event)) {
            const cargoSel: IDescripcionCodigo = event;
            this.service.findAllReemplazar(cargoSel.codGenerico).subscribe(
                (data: HttpResponse<ITurbineUsuarios[]>) => {
                    this.listaReemplazar = data.body;
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        } else {
            this.listaReemplazar = [];
            this.listaReemplazos = [];
        }
    }

    onSelectOperatorCode(event) {
        if (!isNullOrUndefined(event)) {
            const operator: ITurbineUsuarios = event;
            this.service.findAllReemplazos(parseInt(operator.operatorCode, 10)).subscribe(
                (data: HttpResponse<ITurbineUsuarios[]>) => {
                    this.listaReemplazos = data.body;
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        } else {
            this.listaReemplazos = [];
        }
    }

    private asignDescripcionCodigos(data: IDescripcionCodigo[], headers: HttpHeaders) {
        this.listaCargos = data;
        this.sessionStorage.store(SESSION_VARIABLE_CARGOS, data);
    }

    onSelect({ selected }) {
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
