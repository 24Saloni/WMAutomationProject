package org.wm.oneframewok.seleniumadapter.drivers;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.GeckoDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class FirefoxDriverManager extends DriverManager {

	private GeckoDriverService geckoDriverService;
	private static Logger logger = LogManager.getLogger(FirefoxDriverManager.class.getName());

	@Override
	protected void startService() {
		String driverExePath = DriverExecutables.getGeckoDriverExe();
		if (!isServiceInitialized()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try {
				File file = new File(loader.getResource(driverExePath).getFile());
				geckoDriverService = new GeckoDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort().build();
				logger.info("Launching the firefox browser using " + driverExePath.split("drivers/")[1]);

			} catch (NullPointerException e) {
				logger.warn("Gecko Driver exe not found. Using default exe file from server.");
				geckoDriverService = GeckoDriverService.createDefaultService();
			}
			try {
				geckoDriverService.start();
			} catch (IOException e) {
				logger.warn("firefox service couldn't start!!!");
			}
		}
	}

	private boolean isServiceInitialized() {
		return null != geckoDriverService;
	}

	@Override
	public void stopService() {
		if (geckoDriverService != null && isServiceInitialized() && geckoDriverService.isRunning()) {
			logger.info("Stopping the gecko driver service.");
			driver.quit();
			geckoDriverService.stop();
			driver = null;
			geckoDriverService = null;
		} else {
			logger.info("Stopping the gecko driver service.");
			driver.quit();
		}

	}

	@Override
	protected void createDriver() {
		FirefoxOptions options = new FirefoxOptions();
		options.setCapability(CapabilityType.PLATFORM_NAME, Platform.WINDOWS);
		if (isSeleniumGridRequired())
			driver = new RemoteWebDriver(getServerUrl(), options);
		else
			driver = new FirefoxDriver(geckoDriverService, options);
	}

}
