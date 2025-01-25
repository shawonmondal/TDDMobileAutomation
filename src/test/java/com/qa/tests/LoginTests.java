package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import com.qa.utils.TestUtils;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {
    TestUtils testUtils = new TestUtils();
    LoginPage loginPage;
    ProductsPage productsPage;
    JSONObject loginUsers;     //It can be kept at the class level as we are not really manipulating it, we are just reading test data from JSON file

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
        testUtils.log().info("++++++++++++++++++ Login-Test-class before method +++++++++++++++++++++");
        loginPage = new LoginPage();
        testUtils.log().info("\n" + " ********** Starting Test: " + method.getName() + " ************* " + "\n");
    }

    @AfterMethod
    public void afterMethod() {
        testUtils.log().info("++++++++++++ Login-Test-class after method +++++++++++++++++");
        try {
            closeApp();
            openApp();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void invalidUserName() {
//        try{                  // By using Try Catch Block
//            loginPage.enterUserName("invalidusername");
//            loginPage.enterPassword("secret_sauce");
//            loginPage.pressLoginButton();
//
//            String actualErrorText = loginPage.getErrorText();
//            testUtils.log().info("Actual Error: "+ actualErrorText);
//            String expectedErrorText = "Username and password do not match any user in this service.";
//            testUtils.log().info("Expected Error: "+ expectedErrorText);
//
//            Assert.assertEquals(actualErrorText, expectedErrorText);
//
//        } catch (Exception e) {
//            StringWriter stringWriter = new StringWriter();
//            PrintWriter printWriter = new PrintWriter(stringWriter);
//            e.printStackTrace(printWriter);
//            testUtils.log().info(stringWriter.toString());
//            Assert.fail(stringWriter.toString());
//        }
//          //  By using Try Catch Block //
        loginPage.enterUserName(loginUsers.getJSONObject("invalidUsername").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidUsername").getString("password"));
        loginPage.pressLoginButton();

        String actualErrorText = loginPage.getErrorText();
        testUtils.log().info("Actual Error: " + actualErrorText);
        String expectedErrorText = getStringsHM().get("err_invalid_username_or_password");
        testUtils.log().info("Expected Error: " + expectedErrorText);

        Assert.assertEquals(actualErrorText, expectedErrorText);
    }

    @Test
    public void invalidPassword() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginButton();

        String actualErrorText = loginPage.getErrorText()+"xxx";
        testUtils.log().info("Actual Error: " + actualErrorText);
//        String expectedErrorText = "Username and password do not match any user in this service.";
        String expectedErrorText = getStringsHM().get("err_invalid_username_or_password");
        testUtils.log().info("Expected Error: " + expectedErrorText);

        Assert.assertEquals(actualErrorText, expectedErrorText);
    }

    @Test
    public void successfulLogin() {

        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginButton();

        String actualHeaderText = productsPage.getProductHeaderTitle();
        String expectedHeaderText = getStringsHM().get("product_title");
        testUtils.log().info("Expected Product Title: " + expectedHeaderText + "\n" + "Actual Product Title: " + actualHeaderText);
        Assert.assertEquals(actualHeaderText, expectedHeaderText);
    }
}
