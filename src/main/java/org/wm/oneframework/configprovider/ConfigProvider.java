package org.wm.oneframework.configprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wm.oneframework.configprovider.exceptions.PropertyFileNotFoundException;

public final class ConfigProvider {

	private static Properties props;
	private static Map<String, Properties> configMap = new HashMap<>();
	private static final String PROPERTIES_EXT = ".properties";
	private static final String PROPERTIES_FOLDER = "./properties";
	private static Logger logger = LogManager.getLogger(ConfigProvider.class.getName());

	private ConfigProvider() {
	}

	/**
	 * static method to get the instance of the ConfigProvider
	 * 
	 * @param propertyFileName
	 * @return ConfigProvider
	 * @throws FileNotFoundException
	 */
	private static Properties getInstance(String propertyFileName) {
		Properties props = null;
		if (configMap.size() == 0) {
			props = loadProperties(propertyFileName);
			configMap.put(propertyFileName, props);
			return props;
		}
		for (Map.Entry<String, Properties> entry : configMap.entrySet()) {
			if (entry.getKey().equals(propertyFileName)) {
				return entry.getValue();
			}
		}
		props = loadProperties(propertyFileName);
		configMap.put(propertyFileName, props);
		return props;
	}

	/**
	 * static method to get the instance of the ConfigProvider
	 * 
	 * @return ConfigProvider
	 * @throws FileNotFoundException
	 */
	private static Properties getInstance() {
		if (props == null) {
			return loadProperties();
		} else {
			return props;
		}
	}

	private static Properties loadProperties() {
		Properties props = new Properties();
		File propertiesFiles = null;
		ClassLoader loader = Thread.currentThread().getContextClassLoader();

		try {
			propertiesFiles = new File(loader.getResource(PROPERTIES_FOLDER).getFile());
		} catch (NullPointerException e) {
			logger.error(e.getMessage());
			throw new PropertyFileNotFoundException("No properties file found inside 'properties' folder under src/test/resources. Please add all your properties files under mentioned folder(create folder if doesn't exist).");
		}
		try {
			File[] listOfFiles = propertiesFiles.listFiles();
			for (File file : listOfFiles) {
				if (file.isFile() && file.getName().endsWith(PROPERTIES_EXT)) {
					props.load(new FileInputStream(propertiesFiles + File.separator + file.getName()));
				}
			}
		} catch (IOException e) {
			logger.warn("Not able to load property!!");
		}
		return props;
	}

	private static Properties loadProperties(String propertyFile) {
		Properties props = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream is = loader.getResourceAsStream(PROPERTIES_FOLDER + File.separator + propertyFile + PROPERTIES_EXT);
		try {
			props.load(is);
		} catch (NullPointerException e) {
			logger.error(e.getMessage());
			throw new PropertyFileNotFoundException("'" + propertyFile + ".properties' file not found. Please verify mentioned file should be present under 'properties' folder((create folder if doesn't exist)) in src/test/resources.");
		} catch (IOException e) {
			logger.warn("Not able to load property!!");
		}
		return props;
	}

	/**
	 * This method will load all the properties file from properties folder in src/test/resources. It takes key as the parameter and will return its value, if found. Otherwise it will return null. Don't keep same keys in different properties file. In that scenario, use {@link getAsString(String fileName, String key)}
	 * 
	 * @param key
	 * @return value
	 */
	public static String getAsString(String key) {
		String value = getInstance().getProperty(key);
		logger.debug("Reading configuration from properties file. " + key + ":" + value);
		return value;
	}

	/**
	 * It will return value in int.
	 * 
	 * @param key
	 * @return int
	 */
	public static int getAsInt(String key) {
		String value = getInstance().getProperty(key);
		if (value != null) {
			int intValue = Integer.parseInt(value);
			logger.debug("Reading configuration from properties file. " + key + ":" + value);
			return intValue;
		} else {
			return 0;
		}
	}

	/**
	 * This method will load the specific property file from properties folder in src/test/resources. It takes first parameter as properetyFileName(don't put .properties extension) and second parameter is the key. It will return its value, if found. Otherwise it will return null.
	 * 
	 * @param key
	 * @return value
	 */
	public static String getAsString(String fileName, String key) {
		String value = getInstance(fileName).getProperty(key);
		logger.debug("Reading configuration from properties file[" + fileName + "]. " + key + ":" + value);
		return value;
	}

	/**
	 * It will return value in int.
	 * 
	 * @param key
	 * @return int
	 */
	public static int getAsInt(String fileName, String key) {
		String value = getInstance(fileName).getProperty(key);
		if (value != null) {
			logger.debug("Reading configuration from properties file[" + fileName + "]. " + key + ":" + value);
			return Integer.parseInt(value);
		} else {
			return 0;
		}
	}

	public static String getAsString(String environment, String propertyFile, String key) {
		Properties props = new Properties();
		ClassLoader loader = Thread.currentThread().getContextClassLoader();
		InputStream inputStream = loader.getResourceAsStream(PROPERTIES_FOLDER + File.separator + environment + File.separator + propertyFile + PROPERTIES_EXT);
		String value = null;
		try {
			props.load(inputStream);
			value = props.getProperty(key);
			logger.debug("Reading configuration from properties file[" + environment + " - " + propertyFile + "]. " + key + ":" + value);
			props.clear();
			inputStream.close();
		} catch (IOException e) {
			logger.error("an exception was thrown", e.getMessage());
		}
		return value;
	}

	public static void setProperty(String key, String value) {
		getInstance().setProperty(key, value);
	}

}
