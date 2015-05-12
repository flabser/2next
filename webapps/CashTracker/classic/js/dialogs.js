nbApp.dialogChoiceCategory = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "category",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
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
				$("input[name=typeoperation]").val(type_op);

				$("input[name=subcategory]").val("");

				dlg.dialog("close");
			}
		},
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceTargetCash = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "targetcash",
		title : el.title,
		href : "Provider?type=page&id=picklist-cash&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("input[name=summa]").focus();
		}
	});
};

nbApp.dialogChoiceCostCenter = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "costcenter",
		title : el.title,
		href : "Provider?type=page&id=picklist-costcenter&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("textarea[name=basis]").focus();
		}
	});
};

nbApp.dialogChoiceBossAndDemp = function(el, fieldName, isMulti) {
	var dlg = nb.dialog.show({
		effect : "shake",
		targetForm : el.form.name,
		fieldName : fieldName,
		dialogFilterListItem : ".tree-entry",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=view&id=bossandemppicklist&page=1&fieldName=" + fieldName + "&isMulti=" + isMulti,
		onLoad : function() {
			if (isMulti === false) {
				$("[type='checkbox'][data-type='select']", dlg[0]).attr("type", "radio");
			}
		},
		buttons : {
			"select" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

nbApp.dialogChoiceAccessRoles = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "accessroles",
		title : el.title,
		href : "Provider?type=view&id=picklist-roles&page=1",
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		}
	});
};

nbApp.dialogChoiceCategoryForFormula = function(el) {
	var dlg = nb.dialog.show({
		targetForm : el.form.name,
		fieldName : "formula",
		title : el.title,
		maxHeight : 500,
		minHeight : 440,
		width : 500,
		href : "Provider?type=page&id=picklist-category-formula&page=1",
		onExecute : function() {
			var selected = $("[data-type='select']:checked", dlg[0]);
			if (selected.length === 0) {
				return false;
			}

			var ddbid = selected.data("id");
			nb.ajax({
				url : "Provider?type=page&id=get-document-data",
				method : "GET",
				dataType : "json",
				data : {
					ddbid : ddbid,
					items : "comment"
				}
			}).then(function(result) {
				$("[name='formula']").val("#" + ddbid + "@" + result.comment + "#");
				dlg.dialog("close");
			});
		},
		buttons : {
			"ok" : {
				text : nb.getText("select"),
				click : function() {
					dlg[0].dialogOptions.onExecute();
				}
			},
			"cancel" : {
				text : nb.getText("cancel"),
				click : function() {
					dlg.dialog("close");
				}
			}
		},
		close : function() {
			$("[name=formula]").focus();
		}
	});
};
