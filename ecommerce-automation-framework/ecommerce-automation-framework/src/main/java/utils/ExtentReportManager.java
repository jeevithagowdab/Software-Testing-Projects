package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

/**
 * Wraps ExtentReports setup so tests get a shared HTML report instance,
 * with a ThreadLocal<ExtentTest> so parallel test threads log to their
 * own test node instead of interleaving into one node.
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = ConfigReader.get("report.path");
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
            sparkReporter.config().setTheme(Theme.STANDARD);
            sparkReporter.config().setDocumentTitle("E-Commerce Automation Report");
            sparkReporter.config().setReportName("Regression Suite Execution Report");

            extent = new ExtentReports();
            extent.attachReporter(sparkReporter);
            extent.setSystemInfo("Application", "Demoblaze E-Commerce");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("Browser", ConfigReader.get("browser"));
            extent.setSystemInfo("OS", System.getProperty("os.name"));
        }
        return extent;
    }

    public static void createTest(String testName, String description) {
        ExtentTest extentTest = getInstance().createTest(testName, description);
        test.set(extentTest);
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
