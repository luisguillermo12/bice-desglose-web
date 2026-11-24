import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IUniqueSessionControl } from 'app/shared/model/unique-session-control.model';

type EntityResponseType = HttpResponse<IUniqueSessionControl>;
type EntityArrayResponseType = HttpResponse<IUniqueSessionControl[]>;

@Injectable({ providedIn: 'root' })
export class UniqueSessionControlService {
    private resourceUrl = SERVER_API_URL + 'api/unique-session-controls';
    private resourceUrlDeleteList = SERVER_API_URL + 'api/unique-session-controls/delete-list';
    private resourceUrlGetAll = SERVER_API_URL + 'api/unique-session-controls/get-all';

    constructor(private http: HttpClient) {
    }

    create(uniqueSessionControl: IUniqueSessionControl): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(uniqueSessionControl);
        return this.http.post<IUniqueSessionControl>(this.resourceUrl, copy, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(uniqueSessionControl: IUniqueSessionControl): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(uniqueSessionControl);
        return this.http.put<IUniqueSessionControl>(this.resourceUrl, copy, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http.get<IUniqueSessionControl>(`${ this.resourceUrl }/${ id }`, { observe: 'response' }).pipe(
            map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<IUniqueSessionControl[]>(this.resourceUrl, { params: options, observe: 'response' }).pipe(
            map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    getAll(): Observable<EntityArrayResponseType> {
        return this.http.get<IUniqueSessionControl[]>(this.resourceUrlGetAll, { observe: 'response' }).pipe(
            map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${ this.resourceUrl }/${ id }`, { observe: 'response' });
    }

        deleteList(selectedItemsIds: number[]): Observable<EntityResponseType> {
        return this.http.post<any>(this.resourceUrlDeleteList, selectedItemsIds, { observe: 'response' });
    }

    private convertDateFromClient(uniqueSessionControl: IUniqueSessionControl): IUniqueSessionControl {
        const copy: IUniqueSessionControl = Object.assign({}, uniqueSessionControl, {
            sessionCheckTime: uniqueSessionControl.sessionCheckTime != null && uniqueSessionControl.sessionCheckTime.isValid() ? uniqueSessionControl.sessionCheckTime.toJSON() : null,
            sessionEndTime: uniqueSessionControl.sessionEndTime != null && uniqueSessionControl.sessionEndTime.isValid() ? uniqueSessionControl.sessionEndTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.sessionCheckTime = res.body.sessionCheckTime != null ? moment(res.body.sessionCheckTime) : null;
        res.body.sessionEndTime = res.body.sessionEndTime != null ? moment(res.body.sessionEndTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((uniqueSessionControl: IUniqueSessionControl) => {
            uniqueSessionControl.sessionCheckTime = uniqueSessionControl.sessionCheckTime != null ? moment(uniqueSessionControl.sessionCheckTime) : null;
            uniqueSessionControl.sessionEndTime = uniqueSessionControl.sessionEndTime != null ? moment(uniqueSessionControl.sessionEndTime) : null;
        });
        return res;
    }
}
