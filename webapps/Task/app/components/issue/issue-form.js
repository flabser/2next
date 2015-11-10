import Em from 'ember';
import DS from 'ember-data';
import ModelFormMixin from 'lof-task/mixins/components/form';
import Validate from 'lof-task/utils/validator';

export default Em.Component.extend(ModelFormMixin, {
    issue: null,

    saveIssue: 'saveIssue',
    close: 'transitionToIssues',

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('issue'));
            }
        },

        close: function() {
            this.sendAction('close');
        },

        validateName: function() {
            this.validate();
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('issue.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
