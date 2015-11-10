import Em from 'ember';
import ModelRouteMixin from 'lof-task/mixins/routes/model';

export default Em.Route.extend(ModelRouteMixin, {
    model: function() {
        return this.store.findAll('tag');
    },

    actions: {
        didTransition: function() {
            Em.run.schedule('afterRender', this, function() {
                Em.$('#input-new-tag').val('').focus();
            });
        }
    }
});
