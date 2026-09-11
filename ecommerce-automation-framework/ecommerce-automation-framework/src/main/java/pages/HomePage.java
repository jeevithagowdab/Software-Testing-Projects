package pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

/**
 * Page Object for the home / product listing page — category navigation,
 * product search/filter, and selecting a product to open its detail page.
 */
public class HomePage extends BasePage {

    @FindBy(id = "nameofuser")
    private WebElement loggedInUserLabel;

    @FindBy(id = "logout2")
    private WebElement logoutLink;

    @FindBy(linkText = "Laptops")
    private WebElement laptopsCategory;

    @FindBy(linkText = "Phones")
    private WebElement phonesCategory;

    @FindBy(linkText = "Monitors")
    private WebElement monitorsCategory;

    @FindBys(@FindBy(css = "div.card-block h4.card-title a"))
    private List<WebElement> productTitles;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isUserLoggedIn(String expectedUsername) {
        return isDisplayed(loggedInUserLabel) && waitAndGetText(loggedInUserLabel).contains(expectedUsername);
    }

    public void logout() {
        waitAndClick(logoutLink);
    }

    public HomePage filterByCategory(String category) {
        switch (category.toLowerCase()) {
            case "laptops":
                waitAndClick(laptopsCategory);
                break;
            case "phones":
                waitAndClick(phonesCategory);
                break;
            case "monitors":
                waitAndClick(monitorsCategory);
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }
        return this;
    }

    public ProductPage selectProductByName(String productName) {
        for (WebElement product : productTitles) {
            if (product.getText().trim().equalsIgnoreCase(productName.trim())) {
                waitAndClick(product);
                return new ProductPage(driver);
            }
        }
        throw new org.openqa.selenium.NoSuchElementException(
                "Product not found on page: " + productName);
    }

    public int getDisplayedProductCount() {
        return productTitles.size();
    }
}
