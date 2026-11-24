export interface ICompanyValue {
    companyName?: string;
    period1Value?: number;
    period2Value?: number;
}

export class CompanyValue {
    constructor(
        public companyName?: string,
        public period1Value?: number,
        public period2Value?: number
    ) {
    }
}
