<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="APP_NAME" select="'Administrator'" />

	<!-- html5 doctype -->
	<xsl:template name="HTML-DOCTYPE">
		<xsl:text disable-output-escaping="yes">&lt;</xsl:text>!DOCTYPE html<xsl:text disable-output-escaping="yes">&gt;</xsl:text>
	</xsl:template>

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
