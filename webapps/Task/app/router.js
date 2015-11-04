import Em from 'ember';

var Router = Em.Router.extend();

Router.map(function() {

    this.route('dashboard', {
        path: '/'
    });

    this.route('tasks', function() {
        this.route('new');
        this.route('edit', {
            path: '/:task_id/edit',
        });
        this.route('task', {
            path: '/:task_id',
        }, function() {
            this.route('issues', {
                path: 'issues'
            }, function() {
                this.route('new');
                this.route('issue', {
                    path: ':issue_id'
                });
            });
        });
    });

    this.route('issues', {
        path: 'issues'
    }, function() {
        this.route('new');
        this.route('issue', {
            path: ':issue_id'
        });
    });

    this.route('categories', function() {
        this.route('new');
        this.route('category', {
            path: '/:category_id'
        });
    });

    this.route('users', function() {
        this.route('invitation');
    });

    this.route('login');
    this.route('user_profile', {
        path: 'user-profile'
    });

    this.route('error404', {
        path: '/*path'
    });
});

export default Router;
