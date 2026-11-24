package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineGroup;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineGroupRepository;
import cl.tutorial.desglose.service.dto.TurbineGroupDTO;
import cl.tutorial.desglose.service.mapper.TurbineGroupMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing TurbineGroup.
 */
@Service
@Transactional
public class TurbineGroupService {

    private final Logger log = LogManager.getLogger(TurbineGroupService.class);

    private final TurbineGroupRepository turbineGroupRepository;

    private final TurbineGroupMapper turbineGroupMapper;

    public TurbineGroupService(TurbineGroupRepository turbineGroupRepository, TurbineGroupMapper turbineGroupMapper) {
        this.turbineGroupRepository = turbineGroupRepository;
        this.turbineGroupMapper = turbineGroupMapper;
    }

    /**
     * Save a turbineGroup.
     *
     * @param turbineGroupDTO the entity to save
     * @return the persisted entity
     */
    public TurbineGroupDTO save(TurbineGroupDTO turbineGroupDTO) {
        log.info("Request to save TurbineGroup : {}", turbineGroupDTO);

        TurbineGroup turbineGroup = turbineGroupMapper.toEntity(turbineGroupDTO);
        turbineGroup = turbineGroupRepository.save(turbineGroup);
        return turbineGroupMapper.toDto(turbineGroup);
    }

    /**
     * Get all the turbineGroups.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurbineGroupDTO> findAll(Pageable pageable) {
        log.info("Request to get all TurbineGroups");
        return turbineGroupRepository.findAll(pageable)
            .map(turbineGroupMapper::toDto);
    }


    /**
     * Get one turbineGroup by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbineGroupDTO> findOne(Long id) {
        log.info("Request to get TurbineGroup : {}", id);
        return turbineGroupRepository.findById(id)
            .map(turbineGroupMapper::toDto);
    }

    /**
     * Delete the turbineGroup by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TurbineGroup : {}", id);
        turbineGroupRepository.deleteById(id);
    }
}
