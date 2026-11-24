import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { IReemplazosVista, ReemplazosVista } from 'app/shared/model/reemplazos-vista.model';
import { ITurbineUsuarios, TurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { IDesgloseReemplazo } from 'app/shared/model/desglose-reemplazo.model';

@Injectable({ providedIn: 'root' })
export class LogsService {
    private resourceUrlDesgloseRevision = SERVER_API_URL + 'api/desglose-revisions';
    private resourceUrlTurbineUser = SERVER_API_URL + 'api/turbine-users';
    private resourceUrl2Firma = SERVER_API_URL + 'api/desglose-firmas';
    private resourceUrlDesgloseReemplazo = SERVER_API_URL + 'api/desglose-reemplazos';

    constructor(private http: HttpClient) {
    }

    findvistaReemplazos(): Observable<HttpResponse<ReemplazosVista[]>> {
        return this.http.get<IReemplazosVista[]>(`${ this.resourceUrlDesgloseRevision }/vistaReemplazos`, { observe: 'response' });
    }

    findAllReemplazar(cargo: string): Observable<HttpResponse<TurbineUsuarios[]>> {
        return this.http.get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUser }/finAllReemplazar/${ cargo }`, { observe: 'response' });
    }

    findAllReemplazos(operatorCode: number): Observable<HttpResponse<TurbineUsuarios[]>> {
        return this.http.get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUser }/finAllReemplazos/${ operatorCode }`, {
            observe: 'response'
        });
    }

    findEs2Firma(numRolEmpleado: string, numRolEmpleado2Fir: string): Observable<HttpResponse<any>> {
        return this.http.get(`${ this.resourceUrl2Firma }/validasegundafirma/${ numRolEmpleado }/${ numRolEmpleado2Fir }`, {
            observe: 'response'
        });
    }

    create(desgloseReemplazo: IDesgloseReemplazo): Observable<HttpResponse<void>> {
        return this.http.post<void>(`${ this.resourceUrlDesgloseReemplazo }/crear`, desgloseReemplazo, { observe: 'response' });
    }
}
