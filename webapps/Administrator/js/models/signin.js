App.Signin = DS.Model.extend({
	login:DS.attr('string'),
	pwd:DS.attr('string'),
	status:DS.attr('string'),
	error:DS.attr('string')
});

App.User.FIXTURES = [
                     {
                       id: "3",
                       login: "vasu",
                       pwd: "Fix bug with player"
                      
                     },
                     {
                    	  id: "34",
                          login: "sima",
                          pwd: "Fix bug with player"
                     },
                     {
                    	  id: "4",
                          login: "kfdd",
                          pwd: "h player"
                     }
                   ];