package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteJuridico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.ClienteJuridicoRepository;
import cl.tutorial.desglose.service.dto.ClienteJuridicoDTO;
import cl.tutorial.desglose.service.mapper.ClienteJuridicoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ClienteJuridico.
 */
@Service
@Transactional
public class ClienteJuridicoService {

    private final Logger log = LogManager.getLogger(ClienteJuridicoService.class);

    private final ClienteJuridicoRepository clienteJuridicoRepository;

    private final ClienteJuridicoMapper clienteJuridicoMapper;

    public ClienteJuridicoService(ClienteJuridicoRepository clienteJuridicoRepository, ClienteJuridicoMapper clienteJuridicoMapper) {
        this.clienteJuridicoRepository = clienteJuridicoRepository;
        this.clienteJuridicoMapper = clienteJuridicoMapper;
    }

    /**
     * Save a clienteJuridico.
     *
     * @param clienteJuridicoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ClienteJuridicoDTO save(ClienteJuridicoDTO clienteJuridicoDTO) {
        log.info("INICIO - Request to save ClienteJuridico : {}", clienteJuridicoDTO);

        ClienteJuridico clienteJuridico = clienteJuridicoMapper.toEntity(clienteJuridicoDTO);
        clienteJuridico = clienteJuridicoRepository.save(clienteJuridico);
        log.info("FIN - Request to save ClienteJuridico");
        return clienteJuridicoMapper.toDto(clienteJuridico);
    }

    /**
     * Get all the clienteJuridicos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClienteJuridicoDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all ClienteJuridicos");
        log.info("FIN - Request to get all ClienteJuridicos");
        return clienteJuridicoRepository.findAll(pageable).map(clienteJuridicoMapper::toDto);
    }

    /**
     * Get one clienteJuridico by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ClienteJuridicoDTO> findOne(CommonClientePK id) {
        log.info("INICIO - Request to get ClienteJuridico : {}", id);
        log.info("FIN - Request to get ClienteJuridico ");
        return clienteJuridicoRepository.findById(id).map(clienteJuridicoMapper::toDto);
    }

    /**
     * Delete the clienteJuridico by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(CommonClientePK id) {
        log.info("INICIO - Request to delete ClienteJuridico : {}", id);
        log.info("FIN - Request to delete ClienteJuridico");
        clienteJuridicoRepository.deleteById(id);
    }
}
