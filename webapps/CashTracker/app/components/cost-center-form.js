import Em from 'ember';
import DS from 'ember-data';
import ModelFormMixin from '../mixins/components/form';
import Validate from '../utils/validator';

export default Em.Component.extend(ModelFormMixin, {
    costCenter: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('costCenter'));
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

        if (Validate.isEmpty(this.get('costCenter.name'))) {
            this.get('errors').add('name', this.get('i18n').t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
