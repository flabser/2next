import Em from 'ember';
import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
    attrs: {
        attachments: {
            embedded: 'always'
        },
        task: {
            embedded: 'always'
        },
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
});
