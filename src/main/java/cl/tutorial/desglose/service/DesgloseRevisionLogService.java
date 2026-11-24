package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseRevisionLog;
import cl.tutorial.desglose.domain.pk.DesgloseRevisionLogPK;
import cl.tutorial.desglose.repository.DesgloseRevisionLogRepository;
import cl.tutorial.desglose.service.dto.DesgloseRevisionLogDTO;
import cl.tutorial.desglose.service.mapper.DesgloseRevisionLogMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DesgloseRevisionLog.
 */
@Service
@Transactional
public class DesgloseRevisionLogService {

    private final Logger log = LogManager.getLogger(DesgloseRevisionLogService.class);

    private final DesgloseRevisionLogRepository desgloseRevisionLogRepository;

    private final DesgloseRevisionLogMapper desgloseRevisionLogMapper;

    public DesgloseRevisionLogService(DesgloseRevisionLogRepository desgloseRevisionLogRepository, DesgloseRevisionLogMapper desgloseRevisionLogMapper) {
        this.desgloseRevisionLogRepository = desgloseRevisionLogRepository;
        this.desgloseRevisionLogMapper = desgloseRevisionLogMapper;
    }

    /**
     * Save a desgloseRevisionLog.
     *
     * @param desgloseRevisionLogDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseRevisionLogDTO save(DesgloseRevisionLogDTO desgloseRevisionLogDTO) {
        log.info("INICIO - Request to save DesgloseRevisionLog : {}", desgloseRevisionLogDTO);

        DesgloseRevisionLog desgloseRevisionLog = desgloseRevisionLogMapper.toEntity(desgloseRevisionLogDTO);
        desgloseRevisionLog = desgloseRevisionLogRepository.save(desgloseRevisionLog);
        log.info("FIN - Request to save DesgloseRevisionLog : {}", desgloseRevisionLogDTO);
        return desgloseRevisionLogMapper.toDto(desgloseRevisionLog);
    }

    /**
     * Get all the desgloseRevisionLogs.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseRevisionLogDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseRevisionLogs");
        log.info("FIN - Request to get all DesgloseRevisionLogs");
        return desgloseRevisionLogRepository.findAll(pageable).map(desgloseRevisionLogMapper::toDto);
    }

    /**
     * Get one desgloseRevisionLog by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseRevisionLogDTO> findOne(DesgloseRevisionLogPK id) {
        log.info("INICIO - Request to get DesgloseRevisionLog : {}", id);
        log.info("FIN - Request to get DesgloseRevisionLog : {}", id);
        return desgloseRevisionLogRepository.findById(id).map(desgloseRevisionLogMapper::toDto);
    }

    /**
     * Delete the desgloseRevisionLog by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseRevisionLogPK id) {
        log.info("INICIO - Request to delete DesgloseRevisionLog : {}", id);
        log.info("FIN - Request to delete DesgloseRevisionLog : {}", id);
        desgloseRevisionLogRepository.deleteById(id);
    }
}
