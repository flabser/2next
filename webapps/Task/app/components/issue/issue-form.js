import Em from 'ember';
import DS from 'ember-data';
import ModelFormMixin from 'lof-task/mixins/components/form';
import Validate from 'lof-task/utils/validator';

export default Em.Component.extend(ModelFormMixin, {
    issue: null,

    saveIssue: 'saveIssue',
    close: 'transitionToIssues',

    issueInProcess: Em.computed('issue.status', function() {
        return this.get('issue.status') === 'PROCESS';
    }),

    actions: {
        closeIssue: function() {
            this.set('issue.status', 'CLOSE');
            this.send('save');
        },

        save: function() {
            if (this.validate()) {
                this.sendAction('saveIssue', this.get('issue'));
            }
        },

        close: function() {
            this.sendAction('close');
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
