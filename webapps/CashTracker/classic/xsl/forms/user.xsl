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
			<xsl:with-param name="active_aside_id" select="'users'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
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
								<xsl:value-of select="//captions/fullname/@caption" />
							</div>
							<div class="controls">
								<input type="text" name="fullname" value="{//fields/fullname}" class="span5" required="required" />
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/email/@caption" />
							</div>
							<div class="controls">
								<input type="text" name="email" value="{//fields/email}" class="span5" />
							</div>
						</div>
					</div>
				</fieldset>

				<input type="hidden" name="shortname" value="{//fields/fullname}" />
				<input type="hidden" name="userid" value="{//fields/userid}" />
				<input type="hidden" name="organization" value="{//fields/organization}" />
				<input type="hidden" name="rank" value="{//fields/rank}" />

				<input type="hidden" name="type" value="save" />
				<input type="hidden" name="id" value="employer" />
				<input type="hidden" name="key" value="{document/@docid}" />

				<input type="hidden" name="parentdoctype" value="{document/@parentdoctype}" />
				<input type="hidden" name="parentdocid" value="{document/@parentdocid}" />
			</form>
		</section>
	</xsl:template>

</xsl:stylesheet>
