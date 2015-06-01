define('router', ['backbone'], function(Backbone) {

    'use strict';

    var Router = Backbone.Router.extend({
        initialize: function(App) {
            this.App = App;
        },

        routes: {
            '': 'transactions',
            '!': 'transactions',
            '!transactions': 'transactions',
            '!transaction/:transaction_id': 'transaction',
            '!accounts': 'accounts',
            '!account/:account_id': 'account',
            '!categories': 'categories',
            '!category/:category_id': 'category',
            '!costcenters': 'costcenters',
            '!costcenter/:costcenter_id': 'costcenter',
        },

        transactions: function() {
            console.log(this);
            this.App.TransactionsView.render();
        },
        transaction: function(transaction_id) {
            this.App.TransactionView.render(transaction_id);
        },
        accounts: function() {
            this.App.AccountsView.render();
        },
        account: function(account_id) {
            this.App.AccountView.render(account_id);
        },
        categories: function() {
            this.App.CategoriesView.render();
        },
        category: function(category_id) {
            this.App.CategoryView.render(category_id);
        },
        costcenters: function() {
            this.App.CostCentersView.render();
        },
        costcenter: function(costcenter_id) {
            this.App.CostCenterView.render(costcenter_id);
        }
    });

    return Router;
});
