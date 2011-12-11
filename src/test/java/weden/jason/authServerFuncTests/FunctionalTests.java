package weden.jason.authServerFuncTests;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import javax.xml.bind.DatatypeConverter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * The actual tests themselves.
 */
public class FunctionalTests extends TestBase {

    private static final Logger LOG = LogManager.getLogger(FunctionalTests.class);

    @Test(enabled = true, description = "Single Request test -- positive scenario", groups = "functional")
    public void validateFileTest() throws IOException {
        assertTrue(doPositiveTest(USER_PASS).contains("helloWorld"));
    }

    @Test(enabled = true, description = "5 Concurrent GET Requests -- positive scenario", invocationCount = 5, threadPoolSize = 5, groups = "functional, multiThread")
    public void concurrentRequestsTest()
            throws InterruptedException, IOException {
        assertTrue(doPositiveTest(USER_PASS).contains("helloWorld"));
    }

    @Test(enabled = true, description = "5 Sequential GET Requests for each bad user/pass -- negative tests", expectedExceptions = java.net.ProtocolException.class, dataProvider = "credentials", invocationCount = 5, groups = "functional, multiThread")
    public void sequentialRequestsTest(final String userPass)
            throws InterruptedException, IOException {
        assertFalse(doPositiveTest(userPass).contains("helloWorld"));
    }




}
