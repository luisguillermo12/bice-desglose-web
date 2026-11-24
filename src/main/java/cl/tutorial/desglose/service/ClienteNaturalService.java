package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteNatural;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.ClienteNaturalRepository;
import cl.tutorial.desglose.service.dto.ClienteNaturalDTO;
import cl.tutorial.desglose.service.mapper.ClienteNaturalMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ClienteNatural.
 */
@Service
@Transactional
public class ClienteNaturalService {

    private final Logger log = LogManager.getLogger(ClienteNaturalService.class);

    private final ClienteNaturalRepository clienteNaturalRepository;

    private final ClienteNaturalMapper clienteNaturalMapper;

    public ClienteNaturalService(ClienteNaturalRepository clienteNaturalRepository, ClienteNaturalMapper clienteNaturalMapper) {
        this.clienteNaturalRepository = clienteNaturalRepository;
        this.clienteNaturalMapper = clienteNaturalMapper;
    }

    /**
     * Save a clienteNatural.
     *
     * @param clienteNaturalDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ClienteNaturalDTO save(ClienteNaturalDTO clienteNaturalDTO) {
        log.info("INICIO - Request to save ClienteNatural : {}", clienteNaturalDTO);
        ClienteNatural clienteNatural = clienteNaturalMapper.toEntity(clienteNaturalDTO);
        clienteNatural = clienteNaturalRepository.save(clienteNatural);
        log.info("FIN - Request to save ClienteNatural");
        return clienteNaturalMapper.toDto(clienteNatural);
    }

    /**
     * Get all the clienteNaturals.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClienteNaturalDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all ClienteNaturals");
        log.info("FIN - Request to get all ClienteNaturals");
        return clienteNaturalRepository.findAll(pageable).map(clienteNaturalMapper::toDto);
    }

    /**
     * Get one clienteNatural by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ClienteNaturalDTO> findOne(CommonClientePK id) {
        log.info("INICIO - Request to get ClienteNatural : {}", id);
        log.info("FIN - Request to get ClienteNatural");
        return clienteNaturalRepository.findById(id).map(clienteNaturalMapper::toDto);
    }

    /**
     * Delete the clienteNatural by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(CommonClientePK id) {
        log.info("INICIO - Request to delete ClienteNatural : {}", id);
        log.info("FIN - Request to delete ClienteNatural");
        clienteNaturalRepository.deleteById(id);
    }
}
