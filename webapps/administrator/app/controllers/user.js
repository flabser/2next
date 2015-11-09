AdminApp.UserController = Ember.Controller.extend({

    actions: {
        save: function(user) {
            user.save();
            this.transitionTo('users');
        }  

    },
  
    USER_STATUS: [{value: 'UNKNOWN',label: 'UNKNOWN'}, 
                  {value: 'NOT_VERIFIED',label: 'NOT_VERIFIED'},
                  {value: 'REGISTERED',label: 'REGISTERED'},
                  {value: 'USER_WAS_DELETED',label: 'USER_WAS_DELETED'},
                  {value: 'WAITING_FOR_VERIFYCODE',label: 'WAITING_FOR_VERIFYCODE'},
                  {value: 'VERIFYCODE_NOT_SENT',label: 'VERIFYCODE_NOT_SENT'},
                  {value: 'WAITING_FIRST_ENTERING',label: 'WAITING_FIRST_ENTERING'},
                  {value: 'RESET_PASSWORD_NOT_SENT',label: 'RESET_PASSWORD_NOT_SENT'},
                  {value: 'TEMPORARY',label: 'TEMPORARY'},
                  ]    

});

