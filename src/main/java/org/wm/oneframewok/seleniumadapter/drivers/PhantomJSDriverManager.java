package org.wm.oneframewok.seleniumadapter.drivers;

import java.io.File;
import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class PhantomJSDriverManager extends DriverManager {

	private PhantomJSDriverService phantomJSDriverService;
	private static Logger logger = LogManager.getLogger(PhantomJSDriverManager.class.getName());

	@Override
	public void startService() {
		String driverExePath = DriverExecutables.getPhantomjsExe();
		if (!isServiceInitialized()) {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			File file = new File(loader.getResource(driverExePath).getFile());
			phantomJSDriverService = new PhantomJSDriverService.Builder().usingPhantomJSExecutable(file).usingAnyFreePort().build();
			logger.info("Launching using " + driverExePath.split("drivers/")[1]);
			try {
				phantomJSDriverService.start();
			} catch (IOException e) {
				logger.warn("PhantomJS service couldn't start!!!");
			}
		}
	}

	private boolean isServiceInitialized() {
		return null != phantomJSDriverService;
	}

	@Override
	public void stopService() {
		if (phantomJSDriverService != null && isServiceInitialized() && phantomJSDriverService.isRunning()) {
			logger.info("Stopping the phantomJS driver service.");
			driver.quit();
			phantomJSDriverService.stop();
			driver = null;
			phantomJSDriverService = null;
		} else {
			logger.info("Stopping the phantomJS driver service.");
			driver.quit();
		}
	}

	@Override
	/**
	 * @deprecated(PhantomJS is no longer actively developed, and support will eventually be dropped.)
	 */
	@Deprecated
	protected void createDriver() {
		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		driver = new RemoteWebDriver(getServerUrl(), capabilities);

	}

}
