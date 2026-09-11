package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the Login modal.
 * Locators are declared with @FindBy so Page Factory lazily initializes
 * them — elements are only located when first interacted with, not at
 * object construction time.
 */
public class LoginPage extends BasePage {

    @FindBy(id = "login2")
    private WebElement loginLink;

    @FindBy(id = "loginusername")
    private WebElement usernameField;

    @FindBy(id = "loginpassword")
    private WebElement passwordField;

    @FindBy(xpath = "//button[text()='Log in']")
    private WebElement loginButton;

    @FindBy(id = "nameofuser")
    private WebElement loggedInUserLabel;

    private final By loginModal = By.id("logInModal");

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public LoginPage openLoginModal() {
        waitAndClick(loginLink);
        waitForElementVisible(loginModal);
        return this;
    }

    public LoginPage enterUsername(String username) {
        waitAndType(usernameField, username);
        return this;
    }

    public LoginPage enterPassword(String password) {
        waitAndType(passwordField, password);
        return this;
    }

    public HomePage clickLogin() {
        waitAndClick(loginButton);
        return new HomePage(driver);
    }

    /**
     * Convenience method chaining the full login flow —
     * this is what test classes call directly.
     */
    public HomePage loginAs(String username, String password) {
        openLoginModal();
        enterUsername(username);
        enterPassword(password);
        return clickLogin();
    }

    public boolean isLoginSuccessful(String expectedUsername) {
        String label = waitAndGetText(loggedInUserLabel);
        return label.contains(expectedUsername);
    }

    public boolean acceptLoginAlertIfPresent() {
        try {
            switchToAlertAndAccept();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
