var tableField;
var hiddenField;
var formName;
var entryCollections = new Array();
var field;
var isMultiValue;
var table;
var form;

function closePicklist(){ 
	$("#picklist").empty().remove();
	$('#blockWindow').remove();
}

function search(){
	var value=$('search').value;
    var table = document.getElementById('table');
    var trList= table.getElementsByTagName('tr');
    var e = document.getElementsByName('chbox');
    var y=0;
    var x=0;
    var z=0;
    for (var i=0; i<trList.length;i++){ 
	tdList = trList[i].getElementsByTagName('td'); 
	trList[i].style.display="block";
	if (value.length>0){
		if(tdList[0].innerHTML!=""){
			y++;
			}
		if ((trList[i].innerText).substring(0,value.length).toLowerCase() != value.toLowerCase()){
			trList[i].style.display='none';
			}else{
				if(tdList[0].innerHTML!=""){
					x++;
					}
				if(tdList[0].innerHTML!=""){
					z=y;
					}
				}
		if (tdList[0].innerHTML==""){
			trList[i].style.display='none';
			}
		}
	}
    if (x==1){
    	obj=e[z-1];
    	if( e[z-1].checked==false){
    		e[z-1].checked=true;
    		obj.focus();
    		$('search').focus();
    		}
    	}
    }

/* picklist interface */

function keyDown(el){
	if(event.keyCode==27){
		$("#"+el).css("display","none");
		$("#"+el).empty();
		$("#"+el).remove();
		$("#blockWindow").remove();
	}
}

function pickListSingleOk(docid){ 
	text=$("#"+docid).attr("value");
	$("input[name="+field+"]").remove();
	if (field == "signer"){
		$("#coordBlockSign").remove();
		$("#frm").append("<input type='hidden' name='coordBlock' id='coordBlockSign' value='new`tosign`0`"+docid+"'.>")
	}
	if (field=='executor'){
		$("#intexectable tr:gt(0)").remove();
		$("input [name=executor]").remove();
		num=$("#intexectable tr").length;
		addExecutorField(num,docid);
		$('#intexectable').append("<tr>" +
				"<td style='text-align:center'>"+num+"</td>" +
				"<td>"+text+"<input  type='hidden' id='idContrExec"+num+"' value='"+docid+"'/></td>" +
				"<td id='controlOffDate"+num+"'></td>" +
				"<td id='idCorrControlOff"+num+"'></td>" +
				"<td id='switchControl"+num+"'><a href='javascript:controlOff("+num+")'><img  title='����� � ��������' src='/SharedResources/img/classic/exec_control.gif'/><a/></td>" +
		"</tr>");
	}else{
		$("#frm").append("<input type='hidden' name='"+field+"'  id='"+field+"' value='"+docid+"'.>")
	}
	newTable="<table id="+ table +" width='380px' style='border:1px solid #ccc; '><tr><td>"+ text +"</td></tr></table>"
	$("#"+table).replaceWith(newTable)
	pickListClose(); 
}

function pickListBtnOk(table,form,field){
	var k=0;
	var chBoxes = $('input[name=chbox]'); 
	for(var i = 0; i < chBoxes.length; i ++ ){
		if(chBoxes[i].checked){ 
			if (k==0){
				if (field=='executor'){
					$("#intexectable tr:gt(0)").remove();
				}
				newTable="<table id="+ table +" width='380px' ></table>"
				$("#"+ table).replaceWith(newTable)
				$("input[name="+field+"]").remove();
			}
			k=k+1;
			$("#"+ table).append("<tr><td style='border:1px solid #ccc'>"+chBoxes[i].value+"</td></tr>");
				if (field == "signer"){
					$("#coordBlockSign").remove();
					$("#frm").append("<input type='hidden' name='coordBlock'  id='coordBlockSign' value='new`tosign`0`"+chBoxes[i].id+"`367"+"'>")
				}
				if (field=='executor'){
					num=$("#intexectable tr").length;
					addExecutorField(num,chBoxes[i].id);
					$('#intexectable').append("<tr>" +
							"<td style='text-align:center'>"+num+"</td>" +
							"<td>"+chBoxes[i].value+"<input  type='hidden' id='idContrExec"+num+"' value='"+chBoxes[i].id+"'/></td>" +
							"<td id='controlOffDate"+num+"'></td>" +
							"<td id='idCorrControlOff"+num+"'></td>" +
							"<td id='switchControl"+num+"'><a href='javascript:controlOff("+num+")'><img  title='����� � ��������' src='/SharedResources/img/classic/exec_control.gif'/><a/></td>" +
					"</tr>");
				}else{
					$("#"+ form).append("<input type='hidden' name='"+field+"' id='"+field+"' value='"+chBoxes[i].id+"'/>")
				}
		}
	}
	if (k>0){
		pickListClose();  
	}else{
		alert('Выберите значение');
	}
}

function pickListClose(){ 
	$("#picklist").empty().remove();
	$('#blockWindow').remove();
}

function pickListSingleCoordOk(docid){ 
	text=$("#"+docid).attr("value");
	$("input[name=coorder]").remove();
	$("#frm").append("<input type='hidden' name='coorder' id='coorder' value='"+docid+"'/>")
	newTable="<table id='coordertbl' width='100%'><tr><td>"+ text +"</td></tr></table>"
	$("#coordertbl").replaceWith(newTable);
	closePicklistCoord();  
}

function closePicklistCoord(){
	$("#picklist").remove();
	$("#blockWindow").css('z-index','2');
}

function centring(id,wh,ww){
	var w=document.body.clientWidth; 
	var h=document.body.clientHeight;
	var winH=wh; 
	var winW=ww;
	var scrollA=$("body").scrollTop(); 
	var scrollB=$("body").scrollLeft();
	htop=scrollA+((h/2)-(winH/2))
	hleft=scrollB+((w/2)-(winW/2))
	$('#'+id).css('top',htop) ;
	$('#'+id).css('left',hleft) ;
}

function entryOver(cell){
	cell.style.backgroundColor='#FFF1AF';
}

function entryOut(cell){
	cell.style.backgroundColor='#FFFFFF';
}

function closeDialog(el){
	$("#"+el).empty().remove();
	$("#blockWindow").remove();
}

function fastCloseDialog(){
	$("#picklist").css("display","none");
}

jQuery.fn.extend({
    disableSelection : function() {
    	this.each(function() {
    		this.onselectstart = function() { return false; };
    		this.unselectable = "on";
    		jQuery(this).css('-moz-user-select', 'none');
    	});
    },
    enableSelection : function() {
    	this.each(function() {
    		this.onselectstart = function() {};
    		this.unselectable = "off";
    		jQuery(this).css('-moz-user-select', 'auto');
    	});
    }
});