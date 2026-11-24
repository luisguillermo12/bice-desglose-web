import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IBlackListJwt } from 'app/shared/model/black-list-jwt.model';

type EntityResponseType = HttpResponse<IBlackListJwt>;
type EntityArrayResponseType = HttpResponse<IBlackListJwt[]>;

@Injectable({ providedIn: 'root' })
export class BlackListJwtService {
    private resourceUrl = SERVER_API_URL + 'api/black-list-jwts';

    constructor(private http: HttpClient) {}

    create(blackListJwt: IBlackListJwt): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(blackListJwt);
        return this.http
            .post<IBlackListJwt>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(blackListJwt: IBlackListJwt): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(blackListJwt);
        return this.http
            .put<IBlackListJwt>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IBlackListJwt>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IBlackListJwt[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    private convertDateFromClient(blackListJwt: IBlackListJwt): IBlackListJwt {
        const copy: IBlackListJwt = Object.assign({}, blackListJwt, {
            invalidateTime:
                blackListJwt.invalidateTime != null && blackListJwt.invalidateTime.isValid() ? blackListJwt.invalidateTime.toJSON() : null
        });
        return copy;
    }

    private convertDateFromServer(res: EntityResponseType): EntityResponseType {
        res.body.invalidateTime = res.body.invalidateTime != null ? moment(res.body.invalidateTime) : null;
        return res;
    }

    private convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach((blackListJwt: IBlackListJwt) => {
            blackListJwt.invalidateTime = blackListJwt.invalidateTime != null ? moment(blackListJwt.invalidateTime) : null;
        });
        return res;
    }
}
