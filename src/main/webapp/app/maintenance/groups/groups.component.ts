import { Component, OnInit, ViewChild } from '@angular/core';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { Principal } from 'app/core';
import { GroupsService } from 'app/maintenance/groups/groups.service';
import { ITreeOptions, TreeComponent } from 'angular-tree-component';
import { DesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { Sweetalert2Component } from 'app/shared/alert/sweetalert2.component';
import { isNullOrUndefined } from 'util';
import { Subscription } from 'rxjs';
import { ICON_CARD_CLOSE, ICON_CARD_OPEN, PLAT_CODCARGOREVISOR, PLAT_CODEJECUTOR } from 'app/app.constants';

@Component({
    selector: 'jhi-charges',
    templateUrl: './groups.component.html',
    styleUrls: [ 'groups.scss' ]
})
export class GroupsComponent implements OnInit {
    private clienteSubscription: Subscription;
    public isCollapsedDataPanel = false;
    private currentAccount: any;
    public nombreE = '';
    public ejecutores: ITurbineUsuarios[];
    public selectedEjecutores: ITurbineUsuarios[];
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

    constructor(private groupsService: GroupsService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService, private principal: Principal,
                private sweetAlert: Sweetalert2Component) {
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    private loadAll() {
        this.loadExecutors();
        this.loadGroups();
    }

    private loadGroups() {
        this.clienteSubscription = this.groupsService.finAllGrupos().subscribe(
            (res: HttpResponse<Object>) => {
                this.fillGroups(res.body, res.headers);
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private fillGroups(data: any, headers: HttpHeaders) {
        this.nodes = [];
        for (let i = 0; i < data.length; i++) {
            let exists = 0;
            let position = -1;
            const object = Object();
            for (let j = 0; j < this.nodes.length; j++) {
                if (data[ i ][ 0 ] === this.nodes[ j ].idCode) {
                    exists = 1;
                    position = j;
                }
            }
            if (exists === 0) {
                // object.id = data[ i ][ 0 ];
                object.idCode = data[ i ][ 0 ];
                object.name = data[ i ][ 1 ];
                object.padre = true;
                object.children = [];
                if (!isNullOrUndefined(data[ i ][ 2 ])) {
                    object.children.push({ idCode: data[ i ][ 2 ], name: data[ i ][ 3 ], padre: false });
                }
                this.nodes.push(object);
            } else {
                exists = 0;
                for (let j = 0; j < this.nodes[ position ].children.length; j++) {
                    if (data[ i ][ 2 ] === this.nodes[ position ].children[ j ].idCode) {
                        exists = 1;
                    }
                }
                if (exists === 0) {
                    if (!isNullOrUndefined(data[ i ][ 2 ])) {
                        if (!isNullOrUndefined(data[ i ][ 3 ]) && data[ i ][ 3 ].length > 0) {
                            this.nodes[ position ].children.push({ idCode: data[ i ][ 2 ], name: data[ i ][ 3 ], padre: false });
                        }
                    }
                }
            }
        }
        this.tree.treeModel.update();
    }

    private loadExecutors() {
        this.groupsService
        .findEjecutoresSinGrupo()
        .subscribe(
            (res: HttpResponse<ITurbineUsuarios[]>) => this.paginateExecutors(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    private paginateExecutors(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.ejecutores = data;
        if (!isNullOrUndefined(this.ejecutores)) {
            if (this.ejecutores.length > 0) {
                this.selectedEjecutores = [ this.ejecutores[ 0 ] ];
                this.nombreE = this.ejecutores[ 0 ].nombre;
            }
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    public onSelectExecutors({ selected }) {
        this.nombreE = selected[ 0 ].nombre;
        this.selectedEjecutores = selected;
    }

    public onClickAssociateMember() {
        const node = this.tree.treeModel.getActiveNode();
        if (isNullOrUndefined(node)) {
            this.sweetAlert.mensajeError('groups.error', 'groups.errorMensaje2', {});
        } else {
            if (this.nombreE.length > 0 && node.data.name.length > 0 && node.data.padre === true) {
                if (this.ejecutores.length > 0) {
                    this.clienteSubscription = this.sweetAlert
                    .solicitarAccion('groups.mensaje', 'groups.asociar', 'groups.aceptar', 'groups.cancelar', {
                        persona: this.nombreE,
                        persona1: node.data.name
                    })
                    .subscribe(resp => {
                        if (resp === true) {
                            const rolEmpleado = node.data.idCode;
                            const numRolEmpleado = this.selectedEjecutores[ 0 ].operatorCode;
                            const desgloGrupo = new DesgloseGrupo(PLAT_CODCARGOREVISOR, rolEmpleado, PLAT_CODEJECUTOR, numRolEmpleado);
                            this.groupsService
                            .create(desgloGrupo)
                            .subscribe(response => this.loadAll(), (res: HttpErrorResponse) => this.onError(res.message));
                        }
                        this.clienteSubscription.unsubscribe();
                    });
                } else {
                    this.sweetAlert.mensajeError('groups.error', 'groups.errorMensaje2', {});
                }
            } else {
                this.sweetAlert.mensajeError('groups.error', 'groups.errorMensaje3', {});
            }
        }
    }

    public onClickDeleteMember() {
        const node = this.tree.treeModel.getActiveNode();
        if (isNullOrUndefined(node)) {
            this.sweetAlert.mensajeError('groups.error', 'groups.errorMensaje2', {});
        } else {
            if (node.data.name.length > 0) {
                if (node.data.padre === false) {
                    this.clienteSubscription = this.sweetAlert
                    .solicitarAccion('groups.mensaje', 'groups.desasociar', 'groups.aceptar', 'groups.cancelar', {
                        persona: node.data.name,
                        persona1: node.parent.data.name
                    })
                    .subscribe(resp => {
                        if (resp === true) {
                            const rolEmpleado = node.parent.data.idCode;
                            const numRolEmpleado = node.data.idCode;
                            const desgloGrupo = new DesgloseGrupo(PLAT_CODCARGOREVISOR, rolEmpleado, PLAT_CODEJECUTOR, numRolEmpleado);
                            this.groupsService
                            .deleteDesgloseGruposByEntidad(desgloGrupo)
                            .subscribe(() => this.loadAll(), (res: HttpErrorResponse) => this.onError(res.message));
                        }
                        this.clienteSubscription.unsubscribe();
                    });
                } else {
                    this.sweetAlert.mensajeError('groups.error', 'groups.errorMensaje1', {});
                }
            }
        }
    }
}
