import Em from 'ember';

export default Em.Route.extend({
    model: function() {
        return this.store.findAll('task');
    },

    actions: {
        composeRecord: function() {
            this.transitionTo('tasks.new');
        },

        saveRecord: function(task) {
            task.save().then(() => {
                this.transitionTo('tasks');
            }, function() {
                console.log(arguments);
            });
        },

        deleteRecord: function(task) {
            task.destroyRecord().then(() => {
                this.transitionTo('tasks');
            }, function(resp) {
                task.rollbackAttributes();
                alert(resp.errors.message);
            });
        }
    }
});
