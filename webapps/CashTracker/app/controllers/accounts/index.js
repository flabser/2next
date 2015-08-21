import Em from 'ember';

export default Em.Controller.extend({
    accounts: Em.computed.alias('model'),

    hasAddAction: true,

    actions: {
        add() {
            this.transitionToRoute('accounts.new');
        }
    }
});
