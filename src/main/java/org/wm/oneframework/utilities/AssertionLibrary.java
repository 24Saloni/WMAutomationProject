package org.wm.oneframework.utilities;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

import com.aventstack.extentreports.Status;

public class AssertionLibrary {

	private static WebDriver driver;
	private static final String ACTUAL = "<br> <b>Actual: </b>";
	private static final String EXPECTED = "<br> <b>Expected: </b>";
	private static final String LOG_ACTUAL = " Actual:[";
	private static final String LOG_EXPECTED = "] Expected:[";
	private static Logger logger = LogManager.getLogger(AssertionLibrary.class.getName());

	public AssertionLibrary(WebDriver driver) {
		AssertionLibrary.driver = driver;
	}

	public enum Screenshot {
		REQUIRED, NOT_REQUIRED;
	}

	/**
	 * It will work as TestNG assertEquals assertion.
	 * 
	 * @see Assert.assertEquals(actual, expected, message);
	 * @param actual
	 * @param expected
	 * @param message
	 */
	public static void assertEquals(String actual, String expected, String message, Screenshot screenshot) {
		AssertionLibrary.assertEquals((Object) actual, (Object) expected, message, screenshot);
	}

	public static void verifyEquals(String actual, String expected, String message, Screenshot screenshot) {
		try {
			AssertionLibrary.assertEquals(actual, expected, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertEquals(String actual, String expected, String message) {
		AssertionLibrary.assertEquals((Object) actual, (Object) expected, message, Screenshot.REQUIRED);
	}

	public static void verifyEquals(String actual, String expected, String message) {
		verifyEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	public static void assertEquals(Double actual, Double expected, Double delta, String message, Screenshot screenshot) {
		String reportMessage = message + ACTUAL + actual.toString() + EXPECTED + expected.toString();
		Assert.assertEquals(actual, expected, delta, message);
		String logMessage = message + LOG_ACTUAL + actual.toString() + LOG_EXPECTED + expected.toString() + "]";
		logger.info("Assertion: " + logMessage);
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyEquals(Double actual, Double expected, Double delta, String message, Screenshot screenshot) {
		try {
			assertEquals(actual, expected, delta, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertEquals(Double actual, Double expected, Double delta, String message) {
		assertEquals(actual, expected, delta, message, Screenshot.REQUIRED);
	}

	public static void verifyEquals(Double actual, Double expected, Double delta, String message) {
		verifyEquals(actual, expected, delta, message, Screenshot.REQUIRED);
	}

	public static void assertEquals(Object actual, Object expected, String message, Screenshot screenshot) {
		String reportMessage = message + ACTUAL + actual.toString() + EXPECTED + expected.toString();
		Assert.assertEquals(actual, expected, message);
		String logMessage = message + LOG_ACTUAL + actual.toString() + LOG_EXPECTED + expected.toString() + "]";
		logger.info("Assertion: " + logMessage);
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyEquals(Object actual, Object expected, String message, Screenshot screenshot) {
		try {
			assertEquals(actual, expected, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertEquals(Object actual, Object expected, String message) {
		assertEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	public static void verifyEquals(Object actual, Object expected, String message) {
		verifyEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	/**
	 * It will work as TestNG assertTrue assertion.
	 * 
	 * @see Assert.assertTrue(condition, message);
	 * @param actual
	 * @param expected
	 * @param message
	 */
	public static void assertTrue(boolean condition, String message, Screenshot screenshot) {
		String reportMessage = message + "<br> <b>Condition: </b>" + condition;
		Assert.assertTrue(condition, message);
		String logMessage = message + " [true]";
		logger.info("Assertion: " + logMessage);
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyTrue(boolean condition, String message, Screenshot screenshot) {
		try {
			assertTrue(condition, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertTrue(boolean condition, String message) {
		assertTrue(condition, message, Screenshot.REQUIRED);
	}

	public static void verifyTrue(boolean condition, String message) {
		verifyTrue(condition, message, Screenshot.REQUIRED);
	}

	/**
	 * It will work as TestNG assertNotEquals assertion.
	 * 
	 * @see Assert.assertNotEquals(actual, expected, message);
	 * @param actual
	 * @param expected
	 * @param message
	 */
	public static void assertNotEquals(String actual1, String actual2, String message, Screenshot screenshot) {
		assertNotEquals((Object) actual1, (Object) actual2, message, screenshot);
	}

	public static void verifyNotEquals(String actual1, String actual2, String message, Screenshot screenshot) {
		try {
			assertNotEquals((Object) actual1, (Object) actual2, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertNotEquals(String actual1, String actual2, String message) {
		assertNotEquals(actual1, actual2, message, Screenshot.REQUIRED);
	}

	public static void verifyNotEquals(String actual1, String actual2, String message) {
		verifyNotEquals(actual1, actual2, message, Screenshot.REQUIRED);
	}

	public static void assertNotEquals(Double actual1, Double actual2, Double delta, String message, Screenshot screenshot) {
		assertNotEquals(actual1, actual2, message, screenshot);
	}

	public static void verifyNotEquals(Double actual1, Double actual2, Double delta, String message, Screenshot screenshot) {
		try {
			assertNotEquals(actual1, actual2, delta, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertNotEquals(Double actual1, Double actual2, Double delta, String message) {
		assertNotEquals(actual1, actual2, delta, message, Screenshot.REQUIRED);
	}

	public static void verifyNotEquals(Double actual1, Double actual2, Double delta, String message) {
		verifyNotEquals(actual1, actual2, delta, message, Screenshot.REQUIRED);
	}

	public static void assertNotEquals(Object actual1, Object actual2, String message, Screenshot screenshot) {
		String reportMessage = message + "<br> <b>Actual 1</b>" + actual1.toString() + "<br><b>Actual 2</b>" + actual2.toString();
		Assert.assertNotEquals(actual1, actual2, message);
		String logMessage = message + " Actual1[" + actual1.toString() + "] Actual2[" + actual2.toString() + "]";
		logger.info("Assertion[Not Equals]: " + logMessage);
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyNotEquals(Object actual1, Object actual2, String message, Screenshot screenshot) {
		try {
			assertNotEquals(actual1, actual2, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertNotEquals(Object actual1, Object actual2, String message) {
		assertNotEquals(actual1, actual2, message, Screenshot.REQUIRED);
	}

	public static void verifyNotEquals(Object actual1, Object actual2, String message) {
		verifyNotEquals(actual1, actual2, message, Screenshot.REQUIRED);
	}

	/**
	 * It will work as TestNG assertFalse assertion.
	 * 
	 * @see Assert.assertFalse(condition, message);
	 * @param actual
	 * @param expected
	 * @param message
	 */
	public static void assertFalse(boolean condition, String message, Screenshot screenshot) {
		String reportMessage = message + "<br> <b>Condition: </b>" + condition;
		Assert.assertFalse(condition, message);
		logger.info("Assertion: " + reportMessage + " [false]");
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyFalse(boolean condition, String message, Screenshot screenshot) {
		try {
			assertFalse(condition, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertFalse(boolean condition, String message) {
		assertFalse(condition, message, Screenshot.REQUIRED);
	}

	public static void verifyFalse(boolean condition, String message) {
		verifyFalse(condition, message, Screenshot.REQUIRED);
	}

	public static void assertEquals(Set<?> actual, Set<?> expected, String message, Screenshot screenshot) {
		String reportMessage = message + ACTUAL + actual.toString() + EXPECTED + expected.toString();
		Assert.assertEqualsDeep(actual, expected, message);
		String logMessage = message + LOG_ACTUAL + actual.toString() + LOG_EXPECTED + expected.toString() + "]";
		logger.info("Assertion: " + logMessage);
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyEquals(Set<?> actual, Set<?> expected, String message, Screenshot screenshot) {
		try {
			assertEquals(actual, expected, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertEquals(Set<?> actual, Set<?> expected, String message) {
		assertEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	public static void verifyEquals(Set<?> actual, Set<?> expected, String message) {
		verifyEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	public static void assertEquals(Map<?, ?> actual, Map<?, ?> expected, String message, Screenshot screenshot) {
		String reportMessage = message + ACTUAL + actual.toString() + EXPECTED + expected.toString();
		Assert.assertEqualsDeep(actual, expected, message);
		String logMessage = message + LOG_ACTUAL + actual.toString() + LOG_EXPECTED + expected.toString() + "]";
		logger.info("Assertion: " + logMessage);
		attachScreenshotIfRequired(screenshot, reportMessage);
	}

	public static void verifyEquals(Map<?, ?> actual, Map<?, ?> expected, String message, Screenshot screenshot) {
		try {
			assertEquals(actual, expected, message, screenshot);
		} catch (Throwable t) {
			softAssertionFailureLog(screenshot, t);
		}
	}

	public static void assertEquals(Map<?, ?> actual, Map<?, ?> expected, String message) {
		assertEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	public static void verifyEquals(Map<?, ?> actual, Map<?, ?> expected, String message) {
		verifyEquals(actual, expected, message, Screenshot.REQUIRED);
	}

	public static void fail(String message, Throwable t) {
		Assert.fail(message, t);
	}

	public static void fail(String message) {
		Assert.fail(message);
	}

	public static void fail() {
		Assert.fail();
	}

	private static void attachScreenshotIfRequired(Screenshot screenshot, String message, Status status) {
		if (screenshot.equals(Screenshot.REQUIRED)) {
			Screenshots.addStepWithScreenshotInReport(driver, message, status);
		} else {
			Screenshots.addStepInReport(message, status);
		}
	}

	private static void attachScreenshotIfRequired(Screenshot screenshot, String message) {
		attachScreenshotIfRequired(screenshot, message, Status.PASS);
	}

	private static void softAssertionFailureLog(Screenshot screenshot, Throwable t) {
		logger.warn("Verification Failure: " + t.getMessage());
		attachScreenshotIfRequired(screenshot, "<b>Verification Failure: </b>" + t.getMessage(), Status.WARNING);

	}

}
