import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITurbinePermission } from 'app/shared/model/turbine-permission.model';

type EntityResponseType = HttpResponse<ITurbinePermission>;
type EntityArrayResponseType = HttpResponse<ITurbinePermission[]>;

@Injectable({ providedIn: 'root' })
export class TurbinePermissionService {
    private resourceUrl = SERVER_API_URL + 'api/turbine-permissions';

    constructor(private http: HttpClient) {}

    create(turbinePermission: ITurbinePermission): Observable<EntityResponseType> {
        return this.http.post<ITurbinePermission>(this.resourceUrl, turbinePermission, { observe: 'response' });
    }

    update(turbinePermission: ITurbinePermission): Observable<EntityResponseType> {
        return this.http.put<ITurbinePermission>(this.resourceUrl, turbinePermission, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITurbinePermission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITurbinePermission[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
