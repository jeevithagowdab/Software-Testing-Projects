package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.*;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ExtentReportManager;
import utils.ScreenshotUtils;

/**
 * Parent class for all test classes. Handles the WebDriver + ExtentReports
 * lifecycle so individual test classes only contain test logic, not setup
 * boilerplate.
 *
 * @BeforeMethod/@AfterMethod (not @BeforeClass/@AfterClass) are used
 * deliberately: combined with testng.xml's parallel="methods", each test
 * method gets its own browser session via the ThreadLocal in DriverManager,
 * which is what makes parallel execution safe.
 */
public class BaseTest {

    protected static final Logger logger = LogManager.getLogger(BaseTest.class);
    protected WebDriver driver;

    @BeforeSuite(alwaysRun = true)
    public void beforeSuite() {
        ExtentReportManager.getInstance();
        logger.info("=== Test Suite Execution Started ===");
    }

    @BeforeMethod(alwaysRun = true)
    public void setUp(java.lang.reflect.Method method) {
        logger.info("Starting test: {}", method.getName());
        ExtentReportManager.createTest(method.getName(), "Test class: " + method.getDeclaringClass().getSimpleName());

        DriverManager.initDriver();
        driver = DriverManager.getDriver();
        driver.get(ConfigReader.get("url"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        if (result.getStatus() == ITestResult.FAILURE) {
            String screenshotPath = ScreenshotUtils.captureScreenshot(driver, result.getMethod().getMethodName());
            logger.error("Test FAILED: {}", result.getMethod().getMethodName(), result.getThrowable());
            if (screenshotPath != null) {
                try {
                    ExtentReportManager.getTest().fail(result.getThrowable())
                            .addScreenCaptureFromPath(screenshotPath);
                } catch (Exception e) {
                    logger.warn("Could not attach screenshot to report", e);
                }
            }
        } else if (result.getStatus() == ITestResult.SUCCESS) {
            ExtentReportManager.getTest().pass("Test passed");
            logger.info("Test PASSED: {}", result.getMethod().getMethodName());
        } else if (result.getStatus() == ITestResult.SKIP) {
            ExtentReportManager.getTest().skip("Test skipped: " + result.getThrowable());
            logger.warn("Test SKIPPED: {}", result.getMethod().getMethodName());
        }

        DriverManager.quitDriver();
    }

    @AfterSuite(alwaysRun = true)
    public void afterSuite() {
        ExtentReportManager.flush();
        logger.info("=== Test Suite Execution Finished ===");
    }
}
