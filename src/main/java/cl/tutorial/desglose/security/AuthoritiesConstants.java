package cl.tutorial.desglose.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    // public static final String ADMIN = "ROLE_ADMIN";
    // public static final String USER = "ROLE_USER";
    public static final String USER_ROLE_PLAT_DESGLOSE                   = "Plat_Desglose";
    public static final String USER_ROLE_PLAT_DESGLOSE_JEFE              = "Plat_Desglose_Jefe";
    public static final String USER_ROLE_PLAT_DESGLOSE_REVISOR           = "Plat_Desglose_Revisor";
    public static final String USER_ROLE_PLAT_DESGLOSE_EJECUTOR          = "Plat_Desglose_Ejecutor";
    public static final String USER_ROLE_PLAT_DESGLOSE_ADMINISTRADOR     = "Plat_Desglose_Administrador";
    public static final String USER_ROLE_PLAT_DESGLOSE_CUSTOM_CLONE_ROLE = "Plat_Desglose_Custom_Clone";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {
    }
}
