CT.Router = Ember.Router.extend({
    // location: 'history'
});

CT.Router.map(function() {
    this.route('index', {
        path: '/'
    });

    this.route('profile');

    this.route('transactions', function() {
        this.route('new');
    });

    this.route('transaction', {
        path: '/transaction/:transaction_id'
    });

    this.route('users', function() {
        this.route('new');
    });

    this.route('user', {
        path: '/user/:user_id'
    });

    this.route('accounts', function() {
        this.route('new');
    });

    this.route('account', {
        path: '/account/:account_id'
    });

    this.route('categories', function() {
        this.route('new');
    });

    this.route('category', {
        path: '/category/:category_id'
    });

    this.route('cost_centers', {
        path: '/costcenters'
    }, function() {
        this.route('new');
    });

    this.route('cost_center', {
        path: '/costcenter/:costcenter_id'
    });
});
