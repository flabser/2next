import Em from 'ember';
import DS from 'ember-data';
import Validate from '../utils/validator';

export default Em.Component.extend({
    i18n: Em.inject.service(),
    errors: DS.Errors.create(),
    costCenter: null,

    willDestroyElement: function() {
        this.set('errors', null);
    },

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('costCenter'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        },

        validateName: function() {
            this.validate();
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('costCenter.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
