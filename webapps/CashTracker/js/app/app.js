define('App', ['backbone',
    'views/ApplicationView',
    'views/TransactionsView',
    'views/TransactionView',
    'views/AccountsView',
    'views/AccountView',
    'views/CategoriesView',
    'views/CategoryView',
    'views/CostCentersView',
    'views/CostCenterView',
    'views/UsersView',
    'views/UserView'
], function(Backbone,
    ApplicationView,
    TransactionsView,
    TransactionView,
    AccountsView,
    AccountView,
    CategoriesView,
    CategoryView,
    CostCentersView,
    CostCenterView,
    UsersView,
    UserView) {

    'use strict';

    var App = {
        ApplicationView: new ApplicationView(),
        TransactionsView: new TransactionsView(),
        TransactionView: new TransactionView(),
        AccountsView: new AccountsView(),
        AccountView: new AccountView(),
        CategoriesView: new CategoriesView(),
        CategoryView: new CategoryView(),
        CostCentersView: new CostCentersView(),
        CostCenterView: new CostCenterView(),
        UsersView: new UsersView(),
        UserView: new UserView()
    };

    App.ApplicationView.render();

    return App;
});
