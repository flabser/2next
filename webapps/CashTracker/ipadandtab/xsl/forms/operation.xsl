<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/form.xsl" />
	<xsl:import href="../templates/attach.xsl" />
	<xsl:import href="../templates/doc-info.xsl" />
	<xsl:import href="../layout.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="editmode" select="/request/document/@editmode" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title">
				<xsl:if test="document/@status = 'new'">
					<xsl:value-of select="concat(document/captions/new/@caption, ' - ', document/captions/title/@caption)" />
				</xsl:if>
				<xsl:if test="document/@status != 'new'">
					<xsl:value-of select="concat(document/captions/title/@caption, ' ', document/fields/vn)" />
				</xsl:if>
			</xsl:with-param>
			<xsl:with-param name="include">
				<script src="/SharedResources/jquery/js/jquery-fileupload/js/jquery.fileupload.js"></script>
				<script src="/SharedResources/jquery/js/jquery-fileupload/js/vendor/jquery.ui.widget.js"></script>
				<script src="/SharedResources/jquery/js/ui/jquery.ui.datepicker-ru.js" />
				<xsl:call-template name="markisread" />
				<script type="text/javascript">
					var ddbid = '<xsl:value-of select="document/@id" />';
					<![CDATA[
						$(document).ready(function(){
							$("#saldo").load("Provider?type=page&id=saldo&ddbid=" + ddbid);
						}); ]]>
				</script>

				<xsl:if test="document/@editmode = 'edit'">
					<script>
						<![CDATA[
						$(function() {
							$('.date-short').datepicker({
								showOn: 'focus',
								regional: ['ru'],
								showAnim: '',
								firstDay: 1,
								isRTL: false,
								showMonthAfterYear: false
							});
						});]]>
					</script>
				</xsl:if>
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<div class="form-wrapper">
			<div class="container">
				<div class="doc-title">
					<h3>
						<xsl:value-of select="document/captions/title/@caption" />
						<xsl:if test="document/fields/vn and document/fields/vn != ''">
							<xsl:text> № </xsl:text>
							<xsl:value-of select="document/fields/vn" />
						</xsl:if>
						<xsl:value-of select="concat(' ', document/captions/from/@caption, ' ', document/fields/date)" />
					</h3>
				</div>
				<nav class="action-bar">
					<div class="action-group">
						<xsl:call-template name="save" />
					</div>
					<div class="action-group pull-right">
						<xsl:call-template name="cancel" />
					</div>
				</nav>
				<div class="doc-author">
					<xsl:value-of select="//document/captions/author/@caption" />:
					<strong>
						<xsl:value-of select="document/fields/author" />
					</strong>
				</div>
				<div id="saldo" title="Сальдо"></div>
				<div class="form-group">
					<form action="Provider" name="frm" method="post" id="frm" enctype="application/x-www-form-urlencoded">
						<fieldset name="property" class="fieldset">
							<xsl:if test="$editmode != 'edit'">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>
							<legend class="legend">
								<xsl:value-of select="document/captions/properties/@caption" />
							</legend>
							<ol class="fieldset-container">
								<!-- Дата операции -->
								<li class="control-group">
									<div class="control-label">
										<xsl:value-of select="document/captions/date/@caption" />
									</div>
									<div class="controls">
										<input type="text" name="date" value="{substring(document/fields/date, 1, 10)}" class="date-short"
											readonly="readonly" />
									</div>
								</li>
								<!-- Касса -->
								<li class="control-group">
									<div class="control-label">
										<xsl:value-of select="document/captions/cash/@caption" />
									</div>
									<div class="controls">
										<xsl:value-of select="document/fields/cash" />
									</div>
								</li>
								<!-- category -->
								<li class="control-group">
									<div class="control-label">
										<xsl:value-of select="document/captions/category/@caption" />
										<xsl:if test="$editmode = 'edit'">
											<button type="button" class="btn-picklist" onclick="nbApp.dialogChoiceCategory(this)">
												<xsl:attribute name="title" select="document/captions/category/@caption" />
											</button>
										</xsl:if>
									</div>
									<div class="controls">
										<input type="hidden" name="category" value="{document/fields/category/@attrval}" />
										<input type="hidden" name="subcategory" value="{document/fields/subcategory/@attrval}" />

										<span id="typeoperationtbl">
											<xsl:attribute name="class" select="concat('operation-type-icon-', document/fields/typeoperation)" />
										</span>
										<span id="categorytbl">
											<xsl:value-of select="document/fields/category" />
										</span>
										<span id="subcategorytbl">
											<xsl:if test="document/fields/subcategory">
												<xsl:text> / </xsl:text>
												<xsl:value-of select="document/fields/subcategory" />
											</xsl:if>
										</span>
									</div>
								</li>
								<!-- Целевая касса -->
								<li class="control-group" id="control-row-targetcash">
									<xsl:if test="document/@status = 'new' or document/fields/typeoperation !='transfer'">
										<xsl:attribute name="style">display:none;</xsl:attribute>
									</xsl:if>
									<div class="control-label">
										<xsl:value-of select="document/captions/targetcash/@caption" />
										<xsl:if test="$editmode = 'edit'">
											<button type="button" id="targetcashbtn" class="btn-picklist" onclick="nbApp.dialogChoiceTargetCash(this)">
												<xsl:attribute name="title" select="document/captions/targetcash/@caption" />
											</button>
										</xsl:if>
									</div>
									<div class="controls">
										<xsl:call-template name="field">
											<xsl:with-param name="name" select="'targetcash'" />
											<xsl:with-param name="node" select="document/fields/targetcash" />
										</xsl:call-template>
									</div>
								</li>
								<!-- Сумма -->
								<li class="control-group">
									<div class="control-label">
										<xsl:value-of select="document/captions/summa/@caption" />
									</div>
									<div class="controls">
										<input type="number" name="summa" value="{document/fields/summa}" class="number" autocomplete="off"
											required="required" />
									</div>
								</li>
								<!-- Место возникновения -->
								<li class="control-group">
									<div class="control-label">
										<xsl:value-of select="document/captions/costcenter/@caption" />
										<xsl:if test="$editmode = 'edit'">
											<button type="button" class="btn-picklist">
												<xsl:attribute name="title" select="document/captions/costcenter/@caption" />
												<xsl:attribute name="onclick">nbApp.dialogChoiceCostCenter(this)</xsl:attribute>
											</button>
										</xsl:if>
									</div>
									<div class="controls">
										<xsl:call-template name="field">
											<xsl:with-param name="name" select="'costcenter'" />
											<xsl:with-param name="node" select="document/fields/costcenter" />
										</xsl:call-template>
									</div>
								</li>
								<!-- Основание -->
								<li class="control-group">
									<div class="control-label">
										<xsl:value-of select="document/captions/basis/@caption" />
									</div>
									<div class="controls">
										<textarea name="basis" id="basis">
											<xsl:attribute name="title" select="document/fields/basis/@caption" />
											<xsl:value-of select="document/fields/basis" />
										</textarea>
									</div>
								</li>
								<!-- documented -->
								<li class="control-group">
									<div class="control-label"></div>
									<div class="controls">
										<label class="button button-default">
											<input type="checkbox" name="documented" value="1">
												<xsl:attribute name="title" select="document/fields/documented/@caption" />
												<xsl:if test="document/fields/requiredocument='1' or $editmode='readonly'">
													<xsl:attribute name="disabled">disabled</xsl:attribute>
												</xsl:if>
												<xsl:if test="document/fields/documented='1' or document/fields/requiredocument='1'">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
											</input>
											<span class="input-label">
												<xsl:value-of select="document/captions/documented/@caption" />
											</span>
										</label>
									</div>
								</li>
							</ol>
						</fieldset>

						<input type="hidden" name="action" id="action" value="" />
						<input type="hidden" name="type" value="save" />
						<input type="hidden" name="id" value="{@id}" />
						<input type="hidden" name="key" id="key" value="{document/@docid}" />
						<input type="hidden" name="doctype" id="doctype" value="{document/@doctype}" />
						<input type="hidden" name="author" value="{document/fields/author/@attrval}" disabled="disabled" />
						<input type="hidden" name="page" value="{document/@openfrompage}" />
						<input type="hidden" name="formsesid" value="{formsesid}" />
					</form>
				</div>
				<div class="form-group">
					<form action="Uploader" name="upload" id="upload" method="post" enctype="multipart/form-data">
						<fieldset class="fieldset">
							<xsl:if test="$editmode != 'edit'">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>
							<legend class="legend">
								<xsl:value-of select="document/captions/attachments/@caption" />
							</legend>
							<ol class="fieldset-container">
								<input type="hidden" name="type" value="rtfcontent" />
								<input type="hidden" name="formsesid" value="{formsesid}" />
								<xsl:call-template name="attach" />
							</ol>
						</fieldset>
					</form>
				</div>
				<div class="form-group">
					<xsl:call-template name="doc-info" />
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
