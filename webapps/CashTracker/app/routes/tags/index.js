import Em from 'ember';
import ComposeRecordMixin from '../../mixins/m-compose-record';

export default Em.Route.extend(ComposeRecordMixin, {
    model: function() {
        return this.store.findAll('tag');
    }
});
