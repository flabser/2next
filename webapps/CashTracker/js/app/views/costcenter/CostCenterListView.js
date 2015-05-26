nbApp.views.CostCenterListView = function() {

	function CostCenterListView() {

	}

	CostCenterListView.prototype.render = function(costCenter) {
		console.log(costCenter.name);
	};

	return CostCenterListView;
};
