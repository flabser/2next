// выполняем сскрипт после загрузки DOM:
$(function(){
	//Фильтрованные диаграммы
 	$('table')
 		.visualize({
	 		rowFilter: ':not(:last)',
	 		colFilter: ':not(:last-child)'
	 	})
	 	.before("<h2>Б) Исключаем последнюю строку и последний столбец</h2><pre><code>$('table').visualize({<strong>rowFilter: ':not(:last)', colFilter: ':not(:last-child)'</strong>});</code></pre>");
 	
 	$('table')
 		.visualize()
 		.before("<h2>A) Диаграмма без фильтров</h2><pre><code>$('table').visualize();</code></pre>")
});