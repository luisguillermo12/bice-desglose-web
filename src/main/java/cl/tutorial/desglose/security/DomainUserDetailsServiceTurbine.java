package cl.tutorial.desglose.security;

import cl.tutorial.desglose.config.Constants;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineRole;
import cl.tutorial.desglose.conexiones.sfdesglose.domain.TurbineUser;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineRoleRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserGroupRoleRepository;
import cl.tutorial.desglose.conexiones.sfdesglose.repository.TurbineUserRepository;
import cl.tutorial.desglose.service.TurbinePermissionService;
import cl.tutorial.desglose.service.TurbineUserGroupRoleService;
import cl.tutorial.desglose.service.dto.TurbinePermissionDTO;
import cl.tutorial.desglose.service.dto.TurbineUsuarioDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static cl.tutorial.desglose.config.Constants.MDB5;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsServiceTurbine implements UserDetailsService {

    private final Logger log = LogManager.getLogger(DomainUserDetailsServiceTurbine.class);

    private final TurbineUserRepository          turbineUserRepository;
    private final TurbineUserGroupRoleRepository turbineUserGroupRoleRepository;
    private final TurbineRoleRepository          turbineRoleRepository;
    private final TurbinePermissionService       turbinePermissionService;

    private final TurbineUserGroupRoleService turbineUserGroupRoleService;

    public DomainUserDetailsServiceTurbine(TurbineUserRepository turbineUserRepository, TurbineUserGroupRoleRepository turbineUserGroupRoleRepository,
        TurbineRoleRepository turbineRoleRepository, TurbinePermissionService turbinePermissionService,TurbineUserGroupRoleService turbineUserGroupRoleService) {
        this.turbineUserRepository = turbineUserRepository;
        this.turbineUserGroupRoleRepository = turbineUserGroupRoleRepository;
        this.turbineRoleRepository = turbineRoleRepository;
        this.turbinePermissionService = turbinePermissionService;
        this.turbineUserGroupRoleService = turbineUserGroupRoleService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String login) {
        log.info("TURBINE Custom authentication for user {}", login);

        List<TurbineUsuarioDTO> listaUsuarios =  turbineUserGroupRoleService.getAllListaUsuariosRolAll();
        int contador = 0;
        for (TurbineUsuarioDTO usuarioDTO : listaUsuarios){
            if(usuarioDTO.getLoginName().equals(login)) {
               contador ++;
            }
        }
        if(contador == 0) {
          throw new UsernameNotFoundException("User with login " + login + " was not found in the turbine database");
        }
        return turbineUserRepository.findOneByLoginName(login).map(user -> createSpringSecurityUserTurbine(login, user))
            .orElseThrow(() -> new UsernameNotFoundException("User with login " + login + " was not found in the turbine database"));
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUserTurbine(String login, TurbineUser user) {
        if (user.getExpire() != null && LocalDate.now().isAfter(user.getExpire())) {
            throw new UserNotActivatedException("User " + login + " has expired whit date " + user.getExpire().format(Constants.FORMATTER_DATE_SHORT));
        }
        if (user.getExpirePassword() != null && LocalDate.now().isAfter(user.getExpirePassword())) {
            throw new UserNotActivatedException("The password for User " + login + " has expired whit date " + user.getExpirePassword().format(Constants.FORMATTER_DATE_SHORT));
        }
        final Set<Long> rolesIdSet = this.turbineUserGroupRoleRepository.findAllByUserId(user.getUserId()).stream().map(el -> el.getRoleId()).collect(Collectors.toSet());
        if (rolesIdSet == null || rolesIdSet.isEmpty()) {
            throw new UserNotActivatedException("The User " + login + " has no authorities");
        }
        // final List<TurbineRole> allByRoleIdIn = turbineRoleRepository.findAllByRoleIdIn(rolesIdSet);
        final List<TurbineRole> allByRoleIdIn = turbineRoleRepository.findAllByRoleIdInAndRoleNameStartingWithIgnoreCase(rolesIdSet, Constants.ROLE_PART_NAME_PLAT_DESGLOSE);
        log.debug("Authorities for the user " + login + " are : " + allByRoleIdIn);
        List<GrantedAuthority> grantedAuthorities = allByRoleIdIn.stream().map(turbineRole -> new SimpleGrantedAuthority(turbineRole.getRoleName())).collect(Collectors.toList());

        // Agregar rol para clonar.
        if (this.userHasClonePermission(login)) {
            grantedAuthorities.add(new SimpleGrantedAuthority(Constants.PLAT_DESGLOSE_CUSTOM_CLONE_ROLE));
        }
        return new org.springframework.security.core.userdetails.User(user.getLoginName(), user.getPasswordValue(), grantedAuthorities);
    }

    /**
     * Método para saber si el usuario actual tiene permisos para clonar.
     *
     * @return
     */
    public boolean userHasClonePermission(final String userLogin) {
        List<TurbinePermissionDTO> turbinePermissionList = this.turbinePermissionService.findAllPermisosByUser(userLogin);
        for (TurbinePermissionDTO item : turbinePermissionList) {
            if (item.getPermissionName().equals(MDB5)) {
                return true;
            }
        }
        return false;
    }
}
