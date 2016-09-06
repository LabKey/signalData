package org.labkey.test.pages.signaldata;

import org.labkey.test.BaseWebDriverTest;
import org.labkey.test.Locator;
import org.labkey.test.util.Ext4Helper;
import org.openqa.selenium.Keys;

import java.io.File;

public class SignalDataUploadPage
{
    private static final String CLEAR_BUTTON = "Clear Run";

    private BaseWebDriverTest _test;

    public SignalDataUploadPage(BaseWebDriverTest test)
    {
        _test = test;
    }

    public void uploadMetadataFile(File file)
    {
        _test.setFormElement(SignalDataUploadPage.Locators.metadataFileInput, file);
       // _test.waitForElement(SignalDataUploadPage.Locators.metadataFileTextBox(file.getName()));
        _test.click(Locators.uploadMetadataButton);
        _test.waitForElement(Locators.runIdentifier);
    }

    public void uploadFile(File file)
    {
        _test.setFormElement(SignalDataUploadPage.Locators.dropFileInput, file);
    }

    public void uploadIncorrectFile(File file)
    {
        uploadFile(file);
        Locator msgBox = Locators.fileNotUploadedMsgBox(file.getName());
        _test.waitForElement(msgBox);
        msgBox.findElement(_test.getWrappedDriver()).sendKeys(Keys.ESCAPE);
    }

    public void deleteFile(String fileName)
    {
        _test.click(SignalDataUploadPage.Locators.fileLogDeleteCell(fileName));
        _test.waitForElementToDisappear(SignalDataUploadPage.Locators.fileLogCellwithText(fileName));
    }

    public void setRunIDField(String runName)
    {
        _test.setFormElement(Locators.runIdentifier, runName);
        _test.waitForFormElementToEqual(Locators.runIdentifier, runName);
    }

    public void waitForPageLoad()
    {
        _test.waitForElement(SignalDataUploadPage.Locators.metadataFileInput, 1000);
    }

    public void clearRun()
    {
        _test.clickButton(CLEAR_BUTTON, 0);
        _test._ext4Helper.clickWindowButton("Clear Run", "Yes", 0, 0);
        _test.waitForElementToDisappear(Ext4Helper.Locators.getGridRow()); //Check grid is cleared
//        _test.assertFormElementEquals(SignalDataUploadPage.Locators.runIdentifier, ""); //check form is cleared
    }

    public void saveRun()
    {
        _test.clickButton("Save Run", 0);
    }

    private static class Locators
    {
        static final Locator.XPathLocator runIdentifier = Locator.tagWithAttributeContaining("input", "id","RunIdentifier").notHidden();
        static final Locator.XPathLocator metadataFileInput = Locator.tagWithAttribute("input", "type", "file").withoutAttribute("multiple", "multiple");
        static final Locator.XPathLocator dropFileInput = Locator.tagWithAttribute("input", "type", "file").withAttribute("multiple", "multiple");
        static final Locator.XPathLocator uploadMetadataButton = Locator.tagWithAttribute("a", "role", "button").withDescendant(Locator.tag("span").withText("next"));

        static Locator.XPathLocator metadataFileTextBox(String fileName)
        {
            return Locator.inputById("file-inputEl").containing(fileName);
        }

        static Locator.XPathLocator fileLogCellwithText(String text)
        {
            return Locator.xpath("//td[@role='gridcell']//*[text()='" + text + "']");
        }

        static Locator.XPathLocator fileNotUploadedMsgBox(String fileName)
        {
            return Locator.tagWithClass("div", "x4-message-box").notHidden()
                    .withDescendant(Locator.tagWithAttribute("div", "role", "input").withText(fileName + " not uploaded"));
        }

        /**
         * @param filename of row to find
         * @return
         */
        static Locator.XPathLocator fileLogDeleteCell(String filename) {
            return Locator.tagWithAttribute("tr","role", "row").withDescendant(Locator.xpath("//*[text()='" + filename + "']"))
                    .append("//img[contains(@class, 'iconDelete')]");
        }
    }
}
