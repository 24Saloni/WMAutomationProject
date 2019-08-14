package org.wm.oneframework.tests;

import java.lang.reflect.Method;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.wm.oneframework.annotation.ExcelDetails;
import org.wm.oneframework.excelreader.ExcelDataProvider;

public class VerifyExcelFunctionality {

	@Test(dataProvider = "data")
	public void verifyExcelFunctionality_Test1(Map<String, String> input) {
		System.out.println(input.get("Name"));
	}

	@ExcelDetails(excelName = "Book1", sheetName = "Sheet2")
	@Test(dataProvider = "data")
	public void verifyExcelFunctionality_Test2(Map<String, String> input) {
		System.out.println(input.get("Name"));
	}

	@DataProvider(name = "data")
	public Object[][] readExcelData(Method method) {
		return new ExcelDataProvider(getClass()).data(method);
	}
}
