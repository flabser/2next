import Em from 'ember';
import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
    attrs: {
        children: {
            embedded: 'always'
        }
    },

    serializeBelongsTo: function(snapshot, json, relationship) {
        var key = relationship.key;
        var belongsTo = snapshot.belongsTo(key);

        key = this.keyForRelationship ? this.keyForRelationship(key, 'belongsTo', 'serialize') : key;
        json[key] = Em.isNone(belongsTo) ? belongsTo : belongsTo.record.id;
    },

    serializeHasMany: function(snapshot, json, relationship) {
        var key = relationship.key;
        if (key === 'children') {
            return;
        } else {
            this._super.apply(this, arguments);
        }
    }
});
