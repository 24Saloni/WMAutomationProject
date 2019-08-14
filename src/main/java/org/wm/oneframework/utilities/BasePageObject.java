package org.wm.oneframework.utilities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.wm.oneframework.configprovider.ConfigProvider;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.ElementNotSelectableException;
import org.openqa.selenium.ElementNotVisibleException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.remote.LocalFileDetector;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

public class BasePageObject {

	private final JavascriptExecutor javascriptExecutor;
	private Wait<WebDriver> wait;
	private WebDriver driver;
	private static final String SET_INPUT = "Set input: ";
	private static final String CLICK_ACTION = "Click Action";
	private static final String SET_INPUT_COMMAND = "arguments[0].value='%s';";
	private static final String CLICK_COMMAND = "arguments[0].click();";
	private static final String JS_DISPLAY_COMMAND = "arguments[0].style.display='block';";
	private static final String CLICK = "Click: ";
	private static Logger logger = LogManager.getLogger(BasePageObject.class.getName());
	private List<Class<? extends Throwable>> ignoredExceptions = new ArrayList<>();
	private int timeoutDuration = ConfigProvider.getAsInt("TimeOutDuration");

	public BasePageObject(WebDriver webDriver) {
		this.driver = webDriver;
		wait = new WebDriverWait(webDriver, timeoutDuration, ConfigProvider.getAsInt("PollingInterval")).ignoreAll(addIgnoredExceptions());
		javascriptExecutor = (JavascriptExecutor) driver;
		PageFactory.initElements(driver, this);
		new AssertionLibrary(webDriver);
	}

	private List<Class<? extends Throwable>> addIgnoredExceptions() {
		ignoredExceptions.add(StaleElementReferenceException.class);
		ignoredExceptions.add(NoSuchElementException.class);
		ignoredExceptions.add(ElementNotSelectableException.class);
		ignoredExceptions.add(ElementNotVisibleException.class);
		ignoredExceptions.add(ElementNotInteractableException.class);
		return ignoredExceptions;
	}

	public void get(String url) {
		driver.get(url);
		logger.info("Launch Url: " + url);
		Screenshots.addStepWithScreenshotInReport(driver, "Application launched: <a href=\"" + url + "\">" + url + "</a>");
	}

	public void navigateTo(String url) {
		driver.navigate().to(url);
		logger.info("Navigate to Url: " + url);
		Screenshots.addStepWithScreenshotInReport(driver, "Application launched: <a href=\"" + url + "\">" + url + "</a>");
	}

	protected void setImplicitWait(int duration) {
		logger.info("Impicit wait(sec): " + duration);
		driver.manage().timeouts().implicitlyWait(duration, TimeUnit.SECONDS);
	}

	/**
	 * returns the first instance of webElement
	 * 
	 * @param locator
	 * @return WebElement
	 */
	protected WebElement getElement(final By by) {
		return wait.until(new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				return driver.findElement(by);
			}
		});
	}

	/**
	 * returns the first instance of webElement
	 * 
	 * @param locator
	 * @return WebElement
	 */
	protected WebElement getElement(final String locator) {
		return getElement(By.xpath(locator));
	}

	/**
	 * returns list of webElements.
	 * 
	 * @param locator
	 * @return List<WebElement>
	 */
	protected List<WebElement> getElements(final By by) {
		return wait.until(new ExpectedCondition<List<WebElement>>() {
			@Override
			public List<WebElement> apply(WebDriver driver) {
				return driver.findElements(by);
			}
		});
	}

	/**
	 * returns list of webElements.
	 * 
	 * @param locator
	 * @return List<WebElement>
	 */
	protected List<WebElement> getElements(final String locator) {
		return getElements(By.xpath(locator));
	}

	/**
	 * this method checks if the element is present on page.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isElementOnPage(final String locator) {
		return isElementOnPage(By.xpath(locator));
	}

	/**
	 * this method checks if the element is present on page.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isElementOnPage(final By by) {
		return !getElements(by).isEmpty();
	}

	/**
	 * returns true, if element is enabled.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isEnabled(final String locator) {
		return isEnabled(By.xpath(locator));
	}

	/**
	 * returns true, if element is enabled.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isEnabled(final By by) {
		List<WebElement> elementList = getElements(by);
		if (!elementList.isEmpty()) {
			return elementList.get(0).isEnabled();
		} else {
			return false;
		}
	}

	/**
	 * returns true, if element is displayed.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isDisplayed(final String locator) {
		return isDisplayed(By.xpath(locator));
	}

	/**
	 * returns true, if element is displayed.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isDisplayed(final By by) {
		List<WebElement> elementList = getElements(by);
		if (!elementList.isEmpty()) {
			return elementList.get(0).isDisplayed();
		} else {
			return false;
		}
	}

	/**
	 * returns true, if element is selected.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isSelected(final String locator) {
		return isSelected(By.xpath(locator));
	}

	/**
	 * returns true, if element is selected.
	 * 
	 * @param locator
	 * @return boolean
	 */
	protected boolean isSelected(final By by) {
		List<WebElement> elementList = getElements(by);
		if (!elementList.isEmpty()) {
			return elementList.get(0).isSelected();
		} else {
			return false;
		}
	}

	/**
	 * returns the number of instances of the element.
	 * 
	 * @param locator
	 * @return size
	 */
	protected int getElementsSize(final String locator) {
		return getElementsSize(By.xpath(locator));
	}

	/**
	 * returns the number of instances of the element.
	 * 
	 * @param locator
	 * @return size
	 */
	protected int getElementsSize(final By by) {
		if (isElementOnPage(by)) {
			int size = getElements(by).size();
			logger.info("size: " + size);
			return size;
		} else {
			return 0;
		}
	}

	/**
	 * This method sets input value using sendkeys function of selenium. Also provides the feature of clean before setting the value.
	 * 
	 * @param locator
	 * @param clearInput
	 */
	protected void setInputValue(final String locator, final CharSequence value, final boolean clearInput) {
		setInputValue(By.xpath(locator), value, clearInput);
	}

	/**
	 * This method sets input value using sendkeys function of selenium. Also provides the feature of clean before setting the value.
	 * 
	 * @param locator
	 * @param clearInput
	 */
	protected void setInputValue(final By by, final CharSequence value, final boolean clearInput) {
		WebElement element = getElement(by);
		if (clearInput) {
			element.clear();
		}
		element.sendKeys(value);
		logger.info("Set input: " + value);
		Screenshots.addStepWithScreenshotInReport(driver, SET_INPUT + value);
	}

	/**
	 * This method first clears and then sets input value using sendkeys function of selenium.
	 * 
	 * @param locator
	 * @param clearInput
	 */
	protected void setInputValue(final String locator, final CharSequence value) {
		setInputValue(By.xpath(locator), value);
	}

	/**
	 * This method first clears and then sets input value using sendkeys function of selenium.
	 * 
	 * @param locator
	 * @param clearInput
	 */
	protected void setInputValue(final By by, final CharSequence value) {
		WebElement element = getElement(by);
		element.clear();
		if (value == null) {
			logger.warn("Value trying to be passed in setInputValue() method is null. It will result in exception which is org.openqa.selenium.WebDriverException: unknown error: keys should be a string");
		}
		element.sendKeys(value);
		logger.info("Set input: " + value);
		Screenshots.addStepWithScreenshotInReport(driver, SET_INPUT + value);
	}

	/**
	 * This method sets input value using javascript Executor. Also provides the feature of clean before setting the value.
	 * 
	 * @param locator
	 * @param value
	 * @param clearInput
	 */
	protected void setInputValueJS(final String locator, final String value, final boolean clearInput) {
		setInputValueJS(By.xpath(locator), value, clearInput);
	}

	/**
	 * This method sets input value using javascript Executor. Also provides the feature of clean before setting the value.
	 * 
	 * @param locator
	 * @param value
	 * @param clearInput
	 */
	protected void setInputValueJS(final By by, final String value, final boolean clearInput) {
		WebElement element = getElement(by);
		if (clearInput) {
			element.clear();
		}
		javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND, value), element);
		logger.info("Set input: " + value);
		Screenshots.addStepWithScreenshotInReport(driver, SET_INPUT + value);
	}

	/**
	 * This method sets input value using javascript Executor.
	 * 
	 * @param locator
	 * @param value
	 * @param clearInput
	 */
	protected void setInputValueJS(final String locator, final String value) {
		setInputValueJS(By.xpath(locator), value);
	}

	/**
	 * This method sets input value using javascript Executor.
	 * 
	 * @param locator
	 * @param value
	 * @param clearInput
	 */
	protected void setInputValueJS(final By by, final String value) {
		WebElement element = getElement(by);
		element.clear();
		javascriptExecutor.executeScript(String.format(SET_INPUT_COMMAND, value), element);
		logger.info("Set input: " + value);
		Screenshots.addStepWithScreenshotInReport(driver, SET_INPUT + value);
	}

	/**
	 * This method clears the input field value.
	 * 
	 * @param locator
	 */
	protected void clearElement(final String locator) {
		clearElement(By.xpath(locator));
	}

	/**
	 * This method clears the input field value.
	 * 
	 * @param locator
	 */
	protected void clearElement(final By by) {
		getElement(by).clear();
	}

	/**
	 * This method returns the text.
	 * 
	 * @param locator
	 * @return String
	 */
	protected String getText(final String locator) {
		return getText(By.xpath(locator));
	}

	/**
	 * This method returns the text.
	 * 
	 * @param locator
	 * @return String
	 */
	protected String getText(final By by) {
		String text = getElement(by).getText();
		logger.info("Get text: " + text);
		return text;
	}

	/**
	 * This method returns the value of mentioned attribute.
	 * 
	 * @param locator
	 * @param attribute
	 * @return String
	 */
	protected String getAttribute(final String locator, final String attribute) {
		return getAttribute(By.xpath(locator), attribute);

	}

	/**
	 * This method returns the value of mentioned attribute.
	 * 
	 * @param locator
	 * @param attribute
	 * @return String
	 */
	protected String getAttribute(final By by, final String attribute) {
		String attr = getElement(by).getAttribute(attribute);
		logger.info("Get Attribute: " + attr);
		return attr;
	}

	/**
	 * This method returns the css value of mentioned field.
	 * 
	 * @param locator
	 * @param attribute
	 * @return String
	 */
	protected String getCssValue(final String locator, final String attribute) {
		return getCssValue(By.xpath(locator), attribute);
	}

	/**
	 * This method returns the css value of mentioned field.
	 * 
	 * @param locator
	 * @param attribute
	 * @return String
	 */
	protected String getCssValue(final By by, final String attribute) {
		String val = getElement(by).getCssValue(attribute);
		logger.info("CSS value: " + val);
		return val;
	}

	/**
	 * This method clicks using javascript executor.
	 * 
	 * @param locator
	 */
	protected void clickElementJS(final String locator) {
		clickElementJS(By.xpath(locator));
	}

	/**
	 * This method clicks using javascript executor.
	 * 
	 * @param locator
	 */
	protected void clickElementJS(final By by) {
		javascriptExecutor.executeScript(CLICK_COMMAND, getElement(by));
		logger.info(CLICK);
		Screenshots.addStepWithScreenshotInReport(driver, CLICK_ACTION);
	}

	/**
	 * This method clicks using javascript executor. Add description to display in the report.
	 * 
	 * @param locator
	 */
	protected void clickElementJS(final String locator, final String description) {
		clickElementJS(By.xpath(locator), description);
	}

	/**
	 * This method clicks using javascript executor. Add description to display in the report.
	 * 
	 * @param locator
	 */
	protected void clickElementJS(final By by, final String description) {
		javascriptExecutor.executeScript(CLICK_COMMAND, getElement(by));
		logger.info(CLICK + description);
		Screenshots.addStepWithScreenshotInReport(driver, CLICK + description);
	}

	/**
	 * This method first makes the element visible and then perform click action using javascript.
	 * 
	 * @param locator
	 */
	protected void makeElementVisibleAndClick(final String locator) {
		makeElementVisibleAndClick(By.xpath(locator));
	}

	/**
	 * This method first makes the element visible and then perform click action using javascript.
	 * 
	 * @param locator
	 */
	protected void makeElementVisibleAndClick(final By by) {
		WebElement element = getElement(by);
		javascriptExecutor.executeScript(JS_DISPLAY_COMMAND, element);
		javascriptExecutor.executeScript(CLICK_COMMAND, element);
		logger.info(CLICK);
		Screenshots.addStepWithScreenshotInReport(driver, CLICK_ACTION);
	}

	/**
	 * This method first makes the element visible and then perform click action using javascript. Add description to display in the report.
	 * 
	 * @param locator
	 */
	protected void makeElementVisibleAndClick(final String locator, final String description) {
		makeElementVisibleAndClick(By.xpath(locator), description);
	}

	/**
	 * This method first makes the element visible and then perform click action using javascript. Add description to display in the report.
	 * 
	 * @param locator
	 */
	protected void makeElementVisibleAndClick(final By by, final String description) {
		WebElement element = getElement(by);
		javascriptExecutor.executeScript(JS_DISPLAY_COMMAND, element);
		javascriptExecutor.executeScript(CLICK_COMMAND, element);
		logger.info(CLICK + description);
		Screenshots.addStepWithScreenshotInReport(driver, CLICK + description);
	}

	/**
	 * This method performs the normal click operation of Selenium.
	 * 
	 * @param locator
	 */
	protected void clickElement(final String locator) {
		clickElement(By.xpath(locator));
	}

	/**
	 * This method performs the normal click operation of Selenium.
	 * 
	 * @param locator
	 */
	protected void clickElement(final By by) {
		getElement(by).click();
		logger.info(CLICK);
		Screenshots.addStepWithScreenshotInReport(driver, CLICK_ACTION);
	}

	/**
	 * This method performs the normal click operation of Selenium. Add description to display in the report.
	 * 
	 * @param locator
	 */
	protected void clickElement(final String locator, String description) {
		clickElement(By.xpath(locator), description);
	}

	/**
	 * This method performs the normal click operation of Selenium. Add description to display in the report.
	 * 
	 * @param locator
	 */
	protected void clickElement(final By by, String description) {
		getElement(by).click();
		logger.info(CLICK + description);
		Screenshots.addStepWithScreenshotInReport(driver, CLICK + description);
	}

	/**
	 * This method shifts the focus away from the current element.
	 * 
	 * @param locator
	 */
	protected void shiftFocusAway(final By by) {
		getElement(by).sendKeys(Keys.TAB);
	}

	/**
	 * This method shifts the focus away from the current element.
	 * 
	 * @param locator
	 */
	protected void shiftFocusAway(final String locator) {
		shiftFocusAway(By.xpath(locator));
	}

	protected String getPageSource() {
		return driver.getPageSource();
	}

	/**
	 * This method uploads the file to the location mentioned in the filePath.
	 * 
	 * @param by
	 * @param filePath
	 */
	protected void uploadFile(final By by, String filePath) {
		File file = new File(filePath);
		if (WebDriver.class.isAssignableFrom(driver.getClass())) {
			setInputValue(by, file.getAbsolutePath());
			logger.info("File upload successful: " + filePath);
		} else if (RemoteWebDriver.class.isAssignableFrom(((WrapsDriver) driver).getWrappedDriver().getClass())) {
			((RemoteWebDriver) ((WrapsDriver) driver).getWrappedDriver()).setFileDetector(new LocalFileDetector());
			setInputValue(by, file.getAbsolutePath());
			logger.info("File upload successful: " + filePath);
		}
	}

	protected void uploadFile(final String locator, String filePath) {
		uploadFile(By.xpath(locator), filePath);
	}

	protected void highlightElement(final By by) {
		WebElement element = getElement(by);
		for (int i = 0; i < 5; i++) {
			javascriptExecutor.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "color: green; border: 5px solid blue;");
			WaitUtils.sleep(100);
			javascriptExecutor.executeScript("arguments[0].setAttribute('style',arguments[1]);", element, "");
		}
	}

	protected void highlightElement(final String locator) {
		highlightElement(By.xpath(locator));
	}

	private Alert getAlert() {
		return wait.until(ExpectedConditions.alertIsPresent());
	}

	protected boolean isAlertPresent() {
		Alert alert = getAlert();
		if (alert != null) {
			logger.info("Alert Box present.");
			return true;
		} else {
			return false;
		}
	}

	protected void acceptAlertBox() {
		Alert alert = getAlert();
		if (alert != null) {
			alert.accept();
			logger.info("Alert box accepted.");
		} else {
			logger.warn("Alert box is not present!!!");
		}
	}

	protected String getAlertBoxText() {
		Alert alert = getAlert();
		if (alert != null) {
			String text = alert.getText();
			logger.info("Alert box text: " + text);
			return text;
		} else {
			logger.warn("Alert box is not present!!!");
			return null;
		}
	}

	protected void setValueInAlertBox(String valueToSet) {
		Alert alert = getAlert();
		if (alert != null) {
			alert.sendKeys(valueToSet);
			logger.info("Set value in Alert box: " + valueToSet);
		} else {
			logger.warn("Alert box is not present!!!");
		}
	}

	protected void waitForElementToDisappear(final By by) {
		waitForElementToDisappear(by, timeoutDuration);
	}

	protected void waitForElementToDisappear(final By by, int timeoutDuration) {
		int counter = 0;
		do {
			WaitUtils.sleep(1000);
			counter++;
		} while (isElementOnPage(by) && counter < timeoutDuration);
		if (counter == timeoutDuration) {
			throw new TimeoutException("Element is still present after " + timeoutDuration + " seconds.");
		}
	}

	protected void waitForElementToDisappear(final String locator) {
		logger.info("Waiting for element to disappear. Timeout duration: " + timeoutDuration);
		waitForElementToDisappear(By.xpath(locator));
	}

	protected void waitForElementToDisappear(final String locator, int timeoutDuration) {
		logger.info("Waiting for element to disappear. Timeout duration: " + timeoutDuration);
		waitForElementToDisappear(By.xpath(locator), timeoutDuration);
	}

	protected WebElement waitForElementToLoad(final By by) {
		logger.info("Waiting for element to load");
		return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
	}

	protected WebElement waitForElementToLoad(final String locator) {
		return waitForElementToLoad(By.xpath(locator));
	}

	protected void selectFromDropdownByVisibleText(final By by, String text) {
		Select select = new Select(getElement(by));
		select.selectByVisibleText(text);
		logger.info("Select value from dropdown: " + text);
		Screenshots.addStepWithScreenshotInReport(driver, "Select value from dropdown: " + text);
	}

	protected void selectFromDropdownByVisibleText(final String locator, String text) {
		selectFromDropdownByVisibleText(By.xpath(locator), text);
	}

	protected void selectFromDropdownByIndex(final By by, int index) {
		Select select = new Select(getElement(by));
		select.selectByIndex(index);
		logger.info("Select value from dropdown with index: " + index);
		Screenshots.addStepWithScreenshotInReport(driver, "Select value from dropdown with index: " + index);
	}

	protected void selectFromDropdownByIndex(final String locator, int index) {
		selectFromDropdownByIndex(By.xpath(locator), index);
	}

	protected void selectFromDropdownByValue(final By by, String value) {
		Select select = new Select(getElement(by));
		select.deselectByValue(value);
		logger.info("Select value from dropdown: " + value);
		Screenshots.addStepWithScreenshotInReport(driver, "Select value from dropdown: " + value);
	}

	protected void selectFromDropdownByValue(final String locator, String value) {
		selectFromDropdownByValue(By.xpath(locator), value);
	}

}
