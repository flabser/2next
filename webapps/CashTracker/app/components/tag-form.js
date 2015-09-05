import Em from 'ember';
import DS from 'ember-data';

export default Em.Component.extend({
    errors: DS.Errors.create(),

    tag: null,

    actions: {
        save: function() {
            if (this.validate()) {
                this.sendAction('saveRecord', this.get('tag'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (!this.get('tag.name')) {
            this.get('errors').add('name', 'can_not_be_empty');
        }

        return this.get('errors.isEmpty');
    }
});
