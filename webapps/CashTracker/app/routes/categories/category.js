import Em from 'ember';

export default Em.Route.extend({
    templateName: 'categories/category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    }
});
