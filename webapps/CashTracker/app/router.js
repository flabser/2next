import Em from 'ember';
import config from './config/environment';

var Router = Em.Router.extend({
    // location: config.locationType
});

Router.map(function() {
    this.route('index', {
        path: '/'
    });

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
        path: 'cost-centers'
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
            path: '/:user_id'
        });
    });

    this.route('dashboard');
    this.route('reports');
    this.route('login');
    this.route('ws');

    this.route('user_profile', {
        path: 'user-profile'
    });

    this.route('error404', {
        path: '/*path'
    });
});

Em.Route.reopen({
    redirect: function() {
        if (this.routeName === 'index') {
            this.transitionTo('dashboard');
        }
    }
});

export default Router;
