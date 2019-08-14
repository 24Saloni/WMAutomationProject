package org.wm.oneframework.utilities;

import org.wm.oneframework.extentreports.ExtentTestManager;

import com.aventstack.extentreports.MediaEntityModelProvider;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;

public class ExtentUtils {

	private ExtentUtils() {
	}

	public static void log(Status status, String details) {
		ExtentTestManager.getTest().log(status, details);
	}

	public static void log(String details) {
		log(Status.INFO, details);
	}

	public static void log(Status status, Markup markup) {
		ExtentTestManager.getTest().log(status, markup);
	}

	public static void log(Markup markup) {
		log(Status.INFO, markup);
	}

	public static void log(Status status, Throwable t) {
		ExtentTestManager.getTest().log(status, t);
	}

	public static void log(Throwable t) {
		log(Status.FAIL, t);
	}

	public static void log(Status status, String details, MediaEntityModelProvider provider) {
		ExtentTestManager.getTest().log(status, details, provider);
	}

	public static void log(String details, MediaEntityModelProvider provider) {
		log(Status.INFO, details, provider);
	}

	public static void log(Status status, Throwable t, MediaEntityModelProvider provider) {
		ExtentTestManager.getTest().log(status, t, provider);
	}

	public static void log(Throwable t, MediaEntityModelProvider provider) {
		log(Status.FAIL, t, provider);
	}
}
