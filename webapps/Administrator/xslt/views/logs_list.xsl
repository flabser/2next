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
			<thead>
				<tr>
					<td class="col-select">
						<input type="checkbox" data-toggle="id" />
					</td>
					<td>Name</td>
					<td width="15%">Size</td>
				</tr>
			</thead>
			<xsl:for-each select="query/entry">
				<tr>
					<td class="col-select">
						<input type="checkbox" name="id" value="{name}" />
					</td>
					<td>
						<a class="doclink">
							<xsl:attribute name="href" select="concat('Provider?type=edit&amp;element=log&amp;id=', name)" />
							<xsl:value-of select="name" />
						</a>
					</td>
					<td>
						<xsl:value-of select="length" />
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
