import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';

type EntityResponseType = HttpResponse<IDesgloseFirma>;
type EntityArrayResponseType = HttpResponse<IDesgloseFirma[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseFirmaService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-firmas';
    private resourceUrlAsociado2Firma = SERVER_API_URL + 'api/desglose-firmas-empleadoAsociado2Firma';
    private resourceUrlAsociadoFirma = SERVER_API_URL + 'api/desglose-firmas-empleadoAsociado';

    constructor(private http: HttpClient) {
    }

    create(desgloseFirma: IDesgloseFirma): Observable<EntityResponseType> {
        return this.http.post<IDesgloseFirma>(this.resourceUrl, desgloseFirma, { observe: 'response' });
    }

    update(desgloseFirma: IDesgloseFirma): Observable<EntityResponseType> {
        return this.http.put<IDesgloseFirma>(this.resourceUrl, desgloseFirma, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDesgloseFirma>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    empleadoAsociado2Firma(roleEmpleado: string): Observable<EntityArrayResponseType> {
        return this.http.get<IDesgloseFirma[]>(`${this.resourceUrlAsociado2Firma}/${roleEmpleado}`, { observe: 'response' });
    }

    empleadoAsociadoFirma(roleEmpleado: string): Observable<EntityArrayResponseType> {
        return this.http.get<IDesgloseFirma[]>(`${this.resourceUrlAsociadoFirma}/${roleEmpleado}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDesgloseFirma[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
