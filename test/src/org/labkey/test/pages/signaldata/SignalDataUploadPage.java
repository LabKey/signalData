package org.labkey.test.pages.signaldata;

import org.labkey.test.BaseWebDriverTest;
import org.labkey.test.Locator;
import org.labkey.test.util.Ext4Helper;

import java.io.File;

public class SignalDataUploadPage
{
    private static final String CLEAR_BUTTON = "Clear Run";

    protected BaseWebDriverTest _test;

    public SignalDataUploadPage(BaseWebDriverTest test)
    {
        _test = test;
    }

    public void uploadFile(File file)
    {
        _test.setFormElement(SignalDataUploadPage.Locators.fileInput, file);
        _test.waitForElement(SignalDataUploadPage.Locators.fileLogCellwithText(file.getName()));
    }

    public void deleteFile(String fileName)
    {
        _test.click(SignalDataUploadPage.Locators.fileLogDeleteCell(fileName));
        _test.waitForElementToDisappear(SignalDataUploadPage.Locators.fileLogCellwithText(fileName));
    }

    public void setRunIDField(String runName)
    {
        _test.setFormElement(SignalDataUploadPage.Locators.runIdentifier, runName);
        _test.waitForFormElementToEqual(SignalDataUploadPage.Locators.runIdentifier, runName);
    }

    public void waitForPageLoad()
    {
        _test.waitForElement(SignalDataUploadPage.Locators.fileInput, 1000);
    }

    public void clearRun()
    {
        _test.clickButton(CLEAR_BUTTON, 0);
        _test._ext4Helper.clickWindowButton("Clear Run", "Yes", 0, 0);
        _test.waitForElementToDisappear(Ext4Helper.Locators.getGridRow()); //Check grid is cleared
        _test.assertFormElementEquals(SignalDataUploadPage.Locators.runIdentifier, ""); //check form is cleared
    }

    private static class Locators
    {
        public static final Locator.XPathLocator runIdentifier = Locator.xpath("//input[contains(@id,'RunIdentifier')]");
        public static final Locator.XPathLocator fileInput = Locator.xpath("//input[@type='file']");
        public static Locator.XPathLocator fileLogCellwithText(String text)
        {
            return Locator.xpath("//td[@role='gridcell']//*[text()='" + text + "']");
        }

        /**
         * @param filename of row to find
         * @return
         */
        public static final Locator.XPathLocator fileLogDeleteCell(String filename) {
            return Locator.xpath("//tr[@role='row']").withDescendant(Locator.xpath("//*[text()='" + filename + "']"))
                    .append("//img[contains(@class, 'iconDelete')]");
        }
    }
}
