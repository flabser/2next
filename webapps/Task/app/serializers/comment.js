import DS from 'ember-data';

export default DS.RESTSerializer.extend(DS.EmbeddedRecordsMixin, {
    serializeIntoHash: function(hash, type, snapshot, options) {
        options = options || {};
        options.includeId = true;

        var data = this.serialize(snapshot, options);

        delete data['author'];
        delete data['regDate'];

        hash[type.modelName] = data;
    }
});
