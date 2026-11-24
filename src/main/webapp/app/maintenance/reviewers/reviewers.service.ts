import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';
import { map } from 'rxjs/operators';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ITurbineUserGroupRole } from 'app/shared/model/turbine-user-group-role.model';
import { SERVER_API_URL } from 'app/app.constants';
import { IDesgloseGrupo } from 'app/shared/model/desglose-grupo.model';
import { IDesgloseFirma } from 'app/shared/model/desglose-firma.model';

type EntityArrayResponseType = HttpResponse<ITurbineUsuarios[]>;
type EntityResponseType = HttpResponse<ITurbineUserGroupRole>;
type EntityResponseTypeDesgloseFirma = HttpResponse<IDesgloseFirma>;
@Injectable({
    providedIn: 'root'
})
export class ReviewersService {
    private resourceUrlTurbineUserGroupRoles = SERVER_API_URL + 'api/turbine-user-group-roles';
    private resourceUrlDesgloseFirma = SERVER_API_URL + 'api/desglose-firmas';
    constructor(private http: HttpClient) {}
    findUsuariosbyId(id: number): Observable<EntityArrayResponseType> {
        return this.http
            .get<ITurbineUsuarios[]>(`${this.resourceUrlTurbineUserGroupRoles}/find/${id}`, { observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => res));
    }
    finAllFirmas(): Observable<HttpResponse<object>> {
        return this.http.get(`${this.resourceUrlDesgloseFirma}/findAllFirmas`, { observe: 'response' }).pipe(map((res: any) => res));
    }
    createDesgloseFirma(desgloseFirma: IDesgloseFirma): Observable<EntityResponseTypeDesgloseFirma> {
        return this.http
            .post<IDesgloseFirma>(this.resourceUrlDesgloseFirma, desgloseFirma, { observe: 'response' })
            .pipe(map((res: EntityResponseTypeDesgloseFirma) => res));
    }
    deleteDesgloseFirma(desgloseFirma: IDesgloseFirma): Observable<HttpResponse<void>> {
        return this.http.delete<any>(
            `${this.resourceUrlDesgloseFirma}/${desgloseFirma.codCargo}/${desgloseFirma.numRolEmpleado}/${
                desgloseFirma.numRolEmpleado2fir
            }`,
            { observe: 'response' }
        );
    }
}
