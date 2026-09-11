package testdata;

import org.testng.annotations.DataProvider;
import utils.ConfigReader;
import utils.ExcelUtils;

/**
 * Central place for all @DataProvider methods. Test classes reference
 * these by name (dataProvider = "loginData") rather than each test
 * class reading its own Excel sheet — one source of truth per data set.
 */
public class DataProviderUtils {

    private DataProviderUtils() {
    }

    @DataProvider(name = "loginData")
    public static Object[][] getLoginData() {
        return ExcelUtils.getSheetData(ConfigReader.get("excel.path"), "LoginData");
    }

    @DataProvider(name = "searchData")
    public static Object[][] getSearchData() {
        return ExcelUtils.getSheetData(ConfigReader.get("excel.path"), "SearchData");
    }

    @DataProvider(name = "checkoutData")
    public static Object[][] getCheckoutData() {
        return ExcelUtils.getSheetData(ConfigReader.get("excel.path"), "CheckoutData");
    }
}
