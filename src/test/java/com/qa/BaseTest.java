package com.qa;

import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.Properties;

public class BaseTest {
    protected static AppiumDriver driver;
    protected static Properties props;
    protected static HashMap<String, String> stringsHM = new HashMap<String, String>();
    protected static String platform;
    InputStream inputStream;
    InputStream stringsIS;

    TestUtils utils;


//    public BaseTest(){
//        PageFactory.initElements(new AppiumFieldDecorator(driver),this);
//    }


    public static AppiumDriver getDriver() {
        return driver;
    }

    public void setDriver(AppiumDriver driver) {
        this.driver = driver;
    }


    @Parameters({"emulator", "platformName", "platformVersion", "udid", "unlockType", "unlockKey", "deviceName"})
    @BeforeTest
    public void beforeTest(String emulator, String platformName, String platformVersion, String udid, String unlockType, String unlockKey, String deviceName) throws IOException {
        URL url;
        String sessionId;
        platform = platformName;
        try {
//            InputStream inputStream;
            props = new Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);

            // Strings XML Related -
            String xmlFileName = "strings/strings.xml";
            stringsIS = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            utils = new TestUtils();
            stringsHM = utils.parseStringXML(stringsIS); // returns the hashmap

            UiAutomator2Options androidOptions = new UiAutomator2Options().
                    setPlatformName(platformName).
                    setNewCommandTimeout(Duration.ofSeconds(524));
//                    setPlatformVersion(platformVersion).
//                    setDeviceName(deviceName);

            XCUITestOptions iOSOptions = new XCUITestOptions().setPlatformName(platformName).
                    setNewCommandTimeout(Duration.ofSeconds(524)).
                    setPlatformVersion(platformVersion).
                    setDeviceName(deviceName);

            switch (platformName) {
                case "Android":
                    String appUrlAndroid = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +
                            File.separator + "resources" + File.separator + "app" +
                            File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
                    System.out.println("App URL: " + appUrlAndroid);

                    androidOptions.setAutomationName(props.getProperty("androidAutomationName")).
                            setAppPackage(props.getProperty("androidAppPackage")).
                            setAppActivity(props.getProperty("androidAppActivity"));
                    if (emulator.equals("true")) {
                        androidOptions.setPlatformVersion(platformVersion).
                                setDeviceName(deviceName).
                                setAvd(deviceName);
                    } else {
                        androidOptions.setUdid(udid);
                        androidOptions.setUnlockType(unlockType).setUnlockKey(unlockKey);
                    }
                    androidOptions.setApp(appUrlAndroid);
                    url = new URL(props.getProperty("appiumURL"));
                    driver = new AndroidDriver(url, androidOptions);
                    sessionId = driver.getSessionId().toString();
                    break;

                case "iOS":
                    String appUrlIOS = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +
                            File.separator + "resources" + File.separator + "app" +
                            File.separator + "SwagLabsMobileApp.app";
                    System.out.println("App URL: " + appUrlIOS);

                    MutableCapabilities caps = new MutableCapabilities();
                    caps.setCapability("appium:app", "storage:filename=SwagLabsMobileApp.app");  // The filename of the mobile app

                    iOSOptions.setAutomationName(props.getProperty("iOSAutomationName"));
                    iOSOptions.setApp(appUrlIOS); // Will not Use to save time
                    iOSOptions.setBundleId(props.getProperty("iOSBundleId"));

                    MutableCapabilities sauceOptions = new MutableCapabilities();
                    sauceOptions.setCapability("username", "oauth-shawonmondal80-8d52b");
                    sauceOptions.setCapability("accessKey", "*****c505");
                    sauceOptions.setCapability("build", "appium-build-2F0P7");
                    sauceOptions.setCapability("name", "Shawon");
                    sauceOptions.setCapability("deviceOrientation", "PORTRAIT");
                    caps.setCapability("sauce:options", sauceOptions);

                    url = new URL(props.getProperty("saucelabsURL"));
                    driver = new IOSDriver(url, iOSOptions);
                    sessionId = driver.getSessionId().toString();

                    break;
                default:
                    throw new Exception("Invalid Platform!!! - " + platformName);
            }
            setDriver(driver);

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                inputStream.close();  // Close The input Stream
            }
            if (stringsIS != null) {
                stringsIS.close();  // Close The input Stream
            }
        }
    }

    public void waitForTenSecforVisibility(WebElement element) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForVisibility(WebElement element, long timeInSecond) {
        timeInSecond = 0;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeInSecond));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void clickOnElement(WebElement element) {
        waitForTenSecforVisibility(element);
        element.click();
    }

    public void enterTextinTextField(WebElement element, String text) {
        waitForTenSecforVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    public String getAttributOfElement(WebElement element, String text) {
        waitForTenSecforVisibility(element);
//        return element.getAttribute("text");
        return element.getText();
    }

    public String getTextOfElement(WebElement element) {
        waitForTenSecforVisibility(element);
        switch (platform) {
            case "Android":
                return element.getAttribute("text");

            case "iOS":
                return element.getAttribute("label");
        }
        return null;
    }

    public boolean IsElementPresent(WebElement element){
        try{
            waitForTenSecforVisibility(element);
            driver.findElement((By) element);
            return true;

        } catch (Exception e){
            return false;
        }
    }

    public void verifyElementPresent(By locator){
        WebElement element = driver.findElement(locator);
        waitForTenSecforVisibility(element);
        Assert.assertTrue(element.isDisplayed(), "Element is not present");
    }

    public void closeApp() throws IOException {
        props = new Properties();
        String propFileName = "config.properties";
        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        props.load(inputStream);
        System.out.println("Closing App");
        switch (platform) {
            case "Android":
                ((InteractsWithApps) driver).terminateApp(props.getProperty("androidAppPackage"));
                break;

            case "iOS":
                ((InteractsWithApps) driver).terminateApp(props.getProperty("iOSBundleId"));
                break;
        }
    }

    public void openApp() throws IOException {
        props = new Properties();
        String propFileName = "config.properties";
        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        props.load(inputStream);
        System.out.println("Opening App");
        switch (platform) {
            case "Android":
                ((InteractsWithApps) driver).activateApp(props.getProperty("androidAppPackage"));
                break;

            case "iOS":
                ((InteractsWithApps) driver).activateApp(props.getProperty("iOSBundleId"));
                break;
        }
    }
    public WebElement scrollToElement(){
//        return driver.findElement(AppiumBy.androidUIAutomator(
//                "new UiScrollable( new UiSelector()"+".description(\"test-Inventory item page\")).scrollIntoView("
//                + "new UiSelector().description(\"test-Price\"));"));
        return getDriver().findElement(AppiumBy.androidUIAutomator(
                "new UiScrollable(new UiSelector()" + ".scrollable(true)).scrollIntoView("
                        + "new UiSelector().description(\"test-Price\"));"));
    }

    public void iOSScrollToElement() {
//	  RemoteWebElement element = (RemoteWebElement)getDriver().findElement(By.name("test-ADD TO CART")); // Accessibility ID is needed for this one
//	  String elementID = element.getId();
        HashMap<String, String> scrollObject = new HashMap<String, String>();
//	  scrollObject.put("element", elementID);
        scrollObject.put("direction", "down");
//	  scrollObject.put("predicateString", "label == 'ADD TO CART'");
//	  scrollObject.put("name", "test-ADD TO CART");
//	  scrollObject.put("toVisible", "sdfnjksdnfkld");
        getDriver().executeScript("mobile:scroll", scrollObject);
    }

    @AfterTest
    public void afterTest() {
        driver.quit();
    }
}
