import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IRelacionTabla } from 'app/shared/model/relacion-tabla.model';

type EntityResponseType = HttpResponse<IRelacionTabla>;
type EntityArrayResponseType = HttpResponse<IRelacionTabla[]>;

@Injectable({ providedIn: 'root' })
export class RelacionTablaService {
    private resourceUrl = SERVER_API_URL + 'api/relacion-tablas';

    constructor(private http: HttpClient) {}

    create(relacionTabla: IRelacionTabla): Observable<EntityResponseType> {
        return this.http.post<IRelacionTabla>(this.resourceUrl, relacionTabla, { observe: 'response' });
    }

    update(relacionTabla: IRelacionTabla): Observable<EntityResponseType> {
        return this.http.put<IRelacionTabla>(this.resourceUrl, relacionTabla, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IRelacionTabla>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IRelacionTabla[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
