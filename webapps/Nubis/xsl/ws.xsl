<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="page_content">
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="apps">
		<xsl:apply-templates select="entry" />
	</xsl:template>

	<xsl:template match="entry">
		<div>
			<a href="/{.}">
				<xsl:value-of select="." />
			</a>
		</div>
	</xsl:template>

</xsl:stylesheet>
