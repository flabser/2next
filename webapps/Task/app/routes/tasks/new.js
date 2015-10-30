import TaskRoute from './task';

export default TaskRoute.extend({
    model: function() {
        return this.store.createRecord('task');
    }
});
