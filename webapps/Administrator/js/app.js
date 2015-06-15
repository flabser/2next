App = Ember.Application.create();

/*App = Ember.Application.create({  
	  LOG_TRANSITIONS:          true,
	  LOG_TRANSITIONS_INTERNAL: true
	});*/

App.ApplicationAdapter = DS.FixtureAdapter;

/*App.ApplicationAdapter = DS.FirebaseAdapter.extend({
  firebase: new Firebase('https://flickering-heat-9887.firebaseio.com/')
});*/
