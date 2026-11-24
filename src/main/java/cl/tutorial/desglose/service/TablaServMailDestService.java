package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.TablaServMailDest;
import cl.tutorial.desglose.domain.pk.TablaServicioMailDestPK;
import cl.tutorial.desglose.repository.TablaServMailDestRepository;
import cl.tutorial.desglose.service.dto.TablaServMailDestDTO;
import cl.tutorial.desglose.service.mapper.TablaServMailDestMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing TablaServMailDest.
 */
@Service
@Transactional
public class TablaServMailDestService {

    private final Logger log = LogManager.getLogger(TablaServMailDestService.class);

    private final TablaServMailDestRepository tablaServMailDestRepository;

    private final TablaServMailDestMapper tablaServMailDestMapper;

    public TablaServMailDestService(TablaServMailDestRepository tablaServMailDestRepository, TablaServMailDestMapper tablaServMailDestMapper) {
        this.tablaServMailDestRepository = tablaServMailDestRepository;
        this.tablaServMailDestMapper = tablaServMailDestMapper;
    }

    /**
     * Save a tablaServMailDest.
     *
     * @param tablaServMailDestDTO the entity to save
     * @return the persisted entity
     */
    public TablaServMailDestDTO save(TablaServMailDestDTO tablaServMailDestDTO) {
        log.info("Request to save TablaServMailDest : {}", tablaServMailDestDTO);

        TablaServMailDest tablaServMailDest = tablaServMailDestMapper.toEntity(tablaServMailDestDTO);
        tablaServMailDest = tablaServMailDestRepository.save(tablaServMailDest);
        return tablaServMailDestMapper.toDto(tablaServMailDest);
    }

    /**
     * Get all the tablaServMailDests.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TablaServMailDestDTO> findAll(Pageable pageable) {
        log.info("Request to get all TablaServMailDests");
        return tablaServMailDestRepository.findAll(pageable)
            .map(tablaServMailDestMapper::toDto);
    }


    /**
     * Get one tablaServMailDest by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TablaServMailDestDTO> findOne(TablaServicioMailDestPK id) {
        log.info("Request to get TablaServMailDest : {}", id);
        return tablaServMailDestRepository.findById(id)
            .map(tablaServMailDestMapper::toDto);
    }

    /**
     * Delete the tablaServMailDest by id.
     *
     * @param id the id of the entity
     */
    public void delete(TablaServicioMailDestPK id) {
        log.info("Request to delete TablaServMailDest : {}", id);
        tablaServMailDestRepository.deleteById(id);
    }
}
