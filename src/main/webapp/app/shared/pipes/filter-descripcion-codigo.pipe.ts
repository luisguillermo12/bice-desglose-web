import { Pipe, PipeTransform } from '@angular/core';
import { isNullOrUndefined } from 'util';
import { IDescripcionCodigo } from 'app/shared/model/descripcion-codigo.model';
import { CODTABLA_TIPO_MONEDAS } from 'app/app.constants';

/*
 * Filtra los valores del array que sean del tipo de codTabla dado.
 * Usage:
 *   value | filterDescripcionCodigo:codTabla
 * Example:
 *   {{ [elem1, elem2, elem3] | filterDescripcionCodigo:'MONEDAS' }}
 *   resulta en : [elem1]
*/
@Pipe({ name: 'filterDescripcionCodigo' })
export class FilterDescripcionCodigoPipe implements PipeTransform {
    transform(value: Array<IDescripcionCodigo>, codTabla: string): Array<IDescripcionCodigo> {
        if (!isNullOrUndefined(value) && !isNullOrUndefined(codTabla)) {
            let filterResult;
            /**
             * Parche pra ordenas en la interfaz las monedas alfabéticamente y sacar las que tengan código genérico mayor a 300.
             */
            if (CODTABLA_TIPO_MONEDAS === codTabla) {
                filterResult = value.filter(elem => elem.codTabla === codTabla && Number(elem.codGenerico) <= 300);
                const sorted = filterResult.sort((t1, t2) => {
                    const name1 = t1.glsGenericalarga.toLowerCase();
                    const name2 = t2.glsGenericalarga.toLowerCase();
                    if (name1 > name2) {
                        return 1;
                    }
                    if (name1 < name2) {
                        return -1;
                    }
                    return 0;
                });
                return sorted;
            } else {
                filterResult = value.filter(elem => elem.codTabla === codTabla);

            }
            return filterResult;
        }
    }
}
