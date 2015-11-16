import Em from 'ember';

export default Em.Controller.extend({

    showAddAction: false,
    canEditTags: true,
    canEditUsers: true,

    filterTags: Em.computed.filter('tags', function(tag, index, array) {
        /*let list = this.get('tags');

        return list.filter((t) => {
            let t_isNew = t.get('isNew');
            return !t_isNew;
        });*/
        return !tag.get('isNew');
    })
});
