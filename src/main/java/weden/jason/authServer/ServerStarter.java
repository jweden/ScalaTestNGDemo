package weden.jason.authServer;

import java.net.Authenticator;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Starts the server that receives the http GET requests with Basic Auth.  This is called from the tests.
 */
public class ServerStarter {
    public void startAuthServer(int portNum, String userPass) throws Exception {
        BasicAuthenticator auth = new BasicAuthenticator();
        Authenticator.setDefault(auth);
        ServerSocket ss = new ServerSocket(portNum);

        while (true) {
            Socket incoming = ss.accept();
            Runnable r = new BasicAuthServer(incoming, userPass);
            Thread t = new Thread(r);
            t.start();
        }
    }

    /**
     * This is used for developer environment testing only
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        new ServerStarter().startAuthServer(9557, "jason:hello6");
    }
}
