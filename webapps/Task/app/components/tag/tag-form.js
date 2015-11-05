import Em from 'ember';
import DS from 'ember-data';
import ModelForm from '../../mixins/components/form';
import Validate from '../../utils/validator';

export default Em.Component.extend(ModelForm, {
    tag: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('tag'));
            }
        },

        close: function() {
            this.sendAction('close');
        }
    },

    validate: function(fieldName) {
        var i18n = this.get('i18n');
        this.set('errors', DS.Errors.create());

        if (Validate.isEmpty(this.get('tag.name'))) {
            this.get('errors').add('name', i18n.t('validation_empty'));
        }

        return this.get('errors.isEmpty');
    }
});
