<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="formsesid" select="//formsesid" />
	<xsl:variable name="attach_editmode" select="//document/@editmode" />
	<xsl:variable name="attach_get_url"
		select="concat('Provider?type=getattach&amp;formsesid=', $formsesid, '&amp;doctype=', //document/@doctype, '&amp;key=', //document/@docid, '&amp;field=rtfcontent&amp;id=rtfcontent&amp;file=')" />

	<xsl:variable name="caption_add_comment" select="//document/captions/attach_add_comment/@caption" />
	<xsl:variable name="caption_delete_file" select="//document/captions/attach_delete/@caption" />
	<xsl:variable name="caption_file_preview" select="//document/captions/attach_preview/@caption" />

	<xsl:template name="attach_cert">
		<fieldset class="fieldset">
			<xsl:if test="$attach_editmode != 'edit'">
				<xsl:attribute name="disabled" select="'disabled'" />
			</xsl:if>
			<div class="control-group">
				<div class="control-label">
					<xsl:value-of select="document/captions/password/@caption" />
				</div>
				<div class="controls">
					<input type="password" size="40" name="p_eds" />
				</div>
			</div>
			<xsl:if test="$attach_editmode = 'edit'">
				<div class="control-group">
					<div class="control-label">
						<xsl:value-of select="document/captions/attachments_cert/@caption" />
					</div>
					<div class="controls">
						<div class="upload">
							<input id="fileupload" type="file" name="files[]" class="upload-input" />
							<div id="progress" class="progress">
								<div class="progress-bar progress-bar-success"></div>
							</div>
							<div id="attach-files" class="attach-files"></div>
						</div>
					</div>
				</div>
			</xsl:if>
			<xsl:apply-templates select="//document/fields/rtfcontent" />
		</fieldset>
		<div id="template-rtfcontent-entry" style="display:none;">
			<div class="rtf-entry">
				<a class="rtf-file"></a>
				<span class="rtf-comment"></span>
				<div class="rtf-action">
					<a href="javascript:void(0)" class="attach-action-add-comment" title="{$caption_add_comment}" data-click="add_comment">
						<i>
							<img src="/SharedResources/img/classic/icons/comment_add.png" />
						</i>
						<xsl:value-of select="$caption_add_comment" />
					</a>
					<a href="javascript:void(0)" class="attach-action-remove-file" title="{$caption_delete_file}" data-click="remove">
						<i>
							<img src="/SharedResources/img/iconset/cross.png" height="13px" />
						</i>
						<xsl:value-of select="$caption_delete_file" />
					</a>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="rtfcontent">
		<div class="rtfcontent">
			<xsl:apply-templates select="entry" mode="rtfcontent" />
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="rtfcontent">
		<xsl:variable name="extension" select="tokenize(lower-case(@filename), '\.')[last()]" />

		<div class="rtf-entry">
			<a class="rtf-file" target="_blank">
				<xsl:attribute name="href" select="concat($attach_get_url, @filename)" />
				<xsl:value-of select="@filename" />
			</a>
			<xsl:apply-templates select="comment[text() != '']" mode="rtfcontent" />
			<xsl:if test="$attach_editmode = 'edit'">
				<div class="rtf-action">
					<xsl:if
						test="$extension = 'jpg' or $extension = 'jpeg' or $extension = 'gif' or $extension = 'bmp' or $extension = 'png'">
						<a href="javascript:void(0)" class="attach-action-preview" title="preview" onclick="nbApp.attachPreview(this)">
							<i class="icon-eye-open"></i>
							<xsl:value-of select="$caption_file_preview" />
						</a>
					</xsl:if>
					<xsl:if test="false() and comment = ''">
						<a href="javascript:void(0)" class="attach-action-add-comment" title="{$caption_add_comment}" onclick="nbApp.attachAddComment(this, '{@hash}')">
							<i>
								<img src="/SharedResources/img/classic/icons/comment_add.png" />
							</i>
							<xsl:value-of select="$caption_add_comment" />
						</a>
					</xsl:if>
					<a href="javascript:void(0)" class="attach-action-remove-file" title="{$caption_delete_file}"
						onclick="nbApp.attachRemove(this, '{$formsesid}', '{@filename}', '{@hash}')">
						<i>
							<img src="/SharedResources/img/iconset/cross.png" height="13px" />
						</i>
						<xsl:value-of select="$caption_delete_file" />
					</a>
				</div>
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template match="comment" mode="rtfcontent">
		<span class="rtf-comment">
			<xsl:value-of select="." />
		</span>
	</xsl:template>

</xsl:stylesheet>
