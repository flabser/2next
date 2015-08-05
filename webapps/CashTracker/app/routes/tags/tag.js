import Em from 'ember';

export default Em.Route.extend({
    templateName: 'tags/tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    }
});
