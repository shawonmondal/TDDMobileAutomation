package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsDetailsPage;
import com.qa.pages.ProductsPage;
import com.qa.pages.SettingsMenuPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;
import org.testng.asserts.SoftAssert;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class ProductTests extends BaseTest {
    TestUtils testUtils = new TestUtils();
    LoginPage loginPage;
    ProductsPage productsPage;
    SettingsMenuPage settingsMenuPage;
    ProductsDetailsPage productsDetailsPage;

    JSONObject loginUsers;

    @BeforeClass
    public void beforeClass() throws IOException {
        InputStream dataIS = null;
        try {
            String dataFileName = "data/loginUser.json";
            dataIS = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(dataIS);
            loginUsers = new JSONObject(tokener);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (dataIS != null) {
                dataIS.close();
            }
        }
        try {
            closeApp();
            openApp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterClass
    public void afterClass() {
    }

    @BeforeMethod
    public void beforeMethod(Method method) {
        loginPage = new LoginPage();
        testUtils.log().info("\n" + " ********** Starting Test: " + method.getName() + " ************* " + "\n");

        productsPage = loginPage.login(loginUsers.getJSONObject("validUser").getString("username"),
                loginUsers.getJSONObject("validUser").getString("password"));

    }

    @AfterMethod
    public void afterMethod() {

        settingsMenuPage = productsPage.pressMenuButton();
        loginPage = settingsMenuPage.pressLogoutButton();
        try {
            closeApp();
            openApp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void validateProductOnProductsPage() throws InterruptedException {
        SoftAssert softAssert = new SoftAssert();

        String sLBagTitle = productsPage.getProductTitle();
        softAssert.assertEquals(sLBagTitle, getStringsHM().get("products_page_slb_title"));

        String sLBagPrice = productsPage.getProductPrice();
        softAssert.assertEquals(sLBagPrice, getStringsHM().get("products_page_slb_price"));

        softAssert.assertAll();
    }

    @Test
    public void validateProductOnProductsDetailsPage() {
        SoftAssert softAssert = new SoftAssert();

        productsDetailsPage = productsPage.pressProductTitleLink();

        String sLBagTitle = productsDetailsPage.getProductTitleText();
        softAssert.assertEquals(sLBagTitle, getStringsHM().get("product_details_page_slb_title"));

        String sLBagDescribtion = productsDetailsPage.getProductDetailsText();
        softAssert.assertEquals(sLBagDescribtion, getStringsHM().get("product_details_page_slb_txt"));

        String sLBagPrice = productsDetailsPage.scrollToPriceAndValidate();
        softAssert.assertEquals(sLBagPrice, getStringsHM().get("product_details_page_slb_price"));

        productsPage = productsDetailsPage.pressBackToProductsButton();

        softAssert.assertAll();

    }


}
