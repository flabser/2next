<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/form.xsl" />
	<xsl:import href="../layout.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="editmode" select="//document/@editmode" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat(//captions/name/@caption, ' - ', $APP_NAME)" />
			<xsl:with-param name="aside_collapse" select="'aside_collapse'" />
		</xsl:call-template>
		<script type="text/javascript">
			<![CDATA[
			$(document).ready(function(){
				$("input[name=amountcontrol]").number(true, 0, ".", " ");
			});]]>
		</script>
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
							<xsl:value-of select="document/captions/properties/@caption" />
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
								<xsl:value-of select="//captions/user/@caption" />
								<xsl:if test="$editmode = 'edit'">
									<button type="button" class="btn-picklist" title="{//captions/user/@caption}" onclick="nbApp.dialogChoiceBossAndDemp(this, 'user', false)" />
								</xsl:if>
							</div>
							<div class="controls">
								<xsl:call-template name="field">
									<xsl:with-param name="name" select="'user'" />
									<xsl:with-param name="node" select="//fields/user" />
								</xsl:call-template>
								<div>
									<button class="btn btn-def" onclick="nbApp.sendInvite(this.form.user.value);return false;">
										<xsl:value-of select="//captions/btn_send_invite/@caption" />
									</button>
									<div class="hint">
										На email пользователя будет отправлено приглашение.
									</div>
								</div>
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/amountcontrol/@caption" />
							</div>
							<div class="controls">
								<input type="text" name="amountcontrol" value="{//fields/amountcontrol}" class="span2" />
							</div>
						</div>
						<div class="control-group">
							<div class="control-label">
								<xsl:value-of select="//captions/observers/@caption" />
								<xsl:if test="$editmode = 'edit'">
									<button type="button" class="btn-picklist" title="{//captions/observers/@caption}" onclick="nbApp.dialogChoiceBossAndDemp(this, 'observers')" />
								</xsl:if>
							</div>
							<div class="controls">
								<xsl:call-template name="field">
									<xsl:with-param name="name" select="'observers'" />
									<xsl:with-param name="node" select="//fields/observers" />
								</xsl:call-template>
							</div>
						</div>
					</div>
				</fieldset>

				<input type="hidden" name="type" value="save" />
				<input type="hidden" name="id" value="cash" />
				<input type="hidden" name="key" value="{document/@docid}" />
			</form>
		</section>
	</xsl:template>

</xsl:stylesheet>
