MyApp = Ember.Application.create();

//App.ApplicationAdapter = DS.FixtureAdapter;
//
var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

var baseURL = host.extend({
    namespace: 'Administrator/rest'
});

MyApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        	  console.log("type=" + type);
        switch (type) {
            case 'authUser':
                return 'session';
            default:
                return type + 's';
        }
    }
});

MyApp = Ember.Application.create();

var host = DS.RESTAdapter.extend({
    host: 'http://localhost:38779',
});

var baseURL = host.extend({
    namespace: 'Administrator/rest'
});

MyApp.ApplicationAdapter = baseURL.extend({
    pathForType: function(type) {
        	console.log("type=" + type);
            return 'session';

    }
});


MyApp.Router.map(function(){
	this.route('login',{path: '/'});
	this.route('login_failed');

});

MyApp.LoginController = Ember.ObjectController.extend({
	model: {},

	  actions: {
		  login: function(){
		  console.log(this);
		  var c = this;
			 console.log(this.get('username'));
	     var signin = this.store.createRecord('auth_user', {
			        login: this.get('username'),
			        pwd: this.get('password')
	      })

	     signin.save().then(function(user) {
	    	 location.href = 'application.html'
	    	}, function(response) {
	    	    c.transitionToRoute('login_failed');
	    	});
	      console.log('end');
	    }
	  }
});


MyApp.AuthUser = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    roles: DS.attr('string'),
    status:DS.attr('string'),
    error:DS.attr('string')
});


MyApp.Router.map(function(){
//	this.route('signin', {path:'/'});
	this.route('users');
	this.route('outline');
		
});


MyApp.LoginController = Ember.ObjectController.extend({
	model: {},

	  actions: {
		  login: function(){
		  console.log(this);
		  var c = this;
			  console.log(this.get('username'));
	  		console.log(this.get('password'));
	     var signin = this.store.createRecord('auth_user', {
			        login: this.get('username'),	    	  	
			        pwd: this.get('password')
	      })
	      
	     signin.save().then(function(user) {
	    	  console.log("Post saved.");
	    	  console.log(c);
	    	 // c.get('outline').send('actionopen', event);
	    	 // c.transitionToRoute('outline');
	    	 location.href = 'application.html'
	    	}, function(response) {
	    	  console.error("Post not saved!" );
	    	});
	      console.log('end');
	    }	 
	  }	
});
// export default loginController;


/* login: function() {
console.log("login");
var users = this.store.find('user',{ username: this.get('user')})
// var users = this.store.find('user');
console.log(users);
users.then(function loggedIn(){
	  console.log("login2");
	  var currentUser = users.get('firstObject');
	  this.session.set('user', currentUser);

	  var previousTransition = this.get('previousTransition');
	  if(previousTransition){
		  this.set('previousTransition',null);
		  console.log('retrying');
		  previousTransition.retry();
	  }else{
		  this.transitionToRoute('index');
	  }
}.bind(this));
}*/
MyApp.UsersController = Ember.ObjectController.extend({
    	model: function(params) {
    	  console.log("get users");
    	  return this.store.find('user');
      }


});

MyApp.AuthUser = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    roles: DS.attr('string'),
    status:DS.attr('string'),
    error:DS.attr('string')
});

MyApp.Signin = DS.Model.extend({
	login:DS.attr('string'),
	pwd:DS.attr('string'),
	status:DS.attr('string'),
	error:DS.attr('string')
});


MyApp.User = DS.Model.extend({
	login:DS.attr('string'),
	userName:DS.attr('string')
});


MyApp.UsersRoute = Ember.Route.extend({
/*
	 renderTemplate: function() {
        var controller = this.controllerFor('users');

        // Render the `favoritePost` template into
        // the outlet `posts`, and use the `favoritePost`
        // controller.
        this.render('users_list', {
          outlet: 'viewOutlet',
          controller: controller
        });
      }
*/

      model: function(params) {
          	  console.log("router >get users");
          	  return this.store.find('user');
            }

});

MyApp.ApplicationView = Ember.View.extend({
  templateName: 'application'
})
MyApp.ContentView = Ember.View.extend({
  templateName: 'content'
})
MyApp.FooterView = Ember.View.extend({
  templateName: 'footer'
})
Ember.TEMPLATES["application"] = Ember.HTMLBars.template((function() {
  return {
    isHTMLBars: true,
    revision: "Ember@1.12.1",
    blockParams: 0,
    cachedFragment: null,
    hasRendered: false,
    build: function build(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"class","layout_content");
      dom.setAttribute(el1,"id","content");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    render: function render(context, env, contextualElement) {
      var dom = env.dom;
      var hooks = env.hooks, content = hooks.content;
      dom.detectNamespace(contextualElement);
      var fragment;
      if (env.useFragmentCache && dom.canClone) {
        if (this.cachedFragment === null) {
          fragment = this.build(dom);
          if (this.hasRendered) {
            this.cachedFragment = fragment;
          } else {
            this.hasRendered = true;
          }
        }
        if (this.cachedFragment) {
          fragment = dom.cloneNode(this.cachedFragment, true);
        }
      } else {
        fragment = this.build(dom);
      }
      var morph0 = dom.createMorphAt(dom.childAt(fragment, [1]),1,1);
      content(env, morph0, context, "outlet");
      return fragment;
    }
  };
}()));
Ember.TEMPLATES["outline"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      isHTMLBars: true,
      revision: "Ember@1.12.1",
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                            ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("i");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("Users\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        return fragment;
      }
    };
  }());
  return {
    isHTMLBars: true,
    revision: "Ember@1.12.1",
    blockParams: 0,
    cachedFragment: null,
    hasRendered: false,
    build: function build(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createComment("<div class=\"content-overlay\" id=\"content-overlay\" {{action 'hideOpenedNav' on='mouseDown' }}></div>");
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createComment("<header class=\"layout_header\">\n    <div class=\"main-header\">\n        <div class=\"head-item head-nav-app-toggle\" {{action 'navAppMenuToggle'}}>\n            <div class=\"nav-app-toggle\"></div>\n        </div>\n        <div class=\"head-item brand\">\n            <img alt=\"logo\" src=\"img/2next.png\" class=\"brand-logo\" />\n            <span class=\"brand-title\">2Next-Administrator</span>\n        </div>\n        <div class=\"head-item head-nav-ws-toggle\">\n            <div class=\"nav-ws-toggle\" {{action 'navUserMenuToggle' on='mouseDown' }}></div>\n        </div>\n        <div class=\"head-item nav-search-toggle\" id=\"toggle-head-search\" {{action 'toggleSearchForm' on='mouseDown' }}>\n            <i class=\"fa fa-search\"></i>\n        </div>\n        <div class=\"head-item nav-search\" id=\"search-block\">\n            <div class=\"search-toggle-back\" {{action 'toggleSearchForm' on='mouseDown' }}>\n                <i class=\"fa fa-chevron-left\"></i>\n            </div>\n            <form action=\"Provider\" method=\"GET\" name=\"search\">\n                <input type=\"hidden\" name=\"type\" value=\"page\" />\n                <input type=\"hidden\" name=\"id\" value=\"search\" />\n                <input type=\"search\" name=\"keyword\" value=\"\" class=\"search-keyword\" required=\"required\" placeholder=\"Поиск\" />\n                <button type=\"submit\" class=\"search-btn\">\n                    <i class=\"fa fa-search\"></i>\n                </button>\n            </form>\n        </div>\n        <a class=\"no-desktop head-item nav-action\" href=\"#\" data-action=\"add_new\">\n            <i class=\"fa fa-plus\"></i>\n            <span class=\"action-label\"></span>\n        </a>\n    </div>\n</header>");
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("aside");
      dom.setAttribute(el1,"class","nav");
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("    <div class=\"side\">");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n   ");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("     <div class=\"side-container\">");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("            <div class=\"side-nav\">");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n                ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("ul");
      var el3 = dom.createTextNode("\n                    ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("li");
      var el4 = dom.createTextNode("\n");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("                    ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n                    ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("   <li class=\"side-tree-item\">\n{{#link-to 'accounts'}}\n                            <i class=\"fa fa-file-o\"></i>Accounts\n                        {{/link-to}}                    </li>\n                    <li class=\"side-tree-item\">\n{{#link-to 'categories'}}\n                            <i class=\"fa fa-file-o\"></i>Categories\n                        {{/link-to}}                    </li>\n                    <li class=\"side-tree-item\">\n{{#link-to 'cost_centers'}}\n                            <i class=\"fa fa-file-o\"></i>Cost centers\n                        {{/link-to}}                    </li>\n                    <li class=\"side-tree-item\">\n{{#link-to 'tags'}}\n                            <i class=\"fa fa-file-o\"></i>Tags\n                        {{/link-to}}                    </li>\n                    <li class=\"side-tree-item\">\n{{#link-to 'users'}}\n                            <i class=\"fa fa-users\"></i>Users\n                        {{/link-to}}");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n                ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("            </div>");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n  ");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("      </div>");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("    </div>");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("aside");
      dom.setAttribute(el1,"class","body");
      var el2 = dom.createTextNode("\n    View\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createComment("\n<aside class=\"layout_aside nav-ws\">\n    <header>\n        <a href=\"#/userprofile\" class=\"user\">\n            <i class=\"fa fa-user\"></i>\n            <span>{{username}}</span>\n        </a>\n    </header>\n    <footer>\n        <a href=\"Logout\" class=\"ws-exit\">\n            <i class=\"fa fa-sign-out\"></i>\n            <span>{{logout}}</span>\n        </a>\n    </footer>\n</aside>\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    render: function render(context, env, contextualElement) {
      var dom = env.dom;
      var hooks = env.hooks, block = hooks.block, inline = hooks.inline;
      dom.detectNamespace(contextualElement);
      var fragment;
      if (env.useFragmentCache && dom.canClone) {
        if (this.cachedFragment === null) {
          fragment = this.build(dom);
          if (this.hasRendered) {
            this.cachedFragment = fragment;
          } else {
            this.hasRendered = true;
          }
        }
        if (this.cachedFragment) {
          fragment = dom.cloneNode(this.cachedFragment, true);
        }
      } else {
        fragment = this.build(dom);
      }
      var morph0 = dom.createMorphAt(dom.childAt(fragment, [4, 7, 1]),1,1);
      var morph1 = dom.createMorphAt(dom.childAt(fragment, [6]),1,1);
      block(env, morph0, context, "link-to", ["users"], {}, child0, null);
      inline(env, morph1, context, "outlet", ["viewOutlet"], {});
      return fragment;
    }
  };
}()));
Ember.TEMPLATES["signin"] = Ember.HTMLBars.template((function() {
  return {
    isHTMLBars: true,
    revision: "Ember@1.12.1",
    blockParams: 0,
    cachedFragment: null,
    hasRendered: false,
    build: function build(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("ul");
      var el2 = dom.createTextNode("\n\n	");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","center");
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("section");
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","col-logo");
      var el5 = dom.createTextNode("\n				");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("img");
      dom.setAttribute(el5,"src","img/2next.png");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n			");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n			");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      var el5 = dom.createTextNode("\n				");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("h1");
      var el7 = dom.createTextNode("Administrator");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("p");
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("p");
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n					");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("p");
      var el7 = dom.createTextNode("\n						");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("input");
      dom.setAttribute(el7,"type","submit");
      dom.setAttribute(el7,"value","Login");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n					");
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
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","clearfix");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n		");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n		");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("footer");
      var el4 = dom.createTextNode(" version 1.5 Copyright © The F team developers\n			2015 ");
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
    render: function render(context, env, contextualElement) {
      var dom = env.dom;
      var hooks = env.hooks, get = hooks.get, inline = hooks.inline, element = hooks.element;
      dom.detectNamespace(contextualElement);
      var fragment;
      if (env.useFragmentCache && dom.canClone) {
        if (this.cachedFragment === null) {
          fragment = this.build(dom);
          if (this.hasRendered) {
            this.cachedFragment = fragment;
          } else {
            this.hasRendered = true;
          }
        }
        if (this.cachedFragment) {
          fragment = dom.cloneNode(this.cachedFragment, true);
        }
      } else {
        fragment = this.build(dom);
      }
      var element0 = dom.childAt(fragment, [1, 1, 1, 3, 1]);
      var element1 = dom.childAt(element0, [7, 1]);
      var morph0 = dom.createMorphAt(dom.childAt(element0, [3]),0,0);
      var morph1 = dom.createMorphAt(dom.childAt(element0, [5]),0,0);
      inline(env, morph0, context, "input", [], {"type": "text", "value": get(env, context, "username")});
      inline(env, morph1, context, "input", [], {"type": "password", "value": get(env, context, "password")});
      element(env, element1, context, "action", ["login"], {});
      return fragment;
    }
  };
}()));
Ember.TEMPLATES["users"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      isHTMLBars: true,
      revision: "Ember@1.12.1",
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        var hooks = env.hooks, content = hooks.content;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        var element0 = dom.childAt(fragment, [1]);
        var morph0 = dom.createMorphAt(element0,0,0);
        var morph1 = dom.createMorphAt(element0,2,2);
        content(env, morph0, context, "login");
        content(env, morph1, context, "userName");
        return fragment;
      }
    };
  }());
  return {
    isHTMLBars: true,
    revision: "Ember@1.12.1",
    blockParams: 0,
    cachedFragment: null,
    hasRendered: false,
    build: function build(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("h2");
      var el3 = dom.createTextNode("Users List");
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
    render: function render(context, env, contextualElement) {
      var dom = env.dom;
      var hooks = env.hooks, get = hooks.get, block = hooks.block;
      dom.detectNamespace(contextualElement);
      var fragment;
      if (env.useFragmentCache && dom.canClone) {
        if (this.cachedFragment === null) {
          fragment = this.build(dom);
          if (this.hasRendered) {
            this.cachedFragment = fragment;
          } else {
            this.hasRendered = true;
          }
        }
        if (this.cachedFragment) {
          fragment = dom.cloneNode(this.cachedFragment, true);
        }
      } else {
        fragment = this.build(dom);
      }
      var morph0 = dom.createMorphAt(dom.childAt(fragment, [0]),3,3);
      block(env, morph0, context, "each", [get(env, context, "controller")], {}, child0, null);
      return fragment;
    }
  };
}()));