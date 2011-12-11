package weden.jason.authServerFuncTests

import org.testng.annotations.Test
import weden.jason.authServerFuncTests.TestBase
import org.testng.Assert._
import org.scalatest.testng.TestNGSuite

class ScalaFunctionalTests extends TestBase with TestNGSuite {
  @Test(invocationCount = 3)
    def easyTest() {
    import TestBase._
    assertTrue(doPositiveTest(USER_PASS).contains("helloWorld"))
   // assertTrue(doPositiveTest("jason:hello5").contains("helloWorld"))
    }


}