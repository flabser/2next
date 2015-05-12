<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />
	<xsl:variable name="type">activity</xsl:variable>
	<xsl:variable name="doctype">activity</xsl:variable>
	<xsl:variable name="viewtype">activity</xsl:variable>
	<xsl:variable name="currentapp" select="currentview/@app"/>
	<xsl:variable name="currentview" select="currentview"/>
	<xsl:variable name="currentservice" select="currentview/@service"/>

	<xsl:template match="/request[@element != 'activity']">
		<html>
			<head>
				<title>Administrator - Global settings</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper;">
					<div id="blockWindow" style="display:none"/>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">
								<font style="font-size:1.1em;">Global settings</font>
							</h4>
							<span class="action">
								<a>
									<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
									<img src="img/refresh.gif"/>
									<font class="button">Refresh</font>
								</a>
							</span>
							<table border="0" style="margin-top:5px; width:80%; font-size:15px">
								<xsl:for-each select="query/document/*[not(name() ='applications')]">
									<tr>
										<td style="text-align:right;  font-weight:bold; width:150px">
											<xsl:value-of select="name()"/>:&#xA0;
										</td>
										<td>
											<xsl:value-of select="."/>
										</td>
									</tr>
								</xsl:for-each>
							</table>
							<br/>
							<table class="viewtable">
								<tr style="height:32px" class="th">
									<td class="thcell" style="padding:5px">Application name</td>
									<td class="thcell" style="padding:5px">Organization</td>
									<td class="thcell" style="padding:5px">License count</td>
								</tr>
								<xsl:for-each select="query/document/applications/entry">
									<tr class="entrylist">
										<td style="border:1px solid #ccc; padding-left:5px"><xsl:value-of select="apptype"/></td>
										<td style="border:1px solid #ccc; padding-left:5px"><xsl:value-of select="orgname"/></td>
										<td style="border:1px solid #ccc; padding-left:5px"><xsl:value-of select="liccount"/></td>
									</tr>
								</xsl:for-each>
							</table>
						</div>
					</div>
					<xsl:call-template name="footer"/>
				</div>
			</body>
		</html>
	</xsl:template>

</xsl:stylesheet>