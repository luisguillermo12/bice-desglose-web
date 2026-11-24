import { NgModule } from '@angular/core';
import { FilterDescripcionCodigoPipe } from 'app/shared/pipes/filter-descripcion-codigo.pipe';
import { FilterClientsRutNoRutPipe } from 'app/shared/pipes/filter-clients-rut-no-rut.pipe';
import { TransformIvaPipe } from 'app/shared/pipes/transform-iva.pipe';

@NgModule({
    imports: [],
    declarations: [FilterDescripcionCodigoPipe, FilterClientsRutNoRutPipe, TransformIvaPipe],
    exports: [FilterDescripcionCodigoPipe, FilterClientsRutNoRutPipe, TransformIvaPipe],
    providers: [FilterDescripcionCodigoPipe, TransformIvaPipe]
})
export class SharedPipesModule {}
