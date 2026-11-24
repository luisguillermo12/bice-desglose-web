package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteBasico;
import cl.tutorial.desglose.conexiones.cifdesglose.domain.pk.CommonClientePK;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.ClienteBasicoRepository;
import cl.tutorial.desglose.service.dto.ClienteBasicoDTO;
import cl.tutorial.desglose.service.mapper.ClienteBasicoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing ClienteBasico.
 */
@Service
@Transactional
public class ClienteBasicoService {

    private final Logger log = LogManager.getLogger(ClienteBasicoService.class);

    private final ClienteBasicoRepository clienteBasicoRepository;

    private final ClienteBasicoMapper clienteBasicoMapper;

    public ClienteBasicoService(ClienteBasicoRepository clienteBasicoRepository, ClienteBasicoMapper clienteBasicoMapper) {
        this.clienteBasicoRepository = clienteBasicoRepository;
        this.clienteBasicoMapper = clienteBasicoMapper;
    }

    /**
     * Save a clienteBasico.
     *
     * @param clienteBasicoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ClienteBasicoDTO save(ClienteBasicoDTO clienteBasicoDTO) {
        log.info("INICIO - to save ClienteBasico : {}", clienteBasicoDTO);
        ClienteBasico clienteBasico = clienteBasicoMapper.toEntity(clienteBasicoDTO);
        clienteBasico = clienteBasicoRepository.save(clienteBasico);
        log.info("FIN - to save ClienteBasico");
        return clienteBasicoMapper.toDto(clienteBasico);
    }

    /**
     * Get all the clienteBasicos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClienteBasicoDTO> findAll(Pageable pageable) {
        log.info("INICIO - to get all ClienteBasicos");
        log.info("FIN  - to get all ClienteBasicos");
        return clienteBasicoRepository.findAll(pageable).map(clienteBasicoMapper::toDto);
    }

    /**
     * Get one clienteBasico by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ClienteBasicoDTO> findOne(CommonClientePK id) {
        log.info("INICIO -to get ClienteBasico : {}", id);
        log.info("FIN - to get ClienteBasico");
        return clienteBasicoRepository.findById(id).map(clienteBasicoMapper::toDto);
    }

    /**
     * Delete the clienteBasico by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(CommonClientePK id) {
        log.info("INICIO - to delete ClienteBasico : {}", id);
        log.info("FIN - to delete ClienteBasico");
        clienteBasicoRepository.deleteById(id);
    }
}
