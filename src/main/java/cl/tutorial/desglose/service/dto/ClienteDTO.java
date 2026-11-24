package cl.tutorial.desglose.service.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the ClienteJuridico entity.
 */
public class ClienteDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String  rut;
    private String  rutVerificationNumber;
    @NotNull
    @Size(max = 40)
    private String  name;
    @Size(max = 20)
    private String  fatherLastName;
    @Size(max = 20)
    private String  motherLastName;
    private String  activity;
    private String  clientCode;
    private String  clientType;
    private String  identityType;
    private String  sector;
    private String  subSector;
    private Boolean isCompany;

    private BreakdownSearchResponseDTO breakdownSearchResponseDTO;

    private List<String> warnings;

    public List<String> getWarnings() {
        return warnings;
    }

    public void setWarnings(List<String> warnings) {
        this.warnings = warnings;
    }

    public void addWarning(String warning) {
        if (this.warnings == null) {
            this.warnings = new ArrayList<>();
        }
        this.warnings.add(warning);
    }

    public BreakdownSearchResponseDTO getBreakdownSearchResponseDTO() {
        return breakdownSearchResponseDTO;
    }

    public void setBreakdownSearchResponseDTO(BreakdownSearchResponseDTO breakdownSearchResponseDTO) {
        this.breakdownSearchResponseDTO = breakdownSearchResponseDTO;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean isCompany() {
        return isCompany;
    }

    public void setIsCompany(Boolean company) {
        isCompany = company;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public String getClientType() {
        return clientType;
    }

    public void setClientType(String clientType) {
        this.clientType = clientType;
    }

    public String getFatherLastName() {
        return fatherLastName;
    }

    public void setFatherLastName(String fatherLastName) {
        this.fatherLastName = fatherLastName;
    }

    public String getMotherLastName() {
        return motherLastName;
    }

    public void setMotherLastName(String motherLastName) {
        this.motherLastName = motherLastName;
    }

    public String getIdentityType() {
        return identityType;
    }

    public void setIdentityType(String identityType) {
        this.identityType = identityType;
    }

    public String getRutVerificationNumber() {
        return rutVerificationNumber;
    }

    public void setRutVerificationNumber(String rutVerificationNumber) {
        this.rutVerificationNumber = rutVerificationNumber;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getSubSector() {
        return subSector;
    }

    public void setSubSector(String subSector) {
        this.subSector = subSector;
    }

    public Boolean getCompany() {
        return isCompany;
    }

    public void setCompany(Boolean company) {
        isCompany = company;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ClienteDTO))
            return false;
        ClienteDTO that = (ClienteDTO) o;
        return Objects.equals(rut, that.rut) && Objects.equals(name, that.name) && Objects.equals(isCompany, that.isCompany) && Objects.equals(activity, that.activity) && Objects
            .equals(clientCode, that.clientCode) && Objects.equals(clientType, that.clientType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut, name, isCompany, activity, clientCode, clientType);
    }

    @Override
    public String toString() {
        return "ClienteDTO{" + "rut='" + rut + '\'' + ", rutVerificationNumber='" + rutVerificationNumber + '\'' + ", name='" + name + '\'' + ", fatherLastName='" + fatherLastName
            + '\'' + ", motherLastName='" + motherLastName + '\'' + ", activity='" + activity + '\'' + ", clientCode='" + clientCode + '\'' + ", clientType='" + clientType + '\''
            + ", identityType='" + identityType + '\'' + ", sector='" + sector + '\'' + ", subSector='" + subSector + '\'' + ", isCompany=" + isCompany + ", company=" + isCompany()
            + ", company=" + getCompany() + '}';
    }

}
