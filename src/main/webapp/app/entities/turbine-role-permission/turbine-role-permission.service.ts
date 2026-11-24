import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITurbineRolePermission } from 'app/shared/model/turbine-role-permission.model';

type EntityResponseType = HttpResponse<ITurbineRolePermission>;
type EntityArrayResponseType = HttpResponse<ITurbineRolePermission[]>;

@Injectable({ providedIn: 'root' })
export class TurbineRolePermissionService {
    private resourceUrl = SERVER_API_URL + 'api/turbine-role-permissions';

    constructor(private http: HttpClient) {}

    create(turbineRolePermission: ITurbineRolePermission): Observable<EntityResponseType> {
        return this.http.post<ITurbineRolePermission>(this.resourceUrl, turbineRolePermission, { observe: 'response' });
    }

    update(turbineRolePermission: ITurbineRolePermission): Observable<EntityResponseType> {
        return this.http.put<ITurbineRolePermission>(this.resourceUrl, turbineRolePermission, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITurbineRolePermission>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITurbineRolePermission[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
