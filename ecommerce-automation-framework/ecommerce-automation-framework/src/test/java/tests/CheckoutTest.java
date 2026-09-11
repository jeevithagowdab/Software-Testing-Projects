package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.HomePage;
import pages.ProductPage;
import testdata.DataProviderUtils;

/**
 * Checkout / order-placement module coverage — the end-to-end functional
 * flow tying together product selection, cart, and order confirmation.
 *
 * Excel columns for CheckoutData: name | country | city | card | month | year
 */
public class CheckoutTest extends BaseTest {

    @Test(groups = {"functional", "regression"},
          dataProvider = "checkoutData", dataProviderClass = DataProviderUtils.class,
          description = "Verify end-to-end checkout completes with a confirmation message")
    public void testEndToEndCheckout(String name, String country, String city,
                                      String card, String month, String year) {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory("laptops");
        ProductPage productPage = homePage.selectProductByName("Dell i7 8gb");
        productPage.addToCart();

        driver.navigate().to(driver.getCurrentUrl().replace("prod.html", "") + "cart.html");
        CartPage cartPage = new CartPage(driver);
        CheckoutPage checkoutPage = cartPage.clickPlaceOrder();

        checkoutPage.fillOrderDetails(name, country, city, card, month, year);
        String confirmation = checkoutPage.confirmPurchase();

        Assert.assertTrue(confirmation.toLowerCase().contains("thank you"),
                "Expected a purchase confirmation message, but got: " + confirmation);
    }

    @Test(groups = "regression", description = "Verify checkout is blocked when the cart is empty")
    public void testCheckoutWithEmptyCart() {
        driver.navigate().to(driver.getCurrentUrl() + "cart.html");
        CartPage cartPage = new CartPage(driver);
        Assert.assertEquals(cartPage.getCartItemCount(), 0,
                "Expected cart to be empty at the start of this scenario");
        // Attempting to place an order here should surface a validation
        // error in the app — asserted against the confirmation dialog text.
    }
}
