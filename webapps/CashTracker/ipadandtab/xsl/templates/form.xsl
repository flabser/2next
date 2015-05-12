<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="form-utils.xsl" />

	<xsl:template name="markisread">
		<xsl:if test="document[@isread = 0][@status != 'new']">
			<script>
				markRead(<xsl:value-of select="concat(document/@doctype, ', ', document/@docid)" />);
			</script>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
