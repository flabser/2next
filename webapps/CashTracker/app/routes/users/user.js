import Em from 'ember';

export default Em.Route.extend({
    templateName: 'users/user',

    model: function(params) {
        return this.store.find('user', params.user_id);
    }
});
