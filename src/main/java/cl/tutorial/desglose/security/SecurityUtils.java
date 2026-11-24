package cl.tutorial.desglose.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Optional;

import static cl.tutorial.desglose.config.Constants.*;

/**
 * Utility class for Spring Security.
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    /**
     * Get the login of the current user.
     *
     * @return the login of the current user
     */
    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                return (String) authentication.getPrincipal();
            }
            return null;
        });
    }

    /**
     * Get the authority of the current user.
     *
     * @return the Authority of the current user
     */
    public static Optional<String> getCurrentUserAuthority() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Optional<String> usuario;
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                for (Object item : springSecurityUser.getAuthorities().toArray()) {
                    if (!((GrantedAuthority) item).getAuthority().equals(PLAT_DESGLOSE) && !((GrantedAuthority) item).getAuthority().equals(PLAT_DESGLOSE_ADMINISTRADOR)
                        && !((GrantedAuthority) item).getAuthority().equals(PLAT_DESGLOSE_CUSTOM_CLONE_ROLE)) {
                        return ((GrantedAuthority) item).getAuthority();
                    }
                }
            } else if (authentication.getPrincipal() instanceof String) {
                for (Object item : authentication.getAuthorities().toArray()) {
                    if (!((GrantedAuthority) item).getAuthority().equals(PLAT_DESGLOSE) && !((GrantedAuthority) item).getAuthority().equals(PLAT_DESGLOSE_ADMINISTRADOR)
                        && !((GrantedAuthority) item).getAuthority().equals(PLAT_DESGLOSE_CUSTOM_CLONE_ROLE)) {
                        return ((GrantedAuthority) item).getAuthority();
                    }
                }
            }
            return null;
        });
    }

    /**
     * Get the authorities of the current user. un usuario según la plataforma de desglose no puede tener más de un rol.
     *
     * @return the Authorities of the current user
     */
    @Deprecated
    public static Optional<Collection<?>> getCurrentUserAuthorities() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).map(authentication -> {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                return springSecurityUser.getAuthorities().size() > 0 ? springSecurityUser.getAuthorities() : null;
            } else if (authentication.getPrincipal() instanceof String) {
                return authentication.getAuthorities().size() > 0 ? authentication.getAuthorities() : null;
            }
            return null;
        });
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user
     */
    public static Optional<String> getCurrentUserJWT() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication()).filter(authentication -> authentication.getCredentials() instanceof String)
            .map(authentication -> (String) authentication.getCredentials());
    }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise
     */
    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream().noneMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(AuthoritiesConstants.ANONYMOUS)))
            .orElse(false);
    }

    /**
     * If the current user has a specific authority (security role).
     * <p>
     * The name of this method comes from the isUserInRole() method in the Servlet API
     *
     * @param authority
     * 		the authority to check
     *
     * @return true if the current user has the authority, false otherwise
     */
    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(securityContext.getAuthentication())
            .map(authentication -> authentication.getAuthorities().stream().anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority))).orElse(false);
    }
}
