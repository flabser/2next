<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/form.xsl" />
	<xsl:import href="../layout.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="editmode" select="//document/@editmode" />

	<xsl:template match="/request">
		<xsl:choose>
			<xsl:when test="$isAjaxRequest">
				<xsl:call-template name="_content" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="layout">
					<xsl:with-param name="w_title" select="concat(//captions/name/@caption, ' - ', $APP_NAME)" />
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="_content">
		<header class="form-header">
			<h1 class="header-title">
				<xsl:value-of select="//captions/title/@caption" />
				<em style="margin-left:1em;">
					<xsl:value-of select="//fields/category" />
				</em>
			</h1>
			<xsl:apply-templates select="//actionbar">
				<xsl:with-param name="fixed_top" select="''" />
			</xsl:apply-templates>
		</header>
		<section class="form-content">
			<form action="Provider" name="frm" method="post" id="frm" enctype="application/x-www-form-urlencoded">
				<input type="hidden" name="last_page" value="{history/entry[@type = 'page'][last()]}" disabled="disabled" />
				<fieldset class="fieldset">
					<xsl:if test="$editmode != 'edit'">
						<xsl:attribute name="disabled">disabled</xsl:attribute>
					</xsl:if>

					<div class="fieldset-container">
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/name/@caption" />
							</div>
							<div class="controls">
								<input type="text" value="{//fields/name}" name="name" class="span7" required="required" />
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/category_refers_to/@caption" />
							</div>
							<div class="controls">
								<xsl:apply-templates select="document/glossaries/category_refers_to" mode="static-glossaries-input">
									<xsl:with-param name="type" select="'checkbox'" />
									<xsl:with-param name="required" select="'required'" />
									<xsl:with-param name="value" select="//fields/category_refers_to" />
								</xsl:apply-templates>
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/typeoperation/@caption" />
							</div>
							<div class="controls">
								<xsl:apply-templates select="document/glossaries/typeoperation" mode="static-glossaries-select">
									<xsl:with-param name="required" select="'required'" />
									<xsl:with-param name="value" select="//fields/typeoperation" />
								</xsl:apply-templates>
							</div>
						</div>
						<div class="control-group">
							<div class="controls">
								<label style="display:none;">
									<input type="checkbox" name="disable_selection" value="1">
										<xsl:attribute name="title" select="//captions/disable_selection/@caption" />
										<xsl:if test="//fields/disable_selection = '1'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<span class="input-label">
										<xsl:value-of select="//captions/disable_selection/@caption" />
									</span>
								</label>
								<label class="input-wrapper">
									<input type="checkbox" name="requiredocument" value="1">
										<xsl:attribute name="title" select="//captions/requiredocument/@caption" />
										<xsl:if test="//fields/requiredocument = '1'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<span class="input-label">
										<xsl:value-of select="//captions/requiredocument/@caption" />
									</span>
								</label>
								<br />
								<label class="input-wrapper">
									<input type="checkbox" name="requirecostcenter" value="1">
										<xsl:attribute name="title" select="//captions/requirecostcenter/@caption" />
										<xsl:if test="//fields/requirecostcenter = '1'">
											<xsl:attribute name="checked">checked</xsl:attribute>
										</xsl:if>
									</input>
									<span class="input-label">
										<xsl:value-of select="//captions/requirecostcenter/@caption" />
									</span>
								</label>
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/accessroles/@caption" />
							</div>
							<div class="controls">
								<select name="accessroles" class="span7">
									<option></option>
									<xsl:for-each select="//roles/entry">
										<option>
											<xsl:attribute name="value" select="name" />
											<xsl:if test="//fields/accessroles = name">
												<xsl:attribute name="selected" select="'selected'" />
											</xsl:if>
											<xsl:value-of select="concat(name, ' - ', description)" />
										</option>
									</xsl:for-each>
								</select>
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/comment/@caption" />
							</div>
							<div class="controls">
								<textarea name="comment" class="span7">
									<xsl:value-of select="//fields/comment" />
								</textarea>
							</div>
						</div>
					</div>
				</fieldset>

				<input type="hidden" name="type" value="save" />
				<input type="hidden" name="id" value="subcategory" />
				<input type="hidden" name="key" value="{document/@docid}" />
				<input type="hidden" name="parentdocid" value="{document/@parentdocid}" />
				<input type="hidden" name="parentdoctype" value="{document/@parentdoctype}" />
			</form>
		</section>
	</xsl:template>

</xsl:stylesheet>
