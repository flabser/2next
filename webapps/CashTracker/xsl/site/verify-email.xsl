<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

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
					<xsl:value-of select="//captions/title/@caption" />
				</h1>
				<section style="font-size:1.5em;">
					<xsl:if test="//verify-result = 'error'">
						<xsl:value-of select="//captions/verify-error/@caption" />
					</xsl:if>
					<xsl:if test="//verify-result = 'ok'">
						<xsl:value-of select="//captions/verify-done/@caption" />
					</xsl:if>
				</section>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
