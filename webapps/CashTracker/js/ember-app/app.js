define('App', [
    'views/ApplicationView',
    'controllers/ApplicationController',

    'routes/TransactionsRoute',
    'views/TransactionsView',
    'controllers/TransactionsController',

    'routes/TransactionRoute',
    'views/TransactionView',
    'controllers/TransactionController',

    'routes/AccountsRoute',
    'views/AccountsView',
    'controllers/AccountsController',

    'routes/AccountRoute',
    'views/AccountView',
    'controllers/AccountController',

    'routes/CategoriesRoute',
    'views/CategoriesView',
    'controllers/CategoriesController',

    'routes/CategoryRoute',
    'views/CategoryView',
    'controllers/CategoryController',

    'routes/CostCentersRoute',
    'views/CostCentersView',
    'controllers/CostCentersController',

    'routes/CostCenterRoute',
    'views/CostCenterView',
    'controllers/CostCenterController',

    'routes/UsersRoute',
    'views/UsersView',
    'controllers/UsersController',

    'routes/UserRoute',
    'views/UserView',
    'controllers/UserController',

    /* route */
    'router',
    'routes/IndexRoute'
], function(
    ApplicationView,
    ApplicationController,

    TransactionsRoute,
    TransactionsView,
    TransactionsController,

    TransactionRoute,
    TransactionView,
    TransactionController,

    AccountsRoute,
    AccountsView,
    AccountsController,

    AccountRoute,
    AccountView,
    AccountController,

    CategoriesRoute,
    CategoriesView,
    CategoriesController,

    CategoryRoute,
    CategoryView,
    CategoryController,

    CostCentersRoute,
    CostCentersView,
    CostCentersController,

    CostCenterRoute,
    CostCenterView,
    CostCenterController,

    UsersRoute,
    UsersView,
    UsersController,

    UserRoute,
    UserView,
    UserController,

    Router,
    IndexRoute) {

    'use strict';

    var App = {
        ApplicationView: ApplicationView,
        ApplicationController: ApplicationController,

        TransactionsRoute: TransactionsRoute,
        TransactionsView: TransactionsView,
        TransactionsController: TransactionsController,

        TransactionRoute: TransactionRoute,
        TransactionView: TransactionView,
        TransactionController: TransactionController,

        AccountsRoute: AccountsRoute,
        AccountsView: AccountsView,
        AccountsController: AccountsController,

        AccountRoute: AccountRoute,
        AccountView: AccountView,
        AccountController: AccountController,

        CategoriesRoute: CategoriesRoute,
        CategoriesView: CategoriesView,
        CategoriesController: CategoriesController,

        CategoryRoute: CategoryRoute,
        CategoryView: CategoryView,
        CategoryController: CategoryController,

        CostCentersRoute: CostCentersRoute,
        CostCentersView: CostCentersView,
        CostCentersController: CostCentersController,

        CostCenterRoute: CostCenterRoute,
        CostCenterView: CostCenterView,
        CostCenterController: CostCenterController,

        UsersRoute: UsersRoute,
        UsersView: UsersView,
        UsersController: UsersController,

        UserRoute: UserRoute,
        UserView: UserView,
        UserController: UserController,

        Router: Router,
        IndexRoute: IndexRoute
    };

    return App;
});
