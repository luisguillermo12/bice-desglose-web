import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ITurbineUser } from 'app/shared/model/turbine-user.model';
import { ITurbineUsuarios } from 'app/shared/model/turbine-usuarios.model';

type EntityResponseType = HttpResponse<ITurbineUser>;
type EntityArrayResponseType = HttpResponse<ITurbineUser[]>;
type EntityUsuariosResponseType = HttpResponse<ITurbineUsuarios>;
type EntityUsuariosArrayResponseType = HttpResponse<ITurbineUsuarios[]>;

@Injectable({ providedIn: 'root' })
export class TurbineUserService {
    private resourceUrl = SERVER_API_URL + 'api/turbine-users';
    private resourceUrlFindByRole = SERVER_API_URL + 'api/turbine-users/findByRole';
    private resourceUrlFindByRoleIsReplacement = SERVER_API_URL + 'api/turbine-users/findByRoleIsReemplazo';

    constructor(private http: HttpClient) {
    }

    create(turbineUser: ITurbineUser): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(turbineUser);
        return this.http
        .post<ITurbineUser>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(turbineUser: ITurbineUser): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(turbineUser);
        return this.http
        .put<ITurbineUser>(this.resourceUrl, copy, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
        .get<ITurbineUser>(`${ this.resourceUrl }/${ id }`, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findLoginName(loginName: string): Observable<EntityResponseType> {
        return this.http
        .get<ITurbineUser>(`${ this.resourceUrl }/findByLoginName/${ loginName }`, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findByLoginNameSfStf(loginName: string): Observable<EntityResponseType> {
        return this.http
        .get<ITurbineUser>(`${ this.resourceUrl }/findByLoginNameSfStf/${ loginName }`, { observe: 'response' })
        .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    findByRoleisReemplazo(role?: string): Observable<EntityUsuariosResponseType> {
        return this.http
        .get<ITurbineUsuarios>(`${ this.resourceUrlFindByRoleIsReplacement }/${ role }`, { observe: 'response' });
    }

    findByRole(role?: string): Observable<EntityUsuariosResponseType> {
        return this.http
        .get<ITurbineUser>(`${ this.resourceUrlFindByRole }/${ role }`, { observe: 'response' });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
        .get<ITurbineUser[]>(this.resourceUrl, { params: options, observe: 'response' })
        .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ id }`, { observe: 'response' });
    }

    private convertDateFromClient(turbineUser: ITurbineUser): ITurbineUser {
        const copy: ITurbineUser = Object.assign({}, turbineUser, {
            modified: turbineUser.modified != null && turbineUser.modified.isValid() ? turbineUser.modified.format(DATE_FORMAT) : null,
            created: turbineUser.created != null && turbineUser.created.isValid() ? turbineUser.created.format(DATE_FORMAT) : null,
            lastLogin: turbineUser.lastLogin != null && turbineUser.lastLogin.isValid() ? turbineUser.lastLogin.format(DATE_FORMAT) : null,
            expire: turbineUser.expire != null && turbineUser.expire.isValid() ? turbineUser.expire.format(DATE_FORMAT) : null,
            expirePassword: turbineUser.expirePassword != null && turbineUser.expirePassword.isValid() ? turbineUser.expirePassword.format(DATE_FORMAT) : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.modified = res.body.modified != null ? moment(res.body.modified) : null;
        res.body.created = res.body.created != null ? moment(res.body.created) : null;
        res.body.lastLogin = res.body.lastLogin != null ? moment(res.body.lastLogin) : null;
        res.body.expire = res.body.expire != null ? moment(res.body.expire) : null;
        res.body.expirePassword = res.body.expirePassword != null ? moment(res.body.expirePassword) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((turbineUser: ITurbineUser) => {
            turbineUser.modified = turbineUser.modified != null ? moment(turbineUser.modified) : null;
            turbineUser.created = turbineUser.created != null ? moment(turbineUser.created) : null;
            turbineUser.lastLogin = turbineUser.lastLogin != null ? moment(turbineUser.lastLogin) : null;
            turbineUser.expire = turbineUser.expire != null ? moment(turbineUser.expire) : null;
            turbineUser.expirePassword = turbineUser.expirePassword != null ? moment(turbineUser.expirePassword) : null;
        });
        return res;
    }
}
