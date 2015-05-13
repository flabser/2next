// Запускаем срипт после того, как будет загружена DOM:
$(function(){
 $('table').visualize({type: 'pie', height: '300px', width: '420px'});
	$('table').visualize({type: 'bar', width: '420px'});
	$('table').visualize({type: 'area', width: '420px'});
	$('table').visualize({type: 'line', width: '420px'});
});