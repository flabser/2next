<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="actionbar">
		<xsl:if test="count(//action) > 0">
			<div class="action-bar">
				<xsl:apply-templates select="//action" />
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="action">
		<a class="btn action_{@id}" title="{@hint}" href="#" data-action="{@id}">
			<xsl:if test="js">
				<xsl:attribute name="href" select="concat('javascript:', js)" />
			</xsl:if>
			<xsl:if test="@url != ''">
				<xsl:attribute name="href" select="@url" />
			</xsl:if>
			<span>
				<xsl:value-of select="@caption" />
			</span>
		</a>
	</xsl:template>

</xsl:stylesheet>
