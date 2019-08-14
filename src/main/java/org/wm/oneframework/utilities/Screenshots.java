package org.wm.oneframework.utilities;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wm.oneframework.extentreports.ExtentTestManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;

public class Screenshots {

	private static final Logger logger = LogManager.getLogger(Screenshots.class.getName());
	private static String screenshotsFolderPath;
	private static final String SCREENSHOTS_FOLDER = "\\screenshots\\";

	private Screenshots() {
	}

	static {
		createDirectory();
	}

	protected static String captureScreenshot(WebDriver driver, String screenshotName) {

		String randomNumber = RandomStringUtils.randomNumeric(5);
		String destinationPath = screenshotsFolderPath + screenshotName + randomNumber + ".png";
		TakesScreenshot ts = (TakesScreenshot) driver;
		File srcFile = ts.getScreenshotAs(OutputType.FILE);
		try {
			FileUtils.copyFile(srcFile, new File(destinationPath));
		} catch (IOException e) {
			logger.warn("Failed to capture screenshot...");
		}
		return destinationPath;
	}

	public static String getScreenshotsFolderPath() {
		return screenshotsFolderPath;
	}

	private static void createDirectory() {
		String drive = geDriveWithFreeSpace();
		if (drive != null && drive.contains("C:")) {
			screenshotsFolderPath = FileUtils.getTempDirectoryPath() + SCREENSHOTS_FOLDER;
		} else {
			screenshotsFolderPath = drive + SCREENSHOTS_FOLDER;
		}
		File file = new File(screenshotsFolderPath);
		if (!file.exists() && !file.mkdir()) {
			logger.warn("Failed to create directory...");
		}
	}

	public static void addStepWithScreenshotInReport(WebDriver driver, String message, Status status) {
		ExtentTest extentTest = ExtentTestManager.getTest();
		if (extentTest != null) {
			if (driver != null) {
				String path = Screenshots.captureScreenshot(driver, "screenshot");
				try {
					extentTest.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(path).build());
				} catch (IOException e) {
					logger.warn(e.getMessage());
				}
			} else {
				extentTest.log(status, message);
			}
		}
	}

	public static void addStepWithScreenshotInReport(WebDriver driver, String message) {
		addStepWithScreenshotInReport(driver, message, Status.PASS);
	}

	public static void addStepInReport(String message, Status status) {
		ExtentTest extentTest = ExtentTestManager.getTest();
		if (extentTest != null)
			extentTest.log(status, message);
	}

	public static void addStepInReport(String message) {
		addStepInReport(message, Status.PASS);
	}

	private static String geDriveWithFreeSpace() {
		String driveWithFreeSpace = null;
		File[] availableDrives = File.listRoots();
		if (availableDrives.length > 1) {
			for (File file : availableDrives) {
				if (file.getFreeSpace() > 100000000) {
					driveWithFreeSpace = file.toString();
					break;
				}
			}
		}
		return driveWithFreeSpace;
	}

}
