import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { ICuadre } from 'app/shared/model/cuadre.model';

type EntityResponseType = HttpResponse<ICuadre>;
type EntityArrayResponseType = HttpResponse<ICuadre[]>;

@Injectable({ providedIn: 'root' })
export class CuadreService {
    private resourceUrl = SERVER_API_URL + 'api/cuadres';

    constructor(private http: HttpClient) {}

    create(cuadre: ICuadre): Observable<EntityResponseType> {
        return this.http.post<ICuadre>(this.resourceUrl, cuadre, { observe: 'response' });
    }

    update(cuadre: ICuadre): Observable<EntityResponseType> {
        return this.http.put<ICuadre>(this.resourceUrl, cuadre, { observe: 'response' });
    }

    find(codTpoConcep1: string, corConcepto1: number, codTpoConcep2: string, corConcepto2: number): Observable<EntityResponseType> {
        return this.http.get<ICuadre>(`${this.resourceUrl}/${codTpoConcep1}/${corConcepto1}/${codTpoConcep2}/${corConcepto2}`, {
            observe: 'response'
        });
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http.get<ICuadre[]>(this.resourceUrl, { params: options, observe: 'response' });
    }

    delete(codTpoConcep1: string, corConcepto1: number, codTpoConcep2: string, corConcepto2: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${codTpoConcep1}/${corConcepto1}/${codTpoConcep2}/${corConcepto2}`, {
            observe: 'response'
        });
    }
}
