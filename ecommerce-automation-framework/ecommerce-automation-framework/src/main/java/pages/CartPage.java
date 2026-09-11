package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page Object for the shopping cart page — verifying line items,
 * removing products, and moving to checkout.
 */
public class CartPage extends BasePage {

    @FindBys(@FindBy(css = "tr td:nth-child(2)"))
    private List<WebElement> cartItemNames;

    @FindBys(@FindBy(css = "tr td:nth-child(3)"))
    private List<WebElement> cartItemPrices;

    @FindBy(linkText = "Place Order")
    private WebElement placeOrderButton;

    @FindBy(css = "#totalp")
    private WebElement totalPriceLabel;

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public List<String> getCartItemNames() {
        return cartItemNames.stream().map(WebElement::getText).collect(Collectors.toList());
    }

    public boolean isProductInCart(String productName) {
        return getCartItemNames().stream()
                .anyMatch(name -> name.equalsIgnoreCase(productName.trim()));
    }

    public String getTotalPrice() {
        return waitAndGetText(totalPriceLabel);
    }

    public CheckoutPage clickPlaceOrder() {
        waitAndClick(placeOrderButton);
        return new CheckoutPage(driver);
    }

    public int getCartItemCount() {
        return cartItemNames.size();
    }
}
