package org.wm.oneframewok.seleniumadapter.drivers;

public class DriverManagerFactory {

	private DriverManagerFactory() {
	}

	/**
	 * It will return the instance of DriverManager for mentioned browser. In case of incorrect browser name, it will fallback to IE. Supported browsers are IE, Chrome, Firefox and PhantomJS.
	 * 
	 * @param browserName
	 * @return
	 */
	public static DriverManager getManager(String browserName) {

		DriverManager driverManager;

		if (browserName.equalsIgnoreCase("chrome")) {
			driverManager = new ChromeDriverManager();
		} else if (browserName.equalsIgnoreCase("firefox")) {
			driverManager = new FirefoxDriverManager();
		} else if (browserName.equalsIgnoreCase("phantomjs")) {
			driverManager = new PhantomJSDriverManager();
		} else {
			driverManager = new InternetExplorerDriverManager();
		}
		return driverManager;
	}

}