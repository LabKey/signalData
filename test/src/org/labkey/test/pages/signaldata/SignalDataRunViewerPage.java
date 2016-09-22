package org.labkey.test.pages.signaldata;

import org.labkey.test.BaseWebDriverTest;
import org.labkey.test.Locator;

/**
 * User: tgaluhn
 * Date: 9/6/2016
 */
public class SignalDataRunViewerPage
{
    private BaseWebDriverTest _test;

    public SignalDataRunViewerPage(BaseWebDriverTest test)
    {
        _test = test;
    }

    public void waitForPageLoad()
    {
        _test.waitForElement(Locator.xpath("//*[contains(@id,'startqcbtn')]"));
    }

    public void checkRunViewerCheckbox(String resultName)
    {
        _test._ext4Helper.checkGridRowCheckbox(resultName);
    }

    public void showPlot()
    {
        _test.clickButton("Overlay Selected", 0);
        _test.waitForElementToDisappear(Locator.id("sampleinputs").notHidden());
        _test.assertElementPresent(Locator.xpath("//*[@id='plotarea']//*[local-name() = 'path'][@class='line']"));
    }
}
