import CategoryRoute from './category';

export default CategoryRoute.extend({
    model: function() {
        return this.store.createRecord('category');
    }
});
