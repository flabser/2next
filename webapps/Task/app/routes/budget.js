import Em from 'ember';
import ModelRoute from '../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    model: function() {
        return this.store.find('budget', 1);
    },

    setupController: function(controller, model) {
        controller.set('budget', model);
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('transactions.new');
        }
    }
});
