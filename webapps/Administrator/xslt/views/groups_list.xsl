<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />

	<xsl:variable name="type" select="'get_groups_list'" />
	<xsl:variable name="doctype" select="'groups'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Groups list'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">Groups list</h4>
		<div class="actionbar">
			<span class="action">
				<a>
					<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
					<img src="img/refresh.gif" />
					<font class="button">Refresh</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">Provider?type=get_user_profile&amp;key=</xsl:attribute>
					<img src="img/new.gif" />
					<font class="button">New group</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:delGroup();</xsl:attribute>
					<img src="img/delete.gif" />
					<font class="button">Remove group</font>
				</a>
			</span>
			<xsl:call-template name="viewstat" />
		</div>
		<table class="viewtable">
			<tr class="th">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
				</td>
				<td width="20%" class="thcell">
					groupID
					<br />
				</td>
				<td width="15%" class="thcell"></td>
				<td width="15%" class="thcell"></td>
			</tr>
			<xsl:for-each select="query/entry">
				<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
					<xsl:variable name="num" select="position()" />
					<td style="border:1px solid #ccc; text-align:center">
						<input type="checkbox" name="chbox" value="{@docid}" />
					</td>
					<td style="border: 1px solid #ccc; padding-left:5px">
						<a class="doclink" title="{@id}">
							<xsl:attribute name="href">Provider?type=get_group&amp;docid=<xsl:value-of select="@docid" />&amp;dbid=<xsl:value-of
								select="@dbid" /></xsl:attribute>
							<xsl:value-of select="viewtext" />
						</a>
					</td>
					<td style="border: 1px solid #ccc; padding-left:5px" />
					<td style="border: 1px solid #ccc; padding-left:5px" />
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
