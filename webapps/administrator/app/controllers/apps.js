AdminApp.AppsController = Ember.Controller.extend({


    actions: {
        selectAll: function() {},
        deletea: function(obj) {
        	obj.deleteRecord();
            obj.save();
            this.sendAction('refreshRoute');
        }
    }
});


