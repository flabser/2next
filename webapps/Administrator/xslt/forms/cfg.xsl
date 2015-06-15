<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:variable name="type" select="'activity'" />
	<xsl:variable name="doctype" select="'activity'" />
	<xsl:variable name="viewtype" select="'activity'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Global settings'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">
			<font style="font-size:1.1em;">Global settings</font>
		</h4>
		<span class="action">
			<a>
				<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
				<img src="img/refresh.gif" />
				<font class="button">Refresh</font>
			</a>
		</span>
		<table border="0" style="margin-top:5px; width:80%; font-size:15px">
			<xsl:for-each select="view/document/*[not(name() ='applications')]">
				<tr>
					<td style="text-align:right;  font-weight:bold; width:150px">
						<xsl:value-of select="name()" />
						:&#xA0;
					</td>
					<td>
						<xsl:value-of select="." />
					</td>
				</tr>
			</xsl:for-each>
		</table>
		<br />
		<table class="viewtable">
			<tr style="height:32px" class="th">
				<td class="thcell" style="padding:5px">Application name</td>
				<td class="thcell" style="padding:5px">Organization</td>
				<td class="thcell" style="padding:5px">License count</td>
			</tr>
			<xsl:for-each select="query/document/applications/entry">
				<tr class="entrylist">
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="apptype" />
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="orgname" />
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="liccount" />
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
