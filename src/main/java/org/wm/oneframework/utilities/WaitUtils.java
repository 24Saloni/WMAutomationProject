package org.wm.oneframework.utilities;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitUtils {

	private WaitUtils() {
	}

	public static void waitForPageToLoad(WebDriver driver) {
		new WebDriverWait(driver, 60).until(new ExpectedCondition<Boolean>() {
			public Boolean apply(WebDriver driver) {
				return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
			}
		});
	}

	public static void sleep(long inMillis) {
		try {
			Thread.sleep(inMillis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

}
