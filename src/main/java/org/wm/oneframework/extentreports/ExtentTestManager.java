package org.wm.oneframework.extentreports;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

	private ExtentTestManager() {
	}

	private static ExtentReports extent;
	private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();

	public static synchronized ExtentTest getTest() {
		return extentTest.get();
	}

	public static synchronized void endTest() {
		if (extentTest.get() != null)
			extent.removeTest(extentTest.get());
	}

	public static synchronized ExtentTest startTest(String testName, final String desc) {
		extent = ExtentConfiguration.getInstance();
		extentTest.set(extent.createTest(testName + "_" + Thread.currentThread().getId(), desc));
		return extentTest.get();
	}

}
