<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:variable name="type" select="'get_doc_list'" />
	<xsl:variable name="doctype" select="'rule'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Page list'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">
			<xsl:value-of select="concat(query/@app,'Pages')" />
		</h4>
		<div class="actionbar">
			<span class="action">
				<xsl:call-template name="refresh_action" />
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:resetRules('<xsl:value-of select="query/@app" />');</xsl:attribute>
					<img src="img/force.gif" />
					<font class="button">Reload rules</font>
				</a>
			</span>
			<span class="action">
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:delRule("page_rule");</xsl:attribute>
					<img src="img/delete.gif" />
					<font class="button">Delete</font>
				</a>
			</span>
			<xsl:call-template name="viewstat" />
		</div>
		<table class="viewtable">
			<tr class="th">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
				</td>
				<td width="20%" class="thcell">id</td>
				<td width="20%" class="thcell">cache</td>
				<td width="35%" class="thcell">elements</td>
				<td class="thcell">hits</td>
				<td class="thcell" />
			</tr>
			<xsl:for-each select="query/entry">
				<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
					<xsl:variable name="num" select="position()" />
					<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
					<td style="border:1px solid #ccc; text-align:center">
						<input type="checkbox" name="chbox" id="chbox{@id}" value="{@id}" />
					</td>
					<td style="border:1px solid #ccc; padding-left:5px;">
						<a class="doclink" title="{filename}">
							<xsl:attribute name="href" select="@url" />
							<xsl:value-of select="@id" />
						</a>
					</td>
					<td class="title" style="border:1px solid #ccc; padding-left:5px;">
						<xsl:value-of select="cache" />
					</td>
					<td class="title" style="border:1px solid #ccc; padding-left:5px;">
						<xsl:value-of select="elements" />
					</td>
					<td class="title" style="border:1px solid #ccc; padding-left:5px;">
						<xsl:value-of select="hits" />
					</td>
					<td class="title" style="border:1px solid #ccc; text-align:center">
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
