import {Injectable} from '@angular/core';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {Observable} from 'rxjs';
import {SERVER_API_URL} from 'app/app.constants';
import {PdfIngressReport} from 'app/shared/model/pdf-ingress-report.model';
import {PdfSummaryReport} from 'app/shared/model/pdf-summary-report.model';
import {IDesgloseImpresionDTO} from 'app/entities/desglose/desglose-impresion.model';
import {DATE_FORMAT} from 'app/shared';
import {ILog} from 'app/shared/model/log.model';
import {map} from 'rxjs/operators';

type EntityResponseType = HttpResponse<ILog>;
type EntityArrayResponseType = HttpResponse<ILog[]>;
@Injectable({
    providedIn: 'root'
})
export class ReportService {
    private resourceUrl = SERVER_API_URL + 'api/report';
    private INFORME_INGRESO = this.resourceUrl + '/download/informe-ingreso';
    private INFORME_INGRESO_SERVER = this.resourceUrl + '/download/informe-ingreso-server';
    private INFORME_RESUMEN = this.resourceUrl + '/download/informe-resumen-desglose-balance';
    private INFORME_RESUMEN_SERVER = this.resourceUrl + '/download/informe-resumen-desglose-balance-server';
    private INFORME_DESGLOSE_LOG = this.resourceUrl + '/download/log-desglose';
    private INFORME_DESGLOSE_LOG_FILE = this.resourceUrl + '/download/log-desglose-file';
    constructor(private http: HttpClient) {
    }

    InformeIngresoPdf(pdfIngressReport: PdfIngressReport): Observable<any> {
        return this.http.post(this.INFORME_INGRESO, pdfIngressReport, {
            'responseType': 'arraybuffer' as 'json'
        });
    }

    InformeIngresoServerPdf(desgloseImpresionDTO: IDesgloseImpresionDTO): Observable<any> {
        const copy = this.convertDateFromClient(desgloseImpresionDTO);
        return this.http.post(this.INFORME_INGRESO_SERVER, copy, {
            'responseType': 'arraybuffer' as 'json'
        });
    }

    InformeResumenPdf(pdfSummaryReport: PdfSummaryReport): Observable<any> {
        return this.http.post(this.INFORME_RESUMEN, pdfSummaryReport, {
            'responseType': 'arraybuffer' as 'json'
        });
    }
    InformeResumenServerPdf(desgloseImpresionDTO: IDesgloseImpresionDTO): Observable<any> {
        const copy = this.convertDateFromClient(desgloseImpresionDTO);
        return this.http.post(this.INFORME_RESUMEN_SERVER, copy, {
            'responseType': 'arraybuffer' as 'json'
        });
    }
    LogDesglose(fechaInicio:string,fechaFin:string): Observable<EntityArrayResponseType> {
        console.log('URL AA',this.INFORME_DESGLOSE_LOG +'/'+fechaInicio+'/'+fechaFin);
        return this.http.get(`${ this.INFORME_DESGLOSE_LOG }/${fechaInicio}/${fechaFin}`, {observe: 'response'}) .pipe(map((res: EntityArrayResponseType) => res));
    }
    LogDesgloseFile(fichero:string): Observable<any> {
        return this.http.post(this.INFORME_DESGLOSE_LOG_FILE,fichero, {
            'responseType': 'arraybuffer' as 'json'
        }).pipe(map((res: EntityResponseType) => res));
    }
    private convertDateFromClient(desgloseImpresionDTO: IDesgloseImpresionDTO): IDesgloseImpresionDTO {
        const copy: IDesgloseImpresionDTO = Object.assign({}, desgloseImpresionDTO, {
            fecDeflacBase:
                desgloseImpresionDTO.fecDeflacBase != null && desgloseImpresionDTO.fecDeflacBase.isValid()
                    ? desgloseImpresionDTO.fecDeflacBase.format(DATE_FORMAT)
                    : null,
        });
        return copy;
    }
}
