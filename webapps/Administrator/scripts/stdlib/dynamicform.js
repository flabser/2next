function DynamicForm(){
	this.arrayIndex = 0;
	this.dataArray = new Array;
}

DynamicForm.prototype.addData = function(field, value) {
    this.dataArray[this.arrayIndex] = new FormData(field, value);
    this.arrayIndex++;
}

DynamicForm.prototype.submit = function(){
   this.createForm();
   $('dynaform').request({
		asynchronous:false,
		onComplete: onCompleteHandler,
		onException: function(e){
			alert("XML parsing error");
   		}
	});	
}

function onCompleteHandler(req){
	if ( req.readyState == 4 ) {
		data = req.responseXML;	
		if (data != null){
			if (req.status == 200){	
				var xmldoc = data.documentElement;  
				var resp = xmldoc.getElementsByTagName('response');
				var st = resp.item(0).getAttribute('status');					
				if (st =='ok'){						
					var msg = xmldoc.getElementsByTagName('message'); 
					if (msg.length > 0){ 
						msgText = msg[0].text;
						alert(msgText);
					}	
					var redirect = xmldoc.getElementsByTagName('redirect');	
					
					if (redirect.text != null){
						window.location = redirect[0].text;
					}
				}
			}
		}
	}
}

DynamicForm.prototype.createForm = function(){
	var formObj = document.createElement( "form" );

	attr = document.createAttribute("action");
	attr.value = 'Provider';			
	formObj.setAttributeNode(attr);	

	attr = document.createAttribute("name");
	attr.value = "dynaform";			
	formObj.setAttributeNode(attr);	

	attr = document.createAttribute("id");
	attr.value = "dynaform";			
	formObj.setAttributeNode(attr);	

	attr = document.createAttribute("method");
	attr.value = "post";			
	formObj.setAttributeNode(attr);	

	attr = document.createAttribute("enctype");
	attr.value = "application/x-www-form-urlencoded";			
	formObj.setAttributeNode(attr);

	for (var i = 0; i < this.dataArray.length; i++) {
		var input = document.createElement("input");
		attr = document.createAttribute("type");
		attr.value = "hidden";			
		input.setAttributeNode(attr);			
		attr = document.createAttribute("name");
		attr.value = this.dataArray[i].field;			
		input.setAttributeNode(attr);
		attr = document.createAttribute("id");
		attr.value = this.dataArray[i].field;			
		input.setAttributeNode(attr);
		attr = document.createAttribute("value");
		attr.value = this.dataArray[i].value;			
		input.setAttributeNode(attr);
		formObj.appendChild(input);  
	}
	document.body.appendChild(formObj);
}

function FormData(field, value){
	this.field = field;
	this.value = value;
}