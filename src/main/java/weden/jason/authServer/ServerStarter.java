package weden.jason.authServer;

import java.net.Authenticator;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Starts the server that receives the http GET requests with Basic Auth.  This is called from the tests.
 */
public class ServerStarter {
    ServerSocket ss;

    public void startAuthServer(int portNum, String userPass) throws Exception {
        BasicAuthenticator auth = new BasicAuthenticator();
        Authenticator.setDefault(auth);
        ss = new ServerSocket(portNum);

        while (true) {
                Socket incoming = null;
            try {
                    incoming = ss.accept();
                } catch (Exception e)     {
                    //catch ex which happens when do close
                    break;
                }
                Runnable r = new BasicAuthServer(incoming, userPass);
                Thread t = new Thread(r);
                t.start();
        }
    }

    public void stopAuthServer() throws Exception {
        ss.close();
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
