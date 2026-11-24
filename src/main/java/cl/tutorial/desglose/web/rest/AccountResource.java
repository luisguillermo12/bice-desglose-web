package cl.tutorial.desglose.web.rest;

import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineRoleRepository;
import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.domain.SfStfOperator;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRole;
import cl.tutorial.desglose.domain.User;
import cl.tutorial.desglose.repository.*;
import cl.tutorial.desglose.security.DomainUserDetailsServiceTurbine;
import cl.tutorial.desglose.security.SecurityUtils;
import cl.tutorial.desglose.security.UserNotActivatedException;
import cl.tutorial.desglose.service.MailService;
import cl.tutorial.desglose.service.UserService;
import cl.tutorial.desglose.service.dto.PasswordChangeDTO;
import cl.tutorial.desglose.service.dto.UserDTO;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserGroupRoleRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserRepository;
import cl.tutorial.desglose.web.rest.errors.*;
import cl.tutorial.desglose.web.rest.vm.KeyAndPasswordVM;
import cl.tutorial.desglose.web.rest.vm.ManagedUserVM;
import com.codahale.metrics.annotation.Timed;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * REST controller for managing the current user's account.
 */
@RestController
@RequestMapping("/api")
public class AccountResource {

    private final Logger log = LogManager.getLogger(AccountResource.class);

    private final UserRepository                  userRepository;
    private final TurbineUserRepository           turbineUserRepository;
    private final TurbineUserGroupRoleRepository  turbineUserGroupRoleRepository;
    private final TurbineRoleRepository           turbineRoleRepository;
    private final SfStfOperatorRepository         sfStfOperatorRepository;
    private final DomainUserDetailsServiceTurbine domainUserDetailsServiceTurbine;

    private final UserService userService;

    private final MailService mailService;

    public AccountResource(UserRepository userRepository, TurbineUserRepository turbineUserRepository, TurbineUserGroupRoleRepository turbineUserGroupRoleRepository,
        TurbineRoleRepository turbineRoleRepository, SfStfOperatorRepository sfStfOperatorRepository, DomainUserDetailsServiceTurbine domainUserDetailsServiceTurbine,
        UserService userService, MailService mailService) {

        this.userRepository = userRepository;
        this.turbineUserRepository = turbineUserRepository;
        this.turbineUserGroupRoleRepository = turbineUserGroupRoleRepository;
        this.turbineRoleRepository = turbineRoleRepository;
        this.sfStfOperatorRepository = sfStfOperatorRepository;
        this.domainUserDetailsServiceTurbine = domainUserDetailsServiceTurbine;
        this.userService = userService;
        this.mailService = mailService;
    }

    /**
     * POST  /register : register the user.
     *
     * @param managedUserVM
     * 		the managed user View Model
     *
     * @throws InvalidPasswordException
     * 		400 (Bad Request) if the password is incorrect
     * @throws EmailAlreadyUsedException
     * 		400 (Bad Request) if the email is already used
     * @throws LoginAlreadyUsedException
     * 		400 (Bad Request) if the login is already used
     */
    @PostMapping("/register")
    @Timed
    @ResponseStatus(HttpStatus.CREATED)
    public void registerAccount(@Valid @RequestBody ManagedUserVM managedUserVM) {
        if (!checkPasswordLength(managedUserVM.getPassword())) {
            throw new InvalidPasswordException();
        }
        User user = userService.registerUser(managedUserVM, managedUserVM.getPassword());
        mailService.sendActivationEmail(user);
    }

    /**
     * GET  /activate : activate the registered user.
     *
     * @param key
     * 		the activation key
     *
     * @throws RuntimeException
     * 		500 (Internal Server Error) if the user couldn't be activated
     */
    @GetMapping("/activate")
    @Timed
    public void activateAccount(@RequestParam(value = "key") String key) {
        Optional<User> user = userService.activateRegistration(key);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this activation key");
        }
    }

    /**
     * GET  /authenticate : check if the user is authenticated, and return its login.
     *
     * @param request
     * 		the HTTP request
     *
     * @return the login if the user is authenticated
     */
    @GetMapping("/authenticate")
    @Timed
    public String isAuthenticated(HttpServletRequest request) {
        log.info("REST request to check if the current user is authenticated");
        return request.getRemoteUser();
    }

    /**
     * GET  /account : get the current user.
     *
     * @return the current user
     *
     * @throws RuntimeException
     * 		500 (Internal Server Error) if the user couldn't be returned
     */
    @GetMapping("/account")
    @Timed
    @SuppressWarnings("Duplicates")
    public UserDTO getAccount() {
        // SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneWithAuthoritiesByLogin);
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        final UserDTO userDTO =
            turbineUserRepository.findOneByLoginName(userLogin).map(UserDTO::new).orElseThrow(() -> new InternalServerErrorException("User could not be found"));
        final SfStfOperator stfOperator = sfStfOperatorRepository.findFirstByLoginName(userLogin);
        if (stfOperator != null && stfOperator.getOperatorCode() != null) {
            userDTO.setStfOperatorCode(stfOperator.getOperatorCode());
        } else {
            throw new InternalServerErrorException("The user can not be found un the SF_STF scheme");
        }
        final Set<Long> rolesIdSet = this.turbineUserGroupRoleRepository.findAllByUserId(userDTO.getId()).stream().map(el -> el.getRoleId()).collect(Collectors.toSet());
        if (rolesIdSet == null || rolesIdSet.isEmpty()) {
            throw new UserNotActivatedException("The User " + userLogin + " has no authorities");
        }
        // final List<TurbineRole> allByRoleIdIn = turbineRoleRepository.findAllByRoleIdIn(rolesIdSet);
        final List<TurbineRole> allByRoleIdIn = turbineRoleRepository.findAllByRoleIdInAndRoleNameStartingWithIgnoreCase(rolesIdSet, Constants.ROLE_PART_NAME_PLAT_DESGLOSE);
        userDTO.setAuthorities(allByRoleIdIn.stream().map(turbineRole -> turbineRole.getRoleName()).collect(Collectors.toSet()));
        // Agregar rol para clonar.
        if (this.domainUserDetailsServiceTurbine.userHasClonePermission(userLogin)) {
            userDTO.getAuthorities().add(Constants.PLAT_DESGLOSE_CUSTOM_CLONE_ROLE);
        }
        return userDTO;
        /*return userService.getUserWithAuthorities()
            .map(UserDTO::new)
            .orElseThrow(() -> new InternalServerErrorException("User could not be found"));*/
    }

    /**
     * POST  /account : update the current user information.
     *
     * @param userDTO
     * 		the current user information
     *
     * @throws EmailAlreadyUsedException
     * 		400 (Bad Request) if the email is already used
     * @throws RuntimeException
     * 		500 (Internal Server Error) if the user login wasn't found
     */
    @PostMapping("/account")
    @Timed
    public void saveAccount(@Valid @RequestBody UserDTO userDTO) {
        final String userLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new InternalServerErrorException("Current user login not found"));
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getLogin().equalsIgnoreCase(userLogin))) {
            throw new EmailAlreadyUsedException();
        }
        Optional<User> user = userRepository.findOneByLogin(userLogin);
        if (!user.isPresent()) {
            throw new InternalServerErrorException("User could not be found");
        }
        userService.updateUser(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getLangKey(), userDTO.getImageUrl());
    }

    /**
     * POST  /account/change-password : changes the current user's password
     *
     * @param passwordChangeDto
     * 		current and new password
     *
     * @throws InvalidPasswordException
     * 		400 (Bad Request) if the new password is incorrect
     */
    @PostMapping(path = "/account/change-password")
    @Timed
    public void changePassword(@RequestBody PasswordChangeDTO passwordChangeDto) {
        if (!checkPasswordLength(passwordChangeDto.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        userService.changePassword(passwordChangeDto.getCurrentPassword(), passwordChangeDto.getNewPassword());
    }

    /**
     * POST   /account/reset-password/init : Send an email to reset the password of the user
     *
     * @param mail
     * 		the mail of the user
     *
     * @throws EmailNotFoundException
     * 		400 (Bad Request) if the email address is not registered
     */
    @PostMapping(path = "/account/reset-password/init")
    @Timed
    public void requestPasswordReset(@RequestBody String mail) {
        mailService.sendPasswordResetMail(userService.requestPasswordReset(mail).orElseThrow(EmailNotFoundException::new));
    }

    /**
     * POST   /account/reset-password/finish : Finish to reset the password of the user
     *
     * @param keyAndPassword
     * 		the generated key and the new password
     *
     * @throws InvalidPasswordException
     * 		400 (Bad Request) if the password is incorrect
     * @throws RuntimeException
     * 		500 (Internal Server Error) if the password could not be reset
     */
    @PostMapping(path = "/account/reset-password/finish")
    @Timed
    public void finishPasswordReset(@RequestBody KeyAndPasswordVM keyAndPassword) {
        if (!checkPasswordLength(keyAndPassword.getNewPassword())) {
            throw new InvalidPasswordException();
        }
        Optional<User> user = userService.completePasswordReset(keyAndPassword.getNewPassword(), keyAndPassword.getKey());

        if (!user.isPresent()) {
            throw new InternalServerErrorException("No user was found for this reset key");
        }
    }

    private static boolean checkPasswordLength(String password) {
        return !StringUtils.isEmpty(password) && password.length() >= ManagedUserVM.PASSWORD_MIN_LENGTH && password.length() <= ManagedUserVM.PASSWORD_MAX_LENGTH;
    }
}
