var nbApp = {
	app_lang : "RUS",
	init : function() {
		this.menu.init();
		this.ui.init();
	},
	parseMessageToJson : function(xml) {
		return $.xml2json(xml);
	},
	menu : {
		init : function() {
			var overlay = true;
			var menu_overlay;
			var $sub_menu = $(".sub-menu");

			if (overlay) {
				menu_overlay = $("<div class='menu-overlay' style='display:none;' />");
				$('body').append(menu_overlay);

				menu_overlay.mousedown(function(e) {
					e.preventDefault();
					$sub_menu.removeClass("active");
					menu_overlay[0].style.display = "none";
					$("body").removeClass("nav-ws-open");
				});

				menu_overlay[0].addEventListener('touchstart', function(e) {
					$("body").removeClass("nav-ws-open");
					menu_overlay[0].style.display = "none";
					$sub_menu.removeClass("active");
					e.preventDefault();
				}, false);
			}

			$(".js-toggle-nav-ws").mousedown(function(e) {
				e.preventDefault();
				$("body").toggleClass("nav-ws-open");
				menu_overlay[0].style.display = "block";
				$sub_menu.removeClass("active");
			});

			$("[data-role='menu-toggle']").mousedown(function() {
				var className = $(this).attr("data-class");
				var $ddm = $(this).parent();
				var ddmIsActive = $ddm.hasClass(className);
				$sub_menu.removeClass(className);

				if (ddmIsActive) {
					if (overlay && !$ddm.hasClass("action-bar")) {
						menu_overlay[0].style.display = "none";
					}
					$ddm.removeClass(className);
				} else {
					if (overlay && !$ddm.hasClass("action-bar")) {
						menu_overlay[0].style.display = "block";
					}
					$ddm.addClass(className);
				}
			});
		}
	},
	form : {
		submit_wait_block : null,
		submit : function(_targetForm) {
			var options = {
				cache : false,
				type : "POST",
				url : "Provider",
				data : $(_targetForm).serialize(),
				beforeSend : function() {
					nbApp.form.submit_wait_block.show();
				},
				complete : function() {
					nbApp.form.submit_wait_block.hide();
				},
				success : function(xml) {
					var jmsg = nbApp.parseMessageToJson(xml);
					if (jmsg.response.status === "ok") {
						if (jmsg.response.redirect) {
							window.location.href = jmsg.response.redirect;
						}
					} else {
						nbApp.ui.notify(jmsg.response.message.text);
					}
				},
				error : function(xhr, status, errorThrown) {
					nbApp.ui.notify({
						msg : status,
						type : "error"
					});
					nbApp.form.submit_wait_block.hide();
				}
			};

			return $.ajax(options);
		}
	},
	view : {

	},
	ui : {
		init : function() {
			// form_submit_wait_block
			var whtm = "<div class='form_submit_wait_block' style='display:none;'>"
					+ "<div class='form_submit_wait_block_inner'>Сохранение...</div></div>";
			nbApp.form.submit_wait_block = $(whtm);
			$("body").append(nbApp.form.submit_wait_block);

			// ct_app_ui_notify_block
			nbApp.ui.notify_block = $("<div class='ct_app_ui_notify_block' style='display:none;'></div>");
			$("body").append(nbApp.ui.notify_block);
		},
		notify_block : null,
		notify : function(msg) {
			var options;
			if (typeof msg === "string") {
				options = {
					timeout : 1700,
					type : ""
				};
				options.msg = msg;
			} else {
				options = msg;
				options.timeout = msg.timeout || 1700;
				// warn, error, info
				options.type = "ct_app_ui_notify_" + msg.type;
			}

			var $noty = $("<div class='ct_app_ui_notify_block_inner " + options.type + "'>" + options.msg + "</div>");
			nbApp.ui.notify_block.append($noty).show();
			$noty.on("click", function() {
				$noty.remove();
				if (options.callback) {
					options.callback();
				}
			});

			if (options.timeout > -1) {
				setTimeout(function() {
					if ($noty && $noty.length) {
						$noty.fadeOut({
							always : function() {
								$noty.remove();
								if (nbApp.ui.notify_block.text() === "") {
									nbApp.ui.notify_block.fadeOut().html("");
								}
								if (options.callback) {
									options.callback();
								}
							}
						});
					}
				}, options.timeout);
			}
		}
	}
};

$(document).ready(function() {
	nbApp.init();

	var $barDynamic = $(".action-bar-dynamic");
	if ($barDynamic.length) {
		var $selCountBlock = $("<div class='action-target-count'></div>");
		$(".action-bar-container", $barDynamic).prepend($selCountBlock);

		$("[type=checkbox]", ".view").click(function() {
			var checkedCount = $(".view [type=checkbox]:checked").length;
			if (checkedCount > 0) {
				$selCountBlock.html(checkedCount);
				$(".action-bar-dynamic").addClass("active");
			} else {
				$selCountBlock.html("");
				$(".action-bar-dynamic").removeClass("active");
			}
		});
	}

	$("[type=checkbox]", ".view").click(function() {
		var $el = $(this);
		if ($el.hasClass("getSaldoOnDate")) {
			return;
		}
		$el.addClass("getSaldoOnDate");

		var ddbid = $el.val();
		nbApp.getSaldoOnDate(ddbid).then(function(result) {
			$el.parents(".vt-row").children(".doclink").append("<div class='vt-saldo'>" + result + "</div>");
		});
	});

	$("form[name!='search']").submit(function(e) {
		e.preventDefault();
		nbApp.form.submit("form[name!='search']");
	});
});

function sorting(pageId, column, direction) {
	nb.xhr.sendSortRequest(pageId, column, direction).then(function() {
		window.location.reload();
	});
}

function resetFilter(viewId) {
	nb.xhr.resetFilter(viewId).then(function() {
		window.location.reload();
	});
}

function usersWhichReadInTable(el, doctype, id) {
	nb.xhr.getUsersWichRead(id, doctype).then(function(xml) {

		var $tblNode = $("#userswhichreadtbl");
		var row_cells = ["<tr><td>", "1", "</td><td>", "3", "</td></tr>"];

		$(xml).find("entry").each(function() {
			var username = $(this).attr('username');
			if (username !== undefined) {
				if ($("td:contains('" + username + "')", $tblNode).length === 0) {
					row_cells[1] = username;
					row_cells[3] = $(this).attr('eventtime');
					$tblNode.append(row_cells.join(""));
				}
			}
		});
	});
}

function markRead(doctype, docid) {
	setTimeout(function() {
		nb.xhr.markDocumentAsRead(docid, doctype).then(function() {
			nbApp.ui.notify(nb.getText("document_mark_as_read"));
		});
	}, 2000);
}

// -----------------------
var nbStrings = {
	"RUS" : {
		document_not_selected : "Не выбран документ(ы)",
		document_mark_as_read : "Документ отмечен как прочтенный",
		delete_error : "Ошибка удаления", // Жою қателігі, Error
		// deleting
		successfully_deleted : "Удаление завершено успешно", // Жою
		// сәтті
		// аяқталды,
		// Successfully
		// deleted
		deleted : "Удалено",
		undeleted : "Не удалено" // Жойылмады
	}
};

// -----------------------

function delDocument(dbID, typedel) {
	var $checkboxes = $("input[name='chbox']:checked");
	if ($checkboxes.length < 1) {
		nbApp.ui.notify(nb.getText("document_not_selected"));
		return;
	}

	var ck = [];
	$checkboxes.each(function(index, element) {
		ck.push("docid=" + $(element).val());
	});

	$.ajax({
		cache : false,
		type : "POST",
		datatype : "XML",
		url : "Provider?type=page&id=delete_document",
		data : ck.join("&"),
		success : function(msg) {
			var j_msg = nbApp.parseMessageToJson(msg);

			if (j_msg.page.response.status !== "ok") {
				nbApp.ui.notify({
					msg : "<div>" + nb.getText("delete_error") + "</div>" + j_msg.page.response.message,
					type : "error",
					timeout : 3000
				});
				return;
			}

			var deleteDocument = j_msg.page.response.content.delete_document;
			var deletedCount = deleteDocument.deleted.count;
			var unDeletedCount = deleteDocument.undeleted.count;
			var strMsg = "";

			if (unDeletedCount != 0) {
				strMsg += nb.getText("undeleted") + ": " + unDeletedCount + "<br/>";
			}
			strMsg += nb.getText("deleted") + ": " + deletedCount;

			var deletedEntry = deleteDocument.deleted.entry;
			if (typeof deletedEntry === "string") {
				strMsg += "<div>" + deletedEntry + "</div>";
			} else {
				strMsg += "<div>" + deletedEntry.join("<br/>") + "</div>";
			}

			nb.utils.blockUI();
			var resMsg = nb.getText("successfully_deleted");
			resMsg += "<div style='padding:4px;text-align:left;'>" + strMsg + "</div>";
			resMsg += "<div style='padding:4px;text-align:right;'><b>OK</b></div>";

			nbApp.ui.notify({
				msg : resMsg,
				timeout : -1,
				callback : function() {
					window.location.reload();
				}
			});
		},
		error : function(data, status, xhr) {
			nbApp.ui.notify({
				msg : nb.getText("delete_error"),
				type : "error"
			});
		}
	});
}

function chooseFilter(keyword, column) {
	nb.xhr.chooseFilter(page_id.value, column, keyword).then(function() {
		window.location.reload();
	});
}

function resetFilter() {
	nb.xhr.resetFilter(page_id.value).then(function() {
		window.location.reload();
	});
}

function resetCurrentFilter(column) {
	nb.xhr.resetCurrentFilter(page_id.value, column).then(function() {
		window.location.reload();
	});
}

function toggleFilterList(el) {
	$(el).parents(".filter-entry-list-wrapper").find(".filter-entry-list").toggleClass("visible");
}
