import Em from 'ember';

export default Em.Controller.extend({
    actions: {
        check: function() {
            var budget = this.get('budget');
            budget.then(function(m) {
                if (m.get('id') === 0 || m.get('name') === null) {
                    this.set('isEditMode', true);
                    this.transitionToRoute('budget');
                }
            }.bind(this));
        }
    }
});
