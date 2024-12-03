package com.shiv.utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentManager {

    private static ExtentReports extent;

    public static ExtentReports createInstance(String fileName) {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(fileName);
        sparkReporter.config().setTheme(Theme.DARK);
        sparkReporter.config().setDocumentTitle("Test Automation Report");
        sparkReporter.config().setEncoding("utf-8");
        sparkReporter.config().setReportName("Automation Test Results");

        extent = new ExtentReports();
        extent.attachReporter(sparkReporter);
        extent.setSystemInfo("Automation Engineer", "Shivkumar Konnuri");
        extent.setSystemInfo("Organization", "Saksoft Limited");
        extent.setSystemInfo("Build Version", "ELMS_Customer_Portal-1.0");

        return extent;
    }

    public static void flush() {
        if (extent != null) {
            extent.flush();
        }
    }
}
