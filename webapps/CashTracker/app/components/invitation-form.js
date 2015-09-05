import Em from 'ember';
import DS from 'ember-data';

export default Em.Component.extend({
    errors: DS.Errors.create(),

    invitation: null,

    actions: {
        sendInvite: function() {
            if (this.validate()) {
                this.sendAction('sendInvite', this.get('invitation'));
            }
        },

        cancel: function() {
            this.sendAction('cancel');
        }
    },

    validate: function() {
        this.set('errors', DS.Errors.create());

        if (!this.get('invitation.email')) {
            this.get('errors').add('email', 'can_not_be_empty');
        }
        if (!this.get('invitation.message')) {
            this.get('errors').add('message', 'can_not_be_empty');
        }

        return this.get('errors.isEmpty');
    }
});
