package cl.tutorial.desglose.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * Representa la tabla \"sf_stf_operator\".
 * @author carlos eduardo rodriguez
 */
@ApiModel(description = "Representa la tabla \"sf_stf_operator\". @author carlos eduardo rodriguez")
@Entity
@Table(name = "SF_STF_OPERATOR")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SfStfOperator implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * BRANCH_CODE
     */
    @Id
    @NotNull
    @ApiModelProperty(value = "BRANCH_CODE", required = true)
    @Column(name = "branch_code", nullable = false)
    private Integer branchCode;

    /**
     * OPERATOR_CODE
     */
    @NotNull
    @ApiModelProperty(value = "OPERATOR_CODE", required = true)
    @Column(name = "operator_code", nullable = false)
    private Integer operatorCode;

    /**
     * Login
     */
    @ApiModelProperty(value = "Login")
    @Column(name = "login_name")
    private String loginName;

    /**
     * IS_SUPERVISOR
     */
    @ApiModelProperty(value = "IS_SUPERVISOR")
    @Column(name = "is_supervisor")
    private Integer isSupervisor;

    /**
     * SIGNED_ON
     */
    @ApiModelProperty(value = "SIGNED_ON")
    @Column(name = "signed_on")
    private Integer signedOn;

    /**
     * DAY_OPEN
     */
    @ApiModelProperty(value = "DAY_OPEN")
    @Column(name = "day_open")
    private Integer dayOpen;


    public Integer getBranchCode() {
        return branchCode;
    }

    public SfStfOperator branchCode(Integer branchCode) {
        this.branchCode = branchCode;
        return this;
    }

    public void setBranchCode(Integer branchCode) {
        this.branchCode = branchCode;
    }

    public Integer getOperatorCode() {
        return operatorCode;
    }

    public SfStfOperator operatorCode(Integer operatorCode) {
        this.operatorCode = operatorCode;
        return this;
    }

    public void setOperatorCode(Integer operatorCode) {
        this.operatorCode = operatorCode;
    }

    public String getLoginName() {
        return loginName;
    }

    public SfStfOperator loginName(String loginName) {
        this.loginName = loginName;
        return this;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getIsSupervisor() {
        return isSupervisor;
    }

    public SfStfOperator isSupervisor(Integer isSupervisor) {
        this.isSupervisor = isSupervisor;
        return this;
    }

    public void setIsSupervisor(Integer isSupervisor) {
        this.isSupervisor = isSupervisor;
    }

    public Integer getSignedOn() {
        return signedOn;
    }

    public SfStfOperator signedOn(Integer signedOn) {
        this.signedOn = signedOn;
        return this;
    }

    public void setSignedOn(Integer signedOn) {
        this.signedOn = signedOn;
    }

    public Integer getDayOpen() {
        return dayOpen;
    }

    public SfStfOperator dayOpen(Integer dayOpen) {
        this.dayOpen = dayOpen;
        return this;
    }

    public void setDayOpen(Integer dayOpen) {
        this.dayOpen = dayOpen;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        SfStfOperator that = (SfStfOperator) o;
        return Objects.equals(branchCode, that.branchCode) && Objects.equals(operatorCode, that.operatorCode) && Objects.equals(loginName, that.loginName) && Objects
            .equals(isSupervisor, that.isSupervisor) && Objects.equals(signedOn, that.signedOn) && Objects.equals(dayOpen, that.dayOpen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchCode, operatorCode, loginName, isSupervisor, signedOn, dayOpen);
    }

    @Override
    public String toString() {
        return "SfStfOperator{" + "branchCode=" + branchCode + ", operatorCode=" + operatorCode + ", loginName='" + loginName + '\'' + ", isSupervisor=" + isSupervisor
            + ", signedOn=" + signedOn + ", dayOpen=" + dayOpen + '}';
    }
}
