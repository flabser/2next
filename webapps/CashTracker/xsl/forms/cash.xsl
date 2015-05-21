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
					<xsl:with-param name="include">
						<script type="text/javascript">
							<![CDATA[
							$(document).ready(function(){
								$("input[name=amountcontrol]").number(true, 0, ".", " ");
							});]]>
						</script>
					</xsl:with-param>
				</xsl:call-template>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="_content">
		<header class="form-header">
			<h1 class="header-title">
				<xsl:value-of select="//captions/title/@caption" />
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
								<xsl:value-of select="//captions/user/@caption" />
								<xsl:if test="$editmode = 'edit'">
									<button type="button" class="btn-text">
										<xsl:attribute name="class" select="'btn-text btn-picklist'" />
										<xsl:attribute name="title" select="//captions/user/@caption" />
										<xsl:attribute name="onclick">nbApp.dialogChoiceBossAndDemp(this, 'user', false)</xsl:attribute>
										<i class="fa fa-user" />
									</button>
								</xsl:if>
							</div>
							<div class="controls">
								<xsl:call-template name="field">
									<xsl:with-param name="name" select="'user'" />
									<xsl:with-param name="node" select="//fields/user" />
								</xsl:call-template>
								<div>
									<button class="btn btn-def" onclick="nbApp.sendInvite(this.form.user.value);return false;">
										<i class="fa fa-envelope" />
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
									<button type="button" class="btn-text">
										<xsl:attribute name="class" select="'btn-text btn-picklist'" />
										<xsl:attribute name="title" select="//captions/user/@caption" />
										<xsl:attribute name="onclick">nbApp.dialogChoiceBossAndDemp(this, 'observers')</xsl:attribute>
										<i class="fa fa-users" />
									</button>
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
