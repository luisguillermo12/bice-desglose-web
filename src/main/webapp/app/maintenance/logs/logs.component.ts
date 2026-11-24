import {Component, OnInit} from '@angular/core';
import {DescripcionCodigoService} from 'app/entities/descripcion-codigo';
import {CODTABLA_TIPO_CARGOS, ICON_CARD_CLOSE, ICON_CARD_OPEN, PLAT_CODEJECUTOR} from 'app/app.constants';
import {Principal} from 'app/core';
import {JhiAlertService} from 'ng-jhipster';
import {HttpResponse} from '@angular/common/http';
import {SessionStorageService} from 'ngx-webstorage';
import {ReplacementService} from 'app/maintenance/replacement/replacement.service';
import {DATE_FORMAT_CREATE_MOMENT_OBJECT,DATE_FORMAT, Sweetalert2Component} from 'app/shared';
import {Subscription} from 'rxjs';
import {DesgloseReemplazoService} from 'app/entities/desglose-reemplazo';
import {ReportService} from 'app/shared/services/report.service';
import {ILog} from 'app/shared/model/log.model';
import {Moment} from 'moment';
import {isNullOrUndefined} from 'util';

@Component({
    selector: 'jhi-logs',
    templateUrl: './logs.component.html',
    styleUrls: [ 'logs.scss' ]
})
export class LogsComponent implements OnInit {
    private clienteSubscription: Subscription;
    currentAccount: any;
    public isCollapsedDataPanel = false;
    public isCollapsedClientDataPanel = false;
    public PLAT_CODEJECUTOR = PLAT_CODEJECUTOR;
    public CODTABLA_TIPO_CARGOS = CODTABLA_TIPO_CARGOS;
    rows = [];
    selected = [];
    public ICON_CARD_OPEN = ICON_CARD_OPEN;
    public ICON_CARD_CLOSE = ICON_CARD_CLOSE;
    public fechaInicio:Moment;
    public fechaFin:Moment;

    constructor(private reportService: ReportService,private descripcionCodigoService: DescripcionCodigoService, private principal: Principal, private jhiAlertService: JhiAlertService,
                private sessionStorage: SessionStorageService, private service: ReplacementService, private sweetalert: Sweetalert2Component,
                private desgloseReemplazoService: DesgloseReemplazoService) {
    }

    ngOnInit() {
        this.principal.identity().then(account => {
            this.currentAccount = account;
        });
        this.getDesgloseLog();
    }

    descargar() {
        if (this.selected.length > 0) {
            this.clienteSubscription = this.sweetalert
            .solicitarAccion('logs.mensaje', 'logs.alertaDescargar', 'logs.aceptar', 'logs.cancelar')
            .subscribe(res => {
                if (res) {
                    for (const item of this.selected) {
                        const data: ILog = item;
                        this.reportService.LogDesgloseFile(data.nombre).subscribe((datos:HttpResponse<any>)=> {
                            const file = new Blob([ datos ], { type: 'text/plain' });
                            const fileURL = URL.createObjectURL(file);
                            const a = document.createElement('a');
                            document.body.appendChild(a);
                            a.setAttribute('style', 'display: none');
                            a.href = fileURL;
                            a.download = data.nombre;
                            a.click();
                            window.URL.revokeObjectURL(fileURL);
                            a.remove();
                        });
                    }
                }
                this.clienteSubscription.unsubscribe();
            });
        } else {
            this.sweetalert.mensajeWarning('logs.mensaje', 'logs.alertaSeleccionEliminar');
        }
    }
    onSelect({ selected }) {
        this.selected.splice(0, this.selected.length);
        this.selected.push(...selected);
    }
    onClickSearchBreakdown() {
        this.getDesgloseLog();
    }
    getDesgloseLog() {

        if(!isNullOrUndefined(this.fechaInicio) && !isNullOrUndefined(this.fechaFin)) {
            this.reportService.LogDesglose(this.fechaInicio.format(DATE_FORMAT), this.fechaFin.format(DATE_FORMAT)).subscribe(
                (datos: HttpResponse<ILog[]>) => {
                    this.rows = datos.body;
                }
            );
        } else {
            this.reportService.LogDesglose(null, null).subscribe(
                (datos: HttpResponse<ILog[]>) => {
                    this.rows = datos.body;
                }
            );
        }
    }
    private onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }
}
