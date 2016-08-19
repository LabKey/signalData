/**
 * Created by iansigmon on 8/8/16.
 */
Ext4.ns('LABKEY.SignalData');

LABKEY.SignalData.initializeUploadForm = function(metadataFormElId, metadataFileId, metadataTSVId, nextButtonElId, renderToId) {

    var assayType = 'Signal Data';
    var assay;

    var loadAssay = function(cb, scope) {
        if (LABKEY.page && LABKEY.page.assay) {
            assay = LABKEY.page.assay;
            cb.call(scope || this);
        }
        else {
            LABKEY.Assay.getByType({
                type: assayType,
                success: function(definitions) {
                    if (definitions.length == 0) {
                        var link = LABKEY.Utils.textLink({
                            text: 'New assay design',
                            href: LABKEY.ActionURL.buildURL('assay', 'chooseAssayType')
                        });
                        Ext4.get(renderToId).update('To get started, create a "' + assayType + '" assay. ' + link);
                    }
                    else if (definitions.length == 1) {
                        assay = definitions[0];
                        cb.call(scope || this);
                    }
                    else {
                        // In the future could present a dropdown allowing the user to switch between active assay design
                        Ext4.get(renderToId).update('This webpart does not currently support multiple "' + assayType + '" assays in the same folder.');
                    }
                }
            });
        }
    };

    var formSubmitted = function (data) {
        var run = new LABKEY.Exp.Run();
        run.name = data.name;
        run.dataInputs = [ data ];

        if (!data.content)
        {
            data.getContent({
                format: 'jsonTSVExtended',
                success: function (content, format)
                {
                    data.content = content;
                    LABKEY.SignalData.initializeDataFileUploadForm(metadataFormElId, renderToId, assay, run, content);
                    document.getElementById(metadataFormElId).style.display = 'none';
                },
                failure: function (error, format)
                {
                }
            });
        }
    };

    var formFailed = function (form, action) {
        alert('Upload failed!');
    };

    LABKEY.SignalData.disableFileUpload = function() {
        tsvForm.setVisible(true);
        fileForm.setVisible(false);

        activeForm = tsvForm;
    };

    LABKEY.SignalData.enableFileUpload = function() {
        tsvForm.setVisible(false);
        fileForm.setVisible(true);

        activeForm = fileForm;
    };


    var activeForm;
    var fileForm;
    var tsvForm;
    loadAssay(function() {
        fileForm = Ext4.create('Ext.form.Panel',{
            renderTo: metadataFileId,
            border: false,
            bodyStyle: 'background-color: transparent;',
            fileUpload: true,
            url: LABKEY.ActionURL.buildURL("assay", "assayFileUpload", LABKEY.ActionURL.getContainer(), { protocolId: assay.id }),
            items: [{
                // TODO: Add assay template file download.
                xtype: 'filefield',
                id: 'file',
                msgTarget: 'side',
                // buttonOnly: true,
                buttonConfig: {
                    text: 'Upload txt, tsv, xls, xlsx'
                }
            }],
            listeners: {
                actioncomplete : function (form, action) {
                    var data = new LABKEY.Exp.Data(action.result);
                    formSubmitted(data);
                },
                actionfailed: formFailed
            }
        });

        tsvForm = Ext4.create('Ext.form.Panel',{
            renderTo: metadataTSVId,
            border: false,
            bodyStyle: 'background-color: transparent;',
            getTsvInput: function(){
                return Ext4.getCmp('tsv').value;
            },
            items: [{
                // TODO: Add assay template file download.
                xtype: 'textarea',
                id: 'tsv',
                height: 300,
                minHeight: 300,
                width: 400,
                minWidth:150
            }],
            submit:function(options){
                LABKEY.Ajax.request({
                    url: LABKEY.ActionURL.buildURL("assay", "assayFileUpload", LABKEY.ActionURL.getContainer()),
                    params: { protocolId: assay.id, fileName: metadataTSVId + '.txt', fileContent: this.getTsvInput() },
                    success: function(response, options) {
                        var data = new LABKEY.Exp.Data(Ext.util.JSON.decode(response.responseText));
                        formSubmitted(data);
                    },
                    failure: formFailed
                });
            },
            scope: this
        });

        var nextButton = Ext4.create('Ext.button.Button', {
            xtype: 'button',
            renderTo: nextButtonElId,
            text: 'next',
            handler: function () {
                activeForm.submit();
            }
        });

        LABKEY.SignalData.enableFileUpload();
    });
};
