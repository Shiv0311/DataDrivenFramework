package com.shiv.testcases;

import java.util.Hashtable;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;
import com.shiv.common.BaseSetup;

public class AddCustomerTest extends BaseSetup {

//    public String sheetName = "AddCustomerTest";

    @Test(dataProvider = "excelData", dataProviderClass = com.shiv.utilities.TestUtil.class)
    public void addCustomerTest(Hashtable<String, String> data) {
    	
    	String firstname = data.get("firstname");
        String lastname = data.get("lastname");
        String zipcode = data.get("postcode");
        String alertText = data.get("alerttext");
        String runmode = data.get("runmode");
        
        if(!runmode.equalsIgnoreCase("Y")) {
			throw new SkipException("Skipping the test case as run mode for data set is NO");
		}

        log.debug("Test parameters - FirstName: " + firstname + ", LastName: " + lastname + ", ZipCode: " + zipcode + ", AlertText: " + alertText + ", RunMode: " + runmode);
        testReport.get().log(Status.INFO, "Starting Add Customer Test with parameters: " + firstname + ", " + lastname + ", " + zipcode);

        click("addCustButton");
        log.debug("Add Customer button clicked");
        testReport.get().log(Status.INFO, "Clicked Add Customer button");

        String expectedAddCustUrl = "https://way2automation.com/angularjs-protractor/banking/#/manager/addCust";
        Assert.assertTrue(getWait().until(ExpectedConditions.urlToBe(expectedAddCustUrl)), "Failed to navigate to Add Customer page.");
        log.debug("Navigated to Add Customer page");
        testReport.get().log(Status.PASS, "Navigated to Add Customer page");

        type("firstNameInput", firstname);
        type("lastNameInput", lastname);
        type("postalCodeInput", zipcode);

        click("addCustomerButton");
        log.debug("Add Customer button clicked");

        try {
            Alert alert = driver.switchTo().alert();
            String actualAlertText = alert.getText();
            
            log.info("Alert detected: " + actualAlertText);
            log.info("actualAlertText = "+actualAlertText.contains(alertText));

            boolean areFieldsNotEmpty = !(isTextEmpty("firstNameInput") && isTextEmpty("lastNameInput") && isTextEmpty("postalCodeInput"));
            boolean areFieldsEmpty = isTextEmpty("firstNameInput") && isTextEmpty("lastNameInput") && isTextEmpty("postalCodeInput");
            log.debug("areFieldsNotEmpty = " + areFieldsNotEmpty);
            log.debug("areFieldsEmpty = " + areFieldsEmpty);
            // Scenario 1: Alert text matches and all fields are not empty
            if ((actualAlertText.contains(alertText) && areFieldsNotEmpty)) {
                testReport.get().log(Status.PASS, "Alert text matches, and fields are not empty. Test Passed.");
                Assert.assertTrue(true);
            }
            // Scenario 2: Alert text does not match and all fields are empty
            else if ((!actualAlertText.contains(alertText) && areFieldsEmpty) || (actualAlertText.contains("Please check the details") && areFieldsEmpty)) {
                testReport.get().log(Status.PASS, "Alert text does not match, and fields are empty. Test Passed.");
                Assert.assertTrue(true);
            }
            // Any other scenario is a failure
            else {
                log.debug("Unexpected condition: Alert text or field states do not match the expected scenarios.");
                testReport.get().log(Status.FAIL, "Test failed: Unexpected condition. Alert text: " + actualAlertText);
                Assert.fail("Test failed: Unexpected condition. Alert text: " + actualAlertText);
            }
            
            alert.accept();

            // Accept or dismiss the alert based on runMode
            
        } catch (NoAlertPresentException e) {
            log.debug("No alert present. Checking mandatory fields.");

            boolean areFieldsEmpty = isTextEmpty("firstNameInput") || isTextEmpty("lastNameInput") || isTextEmpty("postalCodeInput");
            // Validate if fields are empty when alert is absent
            if (areFieldsEmpty) {
                log.debug("Mandatory fields are missing or invalid.");
                testReport.get().log(Status.PASS, "Customer not added as expected because mandatory fields are missing or invalid.");
                Assert.assertTrue(true, "Test Passed: Mandatory fields are empty or invalid. No customer added.");
            } else {
                testReport.get().log(Status.FAIL, "No alert present, and all mandatory fields are filled. Test failed.");
                Assert.fail("Test Failed: No alert present, and mandatory fields were filled. Unexpected behavior.");
            }
        }

        testReport.get().log(Status.PASS, "Add Customer Test Completed Successfully.");
    }

}
