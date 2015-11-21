import Em from 'ember';

export default Em.Component.extend({
    tagName: 'span',
    classNames: ['issue-status'],
    classNameBindings: ['klass'],

    i18n: Em.inject.service(),
    status: null,
    labelVisible: false,

    klass: Em.computed('status', function() {
        let status = this.get('status');
        switch (status) {
            case 'DRAFT':
                return 'issue-status-draft';
            case 'PROCESS':
                return 'issue-status-process';
            case 'CLOSE':
                return 'issue-status-close';
        }
    }),

    iconClass: Em.computed('status', function() {
        let status = this.get('status');
        switch (status) {
            case 'DRAFT':
                return 'fa fa-file-o';
            case 'PROCESS':
                return 'fa fa-spinner';
            case 'CLOSE':
                return 'fa fa-check-square-o';
        }
    }),

    label: Em.computed('status', function() {
        if (this.get('labelVisible') === false) {
            return;
        }

        let status = this.get('status');
        switch (status) {
            case 'DRAFT':
                return this.get('i18n').t('issue_draft');
            case 'PROCESS':
                return this.get('i18n').t('issue_open');
            case 'CLOSE':
                return this.get('i18n').t('issue_closed');
        }
    })
});
