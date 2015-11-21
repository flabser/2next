import Em from 'ember';

export default Em.Controller.extend({
    i18n: Em.inject.service(),

    queryParams: ['at', 'st', 'tags', 'u'],
    at: 'all',

    headerTitle: Em.computed('at', function() {
        let at = this.get('at');
        return this.get('i18n').t(at);
    })
});
