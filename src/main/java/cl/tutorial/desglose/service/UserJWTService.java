package cl.tutorial.desglose.service;

import cl.tutorial.desglose.config.ApplicationProperties;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.BlackListJwt;
import cl.tutorial.desglose.domain.UniqueSessionControl;
import cl.tutorial.desglose.repository.BlackListJwtRepository;
import cl.tutorial.desglose.repository.UniqueSessionControlRepository;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.security.UserAlreadyLoggedInException;
import cl.tutorial.desglose.security.jwt.TokenProvider;
import cl.tutorial.desglose.web.rest.vm.LoginVM;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing UniqueSessionControl.
 */
@Service
@Transactional
public class UserJWTService {

    private final Logger log = LogManager.getLogger(UserJWTService.class);

    private final UniqueSessionControlRepository uniqueSessionControlRepository;
    private final BlackListJwtRepository         blackListJwtRepository;
    private final TokenProvider                  tokenProvider;
    private final AuthenticationManager          authenticationManager;
    private final ApplicationProperties          applicationProperties;

    public UserJWTService(UniqueSessionControlRepository uniqueSessionControlRepository, BlackListJwtRepository blackListJwtRepository, TokenProvider tokenProvider,
        AuthenticationManager authenticationManager, ApplicationProperties applicationProperties) {
        this.uniqueSessionControlRepository = uniqueSessionControlRepository;
        this.blackListJwtRepository = blackListJwtRepository;
        this.tokenProvider = tokenProvider;
        this.authenticationManager = authenticationManager;
        this.applicationProperties = applicationProperties;
    }
    public String authorize(LoginVM loginVM) {
        final String login = loginVM.getUsername();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login, loginVM.getPassword());

        Authentication authentication = this.authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = (loginVM.isRememberMe() == null) ? false : loginVM.isRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);

        if (applicationProperties.getActiveUniqueSessionControl()) {
            Optional<UniqueSessionControl> byLoginName = uniqueSessionControlRepository.findOneByLoginName(login);
            if (byLoginName.isPresent()) { // Si tiene registro de inicio de sesión.
                UniqueSessionControl uniqueSessionControl = byLoginName.get();
                if (uniqueSessionControl.getSessionCheckTime() != null) { // Si tiene fecha de chequeo de sesión, lanza excepción.
                    String loginDateStr = byLoginName.get().getSessionCheckTime().format(Constants.FORMATTER_DATE_TIME);
                    throw new UserAlreadyLoggedInException("User " + login + " has an active login whit date " + loginDateStr, loginDateStr);
                } else { // Si tiene registro de inicio de sesión sin fecha de inciaio, se le actualiza.
                    uniqueSessionControl.setSessionCheckTime(ZonedDateTime.now());
                    uniqueSessionControl.setSessionLast(uniqueSessionControl.getSessionEndTime());
                    uniqueSessionControl.setSessionEndTime(null);
                    uniqueSessionControl.setJwt(jwt);
                    uniqueSessionControlRepository.save(uniqueSessionControl);
                }
            } else { // Si no tiene registro de inicio de sesión se le crea.
                UniqueSessionControl uniqueSessionControl = new UniqueSessionControl();
                uniqueSessionControl.setLoginName(login);
                uniqueSessionControl.setJwt(jwt);
                uniqueSessionControl.setSessionCheckTime(ZonedDateTime.now());
                uniqueSessionControlRepository.save(uniqueSessionControl);
            }
        }
        return jwt;
    }
    public void update(String login) {
        if (applicationProperties.getActiveUniqueSessionControl()) {
            Optional<UniqueSessionControl> byLoginName = uniqueSessionControlRepository.findOneByLoginName(login);
            if (byLoginName.isPresent()) { // Si tiene registro de inicio de sesión.
                UniqueSessionControl uniqueSessionControl = byLoginName.get();
                    uniqueSessionControl.setSessionCheckTime(ZonedDateTime.now());
                    uniqueSessionControl.setSessionLast(uniqueSessionControl.getSessionLast());
                    uniqueSessionControl.setSessionEndTime(null);
                    uniqueSessionControlRepository.save(uniqueSessionControl);
            }
        }
    }

    public void logout() {
        log.info("INICIO- logout");
        Optional<String> currentUserJWT = SecurityUtils.getCurrentUserJWT();
        Optional<String> currentUserLogin = SecurityUtils.getCurrentUserLogin();
        if (currentUserLogin.isPresent()) {
            Optional<UniqueSessionControl> uniqueSessionControl = uniqueSessionControlRepository.findOneByLoginName(currentUserLogin.get());
            if (uniqueSessionControl.isPresent()) { // Si tiene registro de inicio de sesión lanza excepción.
                UniqueSessionControl toSave = uniqueSessionControl.get();
                toSave.setSessionCheckTime(null);
                toSave.setSessionEndTime(ZonedDateTime.now());
                toSave.setSessionLast(uniqueSessionControl.get().getSessionEndTime());
                uniqueSessionControlRepository.save(toSave);
            }
        }
        if (currentUserJWT.isPresent()) {
            String jwt = currentUserJWT.get();
            // SecurityContext securityContext = SecurityContextHolder.getContext();
            // securityContext.setAuthentication(null);
            // tokenProvider.getAuthentication(jwt).setAuthenticated(false);
            BlackListJwt blackListJwt = new BlackListJwt();
            blackListJwt.setJwt(jwt);
            blackListJwt.setInvalidateTime(ZonedDateTime.now());
            blackListJwtRepository.save(blackListJwt);
            log.debug("FIN - LOGOUT SUCCESS.");
        }
    }
}
