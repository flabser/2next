function entryOver(cell){
    cell.style.backgroundColor='#FFF1AF';    
}

function removeDocument(curDB){
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){
			el=$(chBoxes[i]);
			$.ajax({
				type: "get",
				dataType:"xml",
				url: "Provider?type=delete&element=document&doctype=" + $(chBoxes[i]).attr("class") + "&docid="+chBoxes[i].value+"&dbid="+curDB,
				success: function(xml){					
					//alert($(xml).find("response").attr('status'))
					if($(xml).find("response").attr('status')=='ok'){
						$(el).parent("td").parent("tr").remove();
						alert("document has removed");
					}else{
						alert("document not removed");
					}
			   },
			   error:function (xhr, ajaxOptions, thrownError){
	               if (xhr.status == 400){
	            	  $("body").children().wrapAll("<div id='doerrorcontent' style='display:none'></div>")
	            	  $("body").append("<div id='errordata'>"+xhr.responseText+"</div>")
	            	  $("li[type='square'] > a").attr("href","javascript:backtocontent()")
	               }
	            }    
			});		
		}
	}
	
	
}

function checkvalueinput(el){
	if ($(el).val().length == 0){
		$(el).val("Введите userid для поиска...")
		$(el).css("color","#ccc");
	}
}

function removevalinput(el){
	if( $(el).val().indexOf("Old password has not match")==-1){
		$(el).val("").css("color","black");
	}
}

function finduserid(el){
	findval=$(el).val()
	$.ajax({
		type: "get",
		dataType:"xml",
		url: "Provider?type=get_users_by_key&dbid=Avanti&app=Avanti&element=user&keyword="+findval,
		success: function(xml){	
			$(".entrylist").remove()
			$(xml).find('users').find('entry').each(function(){
				$(".viewtable").append("<tr class='entrylist'>" +
					"<td width='2%' style='border:1px solid #ccc'><center><input type='checkbox' name='chbox' value='"+$(this).find("key").text()+"'/></center></td>" +
					"<td width='2%' style='border:1px solid #ccc'></td>" +
					"<td width='35%' style='border:1px solid #ccc; padding-left:5px'><a href='Provider?type=edit&element=user&key="+$(this).find("key").text()+"'>"+$(this).find("userid").text()+"</td>" +
					"<td width='35%' style='ont-size:0.8em; border:1px solid #ccc; padding-left:5px'>"+$(this).find("email").text()+"</td>" +
					"<td width='26%' style='font-size:0.8em; border:1px solid #ccc; padding-left:5px'>"+ $(this).find("jid").text() +"</td></tr>")
			})
			$("#usersearch").val(findval);
			$(".entrylist").attr("onmouseover","elemBackground(this,'EEEEEE')")
			$(".entrylist").attr("onmouseout","elemBackground(this,'FFFFFF')")
	   }
	 });	
}

function entryOut(cell){
    cell.style.backgroundColor='#efefef';   
}


function checkAll(allChbox) {
	allChbox.checked ? $("input[name=chbox]").attr("checked","true") : $("input[name=chbox]").removeAttr("checked");
}

function delMaindoc(dbID){	
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){		
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "type=delete_maindoc&id=" + chBoxes[i].value + "&dbid=" + dbID,
				success: function(msg){					
					result = result + 1;
				}
			});		
		}
	}
	if (result > 0){
		alert("Удалено " + result + " документа(ов)"); 
	}else{
		alert("Запрос на удаление отправлен на сервер");
	}
	window.location.reload();
}

function delTask(dbID){	
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){		
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "type=delete_task&id=" + chBoxes[i].value + "&dbid=" + dbID,
				success: function(msg){					
					result = result + 1;
				}
			});		
		}
	}
	if (result > 0){
		alert("Удалено " + result + " документа(ов)"); 
	}else{
		alert("Запрос на удаление отправлен на сервер");
	}
	window.location.reload();
}

function delExecution(dbID){	
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){		
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "type=delete_execution&id=" + chBoxes[i].value + "&dbid=" + dbID,
				success: function(msg){					
					result = result + 1;
				}
			});		
		}
	}
	if (result > 0){
		alert("Удалено " + result + " документа(ов)"); 
		}else{
			alert("Документы не удалены");
		}
	window.location.reload();
}

function delProject(dbID){	
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){		
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "type=delete_project&id=" + chBoxes[i].value + "&dbid=" + dbID,
				success: function(msg){					
					result = result + 1;
				}
			});		
		}
	}
	if (result > 0){
		alert("Удалено " + result + " документа(ов)"); 
	}else{
		alert("Запрос на удаление отправлен на сервер");
	}
	window.location.reload();
}

function delGlossary(dbID){	
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){		
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "type=delete_glossary&id=" + chBoxes[i].value + "&dbid=" + dbID,
				success: function(msg){					
					result = result + 1;
				}
			});		
		}
	}
	if (result > 0){
		alert("Удалено " + result + " документа(ов)"); 
	}else{
		alert("Документы не удалены");
	}
	window.location.reload();
}

function resetRules(app){	
	$.ajax({
		type: "POST",
		url: "Provider",
		data: "type=service&app=" + app + "&operation=reset_all_rules",
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("the rules have been not reloaded" );
				}else{
					alert("the rules have been reloaded");					
				}	
			} );
		}
	});
}


function resetXSLT(app){	
	$.ajax({
		type: "POST",
		url: "Provider",
		data: "type=service&app=" + app + "&operation=reset_xslt",
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("the xslt have been not reloaded" );
				}else{
					alert("the xslt have been reloaded");					
				}	
			} );
		}
	});
}

function delUser(){	
	var i; var result = false;
	var chBoxes = document.getElementsByName('chbox');
	for (i = 0; i < chBoxes.length; i++) {	
		if (chBoxes[i].checked){		
			userID = chBoxes[i].value;
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "type=delete&element=user&id=" + userID,
				success: function(xml){
					$(xml).find('response').each(function(){
						var st=$(this).attr('status');
						if (st=="error"){
							alert("user has not been deleted" );
						}else{
							alert("user has been deleted");		
						}	
						window.location.reload();
					} );
				}
			});		
		}
	}
}

function delOrganization(dbID){	
	var i; var result = false;
	var chBoxes = document.getElementsByName('chbox');
	for (i = 0; i < chBoxes.length; i++) {	
		if (chBoxes[i].checked){		
			orgID = chBoxes[i].value;
			$.ajax({
				type: "GET",
				url: "Provider?type=structure&id=delete_organization&dbid=" + dbID + "&key=" + orgID,
				success: function(xml){
					$(xml).find('response').each(function(){
						var st=$(this).attr('status');
						if (st=="error"){
							alert("organization has not been deleted" );
						}else{
							alert("organization has been deleted");		
							window.location.reload();
						}	
					} );
				}
			});		
		}
	}
}

function removeLogFile(){	
	var logsID = $("input[name=id]:checked").serialize();
	$.ajax({
		type: "GET",
		url: "Provider?type=delete&element=log",
		data:logsID,
		cache:false,
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("log file has not been deleted");
				}else{
					alert("log file has been deleted");		
				}
			} );
		}
	});	
}

function completeDeleteUser(){	
	window.location.reload();
}

function showError(){	
	alert('user is not deleted')
}

function delDoc(ruleid){	
	var i; var result = false;
	var chBoxes = document.getElementsByName('chbox');
	for (i = 0; i < chBoxes.length; i++) {	
		if (chBoxes[i].checked){			
			var dataArray = new Array();
			dataArray[0] = new FormData('type', 'del'); 
			dataArray[1] = new FormData('id', ruleid); 
			dataArray[2] = new FormData('key', i);
			result = submitDynamicForm("Provider",dataArray);			
		}
	}
	if (result){
		alert('Документы удалены'); 
	}else{
		alert('Документы удалить не удалось');
	}
}