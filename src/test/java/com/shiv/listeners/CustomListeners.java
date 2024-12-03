package com.shiv.listeners;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.SkipException;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.shiv.common.BaseSetup;
import com.shiv.utilities.ExcelReader;
import com.shiv.utilities.TestUtil;

public class CustomListeners extends BaseSetup implements ITestListener {

    @Override
    public void onTestStart(ITestResult result) {
    	ExcelReader excel = new ExcelReader("C:\\Users\\Shivkumar.K\\Selenium-Workspace\\DataDrivenFramework\\src\\test\\resources\\com\\shiv\\excel\\testdata.xlsx", "test_suite");
        ExtentTest test = extent.createTest(result.getMethod().getMethodName());
        
        if(!TestUtil.isTestRunnable(result.getMethod().getMethodName(), excel)) {
        	throw new SkipException("Skipping the test as Run Mode is NO");
        }
        testReport.set(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        testReport.get().log(Status.PASS, "Test Passed: " + result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        testReport.get().log(Status.FAIL, "Test Failed: " + result.getMethod().getMethodName());
        testReport.get().log(Status.FAIL, result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        testReport.get().log(Status.SKIP, "Test Skipped: " + result.getMethod().getMethodName());
    }

    @Override
    public void onFinish(ITestContext context) {
        extent.flush();
    }
}
