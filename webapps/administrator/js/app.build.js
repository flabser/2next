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
        	console.log("upda5te user");

            user.save();
            this.transitionTo('users');
        }
    }
});

AdminApp.UsersController = Ember.Controller.extend({


    actions: {
        selectAll: function() {},
        deleteu: function(obj) {
        	console.log(obj.id);            
            obj.deleteRecord();
            obj.save();
        },
        isCompleted: function(key, value) {
        	console.log("ddd")
        }
    }
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
    isSupervisor: DS.attr('number'),

    roles: DS.hasMany('role', {
        async: true
      })
});

AdminApp.User.FIXTURES = [{
    id: "3",
    login: " bug",
    pwd: "123",
    email: "dddd",
    role: "role"
}, {
    id: "4",
    login: " player",
    pwd: "123",
    email: "dddd",
    role: "role"
}, {
    id: "5",
    login: "Fix",
    pwd: "123",
    email: "dddd",
    role: "role22"
}];

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
          "line": 50,
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
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("h1");
      var el3 = dom.createTextNode("Application ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","action-bar");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("button");
      dom.setAttribute(el3,"class","btn btn-primary");
      var el4 = dom.createTextNode("Save & Close");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"class","btn");
      dom.setAttribute(el3,"href","#/apps");
      var el4 = dom.createTextNode("Cancel");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("\n            Application name:\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("\n         Owner:\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createTextNode("\n         ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("\n            Database host:\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("\n         Database login:\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createTextNode("\n         ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("\n            Database name:\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n \n     \n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [3, 1]);
      var morphs = new Array(7);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
      morphs[1] = dom.createElementMorph(element1);
      morphs[2] = dom.createMorphAt(dom.childAt(element0, [5, 3]),1,1);
      morphs[3] = dom.createMorphAt(dom.childAt(element0, [7, 3]),1,1);
      morphs[4] = dom.createMorphAt(dom.childAt(element0, [9, 3]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element0, [11, 3]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element0, [13, 3]),1,1);
      return morphs;
    },
    statements: [
      ["content","model.appName",["loc",[null,[2,16],[2,33]]]],
      ["element","action",["save",["get","this",["loc",[null,[4,56],[4,60]]]]],[],["loc",[null,[4,40],[4,62]]]],
      ["inline","input",[],["name","appName","value",["subexpr","@mut",[["get","model.appName",["loc",[null,[13,41],[13,54]]]]],[],[]],"required",true],["loc",[null,[13,12],[13,70]]]],
      ["inline","input",[],["name","owner","value",["subexpr","@mut",[["get","model.owner",["loc",[null,[21,36],[21,47]]]]],[],[]],"required",true],["loc",[null,[21,9],[21,64]]]],
      ["inline","input",[],["name","dbHost","value",["subexpr","@mut",[["get","model.dbHost",["loc",[null,[29,40],[29,52]]]]],[],[]],"required",true],["loc",[null,[29,12],[29,69]]]],
      ["inline","input",[],["name","dbLogin","value",["subexpr","@mut",[["get","model.dbLogin",["loc",[null,[37,38],[37,51]]]]],[],[]],"required",true],["loc",[null,[37,9],[37,68]]]],
      ["inline","input",[],["name","dbName","value",["subexpr","@mut",[["get","model.dbName",["loc",[null,[45,40],[45,52]]]]],[],[]],"required",true],["loc",[null,[45,12],[45,69]]]]
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
              "line": 36,
              "column": 20
            },
            "end": {
              "line": 36,
              "column": 56
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
          ["content","m.title",["loc",[null,[36,44],[36,55]]]]
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
            "line": 34,
            "column": 16
          },
          "end": {
            "line": 38,
            "column": 16
          }
        }
      },
      isEmpty: false,
      arity: 1,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("li");
        dom.setAttribute(el1,"role","presentation");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                ");
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
        ["block","link-to",[["get","m.viewdata",["loc",[null,[36,31],[36,41]]]]],[],0,null,["loc",[null,[36,20],[36,68]]]]
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
          "line": 49,
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
      dom.setAttribute(el1,"class","container");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","header");
      var el3 = dom.createTextNode("\n       ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("nav");
      dom.setAttribute(el3,"class","navbar navbar-default");
      var el4 = dom.createTextNode("\n  ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","container-fluid");
      var el5 = dom.createTextNode("\n    ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","navbar-header");
      var el6 = dom.createTextNode("\n     ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("a");
      dom.setAttribute(el6,"class","navbar-brand");
      dom.setAttribute(el6,"href","page/about");
      var el7 = dom.createTextNode("\n						");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("img");
      dom.setAttribute(el7,"src","../img/logo.png");
      dom.setAttribute(el7,"alt","logo");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("2Next\n					");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n    ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n\n    ");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment(" Collect the nav links, forms, and other content for toggling ");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n    ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","collapse navbar-collapse");
      dom.setAttribute(el5,"id","bs-example-navbar-collapse-1");
      var el6 = dom.createTextNode("\n     \n\n      ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("ul");
      dom.setAttribute(el6,"class","nav navbar-nav navbar-right");
      var el7 = dom.createTextNode("\n        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("li");
      dom.setAttribute(el7,"class","dropdown");
      var el8 = dom.createTextNode("\n          ");
      dom.appendChild(el7, el8);
      var el8 = dom.createElement("a");
      dom.setAttribute(el8,"href","#");
      dom.setAttribute(el8,"class","dropdown-toggle");
      dom.setAttribute(el8,"data-toggle","dropdown");
      dom.setAttribute(el8,"role","button");
      dom.setAttribute(el8,"aria-haspopup","true");
      dom.setAttribute(el8,"aria-expanded","false");
      var el9 = dom.createComment("");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("span");
      dom.setAttribute(el9,"class","caret");
      dom.appendChild(el8, el9);
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n          ");
      dom.appendChild(el7, el8);
      var el8 = dom.createElement("ul");
      dom.setAttribute(el8,"class","dropdown-menu");
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("li");
      var el10 = dom.createElement("a");
      dom.setAttribute(el10,"href","#");
      var el11 = dom.createTextNode("Action");
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      dom.appendChild(el8, el9);
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("li");
      var el10 = dom.createElement("a");
      dom.setAttribute(el10,"href","#");
      var el11 = dom.createTextNode("Another action");
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      dom.appendChild(el8, el9);
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("li");
      var el10 = dom.createElement("a");
      dom.setAttribute(el10,"href","#");
      var el11 = dom.createTextNode("Something else here");
      dom.appendChild(el10, el11);
      dom.appendChild(el9, el10);
      dom.appendChild(el8, el9);
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("li");
      dom.setAttribute(el9,"role","separator");
      dom.setAttribute(el9,"class","divider");
      dom.appendChild(el8, el9);
      var el9 = dom.createTextNode("\n            ");
      dom.appendChild(el8, el9);
      var el9 = dom.createElement("li");
      var el10 = dom.createElement("a");
      dom.setAttribute(el10,"href","#");
      var el11 = dom.createTextNode("Separated link");
      dom.appendChild(el10, el11);
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
      var el6 = dom.createTextNode("\n    ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createComment(" /.navbar-collapse ");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n  ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" /.container-fluid ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("aside");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","outline");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("ul");
      dom.setAttribute(el4,"class","nav nav-pills nav-stacked");
      var el5 = dom.createTextNode("\n");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("aside");
      dom.setAttribute(el2,"class","view");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","footer");
      var el3 = dom.createTextNode("\n       2Next\n    ");
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
      var element0 = dom.childAt(fragment, [0]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1, 1, 1, 5, 1, 1, 1]),0,0);
      morphs[1] = dom.createMorphAt(dom.childAt(element0, [3, 1, 1]),1,1);
      morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),1,1);
      return morphs;
    },
    statements: [
      ["content","model.userProfile.login",["loc",[null,[17,126],[17,153]]]],
      ["block","each",[["get","model",["loc",[null,[34,24],[34,29]]]]],[],0,null,["loc",[null,[34,16],[38,25]]]],
      ["content","outlet",["loc",[null,[43,8],[43,18]]]]
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
    var child0 = (function() {
      return {
        meta: {
          "topLevel": null,
          "revision": "Ember@2.1.0+45f524a3",
          "loc": {
            "source": null,
            "start": {
              "line": 11,
              "column": 8
            },
            "end": {
              "line": 11,
              "column": 115
            }
          }
        },
        isEmpty: false,
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode(" name: ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode(", last modified");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode(" , length ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode(" mB ");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(3);
          morphs[0] = dom.createMorphAt(fragment,1,1,contextualElement);
          morphs[1] = dom.createMorphAt(fragment,3,3,contextualElement);
          morphs[2] = dom.createMorphAt(fragment,5,5,contextualElement);
          return morphs;
        },
        statements: [
          ["content","log.name",["loc",[null,[11,40],[11,52]]]],
          ["content","log.lastModified",["loc",[null,[11,67],[11,87]]]],
          ["content","log.length",["loc",[null,[11,97],[11,111]]]]
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
            "line": 6,
            "column": 0
          },
          "end": {
            "line": 12,
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
        var el1 = dom.createElement("div");
        var el2 = dom.createTextNode("\n        ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("label");
        dom.setAttribute(el2,"class","entry-select");
        var el3 = dom.createTextNode("\n            ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("input");
        dom.setAttribute(el3,"type","checkbox");
        dom.setAttribute(el3,"name","docid");
        dom.setAttribute(el3,"value","{@docid}");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n        ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n        ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1]),3,3);
        return morphs;
      },
      statements: [
        ["block","link-to",["log",["get","log.id",["loc",[null,[11,25],[11,31]]]]],[],0,null,["loc",[null,[11,8],[11,127]]]]
      ],
      locals: ["log"],
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
          "line": 14,
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
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("h3");
      var el3 = dom.createTextNode("Logs");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","action-bar");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"class","btn");
      dom.setAttribute(el3,"href","#/logs/delete");
      var el4 = dom.createTextNode("Delete log");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var morphs = new Array(1);
      morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0]),5,5);
      return morphs;
    },
    statements: [
      ["block","each",[["get","model",["loc",[null,[6,8],[6,13]]]]],[],0,null,["loc",[null,[6,0],[12,9]]]]
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
          "line": 47,
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
      var el3 = dom.createTextNode("User ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n\n\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","input-group");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("span");
      dom.setAttribute(el3,"class","input-group-addon");
      dom.setAttribute(el3,"id","basic-addon1");
      var el4 = dom.createTextNode("User name:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","input-group");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("span");
      dom.setAttribute(el3,"class","input-group-addon");
      dom.setAttribute(el3,"id","basic-addon1");
      var el4 = dom.createTextNode("Login:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","input-group");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("span");
      dom.setAttribute(el3,"class","input-group-addon");
      dom.setAttribute(el3,"id","basic-addon2");
      var el4 = dom.createTextNode("E-mail:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","input-group");
      var el3 = dom.createTextNode("\n		 ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("span");
      dom.setAttribute(el3,"class","input-group-addon");
      dom.setAttribute(el3,"id","basic-addon2");
      var el4 = dom.createTextNode(" Preffered lang:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n\n\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("Status:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("RegDate:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("IsSupervisor:");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","controls");
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n	");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","action-bar");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("button");
      dom.setAttribute(el3,"class","btn btn-primary");
      var el4 = dom.createTextNode("Save &\n			Close");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"class","btn");
      dom.setAttribute(el3,"href","#/users");
      var el4 = dom.createTextNode("Cancel");
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
      var element1 = dom.childAt(element0, [17, 1]);
      var morphs = new Array(9);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element0, [3]),3,3);
      morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),3,3);
      morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),3,3);
      morphs[4] = dom.createMorphAt(dom.childAt(element0, [9]),3,3);
      morphs[5] = dom.createMorphAt(dom.childAt(element0, [11, 3]),0,0);
      morphs[6] = dom.createMorphAt(dom.childAt(element0, [13, 3]),0,0);
      morphs[7] = dom.createMorphAt(dom.childAt(element0, [15, 3]),0,0);
      morphs[8] = dom.createElementMorph(element1);
      return morphs;
    },
    statements: [
      ["content","model.login",["loc",[null,[2,10],[2,25]]]],
      ["inline","input",[],["class","form-control","aria-describedby","basic-addon1","name","userName","value",["subexpr","@mut",[["get","model.userName",["loc",[null,[7,85],[7,99]]]]],[],[]],"required",true],["loc",[null,[7,2],[7,115]]]],
      ["inline","input",[],["class","form-control","aria-describedby","basic-addon1","name","login","value",["subexpr","@mut",[["get","model.login",["loc",[null,[12,82],[12,93]]]]],[],[]],"required",true],["loc",[null,[12,2],[12,109]]]],
      ["inline","input",[],["class","form-control","aria-describedby","basic-addon1","name","email","value",["subexpr","@mut",[["get","model.email",["loc",[null,[17,82],[17,93]]]]],[],[]],"required",true],["loc",[null,[17,2],[17,109]]]],
      ["inline","input",[],["class","form-control","aria-describedby","basic-addon1","name","prefflang","value",["subexpr","@mut",[["get","model.prefferedLang",["loc",[null,[21,86],[21,105]]]]],[],[]],"required",true],["loc",[null,[21,2],[21,121]]]],
      ["inline","select-user-status",[],["value",["subexpr","@mut",[["get","model.status",["loc",[null,[27,51],[27,63]]]]],[],[]]],["loc",[null,[27,24],[27,65]]]],
      ["inline","input",[],["name","regDate","value",["subexpr","@mut",[["get","model.regDate",["loc",[null,[33,53],[33,66]]]]],[],[]],"disabled",true],["loc",[null,[33,24],[34,18]]]],
      ["inline","input",[],["type","checkbox","name","isSupervisor","checked",["subexpr","@mut",[["get","model.isSupervisor",["loc",[null,[39,31],[39,49]]]]],[],[]]],["loc",[null,[38,24],[39,52]]]],
      ["element","action",["save",["get","this",["loc",[null,[43,49],[43,53]]]]],[],["loc",[null,[43,33],[43,55]]]]
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