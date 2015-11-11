import Em from 'ember';
import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
    attrs: {
        attachments: {
            embedded: 'always'
        },
        tags: {
            embedded: 'always'
        },
        children: {
            embedded: 'always'
        }
    },

    serializeIntoHash: function(hash, type, snapshot, options) {
        options = options || {};
        options.includeId = true;

        var data = this.serialize(snapshot, options);

        data.tags = snapshot.hasMany('tags', {
            ids: true
        });

        hash[type.modelName] = data;
    },

    serializeBelongsTo: function(snapshot, json, relationship) {
        var key = relationship.key;
        var belongsTo = snapshot.belongsTo(key);

        key = this.keyForRelationship ? this.keyForRelationship(key, 'belongsTo', 'serialize') : key;
        json[key] = Em.isNone(belongsTo) ? belongsTo : belongsTo.record.id;
    },
});
