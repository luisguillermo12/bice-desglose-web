export interface INoteValue {
    corConcept?: string;
    conceptName?: string;
    period1Value?: number;
    period2Value?: number;
    isTotal?: boolean;
    isHeader?: boolean;
}

export class NoteValue {
    constructor(
        public corConcept?: string,
        public conceptName?: string,
        public period1Value?: number,
        public period2Value?: number,
        public isTotal?: boolean,
        public isHeader?: boolean
    ) {
    }
}
