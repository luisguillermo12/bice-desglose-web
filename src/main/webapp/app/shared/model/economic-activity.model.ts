export interface IEconomicActivity {
    codSector?: string;
    codSubsector?: string;
    codActividad?: string;
    glsActividad?: string;
}

export class EconomicActivity implements IEconomicActivity {
    constructor(public codSector?: string, public codSubsector?: string, public codActividad?: string, public glsActividad?: string) {}
}
