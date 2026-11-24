import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { ILoadDeudaSbifDTO } from 'app/shared/model/load-deuda-sbif-deudasbif.model';
import { IDatosConsultaSbifRequestDTO } from 'app/shared/model/load-deuda-sbif-datos-request';
import { DATE_FORMAT } from 'app/shared';
import { forEach } from '@angular/router/src/utils/collection';
import * as moment from 'moment';
import {isNullOrUndefined} from 'util';

type EntityResponseType = HttpResponse<ILoadDeudaSbifDTO>;
type EntityArrayResponseType = HttpResponse<ILoadDeudaSbifDTO[]>;

@Injectable({ providedIn: 'root' })
export class LoadDeudaSbifService {

    private OBTENER_LOAD_DEUDA_SBIF_URL = SERVER_API_URL + 'api/load-deuda-sbif-obtener';

    constructor(private http: HttpClient) {
    }

    findLoadDeudaSbif(datosConsultaSbifRequestDTO: IDatosConsultaSbifRequestDTO): Observable<any> {
        const copy = this.convertDateFromClient(datosConsultaSbifRequestDTO);
        return this.http.post(this.OBTENER_LOAD_DEUDA_SBIF_URL, copy, {
            'responseType': 'arraybuffer' as 'json'
        })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateFromServer(res)));
    }

    private convertDateFromClient(datosConsultaSbifRequestDTO: IDatosConsultaSbifRequestDTO): IDatosConsultaSbifRequestDTO {
        const fec = datosConsultaSbifRequestDTO.fecConsulta != null && datosConsultaSbifRequestDTO.fecConsulta.isValid()
            ? datosConsultaSbifRequestDTO.fecConsulta.format(DATE_FORMAT)
            : null;
        const fecPeriodoCopy = [];
        if (datosConsultaSbifRequestDTO.arrPERIODOS != null && datosConsultaSbifRequestDTO.arrPERIODOS.length > 0) {
            datosConsultaSbifRequestDTO.arrPERIODOS.forEach(fecPeriodo => {
                const fechaPeriodo = fecPeriodo != null && fecPeriodo.isValid() ? fecPeriodo.format(DATE_FORMAT) : null;
                fecPeriodoCopy.push(fechaPeriodo);
            });
        }
        const copy: IDatosConsultaSbifRequestDTO = Object.assign({}, datosConsultaSbifRequestDTO, {
            fecConsulta: fec,
            arrPERIODOS: fecPeriodoCopy
        });

        return copy;
    }

    private convertDateFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        res.body.forEach(data => {
            data.fecInforme = data.fecInforme != null ? moment(data.fecInforme) : null;
        });
        return res;
    }

}
