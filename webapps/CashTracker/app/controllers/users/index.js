import Em from 'ember';

export default Em.Controller.extend({
    users: Em.computed.alias('model'),

    hasAddAction: true,

    actions: {
        add() {
            this.transitionTo('users.new');
        }
    }
});
