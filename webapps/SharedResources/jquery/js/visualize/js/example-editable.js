
//Делаем таблицу редактируемой, обновляем диаграммы в функции blur
$(function(){	
	 $('table').visualize({type: 'pie', height: '300px', width: '420px'});

	$('table td')
		.click(function(){
			if( !$(this).is('.input') ){
				$(this).addClass('input')
					.html('<input type="text" value="'+ $(this).text() +'" />')
					.find('input').focus()
					.blur(function(){
						//Удаляем класс td, удаляем ввод
						$(this).parent().removeClass('input').html($(this).val() || 0);
						//Обновляем диаграмму	
						$('.visualize').trigger('visualizeRefresh');
					});					
			}
		})
		.hover(function(){ $(this).addClass('hover'); },function(){ $(this).removeClass('hover'); });
});