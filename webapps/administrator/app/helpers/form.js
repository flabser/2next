AdminApp.SelectedHelper = Ember.Helper.helper(function(foo, bar) {
	return foo == bar ? ' selected' : '';
});