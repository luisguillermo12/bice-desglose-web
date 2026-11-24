package cl.tutorial.desglose.service;

import cl.tutorial.desglose.config.ApplicationProperties;
import cl.tutorial.desglose.domain.BlackListJwt;
import cl.tutorial.desglose.domain.UniqueSessionControl;
import cl.tutorial.desglose.repository.BlackListJwtRepository;
import cl.tutorial.desglose.repository.UniqueSessionControlRepository;
import cl.tutorial.desglose.security.jwt.TokenProvider;
import cl.tutorial.desglose.service.dto.UniqueSessionControlDTO;
import cl.tutorial.desglose.service.mapper.UniqueSessionControlMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


/**
 * Service Implementation for managing UniqueSessionControl.
 */
@Service
@Transactional
public class UniqueSessionControlService {

    private final Logger log = LogManager.getLogger(UniqueSessionControlService.class);

    private final UniqueSessionControlRepository uniqueSessionControlRepository;
    private final BlackListJwtRepository         blackListJwtRepository;
    private final UniqueSessionControlMapper     uniqueSessionControlMapper;
    private final TokenProvider                  tokenProvider;
    private final ApplicationProperties          applicationProperties;
    public UniqueSessionControlService(UniqueSessionControlRepository uniqueSessionControlRepository, BlackListJwtRepository blackListJwtRepository,
        UniqueSessionControlMapper uniqueSessionControlMapper, TokenProvider tokenProvider,ApplicationProperties applicationProperties) {
        this.uniqueSessionControlRepository = uniqueSessionControlRepository;
        this.blackListJwtRepository = blackListJwtRepository;
        this.uniqueSessionControlMapper = uniqueSessionControlMapper;
        this.tokenProvider = tokenProvider;
        this.applicationProperties = applicationProperties;
    }

    /**
     * Save a uniqueSessionControl.
     *
     * @param uniqueSessionControlDTO
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public UniqueSessionControlDTO save(UniqueSessionControlDTO uniqueSessionControlDTO) {
        log.info("Request to save UniqueSessionControl : {}", uniqueSessionControlDTO);

        UniqueSessionControl uniqueSessionControl = uniqueSessionControlMapper.toEntity(uniqueSessionControlDTO);
        uniqueSessionControl = uniqueSessionControlRepository.save(uniqueSessionControl);
        return uniqueSessionControlMapper.toDto(uniqueSessionControl);
    }

    /**
     * Get all the uniqueSessionControls.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<UniqueSessionControlDTO> findAll(Pageable pageable) {
        log.info("Request to get all UniqueSessionControls");
        return uniqueSessionControlRepository.findAll(pageable).map(uniqueSessionControlMapper::toDto);
    }

    /**
     * Get all the uniqueSessionControls.
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<UniqueSessionControlDTO> findAll() {
        log.info("Request to get all UniqueSessionControls");
        return uniqueSessionControlRepository.findAll().stream().map(uniqueSessionControlMapper::toDto).collect(Collectors.toList());
    }

    /**
     * Get one uniqueSessionControl by loginName.
     *
     * @param loginName
     * 		the loginName of the User
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UniqueSessionControlDTO> findOneByLoginName(String loginName) {
        log.info("Request to get UniqueSessionControl whit loginName: {}", loginName);
        return uniqueSessionControlRepository.findOneByLoginName(loginName).map(uniqueSessionControlMapper::toDto);
    }

    /**
     * Get one uniqueSessionControl by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<UniqueSessionControlDTO> findOne(Long id) {
        log.info("Request to get UniqueSessionControl : {}", id);
        return uniqueSessionControlRepository.findById(id).map(uniqueSessionControlMapper::toDto);
    }

    /**
     * Delete the uniqueSessionControl by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(Long id) {
        log.info("Request to delete UniqueSessionControl : {}", id);
        final Optional<UniqueSessionControl> byId = uniqueSessionControlRepository.findById(id);
        if (byId.isPresent()) {
            updateBlackListJwtElement(byId);
            uniqueSessionControlRepository.deleteById(id);
        }
    }

    /**
     * Delete the uniqueSessionControl by id.
     *
     * @param selectedItemsIds
     * 		the list of uniqueSessionControl´s id to delete
     */
    public void deleteByList(List<Long> selectedItemsIds) {
        log.info("Request to delete all UniqueSessionControl by idList: {}", selectedItemsIds);
        for (Long item : selectedItemsIds) {
            final Optional<UniqueSessionControl> byId = uniqueSessionControlRepository.findById(item);
            if (byId.isPresent()) {
                updateBlackListJwtElement(byId);
            }
        }
        uniqueSessionControlRepository.deleteAllByIdIn(selectedItemsIds);
    }

    private void updateBlackListJwtElement(Optional<UniqueSessionControl> byId) {
        BlackListJwt blackListJwt = new BlackListJwt();
        blackListJwt.setJwt(byId.get().getJwt());
        blackListJwt.setInvalidateTime(ZonedDateTime.now());
        blackListJwtRepository.save(blackListJwt); // Lo agrego a la lista negra cuando se elimina la sesión para que si hay otro logueado no pueda hacer nada.
    }

    public void updateExpireUniqueSession() {
        // log.info("Request to UniqueSessionControl");
        List<UniqueSessionControl> all = this.uniqueSessionControlRepository.findAll();
        ZonedDateTime currentDate = ZonedDateTime.now();
        List<Long> filtered = all.stream().filter(el -> {
            long time = 0;
            if(el.getSessionCheckTime()!=null) {
                time = ChronoUnit.MINUTES.between(el.getSessionCheckTime(), currentDate);
            }
            return time >= this.applicationProperties.getTimeActiveSession();
        }).map(jwt -> jwt.getId()).collect(Collectors.toList());
        // log.info("Lista de elementos de la UniqueSessionControl a eliminar: " + filtered.toString());

        for (UniqueSessionControl uniqueSessionControl :all) {
            for (Long id: filtered) {
                if(uniqueSessionControl.getId() ==  id) {
                    BlackListJwt blackListJwt = new BlackListJwt();
                    blackListJwt.setJwt(uniqueSessionControl.getJwt());
                    blackListJwt.setInvalidateTime(ZonedDateTime.now());
                    blackListJwtRepository.save(blackListJwt);
                    uniqueSessionControl.setSessionCheckTime(null);
                    uniqueSessionControl.setSessionEndTime(ZonedDateTime.now());
                    uniqueSessionControl.setSessionLast(uniqueSessionControl.getSessionEndTime());
                    this.uniqueSessionControlRepository.save(uniqueSessionControl);
                }
            }
        }
        // return this.uniqueSessionControlRepository.deleteAllByIdIn(filtered);
    }
}
