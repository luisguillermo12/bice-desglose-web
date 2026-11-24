import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITablaServMailDest } from 'app/shared/model/tabla-serv-mail-dest.model';

type EntityResponseType = HttpResponse<ITablaServMailDest>;
type EntityArrayResponseType = HttpResponse<ITablaServMailDest[]>;

@Injectable({ providedIn: 'root' })
export class TablaServMailDestService {
    private resourceUrl = SERVER_API_URL + 'api/tabla-serv-mail-dests';

    constructor(private http: HttpClient) {}

    create(tablaServMailDest: ITablaServMailDest): Observable<EntityResponseType> {
        return this.http.post<ITablaServMailDest>(this.resourceUrl, tablaServMailDest, { observe: 'response' });
    }

    update(tablaServMailDest: ITablaServMailDest): Observable<EntityResponseType> {
        return this.http.put<ITablaServMailDest>(this.resourceUrl, tablaServMailDest, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITablaServMailDest>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITablaServMailDest[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
