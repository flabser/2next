import Em from 'ember';

export default Em.Component.extend({
    tagName: 'div',
    classNames: ['attachments'],
    isVisible: true,
    list: [],

    uploadService: Em.inject.service('upload'),

    actions: {
        upload: function() {
            var fileInput = Em.$('[name=file]');

            if (!fileInput[0].files.length) {
                return;
            }

            var promise = this.get('uploadService').upload(fileInput);
            promise.then((tid) => {
                fileInput.attr('value', '');
                this.sendAction('addAttach', {
                    fieldName: 'files',
                    realFileName: fileInput[0].files[0].name,
                    tempID: tid
                });
            });
        }
    }
});
