<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="text" encoding="utf-8" indent="no" />

	<xsl:template match="/request">
		<xsl:text>{</xsl:text>
		<xsl:apply-templates select="//document-data" />
		<xsl:text>}</xsl:text>
	</xsl:template>

	<xsl:template match="*">
		<xsl:apply-templates mode="item" />
	</xsl:template>

	<xsl:template match="*" mode="item">
		<xsl:text>"</xsl:text>
		<xsl:value-of select="name()" />
		<xsl:text>":"</xsl:text>
		<xsl:value-of select="." />
		<xsl:text>"</xsl:text>
		<xsl:if test="following-sibling::node()">
			<xsl:text>,</xsl:text>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
