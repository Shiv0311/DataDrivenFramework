package com.shiv.common;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.shiv.utilities.ExtentManager;

public class BaseSetup {

    public static WebDriver driver;
    public static Properties config = new Properties();
    public static Properties OR = new Properties();
    public static FileInputStream fis;
    protected static FluentWait<WebDriver> wait;
    protected static Actions action;
    public static Logger log = LogManager.getLogger(BaseSetup.class);
    public static ExtentReports extent;
    public static ThreadLocal<ExtentTest> testReport = new ThreadLocal<>();
    String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());

    @BeforeSuite
    public void setUp() {
        try {
            if (driver == null) {
                fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\com\\shiv\\properties\\config.properties");
                config.load(fis);
                log.debug("config.properties file loaded");

                fis = new FileInputStream(System.getProperty("user.dir") + "\\src\\test\\resources\\com\\shiv\\properties\\OR.properties");
                OR.load(fis);
                log.debug("OR.properties file loaded");

                String browser = config.getProperty("browser").toLowerCase();
                switch (browser) {
                    case "chrome":
                        driver = new ChromeDriver();
                        log.debug("Chrome browser launched");
                        break;
                    case "firefox":
                        driver = new FirefoxDriver();
                        log.debug("Firefox browser launched");
                        break;
                    case "edge":
                        driver = new EdgeDriver();
                        log.debug("Edge browser launched");
                        break;
                    default:
                        throw new RuntimeException("Invalid browser specified in config.properties");
                }

                driver.get(config.getProperty("testsiteurl"));
                log.debug("Navigated to URL: " + config.getProperty("testsiteurl"));
                driver.manage().window().maximize();
                log.debug("Browser window maximized");

                extent = ExtentManager.createInstance(System.getProperty("user.dir") + "\\target\\ExtentReports\\Extent_Report_"+timestamp+".html");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeMethod
    public void startTest() {
        ExtentTest test = extent.createTest(Thread.currentThread().getStackTrace()[2].getMethodName());
        testReport.set(test);
    }

    @AfterMethod
    public void endTest() {
        extent.flush();
    }

    @AfterSuite
    public void tearDown() {
//        if (driver != null) {
//            driver.quit();
//            log.debug("Browser closed");
//        }
        ExtentManager.flush();
    }

    public FluentWait<WebDriver> getWait() {
        if (wait == null) {
            wait = new FluentWait<>(driver)
                    .withTimeout(Duration.ofSeconds(Long.parseLong(config.getProperty("timeout"))))
                    .pollingEvery(Duration.ofSeconds(Long.parseLong(config.getProperty("pollingTime"))))
                    .ignoring(NoSuchElementException.class);
        }
        return wait;
    }

    public Actions getAction() {
        if (action == null) {
            action = new Actions(driver);
        }
        return action;
    }

    public void click(String locator) {
        getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty(locator)))).click();
        testReport.get().log(Status.INFO, "Clicked on: " + locator);
    }

    public void type(String locator, String value) {
    	getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty(locator)))).clear();;
        getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty(locator)))).sendKeys(value);
        testReport.get().log(Status.INFO, "Entered value '" + value + "' in: " + locator);
    }
    
    static WebElement dropdown;
    public void select(String locator, String value) {
    	
    	dropdown = getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty(locator))));
    	Select select = new Select(dropdown);
    	select.selectByVisibleText(value);
    	testReport.get().log(Status.INFO, "Selecting value "+value+" from dropdown : "+locator);
    	
    }
    
    protected boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
    
    protected boolean isTextEmpty(String locator) {
        try {
            // Wait for the element to be clickable and fetch its value
            String value = getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty(locator))))
                                   .getAttribute("value");
            value = value.trim();
            System.out.println("Value is "+value+" or value "+value.isEmpty());
            // Check if the value is null or consists only of whitespace
            return value == null || value.isEmpty();
        } catch (Exception e) {
        	e.printStackTrace();
            log.debug("Error while checking if text is empty for locator: " + locator, e);
            // Treat as empty in case of exceptions
            return false;
        }
    }
    
    protected String getText(String locator) {
    	
    	return getWait().until(ExpectedConditions.elementToBeClickable(By.cssSelector(OR.getProperty(locator)))).getAttribute("value");
    }
}
