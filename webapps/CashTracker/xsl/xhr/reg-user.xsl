<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template match="/request">
		<xsl:choose>
			<xsl:when test="//process-action">
				<xsl:call-template name="process-action" />
			</xsl:when>
			<xsl:otherwise>

			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="process-action">
		<xsl:apply-templates select="//error" />
		<xsl:apply-templates select="//process" />
	</xsl:template>

	<xsl:template match="error">
		<xsl:value-of select="." />
		<xsl:if test="following-sibling::error">
			<xsl:value-of select="','" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="process">
		<xsl:value-of select="." />
		<xsl:if test="following-sibling::process">
			<xsl:value-of select="','" />
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
