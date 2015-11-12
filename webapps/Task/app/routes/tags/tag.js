import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    actions: {
        didTransition: function() {
            return false;
        }
    }
});
