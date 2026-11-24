import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { ClienteSinRutService } from 'app/entities/cliente-sin-rut';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';
import { JhiAlertService } from 'ng-jhipster';
import { CODTABLA_TIPO_ACTIVIDADES_PERSONAS_SIN_RUT, ICON_CARD_CLOSE, ICON_CARD_OPEN } from 'app/app.constants';
import { DescripcionCodigoService } from 'app/entities/descripcion-codigo';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
    selector: 'jhi-clientupdate',
    templateUrl: './clientupdate.component.html',
    styleUrls: [ 'clientupdate.scss' ]
})
export class ClientupdateComponent implements OnInit {
    public data:  IClienteSinRut[] = [];
    public listaClientes: IClienteSinRut[] = [];
    public listaActividades: IDescripcionCodigo[] = [];
    public isCollapsedDataPanel = false;
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;
    filteredData: IClienteSinRut[] = [];
    @ViewChild('table') table: ElementRef;
    constructor(private clienteSinRutService: ClienteSinRutService, private jhiAlertService: JhiAlertService, private tablaDescripcionservice: DescripcionCodigoService) {
    }

    ngOnInit() {
        this.loadAll();
    }

    loadAll() {
        this.tablaDescripcionservice.findByCodTabla(CODTABLA_TIPO_ACTIVIDADES_PERSONAS_SIN_RUT, 'codGenerico,ASC').subscribe(
            (rpt: HttpResponse<IDescripcionCodigo[]>) => {
                this.listaActividades = rpt.body;
                this.clienteSinRutService.findAll().subscribe(
                    (data: HttpResponse<IClienteSinRut[]>) => {
                        this.listaClientes = data.body;
                        this.data = data.body;
                        for (const item of this.listaClientes) {
                            let encontrado = 0;
                            for (const item2 of this.listaActividades) {
                                if (item.codActividad === item2.codGenerico) {
                                    encontrado = 1;
                                    item.actividad = item2.glsGenericalarga;
                                }
                            }
                            if (encontrado === 0) {
                                item.actividad = '';
                            }
                        }
                    },
                    (data: HttpErrorResponse) => this.onError(data.message)
                );
            },
            (rpt: HttpErrorResponse) => this.onError(rpt.message)
        );
    }

    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    filterDatatableName(event) {
        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.listaClientes.filter(function(d) {
            if( d.nomCliente!=null) {
                return d.nomCliente.toLowerCase().indexOf(val) !== -1 || !val;
            }
        });

        // update the rows
        this.data = temp;
        // Whenever the filter changes, always go back to the first page
        this.table.nativeElement.offset = 0;
    }
    filterDatatableApellidoP(event) {
        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.listaClientes.filter(function(d) {
            if( d.nomPaterno!=null) {
                return d.nomPaterno.toLowerCase().indexOf(val) !== -1 || !val;
            }
        });

        // update the rows
        this.data = temp;
        // Whenever the filter changes, always go back to the first page
        this.table.nativeElement.offset = 0;
    }
    filterDatatableApellidoM(event) {
        const val = event.target.value.toLowerCase();

        // filter our data
        const temp = this.listaClientes.filter(function(d) {
            if( d.nomMaterno!=null) {
                return d.nomMaterno.toLowerCase().indexOf(val) !== -1 || !val;
            }
        });

        // update the rows
        this.data = temp;
        // Whenever the filter changes, always go back to the first page
        this.table.nativeElement.offset = 0;
    }
}
