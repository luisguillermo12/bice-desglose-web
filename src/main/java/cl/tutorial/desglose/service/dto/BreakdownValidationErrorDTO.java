package cl.tutorial.desglose.service.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for Breakdown Validation Error.
 */
public class BreakdownValidationErrorDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer   tableRow;
    private Integer   tableColumn;
    private String    errorMessage;
    private LocalDate period;
    private Boolean   isRequired;

    public Integer getTableRow() {
        return tableRow;
    }

    public void setTableRow(Integer tableRow) {
        this.tableRow = tableRow;
    }

    public Integer getTableColumn() {
        return tableColumn;
    }

    public void setTableColumn(Integer tableColumn) {
        this.tableColumn = tableColumn;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof BreakdownValidationErrorDTO))
            return false;
        BreakdownValidationErrorDTO that = (BreakdownValidationErrorDTO) o;
        return tableRow.equals(that.tableRow) && tableColumn.equals(that.tableColumn) && errorMessage.equals(that.errorMessage) && period.equals(that.period) && isRequired
            .equals(that.isRequired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableRow, tableColumn, errorMessage, period, isRequired);
    }

    @Override
    public String toString() {
        return "BreakdownValidationErrorDTO{" + "tableRow=" + tableRow + ", tableColumn=" + tableColumn + ", errorMessage='" + errorMessage + '\'' + ", period=" + period
            + ", isRequired=" + isRequired + '}';
    }
}
