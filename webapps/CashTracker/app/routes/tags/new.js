import TagRoute from './tag';

export default TagRoute.extend({
    model: function() {
        return this.store.createRecord('tag');
    }
});
