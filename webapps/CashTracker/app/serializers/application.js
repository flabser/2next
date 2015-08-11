import DS from 'ember-data';

export default DS.RESTSerializer.extend({
    serializeAttribute: function(record, json, key, attribute) {
        if (attribute.options.readOnly) {
            return;
        }
        return this._super(record, json, key, attribute);
    }
});
