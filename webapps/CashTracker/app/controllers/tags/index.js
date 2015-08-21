import Em from 'ember';

export default Em.Controller.extend({
    tags: Em.computed.alias('model'),

    hasAddAction: true,

    actions: {
        add() {
            this.transitionToRoute('tags.new');
        }
    }
});
