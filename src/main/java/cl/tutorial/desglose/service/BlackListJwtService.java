package cl.tutorial.desglose.service;

import cl.tutorial.desglose.domain.BlackListJwt;
import cl.tutorial.desglose.repository.BlackListJwtRepository;
import cl.tutorial.desglose.security.SecurityUtils;
import io.github.jhipster.config.JHipsterProperties;
import org.apache.logging.log4j.CloseableThreadContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing BlackListJwt.
 */
@Service
@Transactional
public class BlackListJwtService {

    private final Logger log = LogManager.getLogger(BlackListJwtService.class);

    private final BlackListJwtRepository blackListJwtRepository;
    private final JHipsterProperties     jHipsterProperties;

    public BlackListJwtService(BlackListJwtRepository blackListJwtRepository, JHipsterProperties jHipsterProperties) {
        this.blackListJwtRepository = blackListJwtRepository;
        this.jHipsterProperties = jHipsterProperties;
    }

    /**
     * Save a blackListJwt.
     *
     * @param blackListJwt
     * 		the entity to save
     *
     * @return the persisted entity
     */
    public BlackListJwt save(BlackListJwt blackListJwt) {
        return blackListJwtRepository.save(blackListJwt);
    }

    /**
     * Get all the blackListJwts.
     *
     * @param pageable
     * 		the pagination information
     *
     * @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<BlackListJwt> findAll(Pageable pageable) {
        return blackListJwtRepository.findAll(pageable);
    }

    /**
     * Get one blackListJwt by id.
     *
     * @param id
     * 		the id of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public Optional<BlackListJwt> findOne(Long id) {
        return blackListJwtRepository.findById(id);
    }

    /**
     * Get one blackListJwt by jwt.
     *
     * @param jwt
     * 		the jwt of the entity
     *
     * @return the entity
     */
    @Transactional(readOnly = true)
    public List<BlackListJwt> findAllByJwt(String jwt) {
        try (final CloseableThreadContext.Instance ctc = CloseableThreadContext.put("loginId", SecurityUtils.getCurrentUserLogin().orElse("System"))) {
            return blackListJwtRepository.findAllByJwt(jwt);
        }
    }

    /**
     * Delete the blackListJwt by id.
     *
     * @param id
     * 		the id of the entity
     */
    public void delete(Long id) {
        blackListJwtRepository.deleteById(id);
    }

    public Long deleteAllExpiredJwtTokens() {
        long tokenValidityInMinutes = jHipsterProperties.getSecurity().getAuthentication().getJwt().getTokenValidityInSeconds() / 60;
        List<BlackListJwt> all = blackListJwtRepository.findAll();
        ZonedDateTime currentDate = ZonedDateTime.now();
        List<Long> filtered = all.stream().filter(el -> {
            long time = ChronoUnit.MINUTES.between(el.getInvalidateTime(), currentDate);
            return tokenValidityInMinutes - time <= 0;
        }).map(jwt -> jwt.getId()).collect(Collectors.toList());
        return blackListJwtRepository.deleteAllByIdIn(filtered);
    }
}
