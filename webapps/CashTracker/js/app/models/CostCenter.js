define('CostCenter', ['underscore', 'backbone.localStorage'], function(_, Backbone) {

	var store = new Backbone.LocalStorage(window.store || 'CostCenter');

	var CostCenter = Backbone.Model.extend({
		url: '',
		localStorage: store,
		defaults: {
			name: ''
		},
		validate: function(attrs) {
			if (_.isEmpty(attrs.name)) {
				return 'Missing name';
			}
		}
	});

	var CostCenters = Backbone.Collection.extend({
		localStorage: store,
		model: CostCenter
	});

	return {
		Model: CostCenter,
		Collection: CostCenters
	};
});
