CT.Router = Ember.Router.extend({
    // location: 'history'
});

CT.Router.map(function() {
    this.route('index', {
        path: '/'
    });

    this.route('userprofile');

    this.route('transaction', {
        path: '/transactions/:transaction_id'
    });

    this.route('transactions', function() {
        this.route('new');
    });

    this.route('account', {
        path: '/accounts/:account_id'
    });

    this.route('accounts', function() {
        this.route('new');
    });

    this.route('category', {
        path: '/categories/:category_id'
    });

    this.route('categories', function() {
        this.route('new');
    });

    this.route('cost_center', {
        path: '/costcenters/:costcenter_id'
    });

    this.route('cost_centers', {
        path: '/costcenters'
    }, function() {
        this.route('new');
    });

    this.route('tag', {
        path: '/tags/:tag_id'
    });

    this.route('tags', function() {
        this.route('new');
    });

    this.route('user', {
        path: '/users/:user_id'
    });

    this.route('users', function() {
        this.route('new');
    });

    this.route('login');
});

Ember.Route.reopen({
    beforeModel: function() {
        if (!this.session.get('auth_user') && this.routeName !== 'login') {
            this.transitionTo('login');
        }
    },
    redirect: function() {
        if (this.routeName === 'index') {
            this.transitionTo('transactions');
        }
    }
});
