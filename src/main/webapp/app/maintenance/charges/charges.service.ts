import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { map } from 'rxjs/operators';
import { ITurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';

type EntityArrayResponseType = HttpResponse<ITurbineUsuarios[]>;
type EntityResponseType = HttpResponse<ITurbineUserGroupRole>;

@Injectable({ providedIn: 'root' })
export class ChargesService {
    private resourceUrlTurbineUserGroupRoles = SERVER_API_URL + 'api/turbine-user-group-roles';
    private resourceUrlDesgloseGrupos = SERVER_API_URL + 'api/desglose-grupos';
    private resourceUrlTurbineUser = SERVER_API_URL + 'api/turbine-users';

    constructor(private http: HttpClient) {
    }

    create(turbineUserGroupRoles: ITurbineUserGroupRole): Observable<EntityResponseType> {
        return this.http
        .post<ITurbineUserGroupRole>(`${ this.resourceUrlTurbineUserGroupRoles }/save/`, turbineUserGroupRoles, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => res));
    }

    findUsuariosbyId(id: number): Observable<EntityArrayResponseType> {
        return this.http
        .get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUserGroupRoles }/find/${ id }`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => res));
    }

    finUsuariosAll(): Observable<EntityArrayResponseType> {
        return this.http
        .get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUserGroupRoles }/findAll`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => res));
    }

    finUsuariosAllTotal(): Observable<EntityArrayResponseType> {
        return this.http
        .get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUserGroupRoles }/findAllTotal`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => res));
    }

    finUsuariosAllRevision(codeOperator: string): Observable<EntityArrayResponseType> {
        return this.http.get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUserGroupRoles }-revision/${ codeOperator }`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => res));
    }

    deleteTurbineUserGroupRolesByUserByGroupByRole(idUser: number, idGroup: number, idRole: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrlTurbineUserGroupRoles }/deleteByUserByGroupByRole/${ idUser }/${ idGroup }/${ idRole }`, {
            observe: 'response'
        });
    }

    deleteDesgloseGruposByUserId(idUser: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrlDesgloseGrupos }/deleteByUser/${ idUser }`, { observe: 'response' });
    }

    iscanDeleteRevisor(idUser: number): Observable<HttpResponse<any>> {
        return this.http.get(`${ this.resourceUrlTurbineUser }/isCanDeleteRevisor/${ idUser }`, { observe: 'response' });
    }
}
