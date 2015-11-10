AdminApp.UsersController = Ember.Controller.extend({


    actions: {
        selectAll: function() {},
        deleteu: function(obj) {
        	console.log(obj.id);            
            obj.deleteRecord();
            obj.save();
            this.sendAction('refreshRoute');
        },
        isCompleted: function(key, value) {
        	console.log("ddd")
        }
    }
});


