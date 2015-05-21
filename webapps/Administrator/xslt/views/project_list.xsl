<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:variable name="type" select="'get_doc_list'" />
	<xsl:variable name="doctype" select="'project'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Project list'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">
			<xsl:value-of select="query/@dbid" />
			Projects
		</h4>
		<div class="actionbar">
			<span class="action">
				<a>
					<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
					<img src="img/refresh.gif" />
					<font class="button">Refresh</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:removeDocument("<xsl:value-of select="query/@dbid" />");</xsl:attribute>
					<img src="img/delete.gif" />
					<font class="button">Delete document</font>
				</a>
			</span>
			<xsl:call-template name="viewstat" />
		</div>
		<table class="viewtable">
			<tr class="th">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
				</td>
				<td width="10%" class="thcell">docID</td>
				<td width="15%" class="thcell">parentDoc</td>
				<td width="15%" class="thcell">author</td>
				<td width="25%" class="thcell">reg date</td>
				<td width="25%" class="thcell">last update</td>
				<td width="10%" class="thcell">syncstatus</td>
			</tr>
			<xsl:for-each select="query/entry">
				<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
					<xsl:variable name="num" select="position()" />
					<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
					<td style="border:1px solid #ccc; text-align:center">
						<input type="checkbox" name="chbox" id="chbox{@docid}" value="{@docid}" class="{@doctype}" />
					</td>
					<td style="border: 1px solid #ccc; padding-left:5px">
						<a class="doclink" title="{@docid}">
							<xsl:attribute name="href">Provider?type=get_project&amp;docid=<xsl:value-of select="@docid" />&amp;dbid=<xsl:value-of
								select="@dbid" /></xsl:attribute>
							<xsl:value-of select="@docid" />
						</a>
					</td>
					<td class="title" style="border: 1px solid #ccc; padding-left:5px">
						<xsl:value-of select="concat(@parentdocid,' - ', @parentdoctype)" />
					</td>
					<td class="title" style="border: 1px solid #ccc; padding-left:5px">
						<xsl:value-of select="@author" />
					</td>
					<td class="title" style="border: 1px solid #ccc; padding-left:5px">
						<xsl:value-of select="@regdate" />
					</td>
					<td class="title" style="border: 1px solid #ccc; padding-left:5px">
						<xsl:value-of select="@lastupdated" />
					</td>
					<td style="border: 1px solid #ccc; padding-left:5px">
						<xsl:value-of select="@syncstatus" />
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
