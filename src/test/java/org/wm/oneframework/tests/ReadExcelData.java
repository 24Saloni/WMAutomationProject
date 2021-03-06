package org.wm.oneframework.tests;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wm.oneframework.annotation.ExcelDetails;
import org.wm.oneframework.excelreader.ExcelDataProvider;

@ExcelDetails
public class ReadExcelData {

	@Test(dataProvider = "data")
	public void readExcelData_Test(Map<String, String> input) {
		System.out.println(input.get("Name"));
	}

	@ExcelDetails(excelName = "Book1")
	@Test(dataProvider = "data")
	public void readExcelData_Test2(Map<String, String> input) {
		System.out.println(input.get("Name"));
	}

	@Test(dataProvider = "data")
	public void readExcelData_Test3(Map<String, String> input) {
		System.out.println(input.get("Age"));
	}

	@ExcelDetails(excelName = "testdata", sheetName = "data")
	@Test(dataProvider = "data")
	public void readExcelData_Test4(Map<String, String> input) {
		System.out.println(input.get("Age"));
	}

	@ExcelDetails(excelName = "Book1")
	@Test(dataProvider = "data")
	public void readExcelData_Test5(Map<String, String> input) {
		System.out.println(input.get("EmpID"));
	}

	@Test(dataProvider = "data")
	public void readExcelData_Test6(Map<String, String> input) {
		System.out.println(input.get("EmpID"));
	}

	@DataProvider(name = "data")
	public Object[][] readExcelData(Method method) {
		return new ExcelDataProvider(getClass()).data(method);
	}

}
