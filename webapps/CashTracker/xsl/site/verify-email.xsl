<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />
	<xsl:import href="-login.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="body_class" select="'wlc wlc-child-page'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<xsl:call-template name="section_promo" />
		<xsl:call-template name="page_content" />
	</xsl:template>

	<xsl:template name="page_content">
		<div class="wlc-page-content">
			<div class="container">
				<h1>
					<xsl:value-of select="//captions/verify_email_title/@caption" />
				</h1>
				<div style="font-size:1.5em;margin-bottom:2em;">
					<xsl:choose>
						<xsl:when test="//process = 'verify-ok'">
							<xsl:value-of select="//captions/verify_email_done/@caption" />
						</xsl:when>
						<xsl:when test="//process = 'already-registered'">
							<xsl:value-of select="//captions/verify_email_already/@caption" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="//captions/verify_email_error/@caption" />
						</xsl:otherwise>
					</xsl:choose>
				</div>
				<xsl:if test="//process = 'verify-ok'">
					<xsl:call-template name="login" />
				</xsl:if>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
