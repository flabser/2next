'use strict';

var CT = Ember.Application.create({
    modulePrefix: 'CT',
    LOG_TRANSITIONS: true,
    LOG_TRANSITIONS_INTERNAL: true,
    LOG_ACTIVE_GENERATION: true
});

CT.ApplicationAdapter = DS.RESTAdapter.extend({
    pathForType: function(type) {
        switch (type) {
            case 'category':
                return 'categories';
            default:
                return type + 's';
        }
    }
});

DS.RESTAdapter.reopen({
    namespace: 'CashTracker/rest'
});

CT.Router = Ember.Router.extend({
    // location: 'history'
});

CT.Router.map(function() {
    this.route('index', {
        path: '/'
    });

    this.route('userprofile');

    this.route('transactions', function() {
        this.route('new');
        this.route('transaction', {
            path: '/:transaction_id'
        });
    });

    this.route('accounts', function() {
        this.route('new');
        this.route('account', {
            path: '/:account_id'
        });
    });

    this.route('categories', function() {
        this.route('new');
        this.route('category', {
            path: '/:category_id'
        });
    });

    this.route('cost_centers', {
        path: '/costcenters'
    }, function() {
        this.route('new');
        this.route('cost_center', {
            path: '/:costcenter_id'
        });
    });

    this.route('tags', function() {
        this.route('new');
        this.route('tag', {
            path: '/:tag_id'
        });
    });

    this.route('users', function() {
        this.route('new');
        this.route('user', {
            path: '/users/:user_id'
        });
    });

    this.route('login');
});

Ember.Route.reopen({
    redirect: function() {
        if (this.routeName === 'index') {
            this.transitionTo('transactions');
        }
    }
});

CT.AccComponent = Ember.Component.extend({
    templateName: 'components/accounts',

    queryParams: ['offset', 'limit', 'order_by'],

    actions: {
        selectAll: function() {}
    }
});

//Ember.Handlebars.helper('acc', CT.AccComponent);

CT.TransactionsController = Ember.ArrayController.extend({
    queryParams: ['offset', 'limit', 'order_by']
});

Ember.Application.initializer({
    name: 'i18n',

    initialize: function(container, application) {
        application.register('service:i18n', Ember.Object);
        application.inject('route', 'i18n', 'service:i18n');
    }
});

Ember.Application.initializer({
    name: 'session',

    initialize: function(container, application) {
        application.register('service:session', Ember.Object);
        application.inject('route', 'session', 'service:session');
    }
});

CT.Account = DS.Model.extend({
    type: DS.attr('number'),
    name: DS.attr('string'),
    currencyCode: DS.attr('string'),
    openingBalance: DS.attr('number'),
    amountControl: DS.attr('number'),
    owner: DS.belongsTo('user'),
    observers: DS.hasMany('user'),
    includeInTotals: DS.attr('boolean'),
    note: DS.attr('string'),
    sortOrder: DS.attr('number')
});

CT.Category = DS.Model.extend({
    transactionType: DS.attr('number'),
    parentId: DS.belongsTo('category'),
    name: DS.attr('string'),
    note: DS.attr('string'),
    color: DS.attr('number'),
    sortOrder: DS.attr('number')
});

CT.CostCenter = DS.Model.extend({
    name: DS.attr('string')
});

CT.Tag = DS.Model.extend({
    name: DS.attr('string'),
    color: DS.attr('number')
});

CT.Transaction = DS.Model.extend({
    user: DS.belongsTo('user'),
    accountFrom: DS.belongsTo('account'),
    accountTo: DS.belongsTo('account'),
    amount: DS.attr('number'),
    regDate: DS.attr('date'),
    category: DS.belongsTo('category'),
    costCenter: DS.belongsTo('costCenter'),
    tags: DS.hasMany('tag'),
    transactionState: DS.attr('number'),
    transactionType: DS.attr('number'),
    exchangeRate: DS.attr('number'),
    repeat: DS.attr('repeat'),
    every: DS.attr('every'),
    repeatStep: DS.attr('repeatStep'),
    startDate: DS.attr('date'),
    endDate: DS.attr('date'),
    basis: DS.attr('string'),
    note: DS.attr('string'),
    includeInReports: DS.attr('boolean')
});

CT.User = DS.Model.extend({
    login: DS.attr('string'),
    pwd: DS.attr('string'),
    email: DS.attr('string'),
    role: DS.attr('string')
});

CT.AccountsAccountRoute = Ember.Route.extend({
    templateName: 'account',

    model: function(params) {
        return this.store.find('account', params.account_id);
    },

    actions: {
        save: function(account) {
            account.save();
            this.transitionTo('accounts');
        }
    }
});

CT.AccountsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('account');
    },

    actions: {
        selectAll: function() {},
        openNewForm: function() {
            this.transitionTo('accounts.new');
        }
    }
});

CT.AccountsNewRoute = Ember.Route.extend({
    templateName: 'account',

    actions: {
        save: function() {
            var controller = this.controller;
            // TODO validate
            var newAccount = this.store.createRecord('account', {
                type: controller.get('type'),
                name: controller.get('name'),
                currency: controller.get('currency'),
                openingBalance: controller.get('openingBalance'),
                amountControl: controller.get('amountControl'),
                owner: controller.get('owner'),
                observers: controller.get('observers')
            });
            newAccount.save();
        },
        cancel: function() {
            this.transitionTo('accounts');
        }
    }
});

CT.ApplicationRoute = Ember.Route.extend({

    init: function() {
        this.windowOnResize();
        $(window).resize(this.windowOnResize);
    },

    windowOnResize: function() {
        if (window.innerWidth <= 800) {
            $('body').addClass('phone');
        } else {
            $('body').removeClass('phone');
        }
    },

    model: function() {
        var route = this,
            sessionService = this.get('session');

        var req = sessionService.getSession();
        req.then(function(result) {
            if (result.authUser.login) {
                route.session.set('user', result.authUser);
                return result.authUser;
            }
        });
        return req;
    },

    actions: {
        logout: function() {
            var route = this;
            this.get('session').logout().then(function() {
                route.session.set('user', null);
                // route.transitionTo('index');
                window.location.href = 'Provider?id=welcome';
            });

        },

        navAppMenuToggle: function() {
            $('body').toggleClass('nav-app-open');
        },

        navUserMenuToggle: function() {
            $('body').toggleClass('nav-ws-open');
        },

        hideOpenedNav: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        },

        toggleSearchForm: function() {
            $('body').toggleClass('search-open');
        },

        error: function(error, transition) {
            console.log('app error', error);

            if (error.status === 401 || (!this.session.get('user') && this.routeName !== 'login')) {

                /*this.controllerFor('login').setProperties({
                    transition: transition
                });*/

                // this.transitionTo('login');
                // window.location.href = 'Provider?id=login' + location.hash;
            } else {
                return true;
            }
        },

        willTransition: function() {
            $('body').removeClass('nav-app-open nav-ws-open');
        }
    }
});

CT.CategoriesRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('category');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.CategoriesNewRoute = Ember.Route.extend({
    templateName: 'category'
});

CT.CategoriesCategoryRoute = Ember.Route.extend({
    templateName: 'category',

    model: function(params) {
        return this.store.find('category', params.category_id);
    },

    actions: {
        save: function(category) {
            category.save();
            this.transitionTo('categories');
        }
    }
});

CT.CostCentersCostCenterRoute = Ember.Route.extend({
    templateName: 'cost_center',

    model: function(params) {
        return this.store.find('cost_center', params.costcenter_id);
    },

    actions: {
        save: function(costCenter) {
            costCenter.save();
            this.transitionTo('cost_centers');
        }
    }
});

CT.CostCentersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('cost_center');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.CostCentersNewRoute = Ember.Route.extend({
    templateName: 'cost_center',

    actions: {
        create: function() {
            this.transitionTo('cost_centers.new');
        },
        save: function() {
            var controller = this.controller;
            var newCostCenter = this.store.createRecord('costCenter', {
                name: controller.get('name')
            });
            newCostCenter.save();
        },
        cancel: function() {
            this.transitionTo('cost_centers');
        }
    }
});

CT.LoginRoute = Ember.Route.extend({

    actions: {
        login: function() {
            var route = this,
                controller = this.get('controller');

            var userName = controller.get('username'),
                password = controller.get('password');

            this.session.login(userName, password).then(function(user) {
                route.session.set('user', user);

                var transition = controller.get('transition');
                if (transition) {
                    transition.retry();
                } else {
                    route.transitionTo('index');
                }
            });
        },

        cancel: function() {
            this.transitionTo('index');
        }
    },

    beforeModel: function() {
        this.session.set('user', null);
    },

    resetController: function(controller) {
        controller.setProperties({
            username: null,
            password: null,
            transition: null
        });
    }
});

CT.TagsTagRoute = Ember.Route.extend({
    templateName: 'tag',

    model: function(params) {
        return this.store.find('tag', params.tag_id);
    },

    actions: {
        save: function(tag) {
            tag.save();
            this.transitionTo('tags');
        }
    }
});

CT.TagsRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('tag');
    },

    actions: {
        selectAll: function() {},
        openNewForm: function() {
            this.transitionTo('tags.new');
        }
    }
});

CT.TagsNewRoute = Ember.Route.extend({
    templateName: 'tag',

    actions: {
        save: function(tag) {
            var newTag = this.store.createRecord('tag', {
                name: this.controller.get('name'),
                color: this.controller.get('color')
            });
            newTag.save();
        },
        cancel: function() {
            this.transitionTo('tags');
        }
    }
});

CT.TransactionsTransactionRoute = Ember.Route.extend({
    templateName: 'transaction',

    model: function(params) {
        return this.store.find('transaction', params.transaction_id);
    },

    actions: {
        save: function(transaction) {
            transaction.save();
            this.transitionTo('transactions');
        }
    }
});

CT.TransactionsRoute = Ember.Route.extend({
    queryParams: {
        offset: {
            refreshModel: true
        },
        limit: {
            refreshModel: true
        },
        order_by: {
            refreshModel: true
        }
    },

    model: function(params) {
        return this.store.find('transaction');
    },

    beforeModel: function(transition) {
        if (transition.targetName === 'transactions.index') {
            if (!parseInt(transition.queryParams.limit, 0)) {
                transition.queryParams.limit = 10;
            }

            if (!parseInt(transition.queryParams.offset, 0)) {
                transition.queryParams.offset = 0;
            }

            if (!transition.queryParams.order_by || transition.queryParams.order_by === 'undefined') {
                transition.queryParams.order_by = '';
            }

            this.transitionTo('transactions', {
                queryParams: transition.queryParams
            });
        }

        this._super(transition);
    },

    actions: {
        addTransaction: function() {
            var controller = this.controller;
            var newTransaction = this.store.createRecord('transaction', {
                author: controller.get('author'),
                regDate: controller.get('regDate'),
                date: controller.get('date'),
                endDate: controller.get('endDate'),
                parentCategory: controller.get('parentCategory'),
                category: controller.get('category'),
                account: controller.get('account'),
                costCenter: controller.get('costCenter'),
                amount: controller.get('amount'),
                repeat: controller.get('repeat'),
                every: controller.get('every'),
                repeatStep: controller.get('repeatStep'),
                basis: controller.get('basis'),
                observers: controller.get('observers'),
                comment: controller.get('comment')
            });
            newTransaction.save();
        }
    }
});

CT.TransactionsNewRoute = Ember.Route.extend({
    templateName: 'transaction'
});

CT.UsersUserRoute = Ember.Route.extend({
    templateName: 'user',

    model: function(params) {
        return this.store.find('user', params.user_id);
    },

    actions: {
        save: function(user) {
            user.save();
            this.transitionTo('users');
        }
    }
});

CT.UsersRoute = Ember.Route.extend({
    model: function(params) {
        return this.store.find('user');
    },

    actions: {
        selectAll: function() {}
    }
});

CT.UsersNewRoute = Ember.Route.extend({
    templateName: 'user',

    actions: {
        create: function() {
            this.transitionTo('users.new');
        },
        save: function() {
            var controller = this.controller;
            var newUser = this.store.createRecord('user', {
                name: controller.get('name')
            });
            newUser.save();
        },
        cancel: function() {
            this.transitionTo('users');
        }
    }
});

CT.I18nService = Ember.Service.extend({

    translations: [],

    init: function() {
        Ember.HTMLBars._registerHelper('t', this.t);
        this.fetchTranslations().then(function(translations) {
            CT.I18nService.translations = translations;
        });
    },

    fetchTranslations: function() {
        return $.getJSON('rest/page/app-captions').then(function(data) {
            return data._Page.captions;
        });
    },

    t: function(key) {
        if (CT.I18nService.translations.hasOwnProperty(key)) {
            return CT.I18nService.translations[key][0];
        } else {
            return key;
        }
    }
});

CT.SessionService = Ember.Service.extend({

    getSession: function() {
        return $.getJSON('rest/session');
    },

    login: function(userName, password) {
        return $.ajax({
            method: 'POST',
            dataType: 'json',
            contentType: 'application/json',
            url: 'rest/session',
            data: JSON.stringify({
                authUser: {
                    login: userName,
                    pwd: password
                }
            }),
            success: function(result) {
                return result;
            }
        });
    },

    logout: function() {
        return $.ajax({
            method: 'DELETE',
            url: 'rest/session'
        });
    }
});

CT.ApplicationView = Ember.View.extend({
    classNames: ['layout'],

    willInsertElement: function() {
        $('.page-loading').hide();
    }
});

Ember.TEMPLATES["account"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 54,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","form-wrapper");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("header");
      dom.setAttribute(el2,"class","form-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      dom.setAttribute(el3,"class","header-title");
      var el4 = dom.createTextNode("Account / ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/accounts");
      var el5 = dom.createComment("");
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
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","form-content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","fieldset");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element1, [3]);
      var element3 = dom.childAt(element2, [1]);
      var element4 = dom.childAt(element0, [3, 1]);
      var element5 = dom.childAt(element4, [1]);
      var element6 = dom.childAt(element4, [3]);
      var element7 = dom.childAt(element4, [5]);
      var element8 = dom.childAt(element4, [7]);
      var element9 = dom.childAt(element4, [9]);
      var morphs = new Array(14);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1]),1,1);
      morphs[1] = dom.createElementMorph(element3);
      morphs[2] = dom.createMorphAt(element3,0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element5, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element5, [3]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element6, [1]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element6, [3]),1,1);
      morphs[8] = dom.createMorphAt(dom.childAt(element7, [1]),1,1);
      morphs[9] = dom.createMorphAt(dom.childAt(element7, [3]),1,1);
      morphs[10] = dom.createMorphAt(dom.childAt(element8, [1]),1,1);
      morphs[11] = dom.createMorphAt(dom.childAt(element8, [3]),1,1);
      morphs[12] = dom.createMorphAt(dom.childAt(element9, [1]),1,1);
      morphs[13] = dom.createMorphAt(dom.childAt(element9, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","name",["loc",[null,[3,43],[3,51]]]],
      ["element","action",["save",["get","this",["loc",[null,[5,60],[5,64]]]]],[],["loc",[null,[5,44],[5,66]]]],
      ["inline","t",["save"],[],["loc",[null,[5,67],[5,79]]]],
      ["inline","t",["cancel"],[],["loc",[null,[6,45],[6,59]]]],
      ["inline","t",["name"],[],["loc",[null,[13,20],[13,32]]]],
      ["inline","input",[],["name","name","value",["subexpr","@mut",[["get","name",["loc",[null,[16,46],[16,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[16,20],[16,80]]]],
      ["inline","t",["type"],[],["loc",[null,[21,20],[21,32]]]],
      ["inline","input",[],["name","type","value",["subexpr","@mut",[["get","type",["loc",[null,[24,46],[24,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[24,20],[24,80]]]],
      ["inline","t",["amountControl"],[],["loc",[null,[29,20],[29,41]]]],
      ["inline","input",[],["name","amountControl","value",["subexpr","@mut",[["get","amountControl",["loc",[null,[32,55],[32,68]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[32,20],[32,98]]]],
      ["inline","t",["owner"],[],["loc",[null,[37,20],[37,33]]]],
      ["inline","input",[],["name","owner","value",["subexpr","@mut",[["get","owner.id",["loc",[null,[40,47],[40,55]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[40,20],[40,85]]]],
      ["inline","t",["observers"],[],["loc",[null,[45,20],[45,37]]]],
      ["inline","input",[],["name","amountControl","value",["subexpr","@mut",[["get","amountControl",["loc",[null,[48,55],[48,68]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[48,20],[48,98]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["accounts"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 21,
            "column": 24
          },
          "end": {
            "line": 21,
            "column": 94
          }
        }
      },
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
        ["inline","t",["account.new.btn_label"],[],["loc",[null,[21,64],[21,93]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.3+c3accfb0",
          "loc": {
            "source": null,
            "start": {
              "line": 60,
              "column": 24
            },
            "end": {
              "line": 77,
              "column": 24
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("                        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","entry-fields");
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field vaccount-name");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field vaccount-user");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field vaccount-observers");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("span");
          var el4 = dom.createTextNode("\n                                ");
          dom.appendChild(el3, el4);
          var el4 = dom.createComment("");
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n                            ");
          dom.appendChild(el3, el4);
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field vaccount-amount-control");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var element0 = dom.childAt(fragment, [1]);
          var morphs = new Array(4);
          morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
          morphs[1] = dom.createMorphAt(dom.childAt(element0, [3]),1,1);
          morphs[2] = dom.createMorphAt(dom.childAt(element0, [5, 1]),1,1);
          morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),1,1);
          return morphs;
        },
        statements: [
          ["content","name",["loc",[null,[63,28],[63,36]]]],
          ["content","owner.name",["loc",[null,[66,28],[66,42]]]],
          ["content","observers.name",["loc",[null,[70,32],[70,50]]]],
          ["content","amountControl",["loc",[null,[74,28],[74,45]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 49,
            "column": 16
          },
          "end": {
            "line": 80,
            "column": 16
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","entry-wrap");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry-actions");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("a");
        dom.setAttribute(el3,"class","entry-action action-delete");
        dom.setAttribute(el3,"href","#");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("i");
        dom.setAttribute(el4,"class","fa fa-trash");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry document js-swipe-entry");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("label");
        dom.setAttribute(el3,"class","entry-select");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("input");
        dom.setAttribute(el4,"type","checkbox");
        dom.setAttribute(el4,"name","id");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element1 = dom.childAt(fragment, [1]);
        var element2 = dom.childAt(element1, [1, 1]);
        var element3 = dom.childAt(element1, [3]);
        var element4 = dom.childAt(element3, [1, 1]);
        var morphs = new Array(5);
        morphs[0] = dom.createAttrMorph(element2, 'data-ddbid');
        morphs[1] = dom.createAttrMorph(element3, 'data-ddbid');
        morphs[2] = dom.createAttrMorph(element4, 'value');
        morphs[3] = dom.createElementMorph(element4);
        morphs[4] = dom.createMorphAt(element3,3,3);
        return morphs;
      },
      statements: [
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[52,76],[52,78]]]]]]],
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[56,39],[56,41]]]]]]],
        ["attribute","value",["concat",[["get","id",["loc",[null,[58,70],[58,72]]]]]]],
        ["element","action",["selectOne"],[],["loc",[null,[58,76],[58,98]]]],
        ["block","link-to",["accounts.account",["get","this",["loc",[null,[60,54],[60,58]]]]],["class","entry-link"],0,null,["loc",[null,[60,24],[77,36]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 86,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"style","display:flex;");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","view view_accounts");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("header");
      dom.setAttribute(el3,"class","view-header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("h1");
      dom.setAttribute(el4,"class","header-title");
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","pull-right");
      var el6 = dom.createTextNode("\n                saldo\n            ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            Accounts\n            ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("sup");
      dom.setAttribute(el5,"class","entry-count");
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("small");
      var el7 = dom.createTextNode("\n                    count\n                ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n            ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n        ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","nav-action-bar");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","pull-right");
      var el6 = dom.createTextNode("\n                    page-navigator\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","-on-desktop");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","action-bar");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("section");
      dom.setAttribute(el3,"class","view-content");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("header");
      dom.setAttribute(el4,"class","entries-head");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","head-wrap");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("label");
      dom.setAttribute(el6,"class","entry-select");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("input");
      dom.setAttribute(el7,"type","checkbox");
      dom.setAttribute(el7,"class","all");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","entry-captions");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","vaccount-name");
      var el8 = dom.createTextNode("\n                        ");
      dom.appendChild(el7, el8);
      var el8 = dom.createComment("");
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","vaccount-user");
      var el8 = dom.createTextNode("\n                        ");
      dom.appendChild(el7, el8);
      var el8 = dom.createComment("");
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","vaccount-observers");
      var el8 = dom.createTextNode("\n                        ");
      dom.appendChild(el7, el8);
      var el8 = dom.createComment("");
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","vaccount-amount-control");
      var el8 = dom.createTextNode("\n                        ");
      dom.appendChild(el7, el8);
      var el8 = dom.createComment("");
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","entries");
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
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element5 = dom.childAt(fragment, [0]);
      var element6 = dom.childAt(element5, [1]);
      var element7 = dom.childAt(element6, [3]);
      var element8 = dom.childAt(element7, [1, 1]);
      var element9 = dom.childAt(element8, [1, 1]);
      var element10 = dom.childAt(element8, [3]);
      var morphs = new Array(8);
      morphs[0] = dom.createMorphAt(dom.childAt(element6, [1, 3, 3, 1]),1,1);
      morphs[1] = dom.createElementMorph(element9);
      morphs[2] = dom.createMorphAt(dom.childAt(element10, [1]),1,1);
      morphs[3] = dom.createMorphAt(dom.childAt(element10, [3]),1,1);
      morphs[4] = dom.createMorphAt(dom.childAt(element10, [5]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element10, [7]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element7, [3]),1,1);
      morphs[7] = dom.createMorphAt(element5,3,3);
      return morphs;
    },
    statements: [
      ["block","link-to",["accounts.new"],["class","btn"],0,null,["loc",[null,[21,24],[21,106]]]],
      ["element","action",["selectAll"],[],["loc",[null,[30,59],[30,81]]]],
      ["content","viewtext1",["loc",[null,[34,24],[34,37]]]],
      ["content","viewtext2",["loc",[null,[37,24],[37,37]]]],
      ["content","viewtext3",["loc",[null,[40,24],[40,37]]]],
      ["content","viewnumber",["loc",[null,[43,24],[43,38]]]],
      ["block","each",[["get","controller",["loc",[null,[49,24],[49,34]]]]],[],1,null,["loc",[null,[49,16],[80,25]]]],
      ["content","outlet",["loc",[null,[84,4],[84,14]]]]
    ],
    locals: [],
    templates: [child0, child1]
  };
}()));
Ember.TEMPLATES["application"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 40,
            "column": 20
          },
          "end": {
            "line": 42,
            "column": 76
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-icon");
        var el2 = dom.createElement("i");
        dom.setAttribute(el2,"class","fa fa-file-o");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-text");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode(" ");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [3]),0,0);
        return morphs;
      },
      statements: [
        ["inline","t",["transactions"],[],["loc",[null,[42,48],[42,68]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 45,
            "column": 20
          },
          "end": {
            "line": 47,
            "column": 72
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-icon");
        var el2 = dom.createElement("i");
        dom.setAttribute(el2,"class","fa fa-file-o");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-text");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode(" ");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [3]),0,0);
        return morphs;
      },
      statements: [
        ["inline","t",["accounts"],[],["loc",[null,[47,48],[47,64]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child2 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 50,
            "column": 20
          },
          "end": {
            "line": 52,
            "column": 74
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-icon");
        var el2 = dom.createElement("i");
        dom.setAttribute(el2,"class","fa fa-file-o");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-text");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode(" ");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [3]),0,0);
        return morphs;
      },
      statements: [
        ["inline","t",["categories"],[],["loc",[null,[52,48],[52,66]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child3 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 55,
            "column": 20
          },
          "end": {
            "line": 57,
            "column": 76
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-icon");
        var el2 = dom.createElement("i");
        dom.setAttribute(el2,"class","fa fa-file-o");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-text");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode(" ");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [3]),0,0);
        return morphs;
      },
      statements: [
        ["inline","t",["cost_centers"],[],["loc",[null,[57,48],[57,68]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child4 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 60,
            "column": 20
          },
          "end": {
            "line": 62,
            "column": 68
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-icon");
        var el2 = dom.createElement("i");
        dom.setAttribute(el2,"class","fa fa-file-o");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-text");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode(" ");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [3]),0,0);
        return morphs;
      },
      statements: [
        ["inline","t",["tags"],[],["loc",[null,[62,48],[62,60]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child5 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 65,
            "column": 20
          },
          "end": {
            "line": 67,
            "column": 69
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-icon");
        var el2 = dom.createElement("i");
        dom.setAttribute(el2,"class","fa fa-file-o");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n                    ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        dom.setAttribute(el1,"class","nav-item-text");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode(" ");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [3]),0,0);
        return morphs;
      },
      statements: [
        ["inline","t",["users"],[],["loc",[null,[67,48],[67,61]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 94,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("header");
      dom.setAttribute(el1,"class","layout_header");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","main-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","head-item head-nav-app-toggle");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","nav-app-toggle");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","head-item brand");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("img");
      dom.setAttribute(el4,"alt","logo");
      dom.setAttribute(el4,"src","/SharedResources/logos/cashtracker_small.png");
      dom.setAttribute(el4,"class","brand-logo");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("span");
      dom.setAttribute(el4,"class","brand-title");
      var el5 = dom.createTextNode("CashTracker");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","head-item head-nav-ws-toggle");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","nav-ws-toggle");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","head-item nav-search-toggle");
      dom.setAttribute(el3,"id","toggle-head-search");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("i");
      dom.setAttribute(el4,"class","fa fa-search");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","head-item nav-search");
      dom.setAttribute(el3,"id","search-block");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","search-toggle-back");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("i");
      dom.setAttribute(el5,"class","fa fa-chevron-left");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("form");
      dom.setAttribute(el4,"action","Provider");
      dom.setAttribute(el4,"method","GET");
      dom.setAttribute(el4,"name","search");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("input");
      dom.setAttribute(el5,"type","hidden");
      dom.setAttribute(el5,"name","type");
      dom.setAttribute(el5,"value","page");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("input");
      dom.setAttribute(el5,"type","hidden");
      dom.setAttribute(el5,"name","id");
      dom.setAttribute(el5,"value","search");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("input");
      dom.setAttribute(el5,"type","search");
      dom.setAttribute(el5,"name","keyword");
      dom.setAttribute(el5,"value","");
      dom.setAttribute(el5,"class","search-keyword");
      dom.setAttribute(el5,"required","required");
      dom.setAttribute(el5,"placeholder","Поиск");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("button");
      dom.setAttribute(el5,"type","submit");
      dom.setAttribute(el5,"class","search-btn");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("i");
      dom.setAttribute(el6,"class","fa fa-search");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"class","no-desktop head-item nav-action");
      dom.setAttribute(el3,"href","#");
      dom.setAttribute(el3,"data-action","add_new");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("i");
      dom.setAttribute(el4,"class","fa fa-plus");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("span");
      dom.setAttribute(el4,"class","action-label");
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"class","flex");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("aside");
      dom.setAttribute(el2,"class","layout_aside nav-app");
      dom.setAttribute(el2,"id","nav");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("nav");
      dom.setAttribute(el3,"class","side-nav");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("ul");
      dom.setAttribute(el4,"class","side-tree");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("li");
      dom.setAttribute(el5,"class","side-tree-item");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("li");
      dom.setAttribute(el5,"class","side-tree-item");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("li");
      dom.setAttribute(el5,"class","side-tree-item");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("li");
      dom.setAttribute(el5,"class","side-tree-item");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("li");
      dom.setAttribute(el5,"class","side-tree-item");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("li");
      dom.setAttribute(el5,"class","side-tree-item");
      var el6 = dom.createTextNode("\n");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
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
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","content-overlay");
      dom.setAttribute(el2,"id","content-overlay");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","layout_content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("aside");
      dom.setAttribute(el2,"class","side-form");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("aside");
      dom.setAttribute(el2,"class","layout_aside nav-ws");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"href","#/userprofile");
      dom.setAttribute(el4,"class","user");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("i");
      dom.setAttribute(el5,"class","fa fa-user");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("span");
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("footer");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"href","#index");
      dom.setAttribute(el4,"class","ws-exit");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("i");
      dom.setAttribute(el5,"class","fa fa-sign-out");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("span");
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0, 1]);
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element0, [5, 1]);
      var element3 = dom.childAt(element0, [7]);
      var element4 = dom.childAt(element0, [9, 1]);
      var element5 = dom.childAt(fragment, [2]);
      var element6 = dom.childAt(element5, [1, 1, 1]);
      var element7 = dom.childAt(element5, [3]);
      var element8 = dom.childAt(element5, [9]);
      var element9 = dom.childAt(element8, [3, 1]);
      var morphs = new Array(16);
      morphs[0] = dom.createElementMorph(element1);
      morphs[1] = dom.createElementMorph(element2);
      morphs[2] = dom.createElementMorph(element3);
      morphs[3] = dom.createElementMorph(element4);
      morphs[4] = dom.createMorphAt(dom.childAt(element6, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element6, [3]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element6, [5]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element6, [7]),1,1);
      morphs[8] = dom.createMorphAt(dom.childAt(element6, [9]),1,1);
      morphs[9] = dom.createMorphAt(dom.childAt(element6, [11]),1,1);
      morphs[10] = dom.createElementMorph(element7);
      morphs[11] = dom.createMorphAt(dom.childAt(element5, [5]),1,1);
      morphs[12] = dom.createMorphAt(dom.childAt(element5, [7]),1,1);
      morphs[13] = dom.createMorphAt(dom.childAt(element8, [1, 1, 3]),0,0);
      morphs[14] = dom.createElementMorph(element9);
      morphs[15] = dom.createMorphAt(dom.childAt(element9, [3]),0,0);
      return morphs;
    },
    statements: [
      ["element","action",["navAppMenuToggle"],[],["loc",[null,[3,51],[3,80]]]],
      ["element","action",["navUserMenuToggle"],["on","mouseDown"],["loc",[null,[11,39],[11,85]]]],
      ["element","action",["toggleSearchForm"],["on","mouseDown"],["loc",[null,[13,73],[13,118]]]],
      ["element","action",["toggleSearchForm"],["on","mouseDown"],["loc",[null,[17,44],[17,89]]]],
      ["block","link-to",["transactions"],[],0,null,["loc",[null,[40,20],[42,88]]]],
      ["block","link-to",["accounts"],[],1,null,["loc",[null,[45,20],[47,84]]]],
      ["block","link-to",["categories"],[],2,null,["loc",[null,[50,20],[52,86]]]],
      ["block","link-to",["cost_centers"],[],3,null,["loc",[null,[55,20],[57,88]]]],
      ["block","link-to",["tags"],[],4,null,["loc",[null,[60,20],[62,80]]]],
      ["block","link-to",["users"],[],5,null,["loc",[null,[65,20],[67,81]]]],
      ["element","action",["hideOpenedNav"],["on","mouseDown"],["loc",[null,[72,54],[72,96]]]],
      ["content","outlet",["loc",[null,[74,8],[74,18]]]],
      ["inline","outlet",["sideForm"],[],["loc",[null,[77,8],[77,29]]]],
      ["content","session.user.login",["loc",[null,[83,22],[83,44]]]],
      ["element","action",["logout"],[],["loc",[null,[87,45],[87,64]]]],
      ["inline","t",["logout"],[],["loc",[null,[89,22],[89,36]]]]
    ],
    locals: [],
    templates: [child0, child1, child2, child3, child4, child5]
  };
}()));
Ember.TEMPLATES["categories"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 11,
            "column": 24
          },
          "end": {
            "line": 11,
            "column": 99
          }
        }
      },
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
        ["inline","t",["categories.new.btn_label"],[],["loc",[null,[11,66],[11,98]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.3+c3accfb0",
          "loc": {
            "source": null,
            "start": {
              "line": 41,
              "column": 24
            },
            "end": {
              "line": 47,
              "column": 24
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("                        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","entry-fields");
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1, 1]),1,1);
          return morphs;
        },
        statements: [
          ["content","name",["loc",[null,[44,28],[44,36]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 30,
            "column": 16
          },
          "end": {
            "line": 50,
            "column": 16
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","entry-wrap");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry-actions");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("a");
        dom.setAttribute(el3,"class","entry-action action-delete");
        dom.setAttribute(el3,"href","#");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("i");
        dom.setAttribute(el4,"class","fa fa-trash");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry document js-swipe-entry");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("label");
        dom.setAttribute(el3,"class","entry-select");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("input");
        dom.setAttribute(el4,"type","checkbox");
        dom.setAttribute(el4,"name","id");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var element1 = dom.childAt(element0, [1, 1]);
        var element2 = dom.childAt(element0, [3]);
        var element3 = dom.childAt(element2, [1, 1]);
        var morphs = new Array(4);
        morphs[0] = dom.createAttrMorph(element1, 'data-ddbid');
        morphs[1] = dom.createAttrMorph(element2, 'data-ddbid');
        morphs[2] = dom.createAttrMorph(element3, 'value');
        morphs[3] = dom.createMorphAt(element2,3,3);
        return morphs;
      },
      statements: [
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[33,76],[33,78]]]]]]],
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[37,39],[37,41]]]]]]],
        ["attribute","value",["concat",[["get","id",["loc",[null,[39,70],[39,72]]]]]]],
        ["block","link-to",["categories.category",["get","this",["loc",[null,[41,57],[41,61]]]]],["class","entry-link"],0,null,["loc",[null,[41,24],[47,36]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 56,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"style","display:flex;");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","view");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("h1");
      dom.setAttribute(el4,"class","header-title");
      var el5 = dom.createTextNode("Categories");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","nav-action-bar");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","pull-right");
      var el6 = dom.createTextNode("\n                    page-navigator\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","-on-desktop");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","action-bar");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-content");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("header");
      dom.setAttribute(el4,"class","entries-head");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","head-wrap");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("label");
      dom.setAttribute(el6,"class","entry-select");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("input");
      dom.setAttribute(el7,"type","checkbox");
      dom.setAttribute(el7,"data-toggle","id");
      dom.setAttribute(el7,"class","all");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","entry-captions");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      var el8 = dom.createTextNode("\n                        name\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","entries");
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
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element4 = dom.childAt(fragment, [0]);
      var element5 = dom.childAt(element4, [1]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element5, [1, 3, 3, 1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element5, [3, 3]),1,1);
      morphs[2] = dom.createMorphAt(element4,3,3);
      return morphs;
    },
    statements: [
      ["block","link-to",["categories.new"],["class","btn"],0,null,["loc",[null,[11,24],[11,111]]]],
      ["block","each",[["get","controller",["loc",[null,[30,24],[30,34]]]]],[],1,null,["loc",[null,[30,16],[50,25]]]],
      ["content","outlet",["loc",[null,[54,4],[54,14]]]]
    ],
    locals: [],
    templates: [child0, child1]
  };
}()));
Ember.TEMPLATES["category"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 22,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","form-wrapper");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("header");
      dom.setAttribute(el2,"class","form-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      dom.setAttribute(el3,"class","header-title");
      var el4 = dom.createTextNode("Category / ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/categories");
      var el5 = dom.createComment("");
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
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","form-content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("fieldset");
      dom.setAttribute(el3,"class","fieldset");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element1, [3]);
      var element3 = dom.childAt(element2, [1]);
      var element4 = dom.childAt(element0, [3, 1, 1]);
      var morphs = new Array(6);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1]),1,1);
      morphs[1] = dom.createElementMorph(element3);
      morphs[2] = dom.createMorphAt(element3,0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element4, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element4, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","name",["loc",[null,[3,44],[3,52]]]],
      ["element","action",["save",["get","this",["loc",[null,[5,60],[5,64]]]]],[],["loc",[null,[5,44],[5,66]]]],
      ["inline","t",["save"],[],["loc",[null,[5,67],[5,79]]]],
      ["inline","t",["cancel"],[],["loc",[null,[6,47],[6,61]]]],
      ["inline","t",["name"],[],["loc",[null,[13,20],[13,32]]]],
      ["inline","input",[],["name","name","value",["subexpr","@mut",[["get","name",["loc",[null,[16,46],[16,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[16,20],[16,80]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["cost_center"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 22,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","form-wrapper");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("header");
      dom.setAttribute(el2,"class","form-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      dom.setAttribute(el3,"class","header-title");
      var el4 = dom.createTextNode("Cost center / ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/costcenters");
      var el5 = dom.createComment("");
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
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","form-content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("fieldset");
      dom.setAttribute(el3,"class","fieldset");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element1, [3]);
      var element3 = dom.childAt(element2, [1]);
      var element4 = dom.childAt(element0, [3, 1, 1]);
      var morphs = new Array(6);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1]),1,1);
      morphs[1] = dom.createElementMorph(element3);
      morphs[2] = dom.createMorphAt(element3,0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element4, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element4, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","name",["loc",[null,[3,47],[3,55]]]],
      ["element","action",["save",["get","this",["loc",[null,[5,60],[5,64]]]]],[],["loc",[null,[5,44],[5,66]]]],
      ["inline","t",["save"],[],["loc",[null,[5,67],[5,79]]]],
      ["inline","t",["cancel"],[],["loc",[null,[6,48],[6,62]]]],
      ["inline","t",["name"],[],["loc",[null,[13,20],[13,32]]]],
      ["inline","input",[],["name","name","value",["subexpr","@mut",[["get","name",["loc",[null,[16,46],[16,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[16,20],[16,80]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["cost_centers"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 11,
            "column": 24
          },
          "end": {
            "line": 11,
            "column": 102
          }
        }
      },
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
        ["inline","t",["costcenters.new.btn_label"],[],["loc",[null,[11,68],[11,101]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.3+c3accfb0",
          "loc": {
            "source": null,
            "start": {
              "line": 41,
              "column": 24
            },
            "end": {
              "line": 47,
              "column": 24
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("                        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","entry-fields");
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1, 1]),1,1);
          return morphs;
        },
        statements: [
          ["content","name",["loc",[null,[44,28],[44,36]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 30,
            "column": 16
          },
          "end": {
            "line": 50,
            "column": 16
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","entry-wrap");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry-actions");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("a");
        dom.setAttribute(el3,"class","entry-action action-delete");
        dom.setAttribute(el3,"href","#");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("i");
        dom.setAttribute(el4,"class","fa fa-trash");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry document js-swipe-entry");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("label");
        dom.setAttribute(el3,"class","entry-select");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("input");
        dom.setAttribute(el4,"type","checkbox");
        dom.setAttribute(el4,"name","id");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var element1 = dom.childAt(element0, [1, 1]);
        var element2 = dom.childAt(element0, [3]);
        var element3 = dom.childAt(element2, [1, 1]);
        var morphs = new Array(4);
        morphs[0] = dom.createAttrMorph(element1, 'data-ddbid');
        morphs[1] = dom.createAttrMorph(element2, 'data-ddbid');
        morphs[2] = dom.createAttrMorph(element3, 'value');
        morphs[3] = dom.createMorphAt(element2,3,3);
        return morphs;
      },
      statements: [
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[33,76],[33,78]]]]]]],
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[37,39],[37,41]]]]]]],
        ["attribute","value",["concat",[["get","id",["loc",[null,[39,70],[39,72]]]]]]],
        ["block","link-to",["cost_centers.cost_center",["get","this",["loc",[null,[41,62],[41,66]]]]],["class","entry-link"],0,null,["loc",[null,[41,24],[47,36]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 56,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"style","display:flex;");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","view");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("h1");
      dom.setAttribute(el4,"class","header-title");
      var el5 = dom.createTextNode("Cost Centers");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","nav-action-bar");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","pull-right");
      var el6 = dom.createTextNode("\n                    page-navigator\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","-on-desktop");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","action-bar");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-content");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("header");
      dom.setAttribute(el4,"class","entries-head");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","head-wrap");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("label");
      dom.setAttribute(el6,"class","entry-select");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("input");
      dom.setAttribute(el7,"type","checkbox");
      dom.setAttribute(el7,"data-toggle","id");
      dom.setAttribute(el7,"class","all");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","entry-captions");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      var el8 = dom.createTextNode("\n                        name\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","entries");
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
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element4 = dom.childAt(fragment, [0]);
      var element5 = dom.childAt(element4, [1]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element5, [1, 3, 3, 1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element5, [3, 3]),1,1);
      morphs[2] = dom.createMorphAt(element4,3,3);
      return morphs;
    },
    statements: [
      ["block","link-to",["cost_centers.new"],["class","btn"],0,null,["loc",[null,[11,24],[11,114]]]],
      ["block","each",[["get","controller",["loc",[null,[30,24],[30,34]]]]],[],1,null,["loc",[null,[30,16],[50,25]]]],
      ["content","outlet",["loc",[null,[54,4],[54,14]]]]
    ],
    locals: [],
    templates: [child0, child1]
  };
}()));
Ember.TEMPLATES["error"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 4,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("p");
      dom.setAttribute(el1,"class","error");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createComment("");
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
      morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0]),1,1);
      return morphs;
    },
    statements: [
      ["content","model",["loc",[null,[2,4],[2,13]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["loading"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 1,
          "column": 7
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createTextNode("load...");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes() { return []; },
    statements: [

    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["login"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 43,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","login");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("h1");
      dom.setAttribute(el2,"class","login-title");
      var el3 = dom.createComment("");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","login-form");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("form");
      dom.setAttribute(el3,"method","post");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","login-form-bottom");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("label");
      dom.setAttribute(el5,"class","noauth");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("input");
      dom.setAttribute(el6,"type","checkbox");
      dom.setAttribute(el6,"name","noauth");
      dom.setAttribute(el6,"value","1");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("button");
      dom.setAttribute(el5,"type","submit");
      dom.setAttribute(el5,"class","btn");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
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
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","social");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"href","#vk");
      dom.setAttribute(el3,"rel","nofollow");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("i");
      dom.setAttribute(el4,"class","social-icon-vk");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"href","#fb");
      dom.setAttribute(el3,"rel","nofollow");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("i");
      dom.setAttribute(el4,"class","social-icon-fb");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"href","#twitter");
      dom.setAttribute(el3,"rel","nofollow");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("i");
      dom.setAttribute(el4,"class","social-icon-twitter");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("footer");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("ul");
      dom.setAttribute(el3,"class","help-list");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("li");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("a");
      dom.setAttribute(el5,"href","Provider?id=password-recovery");
      dom.setAttribute(el5,"rel","nofollow");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("li");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("a");
      dom.setAttribute(el5,"href","Provider?id=retry-send-verify-email");
      dom.setAttribute(el5,"rel","nofollow");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [3, 1]);
      var element2 = dom.childAt(element1, [5]);
      var element3 = dom.childAt(element2, [3]);
      var element4 = dom.childAt(element0, [7, 1]);
      var morphs = new Array(8);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),0,0);
      morphs[1] = dom.createMorphAt(element1,1,1);
      morphs[2] = dom.createMorphAt(element1,3,3);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [1]),2,2);
      morphs[4] = dom.createElementMorph(element3);
      morphs[5] = dom.createMorphAt(element3,1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element4, [1, 1]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element4, [3, 1]),1,1);
      return morphs;
    },
    statements: [
      ["inline","t",["authorization"],[],["loc",[null,[2,28],[2,49]]]],
      ["inline","input",[],["name","username","value",["subexpr","@mut",[["get","username",["loc",[null,[5,42],[5,50]]]]],[],[]],"required",true,"placeholder","username"],["loc",[null,[5,12],[5,89]]]],
      ["inline","input",[],["type","password","value",["subexpr","@mut",[["get","password",["loc",[null,[6,42],[6,50]]]]],[],[]],"required",true,"placeholder","password"],["loc",[null,[6,12],[6,89]]]],
      ["inline","t",["alien_device"],[],["loc",[null,[9,69],[9,89]]]],
      ["element","action",["login"],[],["loc",[null,[11,50],[11,68]]]],
      ["inline","t",["sign_in"],[],["loc",[null,[12,20],[12,35]]]],
      ["inline","t",["lost_password"],[],["loc",[null,[32,20],[32,41]]]],
      ["inline","t",["no_verify_mail"],[],["loc",[null,[37,20],[37,42]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["tag"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 22,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","form-wrapper");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("header");
      dom.setAttribute(el2,"class","form-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      dom.setAttribute(el3,"class","header-title");
      var el4 = dom.createTextNode("Tag / ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/tags");
      var el5 = dom.createComment("");
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
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","form-content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","fieldset");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element1, [3]);
      var element3 = dom.childAt(element2, [1]);
      var element4 = dom.childAt(element0, [3, 1, 1]);
      var morphs = new Array(6);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1]),1,1);
      morphs[1] = dom.createElementMorph(element3);
      morphs[2] = dom.createMorphAt(element3,0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element4, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element4, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","name",["loc",[null,[3,39],[3,47]]]],
      ["element","action",["save",["get","this",["loc",[null,[5,60],[5,64]]]]],[],["loc",[null,[5,44],[5,66]]]],
      ["inline","t",["save"],[],["loc",[null,[5,67],[5,79]]]],
      ["inline","t",["cancel"],[],["loc",[null,[6,41],[6,55]]]],
      ["inline","t",["name"],[],["loc",[null,[13,20],[13,32]]]],
      ["inline","input",[],["name","name","value",["subexpr","@mut",[["get","name",["loc",[null,[16,46],[16,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[16,20],[16,80]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["tags"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 8,
            "column": 24
          },
          "end": {
            "line": 8,
            "column": 86
          }
        }
      },
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
        ["inline","t",["tag.new.btn_label"],[],["loc",[null,[8,60],[8,85]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.3+c3accfb0",
          "loc": {
            "source": null,
            "start": {
              "line": 34,
              "column": 24
            },
            "end": {
              "line": 40,
              "column": 24
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("                        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","entry-fields");
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field");
          var el3 = dom.createTextNode("\n                                ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1, 1]),1,1);
          return morphs;
        },
        statements: [
          ["content","name",["loc",[null,[37,32],[37,40]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 23,
            "column": 16
          },
          "end": {
            "line": 43,
            "column": 16
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","entry-wrap");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry-actions");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("a");
        dom.setAttribute(el3,"class","entry-action action-delete");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("i");
        dom.setAttribute(el4,"class","fa fa-trash");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry document js-swipe-entry");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("label");
        dom.setAttribute(el3,"class","entry-select");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("input");
        dom.setAttribute(el4,"type","checkbox");
        dom.setAttribute(el4,"name","id");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [1]);
        var element1 = dom.childAt(element0, [1, 1]);
        var element2 = dom.childAt(element0, [3]);
        var element3 = dom.childAt(element2, [1, 1]);
        if (this.cachedFragment) { dom.repairClonedNode(element3,[],true); }
        var morphs = new Array(6);
        morphs[0] = dom.createAttrMorph(element1, 'href');
        morphs[1] = dom.createElementMorph(element1);
        morphs[2] = dom.createAttrMorph(element2, 'data-ddbid');
        morphs[3] = dom.createAttrMorph(element3, 'value');
        morphs[4] = dom.createAttrMorph(element3, 'checked');
        morphs[5] = dom.createMorphAt(element2,3,3);
        return morphs;
      },
      statements: [
        ["attribute","href",["concat",["#/tags/",["get","id",["loc",[null,[26,77],[26,79]]]],"/delete"]]],
        ["element","action",["delete"],[],["loc",[null,[26,90],[26,109]]]],
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[30,39],[30,41]]]]]]],
        ["attribute","value",["concat",[["get","id",["loc",[null,[32,70],[32,72]]]]]]],
        ["attribute","checked",["get","selectAll",["loc",[null,[32,86],[32,95]]]]],
        ["block","link-to",["tags.tag",["get","this",["loc",[null,[34,46],[34,50]]]]],["class","entry-link"],0,null,["loc",[null,[34,24],[40,36]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
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
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"style","display:flex;");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","view");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("h1");
      dom.setAttribute(el4,"class","header-title");
      var el5 = dom.createTextNode("Tags");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","nav-action-bar");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","-on-desktop");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","action-bar");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-content");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("header");
      dom.setAttribute(el4,"class","entries-head");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","head-wrap");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("label");
      dom.setAttribute(el6,"class","entry-select");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("input");
      dom.setAttribute(el7,"type","checkbox");
      dom.setAttribute(el7,"data-toggle","id");
      dom.setAttribute(el7,"class","all");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","entry-captions");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","entries");
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
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element4 = dom.childAt(fragment, [0]);
      var element5 = dom.childAt(element4, [1]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element5, [1, 3, 1, 1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element5, [3, 3]),1,1);
      morphs[2] = dom.createMorphAt(element4,3,3);
      return morphs;
    },
    statements: [
      ["block","link-to",["tags.new"],["class","btn"],0,null,["loc",[null,[8,24],[8,98]]]],
      ["block","each",[["get","controller",["loc",[null,[23,24],[23,34]]]]],[],1,null,["loc",[null,[23,16],[43,25]]]],
      ["content","outlet",["loc",[null,[47,4],[47,14]]]]
    ],
    locals: [],
    templates: [child0, child1]
  };
}()));
Ember.TEMPLATES["transaction"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 84,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","form-wrapper");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("header");
      dom.setAttribute(el2,"class","form-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      dom.setAttribute(el3,"class","header-title");
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("small");
      dom.setAttribute(el4,"id","saldo");
      dom.setAttribute(el4,"class","pull-right");
      dom.setAttribute(el4,"title","Сальдо");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode(" № ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode(" ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n    ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-default");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/transactions");
      var el5 = dom.createComment("");
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
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","form-content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("fieldset");
      dom.setAttribute(el3,"class","fieldset");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" Дата операции ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" Касса ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" category ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" Целевая касса ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" Сумма ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" Место возникновения ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" Основание ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment(" documented ");
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"id","tabs-2");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("form");
      dom.setAttribute(el4,"action","Uploader");
      dom.setAttribute(el4,"name","upload");
      dom.setAttribute(el4,"id","upload");
      dom.setAttribute(el4,"method","post");
      dom.setAttribute(el4,"enctype","multipart/form-data");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment(" Секция \"Вложения\" ");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"display","block");
      dom.setAttribute(el5,"id","att");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("br");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode(" attach\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"id","tabs-3");
      var el4 = dom.createTextNode("\n            doc info\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
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
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element1, [1]);
      var element3 = dom.childAt(element1, [3]);
      var element4 = dom.childAt(element3, [1]);
      var element5 = dom.childAt(element0, [3, 1]);
      var element6 = dom.childAt(element5, [3]);
      var element7 = dom.childAt(element5, [7]);
      var element8 = dom.childAt(element5, [11]);
      var element9 = dom.childAt(element5, [17]);
      var element10 = dom.childAt(element5, [21]);
      var element11 = dom.childAt(element5, [25]);
      var morphs = new Array(18);
      morphs[0] = dom.createMorphAt(element2,3,3);
      morphs[1] = dom.createMorphAt(element2,5,5);
      morphs[2] = dom.createMorphAt(element2,7,7);
      morphs[3] = dom.createElementMorph(element4);
      morphs[4] = dom.createMorphAt(element4,0,0);
      morphs[5] = dom.createMorphAt(dom.childAt(element3, [3]),0,0);
      morphs[6] = dom.createMorphAt(dom.childAt(element6, [1]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element6, [3]),1,1);
      morphs[8] = dom.createMorphAt(dom.childAt(element7, [1]),1,1);
      morphs[9] = dom.createMorphAt(dom.childAt(element7, [3]),1,1);
      morphs[10] = dom.createMorphAt(dom.childAt(element8, [1]),1,1);
      morphs[11] = dom.createMorphAt(dom.childAt(element8, [3]),1,1);
      morphs[12] = dom.createMorphAt(dom.childAt(element9, [1]),1,1);
      morphs[13] = dom.createMorphAt(dom.childAt(element9, [3]),1,1);
      morphs[14] = dom.createMorphAt(dom.childAt(element10, [1]),1,1);
      morphs[15] = dom.createMorphAt(dom.childAt(element10, [3]),1,1);
      morphs[16] = dom.createMorphAt(dom.childAt(element11, [1]),1,1);
      morphs[17] = dom.createMorphAt(dom.childAt(element11, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","title",["loc",[null,[5,8],[5,17]]]],
      ["content","vn",["loc",[null,[5,20],[5,26]]]],
      ["content","date",["loc",[null,[5,27],[5,35]]]],
      ["element","action",["save",["get","this",["loc",[null,[8,60],[8,64]]]]],[],["loc",[null,[8,44],[8,66]]]],
      ["inline","t",["save"],[],["loc",[null,[8,67],[8,79]]]],
      ["inline","t",["cancel"],[],["loc",[null,[9,49],[9,63]]]],
      ["inline","t",["date"],[],["loc",[null,[17,20],[17,32]]]],
      ["inline","input",[],["name","date","value",["subexpr","@mut",[["get","date",["loc",[null,[20,46],[20,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[20,20],[20,80]]]],
      ["inline","t",["account"],[],["loc",[null,[26,20],[26,35]]]],
      ["content","account",["loc",[null,[29,20],[29,31]]]],
      ["inline","t",["category"],[],["loc",[null,[35,20],[35,36]]]],
      ["content","category",["loc",[null,[38,20],[38,32]]]],
      ["inline","t",["amount"],[],["loc",[null,[45,20],[45,34]]]],
      ["inline","input",[],["type","number","name","amount","value",["subexpr","@mut",[["get","amount",["loc",[null,[48,62],[48,68]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[48,20],[48,98]]]],
      ["inline","t",["cost_center"],[],["loc",[null,[54,20],[54,39]]]],
      ["content","costcenter",["loc",[null,[57,20],[57,34]]]],
      ["inline","t",["basis"],[],["loc",[null,[63,20],[63,33]]]],
      ["inline","textarea",[],["name","basis","value",["subexpr","@mut",[["get","basis",["loc",[null,[66,50],[66,55]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[66,20],[66,85]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["transactions"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 11,
            "column": 24
          },
          "end": {
            "line": 11,
            "column": 103
          }
        }
      },
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
        ["inline","t",["transactions.new.btn_label"],[],["loc",[null,[11,68],[11,102]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.3+c3accfb0",
          "loc": {
            "source": null,
            "start": {
              "line": 51,
              "column": 24
            },
            "end": {
              "line": 75,
              "column": 24
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("                        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","entry-fields");
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field op-icon-att");
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field op-account");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field op-date");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field op-amount");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field vcategory-icon-type");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field op-costcenter");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field op-vt");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("span");
          var el4 = dom.createTextNode("\n                                ");
          dom.appendChild(el3, el4);
          var el4 = dom.createComment("");
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n                            ");
          dom.appendChild(el3, el4);
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var element0 = dom.childAt(fragment, [1]);
          var element1 = dom.childAt(element0, [1]);
          var element2 = dom.childAt(element0, [9, 1]);
          var morphs = new Array(7);
          morphs[0] = dom.createElementMorph(element1);
          morphs[1] = dom.createMorphAt(dom.childAt(element0, [3]),1,1);
          morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),1,1);
          morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),1,1);
          morphs[4] = dom.createAttrMorph(element2, 'class');
          morphs[5] = dom.createMorphAt(dom.childAt(element0, [11]),1,1);
          morphs[6] = dom.createMorphAt(dom.childAt(element0, [13, 1]),1,1);
          return morphs;
        },
        statements: [
          ["element","hasattach",[],[],["loc",[null,[53,66],[53,79]]]],
          ["content","account",["loc",[null,[55,28],[55,39]]]],
          ["content","date",["loc",[null,[58,28],[58,36]]]],
          ["content","amount",["loc",[null,[61,28],[61,38]]]],
          ["attribute","class",["concat",["operation-type-icon-",["get","type",["loc",[null,[64,60],[64,64]]]]]]],
          ["content","costCenter",["loc",[null,[67,28],[67,42]]]],
          ["content","comment",["loc",[null,[71,32],[71,43]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 40,
            "column": 16
          },
          "end": {
            "line": 78,
            "column": 16
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","entry-wrap");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry-actions");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("a");
        dom.setAttribute(el3,"class","entry-action action-delete");
        dom.setAttribute(el3,"href","#");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("i");
        dom.setAttribute(el4,"class","fa fa-trash");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry document js-swipe-entry js_saldo_on_date");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("label");
        dom.setAttribute(el3,"class","entry-select");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("input");
        dom.setAttribute(el4,"type","checkbox");
        dom.setAttribute(el4,"name","id");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                ");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element3 = dom.childAt(fragment, [1]);
        var element4 = dom.childAt(element3, [1, 1]);
        var element5 = dom.childAt(element3, [3]);
        var element6 = dom.childAt(element5, [1, 1]);
        var morphs = new Array(4);
        morphs[0] = dom.createAttrMorph(element4, 'data-ddbid');
        morphs[1] = dom.createAttrMorph(element5, 'data-ddbid');
        morphs[2] = dom.createAttrMorph(element6, 'value');
        morphs[3] = dom.createMorphAt(element5,3,3);
        return morphs;
      },
      statements: [
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[43,76],[43,78]]]]]]],
        ["attribute","data-ddbid",["concat",[["get","id",["loc",[null,[47,39],[47,41]]]]]]],
        ["attribute","value",["concat",[["get","id",["loc",[null,[49,70],[49,72]]]]]]],
        ["block","link-to",["transactions.transaction",["get","this",["loc",[null,[51,62],[51,66]]]]],["class","entry-link"],0,null,["loc",[null,[51,24],[75,36]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 84,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"style","display:flex;");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","view view_op");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("h1");
      dom.setAttribute(el4,"class","header-title");
      var el5 = dom.createTextNode("Transactions");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","nav-action-bar");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","pull-right");
      var el6 = dom.createTextNode("\n                    page-navigator\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","-on-desktop");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","action-bar");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-content");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("header");
      dom.setAttribute(el4,"class","entries-head");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","head-wrap op-head");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("label");
      dom.setAttribute(el6,"class","entry-select");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("input");
      dom.setAttribute(el7,"type","checkbox");
      dom.setAttribute(el7,"data-toggle","id");
      dom.setAttribute(el7,"class","all");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","entry-captions");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","op-icon-att");
      var el8 = dom.createTextNode("\n                        ");
      dom.appendChild(el7, el8);
      var el8 = dom.createElement("i");
      dom.setAttribute(el8,"class","fa fa-paperclip");
      dom.appendChild(el7, el8);
      var el8 = dom.createTextNode("\n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","op-account");
      var el8 = dom.createTextNode("\n                        \n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","op-date");
      var el8 = dom.createTextNode("\n                        \n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","op-amount");
      var el8 = dom.createTextNode("\n                        \n                    ");
      dom.appendChild(el7, el8);
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createElement("span");
      dom.setAttribute(el7,"class","entry-field op-costcenter");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","entries");
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
      var el2 = dom.createComment("");
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element7 = dom.childAt(fragment, [0]);
      var element8 = dom.childAt(element7, [1]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element8, [1, 3, 3, 1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element8, [3, 3]),1,1);
      morphs[2] = dom.createMorphAt(element7,3,3);
      return morphs;
    },
    statements: [
      ["block","link-to",["transactions.new"],["class","btn"],0,null,["loc",[null,[11,24],[11,115]]]],
      ["block","each",[["get","controller",["loc",[null,[40,24],[40,34]]]]],[],1,null,["loc",[null,[40,16],[78,25]]]],
      ["content","outlet",["loc",[null,[82,4],[82,14]]]]
    ],
    locals: [],
    templates: [child0, child1]
  };
}()));
Ember.TEMPLATES["user"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 30,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("div");
      dom.setAttribute(el1,"class","form-wrapper");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("header");
      dom.setAttribute(el2,"class","form-header");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("h1");
      dom.setAttribute(el3,"class","header-title");
      var el4 = dom.createTextNode("User / ");
      dom.appendChild(el3, el4);
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","action-bar");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("button");
      dom.setAttribute(el4,"class","btn btn-primary");
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("a");
      dom.setAttribute(el4,"class","btn");
      dom.setAttribute(el4,"href","#/users");
      var el5 = dom.createComment("");
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
      var el2 = dom.createElement("section");
      dom.setAttribute(el2,"class","form-content");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("fieldset");
      dom.setAttribute(el3,"class","fieldset");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-group");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","control-label");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","controls");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createComment("");
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [1]);
      var element2 = dom.childAt(element1, [3]);
      var element3 = dom.childAt(element2, [1]);
      var element4 = dom.childAt(element0, [3, 1]);
      var element5 = dom.childAt(element4, [1]);
      var element6 = dom.childAt(element4, [3]);
      var morphs = new Array(8);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1]),1,1);
      morphs[1] = dom.createElementMorph(element3);
      morphs[2] = dom.createMorphAt(element3,0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element2, [3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element5, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element5, [3]),1,1);
      morphs[6] = dom.createMorphAt(dom.childAt(element6, [1]),1,1);
      morphs[7] = dom.createMorphAt(dom.childAt(element6, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","name",["loc",[null,[3,40],[3,48]]]],
      ["element","action",["save",["get","this",["loc",[null,[5,60],[5,64]]]]],[],["loc",[null,[5,44],[5,66]]]],
      ["inline","t",["save"],[],["loc",[null,[5,67],[5,79]]]],
      ["inline","t",["cancel"],[],["loc",[null,[6,42],[6,56]]]],
      ["inline","t",["name"],[],["loc",[null,[13,20],[13,32]]]],
      ["inline","input",[],["name","name","value",["subexpr","@mut",[["get","name",["loc",[null,[16,46],[16,50]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[16,20],[16,80]]]],
      ["inline","t",["email"],[],["loc",[null,[21,20],[21,33]]]],
      ["inline","input",[],["name","email","value",["subexpr","@mut",[["get","email",["loc",[null,[24,47],[24,52]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[24,20],[24,82]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["userprofile"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 20,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("header");
      dom.setAttribute(el1,"class","form-header");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("h1");
      dom.setAttribute(el2,"class","header-title");
      var el3 = dom.createTextNode("UserProfile / ");
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
      dom.setAttribute(el3,"class","btn btm-primary");
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("a");
      dom.setAttribute(el3,"class","btn");
      dom.setAttribute(el3,"href","#/users");
      var el4 = dom.createComment("");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n    ");
      dom.appendChild(el2, el3);
      dom.appendChild(el1, el2);
      var el2 = dom.createTextNode("\n");
      dom.appendChild(el1, el2);
      dom.appendChild(el0, el1);
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"class","form-content");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("fieldset");
      dom.setAttribute(el2,"class","fieldset");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","control-group");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","control-label");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","controls");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createComment("");
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
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
      var el1 = dom.createTextNode("\n");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
      var element0 = dom.childAt(fragment, [0]);
      var element1 = dom.childAt(element0, [3]);
      var element2 = dom.childAt(element1, [1]);
      var element3 = dom.childAt(fragment, [2, 1, 1]);
      var morphs = new Array(6);
      morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
      morphs[1] = dom.createElementMorph(element2);
      morphs[2] = dom.createMorphAt(element2,0,0);
      morphs[3] = dom.createMorphAt(dom.childAt(element1, [3]),0,0);
      morphs[4] = dom.createMorphAt(dom.childAt(element3, [1]),1,1);
      morphs[5] = dom.createMorphAt(dom.childAt(element3, [3]),1,1);
      return morphs;
    },
    statements: [
      ["content","name",["loc",[null,[2,43],[2,51]]]],
      ["element","action",["save"],[],["loc",[null,[4,40],[4,57]]]],
      ["inline","t",["save"],[],["loc",[null,[4,58],[4,70]]]],
      ["inline","t",["cancel"],[],["loc",[null,[5,38],[5,52]]]],
      ["inline","t",["name"],[],["loc",[null,[12,16],[12,28]]]],
      ["inline","input",[],["name","name","value",["subexpr","@mut",[["get","name",["loc",[null,[15,42],[15,46]]]]],[],[]],"required",true,"class","span7"],["loc",[null,[15,16],[15,76]]]]
    ],
    locals: [],
    templates: []
  };
}()));
Ember.TEMPLATES["users"] = Ember.HTMLBars.template((function() {
  var child0 = (function() {
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 11,
            "column": 24
          },
          "end": {
            "line": 11,
            "column": 89
          }
        }
      },
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
        ["inline","t",["users.new.btn_label"],[],["loc",[null,[11,61],[11,88]]]]
      ],
      locals: [],
      templates: []
    };
  }());
  var child1 = (function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.3+c3accfb0",
          "loc": {
            "source": null,
            "start": {
              "line": 29,
              "column": 24
            },
            "end": {
              "line": 35,
              "column": 24
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("                        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","entry-fields");
          var el2 = dom.createTextNode("\n                            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("span");
          dom.setAttribute(el2,"class","entry-field");
          var el3 = dom.createTextNode("\n                            ");
          dom.appendChild(el2, el3);
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n                        ");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n                        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1, 1]),1,1);
          return morphs;
        },
        statements: [
          ["content","id",["loc",[null,[32,28],[32,34]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.3+c3accfb0",
        "loc": {
          "source": null,
          "start": {
            "line": 18,
            "column": 16
          },
          "end": {
            "line": 38,
            "column": 16
          }
        }
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("                ");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","entry-wrap");
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","entry-actions");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("a");
        dom.setAttribute(el3,"class","entry-action action-delete");
        dom.setAttribute(el3,"data-ddbid","{@id}");
        dom.setAttribute(el3,"href","#");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("i");
        dom.setAttribute(el4,"class","fa fa-trash");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n                    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n                    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"data-ddbid","{@id}");
        dom.setAttribute(el2,"class","entry document js-swipe-entry");
        var el3 = dom.createTextNode("\n                        ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("label");
        dom.setAttribute(el3,"class","entry-select");
        var el4 = dom.createTextNode("\n                            ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("input");
        dom.setAttribute(el4,"type","checkbox");
        dom.setAttribute(el4,"name","docid");
        dom.setAttribute(el4,"value","{@docid}");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n                        ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("                    ");
        dom.appendChild(el2, el3);
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
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1, 3]),3,3);
        return morphs;
      },
      statements: [
        ["block","link-to",["users.user",["get","this",["loc",[null,[29,48],[29,52]]]]],["class","entry-link"],0,null,["loc",[null,[29,24],[35,36]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }());
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 44,
          "column": 0
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createElement("section");
      dom.setAttribute(el1,"style","display:flex;");
      var el2 = dom.createTextNode("\n    ");
      dom.appendChild(el1, el2);
      var el2 = dom.createElement("div");
      dom.setAttribute(el2,"class","view");
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-header");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("h1");
      dom.setAttribute(el4,"class","header-title");
      var el5 = dom.createTextNode("User");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("nav");
      dom.setAttribute(el4,"class","nav-action-bar");
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","pull-right");
      var el6 = dom.createTextNode("\n                    page-navigator\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n                ");
      dom.appendChild(el4, el5);
      var el5 = dom.createElement("div");
      dom.setAttribute(el5,"class","-on-desktop");
      var el6 = dom.createTextNode("\n                    ");
      dom.appendChild(el5, el6);
      var el6 = dom.createElement("div");
      dom.setAttribute(el6,"class","action-bar");
      var el7 = dom.createTextNode("\n                        ");
      dom.appendChild(el6, el7);
      var el7 = dom.createComment("");
      dom.appendChild(el6, el7);
      var el7 = dom.createTextNode("\n                    ");
      dom.appendChild(el6, el7);
      dom.appendChild(el5, el6);
      var el6 = dom.createTextNode("\n                ");
      dom.appendChild(el5, el6);
      dom.appendChild(el4, el5);
      var el5 = dom.createTextNode("\n            ");
      dom.appendChild(el4, el5);
      dom.appendChild(el3, el4);
      var el4 = dom.createTextNode("\n        ");
      dom.appendChild(el3, el4);
      dom.appendChild(el2, el3);
      var el3 = dom.createTextNode("\n        ");
      dom.appendChild(el2, el3);
      var el3 = dom.createElement("div");
      dom.setAttribute(el3,"class","view-content");
      var el4 = dom.createTextNode("\n            ");
      dom.appendChild(el3, el4);
      var el4 = dom.createElement("div");
      dom.setAttribute(el4,"class","entries");
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
      var el2 = dom.createComment("");
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
      var element1 = dom.childAt(element0, [1]);
      var morphs = new Array(3);
      morphs[0] = dom.createMorphAt(dom.childAt(element1, [1, 3, 3, 1]),1,1);
      morphs[1] = dom.createMorphAt(dom.childAt(element1, [3, 1]),1,1);
      morphs[2] = dom.createMorphAt(element0,3,3);
      return morphs;
    },
    statements: [
      ["block","link-to",["users.new"],["class","btn"],0,null,["loc",[null,[11,24],[11,101]]]],
      ["block","each",[["get","controller",["loc",[null,[18,24],[18,34]]]]],[],1,null,["loc",[null,[18,16],[38,25]]]],
      ["content","outlet",["loc",[null,[42,4],[42,14]]]]
    ],
    locals: [],
    templates: [child0, child1]
  };
}()));
Ember.TEMPLATES["components/accounts"] = Ember.HTMLBars.template((function() {
  return {
    meta: {
      "revision": "Ember@1.13.3+c3accfb0",
      "loc": {
        "source": null,
        "start": {
          "line": 1,
          "column": 0
        },
        "end": {
          "line": 1,
          "column": 18
        }
      }
    },
    arity: 0,
    cachedFragment: null,
    hasRendered: false,
    buildFragment: function buildFragment(dom) {
      var el0 = dom.createDocumentFragment();
      var el1 = dom.createTextNode("accounts component");
      dom.appendChild(el0, el1);
      return el0;
    },
    buildRenderNodes: function buildRenderNodes() { return []; },
    statements: [

    ],
    locals: [],
    templates: []
  };
}()));