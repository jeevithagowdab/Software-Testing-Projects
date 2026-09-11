package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for the checkout / place-order modal — fills the order
 * form and confirms purchase, then reads back the confirmation message.
 */
public class CheckoutPage extends BasePage {

    @FindBy(id = "name")
    private WebElement nameField;

    @FindBy(id = "country")
    private WebElement countryField;

    @FindBy(id = "city")
    private WebElement cityField;

    @FindBy(id = "card")
    private WebElement creditCardField;

    @FindBy(id = "month")
    private WebElement monthField;

    @FindBy(id = "year")
    private WebElement yearField;

    @FindBy(xpath = "//button[text()='Purchase']")
    private WebElement purchaseButton;

    @FindBy(css = ".sweet-alert h2")
    private WebElement confirmationHeader;

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public CheckoutPage fillOrderDetails(String name, String country, String city,
                                          String card, String month, String year) {
        waitAndType(nameField, name);
        waitAndType(countryField, country);
        waitAndType(cityField, city);
        waitAndType(creditCardField, card);
        waitAndType(monthField, month);
        waitAndType(yearField, year);
        return this;
    }

    public String confirmPurchase() {
        waitAndClick(purchaseButton);
        return waitAndGetText(confirmationHeader);
    }
}
