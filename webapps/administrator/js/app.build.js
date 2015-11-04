AdminApp = Ember.Application.create();

var baseURL =  DS.RESTAdapter.extend({
    namespace: 'rest'
});

AdminApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        	console.log("type=" + type);
           return type + 's';

    }
});

AdminApp.Router.map(function(){
	this.resource('users', {
	    path: '/'
	  });
	this.route('logs');
	this.route('app', {path: '/app/:app_id'});
	this.route('apps');
	this.route('user', {path: '/users/:user_id'});
	this.route('users');
	this.route('newUser', {path: '/users/new'});
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
        selectAll: function() {}
    }
});



AdminApp.App = DS.Model.extend({
    appName: DS.attr('string'),
    owner: DS.attr('string'),
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
        return this.store.find('app');
    }
});

AdminApp.UsersNewRoute = Ember.Route.extend({
    templateName: 'app'
});

AdminApp.LogsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('log');
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

AdminApp.UsersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user');
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
          "line": 55,
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
      var el2 = dom.createTextNode("\n    ");
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
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","control-group");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-label");
      var el4 = dom.createTextNode("\n            Database password:\n        ");
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
      var el2 = dom.createTextNode("   \n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [3, 1]);
      var morphs = new Array(8);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
      morphs[1] = dom.createElementMorph(element1);
      morphs[2] = dom.createMorphAt(dom.childAt(element0, [5, 3]),1,1);
      morphs[3] = dom.createMorphAt(dom.childAt(element0, [7, 3]),1,1);
      morphs[4] = dom.createMorphAt(dom.childAt(element0, [9, 3]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element0, [11, 3]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element0, [13, 3]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element0, [15, 3]),1,1);
      return morphs;
    },
    statements: [
      ["content","appName",["loc",[null,[2,16],[2,27]]]],
      ["element","action",["save",["get","this",["loc",[null,[4,56],[4,60]]]]],[],["loc",[null,[4,40],[4,62]]]],
      ["inline","input",[],["name","appName","value",["subexpr","@mut",[["get","appName",["loc",[null,[12,41],[12,48]]]]],[],[]],"required",true],["loc",[null,[12,12],[12,64]]]],
      ["inline","input",[],["name","owner","value",["subexpr","@mut",[["get","owner",["loc",[null,[20,36],[20,41]]]]],[],[]],"required",true],["loc",[null,[20,9],[20,58]]]],
      ["inline","input",[],["name","dbHost","value",["subexpr","@mut",[["get","dbHost",["loc",[null,[28,40],[28,46]]]]],[],[]],"required",true],["loc",[null,[28,12],[28,63]]]],
      ["inline","input",[],["name","dbLogin","value",["subexpr","@mut",[["get","dbLogin",["loc",[null,[36,38],[36,45]]]]],[],[]],"required",true],["loc",[null,[36,9],[36,62]]]],
      ["inline","input",[],["name","dbName","value",["subexpr","@mut",[["get","dbName",["loc",[null,[44,40],[44,46]]]]],[],[]],"required",true],["loc",[null,[44,12],[44,63]]]],
      ["inline","input",[],["name","dbPwd","value",["subexpr","@mut",[["get","dbPwd",["loc",[null,[52,39],[52,44]]]]],[],[]],"required",true],["loc",[null,[52,12],[52,61]]]]
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
              "line": 11,
              "column": 20
            },
            "end": {
              "line": 11,
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
          ["content","m.title",["loc",[null,[11,44],[11,55]]]]
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
            "line": 9,
            "column": 16
          },
          "end": {
            "line": 13,
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
        ["block","link-to",[["get","m.viewdata",["loc",[null,[11,31],[11,41]]]]],[],0,null,["loc",[null,[11,20],[11,68]]]]
      ],
      locals: ["m"],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "topLevel": false,
      "revision": "Ember@2.1.0+45f524a3",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 24,
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
      var el2 = dom.createTextNode("application");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","container");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","header");
      var el3 = dom.createTextNode("\n        Administrator\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("aside");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","nav");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("ul");
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
      var element0 = dom.childAt(fragment, [2]);
      var morphs = new Array(2);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [3, 1, 1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element0, [5]),1,1);
      return morphs;
    },
    statements: [
      ["block","each",[["get","model",["loc",[null,[9,24],[9,29]]]]],[],0,null,["loc",[null,[9,16],[13,25]]]],
      ["content","outlet",["loc",[null,[18,8],[18,18]]]]
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
              "line": 11,
              "column": 8
            },
            "end": {
              "line": 11,
              "column": 66
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
          var el1 = dom.createTextNode(" - ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode(" ");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(2);
          morphs[0] = dom.createMorphAt(fragment,1,1,contextualElement);
          morphs[1] = dom.createMorphAt(fragment,3,3,contextualElement);
          return morphs;
        },
        statements: [
          ["content","app.appName",["loc",[null,[11,34],[11,49]]]],
          ["content","app.owner",["loc",[null,[11,52],[11,65]]]]
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
        ["block","link-to",["app",["get","app.id",["loc",[null,[11,25],[11,31]]]]],[],0,null,["loc",[null,[11,8],[11,78]]]]
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
      var el3 = dom.createTextNode("Applications");
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
      dom.setAttribute(el3,"href","#/apps/new");
      var el4 = dom.createTextNode("Add");
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
          "line": 63,
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
      var el3 = dom.createTextNode("User ");
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
      dom.setAttribute(el3,"href","#/users");
      var el4 = dom.createTextNode("Cancel");
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
      var el4 = dom.createTextNode("\n            User name:\n        ");
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
      var el4 = dom.createTextNode("\n         Login:\n        ");
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
      var el4 = dom.createTextNode("\n            Password:\n        ");
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
      var el4 = dom.createTextNode("\n         E-mail:\n        ");
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
      var el4 = dom.createTextNode("\n            Status:\n        ");
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
      var el4 = dom.createTextNode("\n            RegDate:\n        ");
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
      var el4 = dom.createTextNode("\n            IsSupervisor:\n        ");
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
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [3, 1]);
      var morphs = new Array(9);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
      morphs[1] = dom.createElementMorph(element1);
      morphs[2] = dom.createMorphAt(dom.childAt(element0, [5, 3]),1,1);
      morphs[3] = dom.createMorphAt(dom.childAt(element0, [7, 3]),1,1);
      morphs[4] = dom.createMorphAt(dom.childAt(element0, [9, 3]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element0, [11, 3]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element0, [13, 3]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element0, [15, 3]),1,1);
      morphs[8] = dom.createMorphAt(dom.childAt(element0, [17, 3]),1,1);
      return morphs;
    },
    statements: [
      ["content","login",["loc",[null,[2,9],[2,18]]]],
      ["element","action",["save",["get","this",["loc",[null,[4,56],[4,60]]]]],[],["loc",[null,[4,40],[4,62]]]],
      ["inline","input",[],["name","userName","value",["subexpr","@mut",[["get","userName",["loc",[null,[12,42],[12,50]]]]],[],[]],"required",true],["loc",[null,[12,12],[12,66]]]],
      ["inline","input",[],["name","login","value",["subexpr","@mut",[["get","login",["loc",[null,[20,36],[20,41]]]]],[],[]],"required",true],["loc",[null,[20,9],[20,58]]]],
      ["inline","input",[],["name","pwd","value",["subexpr","@mut",[["get","pwd",["loc",[null,[28,37],[28,40]]]]],[],[]],"required",true],["loc",[null,[28,12],[28,57]]]],
      ["inline","input",[],["name","email","value",["subexpr","@mut",[["get","email",["loc",[null,[36,36],[36,41]]]]],[],[]],"required",true],["loc",[null,[36,9],[36,58]]]],
      ["inline","input",[],["name","status","value",["subexpr","@mut",[["get","status",["loc",[null,[44,40],[44,46]]]]],[],[]],"required",true],["loc",[null,[44,12],[44,63]]]],
      ["inline","input",[],["name","regDate","value",["subexpr","@mut",[["get","regDate",["loc",[null,[52,41],[52,48]]]]],[],[]],"required",true],["loc",[null,[52,12],[52,65]]]],
      ["inline","input",[],["name","isSupervisor","value",["subexpr","@mut",[["get","isSupervisor",["loc",[null,[60,46],[60,58]]]]],[],[]],"required",true],["loc",[null,[60,12],[60,74]]]]
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
              "line": 11,
              "column": 8
            },
            "end": {
              "line": 11,
              "column": 70
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
          var el1 = dom.createTextNode("  ");
          dom.appendChild(el0, el1);
          var el1 = dom.createComment("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode(" ");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(2);
          morphs[0] = dom.createMorphAt(fragment,1,1,contextualElement);
          morphs[1] = dom.createMorphAt(fragment,3,3,contextualElement);
          return morphs;
        },
        statements: [
          ["content","user.login",["loc",[null,[11,36],[11,50]]]],
          ["content","user.userName",["loc",[null,[11,52],[11,69]]]]
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
        ["block","link-to",["user",["get","user.id",["loc",[null,[11,26],[11,33]]]]],[],0,null,["loc",[null,[11,8],[11,82]]]]
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
      var el3 = dom.createTextNode("Users");
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
      dom.setAttribute(el3,"href","#/users/new");
      var el4 = dom.createTextNode("new user");
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