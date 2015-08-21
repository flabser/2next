import Em from 'ember';

export default Em.Mixin.create({
    actions: {
        composeRecord: function() {
            this.get('controller').send('composeRecord');
        }
    }
});
