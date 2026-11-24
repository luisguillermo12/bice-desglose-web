package cl.tutorial.desglose.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Desglose.
 * <p>
 * Properties are configured in the application.yml file. See {@link io.github.jhipster.config.JHipsterProperties} for a good example.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private Integer chanelCode;
    private String  applicationName;
    private Integer branchOfficeCode;
    private Long    threadTaskSchedulerInterval;
    private Boolean activeUniqueSessionControl;
    private Integer timeActiveSession;

    public Integer getChanelCode() {
        return chanelCode;
    }

    public void setChanelCode(Integer chanelCode) {
        this.chanelCode = chanelCode;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Integer getBranchOfficeCode() {
        return branchOfficeCode;
    }

    public void setBranchOfficeCode(Integer branchOfficeCode) {
        this.branchOfficeCode = branchOfficeCode;
    }

    public Long getThreadTaskSchedulerInterval() {
        return threadTaskSchedulerInterval;
    }

    public void setThreadTaskSchedulerInterval(Long threadTaskSchedulerInterval) {
        this.threadTaskSchedulerInterval = threadTaskSchedulerInterval;
    }

    public Boolean getActiveUniqueSessionControl() {
        return activeUniqueSessionControl;
    }

    public void setActiveUniqueSessionControl(Boolean activeUniqueSessionControl) {
        this.activeUniqueSessionControl = activeUniqueSessionControl;
    }

    public Integer getTimeActiveSession() {
        return timeActiveSession;
    }

    public void setTimeActiveSession(Integer timeActiveSession) {
        this.timeActiveSession = timeActiveSession;
    }

    @Override
    public String toString() {
        return "ApplicationProperties{" + "chanelCode=" + chanelCode + ", applicationName='" + applicationName + '\'' + ", branchOfficeCode=" + branchOfficeCode
            + ", threadTaskSchedulerInterval=" + threadTaskSchedulerInterval + ", activeUniqueSessionControl=" + activeUniqueSessionControl + ", timeActiveSession="
            + timeActiveSession + '}';
    }
}
