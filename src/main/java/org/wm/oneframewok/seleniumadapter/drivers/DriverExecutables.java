package org.wm.oneframewok.seleniumadapter.drivers;

import org.wm.oneframework.configprovider.ConfigProvider;

public class DriverExecutables {

	private static String chromeDriverExe = "./drivers/chromedriver_2.%s.exe";
	private static String ieDriverExe = "./drivers/IEDriverServer_%sbit_3.12.exe";
	private static String geckoDriverExe = "./drivers/geckodriver_%sbit_v0.20.1.exe";
	private static String phantomJSexe = "./drivers/phantomjs_2.1.1.exe";
	public static final String TEMP_DIR = System.getProperty("java.io.tmpdir");

	private DriverExecutables() {
	}

	protected static void setChromeDriverExe(int version) {
		chromeDriverExe = String.format(chromeDriverExe, version);
	}

	protected static String getChromeDriverExe() {
		return chromeDriverExe;
	}

	protected static void setIEDriverExe(int version) {
		ieDriverExe = String.format(ieDriverExe, version);
	}

	protected static String getIeDriverExe() {
		return ieDriverExe;
	}

	protected static void setGeckoDriverExe(int version) {
		geckoDriverExe = String.format(geckoDriverExe, version);
	}

	protected static String getGeckoDriverExe() {
		return geckoDriverExe;
	}

	protected static String getPhantomjsExe() {
		return phantomJSexe;
	}

	protected static void setBrowserExe() {

		String jdkVersion = System.getProperty("sun.arch.data.model");
		String browserName = ConfigProvider.getAsString("browser");
		if (browserName.equalsIgnoreCase("chrome")) {
			int browserVersion = ConfigProvider.getAsInt("chrome.version");
			if (browserVersion <= 64) {
				setChromeDriverExe(35);
			} else if (browserVersion <= 66) {
				setChromeDriverExe(37);
			} else if (browserVersion <= 68) {
				setChromeDriverExe(40);
			} else if (browserVersion <= 70) {
				setChromeDriverExe(44);
			} else if (browserVersion >= 71) {
				setChromeDriverExe(45);
			}
		} else if (browserName.equalsIgnoreCase("firefox")) {
			if (jdkVersion.equals("32")) {
				setGeckoDriverExe(32);
			} else {
				setGeckoDriverExe(64);
			}
		} else if (browserName.equalsIgnoreCase("ie") || browserName.equalsIgnoreCase("internetexplorer")) {
			if (jdkVersion.equals("32")) {
				setIEDriverExe(32);
			} else {
				setIEDriverExe(64);
			}
		}
	}

}
