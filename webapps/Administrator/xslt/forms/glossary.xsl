<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/action.xsl"/>	
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	 doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:template match="/request">
		<head>
			<title>Administrator - Glossary</title>
			<link rel="stylesheet" href="css/main.css"/>
			<link rel="stylesheet" href="css/view.css"/>
			<link rel="stylesheet" href="css/dialogs.css"/>
			<script type="text/javascript" src="scripts/stdlib/dynamicform.js"/>
			<script type="text/javascript" src="scripts/outline.js"/>
			<script type="text/javascript" src="scripts/service.js"/>
			<script type="text/javascript" src="scripts/dialogs.js"/>
			<script type="text/javascript" src="scripts/form.js"/>
		</head>
		<body>
			<table width="100%">
				<tr>
					<td width="50%">						
						<xsl:call-template name="showasxml_action"/>						
					</td>
					<td style="text-align:right">
						<xsl:call-template name="close_action"/>						
					</td>
				</tr>
			</table>
			<br/>
			<br/>
			<font style="font-family:verdana; font-size:20px;">Головной документ: </font><xsl:value-of select="document/@docid"/>
			<table style="font-family:verdana; margin-top: 25px">
				<td width="20%" class="th">name</td>
				<td width="30%" class="th">value</td>
				<td width="20%" class="th">type</td>
				<td width="5%" class="th">system</td>
				<tr>
					<td style="text-align:right; font-size:17px;">author</td>
					<td>&#xA0;<xsl:value-of select="document/@author"/></td>
					<td>system</td>
				</tr>
				<tr>
					<td style="text-align:right; font-size:17px;">regdate</td>
					<td>&#xA0;<xsl:value-of select="document/@regdate"/></td>
					<td>system</td>
				</tr>
				<tr>
					<td style="text-align:right; font-size:17px;">lastupdate</td>
					<td>&#xA0;<xsl:value-of select="document/@lastupdate"/></td>
					<td>system</td>
				</tr>
				<xsl:for-each select="document/node()">
					<tr>
						<td style="text-align:right ;font-size:17px; font-weight:bold;">
							<xsl:value-of select="name(.)"/> 						
						</td>
						<td style="font-size:17">
							&#xA0;&#xA0;<xsl:value-of select="node()"/>
						</td>
						<td style="font-size:1em">
							<xsl:value-of select="@type"/>
						</td>					
					</tr>
				</xsl:for-each>
			</table>
		</body>
	</xsl:template>
</xsl:stylesheet>