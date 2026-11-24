package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.RelacionTabla;
import cl.tutorial.desglose.repository.RelacionTablaRepository;
import cl.tutorial.desglose.service.dto.RelacionTablaDTO;
import cl.tutorial.desglose.service.mapper.RelacionTablaMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing RelacionTabla.
 */
@Service
@Transactional
public class RelacionTablaService {

    private final Logger log = LogManager.getLogger(RelacionTablaService.class);

    private final RelacionTablaRepository relacionTablaRepository;

    private final RelacionTablaMapper relacionTablaMapper;

    public RelacionTablaService(RelacionTablaRepository relacionTablaRepository, RelacionTablaMapper relacionTablaMapper) {
        this.relacionTablaRepository = relacionTablaRepository;
        this.relacionTablaMapper = relacionTablaMapper;
    }

    /**
     * Save a relacionTabla.
     *
     * @param relacionTablaDTO the entity to save
     * @return the persisted entity
     */
    public RelacionTablaDTO save(RelacionTablaDTO relacionTablaDTO) {
        log.info("Request to save RelacionTabla : {}", relacionTablaDTO);

        RelacionTabla relacionTabla = relacionTablaMapper.toEntity(relacionTablaDTO);
        relacionTabla = relacionTablaRepository.save(relacionTabla);
        return relacionTablaMapper.toDto(relacionTabla);
    }

    /**
     * Get all the relacionTablas.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<RelacionTablaDTO> findAll(Pageable pageable) {
        log.info("Request to get all RelacionTablas");
        return relacionTablaRepository.findAll(pageable)
            .map(relacionTablaMapper::toDto);
    }


    /**
     * Get one relacionTabla by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<RelacionTablaDTO> findOne(Long id) {
        log.info("Request to get RelacionTabla : {}", id);
        return relacionTablaRepository.findById(id)
            .map(relacionTablaMapper::toDto);
    }

    /**
     * Delete the relacionTabla by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete RelacionTabla : {}", id);
        relacionTablaRepository.deleteById(id);
    }
}
