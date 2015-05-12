nbApp.dialogChoiceCategory = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "category",
		title : el.title,
		dialogClass : "nextbase-dialog-responsive",
		draggable : false,
		filter : false,
		resizable : false,
		width : 500,
		height : $(window).height(),
		href : "Provider?type=page&id=picklist-category&page=1",
		onExecute : function() {
			if (nb.form.setValues(dlg, null)) {
				var selected = $("[data-type='select']:checked", dlg[0]);

				var type_op;
				var requireDocument;
				var requireCostCenter;

				if ($(selected[0]).hasClass("js-response")) {
					var _parent = $(selected[0]).parents(".js-parent");
					var parentCat = _parent.children("label:first");
					var parentCatName = parentCat.text();
					$("#categorytbl").html(parentCatName + " / " + $("#categorytbl").text());

					type_op = $("[name='viewtext3']", parentCat).val();
					requireDocument = $("[name='viewtext5']", parentCat).val() == "1";
					requireCostCenter = $("[name='viewtext6']", parentCat).val() == "1";
				} else {
					type_op = $("[data-id=" + selected[0].value + "][name='viewtext3']").val();
					requireDocument = $("[data-id=" + selected[0].value + "][name='viewtext5']").val() == "1";
					requireCostCenter = $("[data-id=" + selected[0].value + "][name='viewtext6']").val() == "1";
				}

				if (type_op == "in" || type_op == "out") {
					$("input[name=targetcash]").val("");
					$("#targetcashtbl").html("");
					$("#control-row-targetcash").hide();
				} else if (type_op == "transfer") {
					$("#control-row-targetcash").show();
					$("input[name=costcenter]").val("");
					$("#costcentertbl").html("");
				} else if (type_op == "calcstuff") {
					$("#control-row-targetcash").show();
					$("input[name=costcenter]").val("");
					$("#costcentertbl").html("");
				} else if (type_op == "getcash") {
					requireCostCenter = true;
				} else if (type_op == "withdraw") {

				}

				if (requireDocument) {
					$("[name=documented]").attr("required", "required").attr("checked", true);
					$("[name=documented]").attr("onclick", "return false");
					$("[name=documented]").attr("onkeydown", "return false");
				} else {
					$("[name=documented]").removeAttr("required").removeAttr("disabled");
					$("[name=documented]").attr("onclick", null);
					$("[name=documented]").attr("onkeydown", null);
				}

				if (requireCostCenter) {
					$("[name=costcenter]").attr("required", "required");
				} else {
					$("[name=costcenter]").removeAttr("required").removeAttr("disabled");
				}

				$("#subcategorytbl").html("");
				$("#typeoperationtbl").attr("class", "operation-type-icon-" + type_op).attr("title", type_op);

				$("input[name=subcategory]").val("");

				dlg.dialog("close");
			}
		},
		buttons : {
			"ok" : {
				text : nb.strings.select,
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.strings.cancel,
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		open : function(e) {
			makeResponsiveDialog(e);

			if (Modernizr.touch) {
				$(".form-wrapper").addClass("hidden");
			}
		},
		close : function() {
			if (Modernizr.touch) {
				$(".form-wrapper").removeClass("hidden");
			}
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceTargetCash = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "targetcash",
		title : el.title,
		dialogClass : "nextbase-dialog-responsive",
		draggable : false,
		filter : false,
		resizable : false,
		width : 500,
		height : $(window).height(),
		href : "Provider?type=page&id=picklist-cash&page=1",
		buttons : {
			"ok" : {
				text : nb.strings.select,
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.strings.cancel,
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		open : function(e) {
			makeResponsiveDialog(e);

			if (Modernizr.touch) {
				$(".form-wrapper").addClass("hidden");
			}
		},
		close : function() {
			if (Modernizr.touch) {
				$(".form-wrapper").removeClass("hidden");
			}
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceCostCenter = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "costcenter",
		title : el.title,
		dialogClass : "nextbase-dialog-responsive",
		draggable : false,
		filter : false,
		resizable : false,
		width : 500,
		height : $(window).height(),
		href : "Provider?type=page&id=picklist-costcenter&page=1",
		buttons : {
			"ok" : {
				text : nb.strings.select,
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.strings.cancel,
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		open : function(e) {
			makeResponsiveDialog(e);

			if (Modernizr.touch) {
				$(".form-wrapper").addClass("hidden");
			}
		},
		close : function() {
			if (Modernizr.touch) {
				$(".form-wrapper").removeClass("hidden");
			}
			$("[name=basis]").focus();
		}
	});
};

nbApp.dialogChoiceAccessRoles = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "accessroles",
		title : el.title,
		dialogClass : "nextbase-dialog-responsive",
		draggable : false,
		filter : false,
		resizable : false,
		width : 500,
		height : $(window).height(),
		href : "Provider?type=view&id=picklist-roles&page=1",
		buttons : {
			"ok" : {
				text : nb.strings.select,
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.strings.cancel,
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

function makeResponsiveDialog(e) {
	var dlgw = $(e.target).parents("[role=dialog]");
	var btnHeight = $(".ui-dialog-buttonpane", dlgw).outerHeight();
	var dlgwHeight = $(dlgw).outerHeight();

	dlgw.css("height", dlgwHeight - btnHeight);
	$(e.target).css("height", dlgwHeight - (btnHeight * 2) - 10);
}
