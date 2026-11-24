package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.DesgloseChequeoInfo;
import cl.tutorial.desglose.domain.pk.DesgloseChequeoInfoPK;
import cl.tutorial.desglose.repository.DesgloseChequeoInfoRepository;
import cl.tutorial.desglose.service.dto.DesgloseChequeoInfoDTO;
import cl.tutorial.desglose.service.mapper.DesgloseChequeoInfoMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing DesgloseChequeoInfo.
 */
@Service
@Transactional
public class DesgloseChequeoInfoService {

    private final Logger log = LogManager.getLogger(DesgloseChequeoInfoService.class);

    private final DesgloseChequeoInfoRepository desgloseChequeoInfoRepository;

    private final DesgloseChequeoInfoMapper desgloseChequeoInfoMapper;

    public DesgloseChequeoInfoService(DesgloseChequeoInfoRepository desgloseChequeoInfoRepository, DesgloseChequeoInfoMapper desgloseChequeoInfoMapper) {
        this.desgloseChequeoInfoRepository = desgloseChequeoInfoRepository;
        this.desgloseChequeoInfoMapper = desgloseChequeoInfoMapper;
    }

    /**
     * Save a desgloseChequeoInfo.
     *
     * @param desgloseChequeoInfoDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public DesgloseChequeoInfoDTO save(DesgloseChequeoInfoDTO desgloseChequeoInfoDTO) {
        log.info("INICIO - Request to save DesgloseChequeoInfo : {}", desgloseChequeoInfoDTO);
        DesgloseChequeoInfo desgloseChequeoInfo = desgloseChequeoInfoMapper.toEntity(desgloseChequeoInfoDTO);
        desgloseChequeoInfo = desgloseChequeoInfoRepository.save(desgloseChequeoInfo);
        log.info("FIN - Request to get all DescripcionCodigos");
        return desgloseChequeoInfoMapper.toDto(desgloseChequeoInfo);
    }

    /**
     * Get all the desgloseChequeoInfos.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<DesgloseChequeoInfoDTO> findAll(Pageable pageable) {
        log.info("INICIO - Request to get all DesgloseChequeoInfos");
        log.info("FIN - Request to get all DesgloseChequeoInfos");
        return desgloseChequeoInfoRepository.findAll(pageable).map(desgloseChequeoInfoMapper::toDto);
    }

    /**
     * Get one desgloseChequeoInfo by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<DesgloseChequeoInfoDTO> findOne(DesgloseChequeoInfoPK id) {
        log.info("INICIO - Request to get DesgloseChequeoInfo : {}", id);
        log.info("FIN - Request to get DesgloseChequeoInfo : {}", id);
        return desgloseChequeoInfoRepository.findById(id).map(desgloseChequeoInfoMapper::toDto);
    }

    /**
     * Delete the desgloseChequeoInfo by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(DesgloseChequeoInfoPK id) {
        log.info("INICIO - Request to delete DesgloseChequeoInfo : {}", id);
        log.info("FIN - Request to delete DesgloseChequeoInfo : {}", id);
        desgloseChequeoInfoRepository.deleteById(id);
    }
}
