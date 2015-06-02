define('App', [
    'views/application',
    'controllers/application',

    'routes/transactions',
    'views/transactions',
    'controllers/transactions',

    'routes/transaction',
    'views/transaction',
    'controllers/transaction',

    'routes/accounts',
    'views/accounts',
    'controllers/accounts',

    'routes/account',
    'views/account',
    'controllers/account',

    'routes/categories',
    'views/categories',
    'controllers/categories',

    'routes/category',
    'views/category',
    'controllers/category',

    'routes/cost_centers',
    'views/cost_centers',
    'controllers/cost_centers',

    'routes/cost_center',
    'views/cost_center',
    'controllers/cost_center',

    'routes/users',
    'views/users',
    'controllers/users',

    'routes/user',
    'views/user',
    'controllers/user',

    /* route */
    'router'
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

    Router) {

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

        Router: Router
    };

    return App;
});
