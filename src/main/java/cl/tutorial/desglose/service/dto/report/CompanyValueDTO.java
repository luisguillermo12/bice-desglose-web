package cl.tutorial.desglose.service.dto.report;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * A DTO for the /download/informe_resumen_desglose_balance_A4 service.
 */
public class CompanyValueDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String     companyName;
    private BigDecimal period1Value;
    private BigDecimal period2Value;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public BigDecimal getPeriod1Value() {
        return period1Value;
    }

    public void setPeriod1Value(BigDecimal period1Value) {
        this.period1Value = period1Value;
    }

    public BigDecimal getPeriod2Value() {
        return period2Value;
    }

    public void setPeriod2Value(BigDecimal period2Value) {
        this.period2Value = period2Value;
    }

    @Override
    public String toString() {
        return "CompanyValueDTO{" + "companyName='" + companyName + '\'' + ", period1Value='" + period1Value + '\'' + ", period2Value='" + period2Value + '\'' + '}';
    }
}
