package com.shiv.utilities;

import java.io.FileInputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class ExcelReader {
	
	public String path;
	public FileInputStream fis;
	public Sheet sheet0;
	
	public ExcelReader(String path, String sheetName) {
		
		this.path=path;
		try {
			System.out.println();
			fis = new FileInputStream(path);
			System.out.println("path of xls is "+fis.toString());
			Workbook wb = WorkbookFactory.create(fis);
			sheet0 = wb.getSheetAt(wb.getSheetIndex(sheetName));			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public Object[][] getData() {
		int rowCount = sheet0.getLastRowNum();
	    int colCount = sheet0.getRow(0).getLastCellNum();
		Object[][] data = new Object[rowCount][colCount];
		for(int i=1; i<=sheet0.getLastRowNum(); i++) {
			
			for(int j=0; j<sheet0.getRow(i).getLastCellNum(); j++) {
				try {
					data[i-1][j] = sheet0.getRow(i).getCell(j).toString();
					System.out.println("Data at [" + (i - 1) + "][" + j + "]: " + data[i - 1][j]);
				} catch (Exception e) {
                    data[i - 1][j] = ""; // Default empty string for missing cells
                    System.err.println("Error reading data at [" + (i - 1) + "][" + j + "]: " + e.getMessage());
                }
				
			}
			
		}
		return data;
		
	}

}
