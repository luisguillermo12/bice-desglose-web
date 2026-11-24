package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.Cuadre;
import cl.tutorial.desglose.domain.pk.CuadrePK;
import cl.tutorial.desglose.repository.CuadreRepository;
import cl.tutorial.desglose.service.dto.CuadreDTO;
import cl.tutorial.desglose.service.mapper.CuadreMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing Cuadre.
 */
@Service
@Transactional
public class CuadreService {

    private final Logger log = LogManager.getLogger(CuadreService.class);

    private final CuadreRepository cuadreRepository;

    private final CuadreMapper cuadreMapper;

    public CuadreService(CuadreRepository cuadreRepository, CuadreMapper cuadreMapper) {
        this.cuadreRepository = cuadreRepository;
        this.cuadreMapper = cuadreMapper;
    }

    /**
     * Save a cuadre.
     *
     * @param cuadreDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public CuadreDTO save(CuadreDTO cuadreDTO) {
        log.info("INICIO - Request to save Cuadre : {}", cuadreDTO);
        Cuadre cuadre = cuadreMapper.toEntity(cuadreDTO);
        cuadre = cuadreRepository.save(cuadre);
        log.info("FIN - Request to save Cuadre");
        return cuadreMapper.toDto(cuadre);
    }

    /**
     * Get all the cuadres.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<CuadreDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all Cuadres");
        log.info("FIN - Request to get all Cuadres");
        return cuadreRepository.findAll(pageable).map(cuadreMapper::toDto);
    }

    /**
     * Get one cuadre by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<CuadreDTO> findOne(CuadrePK id) {
        log.info("INICIO - Request to get Cuadre : {}", id);
        log.info("FIN - Request to get Cuadre");
        return cuadreRepository.findById(id).map(cuadreMapper::toDto);
    }

    /**
     * Delete the cuadre by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(CuadrePK id) {
        log.info("INICIO - Request to delete Cuadre : {}", id);
        log.info("FIN - Request to delete Cuadre");
        cuadreRepository.deleteById(id);
    }
}
