<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="formsesid" select="//formsesid" />
	<xsl:variable name="attach_editmode" select="//document/@editmode" />
	<xsl:variable name="attach_get_url"
		select="concat('Provider?type=getattach&amp;formsesid=', $formsesid, '&amp;doctype=', //document/@doctype, '&amp;field=rtfcontent&amp;id=rtfcontent&amp;file=')" />

	<xsl:variable name="caption_add_comment" select="//document/captions/attach_add_comment/@caption" />
	<xsl:variable name="caption_delete_file" select="//document/captions/attach_delete/@caption" />
	<xsl:variable name="caption_file_preview" select="//document/captions/attach_preview/@caption" />

	<xsl:template name="attach">
		<div class="attach">
			<xsl:if test="$attach_editmode = 'edit'">
				<div class="upload">
					<input id="fileupload" type="file" name="files[]" class="upload-input" />
					<div id="progress" class="progress">
						<div class="progress-bar progress-bar-success"></div>
					</div>
					<div id="attach-files" class="attach-files"></div>
				</div>
			</xsl:if>
			<xsl:apply-templates select="//document/fields/rtfcontent" />
		</div>
		<div id="template-rtfcontent-entry" style="display:none;">
			<div class="rtf-entry">
				<div class="rtf-action">
					<button type="button" class="button button-default" data-click="add_comment">
						<img src="/SharedResources/img/classic/icons/comment_add.png" />
					</button>
					<button type="button" class="button button-default" data-click="remove">
						<img src="/SharedResources/img/iconset/cross.png" />
					</button>
				</div>
				<a class="rtf-file"></a>
				<span class="rtf-comment"></span>
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
			<xsl:if test="$attach_editmode = 'edit'">
				<div class="rtf-action">
					<xsl:if
						test="$extension = 'jpg' or $extension = 'jpeg' or $extension = 'gif' or $extension = 'bmp' or $extension = 'png'">
						<button type="button" class="button button-default" title="preview" onclick="nbApp.attach.preview(this)">
							<span class="icon-eye-open"></span>
						</button>
					</xsl:if>
					<xsl:if test="false() and comment = ''">
						<button type="button" class="button button-default" title="{$caption_add_comment}" onclick="nbApp.attach.addComment(this, '{@hash}')">
							<img src="/SharedResources/img/classic/icons/comment_add.png" />
						</button>
					</xsl:if>
					<button type="button" class="button button-default" title="{$caption_delete_file}"
						onclick="nbApp.attach.remove(this, '{$formsesid}', '{@filename}', '{@hash}')">
						<img src="/SharedResources/img/iconset/cross.png" />
					</button>
				</div>
			</xsl:if>
			<a class="rtf-file" target="_blank">
				<xsl:attribute name="href" select="concat($attach_get_url, @filename, '&amp;key=', @id)" />
				<xsl:value-of select="@filename" />
			</a>
			<xsl:apply-templates select="comment[text() != '']" mode="rtfcontent" />
		</div>
	</xsl:template>

	<xsl:template match="comment" mode="rtfcontent">
		<span class="rtf-comment">
			<xsl:value-of select="." />
		</span>
	</xsl:template>

</xsl:stylesheet>
