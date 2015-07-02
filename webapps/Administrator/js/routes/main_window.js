AdminApp.MainWindowRoute = Ember.Route.extend({
    model: function() {
    	  return [{
    	      title: "Logs",
    	      viewdata: "logs"
    	    }, {
    	      title: "Users",
    	   	   viewdata: "users"
    	    }];
    }
});
