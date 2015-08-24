import Em from 'ember';

export default Em.Controller.extend({
    categories: Em.computed.alias('model'),

    hasAddAction: true,

    actions: {
        composeRecord() {
            this.transitionToRoute('categories.new');
        }
    }
});
