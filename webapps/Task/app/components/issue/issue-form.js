import Em from 'ember';
import DS from 'ember-data';
import ModelFormMixin from 'lof-task/mixins/components/form';
import Validate from 'lof-task/utils/validator';

export default Em.Component.extend(ModelFormMixin, {
    issue: null,
    edit: false,
    isEditing: Em.computed.bool('edit'),

    saveIssue: 'saveIssue',
    addComment: 'addComment',
    close: 'transitionToIssues',
    back: 'transitionToIssues',

    canCloseIssue: Em.computed('issue.status', function() {
        return !this.get('issue.isNew') && this.get('issue.status') === 'PROCESS';
    }),

    canAddComment: Em.computed('issue.status', function() {
        if (this.get('issue.isNew')) {
            return false;
        }

        let status = this.get('issue.status');
        return status === 'PROCESS' || status === 'DRAFT';
    }),

    actions: {
        back: function() {
            this.sendAction('back');
        },

        addIssue: function() {
            this.set('issue.status', 'PROCESS');
            this.send('save');
        },

        saveAsDraft: function() {
            this.set('issue.status', 'DRAFT');
            this.send('save');
        },

        closeIssue: function() {
            this.set('issue.status', 'CLOSE');
            this.send('save');
        },

        addComment: function() {
            this.sendAction('addComment', this.get('issue'), this.get('newComment'));
        },

        editIssue: function() {
            this.set('edit', true);
        },

        save: function() {
            if (this.validate()) {
                this.sendAction('saveIssue', this.get('issue'));
            }
        },

        close: function() {
            if (!this.get('issue.isNew') && this.get('isEditing')) {
                this.set('edit', false);
            } else {
                this.sendAction('close');
            }
        },

        validateBody: function() {
            this.validate('body');
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('issue.body'))) {
            this.get('errors').add('body', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
