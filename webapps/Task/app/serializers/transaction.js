import Em from 'ember';
import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
    attrs: {
        attachments: {
            embedded: 'always'
        },
        account: {
            embedded: 'always'
        },
        transferAccount: {
            embedded: 'always'
        },
        category: {
            embedded: 'always'
        },
        costCenter: {
            embedded: 'always'
        },
        tags: {
            embedded: 'always'
        }
    },

    serializeIntoHash: function(hash, type, snapshot, options) {
        options = options || {};
        options.includeId = true;

        var data = this.serialize(snapshot, options);

        if (data.transactionType !== 'T') {
            data.transferAccount = null;
        }

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
