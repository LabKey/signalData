/*
 * Copyright (c) 2016 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
