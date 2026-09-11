package utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Captures a screenshot on test failure and saves it under reports/screenshots/,
 * named with the test name + timestamp so repeated runs don't overwrite evidence.
 */
public class ScreenshotUtils {

    private static final Logger logger = LogManager.getLogger(ScreenshotUtils.class);

    private ScreenshotUtils() {
    }

    public static String captureScreenshot(WebDriver driver, String testName) {
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = testName + "_" + timestamp + ".png";
        String destPath = ConfigReader.get("screenshot.path") + fileName;

        try {
            Files.createDirectories(Paths.get(ConfigReader.get("screenshot.path")));

            TakesScreenshot ts = (TakesScreenshot) driver;
            File srcFile = ts.getScreenshotAs(OutputType.FILE);
            File destFile = new File(destPath);
            Files.copy(srcFile.toPath(), destFile.toPath());

            logger.info("Screenshot captured: {}", destPath);
            return destPath;

        } catch (IOException e) {
            logger.error("Failed to capture screenshot for test: {}", testName, e);
            return null;
        }
    }
}
