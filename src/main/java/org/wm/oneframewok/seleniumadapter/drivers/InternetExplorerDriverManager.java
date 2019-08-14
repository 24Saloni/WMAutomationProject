package org.wm.oneframewok.seleniumadapter.drivers;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.RemoteWebDriver;

public class InternetExplorerDriverManager extends DriverManager {

	private InternetExplorerDriverService ieDriverService;
	private static Logger logger = LogManager.getLogger(InternetExplorerDriverManager.class.getName());

	@Override
	public void startService() {
		String driverExePath = DriverExecutables.getIeDriverExe();
		if (!isServiceInitialized()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			File file = new File(loader.getResource(driverExePath).getFile());
			ieDriverService = new InternetExplorerDriverService.Builder().usingDriverExecutable(file).usingAnyFreePort().build();
			logger.info("Launching the Internet Explorer browser using " + driverExePath.split("drivers/")[1]);

			try {
				ieDriverService.start();
			} catch (IOException e) {
				logger.warn("IE Service couldn't start!!!");
			}
		}
	}

	private boolean isServiceInitialized() {
		return null != ieDriverService;
	}

	@Override
	public void stopService() {
		if (ieDriverService != null && isServiceInitialized() && ieDriverService.isRunning()) {
			logger.info("Stopping the ie driver service.");
			driver.quit();
			ieDriverService.stop();
			driver = null;
			ieDriverService = null;
		} else {
			logger.info("Stopping the ie driver service.");
			driver.quit();
		}
	}

	@Override
	protected void createDriver() {
		InternetExplorerOptions options = new InternetExplorerOptions();
		options.introduceFlakinessByIgnoringSecurityDomains();
		if (isSeleniumGridRequired()) {
			options.setCapability(CapabilityType.PLATFORM_NAME, getPlatform());
			options.setCapability(CapabilityType.VERSION, "11");
			driver = new RemoteWebDriver(getServerUrl(), options);
		} else
			driver = new InternetExplorerDriver(ieDriverService, options);
	}

}
