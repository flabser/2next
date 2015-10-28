import Em from 'ember';
import ModelRoute from '../../mixins/routes/model';

export default Em.Route.extend(ModelRoute, {
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    setupController: function(controller, model) {
        controller.set('category', model);
        controller.set('categories', this.store.findAll('category'));
    }
});
