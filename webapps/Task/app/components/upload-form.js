import Em from 'ember';
import FormatUtils from '../utils/format-utils';

const {
    $
} = Em;

export default Em.Component.extend({
    tagName: 'div',
    classNames: ['upload'],
    uploadVisible: true,
    maxSize: 3,
    fieldName: 'files',
    attachments: [],
    modelPath: '',
    modelId: null,

    uploadService: Em.inject.service('upload'),

    didInsertElement: function() {
        this.initJqFileUpload();
    },

    initJqFileUpload: function() {
        const PATH = 'rest/file/upload';
        var _this = this;
        var $ul = $(this.get('element')).find('ul.uploads');
        var $drop = $ul.find('.drop');

        $(this.get('element')).fileupload({

            url: PATH,

            // This element will accept file drag/drop uploading
            dropZone: $drop,

            // This function is called when a file is added to the queue;
            // either via the browse button, or via drag/drop:
            add: function(e, data) {
                var $tpl = $('<li class="working"><span class="progress"></span><button type="button" class="btn cancel">x</button><p></p></li>');

                // Append the file name and file size
                $tpl.find('p').text(data.files[0].name)
                    .append('<i>' + FormatUtils.formatFileSize(data.files[0].size) + '</i>');

                // Add the HTML to the UL element
                data.context = $tpl.appendTo($ul);
                data.progressEl = $tpl.find('.progress');

                // Listen for clicks on the cancel icon
                $tpl.find('.cancel').click(function() {
                    if ($tpl.hasClass('working')) {
                        jqXHR.abort();
                    }

                    _this.sendAction('removeAttach', $(this).data('tid'));

                    $tpl.fadeOut(function() {
                        $tpl.remove();
                    });
                });

                // Automatically upload the file once it is added to the queue
                var jqXHR = data.submit();
            },

            progress: function(e, data) {
                // Calculate the completion percentage of the upload
                var progress = parseInt(data.loaded / data.total * 100, 10);

                data.progressEl.css('width', progress + '%');

                if (progress === 100) {
                    data.context.removeClass('working').addClass('done');
                }
            },

            fail: function(e, data) {
                // Something has gone wrong!
                data.context.addClass('error');
            },

            done: function(e, data) {
                _this.sendAction('addAttach', {
                    fieldName: _this.get('fieldName'),
                    realFileName: data.originalFiles[0].name,
                    tempID: data.result
                });
                data.context.find('.cancel').data('tid', data.result);
            }
        });
    },

    actions: {
        browse: function() {
            $(this.get('element')).find('input[type=file]').click();
        },

        upload: function() {
            var fileInput = $('[name=file]');

            if (!fileInput[0].files.length) {
                return;
            }

            var file = fileInput[0].files[0];

            var promise = this.get('uploadService').upload(file);
            promise.then((tid) => {
                fileInput.val('');
                this.sendAction('addAttach', {
                    fieldName: this.get('fieldName'),
                    realFileName: file.name,
                    tempID: tid
                });
            });
        },

        removeAttach: function(attach) {
            let url = attach.get('url');
            console.log(url);
            // rest/{{modelUrl}}/{{modelId}}/{{attach.fieldName}}/{{attach.realFileName}}
            $.ajax({
                method: 'DELETE',
                url: 'rest/' + this.get('modelUrl') + '/' + this.get('modelId') + '/' + attach.get('fieldName') + '/' + attach.get('realFileName'),
                success: function() {
                    attach.deleteRecord();
                }
            });
        }
    }
});
