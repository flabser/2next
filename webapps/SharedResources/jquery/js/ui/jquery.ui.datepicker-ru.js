jQuery(function($) {
	$.datepicker.regional['ru'] = {
		dateFormat : 'dd.mm.yy',
		firstDay : 1,
		isRTL : false,
		showMonthAfterYear : false,
		yearSuffix : ''
	};
	$.datepicker.setDefaults($.datepicker.regional['ru']);
});