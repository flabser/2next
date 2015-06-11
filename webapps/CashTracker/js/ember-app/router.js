define('router', ['ember'], function(Ember) {
    'use strict';

    var Router = Ember.Router.extend({
        // location: 'history'
    });

    Router.map(function() {
        this.route('index', {
            path: '/'
        });

        this.route('profile');

        this.route('transactions');

        this.route('transaction', function() {
            this.route('new');
            this.route('edit', {
                path: '/:transaction_id'
            });
        });

        this.route('users');

        this.route('accounts');

        this.route('account', function() {
            this.route('new');
            this.route('edit', {
                path: '/:account_id'
            });
        });

        this.route('categories');

        this.route('category', function() {
            this.route('new');
            this.route('edit', {
                path: '/:category_id'
            });
        });

        this.route('cost_centers', {
            path: '/costcenters'
        });

        this.route('cost_center', {
            path: '/costcenter'
        }, function() {
            this.route('new');
            this.route('edit', {
                path: '/:costcenter_id'
            });
        });
    });

    return Router;
});
