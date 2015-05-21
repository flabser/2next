<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/saldo.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template match="/request">
		<xsl:call-template name="saldoPlain" />
	</xsl:template>

</xsl:stylesheet>
