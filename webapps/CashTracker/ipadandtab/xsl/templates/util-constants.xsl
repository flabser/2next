<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="APP_NAME" select="'Cash Tracker'" />
	<xsl:variable name="APP_LOGO_IMG_SRC" select="'/SharedResources/logos/cashtracker_small.png'" />

	<xsl:variable name="VIEW_TEXT_ARROW">
		<span class="vt-arrow"></span>
	</xsl:variable>

	<!-- html5 doctype -->
	<xsl:template name="HTML-DOCTYPE">
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text>!DOCTYPE html<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
	</xsl:template>

</xsl:stylesheet>
