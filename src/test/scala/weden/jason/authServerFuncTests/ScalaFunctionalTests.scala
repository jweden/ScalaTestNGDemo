package weden.jason.authServerFuncTests

import org.testng.annotations.Test
import org.testng.Assert._
import org.scalatest.testng.TestNGSuite
import java.net.{URLConnection, URL}
import javax.xml.bind.DatatypeConverter
import TestBase._
import java.io.{InputStreamReader, BufferedReader}
import org.apache.log4j.{Logger, LogManager}

class ScalaFunctionalTests extends TestBase with TestNGSuite {
  private final val LOG: Logger = LogManager.getLogger(classOf[TestBase])

  @Test(invocationCount = 3, description = "Repeat of test but this time from Scala")
    def easyTest() {
      assertTrue(doPositiveTest(USER_PASS).contains("helloWorld"))
    }

  @Test(description = "Bad Basic Auth Header -- no space after \"Basic\"")
  def badBasicHeaderTest() {
    val url: URL = new URL("http://localhost:" + PORT_NUM + "/" + "hello.txt")
    val con: URLConnection = url.openConnection
    val encoding: String = DatatypeConverter.printBase64Binary(USER_PASS.getBytes)

    //Negative test -- notice no space after Basic
    con.addRequestProperty("Authorization", "Basic" + encoding)
    con.connect

    try {
      new BufferedReader(new InputStreamReader(con.getInputStream))
      fail("The server is erroneously allowing a bad Basic Auth header -- no space after \"Basic\"");
    } catch  {
      case e:java.net.ProtocolException =>  LOG.info("Request correctly rejected")
      case _ =>  fail("Unexpected exception");
    }
  }
}