package cl.tutorial.desglose.security;

import org.springframework.security.core.AuthenticationException;

/**
 * This exception is thrown in case of an already logged user trying to authenticate.
 */
public class UserAlreadyLoggedInException extends AuthenticationException {

    private static final long serialVersionUID = 1L;

    private String loginDate;

    public UserAlreadyLoggedInException(String message, String loginDate) {
        super(message);
        this.loginDate = loginDate;
    }

    public UserAlreadyLoggedInException(String message, Throwable t, String loginDate) {
        super(message, t);
        this.loginDate = loginDate;
    }

    public String getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(String loginDate) {
        this.loginDate = loginDate;
    }
}
