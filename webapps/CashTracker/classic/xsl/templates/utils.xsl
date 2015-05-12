<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="util-js-mark-as-read">
		<xsl:if test="document[@isread = 0][@status != 'new']">
			<script type="text/javascript">
				nbApp.markRead(<xsl:value-of select="document/@doctype" />, <xsl:value-of select="document/@docid" />);
			</script>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
