import Em from 'ember';

export default Em.Controller.extend({
    accounts: Em.computed.alias('model'),

    hasAddAction: true,

    actions: {
        composeRecord() {
            this.transitionToRoute('accounts.new');
        }
    }
});
