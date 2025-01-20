package com.qa.listeners;

import com.qa.BaseTest;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

public class TestListner implements ITestListener {

    public void onTestFailure(ITestResult result){
        if(result.getThrowable() != null){
            StringWriter stringWriter = new StringWriter();
            PrintWriter printWriter = new PrintWriter(stringWriter);
            result.getThrowable().printStackTrace(printWriter);
            System.out.println("Failed:--------- "+ stringWriter.toString());
        }
        BaseTest baseTest = new BaseTest();
        File file = baseTest.getDriver().getScreenshotAs(OutputType.FILE);
        System.out.println("Will create ScreenSHot");
        try {
            FileUtils.copyFile(file, new File("SampleScr.png"));
            System.out.println("Created ScreenSHot");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
