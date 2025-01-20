package com.qa.tests;

import com.qa.BaseTest;
import com.qa.pages.LoginPage;
import com.qa.pages.ProductsPage;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;

public class LoginTests extends BaseTest {

    LoginPage loginPage;
    ProductsPage productsPage;
    InputStream dataIS;
    JSONObject loginUsers;

    @BeforeClass
    public void beforeClass() throws IOException {
        try{
            String dataFileName = "data/loginUser.json";
            dataIS = getClass().getClassLoader().getResourceAsStream(dataFileName);
            JSONTokener tokener = new JSONTokener(dataIS);
            loginUsers = new JSONObject(tokener);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if(dataIS != null){
                dataIS.close();
            }
        }

    }

    @AfterClass
    public void afterClass(){

    }

    @BeforeMethod
    public void beforeMethod(Method method){
        loginPage = new LoginPage();
        System.out.println("\n" + " ********** Starting Test: "+ method.getName() + " ************* " + "\n");

    }

    @AfterMethod
    public void afterMethod(){

    }


    @Test
    public void invalidUserName() {
//        try{                  // By using Try Catch Block
//            loginPage.enterUserName("invalidusername");
//            loginPage.enterPassword("secret_sauce");
//            loginPage.pressLoginButton();
//
//            String actualErrorText = loginPage.getErrorText();
//            System.out.println("Actual Error: "+ actualErrorText);
//            String expectedErrorText = "Username and password do not match any user in this service.";
//            System.out.println("Expected Error: "+ expectedErrorText);
//
//            Assert.assertEquals(actualErrorText, expectedErrorText);
//
//        } catch (Exception e) {
//            StringWriter stringWriter = new StringWriter();
//            PrintWriter printWriter = new PrintWriter(stringWriter);
//            e.printStackTrace(printWriter);
//            System.out.println(stringWriter.toString());
//            Assert.fail(stringWriter.toString());
//        }
//          //  By using Try Catch Block //
            loginPage.enterUserName(loginUsers.getJSONObject("invalidUsername").getString("username"));
            loginPage.enterPassword(loginUsers.getJSONObject("invalidUsername").getString("password"));
            loginPage.pressLoginButton();

            String actualErrorText = loginPage.getErrorText();
            System.out.println("Actual Error: "+ actualErrorText);
            String expectedErrorText = stringsHM.get("err_invalid_username_or_password");
            System.out.println("Expected Error: "+ expectedErrorText);

            Assert.assertEquals(actualErrorText, expectedErrorText);
    }

    @Test
    public void invalidPassword() {
        loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));
        loginPage.pressLoginButton();

        String actualErrorText = loginPage.getErrorText();
        System.out.println("Actual Error: "+ actualErrorText);
//        String expectedErrorText = "Username and password do not match any user in this service.";
        String expectedErrorText = stringsHM.get("err_invalid_username_or_password");
        System.out.println("Expected Error: "+ expectedErrorText);

        Assert.assertEquals(actualErrorText, expectedErrorText);
    }

    @Test
    public void successfulLogin() {

        loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));
        loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));
        productsPage = loginPage.pressLoginButton();

        String actualHeaderText = productsPage.getProductHeaderTitle();
        String expectedHeaderText = stringsHM.get("product_title");
        System.out.println("Expected Product Title: "+ expectedHeaderText + "\n" + "Actual Product Title: " + actualHeaderText);
        Assert.assertEquals(actualHeaderText, expectedHeaderText);
    }


}
