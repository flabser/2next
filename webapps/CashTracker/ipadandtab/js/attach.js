$(function() {
	'use strict';

	$('#progress').css('display', 'none');

	if (typeof $.fn.fileupload == 'undefined') {
		return;
	}

	$('#fileupload').fileupload({
		url : 'Uploader',
		dataType : 'xml',
		send : function() {
			$('#progress').css('display', 'block');
		},
		done : function(e, data) {
			var msg = nbApp.parseMessageToJson(data.result);
			var tpl = $('#template-rtfcontent-entry').children().clone();

			if (!msg.message) {
				$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
				alert('file upload error');
				return;
			}

			var filename = msg.message[0].toString();
			var hash = msg.message[1].toString();
			var formsesid = msg.message[3].text;
			var fileUrl = 'Provider?type=getattach&formsesid=' + formsesid;
			fileUrl += '&field=rtfcontent&id=rtfcontent&file=' + filename;

			$(tpl).appendTo('#attach-files');
			$(tpl).append('<input type="hidden" name="filename" value="' + filename + '"/>');
			$(tpl).append('<input type="hidden" name="fileid" value="' + formsesid + '"/>');
			$(tpl).append('<input type="hidden" name="filehash" value="' + hash + '"/>');
			$('a.rtf-file', tpl).attr({
				href : fileUrl,
				target : '_blank'
			}).text(filename);
			$('[data-click=add_comment]', tpl).on('click', function() {
				nbApp.attach.addComment(this, hash);
			});
			$('[data-click=remove]', tpl).on('click', function() {
				nbApp.attach.remove(this, formsesid, filename, hash, true);
			});

			setTimeout(function() {
				$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
			}, 1000);
		},
		progressall : function(e, data) {
			var progress = parseInt(data.loaded / data.total * 100, 10);
			$('#progress .progress-bar').css('width', progress + '%').text(progress + '%');
		},
		fail : function() {
			$('.progress-bar', $('#progress').css('display', 'none')[0]).css('width', '0%');
			alert('file upload fail');
		}
	}).prop('disabled', !$.support.fileInput).parent().addClass($.support.fileInput ? undefined : 'disabled');

});

nbApp.attach = {};

nbApp.attach.preview = function(el) {
	var rtfe = $(el).parents('.rtf-entry');
	var link = $(rtfe).find('a.rtf-file').append('<i class="rtf-loading"></i><br/>');
	$('<img class="attach-preview" style="display:none;">').attr('src', link.attr('href')).on('load', function() {
		$(this).show();
		$('i.rtf-loading', link).remove();
	}).on('error', function() {
		alert('load error');
		$('i.rtf-loading', link).remove();
		$(this).remove();
	}).appendTo(rtfe);
	$(el).hide();
};

nbApp.attach.addComment = function(el, hash) {
	var rtfe = $(el).parents('.rtf-entry');
	var dlg = nb.dialog.show({
		message : $('<div><textarea rows="3"></textarea></div>').html(),
		title : 'Коментарий',
		dialogClass : 'nextbase-dialog-responsive',
		position : 'top',
		buttons : {
			'Ok' : {
				text : 'Ok',
				click : function() {
					var comment = $('textarea', dlg).val();
					if (comment.length === 0) {
						return false;
					}

					$('#frm').append('<input type="hidden" name="comment' + hash + '" value="' + comment + '">');
					$(rtfe).find('a.rtf-file').after($('<span class="rtf-comment">').html(comment));

					$(el).hide();
					dlg.dialog('close');
				}
			},
			'Cancel' : {
				text : 'Oтмена',
				click : function() {
					dlg.dialog('close');
				}
			}
		}
	});
};

nbApp.attach.remove = function(el, formsesid, filename, hash, deleteEntry) {
	if (deleteEntry === true) {
		$(el).parents('.rtf-entry').remove();
	} else {
		$(el).parents('.rtf-entry').addClass('to-delete');
	}
	$('#' + hash).remove();

	$('#frm').append('<input type="hidden" name="deletertfcontentsesid" value="' + formsesid + '" />');
	$('#frm').append('<input type="hidden" name="deletertfcontentname" value="' + filename + '" />');
	$('#frm').append('<input type="hidden" name="deletertfcontentfield" value="rtfcontent" />');
};
