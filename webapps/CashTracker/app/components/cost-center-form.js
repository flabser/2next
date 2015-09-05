import Em from 'ember';
import DS from 'ember-data';

export default Em.Component.extend({
    errors: DS.Errors.create(),

    costCenter: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('costCenter'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (!this.get('costCenter.name')) {
            this.get('errors').add('name', 'can_not_be_empty');
        }

        return this.get('errors.isEmpty');
    }
});
