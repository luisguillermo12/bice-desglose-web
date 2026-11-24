import { Component, OnInit } from '@angular/core';
import { ChargesService } from './charges.service';
import { HttpErrorResponse, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { JhiAlertService, JhiParseLinks } from 'ng-jhipster';
import { IUser, Principal, User, UserService } from 'app/core';
import { ICON_CARD_CLOSE, ICON_CARD_OPEN, ID_GROUP_STF, USER_ROLE_PLAT_DESGLOSE_EJECUTOR, USER_ROLE_PLAT_DESGLOSE_JEFE, USER_ROLE_PLAT_DESGLOSE_REVISOR } from 'app/app.constants';
import { TurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';
import { Sweetalert2Component } from 'app/shared/alert/sweetalert2.component';
import { Subscription } from 'rxjs';
import { RoleService } from 'app/shared/services/role.service';
import { ITurbineRole } from 'app/shared/model/turbine-role.model';
import { isNullOrUndefined } from 'util';
import { TranslateService } from '@ngx-translate/core';

@Component({
    selector: 'jhi-charges',
    templateUrl: './charges.component.html',
    styleUrls: [ 'charges.scss' ]
})
export class ChargesComponent implements OnInit {
    private userJefe: User;
    private userRevisor: User;
    private userEjecutor: User;
    private user: User;
    private clienteSubscription: Subscription;
    public isCollapsedDataPanel = false;
    private currentAccount: any;
    public nombreJ = '';
    public nombreR = '';
    public nombreE = '';
    public nombreU = '';
    public jefes: ITurbineUsuarios[];
    public revisores: ITurbineUsuarios[];
    public ejecutores: ITurbineUsuarios[];
    public usuarios: ITurbineUsuarios[];
    public selectedEjecutores: ITurbineUsuarios[];
    public selectedRevisores: ITurbineUsuarios[];
    public selectedJefes: ITurbineUsuarios[];
    public selectedUsuarios: ITurbineUsuarios[];
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;

    constructor(private chargesService: ChargesService, private parseLinks: JhiParseLinks, private jhiAlertService: JhiAlertService, private principal: Principal,
                private sweetAlert2: Sweetalert2Component, private roleService: RoleService, private userService: UserService, private translateService: TranslateService
    ) {
    }

    ngOnInit() {
        this.loadAll();
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
    }

    private loadAll() {
        this.cargarEjecutores();
        this.cargarJefes();
        this.cargarRevisores();
        this.cargarUsuarios();
    }

    private paginateJefes(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.jefes = data;
        if (data !== undefined || data !== null || data !== []) {
            if (this.jefes.length > 0) {
                this.selectedJefes = [ this.jefes[ 0 ] ];
                this.nombreJ = this.jefes[ 0 ].nombre;
            }
        }
    }

    private paginateRevisores(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.revisores = data;
        if (data !== undefined || data !== null || data !== []) {
            if (this.revisores.length > 0) {
                this.selectedRevisores = [ this.revisores[ 0 ] ];
                this.nombreR = this.revisores[ 0 ].nombre;
            }
        }
    }

    private paginateEjecutores(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.ejecutores = data;
        if (data !== undefined || data !== null || data !== []) {
            if (this.ejecutores.length > 0) {
                this.selectedEjecutores = [ this.ejecutores[ 0 ] ];
                this.nombreE = this.ejecutores[ 0 ].nombre;
            }
        }
    }

    private paginateusuarios(data: ITurbineUsuarios[], headers: HttpHeaders) {
        this.usuarios = data;
        if (data !== undefined || data !== null || data !== []) {
            if (this.usuarios.length > 0) {
                this.selectedUsuarios = [ this.usuarios[ 0 ] ];
                this.nombreU = this.usuarios[ 0 ].nombre;
            }
        }
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    public onSelectEjecutores({ selected }) {
        this.nombreE = selected[ 0 ].nombre;
        this.selectedEjecutores = selected;
    }

    public onSelectRevisores({ selected }) {
        this.nombreR = selected[ 0 ].nombre;
        this.selectedRevisores = selected;
    }

    public onSelectJefes({ selected }) {
        this.nombreJ = selected[ 0 ].nombre;
        this.selectedJefes = selected;
    }

    public onSelectUsuarios({ selected }) {
        this.nombreU = selected[ 0 ].nombre;
        this.selectedUsuarios = selected;
    }

    public onClickEliminarRevisor($event) {
        const cargoStr = this.translateService.instant('charges.reviewer');
        if (this.revisores.length > 0) {
            if (this.selectedRevisores.length > 0) {
                this.clienteSubscription = this.sweetAlert2
                .solicitarAccion('charges.mensaje', 'charges.desasociar', 'charges.aceptar', 'charges.cancelar', {
                    persona: this.nombreR,
                    cargo: cargoStr
                })
                .subscribe(res => {
                    if (res) {
                        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_REVISOR).subscribe(
                            (respuesta: HttpResponse<ITurbineRole>) => {
                                const idUsuario = parseInt(this.selectedRevisores[ 0 ].user_id, 10);
                                const group = ID_GROUP_STF;
                                const idRole = respuesta.body.roleId;
                                this.chargesService.iscanDeleteRevisor(idUsuario).subscribe(
                                    (res2: HttpResponse<number>) => {
                                        if (res2.body === 0) {
                                            this.chargesService
                                            .deleteTurbineUserGroupRolesByUserByGroupByRole(idUsuario, group, idRole)
                                            .subscribe(
                                                (res1: HttpResponse<void>) => {
                                                    this.cargarUsuarios();
                                                    this.cargarRevisores();
                                                },
                                                (res1: HttpErrorResponse) => this.onError(res1.message)
                                            );
                                        } else {
                                            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaEliminacion');
                                        }
                                    },
                                    (res2: HttpErrorResponse) => this.onError(res2.message)
                                );
                            },
                            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
                        );
                    }
                    this.clienteSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo', { cargo: cargoStr });
            }
        } else {
            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo', { cargo: cargoStr });
        }
    }

    public onClickEliminarJefe($event) {
        const cargoStr = this.translateService.instant('charges.boss');
        if (this.jefes.length > 0) {
            if (this.selectedJefes.length > 0) {
                this.clienteSubscription = this.sweetAlert2
                .solicitarAccion('charges.mensaje', 'charges.desasociar', 'charges.aceptar', 'charges.cancelar', {
                    persona: this.nombreJ,
                    cargo: cargoStr
                })
                .subscribe(res => {
                    if (res) {
                        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_JEFE).subscribe(
                            (respuesta: HttpResponse<ITurbineRole>) => {
                                const idUsuario = parseInt(this.selectedJefes[ 0 ].user_id, 10);
                                const group = ID_GROUP_STF;
                                const idRole = respuesta.body.roleId;
                                this.chargesService.deleteTurbineUserGroupRolesByUserByGroupByRole(idUsuario, group, idRole).subscribe(
                                    (res1: HttpResponse<void>) => {
                                        this.cargarUsuarios();
                                        this.cargarJefes();
                                    },
                                    (res1: HttpErrorResponse) => this.onError(res1.message)
                                );
                            },
                            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
                        );
                    }
                    this.clienteSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo', { cargo: cargoStr });
            }
        } else {
            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo', { cargo: cargoStr });
        }
    }

    public onClickEliminarEjecutor($event) {
        const cargoStr = this.translateService.instant('charges.executor');
        if (this.ejecutores.length > 0) {
            if (this.selectedEjecutores.length > 0) {
                this.clienteSubscription = this.sweetAlert2
                .solicitarAccion('charges.mensaje', 'charges.desasociar', 'charges.aceptar', 'charges.cancelar', {
                    persona: this.nombreE,
                    cargo: cargoStr
                })
                .subscribe(res => {
                    if (res) {
                        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_EJECUTOR).subscribe(
                            (respuesta: HttpResponse<ITurbineRole>) => {
                                const idUsuario = parseInt(this.selectedEjecutores[ 0 ].user_id, 10);
                                const group = ID_GROUP_STF;
                                const idRole = respuesta.body.roleId;
                                this.chargesService.deleteTurbineUserGroupRolesByUserByGroupByRole(idUsuario, group, idRole).subscribe(
                                    (res1: HttpResponse<void>) => {
                                        this.chargesService.deleteDesgloseGruposByUserId(idUsuario).subscribe(
                                            response => {
                                                this.cargarUsuarios();
                                                this.cargarEjecutores();
                                            },
                                            (response: HttpErrorResponse) => this.onError(response.message)
                                        );
                                    },
                                    (res1: HttpErrorResponse) => this.onError(res1.message)
                                );
                            },
                            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
                        );
                    }
                    this.clienteSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo', { cargo: cargoStr });
            }
        } else {
            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo', { cargo: cargoStr });
        }
    }

    public onClickAsociarJefe($event) {
        if (this.usuarios.length > 0) {
            if (this.selectedUsuarios.length > 0) {
                this.clienteSubscription = this.sweetAlert2
                .solicitarAccion('charges.mensaje', 'charges.asociar', 'charges.aceptar', 'charges.cancelar', {
                    persona: this.nombreU,
                    cargo: this.translateService.instant('charges.boss')
                })
                .subscribe(res => {
                    if (res) {
                        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_JEFE).subscribe(
                            (respuesta: HttpResponse<ITurbineRole>) => {
                                const idUsuario = parseInt(this.selectedUsuarios[ 0 ].user_id, 10);
                                const group = ID_GROUP_STF;
                                const idRole = respuesta.body.roleId;
                                const user = new TurbineUserGroupRole(idUsuario, group, idRole);
                                this.chargesService.create(user).subscribe(
                                    response => {
                                        this.cargarUsuarios();
                                        this.cargarJefes();
                                    },
                                    (response: HttpErrorResponse) => this.onError(response.message)
                                );
                            },
                            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
                        );
                    }
                    this.clienteSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo');
            }
        } else {
            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo');
        }
    }

    public onClickAsociarRevisor($event) {
        if (this.usuarios.length > 0) {
            if (this.selectedUsuarios.length > 0) {
                this.clienteSubscription = this.sweetAlert2
                .solicitarAccion('charges.mensaje', 'charges.asociar', 'charges.aceptar', 'charges.cancelar', {
                    persona: this.nombreU,
                    cargo: this.translateService.instant('charges.reviewer')
                })
                .subscribe(res => {
                    if (res) {
                        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_REVISOR).subscribe(
                            (respuesta: HttpResponse<ITurbineRole>) => {
                                const idUsuario = parseInt(this.selectedUsuarios[ 0 ].user_id, 10);
                                const group = ID_GROUP_STF;
                                const idRole = respuesta.body.roleId;
                                const user = new TurbineUserGroupRole(idUsuario, group, idRole);
                                this.chargesService.create(user).subscribe(
                                    response => {
                                        this.cargarUsuarios();
                                        this.cargarRevisores();
                                    },
                                    (response: HttpErrorResponse) => this.onError(response.message)
                                );
                            },
                            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
                        );
                    }
                    this.clienteSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo');
            }
        } else {
            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo');
        }
    }

    public onClickAsociarEjecutor($event) {
        if (this.usuarios.length > 0) {
            if (this.selectedUsuarios.length > 0) {
                this.clienteSubscription = this.sweetAlert2
                .solicitarAccion('charges.mensaje', 'charges.asociar', 'charges.aceptar', 'charges.cancelar', {
                    persona: this.nombreU,
                    cargo: this.translateService.instant('charges.executor')
                })
                .subscribe(res => {
                    if (res) {
                        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_EJECUTOR).subscribe(
                            (respuesta: HttpResponse<ITurbineRole>) => {
                                const idUsuario = parseInt(this.selectedUsuarios[ 0 ].user_id, 10);
                                const group = ID_GROUP_STF;
                                const idRole = respuesta.body.roleId;
                                const user = new TurbineUserGroupRole(idUsuario, group, idRole);
                                this.chargesService.create(user).subscribe(
                                    response => {
                                        this.cargarUsuarios();
                                        this.cargarEjecutores();
                                    },
                                    (response: HttpErrorResponse) => this.onError(response.message)
                                );
                            },
                            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
                        );
                    }
                    this.clienteSubscription.unsubscribe();
                });
            } else {
                this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo');
            }
        } else {
            this.sweetAlert2.mensajeWarning('charges.aviso', 'charges.alertaSeleccionCargo');
        }
    }

    private cargarEjecutores() {
        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_EJECUTOR).subscribe(
            (respuesta: HttpResponse<ITurbineRole>) => {
                this.chargesService
                .findUsuariosbyId(respuesta.body.roleId)
                .subscribe(
                    (res: HttpResponse<ITurbineUsuarios[]>) => this.paginateEjecutores(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            },
            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
        );
    }

    private cargarRevisores() {
        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_REVISOR).subscribe(
            (respuesta: HttpResponse<ITurbineRole>) => {
                this.chargesService
                .findUsuariosbyId(respuesta.body.roleId)
                .subscribe(
                    (res: HttpResponse<ITurbineUsuarios[]>) => this.paginateRevisores(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            },
            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
        );
    }

    private cargarJefes() {
        this.roleService.getByRoleName(USER_ROLE_PLAT_DESGLOSE_JEFE).subscribe(
            (respuesta: HttpResponse<ITurbineRole>) => {
                this.chargesService
                .findUsuariosbyId(respuesta.body.roleId)
                .subscribe(
                    (res: HttpResponse<ITurbineUsuarios[]>) => this.paginateJefes(res.body, res.headers),
                    (res: HttpErrorResponse) => this.onError(res.message)
                );
            },
            (respuesta: HttpErrorResponse) => this.onError(respuesta.message)
        );
    }

    private cargarUsuarios() {
        this.chargesService.finUsuariosAll().subscribe(
            (res: HttpResponse<ITurbineUsuarios[]>) => this.paginateusuarios(res.body, res.headers),
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }
}
