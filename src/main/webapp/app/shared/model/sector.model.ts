export interface ISector {
    codSector?: string;
    glsSector?: string;
}

export class Sector implements ISector {
    constructor(public codSector?: string, public glsSector?: string) {}
}
