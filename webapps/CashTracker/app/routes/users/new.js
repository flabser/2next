import UserRoute from './user';

export default UserRoute.extend({
    model: function() {
        return this.store.createRecord('user');
    }
});
