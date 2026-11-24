import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';

type EntityResponseType = HttpResponse<IDesgloseGrupo>;
type EntityArrayResponseType = HttpResponse<IDesgloseGrupo[]>;

@Injectable({ providedIn: 'root' })
export class DesgloseGrupoService {
    private resourceUrl = SERVER_API_URL + 'api/desglose-grupos';
    private resourceUrlRoleJefeGrupo = SERVER_API_URL + 'api/desglose-grupos-byRolEmpleado';
    private resourceUrlRoleEjecutor = SERVER_API_URL + 'api/desglose-grupos-findFirtByRolEjecutor';

    constructor(private http: HttpClient) {
    }

    create(desgloseGrupo: IDesgloseGrupo): Observable<EntityResponseType> {
        return this.http.post<IDesgloseGrupo>(this.resourceUrl, desgloseGrupo, { observe: 'response' });
    }

    update(desgloseGrupo: IDesgloseGrupo): Observable<EntityResponseType> {
        return this.http.put<IDesgloseGrupo>(this.resourceUrl, desgloseGrupo, { observe: 'response' });
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IDesgloseGrupo>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    findRoleEmpleado(roleEmpleado: string): Observable<EntityArrayResponseType> {
        return this.http.get<IDesgloseGrupo[]>(`${this.resourceUrlRoleJefeGrupo}/${roleEmpleado}`, { observe: 'response' });
    }

    findRoleEjecutor(roleEmpleado: string): Observable<EntityResponseType> {
        return this.http.get<IDesgloseGrupo>(`${this.resourceUrlRoleEjecutor}/${roleEmpleado}`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IDesgloseGrupo[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }
}
