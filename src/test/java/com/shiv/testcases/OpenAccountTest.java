package com.shiv.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.shiv.common.BaseSetup;

public class OpenAccountTest extends BaseSetup {
	
	@Test(dataProvider = "excelData", dataProviderClass = com.shiv.utilities.TestUtil.class)
	public void openAccountTest(Hashtable<String, String> data) throws InterruptedException{
		try {
			String customer = data.get("customer");
			String currency = data.get("currency");
			String message = data.get("message");
			String runmode = data.get("runmode");
			
			if(!runmode.equalsIgnoreCase("Y")) {
				throw new SkipException("Skipping the test case as run mode for data set is NO");
			}
			
			click("openAccButton");
			WebElement custname = getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty("custName"))));
			WebElement currencyType = getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty("currency"))));
			select("custName",customer);
			log.debug("Customer with name " + customer + "is selected from the dropdown");
			select("currency",currency);
			log.debug("Currency " +currency+ " is selected from the dropdown");
			click("processButton");
			testReport.get().log(Status.INFO, "Clicked the process button to open the account of the customer");
			Alert alert = driver.switchTo().alert();
			testReport.get().log(Status.INFO, "Switched to alert to check the message");
			String actualAlertMessage = alert.getText();
			String expectedAlertMessage = message;
			Assert.assertTrue(actualAlertMessage.contains(expectedAlertMessage));
			testReport.get().log(Status.INFO, "Message is as requested");
			alert.accept();
//			if(runmode.equals("Y")) {
//				
//				alert.accept();
//				
//			}
//			else {
//				alert.dismiss();
//			}
		} catch(Exception e) {
			
			log.debug("No alert present. Checking mandatory fields.");

            boolean areFieldsEmpty = isTextEmpty("custName") || isTextEmpty("currency");
            // Validate if fields are empty when alert is absent
            if (areFieldsEmpty) {
                log.debug("Mandatory fields are missing or invalid.");
                testReport.get().log(Status.PASS, "Account not opened as expected because mandatory fields are missing or invalid.");
                Assert.assertTrue(true, "Test Passed: Mandatory fields are empty or invalid. No account opened.");
            } else {
                testReport.get().log(Status.FAIL, "No alert present, and all mandatory fields are filled. Test failed.");
                Assert.fail("Test Failed: No alert present, and mandatory fields were filled. Unexpected behavior.");
            }
			
		}
		
		
		
	}

}
