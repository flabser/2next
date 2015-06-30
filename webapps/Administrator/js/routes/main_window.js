MyApp.MainWindowRoute = Ember.Route.extend({

    model: function() {
    	  return [{
    	      title: "Logs",
    	         	  route: "logs"
    	    }, {
    	      title: "Users",
    	     	    		  route: "users"
    	    }];
    }
});
