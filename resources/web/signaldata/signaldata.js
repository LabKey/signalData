/*
 * Copyright (c) 2013-2014 LabKey Corporation
 *
 * Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
 */
if (!LABKEY.SignalData) {
    LABKEY.SignalData = {};
}

LABKEY.SignalData.signalDataSelection = function(dataRegion, dataRegionName) {
    window.location = LABKEY.ActionURL.buildURL(LABKEY.getModuleProperty('signaldata', 'QCViewProviderModule'), 'qc', null, {
        selectionKey: dataRegion.selectionKey,
        queryName: dataRegion.queryName,
        schemaName: dataRegion.schemaName,
        rowId: LABKEY.ActionURL.getParameter('rowId')
    });
};