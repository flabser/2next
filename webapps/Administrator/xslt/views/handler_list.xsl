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
			<xsl:with-param name="w_title" select="'Administrator - Handlers list'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">
			<xsl:value-of select="query/@app" />
			Handlers
		</h4>
		<div class="actionbar">
			<span class="action">
				<xsl:call-template name="refresh_action" />
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:resetRules('<xsl:value-of select="query/@app" />');</xsl:attribute>
					<img src="img/force.gif" />
					<font class="button">Reload rules</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:delRule("handler_rule");</xsl:attribute>
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
				<td width="25%" class="thcell">id</td>
				<td width="30%" class="thcell">trigger</td>
				<td class="thcell" width="35%">run under...</td>
				<td class="thcell"></td>
			</tr>
			<xsl:for-each select="query/entry">
				<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
					<xsl:variable name="num" select="position()" />
					<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
					<td style="border:1px solid #ccc; text-align:center">
						<input type="checkbox" name="chbox" id="chbox{@docid}" value="{@docid}" />
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<a class="doclink" title="{filename}">
							<xsl:attribute name="href">Provider?type=edit&amp;element=handler_rule&amp;id=<xsl:value-of
								select="@docid" />&amp;app=<xsl:value-of select="app" /></xsl:attribute>
							<xsl:value-of select="@docid" />
						</a>
					</td>
					<td class="title" style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="trigger" />
					</td>
					<td class="title" style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="rununderuser" />
					</td>
					<td class="title" style="border:1px solid #ccc; text-align:center">
						<a style="margin-left:10" title="run unscheduled ..." target="_blank">
							<xsl:attribute name="href">Provider?type=service&amp;operation=do_saved_handler&amp;id=<xsl:value-of
								select="@docid" />&amp;app=<xsl:value-of select="app" /></xsl:attribute>
							<img src="img/force_small.gif" />
						</a>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
