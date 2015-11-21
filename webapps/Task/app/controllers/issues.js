import Em from 'ember';

export default Em.Controller.extend({
    i18n: Em.inject.service(),

    queryParams: ['at', 'st', 'tags', 'u'],
    at: 'all',

    headerTitle: Em.computed('at', function() {
        let at = this.get('at');
        return this.get('i18n').t(at);
    }),

    actions: {
        addComment: function(issue, commentText) {
            let comment = this.store.createRecord('comment', {
                comment: commentText
            });
            issue.comments.pushObject(comment);
        }
    }
});
