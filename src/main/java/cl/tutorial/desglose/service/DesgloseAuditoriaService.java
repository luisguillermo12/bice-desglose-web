package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseAuditoria;
import cl.tutorial.desglose.domain.pk.DesgloseAuditoriaPK;
import cl.tutorial.desglose.repository.DesgloseAuditoriaRepository;
import cl.tutorial.desglose.service.dto.DesgloseAuditoriaDTO;
import cl.tutorial.desglose.service.mapper.DesgloseAuditoriaMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DesgloseAuditoria.
 */
@Service
@Transactional
public class DesgloseAuditoriaService {

    private final Logger log = LogManager.getLogger(DesgloseAuditoriaService.class);

    private final DesgloseAuditoriaRepository desgloseAuditoriaRepository;

    private final DesgloseAuditoriaMapper desgloseAuditoriaMapper;

    public DesgloseAuditoriaService(DesgloseAuditoriaRepository desgloseAuditoriaRepository, DesgloseAuditoriaMapper desgloseAuditoriaMapper) {
        this.desgloseAuditoriaRepository = desgloseAuditoriaRepository;
        this.desgloseAuditoriaMapper = desgloseAuditoriaMapper;
    }

    /**
     * Save a desgloseAuditoria.
     *
     * @param desgloseAuditoriaDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseAuditoriaDTO save(DesgloseAuditoriaDTO desgloseAuditoriaDTO) {
        log.info("INICIO - Request to save DesgloseAuditoria : {}", desgloseAuditoriaDTO);

        DesgloseAuditoria desgloseAuditoria = desgloseAuditoriaMapper.toEntity(desgloseAuditoriaDTO);
        desgloseAuditoria = desgloseAuditoriaRepository.save(desgloseAuditoria);
        log.info("FIN - Request to save DesgloseAuditoria ");
        return desgloseAuditoriaMapper.toDto(desgloseAuditoria);
    }

    /**
     * Get all the desgloseAuditorias.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseAuditoriaDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseAuditorias");
        log.info("FIN - Request to get all DesgloseAuditorias");
        return desgloseAuditoriaRepository.findAll(pageable).map(desgloseAuditoriaMapper::toDto);
    }

    /**
     * Get one desgloseAuditoria by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseAuditoriaDTO> findOne(DesgloseAuditoriaPK id) {
        log.info("INICIO - Request to get DesgloseAuditoria : {}", id);
        log.info("FIN - Request to get DesgloseAuditoria : {}", id);
        return desgloseAuditoriaRepository.findById(id).map(desgloseAuditoriaMapper::toDto);
    }

    /**
     * Delete the desgloseAuditoria by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseAuditoriaPK id) {
        log.info("INICIO Request to delete DesgloseAuditoria : {}", id);
        log.info("FIN Request to delete DesgloseAuditoria : {}", id);
        desgloseAuditoriaRepository.deleteById(id);
    }
}
