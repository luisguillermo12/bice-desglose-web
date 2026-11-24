package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the SfStfOperator entity.
 */
public class SfStfOperatorDTO implements Serializable {


    @NotNull
    private Integer branchCode;

    @NotNull
    private Integer operatorCode;

    private String loginName;

    private Integer isSupervisor;

    private Integer signedOn;

    private Integer dayOpen;


    public Integer getBranchCode() {
        return branchCode;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getOperatorCode() {
        return operatorCode;
    }

    public void setOperatorCode(Integer operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getIsSupervisor() {
        return isSupervisor;
    }

    public void setIsSupervisor(Integer isSupervisor) {
        this.isSupervisor = isSupervisor;
    }

    public Integer getSignedOn() {
        return signedOn;
    }

    public void setSignedOn(Integer signedOn) {
        this.signedOn = signedOn;
    }

    public Integer getDayOpen() {
        return dayOpen;
    }

    public void setDayOpen(Integer dayOpen) {
        this.dayOpen = dayOpen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof SfStfOperatorDTO))
            return false;
        SfStfOperatorDTO that = (SfStfOperatorDTO) o;
        return getBranchCode().equals(that.getBranchCode()) && getOperatorCode().equals(that.getOperatorCode()) && getLoginName().equals(that.getLoginName()) && getIsSupervisor()
            .equals(that.getIsSupervisor()) && getSignedOn().equals(that.getSignedOn()) && getDayOpen().equals(that.getDayOpen());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getBranchCode(), getOperatorCode(), getLoginName(), getIsSupervisor(), getSignedOn(), getDayOpen());
    }

    @Override
    public String toString() {
        return "SfStfOperatorDTO{" + "branchCode=" + branchCode + ", operatorCode=" + operatorCode + ", loginName='" + loginName + '\'' + ", isSupervisor=" + isSupervisor
            + ", signedOn=" + signedOn + ", dayOpen=" + dayOpen + '}';
    }
}
