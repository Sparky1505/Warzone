package TestSuites;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test suite configured to execute all test suites associated with this game project.
 *
 */
@RunWith(Suite.class)
@SuiteClasses({ CommandControllerTestSuite.class, MainGameTestSuite.class, MapTestSuite.class })
public class TestSuiteRunner {
}