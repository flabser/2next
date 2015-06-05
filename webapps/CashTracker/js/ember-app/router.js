define('router', ['ember'], function(Ember) {
    'use strict';

    var Router = Ember.Router.extend();

    Router.map(function() {
        this.resource('index', {
            path: '/'
        });

        this.resource('profile');

        this.resource('transactions');

        this.resource('transaction', function() {
            this.route('new');
            this.route('edit', {
                path: '/:transaction_id'
            });
        });

        this.resource('users');

        this.resource('accounts');

        this.resource('account', function() {
            this.route('new');
            this.route('edit', {
                path: '/:account_id'
            });
        });

        this.resource('categories');

        this.resource('category', function() {
            this.route('new');
            this.route('edit', {
                path: '/:category_id'
            });
        });

        this.resource('cost_centers', {
            path: '/costcenters'
        });

        this.resource('cost_center', {
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
