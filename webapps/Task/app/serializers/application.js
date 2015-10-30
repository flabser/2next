import DS from 'ember-data';

export default DS.RESTSerializer.extend({
    serializeAttribute: function(snapshot, json, key, attribute) {
        if (attribute.options.readOnly) {
            return;
        }
        return this._super(snapshot, json, key, attribute);
    }
});
