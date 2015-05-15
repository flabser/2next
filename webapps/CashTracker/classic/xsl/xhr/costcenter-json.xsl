<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="text" encoding="utf-8" indent="no" />

	<xsl:template match="/request">
		<xsl:text>[</xsl:text>
		<xsl:apply-templates select="//page//query/entry" />
		<xsl:text>]</xsl:text>
	</xsl:template>

	<xsl:template match="entry">
		<xsl:text>{</xsl:text>
		<xsl:text>ddbid:'</xsl:text>
		<xsl:value-of select="@id" />
		<xsl:text>',</xsl:text>
		<xsl:text>text:'</xsl:text>
		<xsl:value-of select="viewcontent/viewtext1" />
		<xsl:text>'</xsl:text>
		<xsl:text>}</xsl:text>
		<xsl:if test="following-sibling::entry">
			<xsl:value-of select="','" />
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
