package weden.jason.authServerFuncTests;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;
import weden.jason.authServer.ServerStarter;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Base class for TestNG test classes. Has support for data-driven tests using TestNG data provider.
 */
public class TestBase {

    private static final Logger LOG = LogManager.getLogger(TestBase.class);
    protected static final String USER_PASS = "jason:hello5";
    protected static final int PORT_NUM = 9565;
    ServerStarter ss;

    @BeforeSuite(description = "Start the Server")
    public void beforeSuite() throws Exception {
        LOG.info("Starting Server");
        new Thread() {
            public void run() {
                try {
                    ss = new ServerStarter();
                    ss.startAuthServer(PORT_NUM, USER_PASS);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @DataProvider(name = "credentials")
    public Object[][] obtainTestCasesDataProvider() {
        return new Object[][]{
                {"jason:hello"},
                {"wont:work"},
                {"hello5:jason"}
        };
    }

    /**
     * Send in a GET request and return the response from this request as a String.
     *
     * @param userPassToUse
     * @return
     * @throws java.io.IOException
     */
    public String doPositiveTest(String userPassToUse) throws IOException {
        URL url = new URL("http://localhost:" + PORT_NUM + "/" + "hello.txt");
        URLConnection con = url.openConnection();
        String encoding = DatatypeConverter.printBase64Binary(userPassToUse.getBytes());
        con.addRequestProperty("Authorization", "Basic " + encoding);
        con.connect();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
            sb.append(line + "\n");
        }
        LOG.info(sb);
        return sb.toString();
    }

    @AfterSuite(description = "Stopping the Server")
    public void afterSuite() throws Exception {
        LOG.info("Stopping Server");
        ss.stopAuthServer();
    }
}
