<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />

	<xsl:variable name="type" select="'get_doc_list'" />
	<xsl:variable name="doctype" select="'log'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat('Administrator - ', currentview/@title)" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">Logs</h4>
		<div class="actionbar">
			<span class="action">
				<a>
					<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
					<img src="img/refresh.gif" />
					<font class="button">Refresh</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:removeLogFile();</xsl:attribute>
					<img src="img/delete.gif" />
					<font class="button">Remove</font>
				</a>
			</span>
			<xsl:call-template name="viewstat" />
		</div>
		<table class="viewtable">
			<tr class="th" style="height:32px">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
				</td>
				<td class="thcell">Name</td>
				<td width="15%" class="thcell">Size</td>
			</tr>
			<xsl:for-each select="query/entry">
				<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
					<xsl:variable name="num" select="position()" />
					<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
					<td style="border:1px solid #ccc; text-align:center">
						<input type="checkbox" name="id" value="{name}" />
					</td>
					<td style="border: 1px solid #ccc; padding-left:5px">
						<a class="doclink" title="{name}">
							<xsl:attribute name="href">Provider?type=edit&amp;element=log&amp;id=<xsl:value-of select="name" /></xsl:attribute>
							<xsl:value-of select="name" />
						</a>
					</td>
					<td class="title" style="border: 1px solid #ccc; padding-left:5px">
						<xsl:value-of select="length" />
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
