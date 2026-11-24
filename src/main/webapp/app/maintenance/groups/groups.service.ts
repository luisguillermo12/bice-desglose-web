import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { map } from 'rxjs/operators';
import { ITurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';
import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';

type EntityArrayResponseType = HttpResponse<ITurbineUsuarios[]>;
type EntityResponseType = HttpResponse<ITurbineUserGroupRole>;
type EntityResponseTypeDesgloseGrupo = HttpResponse<IDesgloseGrupo>;

@Injectable({ providedIn: 'root' })
export class GroupsService {
    private resourceUrlTurbineUserGroupRoles = SERVER_API_URL + 'api/turbine-user-group-roles';
    private resourceUrlDesgloseGrupos = SERVER_API_URL + 'api/desglose-grupos';

    constructor(private http: HttpClient) {
    }

    findEjecutoresSinGrupo(): Observable<EntityArrayResponseType> {
        return this.http
        .get<ITurbineUsuarios[]>(`${ this.resourceUrlTurbineUserGroupRoles }/findAllEjecutoresSinGrupo`, { observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => res));
    }

    finAllGrupos(): Observable<HttpResponse<object>> {
        return this.http
        .get(`${ this.resourceUrlTurbineUserGroupRoles }/findAllGrupos`, { observe: 'response' })
        .pipe(map((res: any) => res));
    }

    create(desgloseGrupo: IDesgloseGrupo): Observable<EntityResponseTypeDesgloseGrupo> {
        return this.http
        .post<IDesgloseGrupo>(this.resourceUrlDesgloseGrupos, desgloseGrupo, { observe: 'response' })
        .pipe(map((res: EntityResponseTypeDesgloseGrupo) => res));
    }

    deleteDesgloseGruposByEntidad(desgloseGrupo: IDesgloseGrupo): Observable<HttpResponse<any>> {
        return this.http.delete<any>(
            `${ this.resourceUrlDesgloseGrupos }/eliminaEntidad/${ desgloseGrupo.codCargo }/${ desgloseGrupo.numRolEmpleado }/${
                desgloseGrupo.desCodCargo
                }/${ desgloseGrupo.desNumRolEmpleado }`,
            { observe: 'response' }
        );
    }
}
