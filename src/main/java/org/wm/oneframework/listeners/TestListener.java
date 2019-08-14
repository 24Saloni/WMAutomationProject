package org.wm.oneframework.listeners;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.wm.oneframework.extentreports.ExtentConfiguration;
import org.wm.oneframework.extentreports.ExtentTestManager;

import com.aventstack.extentreports.Status;

public class TestListener implements ITestListener {

	private static Logger logger = LogManager.getLogger(TestListener.class.getName());
	private static Map<String, String> allParameters = new HashMap<>();
	private static Map<String, String> suiteParameters = new HashMap<>();
	private static Map<String, String> localParameters = new HashMap<>();

	private static String getTestMethodName(ITestResult iTestResult) {
		return iTestResult.getMethod().getConstructorOrMethod().getName();
	}

	public Map<String, String> getAllParameters() {
		return allParameters;
	}

	public Map<String, String> getSuiteParameters() {
		return suiteParameters;
	}

	public Map<String, String> getLocalParameters() {
		return localParameters;
	}

	@Override
	public void onStart(ITestContext iTestContext) {
		allParameters = iTestContext.getSuite().getXmlSuite().getAllParameters();
		suiteParameters = iTestContext.getSuite().getXmlSuite().getParameters();
		localParameters = iTestContext.getCurrentXmlTest().getLocalParameters();
	}

	@Override
	public void onFinish(ITestContext iTestContext) {
		ExtentConfiguration.getInstance().flush();
		ExtentTestManager.endTest();
	}

	@Override
	public void onTestStart(ITestResult iTestResult) {
		ExtentTestManager.startTest(iTestResult.getMethod().getMethodName(), iTestResult.getMethod().getDescription());
	}

	@Override
	public void onTestSuccess(ITestResult iTestResult) {
		logger.info(iTestResult.getName() + " passed successfully!!");
	}

	@Override
	public void onTestFailure(ITestResult iTestResult) {
		logger.warn(getTestMethodName(iTestResult) + " failed.");
		if (ExtentTestManager.getTest() != null)
			ExtentTestManager.getTest().log(Status.FAIL, iTestResult.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult iTestResult) {
		if (ExtentTestManager.getTest() != null) {
			logger.warn(iTestResult.getName() + " execution got skipped.");
			ExtentTestManager.getTest().log(Status.SKIP, iTestResult.getName() + " execution got skipped.");
		}
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
		logger.info("");
	}

}
