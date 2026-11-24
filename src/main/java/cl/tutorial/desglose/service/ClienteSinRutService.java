package cl.tutorial.desglose.service;

import cl.tutorial.desglose.conexiones.cifdesglose.domain.ClienteSinRut;
import cl.tutorial.desglose.conexiones.cifdesglose.repository.ClienteSinRutRepository;
import cl.tutorial.desglose.service.dto.ClienteSinRutDTO;
import cl.tutorial.desglose.service.mapper.ClienteSinRutMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing ClienteSinRut.
 */
@Service
@Transactional
public class ClienteSinRutService {

    private final Logger log = LogManager.getLogger(ClienteSinRutService.class);

    private final ClienteSinRutRepository clienteSinRutRepository;

    private final ClienteSinRutMapper clienteSinRutMapper;

    public ClienteSinRutService(ClienteSinRutRepository clienteSinRutRepository, ClienteSinRutMapper clienteSinRutMapper) {
        this.clienteSinRutRepository = clienteSinRutRepository;
        this.clienteSinRutMapper = clienteSinRutMapper;
    }

    /**
     * Save a clienteSinRut.
     *
     * @param clienteSinRutDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public ClienteSinRutDTO save(ClienteSinRutDTO clienteSinRutDTO) {
        log.info("INICIO - Request to save ClienteSinRut : {}", clienteSinRutDTO);
        ClienteSinRut clienteSinRut = clienteSinRutMapper.toEntity(clienteSinRutDTO);
        clienteSinRut = clienteSinRutRepository.save(clienteSinRut);
        log.info("FIN - Request to save ClienteSinRut");
        return clienteSinRutMapper.toDto(clienteSinRut);
    }

    /**
     * Get all the clienteSinRuts.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClienteSinRutDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all ClienteSinRuts");
        final Page<ClienteSinRut> all = clienteSinRutRepository.findAll(pageable);
        log.info("FIN - Request to get all ClienteSinRuts");
        return all.map(clienteSinRutMapper::toDto);
    }

    /**
     * @return
     *
     * @autor carlos eduardo rodriguez perez
     */
    @Transactional(readOnly = true)
    public List<ClienteSinRutDTO> findAll() {
        log.info("INICIO - Request to get findAll");
        List<ClienteSinRut> list = clienteSinRutRepository.findAllByOrderByNomClienteAsc();
        log.info("FIN - Request to get findAll");
        return clienteSinRutMapper.toDto(list);
    }

    /**
     * Get one clienteSinRut by codCliente.
     *
     * @param codCliente
     * 		the codCliente of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<ClienteSinRutDTO> findOne(String codCliente) {
        log.info("INICIO - Request to get ClienteSinRut : {}", codCliente);
        log.info("FIN - Request to get ClienteSinRut");
        return clienteSinRutRepository.findOneByCodCliente(codCliente).map(clienteSinRutMapper::toDto);
    }

    /**
     * Delete the clienteSinRut by codCliente.
     *
     * @param codCliente
     * 		the codCliente of the entity
     */
    public void deleteByCodCliente(String codCliente) {
        log.info("INICIO - Request to delete ClienteSinRut : {}", codCliente);
        log.info("FIN - Request to delete ClienteSinRut");
        clienteSinRutRepository.deleteByCodCliente(codCliente);
    }

    public Optional<ClienteSinRutDTO> findOneByCodCliente(String codCliente) {
        log.info("INICIO -  Request findOneByCodCliente: {}",codCliente);
        log.info("FIN - Request findOneByCodCliente");
        return clienteSinRutRepository.findOneByCodCliente(codCliente).map(clienteSinRutMapper::toDto);
    }
}
