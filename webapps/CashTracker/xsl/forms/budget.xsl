<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/form.xsl" />
	<xsl:import href="../layout.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="editmode" select="//document/@editmode" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat(//captions/title/@caption, ' - ', $APP_NAME)" />
			<xsl:with-param name="aside_collapse" select="'aside_collapse'" />
			<xsl:with-param name="active_aside_id" select="'budgets'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<xsl:choose>
			<xsl:when test="//document/@status != 'new' and //document/@id">
				<xsl:call-template name="form_content" />
			</xsl:when>
			<xsl:otherwise>
				<h1 style="padding:2em;">
					<xsl:value-of select="//error" />
				</h1>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="form_content">
		<header class="form-header">
			<h3 class="doc-title">
				<xsl:value-of select="//captions/title/@caption" />
			</h3>
			<xsl:apply-templates select="//actionbar">
				<xsl:with-param name="fixed_top" select="''" />
			</xsl:apply-templates>
		</header>
		<section class="form-content">
			<form action="Provider" name="frm" method="post" id="frm" enctype="application/x-www-form-urlencoded">
				<input type="hidden" name="last_page" value="{history/entry[@type = 'page'][last()]}" disabled="disabled" />
				<fieldset name="property" class="fieldset">
					<xsl:if test="$editmode != 'edit'">
						<xsl:attribute name="disabled">disabled</xsl:attribute>
					</xsl:if>
					<legend class="legend">
						<div class="legend-tab">
							<xsl:value-of select="//captions/properties/@caption" />
						</div>
					</legend>

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
								<xsl:value-of select="//captions/moderator/@caption" />
								<xsl:if test="$editmode = 'edit'">
									<button type="button" class="btn-picklist" title="{//captions/moderator/@caption}" onclick="nbApp.dialogChoiceBossAndDemp(this, 'moderator')" />
								</xsl:if>
							</div>
							<div class="controls">
								<xsl:call-template name="field">
									<xsl:with-param name="name" select="'moderator'" />
									<xsl:with-param name="node" select="//fields/moderator" />
								</xsl:call-template>
							</div>
						</div>
					</div>
				</fieldset>

				<input type="hidden" name="type" value="save" />
				<input type="hidden" name="id" value="budget" />
				<input type="hidden" name="key" value="{document/@docid}" />
			</form>
		</section>
		<footer>
			<div class="text-center">
				<form action="Provider" method="DELETE">
					<button class="btn btn-remove-budget" name="remove-budget" value="do">Удалить бюджет</button>
				</form>
			</div>
		</footer>
	</xsl:template>

</xsl:stylesheet>
