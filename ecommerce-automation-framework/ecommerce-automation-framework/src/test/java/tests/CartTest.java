package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.HomePage;
import pages.ProductPage;

/**
 * Cart module coverage: add-to-cart flow and cart-state verification.
 */
public class CartTest extends BaseTest {

    @Test(groups = {"functional", "regression"}, description = "Verify a product can be added to the cart")
    public void testAddSingleProductToCart() {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory("laptops");
        ProductPage productPage = homePage.selectProductByName("Sony vaio i5");
        productPage.addToCart();

        driver.navigate().to(driver.getCurrentUrl().replace("prod.html", "") + "cart.html");
        CartPage cartPage = new CartPage(driver);
        Assert.assertTrue(cartPage.isProductInCart("Sony vaio i5"),
                "Expected 'Sony vaio i5' to appear in the cart after adding it");
    }

    @Test(groups = "regression", description = "Verify multiple products can be added and both appear in cart")
    public void testAddMultipleProductsToCart() {
        HomePage homePage = new HomePage(driver);

        homePage.filterByCategory("phones");
        ProductPage product1 = homePage.selectProductByName("Samsung galaxy s6");
        product1.addToCart();

        driver.navigate().back();
        homePage.filterByCategory("phones");
        ProductPage product2 = homePage.selectProductByName("Nokia lumia 1520");
        product2.addToCart();

        driver.navigate().to(driver.getCurrentUrl().replace("prod.html", "") + "cart.html");
        CartPage cartPage = new CartPage(driver);
        Assert.assertEquals(cartPage.getCartItemCount(), 2,
                "Expected 2 items in the cart after adding two products");
    }

    @Test(groups = "regression", description = "Verify cart total price is calculated and displayed")
    public void testCartTotalPriceDisplayed() {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory("laptops");
        ProductPage productPage = homePage.selectProductByName("MacBook air");
        productPage.addToCart();

        driver.navigate().to(driver.getCurrentUrl().replace("prod.html", "") + "cart.html");
        CartPage cartPage = new CartPage(driver);
        Assert.assertFalse(cartPage.getTotalPrice().isEmpty(), "Expected a non-empty cart total");
    }
}
