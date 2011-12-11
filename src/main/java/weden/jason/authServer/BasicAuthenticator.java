package weden.jason.authServer;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

/**
 * This small class is needed to enable Basic Authentication
 */
public class BasicAuthenticator extends Authenticator {
    protected BasicAuthenticator() {
        super();
    }

    protected PasswordAuthentication getPasswordAuthentication() {
        return (new PasswordAuthentication("user", "password".toCharArray()));
    }
}
