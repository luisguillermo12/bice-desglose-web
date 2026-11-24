package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRolePermission;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineRolePermissionRepository;
import cl.tutorial.desglose.service.dto.TurbineRolePermissionDTO;
import cl.tutorial.desglose.service.mapper.TurbineRolePermissionMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing TurbineRolePermission.
 */
@Service
@Transactional
public class TurbineRolePermissionService {

    private final Logger log = LogManager.getLogger(TurbineRolePermissionService.class);

    private final TurbineRolePermissionRepository turbineRolePermissionRepository;

    private final TurbineRolePermissionMapper turbineRolePermissionMapper;

    public TurbineRolePermissionService(TurbineRolePermissionRepository turbineRolePermissionRepository, TurbineRolePermissionMapper turbineRolePermissionMapper) {
        this.turbineRolePermissionRepository = turbineRolePermissionRepository;
        this.turbineRolePermissionMapper = turbineRolePermissionMapper;
    }

    /**
     * Save a turbineRolePermission.
     *
     * @param turbineRolePermissionDTO the entity to save
     * @return the persisted entity
     */
    public TurbineRolePermissionDTO save(TurbineRolePermissionDTO turbineRolePermissionDTO) {
        log.debug("Request to save TurbineRolePermission : {}", turbineRolePermissionDTO);

        TurbineRolePermission turbineRolePermission = turbineRolePermissionMapper.toEntity(turbineRolePermissionDTO);
        turbineRolePermission = turbineRolePermissionRepository.save(turbineRolePermission);
        return turbineRolePermissionMapper.toDto(turbineRolePermission);
    }

    /**
     * Get all the turbineRolePermissions.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurbineRolePermissionDTO> findAll(Pageable pageable) {
        log.info("Request to get all TurbineRolePermissions");
        return turbineRolePermissionRepository.findAll(pageable)
            .map(turbineRolePermissionMapper::toDto);
    }


    /**
     * Get one turbineRolePermission by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbineRolePermissionDTO> findOne(Long id) {
        log.info("Request to get TurbineRolePermission : {}", id);
        return turbineRolePermissionRepository.findById(id)
            .map(turbineRolePermissionMapper::toDto);
    }

    /**
     * Delete the turbineRolePermission by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TurbineRolePermission : {}", id);
        turbineRolePermissionRepository.deleteById(id);
    }
}
