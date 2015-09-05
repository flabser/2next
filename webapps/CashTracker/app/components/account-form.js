import Em from 'ember';
import DS from 'ember-data';

export default Em.Component.extend({
    errors: DS.Errors.create(),

    account: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('account'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (!this.get('account.name')) {
            this.get('errors').add('name', 'can_not_be_empty');
        }

        return this.get('errors.isEmpty');
    }
});
