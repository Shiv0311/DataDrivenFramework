package com.shiv.testcases;

import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.shiv.common.BaseSetup;

public class LoginTest extends BaseSetup {
	
	@Test
	public void loginTest() {
		
		try {
			click("mgrLoginButton");
			log.debug("Manager Login Button clicked");
			String expectedLoggedInUrl = "https://way2automation.com/angularjs-protractor/banking/#/manager";
			Boolean actualLoggedInUrl = getWait().until(ExpectedConditions.urlToBe(expectedLoggedInUrl));
			Assert.assertTrue(actualLoggedInUrl);
			log.debug("Logged in url validated");
			Assert.assertTrue(isElementPresent(By.cssSelector(OR.getProperty("addCustButton"))));
			log.debug("Add Customer Button is present");
			log.debug("Manager Logged in Succcessfully");
		}
		catch(Exception e) {
			e.printStackTrace();
			log.debug(e);
		}
		
	}	

}
