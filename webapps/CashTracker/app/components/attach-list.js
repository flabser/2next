import Em from 'ember';

export default Em.Component.extend({
    fieldName: 'files',
    list: [],

    uploadService: Em.inject.service('upload'),

    actions: {
        upload: function() {
            var fileInput = Em.$('[name=file]');

            if (!fileInput[0].files.length) {
                return;
            }

            var file = fileInput[0].files[0];

            var promise = this.get('uploadService').upload(file);
            promise.then((tid) => {
                fileInput.attr('value', '');
                this.sendAction('addAttach', {
                    fieldName: this.get('fieldName'),
                    realFileName: file.name,
                    tempID: tid
                });
            });
        }
    }
});
