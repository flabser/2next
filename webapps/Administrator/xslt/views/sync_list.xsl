<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />
	<xsl:variable name="type">get_doc_list</xsl:variable>
	<xsl:variable name="doctype">rule</xsl:variable>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:template match="/request">
		<font style="font-family:verdana; font-size:20px;">
			<xsl:value-of select="query/@app"/>
		</font>
		<font style="font-family:verdana; font-size:15px;margin-left:5px">Synchronizations</font>
		<div class="actionbar">
			<span class="action">
				<xsl:call-template name="refresh_action"/>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:resetRules('<xsl:value-of select="query/@app"/>');</xsl:attribute>
					<img src="img/force.gif"/>
					<font class="button">Reload rules</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:delRule("sync_rule");</xsl:attribute>
					<img src="img/delete.gif"/>
					<font class="button">Delete</font>
				</a>
			</span>
			<xsl:call-template name="viewstat"/>
		</div>
		<table class="viewtable">
			<tr class="th">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
				</td>
				<td width="20%" class="thcell">id</td>
				<td width="20%" class="thcell">cutoff</td>
				<td width="15%" class="thcell">cutoff strategy</td>
				<td width="20%" class="thcell">doctype</td>
				<td class="thcell" width="15%"></td>
				<td class="thcell"></td>
			</tr>
			<xsl:for-each select="query/entry">
				<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
					<xsl:variable name="num" select="position()"/>
					<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
					<td style="border:1px solid #ccc; text-align:center">
						<input type="checkbox" name="chbox" id="chbox{@docid}" value="{@docid}"/>
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<a class="doclink" title="{filename}">
							<xsl:attribute name="href">Provider?type=get_sync&amp;id=<xsl:value-of select="@docid"/>&amp;app=<xsl:value-of select="app"/></xsl:attribute>
							<xsl:value-of select="@docid"/>
						</a>
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="cutoff"/>
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="cutoffstrategy"/>
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="doctype"/>
					</td>
					<td class="title" style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="description"/>
					</td>
					<td style="border:1px solid #ccc; text-align:center">
						<a>
							<xsl:attribute name="href">javascript:sync('<xsl:value-of select="@docid"/>','<xsl:value-of select="app"/>')</xsl:attribute>
							<img src="img/force_small.gif"/>
						</a>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
</xsl:stylesheet>