package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

/**
 * Page Object for an individual product detail page — add-to-cart action
 * and reading back product name/price for assertions.
 */
public class ProductPage extends BasePage {

    @FindBy(css = "h2.name")
    private WebElement productName;

    @FindBy(css = "h3.price-container")
    private WebElement productPrice;

    @FindBy(linkText = "Add to cart")
    private WebElement addToCartButton;

    public ProductPage(WebDriver driver) {
        super(driver);
    }

    public String getProductName() {
        return waitAndGetText(productName);
    }

    public String getProductPrice() {
        return waitAndGetText(productPrice);
    }

    public ProductPage addToCart() {
        waitAndClick(addToCartButton);
        switchToAlertAndAccept();
        return this;
    }
}
