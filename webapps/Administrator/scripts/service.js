function requetsService(value){
	var pars = 'type='+value+'&'+Math.random();
	var myAjax = new Ajax.Updater('view', 'Service', {method: 'get', parameters: pars});
}	

function resetCache(cacheID){
	$.ajax({
		type: "get",
		url: "Provider?type=service&operation=reset_cache&cacheid="+cacheID ,
		success: function(){					
			window.location.reload()
	   }
	});		
}

function Numeric(el) {
	$(el).keypress(function (e) {
		if ((e.which < 48) || (e.which > 57) ) {
			if( (e.which != 8) ){
				return false
			}
		}
	});
}

function updateDBver(text, app){
	 var myDiv = document.createElement("DIV");
	   divhtml ="<div id='dialog-message' title='Database version'  >";
	   divhtml+="<span style='height:40px; width:100%; text-align:center;'>"+
	   			"<input id='newverdb' type='text' onkeydown='javascript:Numeric(this)' value='"+ text +"' style='font-size:13px; width:100px; padding:3px; margin: 5px auto'></input>"+"</span>";
	   divhtml += "</div>";
	   myDiv.innerHTML = divhtml;
	   document.body.appendChild(myDiv);
	   $("#dialog").dialog("destroy");
	   $( "#dialog-message" ).dialog({
		modal: true,
		buttons: {
			"Ok": function() {
				$.ajax({
					type: "POST",
					url: "Provider?type=service&operation=run_database_patch&app="+app+"&id="+ $("#newverdb").val(),
					datatype:"html",
					data: $("form").serialize(),
					success: function(xml){
						 $("#currentDBversion").html($("newverdb").val())
						$( this ).dialog( "close" );
						$( this ).remove();
					},
					error:function (xhr, ajaxOptions, thrownError){
						$( this ).dialog( "close" );
						$( this ).remove();
		            }    
				});
				
			}
		}
	});
	$( ".ui-dialog button" ).focus();
}

function delRule(type){
	var i; 
	result = 0;
	var chBoxes = $("input[name^='chbox']");
	for (i = 0; i < chBoxes.length; i++) {		
		if (chBoxes[i].checked){		
			$.ajax({
				type: "POST",
				url: "Provider",
				data: "Provider?type=delete&element=" + type+"&id="+chBoxes[i].value,
				success: function(msg){					
					result = result + 1;
			   }
			});		
		}
	}
	if (result > 0){
		alert("������� " + result + " ���������(��)"); 
	}else{
		alert("������ �� �������� ��������� �� ������");
	}
	window.location.reload();
	
}

function nextStartUpdate(){
	$("#nextStart").html(" ")
}

function trigerUpdate(el){
	if($(el).val() != "SCHEDULER"){
		$(".sheduler").css("display","none")
	}else{
		$(".sheduler").attr("style","")
	}
}

function handlesorceUpdate(app){
	$("#tohandlevalue option").remove()
	if($("#tohandlesource").val() != "QUERY"){
		$("#tohandlevalue").append("<option value='ALLTASKS'>ALLTASKS</option>");
		$("#tohandlevalue").append("<option value='DOCUMENTS'>DOCUMENTS</option>");
	}else{
		$.ajax({
			url: 'Provider?type=get_query_list&app='+app+'&onlyxml',
			datatype:'xml',
			success: function(data) {
			maxpage = $(data).find("query").attr("maxpage");
			i=1
			k=2
			if (maxpage > i ){
				$(data).find("entry[doctype=queryrule]").each(function(index, element){
					 $("#tohandlevalue").append("<option value='"+ $(element).attr("docid")+"'>"+ $(element).attr("docid") +"</option>")
				});
				while ( i<maxpage){
						$.ajax({
							url: 'Provider?type=get_query_list&app='+app+'&amp;page='+k+'&onlyxml',
							datatype:'xml',
							success: function(data) {
								$(data).find("entry[doctype=queryrule]").each(function(index, element){
									  $("#tohandlevalue").append("<option value='"+ $(element).attr("docid")+"'>"+ $(element).attr("docid") +"</option>")
								});
							}
						});	
					i++;
					k++;
				}
			}else{
				$(data).find("entry[doctype=queryrule]").each(function(index, element){
					  $("#tohandlevalue").append("<option value='"+ $(element).attr("docid")+"'>"+ $(element).attr("docid") +"</option>")
				});
			}
		}
	});	
	}
}

function openActivityProperties(el){
	$(el).parent('td').parent("tr").next("tr").attr("style","border-top:1px solid #ccc;  border-bottom:1px solid #ccc;");
	$(el).attr("src","/SharedResources/img/classic/minus.gif");
	$(el).attr("onclick","javascript:closeActivityProperties(this)");
}

function closeActivityProperties(el){
	$(el).parent('td').parent("tr").next("tr").attr("style","display:none");
	$(el).attr("src","/SharedResources/img/classic/plus.gif");
	$(el).attr("onclick","javascript:openActivityProperties(this)");
}

function changeTypeQuery(ruleid, app, dbid){
	$.ajax({
		type: "POST",
		dataType:"html",
		url: "Provider?type=edit&element=query_rule&id="+ruleid+"&app="+app+"&dbid="+dbid,
		data: $("form").serialize(),
		success: function(data){
			window.location.reload()
		}
});
}

function execute(form) {	
	if ($("#script").attr("value") == ''){		
		alert ('���������� ������ ������');
		return;
	}
	$("#result").text("");
	$("#console").empty();
	//$("#console").html("<font style='font-size:12px;'>Starting handler </font><font style='font-size:12px; color:blue'>"+ $("#id").val()+"</font><br/>")
	$.ajax({
			type: "POST",
			url: "Provider?type=service&operation=do_handler",
			dataType:"xml",
			data: $("#" + form).serialize(),
			success: function(data){
				status = $(data).find("handler").attr("status");
				if (status == "ok"){
					$("#console").append("<font style='font-size:12px;'>execution status </font><font style='font-size:12px; color:blue'> "+ $("#id").val()+"</font><font style='font-size:12px;'> - OK</font><br/>")
					$(data).find("console_output").find("entry").each(function(index, element){
						$("#console").append("<font style='font-size:12px;'>"+$(this).text()+"</font><br/>")
					});
				
					$("#result").html("<font style='font-size:12px;'>"+$(data).find("string_result").text()+"</font><br/>")
				}else{
					$("#console").append("<font style='font-size:12px;'>execution status - </font><font style='font-size:12px; color:red; '>Error</font><br/>")
					$("#console").append("<font style='font-size:12px; color:red;'>"+$(data).find("console_output").text()+"</font><br/>")
					$("#console").append("<font style='font-size:12px; color:red;'>ERROR :"+$(data).find("error").text()+"</font><br/>")
				}
			}
	});
}


/*function execute(form) {	
	if ($("#script").attr("value") == ''){		
		alert ('���������� ������ ������');
		return;
	}
	$("#result").text("");
	$.ajax({
			type: "POST",
			url: "Provider",
			data: $("#" + form).serialize(),
			success: function(msg){
				$("#result").text(msg);
			}
	});
}
*/
function changeRegNumber(key,number, app){
	$.ajax({
		type: "GET",
		url: "Provider?type=service&operation=post_reg_num&key="+key+"&value="+number+"&app="+app,
		success: function(msg){
			window.location.reload();
		}
	});
}

function restore_backup(backup_id,app){
	$.ajax({
		type: "GET",
		url: "Provider?type=service&operation=restore_from_backup&id="+backup_id+"&app="+app,
		success: function(msg){
			window.location.reload();
		}
	});
}

function sync(id,app){
	divhtml="<div class='picklistsmall' id='picklist'>" +
				"<div class='headersmall'>" +
					"<font style='font-family:arial; font-size:1em'>������������� </font>" +
					"<div class='closeButton'  onclick='closePicklist(); '>" +
						"<img src='/SharedResources/img/classic/smallcancel.gif' style='border:0;'/>" +
					"</div>" +
				"</div>" +
				"<div class='contentsmall'>" +
					"<font>���� �������������</font>" +
				"</div>" +
				"<div class='buttonPane' style='margin-top:13%;padding-right:1%; text-align:right; '>" +
					"<a href='javascript:closePicklist()'>" +
						"<font class='button'>��</font>" +
					"</a>&#xA0;&#xA0;" +
				"</div>"+
			"</div>";
	$("body").append(divhtml);

/* ���������� drag and drop */
	$("#picklist").draggable({handle:".header"});

/* ����� ����� �������� ������ ������������ ���������� ����*/
	centring('picklist',150,300); // (id ����, ������ ����, ������ ����)

/* �������� div ��� ���������� ������� ��� ����� �������� ������ ������������*/
	blockWindow = "<div  class = 'blockWindow' id = 'blockWindow'></div>"; 
	$("body").append(blockWindow);
	$('#blockWindow').css('height',document.body.clientHeight); 
	$('#blockWindow').css('width',document.body.clientWidth);
	$.ajax({
		type: "get",
		url: 'Provider?type=do_sync&id='+id+'&app='+app,
		async:'false',
		error:function(){
			$(".contentsmall").html("<font>������������� ����������� �������</font>")
		},
		complete: function(xml){
			msg=$(xml).find("message").text();
			$(".contentsmall").html("<font>������������� ���������  "+msg+"</font>");
		}
	});
}

var field; 

function autocomplete(el){
	field= el;
	if ($(field).val().length !=0){
		$(document).click(function(e){ 
			if ($(e.target).attr("id") != "autocompleteDiv" ) {
				$("#autocompleteDiv").remove()
			}
		});
		$.ajax({
			type: "get",
			datatype:"xml",
			url: 'Provider?type=get_users_by_key&keyword='+ $(el).val(),
			async:'true',
			error:function(){
				alert("error")
			},
			success: function(xml){
				if ($('#autocompleteDiv').length){
					$('#autocompleteDiv').html("")
					$('#autocompleteDiv').append("<table id='autocompleteTable' width='100%'></table>")
					$(xml).find("entry").each(function () {
						user=$(this).find("userid").text();
						$("#autocompleteTable").append("<tr onmouseover='entryOver(this)' onclick='selectId(this)' onmouseout='entryOut(this)'><td><font style='margin-left:3px'>"+user+"</font></td></tr>")	
						$("#autocompleteTable tr").css("cursor","pointer");    
					});
				}else{
					var offset = $(el).offset();
					div="<div id='autocompleteDiv'></div>"
						$("body").append(div);
					$('#autocompleteDiv').html("")
					$('#autocompleteDiv').append("<table id='autocompleteTable' width='100%'></table>")
					$(xml).find("entry").each(function () {
						user=$(this).find("userid").text();
						$("#autocompleteTable").append("<tr onmouseover='entryOver(this)' onclick='selectId(this)' onmouseout='entryOut(this)'><td><font style='margin-left:3px'>"+user+"</font></td></tr>")	
						$("#autocompleteTable tr").css("cursor","pointer");
					});
					$("#autocompleteDiv").attr("style","width:176px; height:200px; border: 1px solid #ccc; z-index:5; display:block; position:absolute; background:#ffffff; overflow:auto")
					$("#autocompleteDiv").css('left',offset.left)
					$("#autocompleteDiv").css('top',offset.top + 25)
				}
			}
		});
		}else{
			$("#autocompleteDiv").remove();
		}
}

function selectIDtoField(el){
	field= el;
	$(document).click(function(e){ 
		if ($(e.target).attr("id") != "autocompleteDiv" ) {
			$("#autocompleteDiv").remove()
		}
	});
	$.ajax({
		type: "get",
		datatype:"xml",
		url: 'Provider?type=get_users_by_key&keyword=',
		async:'true',
		error:function(){
			alert("error")
		},
		success: function(xml){
			if ($('#autocompleteDiv').length){
				$('#autocompleteDiv').html("")
				$('#autocompleteDiv').append("<table id='autocompleteTable' width='100%'></table>")
				$(xml).find("entry").each(function () {
					user=$(this).find("userid").text();
					$("#autocompleteTable").append("<tr onmouseover='entryOver(this)' onclick='selectId(this)' onmouseout='entryOut(this)'><td><font style='margin-left:3px'>"+user+"</font></td></tr>")	
					$("#autocompleteTable tr").css("cursor","pointer");    
				});
			}else{
				var offset = $(el).offset();
				div="<div id='autocompleteDiv'></div>"
					$("body").append(div);
				$('#autocompleteDiv').html("")
				$('#autocompleteDiv').append("<table id='autocompleteTable' width='100%'></table>")
				$(xml).find("entry").each(function () {
					user=$(this).find("userid").text();
					$("#autocompleteTable").append("<tr onmouseover='entryOver(this)' onclick='selectId(this)' onmouseout='entryOut(this)'><td><font style='margin-left:3px'>"+user+"</font></td></tr>")	
					$("#autocompleteTable tr").css("cursor","pointer");
				});
				$("#autocompleteDiv").attr("style","width:176px; height:200px; border: 1px solid #ccc; z-index:5; display:block; position:absolute; background:#ffffff; overflow:auto")
				$("#autocompleteDiv").css('left',offset.left)
				$("#autocompleteDiv").css('top',offset.top + 25)
			}
		}
	}
	);
}

function selectId(el){
	id = $(el).text();
	$(field).val(id)
	$(field).attr("readonly","readonly")
	$(field).removeAttr("onkeyup");
	$("#linktofullstructure"+$(field).attr('name')).remove()
	newfield='<tr class="new'+$(field).attr('name')+'">'+
					'<td style="text-align:right">&#xA0;'+$(field).attr('name')+'&#xA0;</td>'+
					'<td style="font-size:17px;">'+
						'<input type="text" name="'+$(field).attr('name')+'" size="25" style="margin-left:5px" onkeyup="javascript:autocomplete(this)">'+
						'</input>&#xA0;<a id="linktofullstructure'+$(field).attr('name')+'" href="javascript:selectIDtoField($(&quot;.new'+$(field).attr('name')+' input&quot;))" style="font-size:12px">Select</a>'+
					'</td>'+
					'<td></td>'+
			'</tr>'
	$("<img style='cursor:pointer; margin-right:5%'  onclick='removetr(this)' src='img/closesmall.gif'/>").insertBefore(".new"+$(field).attr('name')+":first td:first font");				
	//$(".new"+$(field).attr('name')+":first td:first").append("<img style='cursor:pointer'  onclick='removetr(this)' src='img/closesmall.gif'/>");
	$(".new"+$(field).attr('name')).after(newfield);
	$(".new"+$(field).attr('name')+":first").removeAttr("class");
	$("#autocompleteDiv").remove();
}

function removetr(el){
	$(el).parents('tr:first').remove()	
}