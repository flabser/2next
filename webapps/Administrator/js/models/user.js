App.User = DS.Model.extend({
	login:DS.attr('string'),
	userName:DS.attr('string')
});

App.User.FIXTURES = [
                     {
                       id: "3",
                       login: "vasu",
                       userName: "Fix bug with player"
                      
                     },
                     {
                    	  id: "34",
                          login: "sima",
                          userName: "Fix bug with player"
                     },
                     {
                    	  id: "4",
                          login: "kfdd",
                          userName: "h player"
                     }
                   ];

