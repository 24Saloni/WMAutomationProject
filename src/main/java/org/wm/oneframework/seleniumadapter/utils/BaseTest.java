package org.wm.oneframework.seleniumadapter.utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wm.oneframewok.seleniumadapter.drivers.DriverManager;
import org.wm.oneframewok.seleniumadapter.drivers.DriverManagerFactory;
import org.wm.oneframework.configprovider.ConfigProvider;
import org.wm.oneframework.excelreader.ExcelDataProvider;
import org.wm.oneframework.utilities.Screenshots;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

@Listeners(org.wm.oneframework.listeners.TestListener.class)
public class BaseTest {

	private static ThreadLocal<DriverManager> driverManager = new ThreadLocal<>();
	private static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
	public static Logger logger = LogManager.getLogger(BaseTest.class.getName());
	private String browserName = ConfigProvider.getAsString("browser");

	public void folderCleanup() throws IOException {
		File file = new File(Screenshots.getScreenshotsFolderPath());
		if (file.exists())
			FileUtils.cleanDirectory(file);
	}

	@DataProvider(name = "data")
	public Object[][] readExcelData(Method method) {
		logger.debug("Reading data from excel.");
		return new ExcelDataProvider(getClass()).data(method);
	}

	public WebDriver getDriver() {
		if (driverManager.get() == null)
			driverManager.set(DriverManagerFactory.getManager(browserName));
		driver.set(driverManager.get().getDriver());
		driver.get().manage().timeouts().implicitlyWait(ConfigProvider.getAsInt("ImplicitWait"), TimeUnit.SECONDS);
		if (!browserName.equalsIgnoreCase("chrome"))
			driver.get().manage().window().maximize();
		return driver.get();
	}

	@AfterSuite(alwaysRun = true)
	public void stopDriverService() {
		driverManager.get().stopService();
	}

	public void launchApplication(String url) {
		getDriver().get(url);
		Screenshots.addStepWithScreenshotInReport(driver.get(), "Application launched: <a href=\"" + url + "\">" + url + "</a>");
	}

}
