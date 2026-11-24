import { Component, OnInit } from '@angular/core';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { JhiAlertService } from 'ng-jhipster';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { CODTABLA_TIPO_ACTIVIDADES_PERSONAS_SIN_RUT, ICON_CARD_CLOSE, ICON_CARD_OPEN } from 'app/app.constants';
import { IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';
import { ActivatedRoute } from '@angular/router';
import { ClienteSinRutService } from 'app/entities/cliente-sin-rut';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Principal } from 'app/core';
import { Sweetalert2Component } from 'app/shared';

@Component({
    selector: 'jhi-client-update-form',
    templateUrl: './clientupdateform.component.html',
    styleUrls: [ 'clientupdate.scss' ]
})
export class ClientupdateformComponent implements OnInit {
    public isCollapsedDataPanel = false;
    public clienteSinRut: IClienteSinRut;
    public listaActividades: IDescripcionCodigo[] = [];
    currentAccount: any;
    forma: FormGroup;
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;

    constructor(private jhiAlertService: JhiAlertService, private tablaDescripcionservice: DescripcionCodigoService, private activatedRoute: ActivatedRoute,
                private clienteSinRutService: ClienteSinRutService, private principal: Principal, private sweetalert: Sweetalert2Component) {
    }

    ngOnInit() {
        this.forma = new FormGroup({
            nombre: new FormControl('', [ Validators.required ]),
            apellidoPaterno: new FormControl(''),
            apellidoMaterno: new FormControl(''),
            actividadEconomica: new FormControl(0, [ Validators.required ])
        });
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.loadAll();
    }

    loadAll() {
        this.tablaDescripcionservice.findByCodTabla(CODTABLA_TIPO_ACTIVIDADES_PERSONAS_SIN_RUT, 'glsGenericalarga,ASC').subscribe(
            (data: HttpResponse<IDescripcionCodigo[]>) => {
                this.listaActividades = data.body;
            },
            (data: HttpErrorResponse) => this.onError(data.message)
        );
        const id = this.activatedRoute.snapshot.paramMap.get('id');
        this.clienteSinRutService.find(id).subscribe(
            (rpt: HttpResponse<IClienteSinRut>) => {
                this.clienteSinRut = rpt.body;
                this.forma.setValue({
                    nombre: this.clienteSinRut.nomCliente,
                    apellidoPaterno: this.clienteSinRut.nomPaterno,
                    apellidoMaterno: this.clienteSinRut.nomMaterno,
                    actividadEconomica: this.clienteSinRut.codActividad
                });
            },
            (rpt: HttpErrorResponse) => this.onError(rpt.message)
        );
    }

    previousState() {
        window.history.back();
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    grabarDatos() {
        if (this.forma.valid) {
            this.clienteSinRut.nomCliente = this.forma.value[ 'nombre' ];
            this.clienteSinRut.nomPaterno = this.forma.value[ 'apellidoPaterno' ];
            this.clienteSinRut.nomMaterno = this.forma.value[ 'apellidoMaterno' ];
            this.clienteSinRut.codActividad = this.forma.value[ 'actividadEconomica' ];
            this.clienteSinRutService.update(this.clienteSinRut).subscribe(
                (data: HttpResponse<IClienteSinRut>) => {
                    this.sweetalert.mensajeSuccess('clientupdate.mensaje', 'clientupdate.exito');
                    this.previousState();
                },
                (data: HttpErrorResponse) => this.onError(data.message)
            );
        }
    }
}
