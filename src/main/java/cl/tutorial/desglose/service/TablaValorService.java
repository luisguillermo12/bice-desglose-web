package cl.tutorial.desglose.service;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.TablaValor;
import cl.tutorial.desglose.repository.TablaValorRepository;
import cl.tutorial.desglose.service.dto.TablaValorDTO;
import cl.tutorial.desglose.service.mapper.TablaValorMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing TablaValor.
 */
@Service
@Transactional
public class TablaValorService {

    private final Logger log = LogManager.getLogger(TablaValorService.class);

    private final TablaValorRepository tablaValorRepository;

    private final TablaValorMapper tablaValorMapper;

    public TablaValorService(TablaValorRepository tablaValorRepository, TablaValorMapper tablaValorMapper) {
        this.tablaValorRepository = tablaValorRepository;
        this.tablaValorMapper = tablaValorMapper;
    }

    /**
     * Save a tablaValor.
     *
     * @param tablaValorDTO the entity to save
     * @return the persisted entity
     */
    public TablaValorDTO save(TablaValorDTO tablaValorDTO) {
        log.info("Request to save TablaValor : {}", tablaValorDTO);

        TablaValor tablaValor = tablaValorMapper.toEntity(tablaValorDTO);
        tablaValor = tablaValorRepository.save(tablaValor);
        return tablaValorMapper.toDto(tablaValor);
    }

    /**
     * Get all the tablaValors.
     *
     * @param pageable the pagination information
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<TablaValorDTO> findAll(Pageable pageable) {
        log.info("Request to get all TablaValors");
        return tablaValorRepository.findAll(pageable)
            .map(tablaValorMapper::toDto);
    }


    /**
     * Get one tablaValor by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<TablaValorDTO> findOne(Long id) {
        log.info("Request to get TablaValor : {}", id);
        return tablaValorRepository.findById(id)
            .map(tablaValorMapper::toDto);
    }

    /**
     * Delete the tablaValor by id.
     *
     * @param id the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete TablaValor : {}", id);
        tablaValorRepository.deleteById(id);
    }

    /**
     * @author carlos eduardo rodriguez perez
     * @param codTabla
     * @param codRegStatus
     * @return
     */
    public List<TablaValorDTO> findByCodTablaAndCodRegStatus(String codTabla,Integer codRegStatus){
        log.info("busqueda de Tabla Valor por {} y {}",codTabla,codRegStatus);
        List<TablaValor> lista = tablaValorRepository.findByCodTablaAndCodRegStatus(codTabla,codRegStatus);
        return  this.tablaValorMapper.toDto(lista);
    }

    public TablaValorDTO getIvaValue() {
        LocalDate currentDate = LocalDate.now();
        log.info("busqueda del IVA con fecha: {}, CODTABLA_IVA: {} y COD_REG_STATUS_ZERO: {}", currentDate, Constants.CODTABLA_IVA, Constants.COD_REG_STATUS_ZERO);
        TablaValor tablaValor = tablaValorRepository.findByCodTablaAndCodRegStatusAndFecValidezLessThanEqual(Constants.CODTABLA_IVA, Constants.COD_REG_STATUS_ZERO, currentDate);
        return this.tablaValorMapper.toDto(tablaValor);
    }
}
