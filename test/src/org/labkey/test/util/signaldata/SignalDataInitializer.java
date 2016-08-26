package org.labkey.test.util.signaldata;

import org.labkey.test.BaseWebDriverTest;
import org.labkey.test.Locator;
import org.labkey.test.TestFileUtils;
import org.labkey.test.WebDriverWrapper;
import org.labkey.test.util.LogMethod;

public class SignalDataInitializer
{
    private final BaseWebDriverTest _test;
    private final String _project;

    public static final String RAW_SignalData_ASSAY = "RawSignalData";
    public static final String RAW_SignalData_DESC = "SignalData Raw Assay Data";

    public static final String RAW_SignalData_PIPELINE_PATH = TestFileUtils.getSampleData("rawSignalData").getPath();

    public SignalDataInitializer(BaseWebDriverTest test, String projectName)
    {
        _test = test;
        _project = projectName;
    }

    @LogMethod
    public void setupProject()
    {
        _test._containerHelper.createProject(_project, "Assay");
        _test._containerHelper.enableModule(_project, "SignalData");

        defineRawSignalDataAssay();
        uploadRawSignalDataData();

        _test.goToProjectHome();
    }

    @LogMethod
    private void defineRawSignalDataAssay()
    {
        _test.goToProjectHome();

        _test.log("Defining Raw SignalData Assay");
        _test.clickAndWait(Locator.linkWithText("Manage Assays"));
        _test.clickButton("New Assay Design");

        _test.assertTextPresent("Raw SignalData");
        _test.checkCheckbox(Locator.radioButtonByNameAndValue("providerName", "Raw SignalData"));
        _test.clickButton("Next");

        _test.waitForElement(Locator.xpath("//input[@id='AssayDesignerName']"), _test.WAIT_FOR_JAVASCRIPT);
        _test.setFormElement(Locator.xpath("//input[@id='AssayDesignerName']"), RAW_SignalData_ASSAY);
        _test.setFormElement(Locator.xpath("//textarea[@id='AssayDesignerDescription']"), RAW_SignalData_DESC);
        _test.fireEvent(Locator.xpath("//input[@id='AssayDesignerName']"), WebDriverWrapper.SeleniumEvent.blur);

        // Make Runs/Results editable
        _test.checkCheckbox(Locator.checkboxByName("editableRunProperties"));
        _test.checkCheckbox(Locator.checkboxByName("editableResultProperties"));

        _test.clickButton("Save", 0);
        _test.waitForText(10000, "Save successful.");
        _test.clickButton("Save & Close");

        _test.setPipelineRoot(RAW_SignalData_PIPELINE_PATH);
    }

    @LogMethod
    private void uploadRawSignalDataData()
    {
        _test.beginAt("/" + _project + "/signaldata-mockSignalDataWatch.view");
        _test.waitForText("Ready to Load");
        _test.click(Locator.tagWithClass("input", "hplc-run-btn"));
        _test.waitForText("Test Run Upload Complete");
        _test.sleep(1500);
    }
}
