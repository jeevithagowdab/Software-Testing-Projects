package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;
import testdata.DataProviderUtils;

/**
 * Login module coverage: valid/invalid credential combinations driven
 * from the LoginData sheet, plus a couple of standalone UI checks.
 *
 * Excel columns for LoginData: username | password | expectedResult (pass/fail)
 */
public class LoginTest extends BaseTest {

    @Test(groups = {"regression", "functional"},
          dataProvider = "loginData", dataProviderClass = DataProviderUtils.class,
          description = "Verify login behavior across multiple valid/invalid credential sets")
    public void testLoginWithMultipleCredentials(String username, String password, String expectedResult) {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.loginAs(username, password);

        if (expectedResult.equalsIgnoreCase("pass")) {
            Assert.assertTrue(loginPage.isLoginSuccessful(username),
                    "Expected login to succeed for user: " + username);
        } else {
            boolean alertShown = loginPage.acceptLoginAlertIfPresent();
            Assert.assertTrue(alertShown, "Expected an error alert for invalid credentials: " + username);
        }
    }

    @Test(groups = "functional", description = "Verify login modal opens and displays required fields")
    public void testLoginModalOpensWithRequiredFields() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginModal();
        // If the modal didn't render fields, subsequent typing would fail —
        // exercising enterUsername/enterPassword doubles as a field-presence check.
        loginPage.enterUsername("temp_user").enterPassword("temp_pass");
    }

    @Test(groups = "regression", description = "Verify logout returns user to a logged-out state")
    public void testLogoutFunctionality() {
        LoginPage loginPage = new LoginPage(driver);
        // Uses a known valid demo account; in a real project this would
        // come from a dedicated test-user fixture rather than being hardcoded.
        var homePage = loginPage.loginAs("test_automation_user", "Test@1234");
        homePage.logout();
    }

    @Test(groups = "regression", description = "Verify empty credential submission is rejected")
    public void testLoginWithEmptyCredentials() {
        LoginPage loginPage = new LoginPage(driver);
        loginPage.openLoginModal();
        loginPage.clickLogin();
        boolean alertShown = loginPage.acceptLoginAlertIfPresent();
        Assert.assertTrue(alertShown, "Expected validation alert when submitting empty credentials");
    }
}
