package org.wm.oneframework.tests;

import java.util.Map;

import org.openqa.selenium.support.PageFactory;
import org.testng.annotations.Test;
import org.wm.oneframework.annotation.ExcelDetails;
import org.wm.oneframework.configprovider.ConfigProvider;
import org.wm.oneframework.objectrepository.HomePage;
import org.wm.oneframework.objectrepository.WMDashboardPage;
import org.wm.oneframework.seleniumadapter.utils.BaseTest;
import org.wm.oneframework.utilities.AssertionLibrary;
import org.wm.oneframework.utilities.PageObjectFactory;

@ExcelDetails
public class LoginTest extends BaseTest {

	@Test(dataProvider = "data", description = "successful login")
	public void loginTest(Map<String, String> input) throws InterruptedException {

		launchApplication(ConfigProvider.getAsString("url"));
		HomePage homePage = PageObjectFactory.initElements(getDriver(), HomePage.class);
		homePage.clickMyWMLink();
		homePage.enterEmailField(input.get("username").trim());
		homePage.enterPasswordField(input.get("password").trim());
		homePage.clickLoginButton();
		
		WMDashboardPage dashboardPage = PageFactory.initElements(getDriver(), WMDashboardPage.class);
		AssertionLibrary.assertEquals(dashboardPage.getTextBradCrumb(), input.get("breadcrumb").trim(),"Verified that dashboard is displayed");
		
		
	}	

}
