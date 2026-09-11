package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.HomePage;
import pages.ProductPage;
import testdata.DataProviderUtils;

/**
 * Product search / category filter module coverage.
 * Excel columns for SearchData: category | productName
 */
public class ProductSearchTest extends BaseTest {

    @Test(groups = {"regression", "functional"},
          dataProvider = "searchData", dataProviderClass = DataProviderUtils.class,
          description = "Verify filtering by category and opening a specific product's detail page")
    public void testFilterByCategoryAndOpenProduct(String category, String productName) {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory(category);

        ProductPage productPage = homePage.selectProductByName(productName);
        Assert.assertEquals(productPage.getProductName().trim(), productName.trim(),
                "Product detail page title did not match the selected product");
    }

    @Test(groups = "functional", description = "Verify product listing displays more than zero products by default")
    public void testHomePageDisplaysProducts() {
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.getDisplayedProductCount() > 0,
                "Expected at least one product listed on the home page");
    }

    @Test(groups = "regression", description = "Verify laptops category filter narrows results correctly")
    public void testLaptopsCategoryFilter() {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory("laptops");
        Assert.assertTrue(homePage.getDisplayedProductCount() > 0,
                "Expected laptop products to be listed after filtering");
    }

    @Test(groups = "regression", description = "Verify phones category filter narrows results correctly")
    public void testPhonesCategoryFilter() {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory("phones");
        Assert.assertTrue(homePage.getDisplayedProductCount() > 0,
                "Expected phone products to be listed after filtering");
    }

    @Test(groups = "regression", description = "Verify product price is displayed on the product detail page")
    public void testProductPriceIsDisplayed() {
        HomePage homePage = new HomePage(driver);
        homePage.filterByCategory("monitors");
        ProductPage productPage = homePage.selectProductByName("Apple monitor 24");
        Assert.assertFalse(productPage.getProductPrice().isEmpty(), "Expected product price to be visible");
    }
}
