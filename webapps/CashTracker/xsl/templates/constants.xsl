<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="APP_NAME" select="'CashTracker'" />
	<xsl:variable name="APP_LOGO_IMG_SRC" select="'/SharedResources/logos/cashtracker_small.png'" />

	<!-- html5 doctype -->
	<xsl:template name="HTML-DOCTYPE">
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text>!DOCTYPE html<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
	</xsl:template>

	<xsl:variable name="UI_CLIENT">
		<xsl:choose>
			<xsl:when test="//@useragent = 'IPAD_SAFARI' or //@useragent = 'GALAXY_TAB_SAFARI' or //@useragent = 'ANDROID'">
				<xsl:value-of select="'mobile'" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="'desctop'" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>

	<xsl:variable name="VIEW_TEXT_ARROW">
		<span class="vt-arrow"></span>
	</xsl:variable>

	<xsl:template name="STYLE_FIX_FIELDSET">
		<style>
			/* fix: fieldset content overflow */
			fieldset {
				display: block;
				min-width: inherit; /* chrome */
			}
			@-moz-document url-prefix() {
					fieldset {
					display: table-column !important;
				}
			}
		</style>
	</xsl:template>

</xsl:stylesheet>
