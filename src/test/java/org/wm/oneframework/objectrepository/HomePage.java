package org.wm.oneframework.objectrepository;

import org.openqa.selenium.WebDriver;
import org.wm.oneframework.utilities.BasePageObject;

public class HomePage extends BasePageObject {

	public HomePage(WebDriver driver) {
		super(driver);
	}

	private String MY_WM_LINK = "//button[@class='ButtonTextIcon btn d-flex align-items-center p-0 wm-link']";
	private String EMAIL_FIELD = "//div[@class='__floater__container']//input[@type='email']";
	private String PASSWORD_FIELD = "//div[@class='__floater__container']//input[@type='password']";
	private String LOGIN_BUTTON = "(//div[contains(@class,'Login')]//button[@type='submit'])[2]";
	
	public void clickMyWMLink() {
		clickElement(MY_WM_LINK);
	}

	public void enterEmailField(String email) {
		setInputValue(EMAIL_FIELD, email);
	}
	
	public void enterPasswordField(String password) {
		setInputValue(PASSWORD_FIELD, password);
	}
	
	public void clickLoginButton() {
		clickElement(LOGIN_BUTTON);
	}
}
