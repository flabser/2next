<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:variable name="dbid" select="request/@dbid" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Scheduler'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<table width="100%">
			<tr>
				<td width="50%">
					<xsl:call-template name="showasxml_action" />
					<a style="margin-left:10px">
						<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
						<img src="img/refresh.gif" />
						<font class="button">Refresh</font>
					</a>
				</td>
				<td style="text-align:right">
					<xsl:call-template name="close_action" />
				</td>
			</tr>
		</table>
		<br />
		<font style="font-family:verdana; font-size:20px;">
			Scheduler -
			<xsl:value-of select="document/id" />
		</font>
		<br />
		<hr />
		<form action="Provider" method="post" id="scriptest" enctype="application/x-www-form-urlencoded">
			<table border="0" style="margin-top:35px; width:80%; font-size:15px">
				<xsl:for-each select="document/*">
					<tr>
						<td style="text-align:right;  font-weight:bold; width:150px">
							<xsl:value-of select="concat(name(),': ')" />
						</td>
						<td>
							<xsl:value-of select="." />
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<input type="hidden" name="id" id="id" value="{rule/@id}" />
			<input type="hidden" name="groupby" id="groupby" value="{rule/groupby}" />
		</form>
		<div id="footer">
			<div style=" padding:2px 10px 0px 10px; color: #444444; width:600px; margin-top:3px; float:left">
				<a target="_parent" href="Logout" title="{outline/fields/logout/@caption}">
					<img src="img/logout.gif" style="width:15px; height:15px" />
					<font style="margin-left:5px; font-size:11px; vertical-align:3px">logout</font>
				</a>
				&#xA0;
			</div>
			<div style="padding:5px 20px 0px 10px; font-color:#444; width:300px; float:right">
				<div id="langview" style="font-size:12px; float:right">
					<font style="vertical-align:2px">NextBase � 2014</font>
				</div>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
