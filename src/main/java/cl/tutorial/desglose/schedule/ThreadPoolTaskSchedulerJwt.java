package cl.tutorial.desglose.schedule;

import cl.tutorial.desglose.config.ApplicationProperties;
import cl.tutorial.desglose.domain.Desglose;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.service.BlackListJwtService;
import cl.tutorial.desglose.service.DesgloseService;
import cl.tutorial.desglose.service.UniqueSessionControlService;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;
import sun.security.krb5.internal.crypto.Des;

import javax.annotation.PostConstruct;

@Component
public class ThreadPoolTaskSchedulerJwt {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final BlackListJwtService     blackListJwtService;
    private final ApplicationProperties   applicationProperties;
    private final UniqueSessionControlService uniqueSessionControlService;
    private final DesgloseService desgloseService;

    public ThreadPoolTaskSchedulerJwt(ThreadPoolTaskScheduler taskScheduler, DesgloseService desgloseService, BlackListJwtService blackListJwtService, UniqueSessionControlService uniqueSessionControlService,
                                      ApplicationProperties applicationProperties) {
        this.taskScheduler = taskScheduler;
        this.blackListJwtService = blackListJwtService;
        this.applicationProperties = applicationProperties;
        this.uniqueSessionControlService = uniqueSessionControlService;
        this.desgloseService= desgloseService;
    }

    @PostConstruct
    public void scheduleRunnableWithCronTrigger() {
        if (applicationProperties.getActiveUniqueSessionControl()) {
            try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("loginId", "System")) {
                taskScheduler.scheduleWithFixedDelay(new RunnableTaskDeleteOldBlackListJwt(blackListJwtService,desgloseService, uniqueSessionControlService), applicationProperties.getThreadTaskSchedulerInterval());
            }
        }
    }
}
