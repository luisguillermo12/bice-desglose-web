package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.security.jwt.JWTFilter;
import cl.tutorial.desglose.service.UserJWTService;
import cl.tutorial.desglose.web.rest.vm.LoginVM;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URISyntaxException;

/**
 * Controller to authenticate users.
 */
@RestController
@RequestMapping("/api")
public class UserJWTController {

    private final UserJWTService userJWTService;

    private final Logger log = LogManager.getLogger(UserJWTController.class);

    public UserJWTController(UserJWTService userJWTService) {
        this.userJWTService = userJWTService;
    }

    @PostMapping("/authenticate")
    @Timed
    public ResponseEntity<JWTToken> authorize(@Valid @RequestBody LoginVM loginVM) {
        String jwt = userJWTService.authorize(loginVM);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JWTFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);
        return new ResponseEntity<>(new JWTToken(jwt), httpHeaders, HttpStatus.OK);
    }
    @PostMapping("/update-session")
    @Timed
    public ResponseEntity<Void> update(@RequestBody String login) {
        userJWTService.update(login);
        return ResponseEntity.ok().headers(new HttpHeaders()).build();
    }

    /**
     * POST  /logout : Invalidate the user JWT.
     *
     * @return the ResponseEntity with status 201 (Created) and with body the new uniqueSessionControlDTO, or with status 400 (Bad Request) if the uniqueSessionControl has already
     * an ID
     *
     * @throws URISyntaxException
     * 		if the Location URI syntax is incorrect
     */
    @PostMapping("/logout")
    @Timed
    public ResponseEntity<Void> logout() throws URISyntaxException {
        log.info("REST request to logout");
        userJWTService.logout();
        return ResponseEntity.ok().headers(new HttpHeaders()).build();
    }

    /**
     * Object to return as body in JWT Authentication.
     */
    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
