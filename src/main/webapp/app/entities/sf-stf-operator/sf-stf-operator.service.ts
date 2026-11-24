import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ISfStfOperator } from 'app/shared/model/sf-stf-operator.model';
import { log } from 'util';

type EntityResponseType = HttpResponse<ISfStfOperator>;
type EntityArrayResponseType = HttpResponse<ISfStfOperator[]>;

@Injectable({ providedIn: 'root' })
export class SfStfOperatorService {
    private resourceUrl = SERVER_API_URL + 'api/sf-stf-operators';
    private resourceUrlByLoginName = SERVER_API_URL + 'api/sf-stf-operators-byLoginName';
    private resourceUrlListarAnalistas = SERVER_API_URL + 'api/sf-stf-operators-listar-analistas';
    constructor(private http: HttpClient) {}

    create(sfStfOperator: ISfStfOperator): Observable<EntityResponseType> {
        return this.http.post<ISfStfOperator>(this.resourceUrl, sfStfOperator, { observe: 'response' });
    }

    update(sfStfOperator: ISfStfOperator): Observable<EntityResponseType> {
        return this.http.put<ISfStfOperator>(this.resourceUrl, sfStfOperator, { observe: 'response' });
    }
    findByLoginName(loginName: string): Observable<EntityResponseType> {
        return this.http.get<ISfStfOperator>(`${this.resourceUrlByLoginName}/${loginName}`, { observe: 'response' });
    }
    listarAnalistas(): Observable<EntityArrayResponseType> {
        return this.http.get<ISfStfOperator[]>(`${this.resourceUrlListarAnalistas}`, { observe: 'response' });
    }
    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ISfStfOperator>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ISfStfOperator[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
