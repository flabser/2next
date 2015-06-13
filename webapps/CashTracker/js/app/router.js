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
    });

    this.route('transaction', {
        path: '/transactions/:transaction_id'
    });

    this.route('accounts', function() {
        this.route('new');
    });

    this.route('account', {
        path: '/accounts/:account_id'
    });

    /*this.route('account', {
        path: '/accounts/:account_id'
    });*/

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

    this.route('users', function() {
        this.route('new');
        this.route('edit', {
            path: '/:user_id'
        });
    });
});

Ember.Route.reopen({
    redirect: function() {
        if (this.routeName === 'index') {
            this.transitionTo('transactions');
        }
    }
});
