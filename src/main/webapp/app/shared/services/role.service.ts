import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { SERVER_API_URL } from 'app/app.constants';
import { ITurbineRole } from 'app/shared/model/turbine-role.model';

type EntityArrayResponseType = HttpResponse<ITurbineRole>;

@Injectable({ providedIn: 'root' })
export class RoleService {
    private resourceUrl = SERVER_API_URL + 'api/turbine-roles';
    private GET_BYROLENAME = this.resourceUrl + '/findByRoleName';

    constructor(private http: HttpClient) {
    }

    /**
     * @author Carlos Eduardo Rodriguez Perez
     * Obtener Un Rol pasandole como parametro el nombre
     * @param roleName
     */
    getByRoleName(roleName: string): Observable<EntityArrayResponseType> {
        return this.http.get<ITurbineRole>(`${ this.GET_BYROLENAME }/${ roleName }`, {
            observe: 'response'
        });
    }
}
