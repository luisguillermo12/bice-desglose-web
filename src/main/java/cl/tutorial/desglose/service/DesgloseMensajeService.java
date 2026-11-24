package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseMensaje;
import cl.tutorial.desglose.domain.pk.DesgloseMensajePK;
import cl.tutorial.desglose.repository.DesgloseMensajeRepository;
import cl.tutorial.desglose.service.dto.DesgloseMensajeDTO;
import cl.tutorial.desglose.service.mapper.DesgloseMensajeMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service Implementation for managing DesgloseMensaje.
 */
@Service
@Transactional
public class DesgloseMensajeService {

    private final Logger log = LogManager.getLogger(DesgloseMensajeService.class);

    private final DesgloseMensajeRepository desgloseMensajeRepository;

    private final DesgloseMensajeMapper desgloseMensajeMapper;

    public DesgloseMensajeService(DesgloseMensajeRepository desgloseMensajeRepository, DesgloseMensajeMapper desgloseMensajeMapper) {
        this.desgloseMensajeRepository = desgloseMensajeRepository;
        this.desgloseMensajeMapper = desgloseMensajeMapper;
    }

    /**
     * Save a desgloseMensaje.
     *
     * @param desgloseMensajeDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseMensajeDTO save(DesgloseMensajeDTO desgloseMensajeDTO) {
        log.info("INICIO - Request to save DesgloseMensaje : {}", desgloseMensajeDTO);

        DesgloseMensaje desgloseMensaje = desgloseMensajeMapper.toEntity(desgloseMensajeDTO);
        desgloseMensaje = desgloseMensajeRepository.save(desgloseMensaje);
        log.info("FIN - Request to save DesgloseMensaje : {}", desgloseMensajeDTO);
        return desgloseMensajeMapper.toDto(desgloseMensaje);
    }

    /**
     * Get all the desgloseMensajes.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseMensajeDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseMensajes");
        log.info("FIN - Request to get all DesgloseMensajes");
        return desgloseMensajeRepository.findAll(pageable).map(desgloseMensajeMapper::toDto);
    }

    /**
     * Get one desgloseMensaje by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<DesgloseMensajeDTO> findAllById(DesgloseMensajePK id) {
        log.info("INICIO - Request to get DesgloseMensaje : {}", id);
        List<DesgloseMensaje> lista = desgloseMensajeRepository
            .findAllByCodClienteAndCodPlantillaAndCodTipoDesAndCodSubtipoDesAndCodMonedaOrderByFecEventAsc(id.getCodCliente(), id.getCodPlantilla(), id.getCodTipoDes(),
                id.getCodSubtipoDes(), id.getCodMoneda());
        log.info("FIN - Request to get DesgloseMensaje : {}", id);
        return desgloseMensajeMapper.toDto(lista);
    }

    /**
     * Delete the desgloseMensaje by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseMensajePK id) {
        log.info("INICIO - Request to delete DesgloseMensaje : {}", id);
        log.info("FIN - Request to delete DesgloseMensaje : {}", id);
        desgloseMensajeRepository.deleteById(id);
    }
}
