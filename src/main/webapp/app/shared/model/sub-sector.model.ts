export interface ISubSector {
    codSector?: string;
    codSubSector?: string;
    glsSubSector?: string;
}

export class SubSector implements ISubSector {
    constructor(public codSector?: string, public codSubSector?: string, public glsSubSector?: string) {}
}
