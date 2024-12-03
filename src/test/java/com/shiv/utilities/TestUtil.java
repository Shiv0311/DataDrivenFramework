package com.shiv.utilities;

import java.lang.reflect.Method;
import java.util.Hashtable;

import org.apache.poi.ss.usermodel.Sheet;
import org.testng.annotations.DataProvider;

public class TestUtil {

    @DataProvider(name = "excelData")
    public Object[][] provideExcelData(Method m) {
        String filePath = "C:\\Users\\Shivkumar.K\\Selenium-Workspace\\DataDrivenFramework\\src\\test\\resources\\com\\shiv\\excel\\testdata.xlsx";
        String sheetName = m.getName();
        System.out.println("sheetName = " + sheetName);
        ExcelReader excel = new ExcelReader(filePath, sheetName);
        Object[][] data = excel.getData();

        // Convert the Object[][] into a Hashtable-based format
        Object[][] dataWithHashtable = new Object[data.length][1];

        for (int i = 0; i < data.length; i++) {
            Hashtable<String, String> table = new Hashtable<>();
            for (int j = 0; j < data[i].length; j++) {
                String columnName = excel.sheet0.getRow(0).getCell(j).toString(); // Get the column name (header)
                table.put(columnName, data[i][j].toString());
            }
            dataWithHashtable[i][0] = table;
        }

        return dataWithHashtable;
    }

    public static boolean isTestRunnable(String testName, ExcelReader excel) {
        // Ensure the correct sheet is loaded
        String sheetName = "test_suite";
        Sheet testSuiteSheet = excel.sheet0;

        if (!testSuiteSheet.getSheetName().equals(sheetName)) {
            throw new IllegalArgumentException("Incorrect sheet loaded. Expected: " + sheetName);
        }

        int testNameColumn = -1;
        int runModeColumn = -1;

        // Find column indices for "TestName" and "RunMode"
        for (int i = 0; i < testSuiteSheet.getRow(0).getLastCellNum(); i++) {
            String columnName = testSuiteSheet.getRow(0).getCell(i).toString();
            if (columnName.equalsIgnoreCase("TCID")) {
                testNameColumn = i;
            } else if (columnName.equalsIgnoreCase("Runmode")) {
                runModeColumn = i;
            }
        }

        // Ensure required columns exist
        if (testNameColumn == -1 || runModeColumn == -1) {
            throw new IllegalArgumentException("Required columns 'TestName' and 'RunMode' are missing.");
        }

        // Search for the test case and check its RunMode
        for (int i = 1; i <= testSuiteSheet.getLastRowNum(); i++) { // Start from row 1 (skip header)
            String testCase = testSuiteSheet.getRow(i).getCell(testNameColumn).toString();
            if (testCase.equalsIgnoreCase(testName)) {
                String runMode = testSuiteSheet.getRow(i).getCell(runModeColumn).toString();
                return runMode.equalsIgnoreCase("Y");
            }
        }

        // Default return value if testName is not found
        System.err.println("Test case '" + testName + "' not found in the sheet.");
        return false;
    }
}
