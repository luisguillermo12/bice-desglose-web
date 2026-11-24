package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRole;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineRoleRepository;
import cl.tutorial.desglose.service.dto.TurbineRoleDTO;
import cl.tutorial.desglose.service.mapper.TurbineRoleMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing TurbineRole.
 */
@Service
@Transactional
public class TurbineRoleService {

    private final Logger log = LogManager.getLogger(TurbineRoleService.class);

    private final TurbineRoleRepository turbineRoleRepository;

    private final TurbineRoleMapper turbineRoleMapper;

    public TurbineRoleService(TurbineRoleRepository turbineRoleRepository, TurbineRoleMapper turbineRoleMapper) {
        this.turbineRoleRepository = turbineRoleRepository;
        this.turbineRoleMapper = turbineRoleMapper;
    }

    /**
     * Save a turbineRole.
     *
     * @param turbineRoleDTO the entity to save
     * @return the persisted entity
     */
    public TurbineRoleDTO save(TurbineRoleDTO turbineRoleDTO) {
        log.info("Request to save TurbineRole : {}", turbineRoleDTO);

        TurbineRole turbineRole = turbineRoleMapper.toEntity(turbineRoleDTO);
        turbineRole = turbineRoleRepository.save(turbineRole);
        return turbineRoleMapper.toDto(turbineRole);
    }

    /**
     * Get all the turbineRoles.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TurbineRoleDTO> findAll(Pageable pageable) {
        log.info("Request to get all TurbineRoles");
        return turbineRoleRepository.findAll(pageable)
            .map(turbineRoleMapper::toDto);
    }


    /**
     * Get one turbineRole by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbineRoleDTO> findOne(Long id) {
        log.info("Request to get TurbineRole : {}", id);
        return turbineRoleRepository.findById(id)
            .map(turbineRoleMapper::toDto);
    }

    /**
     * Get one TurbineRole by roleName
     * @author carlos eduardo rodriguez perez
     * @param roleName
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TurbineRoleDTO> finRoleName(String roleName){
        log.info("Request to get TurbineRole: {}",roleName);
        return turbineRoleRepository.findByRoleName(roleName)
            .map(turbineRoleMapper::toDto);
    }

    /**
     * Delete the turbineRole by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TurbineRole : {}", id);
        turbineRoleRepository.deleteById(id);
    }
}
