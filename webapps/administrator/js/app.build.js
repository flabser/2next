AdminApp = Ember.Application.create();

var baseURL =  DS.RESTAdapter.extend({
    namespace: 'rest'
});

AdminApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
           return type + 's';

    }
});

AdminApp.Router.map(function(){
	this.route('users', {
	    path: '/'
	  });
	this.route('logs');
	this.route('app', {path: '/app/:app_id'});
	this.route('apps');
	this.route('user', {path: '/users/:user_id'});
	this.route('users');
	this.route('newUser', {path: '/users/new'});
	this.route('userprofile');
});


AdminApp.ApplicationView = Ember.Component.extend({
    tagName: ''
});

AdminApp.SelectNativeComponent = Ember.Component.extend({
    tagName: 'select',
    content: null,
    value: null,

    didInsertElement: function() {
        var selectEl = this.get('element');
        var v = this.get('value');
        this.$('option[value=' + v + ']', selectEl).attr('selected', true);
    },

    change: function() {
        this.send('change');
    },

    actions: {
        change: function() {
            var selectEl = this.get('element'),
                selectedIndex = selectEl.selectedIndex,
                content = this.get('content'),
                selection = content.objectAt(selectedIndex);

            this.set('value', selection.value);
        }
    }
});

AdminApp.SelectUserStatusComponent = Ember.Component.extend({
    tagName: 'div',
    classNames: ['select-user-staus'],
    value: null,

    types: [{
        code: '455',
        name: 'NOT_VERIFIED'
    }, {
        code: '456',
        name: 'REGISTERED'
    }, {
        code: '457',
        name: 'USER_WAS_DELETED'
    }],

    _showActive: function() {
        var type = this.get('value');
        if (type) {
            var el = Em.$('[data-type=' + type + ']:not(.active)', this.element);
            if (el.length) {
                Em.$('.active', this.element).removeClass('active');
                el.addClass('active');
            }
        } else {
            Em.$('.active', this.element).removeClass('active');
        }
    },

    valueObserver: Em.observer('value', function() {
        this._showActive();
    }),

    didInsertElement: function() {
        this._showActive();
    },

    actions: {
        toggle: function(type) {
            if (type !== this.get('value')) {
                this.set('value', type);
            } else {
                this.set('value', '');
            }
        }
    }
});

AdminApp.AppController = Ember.Controller.extend({
    actions: {
        save: function(app) {
        	console.log("save app");
            app.save();
            this.transitionTo('apps');
        }
    }
});

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



AdminApp.LogsController = Ember.Controller.extend({
    actions: {
        selectAll: function() {}
    }
});


AdminApp.NewUserController = Ember.Controller.extend({
    actions: {
        save: function(user) {
            console.log("save new user", user);

            var newUser = this.store.createRecord('user', {
                login: user.login,
                pwd: user.pwd,
                email: user.email
            });
            newUser.save();

            this.transitionTo('users');
        }
    }
});

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



AdminApp.SelectedHelper = Ember.Helper.helper(function(foo, bar) {
	return foo == bar ? ' selected' : '';
});
AdminApp.App = DS.Model.extend({
    appName: DS.attr('string'),
    owner: DS.attr('string'),
    appType: DS.attr('string'),
    status: DS.attr('string'),
    dbHost: DS.attr('string'),
    dbLogin: DS.attr('string'),
    dbPwd: DS.attr('date'),
    dbName: DS.attr('string'),
  
});

AdminApp.Log = DS.Model.extend({
    name: DS.attr('string'),
    length: DS.attr('number'),
    lastModified: DS.attr('string')
    
});

AdminApp.Role = DS.Model.extend({
  name: DS.attr('string'),
  user: DS.belongsTo('role')
})
AdminApp.User = DS.Model.extend({
    userName: DS.attr('string'),
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    verifyCode: DS.attr('string'),
    primaryRegDate: DS.attr('date'),
    regDate: DS.attr('string'),
    status: DS.attr('string'),
    email: DS.attr('string'),
    isSupervisor: DS.attr('boolean'),
    dbLogin: DS.attr('string'),
    defaultDbPwd:  DS.attr('string'),

    roles: DS.hasMany('role', {
        async: true
      })
});


AdminApp.AppRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('app', params.app_id);
    }
});

AdminApp.ApplicationRoute = Ember.Route.extend({
    templateName: 'application',

    model: function() {
        return [{
            title: "Logs",
            viewdata: "logs"
        }, {
            title: "Users",
            viewdata: "users"
        }, {
                     title: "Applications",
                     viewdata: "apps"
            }];
    }
});

AdminApp.AppsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('app');
    }
});

AdminApp.UsersNewRoute = Ember.Route.extend({
    templateName: 'app'
});

AdminApp.LogsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('log');
    }
});


AdminApp.NewUserRoute = Ember.Route.extend({
     templateName: 'user'
});

AdminApp.UserRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user', params.user_id);
    }
});


AdminApp.UserProfileRoute = Ember.Route.extend({

    model: function() {
    	var u =  this.store.get('session.user');
    	console.log(u);
        return this.get('session.user');
    },

   
    setupController: function(controller, model) {
        controller.set('userProfile', model);
    }
    
    
});

AdminApp.UsersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.findAll('user');
    }
	
});

AdminApp.UsersNewRoute = Ember.Route.extend({
    templateName: 'user'
});

Ember.TEMPLATES["app"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 33,
          "column": 6
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("h1");
      var el3 = dom.createTextNode("Application ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("form");
      dom.setAttribute(el2,"class","form-horizontal");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputAppType");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Type");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputAppType");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Name");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    	");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputAppType");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Owner");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n  		 ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputAppType");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Database host");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n  		 ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputAppType");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Database login");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    	 ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputAppType");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Database name");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    	");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n        	");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createTextNode("Save & Close");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        	");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/apps");
      var el5 = dom.createTextNode("Cancel");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    	");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [3]);
      var element2 = dom.childAt(element1, [13, 1]);
      var morphs = new Array(8);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element1, [1, 3]),0,0);
      morphs[2] = dom.createMorphAt(dom.childAt(element1, [3, 3]),0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element1, [5, 3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element1, [7, 3]),0,0);
      morphs[5] = dom.createMorphAt(dom.childAt(element1, [9, 3]),0,0);
      morphs[6] = dom.createMorphAt(dom.childAt(element1, [11, 3]),0,0);
      morphs[7] = dom.createElementMorph(element2);
      return morphs;
    },
    statements: [
      ["content","model.appName",["loc",[null,[2,17],[2,34]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.appType",["loc",[null,[6,76],[6,89]]]]],[],[]],"disabled",true],["loc",[null,[6,25],[6,105]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.appName",["loc",[null,[10,76],[10,89]]]]],[],[]],"required",true],["loc",[null,[10,25],[10,105]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.owner",["loc",[null,[14,76],[14,87]]]]],[],[]],"required",true],["loc",[null,[14,25],[14,103]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.dbHost",["loc",[null,[18,76],[18,88]]]]],[],[]],"required",true],["loc",[null,[18,25],[18,104]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.dbLogin",["loc",[null,[22,76],[22,89]]]]],[],[]],"required",true],["loc",[null,[22,25],[22,105]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.dbName",["loc",[null,[26,76],[26,88]]]]],[],[]],"required",true],["loc",[null,[26,25],[26,104]]]],
      ["element","action",["save",["get","this",["loc",[null,[29,57],[29,61]]]]],[],["loc",[null,[29,41],[29,63]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["application"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "topLevel": null,
          "revision": "Ember@2.1.0+45f524a3",
          "loc": {
            "source": null,
            "start": {
              "line": 35,
              "column": 22
            },
            "end": {
              "line": 35,
              "column": 58
            }
          }
        },
        isEmpty: false,
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode(" ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode(" ");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment,1,1,contextualElement);
          return morphs;
        },
        statements: [
          ["content","m.title",["loc",[null,[35,46],[35,57]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "topLevel": null,
        "revision": "Ember@2.1.0+45f524a3",
        "loc": {
          "source": null,
          "start": {
            "line": 33,
            "column": 18
          },
          "end": {
            "line": 37,
            "column": 18
          }
        }
      },
      isEmpty: false,
      arity: 1,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                  ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("li");
        var el2 = dom.createTextNode("\n                      ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                  ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1]),1,1);
        return morphs;
      },
      statements: [
        ["block","link-to",[["get","m.viewdata",["loc",[null,[35,33],[35,43]]]]],[],0,null,["loc",[null,[35,22],[35,70]]]]
      ],
      locals: ["m"],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 53,
          "column": 0
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","layout");
      var el2 = dom.createTextNode("\n  ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","container");
      var el3 = dom.createTextNode("\n      ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","header");
      var el4 = dom.createTextNode("\n         ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","navbar navbar-default");
      var el5 = dom.createTextNode("\n    ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","container-fluid");
      var el6 = dom.createTextNode("\n      ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","navbar-header");
      var el7 = dom.createTextNode("\n       ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("a");
      dom.setAttribute(el7,"class","navbar-brand");
      dom.setAttribute(el7,"href","page/about");
      var el8 = dom.createTextNode("\n  			");
      dom.appendChild(el7, el8);
      var el8 = dom.createElement("img");
      dom.setAttribute(el8,"src","../img/logo.png");
      dom.setAttribute(el8,"alt","logo");
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("  Administrator");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n      ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n      ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment(" Collect the nav links, forms, and other content for toggling ");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n      ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","collapse navbar-collapse");
      dom.setAttribute(el6,"id","bs-example-navbar-collapse-1");
      var el7 = dom.createTextNode("\n       \n\n        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("ul");
      dom.setAttribute(el7,"class","nav navbar-nav navbar-right");
      var el8 = dom.createTextNode("\n          ");
      dom.appendChild(el7, el8);
      var el8 = dom.createElement("li");
      dom.setAttribute(el8,"class","dropdown");
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("a");
      dom.setAttribute(el9,"href","#");
      dom.setAttribute(el9,"class","dropdown-toggle");
      dom.setAttribute(el9,"data-toggle","dropdown");
      dom.setAttribute(el9,"role","button");
      dom.setAttribute(el9,"aria-haspopup","true");
      dom.setAttribute(el9,"aria-expanded","false");
      var el10 = dom.createComment("");
      dom.appendChild(el9, el10);
      var el10 = dom.createElement("span");
      dom.setAttribute(el10,"class","caret");
      dom.appendChild(el9, el10);
      dom.appendChild(el8, el9);
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("ul");
      dom.setAttribute(el9,"class","dropdown-menu");
      var el10 = dom.createTextNode("\n              ");
      dom.appendChild(el9, el10);
      var el10 = dom.createElement("li");
      var el11 = dom.createElement("a");
      dom.setAttribute(el11,"href","#");
      var el12 = dom.createTextNode("Action");
      dom.appendChild(el11, el12);
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      var el10 = dom.createTextNode("\n              ");
      dom.appendChild(el9, el10);
      var el10 = dom.createElement("li");
      var el11 = dom.createElement("a");
      dom.setAttribute(el11,"href","#");
      var el12 = dom.createTextNode("Another action");
      dom.appendChild(el11, el12);
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      var el10 = dom.createTextNode("\n              ");
      dom.appendChild(el9, el10);
      var el10 = dom.createElement("li");
      var el11 = dom.createElement("a");
      dom.setAttribute(el11,"href","#");
      var el12 = dom.createTextNode("Something else here");
      dom.appendChild(el11, el12);
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      var el10 = dom.createTextNode("\n              ");
      dom.appendChild(el9, el10);
      var el10 = dom.createElement("li");
      dom.setAttribute(el10,"role","separator");
      dom.setAttribute(el10,"class","divider");
      dom.appendChild(el9, el10);
      var el10 = dom.createTextNode("\n              ");
      dom.appendChild(el9, el10);
      var el10 = dom.createElement("li");
      var el11 = dom.createElement("a");
      dom.setAttribute(el11,"href","#");
      var el12 = dom.createTextNode("Separated link");
      dom.appendChild(el11, el12);
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      var el10 = dom.createTextNode("\n            ");
      dom.appendChild(el9, el10);
      dom.appendChild(el8, el9);
      var el9 = dom.createTextNode("\n          ");
      dom.appendChild(el8, el9);
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n        ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n      ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createComment(" /.navbar-collapse ");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n    ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createComment(" /.container-fluid ");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n  ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n      ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n      ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("aside");
      var el4 = dom.createTextNode("\n          ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","outline");
      var el5 = dom.createTextNode("\n              ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("ul");
      dom.setAttribute(el5,"class","nav nav-pills nav-stacked");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("              ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n          ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n      ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n      ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("section");
      dom.setAttribute(el3,"class","view");
      var el4 = dom.createTextNode("\n          ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n      ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    \n  ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n  ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","footer-spacer");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n  ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("footer");
      dom.setAttribute(el2,"class","footer");
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","container");
      var el4 = dom.createTextNode("\n      ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("span");
      dom.setAttribute(el4,"class","glyphicon glyphicon-copyright-mark");
      dom.setAttribute(el4,"aria-hidden","true");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode(" 2015 ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"href","//flabser.com");
      var el5 = dom.createTextNode("flabser.com");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n  ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0, 1]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1, 1, 1, 5, 1, 1, 1]),0,0);
      morphs[1] = dom.createMorphAt(dom.childAt(element0, [3, 1, 1]),1,1);
      morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),1,1);
      return morphs;
    },
    statements: [
      ["content","model.userProfile.login",["loc",[null,[16,128],[16,155]]]],
      ["block","each",[["get","model",["loc",[null,[33,26],[33,31]]]]],[],0,null,["loc",[null,[33,18],[37,27]]]],
      ["content","outlet",["loc",[null,[42,10],[42,20]]]]
    ],
    locals: [],
    templates: [child0]
  };
}()));
Ember.TEMPLATES["apps"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "topLevel": null,
          "revision": "Ember@2.1.0+45f524a3",
          "loc": {
            "source": null,
            "start": {
              "line": 20,
              "column": 9
            },
            "end": {
              "line": 20,
              "column": 50
            }
          }
        },
        isEmpty: false,
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode(" ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment,1,1,contextualElement);
          dom.insertBoundary(fragment, null);
          return morphs;
        },
        statements: [
          ["content","app.appName",["loc",[null,[20,35],[20,50]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "topLevel": null,
        "revision": "Ember@2.1.0+45f524a3",
        "loc": {
          "source": null,
          "start": {
            "line": 16,
            "column": 4
          },
          "end": {
            "line": 30,
            "column": 4
          }
        }
      },
      isEmpty: false,
      arity: 1,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("				");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("tr");
        var el2 = dom.createTextNode("\n\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createTextNode("\n						");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-default");
        var el4 = dom.createTextNode("\n							");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("span");
        dom.setAttribute(el4,"class","glyphicon glyphicon-remove");
        dom.setAttribute(el4,"aria-hidden","true");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n						");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n					");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n				");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var element1 = dom.childAt(element0, [11, 1]);
        var morphs = new Array(6);
        morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),0,0);
        morphs[1] = dom.createMorphAt(dom.childAt(element0, [3]),0,0);
        morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),0,0);
        morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),0,0);
        morphs[4] = dom.createMorphAt(dom.childAt(element0, [9]),0,0);
        morphs[5] = dom.createElementMorph(element1);
        return morphs;
      },
      statements: [
        ["content","app.id",["loc",[null,[19,9],[19,19]]]],
        ["block","link-to",["app",["get","app.id",["loc",[null,[20,26],[20,32]]]]],[],0,null,["loc",[null,[20,9],[20,62]]]],
        ["content","app.owner",["loc",[null,[21,9],[21,22]]]],
        ["content","app.appType",["loc",[null,[22,9],[22,24]]]],
        ["content","app.status",["loc",[null,[23,9],[23,23]]]],
        ["element","action",["deletea",["get","app",["loc",[null,[25,33],[25,36]]]]],[],["loc",[null,[25,14],[25,38]]]]
      ],
      locals: ["app"],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 35,
          "column": 6
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","panel panel-default");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment(" Default panel contents ");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","panel-heading");
      var el4 = dom.createTextNode("Applications");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("table");
      dom.setAttribute(el3,"class","table");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("thead");
      var el5 = dom.createTextNode("\n				");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("tr");
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Name");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Owner");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Type");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Status");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n				");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("tbody");
      var el5 = dom.createTextNode("\n");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var morphs = new Array(1);
      morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0, 1, 5, 3]),1,1);
      return morphs;
    },
    statements: [
      ["block","each",[["get","model",["loc",[null,[16,12],[16,17]]]]],[],0,null,["loc",[null,[16,4],[30,13]]]]
    ],
    locals: [],
    templates: [child0]
  };
}()));
Ember.TEMPLATES["logs"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "topLevel": null,
        "revision": "Ember@2.1.0+45f524a3",
        "loc": {
          "source": null,
          "start": {
            "line": 20,
            "column": 4
          },
          "end": {
            "line": 33,
            "column": 4
          }
        }
      },
      isEmpty: false,
      arity: 1,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("				");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("tr");
        var el2 = dom.createTextNode("\n\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createElement("a");
        var el4 = dom.createComment("");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createTextNode("\n						");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-default");
        var el4 = dom.createTextNode("\n							");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("span");
        dom.setAttribute(el4,"class","glyphicon glyphicon-remove");
        dom.setAttribute(el4,"aria-hidden","true");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n						");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n					");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n				");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var element1 = dom.childAt(element0, [3, 0]);
        var element2 = dom.childAt(element0, [9, 1]);
        var morphs = new Array(5);
        morphs[0] = dom.createAttrMorph(element1, 'href');
        morphs[1] = dom.createMorphAt(element1,0,0);
        morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),0,0);
        morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),0,0);
        morphs[4] = dom.createElementMorph(element2);
        return morphs;
      },
      statements: [
        ["attribute","href",["concat",["/rest/logs/",["get","log.name",["loc",[null,[24,31],[24,39]]]]]]],
        ["content","log.name",["loc",[null,[24,43],[24,55]]]],
        ["content","log.lastModified",["loc",[null,[25,9],[25,29]]]],
        ["content","log.length",["loc",[null,[26,9],[26,23]]]],
        ["element","action",["deletea",["get","app",["loc",[null,[28,33],[28,36]]]]],[],["loc",[null,[28,14],[28,38]]]]
      ],
      locals: ["log"],
      templates: []
    };
  }());
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 40,
          "column": 0
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("ul");
      dom.setAttribute(el2,"class","nav nav-tabs");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("li");
      dom.setAttribute(el3,"class","active");
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"href","#");
      var el5 = dom.createTextNode("Common server logs");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("li");
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"href","#");
      var el5 = dom.createTextNode("Localization warnings");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("li");
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"href","#");
      var el5 = dom.createTextNode("Requests logs");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","panel panel-default");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment(" Default panel contents ");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","panel-heading");
      var el4 = dom.createTextNode("Logs");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("table");
      dom.setAttribute(el3,"class","table  table-hover");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("thead");
      var el5 = dom.createTextNode("\n				");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("tr");
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("#");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Name");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Last modified");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Size");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n				");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("tbody");
      var el5 = dom.createTextNode("\n");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var morphs = new Array(1);
      morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0, 3, 5, 3]),1,1);
      return morphs;
    },
    statements: [
      ["block","each",[["get","model",["loc",[null,[20,12],[20,17]]]]],[],0,null,["loc",[null,[20,4],[33,13]]]]
    ],
    locals: [],
    templates: [child0]
  };
}()));
Ember.TEMPLATES["user"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 48,
          "column": 6
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      var el2 = dom.createTextNode("\n	\n   		 ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","page-header");
      var el3 = dom.createTextNode("\n     	   ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      var el4 = dom.createTextNode("User");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("small");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("small");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n  		  ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("form");
      dom.setAttribute(el2,"class","form-horizontal");
      var el3 = dom.createTextNode("\n\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("User	name");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Login");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("E-mail");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Preffered lang");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Status");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			 ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createTextNode("\n					");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("	\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Database login");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("Database default password");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("	\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","form-group");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("label");
      dom.setAttribute(el4,"for","inputEmail");
      dom.setAttribute(el4,"class","control-label col-xs-2");
      var el5 = dom.createTextNode("IsSupervisor");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-xs-5");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("		\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createTextNode("Save & Close");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/users");
      var el5 = dom.createTextNode("Cancel");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [1, 1]);
      var element2 = dom.childAt(element0, [3]);
      var element3 = dom.childAt(element2, [17, 1]);
      var morphs = new Array(11);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1]),0,0);
      morphs[1] = dom.createMorphAt(dom.childAt(element1, [2]),0,0);
      morphs[2] = dom.createMorphAt(dom.childAt(element2, [1, 3]),0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [3, 3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element2, [5, 3]),0,0);
      morphs[5] = dom.createMorphAt(dom.childAt(element2, [7, 3]),0,0);
      morphs[6] = dom.createMorphAt(dom.childAt(element2, [9, 3]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element2, [11, 3]),0,0);
      morphs[8] = dom.createMorphAt(dom.childAt(element2, [13, 3]),0,0);
      morphs[9] = dom.createMorphAt(dom.childAt(element2, [15, 3]),0,0);
      morphs[10] = dom.createElementMorph(element3);
      return morphs;
    },
    statements: [
      ["content","model.login",["loc",[null,[4,24],[4,39]]]],
      ["content","model.regDate",["loc",[null,[4,54],[4,71]]]],
      ["inline","input",[],["class","form-control","name","userName","value",["subexpr","@mut",[["get","model.userName",["loc",[null,[11,76],[11,90]]]]],[],[]],"required",true],["loc",[null,[11,25],[11,106]]]],
      ["inline","input",[],["class","form-control","name","login","value",["subexpr","@mut",[["get","model.login",["loc",[null,[15,73],[15,84]]]]],[],[]],"required",true],["loc",[null,[15,25],[15,100]]]],
      ["inline","input",[],["class","form-control","name","email","value",["subexpr","@mut",[["get","model.email",["loc",[null,[19,73],[19,84]]]]],[],[]],"required",true],["loc",[null,[19,25],[19,100]]]],
      ["inline","input",[],["class","form-control","name","prefflang","value",["subexpr","@mut",[["get","model.prefferedLang",["loc",[null,[23,77],[23,96]]]]],[],[]],"required",true],["loc",[null,[23,25],[23,112]]]],
      ["inline","select-native",[],["class","form-control","content",["subexpr","@mut",[["get","USER_STATUS",["loc",[null,[28,50],[28,61]]]]],[],[]],"value",["subexpr","@mut",[["get","model.status",["loc",[null,[28,68],[28,80]]]]],[],[]]],["loc",[null,[28,5],[28,82]]]],
      ["inline","input",[],["class","form-control","name","dblogin","value",["subexpr","@mut",[["get","model.dbLogin",["loc",[null,[33,75],[33,88]]]]],[],[]],"required",true],["loc",[null,[33,25],[33,104]]]],
      ["inline","input",[],["class","form-control","name","dbdefaultpwd","value",["subexpr","@mut",[["get","model.defaultDbPwd",["loc",[null,[37,80],[37,98]]]]],[],[]],"required",true],["loc",[null,[37,25],[37,114]]]],
      ["inline","input",[],["type","checkbox","name","isSupervisor","checked",["subexpr","@mut",[["get","model.isSupervisor",["loc",[null,[41,77],[41,95]]]]],[],[]]],["loc",[null,[41,25],[41,97]]]],
      ["element","action",["save",["get","this",["loc",[null,[44,50],[44,54]]]]],[],["loc",[null,[44,34],[44,56]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["users"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "topLevel": null,
          "revision": "Ember@2.1.0+45f524a3",
          "loc": {
            "source": null,
            "start": {
              "line": 19,
              "column": 9
            },
            "end": {
              "line": 19,
              "column": 51
            }
          }
        },
        isEmpty: false,
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode(" ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment,1,1,contextualElement);
          dom.insertBoundary(fragment, null);
          return morphs;
        },
        statements: [
          ["content","user.login",["loc",[null,[19,37],[19,51]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "topLevel": null,
        "revision": "Ember@2.1.0+45f524a3",
        "loc": {
          "source": null,
          "start": {
            "line": 16,
            "column": 4
          },
          "end": {
            "line": 29,
            "column": 4
          }
        }
      },
      isEmpty: false,
      arity: 1,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("				");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("tr");
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n					");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("td");
        var el3 = dom.createTextNode("\n						");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-default");
        var el4 = dom.createTextNode("\n							");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("span");
        dom.setAttribute(el4,"class","glyphicon glyphicon-remove");
        dom.setAttribute(el4,"aria-hidden","true");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n						");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n					");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n				");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var element1 = dom.childAt(element0, [11, 1]);
        var morphs = new Array(6);
        morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),0,0);
        morphs[1] = dom.createMorphAt(dom.childAt(element0, [3]),0,0);
        morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),0,0);
        morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),0,0);
        morphs[4] = dom.createMorphAt(dom.childAt(element0, [9]),0,0);
        morphs[5] = dom.createElementMorph(element1);
        return morphs;
      },
      statements: [
        ["content","user.id",["loc",[null,[18,9],[18,20]]]],
        ["block","link-to",["user",["get","user.id",["loc",[null,[19,27],[19,34]]]]],[],0,null,["loc",[null,[19,9],[19,63]]]],
        ["content","user.userName",["loc",[null,[20,9],[20,26]]]],
        ["content","user.email",["loc",[null,[21,9],[21,23]]]],
        ["content","user.status",["loc",[null,[22,9],[22,24]]]],
        ["element","action",["deleteu",["get","user",["loc",[null,[24,33],[24,37]]]]],[],["loc",[null,[24,14],[24,39]]]]
      ],
      locals: ["user"],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 34,
          "column": 0
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","panel panel-default");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment(" Default panel contents ");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","panel-heading");
      var el4 = dom.createTextNode("Users");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("table");
      dom.setAttribute(el3,"class","table");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("thead");
      var el5 = dom.createTextNode("\n				");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("tr");
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Login");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Name");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("E-mail");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("th");
      var el7 = dom.createTextNode("Status");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n				");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("tbody");
      var el5 = dom.createTextNode("\n");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var morphs = new Array(1);
      morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0, 1, 5, 3]),1,1);
      return morphs;
    },
    statements: [
      ["block","each",[["get","model",["loc",[null,[16,12],[16,17]]]]],[],0,null,["loc",[null,[16,4],[29,13]]]]
    ],
    locals: [],
    templates: [child0]
  };
}()));
Ember.TEMPLATES["components/select-native"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "topLevel": null,
        "revision": "Ember@2.1.0+45f524a3",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 5,
            "column": 0
          }
        }
      },
      isEmpty: false,
      arity: 1,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("option");
        var el2 = dom.createTextNode("\n        ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n    ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var morphs = new Array(2);
        morphs[0] = dom.createAttrMorph(element0, 'value');
        morphs[1] = dom.createMorphAt(element0,1,1);
        return morphs;
      },
      statements: [
        ["attribute","value",["concat",[["get","it.value",["loc",[null,[2,21],[2,29]]]]]]],
        ["content","it.label",["loc",[null,[3,8],[3,20]]]]
      ],
      locals: ["it"],
      templates: []
    };
  }());
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 6,
          "column": 0
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createComment("");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var morphs = new Array(1);
      morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
      dom.insertBoundary(fragment, 0);
      dom.insertBoundary(fragment, null);
      return morphs;
    },
    statements: [
      ["block","each",[["get","content",["loc",[null,[1,8],[1,15]]]]],[],0,null,["loc",[null,[1,0],[5,9]]]]
    ],
    locals: [],
    templates: [child0]
  };
}()));
Ember.TEMPLATES["components/select-user-status"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "topLevel": null,
        "revision": "Ember@2.1.0+45f524a3",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 6,
            "column": 0
          }
        }
      },
      isEmpty: false,
      arity: 2,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("button");
        dom.setAttribute(el1,"type","button");
        dom.setAttribute(el1,"class","btn");
        var el2 = dom.createTextNode("\n       \n        ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("span");
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n    ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var morphs = new Array(3);
        morphs[0] = dom.createAttrMorph(element0, 'data-type');
        morphs[1] = dom.createElementMorph(element0);
        morphs[2] = dom.createMorphAt(dom.childAt(element0, [1]),0,0);
        return morphs;
      },
      statements: [
        ["attribute","data-type",["get","type.name",["loc",[null,[2,38],[2,47]]]]],
        ["element","action",["toggle",["get","type.name",["loc",[null,[2,80],[2,89]]]]],[],["loc",[null,[2,62],[2,91]]]],
        ["content","type.name",["loc",[null,[4,14],[4,27]]]]
      ],
      locals: ["index","type"],
      templates: []
    };
  }());
  return {
    meta: {
      "topLevel": null,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 7,
          "column": 0
        }
      }
    },
    isEmpty: false,
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createComment("");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var morphs = new Array(1);
      morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
      dom.insertBoundary(fragment, 0);
      dom.insertBoundary(fragment, null);
      return morphs;
    },
    statements: [
      ["block","each-in",[["get","types",["loc",[null,[1,11],[1,16]]]]],[],0,null,["loc",[null,[1,0],[6,12]]]]
    ],
    locals: [],
    templates: [child0]
  };
}()));