import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { ISector } from 'app/shared/model/sector.model';
import { IEconomicActivity } from 'app/shared/model/economic-activity.model';
import { ISubSector } from 'app/shared/model/sub-sector.model';

type EntityArrayResponseTypeSector = HttpResponse<ISector[]>;
type EntityArrayResponseTypeSubSector = HttpResponse<ISubSector[]>;
type EntityArrayResponseTypeActivity = HttpResponse<IEconomicActivity[]>;

@Injectable({ providedIn: 'root' })
export class CustomSqlService {
    private resourceUrl = SERVER_API_URL + 'api/custom-sql';
    private GET_ALL_ECONOMIC_ACTIVITIES = this.resourceUrl + '/get-all-economic-activities';
    private GET_ALL_SECTORS = this.resourceUrl + '/get-all-sectors';
    private GET_ALL_SUB_SECTORS = this.resourceUrl + '/get-all-subsectors';
    private GET_SUBSECTORS_BY_SECTOR = this.resourceUrl + '/get-subsectors-by-sector';
    private GET_ALL_ECONOMIC_ACTIVITIES_BY_SECTOR = this.resourceUrl + '/get-economic-activities-by-sector';

    constructor(private http: HttpClient) {
    }

    /**
     * Obtener todas las actividades económicas.
     */
    getAllEconomicActivities(): Observable<EntityArrayResponseTypeActivity> {
        return this.http.get<IEconomicActivity[]>(`${this.GET_ALL_ECONOMIC_ACTIVITIES}`, {
            observe: 'response'
        });
    }

    /**
     * Obtener todas los sectores.
     */
    getAllSectors(): Observable<EntityArrayResponseTypeSector> {
        return this.http.get<ISector[]>(`${this.GET_ALL_SECTORS}`, {
            observe: 'response'
        });
    }

    /**
     * Obtener todas los sub sectores.
     */
    getAllSubSectors(): Observable<EntityArrayResponseTypeSubSector> {
        return this.http.get<ISubSector[]>(`${this.GET_ALL_SUB_SECTORS}`, {
            observe: 'response'
        });
    }

    /**
     * Obtener los sub-sectores por código de sector.
     */
    getSubSectorsBySector(codSector: string): Observable<EntityArrayResponseTypeSubSector> {
        return this.http.get<ISubSector[]>(`${this.GET_SUBSECTORS_BY_SECTOR}?codSector=${codSector}`, {
            observe: 'response'
        });
    }

    /**
     * Obtener todas los sectores por código de sector.
     */
    getEconomicActivitiesBySector(codSector: string): Observable<EntityArrayResponseTypeActivity> {
        return this.http.get<IEconomicActivity[]>(`${this.GET_ALL_ECONOMIC_ACTIVITIES_BY_SECTOR}?codSector=${codSector}`, {
            observe: 'response'
        });
    }
}
