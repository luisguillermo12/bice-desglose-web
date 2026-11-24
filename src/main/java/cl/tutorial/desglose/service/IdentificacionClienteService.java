package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.IdentificacionCliente;
import cl.tutorial.desglose.domain.pk.IdentificacionClientePK;
import cl.tutorial.desglose.repository.IdentificacionClienteRepository;
import cl.tutorial.desglose.service.dto.IdentificacionClienteDTO;
import cl.tutorial.desglose.service.mapper.IdentificacionClienteMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing IdentificacionCliente.
 */
@Service
@Transactional
public class IdentificacionClienteService {

    private final Logger log = LogManager.getLogger(IdentificacionClienteService.class);

    private final IdentificacionClienteRepository identificacionClienteRepository;

    private final IdentificacionClienteMapper identificacionClienteMapper;

    public IdentificacionClienteService(IdentificacionClienteRepository identificacionClienteRepository, IdentificacionClienteMapper identificacionClienteMapper) {
        this.identificacionClienteRepository = identificacionClienteRepository;
        this.identificacionClienteMapper = identificacionClienteMapper;
    }

    /**
     * Save a identificacionCliente.
     *
     * @param identificacionClienteDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public IdentificacionClienteDTO save(IdentificacionClienteDTO identificacionClienteDTO) {
        log.info("Request to save IdentificacionCliente : {}", identificacionClienteDTO);

        IdentificacionCliente identificacionCliente = identificacionClienteMapper.toEntity(identificacionClienteDTO);
        identificacionCliente = identificacionClienteRepository.save(identificacionCliente);
        return identificacionClienteMapper.toDto(identificacionCliente);
    }

    /**
     * Get all the identificacionClientes.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<IdentificacionClienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all IdentificacionClientes");
        return identificacionClienteRepository.findAll(pageable).map(identificacionClienteMapper::toDto);
    }

    /**
     * Get one identificacionCliente by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<IdentificacionClienteDTO> findOne(IdentificacionClientePK id) {
        log.info("Request to get IdentificacionCliente : {}", id);
        return identificacionClienteRepository.findById(id).map(identificacionClienteMapper::toDto);
    }

    /**
     * Delete the identificacionCliente by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(IdentificacionClientePK id) {
        log.info("Request to delete IdentificacionCliente : {}", id);
        identificacionClienteRepository.deleteById(id);
    }
}
