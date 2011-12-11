package weden.jason.authServer;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.net.Socket;

/**
 * The code for the multi-threaded server that responds to HTTP GET requests with Basic Authentication
 */
public class BasicAuthServer implements Runnable {
    Socket s;
    OutputStream os;
    String userPass;
    String fileNameToRead = null;

    static final String realm = "dx";

    String reply1 = "HTTP/1.1 401 Unauthorized\r\n" +
            "WWW-Authenticate: Basic realm=\"" + realm + "\"\r\n\r\n";

    String reply2 = "HTTP/1.1 200 OK\r\n" +
            "Date: Wed, 30 Nov 2011 12:00:20 GMT\r\n" +
            "Server: Apache/1.3.15 (Unix)\r\n" +
            "Connection: close\r\n" +
            "Content-Type: text/html; charset=iso-8859-1\r\n" +
            "\r\n";


    protected BasicAuthServer(Socket s, String userPass) {
        this.s = s;
        this.userPass = userPass;
    }

    /**
     * Parse through incoming request for the Auth and GET headers
     *
     * @param s -- the socket used
     * @return
     * @throws IOException
     */
    private String readAll(Socket s) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = br.readLine()).length() != 0) {
            if (line.contains("Authorization")) {
                sb.append(line + "\n");
            } else if (line.contains("GET")) {
                String[] slashSplit = line.split("/");
                String[] spaceSplit = slashSplit[1].split(" ");
                fileNameToRead = spaceSplit[0];
            }
        }
        return sb.toString();
    }

    /**
     * run() method for multi-threading.  Sends the correct reply back of which there are only two types now.
     */
    public void run() {
        try {
            String output = readAll(s);
            String[] authSplit = output.split("Authorization: Basic ");
            os = s.getOutputStream();
            if (authSplit.length > 1) {
                String pwdString = new String(DatatypeConverter.parseBase64Binary(authSplit[1]));
                if ((userPass).equals(pwdString)) {
                    os.write((reply2 + readFile()).getBytes());
                } else {
                    os.write(reply1.getBytes());
                }
            } else {
                os.write(reply1.getBytes());
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    /**
     * Read in file from directory specified in tests.
     *
     * @return
     * @throws IOException
     */
    private String readFile() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileNameToRead));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }
}
