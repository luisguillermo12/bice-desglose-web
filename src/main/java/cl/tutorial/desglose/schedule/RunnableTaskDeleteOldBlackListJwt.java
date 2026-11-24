package cl.tutorial.desglose.schedule;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.UniqueSessionControl;
import cl.tutorial.desglose.service.BlackListJwtService;
import cl.tutorial.desglose.service.DesgloseService;
import cl.tutorial.desglose.service.UniqueSessionControlService;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;

public class RunnableTaskDeleteOldBlackListJwt implements Runnable {

    private final Logger log = LogManager.getLogger(RunnableTaskDeleteOldBlackListJwt.class);

    private final BlackListJwtService blackListJwtService;

    private final DesgloseService desgloseService;

    private final UniqueSessionControlService uniqueSessionControlService;
    public RunnableTaskDeleteOldBlackListJwt(BlackListJwtService blackListJwtService,DesgloseService desgloseService ,UniqueSessionControlService uniqueSessionControlService) {
        this.blackListJwtService = blackListJwtService;
        this.uniqueSessionControlService = uniqueSessionControlService;
        this.desgloseService = desgloseService;
    }

    @Override
    public void run() {
        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("loginId", "System")) {
            this.log.info("INICIO - Tarea para eliminar los jwt de la lista negra y tambien los uniqueSession  Hora: " + LocalDateTime.now().format(Constants.FORMATTER_DATE_TIME));
            Long deletedElementsCount = Constants.ZERO_AS_LONG;
            Long deletedElementsCount1 = Constants.ZERO_AS_LONG;
            try {
                deletedElementsCount = this.blackListJwtService.deleteAllExpiredJwtTokens();
                this.uniqueSessionControlService.updateExpireUniqueSession();
                // this.desgloseService.deleteAllDesglosesTemporal();
            } catch (Exception ex) {
                this.log.error(ex.getMessage());
            }
            this.log.info("FIN - Tarea para eliminar los jwt de la lista negra y tambien los uniqueSession mayor o igual a 5 minutos de inactividad.. Hora: " + LocalDateTime.now().format(Constants.FORMATTER_DATE_TIME) + " [deletedElementsCount = "
                + deletedElementsCount + " - " + deletedElementsCount1 + "]");
        }
    }
}
