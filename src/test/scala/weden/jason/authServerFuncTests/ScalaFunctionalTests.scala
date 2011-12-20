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
  private final val LOG: Logger = LogManager.getLogger(classOf[ScalaFunctionalTests])

  @Test(invocationCount = 3, description = "Repeat of test in java but this time from Scala to show calling Java base class")
  def positiveTest() {
    assertTrue(doPositiveTest(USER_PASS).contains("helloWorld"))
  }

  @Test(description = "Bad Basic Auth Header and Random Header scenarios")
  def headerTests() {

    def commonSetup(authValue: String): URLConnection = {
      val url: URL = new URL("http://localhost:" + PORT_NUM + "/" + "hello.txt")
      val con: URLConnection = url.openConnection
      val encoding: String = DatatypeConverter.printBase64Binary(USER_PASS.getBytes)
      con.addRequestProperty("Authorization", authValue + encoding)
      con
    }

    def badBasicHeaderTest() {
      //Negative test -- notice no space after Basic
      val con = commonSetup("Basic")
      con.connect

      try {
        new BufferedReader(new InputStreamReader(con.getInputStream))
        fail("The server is erroneously allowing a bad Basic Auth header -- no space after \"Basic\"");
      } catch {
        case e: java.net.ProtocolException => LOG.info("Request correctly rejected")
        case _ => fail("Unexpected exception");
      }
    }

    def randomHeaderTest() {
      val con = commonSetup("Basic ")
      con.addRequestProperty("Foo", "Bar")
      con.connect

      val in: BufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream))
      val sb: StringBuilder = new StringBuilder
      var line = ""

      while ((({
        line = in.readLine;
        line
      })) != null) {
        sb.append(line + "\n")
      }

      assertTrue(sb.toString().contains("helloWorld"))
    }

    badBasicHeaderTest()
    randomHeaderTest()
  }
}