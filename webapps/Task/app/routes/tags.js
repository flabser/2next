import Em from 'ember';

export default Em.Route.extend({
    beforeModel: function() {
        console.log('tags beforeModel, get check changes');
    },

    model: function() {
        // if has changes this.store.query('tag') else peekAll
        return this.store.peekAll('tag');
    },

    renderTemplate: function(controller, model) {
        var tagsController = this.controllerFor('tags');

        this.render('tags', {
            into: 'application',
            controller: tagsController
        });
    },

    actions: {
        didTransition: function() {
            Em.run.schedule('afterRender', this, function() {
                Em.$('#input-new-tag').val('').focus();
            });
        }
    }
});
