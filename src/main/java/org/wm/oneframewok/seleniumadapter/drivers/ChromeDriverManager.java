package org.wm.oneframewok.seleniumadapter.drivers;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class ChromeDriverManager extends DriverManager {

	private ChromeDriverService chService;
	private static Logger logger = LogManager.getLogger(ChromeDriverManager.class.getName());

	@Override
	public void startService() {
		String driverExePath = DriverExecutables.getChromeDriverExe();
		if (!isServiceInitialized()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try {
				File file = new File(loader.getResource(driverExePath).getFile());
				chService = new ChromeDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort().build();
				logger.info("Launching the chrome browser using " + driverExePath.split("drivers/")[1]);
			} catch (NullPointerException e) {
				logger.warn("Chrome Driver exe not found. Using default exe file from server to launch chorome browser.");
				chService = ChromeDriverService.createDefaultService();
			}
			try {
				chService.start();
			} catch (IOException e) {
				logger.warn("Chrome service couldn't start!!!");
			}
		}
	}

	private boolean isServiceInitialized() {
		return null != chService;
	}

	@Override
	public void stopService() {
		if (chService != null && isServiceInitialized() && chService.isRunning()) {
			logger.info("Stopping the chrome driver service.");
			driver.quit();
			chService.stop();
			driver = null;
			chService = null;
		} else {
			logger.info("Stopping the chrome driver service.");
			driver.quit();
		}
	}

	@Override
	public void createDriver() {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("test-type");
		options.addArguments("--start-maximized");
		options.addArguments("--disable-extensions");
		options.addArguments("--disable-infobars");
		options.addArguments("--disable-notifications");
		if (isSeleniumGridRequired) {
			options.setCapability(CapabilityType.PLATFORM_NAME, getPlatform());
			driver = new RemoteWebDriver(getServerUrl(), options);
		} else
			driver = new ChromeDriver(chService, options);
	}

}