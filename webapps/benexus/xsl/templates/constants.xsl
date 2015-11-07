<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="APP_LOGO_IMG_SRC" select="'/SharedResources/logos/nubis.png'" />

	<!-- html5 doctype -->
	<xsl:template name="HTML-DOCTYPE">
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text>!DOCTYPE html<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
	</xsl:template>


	<xsl:template name="alerts">
		<div class="alert alert-success reg-result-ok" role="alert" style="display:none;" id="green-alert"></div>
		<div class="alert alert-info reg-result-ok" role="alert" style="display:none;" id="blue-alert"></div>
		<div class="alert alert-warning reg-result-ok" role="alert" style="display:none;" id="yellow-alert"></div>
		<div class="alert alert-danger reg-result-ok" role="alert" style="display:none;" id="red-alert"></div>
	</xsl:template>
	
</xsl:stylesheet>
