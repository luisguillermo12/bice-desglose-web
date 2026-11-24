import { Component, OnInit, ViewChild } from '@angular/core';
import { Sweetalert2Component } from 'app/shared/alert/sweetalert2.component';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { Principal } from 'app/core';
import { ITreeOptions, TreeComponent } from 'angular-tree-component';
import { ReviewersService } from 'app/maintenance/reviewers/reviewers.service';
import { Subscription } from 'rxjs';
import { isNullOrUndefined } from 'util';
import { DesgloseFirma } from 'app/shared/model/desglose-firma.model';
import { ICON_CARD_CLOSE, ICON_CARD_OPEN, PLAT_CODCARGOREVISOR, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';
import { RoleService } from 'app/shared/services/role.service';
import { ITurbineRole } from 'app/shared/model/turbine-role.model';

@Component({
    selector: 'jhi-charges',
    templateUrl: './reviewers.component.html',
    styleUrls: [ 'reviewers.scss' ]
})
export class ReviewersComponent implements OnInit {

    private clienteSubscription: Subscription;
    public isCollapsedDataPanel = false;
    private currentAccount: any;
    public nombreE = '';
    public revisores: ITurbineUsuarios[];
    public firmas: ITurbineUsuarios[];
    public selectedRevisores: ITurbineUsuarios[];
    public nodes = [];
    public options: ITreeOptions = {
        useVirtualScroll: true,
        nodeHeight: 8,
        actionMapping: {
            mouse: {}
        }
    };
    @ViewChild(TreeComponent) private tree: TreeComponent;
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;

    constructor(private reviewersService: ReviewersService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService, private principal: Principal,
                private sweetalert: Sweetalert2Component, private roleService: RoleService) {
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    private loadAll() {
        this.cargarRevisores();
    }

    private cargarNodos() {
        this.reviewersService.finAllFirmas().subscribe(
            (res: HttpResponse<Object>) => this.llenarNodosFirmas(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private llenarFirmas(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.firmas = data;
        this.nodes = [];
        for (let i = 0; i < this.firmas.length; i++) {
            const object = Object();
            object.idCode = this.firmas[ i ].operatorCode;
            object.name = this.firmas[ i ].nombre;
            object.padre = true;
            object.children = [];
            this.nodes.push(object);
        }
        this.tree.treeModel.update();
        this.cargarNodos();
    }

    private llenarNodosFirmas(data: any, headers: HttpHeaders) {
        for (let i = 0; i < data.length; i++) {
            let existe = 0;
            let posicion = -1;
            for (let j = 0; j < this.nodes.length; j++) {
                if (data[ i ][ 0 ] === this.nodes[ j ].idCode) {
                    existe = 1;
                    posicion = j;
                }
            }
            if (existe === 1) {
                let existeHijo = 0;
                for (let j = 0; j < this.nodes[ posicion ].children.length; j++) {
                    if (data[ i ][ 1 ] === this.nodes[ posicion ].children[ j ].idCode) {
                        existeHijo = 1;
                    }
                }
                if (existeHijo === 0) {
                    if (data[ i ][ 1 ] !== null) {
                        if (!isNullOrUndefined(data[ i ][ 2 ])) {
                            this.nodes[ posicion ].children.push({ idCode: data[ i ][ 1 ], name: data[ i ][ 2 ], padre: false });
                        }
                    }
                }
            }
        }
        this.tree.treeModel.update();
    }

    private cargarRevisores() {
        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_REVISOR).subscribe(
            (respuesta: HttpResponse<ITurbineRole>) => {
                this.reviewersService.findUsuariosbyId(respuesta.body.roleId).subscribe(
                    (res: HttpResponse<ITurbineUsuarios[]>) => {
                        this.llenarFirmas(res.body, res.headers);
                        this.paginateRevisores(res.body, res.headers);
                    },
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            },
            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
        );
    }

    private paginateRevisores(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.revisores = data;
        if (data !== undefined || data !== null || data !== []) {
            if (this.revisores.length > 0) {
                this.selectedRevisores = [ this.revisores[ 0 ] ];
                this.nombreE = this.revisores[ 0 ].nombre;
            }
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    public onSelectRevisores({ selected }) {
        this.nombreE = selected[ 0 ].nombre;
        this.selectedRevisores = selected;
    }

    public onClickAsociarIntegrante() {
        const node = this.tree.treeModel.getActiveNode();
        const nodes = this.tree.treeModel.getActiveNodes();
        if (isNullOrUndefined(node)) {
            this.sweetalert.mensajeError('groups.error', 'reviewers.errorMensaje2', {});
        } else {
            if (this.nombreE.length > 0 && node.data.name.length > 0) {
                if (this.revisores.length > 0) {
                    if (node.data.padre) {
                        if (node.children.length === 0) {
                            let bandera = 0;
                            nodes[ 0 ].parent.data.children.forEach((padre, index) => {
                                padre.children.forEach((hijo, i) => {
                                    if (hijo.idCode === this.selectedRevisores[ 0 ].operatorCode) {
                                        bandera++;
                                    }
                                });
                            });
                            if (bandera === 0) {
                                if (node.data.idCode !== this.selectedRevisores[ 0 ].operatorCode) {
                                    this.clienteSubscription = this.sweetalert
                                    .solicitarAccion('reviewers.mensaje', 'reviewers.asociar', 'reviewers.aceptar', 'reviewers.cancelar', {
                                        persona: this.nombreE,
                                        persona1: node.data.name
                                    })
                                    .subscribe(resp => {
                                        if (resp) {
                                            const rolEmpleado = node.data.idCode;
                                            const numRolEmpleado = this.selectedRevisores[ 0 ].operatorCode;
                                            const desgloFirma: DesgloseFirma = new DesgloseFirma(
                                                PLAT_CODCARGOREVISOR,
                                                rolEmpleado,
                                                numRolEmpleado
                                            );
                                            this.reviewersService
                                            .createDesgloseFirma(desgloFirma)
                                            .subscribe(
                                                response => this.loadAll(),
                                                (response: HttpErrorResponse) => this.onError(response.message)
                                            );
                                        }
                                        this.clienteSubscription.unsubscribe();
                                    });
                                } else {
                                    this.sweetalert.mensajeError('reviewers.error', 'reviewers.errorMensaje5', { persona: this.nombreE });
                                }
                            } else {
                                this.sweetalert.mensajeError('reviewers.error', 'reviewers.errorMensaje6', { persona: this.nombreE });
                            }
                        } else {
                            this.sweetalert.mensajeError('reviewers.error', 'reviewers.errorMensaje4', { persona: node.data.name });
                        }
                    } else {
                        this.sweetalert.mensajeError('reviewers.error', 'reviewers.errorMensaje3', {});
                    }
                } else {
                    this.sweetalert.mensajeError('reviewers.error', 'reviewers.errorMensaje2', {});
                }
            }
        }
    }

    public onClickEliminarIntegrante() {
        const node = this.tree.treeModel.getActiveNode();
        if (isNullOrUndefined(node)) {
            this.sweetalert.mensajeError('groups.error', 'reviewers.errorMensaje3', {});
        } else {
            if (node.data.name.length > 0) {
                if (!node.data.padre) {
                    this.clienteSubscription = this.sweetalert
                    .solicitarAccion('reviewers.mensaje', 'reviewers.desasociar', 'reviewers.aceptar', 'reviewers.cancelar', {
                        persona: node.data.name,
                        persona1: node.parent.data.name
                    })
                    .subscribe(resp => {
                        if (resp) {
                            const rolEmpleado = node.parent.data.idCode;
                            const numRolEmpleado = node.data.idCode;
                            const desgloFirma: DesgloseFirma = new DesgloseFirma(PLAT_CODCARGOREVISOR, rolEmpleado, numRolEmpleado);

                            this.reviewersService
                            .deleteDesgloseFirma(desgloFirma)
                            .subscribe(response => this.loadAll(), (response: HttpErrorResponse) => this.onError(response.message));
                        }
                        this.clienteSubscription.unsubscribe();
                    });
                } else {
                    this.sweetalert.mensajeError('reviewers.error', 'reviewers.errorMensaje1', {});
                }
            }
        }
    }
}
