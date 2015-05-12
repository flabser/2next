function fieldOnBlur(field) {
	field.style.backgroundColor = '#FFFFFF';
}

function Numeric() {
	if ((event.keyCode < 48) || (event.keyCode > 57)) {
		event.returnValue = false;
	}
}

function saveRule(ruleid, app, dbid) {
	$.ajax({
		type: "POST",
		dataType:"html",
		url: "Provider?type=save&element=query_rule&id"+ruleid+"&app="+app+"&dbid="+dbid,
		data: $("form").serialize(),
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("rule has been not saved" );
				}else{
					alert("rule has been saved");
					window.location.href = document.referrer;
				}	
			});
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

function saveHandler() {
	$.ajax({
		type: "POST",
		dataType:"html",
		url: "Provider?type=save&element=handler_rule",
		data: $("form").serialize(),
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("handler has been not saved" );
				}else{
					alert("handler has been saved");
					window.location.href = document.referrer;
				}	
			});
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

function selectLoginMode(element,app){
	if(($(element).val() == '0')||($(element).val() == '2')){
		$("[name=question_"+app+"]").attr("readonly","readonly").val("").attr("class","readonly");
		$("[name=answer_"+app+"]").attr("readonly","readonly").val("").attr("class","readonly");
	}else{
		$("[name=question_"+app+"]").removeAttr("readonly").removeAttr("class")
		$("[name=answer_"+app+"]").removeAttr("readonly").removeAttr("class")
	}
}

function backtocontent(){
	$('#doerrorcontent').css('display','block'); 
	$('#errordata').remove();
}


var oldpassword = null

function changePassword(ParentObj){
	if (oldpassword == null){
		oldpassword = $("#pwd1").val() ;
	}
	$("#pwd1").val("") 
	$("#pwd1").removeAttr("readonly") 
	newElem="<tr>";
	newElem+="<td class='fc'>Re-enter password:&#xA0;</td>";
	newElem+="<td>";
	newElem+="<input type='password' id='pwd2' size='20'> </input>&#xA0; ";
	newElem+="<font style='font-size:12px; text-decoration:underline ; color:blue; cursor:pointer' onclick='javascript:cancelChangePassword(this)'>"
	newElem+="Cancel"
	newElem+="</font>";
	newElem+="<font  style='font-size:12px; margin-left:10px; text-decoration:underline ; color:blue; cursor:pointer' onclick='javascript:okChangePassword(this)'>"
	newElem+="Ok"
	newElem+="</font>";
	newElem+="</td>";
	newElem+="</tr>";
	$(newElem).insertAfter($(ParentObj).parents('tr') );
	$(ParentObj).remove();
}

function cancelChangePassword(ParentObj){
	$(ParentObj).parents('tr').remove();
	$("#pwd1").val(oldpassword);
	newElem="<font  style='font-size:12px; margin-left:2%; text-decoration:underline ; color:blue; cursor:pointer' onclick='javascript:changePassword(this)'>"
	newElem+="Change password"
	newElem+="</font>";
	$(newElem).insertAfter($("#pwd1"));
	$("#pwd1").attr("readonly","readonly") 
}

function okChangePassword(ParentObj){
	if ($("#pwd1").val() != $("#pwd2").val() || $("#pwd1").val().length == 0 || $("#pwd2").val().length == 0){
		alert("Your password do not match. Please try again.")
		return false;
	}
	$(ParentObj).parents('tr').remove();
	newElem="<font  style='font-size:12px; margin-left:2%; text-decoration:underline ; color:blue; cursor:pointer' onclick='javascript:changePassword(this)'>"
		newElem+="Change password"
			newElem+="</font>";
	$(newElem).insertAfter($("#pwd1"));
	$("#pwd1").attr("readonly","readonly") 
}

var countFieldsRule = null;

function addFieldToRule(){
	if(countFieldsRule == null){
		countFieldsRule=$("#rulefieldstable tr").length ;
	}
	newelem="<tr id='newfield"+countFieldsRule+"'><td style='font-size:10px'><a href='javascript:okaddfield("+countFieldsRule+")'>Ok</a>&#xA0;<a href='javascript:canceladdfield("+countFieldsRule+")'>Cancel</a></td>"
	newelem+="<td><input type='text' id='name' style='width:100%'></input></td>"
	newelem+="<td><input type='text' id='valuesource' style='width:100%'></input></td>"
	newelem+="<td><input type='text' id='value' style='width:100%'></input></td>"
	newelem+="<td><input type='text' id='macro' style='width:100%'></input></td>"
	newelem+="<td><input type='text' id='iferror' style='width:100%'></input></td>"
	newelem+="<td><input type='text' id='publishas' style='width:100%'></input></td></tr>"
	$("#rulefieldstable").append(newelem);
	countFieldsRule++;
}

function canceladdfield (count){
	$("#newfield"+count).remove()
}

function okaddfield (num){
	parentObj=$("#newfield"+num +" td");
	if ($(parentObj).children("#name").val().length == 0 ){
		alert("please enter field 'name'");
		$(parentObj).children("#name").focus();
	}else{
		if($(parentObj).children("#value").val().length == 0 ){
			alert("please enter field 'value'");
			$(parentObj).children("#value").focus();
		}else{
			resultvalue=$("#name").val()+'#'+$("#publishas").val()+'#'+$("#source").val()+'^'+$("#value").val()+'^type'
			$(parentObj).each(function(){
				inputval=$(this).children("input").val();
				if (inputval != undefined){
					$(this).html(inputval);
				}else{
					$(this).attr("align","center");
					$(this).html("<input type='checkbox' name='chbox' id='chbox'/>");
				}
			});
			$("#newfield"+num).append("<input type='hidden' value='"+resultvalue+"' name='field'>")
		}
	}
}

function editRuleFields(){
	if (countFieldsRule == null){
		countFieldsRule=1
	}
	$("input[name^='chbox']:checked").each(function(){
		parenttr=$(this).parent('td').parent('tr')
		$(parenttr).attr("id","newfield"+countFieldsRule)
		name=parenttr.children('td').eq(1).text()
		valuesource=parenttr.children('td').eq(2).text()
		value=parenttr.children('td').eq(3).text()
		macro=parenttr.children('td').eq(4).text()
		iferror=parenttr.children('td').eq(5).text()
		publishas=parenttr.children('td').eq(6).text()
		newelem="<td style='font-size:10px'><a href='javascript:okaddfield("+countFieldsRule+")'>Ok</a>&#xA0;<a href='javascript:canceleditfield("+countFieldsRule+")'>Cancel</a></td>"
		newelem+="<td><input type='text' id='name' style='width:100%'/></td>"
		newelem+="<td><input type='text' id='valuesource' style='width:100%'/></td>"
		newelem+="<td><input type='text' id='value' style='width:100%'/></td>"
		newelem+="<td><input type='text' id='macro' style='width:100%'/></td>"
		newelem+="<td><input type='text' id='iferror' style='width:100%'/></td>"
		newelem+="<td><input type='text' id='publishas' style='width:100%'/></td>"
		parenttr.html(newelem)
		$(parenttr).children("td").children("#name").val(name)
		$(parenttr).children("td").children("#valuesource").val(valuesource)
		$(parenttr).children("td").children("#value").val(value)
		$(parenttr).children("td").children("#macro").val(macro)
		$(parenttr).children("td").children("#iferror").val(iferror)
		$(parenttr).children("td").children("#publishas").val(publishas)
		$(parenttr).append("<input type='hidden' id='prevname' value=''/>")
		$(parenttr).append("<input type='hidden' id='prevvaluesource' value=''/>")
		$(parenttr).append("<input type='hidden' id='prevvalue' value=''/>")
		$(parenttr).append("<input type='hidden' id='prevmacro' value=''/>")
		$(parenttr).append("<input type='hidden' id='previferror' value=''/>")
		$(parenttr).append("<input type='hidden' id='prevpublishas' value=''/>")
		$(parenttr).children("#prevname").val(name)
		$(parenttr).children("#prevvaluesource").val(valuesource)
		$(parenttr).children("#prevvalue").val(value)
		$(parenttr).children("#prevmacro").val(macro)
		$(parenttr).children("#previferror").val(iferror)
		$(parenttr).children("#prevpublishas").val(publishas)
		countFieldsRule++;
	});
}

function canceleditfield (count){
	parenttr=$("#newfield"+count);
	parenttr.children('td').eq(0).html("<input type='checkbox' name='chbox' id='chbox'></input>");
	parenttr.children('td').eq(1).html(parenttr.children("#prevname").val());
	parenttr.children('td').eq(2).html(parenttr.children("#prevvaluesource").val());
	parenttr.children('td').eq(3).html(parenttr.children("#prevvalue").val());
	parenttr.children('td').eq(4).html(parenttr.children("#prevmacro").val());
	parenttr.children('td').eq(5).html(parenttr.children("#previferror").val());
	parenttr.children('td').eq(6).html(parenttr.children("#prevpublishas").val());
}

function removeRuleFields(){
	$("input[name^='chbox']:checked").each(function(){
		$(this).parent('td').parent('tr').remove();
	});
}

function saveUser() {		
	var formData = $("form").serialize();
	$.ajax({
		type: "POST",
		url: "Provider?type=save&element=user_profile",
		data: formData,
		beforeSend: function(){
			if (($("#pwd1").val() != $("#pwd2").val() || $("#pwd1").val().length == 0 || $("#pwd2").val().length == 0) && $("input[name=key]").val().length == 0){
				alert("Your password do not match. Please try again.")
				return false;
			}else{
				$(".enabledappsname").each(function(index, element){
					name=$(element).val();
					$(".QA_"+name).each(function(index,element){
						if( $(element).children("td").children("[name=question_"+name+"]").attr("class") !="readonly"){
							qa = $(element).children("td").children("[name=question_"+name+"]").val()+"`"+$(element).children("td").children("[name=answer_"+name+"]").val()
							var input = $('<input>', {
								name: "QA_"+name,
								type: 'hidden',
								value: qa
							});
							$("form").append(input);
						}
					})
				});
			}
			
		},
		success: function(data){
			alert("user has been saved");
			window.location.href = document.referrer;
		},
		error: function(data){
			alert("user has not been saved");
		}
	});
}

function AddNewQuestAndAnswer(element,app){
	parenttr = $(element).parent("td").parent("tr");
	qcaption="<font style='font-size:12px'>Question: </font>";
	acaption="<font style='font-size:12px'>Answer: </font>";
	var qinput ="<input type='text' size='30' name='question_"+app+"'/>";
	var ainput ="<input type='text' size='30' name='answer_"+app+"'/>";
	addnewqa = "<a style='margin-left:5px' href='javascript:$.noop()' onclick='javascript:AddNewQuestAndAnswer(this,"+ "&quot;" +app+ "&quot;" +")'>add</a>";
	removeqa = "<a style='margin-left:5px' href='javascript:$.noop()' onclick='javascript:RemoveQuestAndAnswer(this,"+ "&quot;" +app+ "&quot;" +")'>remove</a>";
	$("<tr class='QA_"+app+"'><td></td> <td></td> <td>"+qcaption+qinput+"</td><td>"+acaption+ainput+addnewqa+ removeqa + "</td> </tr>").insertAfter($(parenttr))
}

function RemoveQuestAndAnswer(element,app){
	parenttr = $(element).parent("td").parent("tr").remove();
}

function saveEmployer(userid) {		
	var formData = $("form").serialize();
	$.ajax({
		type: "POST",
		url: "Provider?type=save_employer&key="+userid,
		data: formData,
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("user has been not saved" );
				}else{
					alert("user has been saved");
					window.location=history.back()
				}	
			} );
		}
	});
}	

function saveBoss(userid) {		
	var formData = $("form").serialize();
	$.ajax({
		type: "POST",
		url: "Provider?type=save_boss&key="+userid,
		data: formData,
		success: function(xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st=="error"){
					alert("user has been not saved" );
				}else{
					alert("user has been saved");
					window.location=history.back()
				}	
			} );
		}
	});
}	

function completeSaveUser() {
	alert("user save")
}

function SaveForm(typeForm, formName) {
	var formData = Form.serialize(formName);
	var myAjax = new Ajax.Request('Provider', {
		method : 'POST',
		postBody : formData,
		onComplete : showResponse,
		onFailure : showError
	});
}

function showResponse(req) {
	var ready = req.readyState;
	var status = req.status;
	var doc = null;
	if (ready == 4) {
		data = req.responseXML;
		if (data != null) {
			if (status == 200) {
				var xmldoc = data.documentElement;
				var st = xmldoc.getAttribute('status');
				var msg = xmldoc.getElementsByTagName('message');
				if (st == 'ok') {
					if (msg[0].text != '') {
						msg = msg[0].text
						redirect = xmldoc.getElementsByTagName('redirect')[0].text;
						notification(msg, 'info', redirect);
					} else {
						msg = "�������� ��������";
						redirect = xmldoc.getElementsByTagName('redirect')[0].text;
						notification(msg, 'info', redirect);
					}
				} else if (st == 'validationerror') {
					msgText = '';
					for (i = 0; i < msg.length; i++) {
						msgText = msgText + msg[i].firstChild.data + "\n";
					}
					alertBox(msgText, 'warning');
				} else if (st == 'error') {
					alertBox(msg[0].firstChild.data, 'error');
				} else {
					alert('������ ������ ����������� ������: ' + st);
				}
			} else {
				msg = '������ ������ : ' + status;
				alertBox(msg, 'warning');
				document.write(req.responseText);
			}
		} else {
			msg = '�� ������� �������� ����� � ���������� �������� ����������';
			alertBox(msg, 'warning');
		}
	}
}

function showError(req) {
	msg = '�������� ������ ��� ����������!';
	alert(msg);
}

/*set of upload function*/
function submitFile(form, tableID, fieldName) {
	if ($(fieldName).value == '') {
		alert('������� ��� ����� ��� ��������');
	} else {
		form = $(form);
		var frame = createIFrame();
		frame.onSendComplete = function() {
			uploadComplete(tableID, getIFrameXML(frame));
		};
		form.setAttribute('target', frame.id);
		form.submit();
		form.reset();
	}
}
var cnt = 0;

function uploadComplete(tableID, doc) {
	if (!doc)
		return;
	// if (typeof(element)=="string") element=document.getElementById(element);
	var xmldoc = doc.documentElement;
	var st = xmldoc.getAttribute('status');
	var msg = xmldoc.getElementsByTagName('message');
	if (st = 'ok') {
		var table = document.getElementById(tableID);
		var newrow = table.insertRow(table.row);
		newrow.insertCell().innerHTML = msg[0].firstChild.data;
	} else {
		alert('��������� ������ �� ������� ��� �������� �����');
	}
}

function createIFrame() {
	var id = 'f' + Math.floor(Math.random() * 99999);
	var div = document.createElement('div');
	var divHTML = '<iframe style="display:none" src="about:blank" id="' + id
			+ '" name="' + id + '" onload="sendComplete(\'' + id
			+ '\')"></iframe>';
	div.innerHTML = divHTML;
	document.body.appendChild(div);
	return document.getElementById(id);
}

function sendComplete(id) {
	var iframe = document.getElementById(id);
	if (iframe.onSendComplete && typeof (iframe.onSendComplete) == 'function')
		iframe.onSendComplete();
}

function getIFrameXML(iframe) {
	var doc = iframe.contentDocument;
	if (!doc && iframe.contentWindow)
		doc = iframe.contentWindow.document;
	if (!doc)
		doc = window.frames[iframe.id].document;
	if (!doc)
		return null;
	if (doc.location == "about:blank")
		return null;
	if (doc.XMLDocument)
		doc = doc.XMLDocument;
	return doc;
}

function alertBox(msg, status, redirect) {
	if ($('box')) {
		return false;
	}
	var message = msg;
	divhtml = "<div class='box' id='box'>";
	divhtml += "<div  class='headerBox' onmousedown='move(box)' ><font style='font-size:0.8em'>SmartDoc</font>";
	divhtml += "<div class='closeButton'  onclick='closeAlertBox();'><img src='sdimg/smallcancel.gif' style='border:0;'/>";
	divhtml += "</div></div>";
	divhtml += "<div id='contentpane'  style='width:400px; height:70px;'></div>";
	if(redirect){
		divhtml += "<input type='button' id='button' onClick='closeAlertBox(redirect)' value='ok' style='width:75px; margin-top:15px;margin-left:45px'/>"
	}else{
		divhtml += "<input type='button' id='button' onClick='closeAlertBox()' value='ok' style='width:75px; margin-top:15px;margin-left:45px'/>"
	}
	divhtml += "</div>";
	var block = "<div class='blockWindow' id='blockWindow'/>";
	var myDiv = document.createElement("DIV");
	var MyBlockWindow = document.createElement("DIV");
	myDiv.innerHTML = divhtml;
	MyBlockWindow.innerHTML = block;
	document.body.appendChild(myDiv);
	document.body.appendChild(MyBlockWindow);
	$('blockWindow').style.height = document.body.clientHeight;
	$('blockWindow').style.width = document.body.clientWidth;
	$('blockWindow').style.display = "block";
	var w = document.body.clientWidth;
	var h = document.body.clientHeight;
	var winH = 80;
	var winW = 400;
	var scrollA = document.body.scrollTop;
	var scrollB = document.body.scrollLeft;
	$('box').style.top = scrollA + ((h / 2) - (winH / 2));
	$('box').style.left = scrollB + ((w / 2) - (winW / 2));
	$('box').style.display = 'block';
	document.body.className = "noselect";
	if (status == 'warning') {
		$('contentpane').innerHTML = "<img onLoad='transparency ()' src='sdimg/messagebox_warning.png' style='border:none;margin-right:350px; margin-top:20px;'/>";
	} else {
		$('contentpane').innerHTML = "<img onLoad='transparency ()' src='sdimg/messagebox_info.png' style='border:none;margin-right:350px; margin-top:20px;'/>";
	}
	if (status == 'error') {
		$('contentpane').innerHTML = "<img onLoad='transparency ()' src='sdimg/messagebox_critical.png' style='border:none;margin-right:350px; margin-top:20px;'/>";
	}
	$('contentpane').innerHTML += "<div style='margin-top:-45px; margin-left:50px; width:350px; font:13px'><font1 >"
			+ message + "</font1></div>";

}

function closeAlertBox(redirect) {
	var delAlertBox = $('box').parentNode;
	var parentAlertBox = document.body;
	parentAlertBox.removeChild(delAlertBox);
	document.body.className = "";
	var delBlockWindow = $('blockWindow').parentNode;
	var parentBlockWindow = document.body;
	parentBlockWindow.removeChild(delBlockWindow);
	if (redirect) {
		window.location = redirect;
	}
}

function saveDocument(typeForm, formName){
	var myDiv = document.createElement("DIV");
	divhtml ="<div id='dialog-message' title='Save'>";
	divhtml+="<br/>" +"<table style='width:100%; margin-top:23px;>" +
		"<tr>" +
		"<td><font style='font-size:17px'>Please wait...</font></td>" +
		"</tr>" +
		"<tr>" +
		"<td><font style='font-size:13px'>document is saving</font></td>" +
		"</tr>" +
		"<tr>" +
		"<td align='center'>" +
		"<img id='loadingimg' style='margin-top:23px' src='/SharedResources/img/classic/loading.gif'/>" +
		"</td>" +
		"</tr>" +
		"</span>";
	 divhtml += "</div>";
	 myDiv.innerHTML = divhtml;
	 document.body.appendChild(myDiv);
	 $("#dialog").dialog("destroy");
	 $("#dialog-message").dialog({
		   modal: true,
		   height:251
	 });
	var formData = $("form").serialize();
	$.ajax({
		type: "POST",
		url: 'Provider',
		data: $("form").serialize(),
		success:function (xml){
			$(xml).find('response').each(function(){
				var st=$(this).attr('status');
				if (st =="error" || st =="undefined"){
					$( "#dialog-message" ).dialog({ height: 210 });
					$( "#dialog-message" ).dialog({ buttons: { 
						Ok: function() {
							$(this).dialog('close');
							$( "#dialog-message" ).remove()
						}
					} 
					});
					msgtext=$(xml).find('message').text();
					$("#loadingimg").remove()
					$( "#dialog-message" ).dialog({ title: 'Notice' });
					if (msgtext.length==0){
						$("#dialog-message" ).html("<br/><span style='height:35px; margin-top:10px;'>"+"<font style='font-size:13px; margin-left:16%;'>Document dont saved</font>"+"</span>");
					}else{
						$("#dialog-message" ).html("<br/><span style='height:35px'><font style='font-size:14px'>"+msgtext+"</font></span>");
					}
				}
				if (st == "ok"){
					$( "#dialog-message" ).dialog({ height: 210 });
					$( "#dialog-message" ).dialog({ buttons: { 
						Ok: function() {
							$(this).dialog('close');
							$( "#dialog-message" ).remove()
							window.history.back();
						}
					} 
					});
					msgtext=$(xml).find('message').text();
					$("#loadingimg").remove()
					redirect=redirecturl;
					$( "#dialog-message" ).dialog({ title: 'Notice' });
					if (msgtext.length==0){
						$("#dialog-message" ).html("<br/><span style='height:35px; margin-top:10px;'>"+"<font style='font-size:13px; margin-left:16%;'>Document saved</font></span>");
					}else{
						$("#dialog-message" ).html("<br/><span style='height:35px'><font style='font-size:14px'>"+msgtext+"</font></span>");
					}
				}	
			});
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