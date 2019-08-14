package org.wm.oneframewok.seleniumadapter.drivers;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.wm.oneframework.configprovider.ConfigProvider;

public abstract class DriverManager {

	protected WebDriver driver;
	private static Logger logger = LogManager.getLogger(ChromeDriverManager.class.getName());
	boolean isSeleniumGridRequired = isSeleniumGridRequired();

	protected abstract void startService();

	protected abstract void createDriver();

	public void stopService() {
	}

	public WebDriver getDriver() {
		if (null == driver) {
			if (!isSeleniumGridRequired) {
				DriverExecutables.setBrowserExe();
				startService();
			}
			createDriver();
		}
		return driver;
	}

	boolean isSeleniumGridRequired() {
		String value = ConfigProvider.getAsString("SeleniumGrid");
		return Boolean.valueOf(value);
	}

	Platform getPlatform() {
		String platformValue = ConfigProvider.getAsString("platform");
		if (platformValue.equalsIgnoreCase("Windows7") || platformValue.equalsIgnoreCase("windows") || platformValue.equalsIgnoreCase("7")) {
			return Platform.WINDOWS;
		} else if (platformValue.equalsIgnoreCase("windows8") || platformValue.equalsIgnoreCase("8")) {
			return Platform.WIN8;
		} else if (platformValue.equalsIgnoreCase("windows8.1") || platformValue.equalsIgnoreCase("8.1")) {
			return Platform.WIN8_1;
		} else if (platformValue.equalsIgnoreCase("windows10") || platformValue.equalsIgnoreCase("10")) {
			return Platform.WIN10;
		} else if (platformValue.equalsIgnoreCase("windowsXP") || platformValue.equalsIgnoreCase("xp")) {
			return Platform.XP;
		} else if (platformValue.equalsIgnoreCase("mac")) {
			return Platform.MAC;
		} else if (platformValue.equalsIgnoreCase("vista")) {
			return Platform.VISTA;
		} else if (platformValue.equalsIgnoreCase("linux")) {
			return Platform.LINUX;
		} else if (platformValue.equalsIgnoreCase("unix")) {
			return Platform.UNIX;
		} else {
			return Platform.ANY;
		}
	}

	URL getServerUrl() {
		URL url = null;
		String urlString = "";
		try {
			urlString = ConfigProvider.getAsString("hub_url").trim();
			url = toURL(urlString);
		} catch (NullPointerException e) {
			logger.warn("hub_url property is not defned.");
		}
		if (urlString.isEmpty()) {
			logger.warn("hub_url value is not defned.");
		}
		return url;
	}

	private URL toURL(String urlString) {
		URL url = null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			logger.warn("url may not be correct: " + url);
		}
		return url;
	}

}