define(['ember'], function(Ember) {
    'use strict';

    var Router = Ember.Router.extend();

    Router.map(function() {
        this.resource('index', {
            path: '/'
        });

        this.resource('transactions', {
            path: '/transactions'
        });

        this.resource('transaction', {
            path: '/transaction'
        }, function() {
            this.resource('transaction.new', {
                path: '/new'
            });
            this.resource('transaction.edit', {
                path: '/:transaction_id'
            });
        });

        this.resource('users', {
            path: '/users'
        });

        this.resource('accounts', {
            path: '/accounts'
        });

        this.resource('account', {
            path: '/account'
        }, function() {
            this.resource('account.new', {
                path: '/new'
            });
            this.resource('account.edit', {
                path: '/:account_id'
            });
        });

        this.resource('categories', {
            path: '/categories'
        });

        this.resource('category', {
            path: '/category'
        }, function() {
            this.resource('category.new', {
                path: '/new'
            });
            this.resource('category.edit', {
                path: '/:category_id'
            });
        });

        this.resource('cost_centers', {
            path: '/costcenters'
        });

        this.resource('cost_center', {
            path: '/costcenter'
        }, function() {
            this.resource('cost_center.new', {
                path: '/new'
            });
            this.resource('cost_center.edit', {
                path: '/:costcenter_id'
            });
        });
    });

    return Router;
});
