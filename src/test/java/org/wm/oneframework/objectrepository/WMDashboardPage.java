package org.wm.oneframework.objectrepository;

import org.openqa.selenium.WebDriver;
import org.wm.oneframework.utilities.BasePageObject;

public class WMDashboardPage extends BasePageObject{

	public WMDashboardPage(WebDriver webDriver) {
		super(webDriver);
	}
	
	private String DASHBOARD_BREADCRUMB = "(//div[@class='UserDashboard']//div[@class='inter-font-weight-bold'])[2]";

	public String getTextBradCrumb() {
		return getText(DASHBOARD_BREADCRUMB);
	}
}
