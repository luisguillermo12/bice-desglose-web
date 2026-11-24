import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITurbineGroup } from 'app/shared/model/turbine-group.model';

type EntityResponseType = HttpResponse<ITurbineGroup>;
type EntityArrayResponseType = HttpResponse<ITurbineGroup[]>;

@Injectable({ providedIn: 'root' })
export class TurbineGroupService {
    private resourceUrl = SERVER_API_URL + 'api/turbine-groups';

    constructor(private http: HttpClient) {}

    create(turbineGroup: ITurbineGroup): Observable<EntityResponseType> {
        return this.http.post<ITurbineGroup>(this.resourceUrl, turbineGroup, { observe: 'response' });
    }

    update(turbineGroup: ITurbineGroup): Observable<EntityResponseType> {
        return this.http.put<ITurbineGroup>(this.resourceUrl, turbineGroup, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<ITurbineGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ITurbineGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
