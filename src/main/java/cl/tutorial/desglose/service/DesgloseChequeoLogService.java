package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseChequeoLog;
import cl.tutorial.desglose.domain.pk.DesgloseChequeoLogPK;
import cl.tutorial.desglose.repository.DesgloseChequeoLogRepository;
import cl.tutorial.desglose.service.dto.DesgloseChequeoLogDTO;
import cl.tutorial.desglose.service.mapper.DesgloseChequeoLogMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DesgloseChequeoLog.
 */
@Service
@Transactional
public class DesgloseChequeoLogService {

    private final Logger log = LogManager.getLogger(DesgloseChequeoLogService.class);

    private final DesgloseChequeoLogRepository desgloseChequeoLogRepository;

    private final DesgloseChequeoLogMapper desgloseChequeoLogMapper;

    public DesgloseChequeoLogService(DesgloseChequeoLogRepository desgloseChequeoLogRepository, DesgloseChequeoLogMapper desgloseChequeoLogMapper) {
        this.desgloseChequeoLogRepository = desgloseChequeoLogRepository;
        this.desgloseChequeoLogMapper = desgloseChequeoLogMapper;
    }

    /**
     * Save a desgloseChequeoLog.
     *
     * @param desgloseChequeoLogDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseChequeoLogDTO save(DesgloseChequeoLogDTO desgloseChequeoLogDTO) {
        log.info("INICIO - Request to save DesgloseChequeoLog : {}", desgloseChequeoLogDTO);
        DesgloseChequeoLog desgloseChequeoLog = desgloseChequeoLogMapper.toEntity(desgloseChequeoLogDTO);
        desgloseChequeoLog = desgloseChequeoLogRepository.save(desgloseChequeoLog);
        log.info("FIN - Request to save DesgloseChequeoLog : {}", desgloseChequeoLogDTO);
        return desgloseChequeoLogMapper.toDto(desgloseChequeoLog);
    }

    /**
     * Get all the desgloseChequeoLogs.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseChequeoLogDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseChequeoLogs");
        log.info("FIN - Request to get all DesgloseChequeoLogs");
        return desgloseChequeoLogRepository.findAll(pageable).map(desgloseChequeoLogMapper::toDto);
    }

    /**
     * Get one desgloseChequeoLog by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseChequeoLogDTO> findOne(DesgloseChequeoLogPK id) {
        log.info("INICIO - Request to get DesgloseChequeoLog : {}", id);
        log.info("FIN - Request to get DesgloseChequeoLog : {}", id);
        return desgloseChequeoLogRepository.findById(id).map(desgloseChequeoLogMapper::toDto);
    }

    /**
     * Delete the desgloseChequeoLog by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseChequeoLogPK id) {
        log.info("INICIO - Request to delete DesgloseChequeoLog : {}", id);
        log.info("FIN - Request to delete DesgloseChequeoLog : {}", id);
        desgloseChequeoLogRepository.deleteById(id);
    }
}
