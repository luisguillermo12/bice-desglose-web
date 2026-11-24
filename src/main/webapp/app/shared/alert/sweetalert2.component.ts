import { Component, Injectable } from '@angular/core';
import swal, { SweetAlertType } from 'sweetalert2';
import { Observable, Subject } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { isNullOrUndefined } from 'util';

const swalWithBootstrapButtons = swal.mixin({
    confirmButtonClass: 'btn btn-primary',
    cancelButtonClass: 'btn btn-default',
    buttonsStyling: false
});

@Component({
    selector: 'jhi-sweetalert2',
    template: ''
})
@Injectable({ providedIn: 'root' })
export class Sweetalert2Component {
    private estadoSolicitarAccion = new Subject<boolean>();

    constructor(private translateService: TranslateService) {
    }

    public mensajeError(titulo: string, texto: string, valoresTexto?: Object) {
        this.desplegarMensaje('error', this.translateService.instant(titulo), this.translateService.instant(texto, valoresTexto));
    }

    public mensajeSuccess(titulo: string, texto: string, valoresTexto?: Object) {
        this.desplegarMensaje1('success', this.translateService.instant(titulo), this.translateService.instant(texto, valoresTexto));
    }

    public mensajeWarning(titulo: string, texto: string, valoresTexto?: Object) {
        this.desplegarMensaje1('warning', this.translateService.instant(titulo), this.translateService.instant(texto, valoresTexto));
    }

    public mensajeInfo(titulo: string, texto: string, valoresTexto?: Object) {
        this.desplegarMensaje('info', this.translateService.instant(titulo), this.translateService.instant(texto, valoresTexto));
    }

    public mensajeQuestion(titulo: string, texto: string, valoresTexto?: Object) {
        this.desplegarMensaje1('question', this.translateService.instant(titulo), this.translateService.instant(texto, valoresTexto));
    }

    private desplegarMensaje(tipo: any, titulo: string, texto: string) {
        const confirmButtonText = this.translateService.instant('breakdown.accept');
        swal({
            type: tipo,
            title: titulo,
            text: texto,
            confirmButtonText
        });
    }

    private desplegarMensaje1(tipo: any, titulo: string, texto: string) {
        const confirmButtonText = this.translateService.instant('breakdown.accept');
        swal({
            type: tipo,
            title: titulo,
            text: texto,
            confirmButtonText
        });
    }

    public solicitarAccion(
        titulo: string,
        texto: string,
        confirmButtonText: string,
        cancelButtonText: string,
        valoresTexto?: Object
    ): Observable<boolean> {
        swalWithBootstrapButtons({
            title: this.translateService.instant(titulo),
            text: this.translateService.instant(texto, valoresTexto),
            type: 'warning',
            showCancelButton: true,
            confirmButtonText: this.translateService.instant(confirmButtonText),
            cancelButtonText: this.translateService.instant(cancelButtonText),
            reverseButtons: true
        }).then(result => {
            if (result.value) {
                this.estadoSolicitarAccion.next(true);
            } else if (result.dismiss === swal.DismissReason.cancel) {
                this.estadoSolicitarAccion.next(false);
            }
        });
        return this.estadoSolicitarAccion.asObservable();
    }

    public sweetAlertQuestionMessage(titulo: string, texto: string, alertType: SweetAlertType, confirmButtonText: string, cancelButtonText: string,
                                     interpolateParams?: Object): Observable<boolean> {
        swalWithBootstrapButtons({
            title: this.translateService.instant(titulo),
            text: this.translateService.instant(texto, interpolateParams),
            type: alertType,
            showCancelButton: true,
            confirmButtonText: this.translateService.instant(confirmButtonText),
            cancelButtonText: this.translateService.instant(cancelButtonText),
            reverseButtons: true
        }).then(result => {
            if (result.value) {
                this.estadoSolicitarAccion.next(true);
            } else if (result.dismiss === swal.DismissReason.cancel) {
                this.estadoSolicitarAccion.next(false);
            }
        });
        return this.estadoSolicitarAccion.asObservable();
    }
}
