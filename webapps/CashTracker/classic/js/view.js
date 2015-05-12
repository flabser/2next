/*
 * appendSaldoToElTitleByDocDate
 */
nbApp.appendSaldoToElTitleByDocDate = function(el, ddbid) {
	nbApp.xhrGetSaldoOnDate(ddbid).then(function(result) {
		$(el).attr("title", result);
	});
};

$(document).ready(function() {
	$(".js_saldo_on_date", ".view").on("mouseover", function() {
		nbApp.appendSaldoToElTitleByDocDate(this, $(this).data("ddbid"));
		$(this).off("mouseover");
	});
});
