package com.qa;

//import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
import com.aventstack.extentreports.Status;
import com.qa.reports.ExtentReport;
import com.qa.utils.TestUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.By;
import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.URL;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

//@Listeners(ExtentITestListenerAdapter.class)
public class BaseTest {
//    private static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    protected static ThreadLocal<AppiumDriver> driver = new ThreadLocal<AppiumDriver>();
    protected static ThreadLocal<Properties> props = new ThreadLocal<Properties>();
    protected static ThreadLocal<HashMap<String, String>> stringsHM = new ThreadLocal<HashMap<String, String>>();
    protected static ThreadLocal<String> platform = new ThreadLocal<String>();
    protected static ThreadLocal<String> dateTime = new ThreadLocal<String>();
    protected static ThreadLocal<String> deviceName = new ThreadLocal<String>();
    private static AppiumDriverLocalService server;
    TestUtils testUtils = new TestUtils();
//    static Logger log = LogManager.getLogger(BaseTest.class.getName());

    public AppiumDriver getDriver() {
        return driver.get();
    }

    public void setDriver(AppiumDriver driver2) {
        driver.set(driver2);
    }

    public Properties getProps() {
        return props.get();
    }

    public void setProps(Properties props2) {
        props.set(props2);
    }

    public HashMap<String, String> getStringsHM() {
        return stringsHM.get();
    }

    public void setStringsHM(HashMap<String, String> stringsHM2) {
        stringsHM.set(stringsHM2);
    }

    public String getPlatform() {
        return platform.get();
    }

    public void setPlatform(String platform2) {
        platform.set(platform2);
    }

    public String getDateTime() {
        return dateTime.get();
    }

    public void setDateTime(String dateTime2) {
        dateTime.set(dateTime2);
    }


    public String getDeviceName() {
        return deviceName.get();
    }

    public void setDeviceName(String deviceName2) {
        deviceName.set(deviceName2);
    }


    //    public BaseTest(){
//	/*
//	In Appium java client versions 9.x.x and later, passing a null driver at the beginning of execution is not
//	permitted, unlike in previous versions. To resolve this issue, comment out the line below and move it into
//	the constructor of each page object class. This ensures that the driver is initialized before the BaseTest
//	constructor is called, preventing it from being null.
//	// PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);
//	 */
//    }

    public void startRecording(){
        try {
            getDriver().executeScript("mobile: startMediaProjectionRecording");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String stopRecording(){
        try {
            getDriver().executeScript("mobile: stopMediaProjectionRecording");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @BeforeMethod
    public void beforeMethod() {
        testUtils.log().info("  *****************************  Super before method  *****************************");
        ((CanRecordScreen) getDriver()).startRecordingScreen();
    }

    @AfterMethod
    public synchronized void afterMethod(ITestResult result) throws Exception {
        testUtils.log().info("  *****************************  Super after method  *****************************");
        String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();

        // int FAILURE = 2; it means failure denotes 2
        if (result.getStatus() == 2) {

            Map<String, String> params = new HashMap<String, String>();
            params = result.getTestContext().getCurrentXmlTest().getAllParameters();

            String dir = "Videos" + File.separator +
                    params.get("platformName") + "_" + params.get("platformVersion") + "_" + params.get("deviceName") + File.separator +
                    getDateTime() + File.separator +
                    result.getTestClass().getRealClass().getSimpleName();

            File videoDir = new File(dir);

            // If directories are not created it will create it if it exists it will just override the files.
            synchronized (videoDir) {
                if (!videoDir.exists()) {
                    videoDir.mkdirs();
                }
            }
            FileOutputStream fileOutputStream = null;
            try {
                fileOutputStream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4");
                fileOutputStream.write(Base64.getDecoder().decode(media));
                fileOutputStream.close();
                testUtils.log().info("Location of Recording: " + videoDir + File.separator + result.getName() + ".mp4");
            } catch (Exception e) {
                testUtils.log().error("error during video capture" + e.toString());
            } finally {
                if(fileOutputStream != null) {
                    fileOutputStream.close();
                }
            }
        }
    }


    @BeforeSuite
    public void beforeSuite() throws Exception, Exception {
        ThreadContext.put("ROUTINGKEY", "ServerLogs");
//		server = getAppiumService(); // -> If using Mac, uncomment this statement and comment below statement
        server = getAppiumServerDefault(); // -> If using Windows, uncomment this statement and comment above statement
        if(!checkIfAppiumServerIsRunnning(4723)) {
            server.start();
            server.clearOutPutStreams(); // -> Comment this if you want to see server logs in the console
            testUtils.log().info("Appium server started");
        } else {
            testUtils.log().info("Appium server already running");
        }
    }

    public boolean checkIfAppiumServerIsRunnning(int port) throws Exception {
        boolean isAppiumServerRunning = false;
        ServerSocket socket;
        try {
            socket = new ServerSocket(port);
            socket.close();
        } catch (IOException e) {
            System.out.println("1");
            isAppiumServerRunning = true;
        } finally {
            socket = null;
        }
        return isAppiumServerRunning;
    }

    @AfterSuite (alwaysRun = true)
    public void afterSuite() {
        if(server.isRunning()){
            server.stop();
            testUtils.log().info("Appium server stopped");
        }
    }

    // for Windows
    public AppiumDriverLocalService getAppiumServerDefault() {
        return AppiumDriverLocalService.buildDefaultService();
    }

    // for Mac. Update the paths as per your Mac setup
    public AppiumDriverLocalService getAppiumService() {
        HashMap<String, String> environment = new HashMap<String, String>();
//        environment.put("PATH", "enter_your_path_here" + System.getenv("PATH"));
//        environment.put("ANDROID_HOME", "enter_android_home_path");
        return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe"))
                .withAppiumJS(new File("C:\\Users\\shawo\\AppData\\Roaming\\npm\\node_modules\\appium\\lib\\main.js"))
                .usingPort(4723)
                .withArgument(GeneralServerFlag.SESSION_OVERRIDE)
//				.withArgument(() -> "--allow-insecure","chromedriver_autodownload")
//                .withEnvironment(environment)
                .withLogFile(new File("ServerLogs/server.log")));
    }


    @Parameters({"emulator", "platformName", "udid", "unlockType", "unlockKey", "deviceName",
            "systemPort", "chromeDriverPort", "wdaLocalPort", "webkitDebugProxyPort"})
    @BeforeTest
    public void beforeTest(@Optional("androidOnly") String emulator, String platformName, String udid, @Optional("androidOnly") String unlockType, @Optional("androidOnly") String unlockKey, String deviceName,
                           @Optional("androidOnly") String systemPort, @Optional("androidOnly") String chromeDriverPort,
                           @Optional("iOSOnly") String wdaLocalPort, @Optional("iOSOnly") String webkitDebugProxyPort ) throws IOException {
        testUtils.log().info("***********************  Super before test  *****************************");

        URL url;

        InputStream inputStream = null;
        InputStream stringsIS = null;

        testUtils = new TestUtils();
        String sessionId;
        setPlatform(platformName);
        setDateTime(testUtils.dateTime());
        setDeviceName(deviceName);
        Properties props = new Properties();
        AppiumDriver driver;


        String strFile = "logs" + File.separator + platformName + "_" + deviceName;
        File logFile = new File(strFile);
        if (!logFile.exists()) {
            logFile.mkdirs();
        }
        //route logs to separate file for each thread
        ThreadContext.put("ROUTINGKEY", strFile);
        testUtils.log().info("log path: " + strFile);


        try {
            props = new Properties();
            String propFileName = "config.properties";
            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
            props.load(inputStream);
            setProps(props);

            // Strings XML Related -
            String xmlFileName = "strings/strings.xml";
            stringsIS = getClass().getClassLoader().getResourceAsStream(xmlFileName);
            setStringsHM(testUtils.parseStringXML(stringsIS)); // returns the hashmap

            DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
            UiAutomator2Options androidOptions = new UiAutomator2Options().
                    setPlatformName(platformName).
                    setNewCommandTimeout(Duration.ofSeconds(1624)).
                    setDeviceName(deviceName).
                    setUdid(udid);

            XCUITestOptions iOSOptions = new XCUITestOptions().setPlatformName(platformName).
                    setNewCommandTimeout(Duration.ofSeconds(1524)).
//                    setPlatformVersion(platformVersion).
                    setDeviceName(deviceName).
                    setUdid(udid);

            url = new URL(props.getProperty("appiumURL"));

            switch (platformName) {
                case "Android":
                    androidOptions.setAutomationName(props.getProperty("androidAutomationName")).
                            setAppPackage(props.getProperty("androidAppPackage")).
                            setAppActivity(props.getProperty("androidAppActivity"));
                    if (emulator.equals("true")) {
                        androidOptions.setAvd(deviceName).setAvdLaunchTimeout(Duration.ofSeconds(260));
                    } else {
                        androidOptions.setUnlockType(unlockType).setUnlockKey(unlockKey);
                    }
                    androidOptions.setSystemPort(Integer.parseInt(systemPort)).
                            setChromedriverPort(Integer.parseInt(chromeDriverPort));

                    String appUrlAndroid = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +
                            File.separator + "resources" + File.separator + "app" +
                            File.separator + "Android.SauceLabs.Mobile.Sample.app.2.7.1.apk";
                    testUtils.log().info("App URL: " + appUrlAndroid);
                    androidOptions.setApp(appUrlAndroid);

//                    url = new URL(props.getProperty("appiumURL") + "4723");         // for running in parallel for both iOS and Android here port is different

                    // This becomes the local variable for this method
                    driver = new AndroidDriver(url, androidOptions);
                    sessionId = driver.getSessionId().toString();
                    break;

                case "iOS":
                    iOSOptions.setAutomationName(props.getProperty("iOSAutomationName"));
//                    MutableCapabilities caps = new MutableCapabilities();
//                    caps.setCapability("appium:app", "storage:filename=SwagLabsMobileApp.app");  // The filename of the mobile app
                    String appUrlIOS = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" +
                            File.separator + "resources" + File.separator + "app" +
                            File.separator + "SwagLabsMobileApp.app";
                    testUtils.log().info("App URL: " + appUrlIOS);

                    iOSOptions.setBundleId(props.getProperty("iOSBundleId"))
                            .setWdaLocalPort(Integer.parseInt(wdaLocalPort));
                    desiredCapabilities.setCapability("appium:webkitDebugProxyPort", webkitDebugProxyPort);
                    iOSOptions.setApp(appUrlIOS);       // Will not Use everytime to save time

//                    url = new URL(props.getProperty("saucelabsURL"));
//                    url = new URL(props.getProperty("appiumURL") + "4724");  // for running in parallel for both iOS and ANdrod here port is different
                    // This becomes the local variable
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
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(TestUtils.WAIT));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitForVisibility(WebElement element, long timeInSecond) {
        timeInSecond = 0;
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(timeInSecond));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public void clickOnElement(WebElement element) {
        waitForTenSecforVisibility(element);
        element.click();
    }


    public void clickOnElement(WebElement element, String msg) {
        waitForTenSecforVisibility(element);
        testUtils.log().info(msg); // For logger
        ExtentReport.getTest().log(Status.INFO, msg); // For Extent Report
        element.click();
    }

    public void enterTextinTextField(WebElement element, String text) {
        waitForTenSecforVisibility(element);
        element.clear();
        element.sendKeys(text);
    }

    public void enterTextinTextField(WebElement element, String text, String msg) {
        waitForTenSecforVisibility(element);
        testUtils.log().info(msg);
        ExtentReport.getTest().log(Status.INFO, msg); // For Extent Report
        element.clear();
        element.sendKeys(text);
    }

    public String getAttributOfElement(WebElement element, String text) {
        waitForTenSecforVisibility(element);
//        return element.getAttribute("text");
        return element.getText();
    }

    public String getTextOfElement(WebElement element, String msg) {
        String txt = null;
        waitForTenSecforVisibility(element);
        switch (getPlatform()) {
            case "Android":
                txt = element.getAttribute("text");
                break;
            case "iOS":
                txt = element.getAttribute("label");
                break;
        }
        testUtils.log().info(msg + txt);
        ExtentReport.getTest().log(Status.INFO, msg + txt); // For Extent Report
        return txt;
    }

    public boolean IsElementPresent(WebElement element) {
        try {
            waitForTenSecforVisibility(element);
            getDriver().findElement((By) element);
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    public void verifyElementPresent(By locator) {
        WebElement element = getDriver().findElement(locator);
        waitForTenSecforVisibility(element);
        Assert.assertTrue(element.isDisplayed(), "Element is not present");
    }

    public void closeApp() throws IOException {
        Properties props = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = null;
        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        props.load(inputStream);
        testUtils.log().info("Closing App");
        switch (getPlatform()) {
            case "Android":
                ((InteractsWithApps) getDriver()).terminateApp(props.getProperty("androidAppPackage"));
                break;

            case "iOS":
                ((InteractsWithApps) getDriver()).terminateApp(props.getProperty("iOSBundleId"));
                break;
        }
    }

    public void openApp() throws IOException {
        Properties props = new Properties();
        String propFileName = "config.properties";
        InputStream inputStream = null;
        inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
        props.load(inputStream);
        testUtils.log().info("Opening App");
        switch (getPlatform()) {
            case "Android":
                ((InteractsWithApps) getDriver()).activateApp(props.getProperty("androidAppPackage"));
                break;

            case "iOS":
                ((InteractsWithApps) getDriver()).activateApp(props.getProperty("iOSBundleId"));
                break;
        }
    }

    public WebElement scrollToElement() {
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

    @AfterTest (alwaysRun = true)
    public void afterTest() {
        if(getDriver() != null){
            getDriver().quit();
        }
    }
}
