<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="actionbar">
		<xsl:param name="fixed_top" select="'action-bar-top'" />

		<xsl:if test="count(//action) > 0 or //view_content">
			<div class="action-bar {$fixed_top}">
				<xsl:if test="count(//action) > 0">
					<div class="action-group">
						<xsl:apply-templates select="//action" />
					</div>
				</xsl:if>
				<xsl:if test="//view_content">
					<div class="action-group pull-right">
						<xsl:apply-templates select="//view_content" mode="page-navigator" />
					</div>
				</xsl:if>
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
			<i class="action_icon"></i>
			<xsl:value-of select="@caption" />
		</a>
	</xsl:template>

</xsl:stylesheet>
