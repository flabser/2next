import Em from 'ember';

export default Em.Route.extend({
    actions: {
        didTransition: function() {
            return false;
        }
    }
});
