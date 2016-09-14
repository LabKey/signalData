package org.labkey.test.pages.signaldata;

import org.jetbrains.annotations.NotNull;
import org.labkey.test.BaseWebDriverTest;
import org.labkey.test.Locator;
import org.labkey.test.util.DataRegionTable;

import java.util.List;

public class SignalDataAssayBeginPage
{
    private BaseWebDriverTest _test;

    public SignalDataAssayBeginPage(BaseWebDriverTest test)
    {
        _test = test;
    }

    public void setSearchBox(String value)
    {
        _test.setFormElement(Locators.searchBox, value);
        _test.waitForText("Search CONTAINS " + value);
        _test.waitForFormElementToEqual(Locators.searchBox, value);
    }

    public void clearSearchBox()
    {
        setSearchBox("");
    }

    public void waitForPageLoad()
    {
        _test.waitForElement(Locators.searchBox);
    }

    public void waitForGridValue(String value, int qty)
    {
        _test.waitForElements(Locators.gridCell(value), qty);
    }

    @NotNull
    private DataRegionTable getDataRegionTable()
    {
        return new DataRegionTable("aqwp101", _test.getDriver());
    }

    public int getRowCount()
    {
        return getDataRegionTable().getDataRowCount();
    }

    public void resetUploadedData(String runToKeep)
    {
        DataRegionTable table = getDataRegionTable();
        table.uncheckAll();
        int i = 0;
        boolean somethingToDelete = false;
        for (String runIdentifier : table.getColumnDataAsText("Run Identifier"))
        {
            if (!runToKeep.equals(runIdentifier))
            {
                table.checkCheckbox(i);
                somethingToDelete = true;
            }
            i++;
        }
        if (somethingToDelete)
        {
            _test.clickButton("Delete", 0);
            _test.acceptAlert();
        }

        // TODO: Validate the delete?
    }

    public void resetSelections()
    {
        getDataRegionTable().uncheckAll();
    }

    public void selectData(@NotNull String name, @NotNull String run)
    {
        DataRegionTable table = getDataRegionTable();
        int i = 0;
        for (List<String> rowData : table.getRows("Name", "Run Identifier"))
        {
            if (name.equalsIgnoreCase(rowData.get(0)) && run.equalsIgnoreCase(rowData.get(1)))
            {
                table.checkCheckbox(i);
                break;
            }
            i++;
        }
    }

    public SignalDataUploadPage navigateToImportPage()
    {
        _test.clickButton("Import Data");
        SignalDataUploadPage page = new SignalDataUploadPage(_test);
        page.waitForPageLoad();
        return page;
    }

    public SignalDataRunViewerPage viewRuns()
    {
        _test.clickButton("View Selected Runs");
        SignalDataRunViewerPage page = new SignalDataRunViewerPage(_test);
        page.waitForPageLoad();
        return page;
    }

    private static class Locators
    {
        private static final Locator.XPathLocator searchBox = Locator.tagWithAttributeContaining("input", "name","runs-search");
        static Locator.XPathLocator gridCell(String contents)
        {
            return Locator.tag("tr[contains(@class,'labkey-row') or contains(@class,'labkey-alternate-row')]").withDescendant(Locator.tagWithText("td", contents));
        }
    }
}
