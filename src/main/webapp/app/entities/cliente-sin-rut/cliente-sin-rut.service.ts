import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IClienteSinRut } from 'app/shared/model/cliente-sin-rut.model';

type EntityResponseType = HttpResponse<IClienteSinRut>;
type EntityArrayResponseType = HttpResponse<IClienteSinRut[]>;

@Injectable({ providedIn: 'root' })
export class ClienteSinRutService {
    private resourceUrl = SERVER_API_URL + 'api/cliente-sin-ruts';

    constructor(private http: HttpClient) {}

    create(clienteSinRut: IClienteSinRut): Observable<EntityResponseType> {
        return this.http.post<IClienteSinRut>(this.resourceUrl, clienteSinRut, { observe: 'response' });
    }

    update(clienteSinRut: IClienteSinRut): Observable<EntityResponseType> {
        return this.http.put<IClienteSinRut>(this.resourceUrl, clienteSinRut, { observe: 'response' });
    }

    find(codCliente: string): Observable<EntityResponseType> {
        return this.http.get<IClienteSinRut>(`${this.resourceUrl}/${codCliente}`, { observe: 'response' });
    }

    findAll(): Observable<EntityArrayResponseType> {
        return this.http.get<IClienteSinRut[]>(`${this.resourceUrl}/all`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IClienteSinRut[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(codCliente: string): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${codCliente}`, { observe: 'response' });
    }
}
