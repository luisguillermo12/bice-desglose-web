import { Pipe, PipeTransform } from '@angular/core';
import { isNullOrUndefined } from 'util';

@Pipe({ name: 'transformIvaPipe' })
export class TransformIvaPipe implements PipeTransform {
    transform(value: number): number {
        if (!isNullOrUndefined(value)) {
            return Math.trunc(value * 100);
        }
    }
}
