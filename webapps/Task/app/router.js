import Em from 'ember';

var Router = Em.Router.extend();

Router.map(function() {

    this.route('issues', function() {
        this.route('new');
        this.route('issue', {
            path: '/:issue_id/edit',
        });
    });

    this.route('tags', function() {
        this.route('new');
        this.route('tag', {
            path: '/:tag_id'
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
