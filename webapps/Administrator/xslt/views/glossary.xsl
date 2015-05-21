<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:variable name="doctype" select="'glossary'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Document activity'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">
			<xsl:value-of select="concat(/request/@app,' ', query/@ruleid ,' Glossary')" />
		</h4>
		<div class="actionbar">
			<span class="action">
				<xsl:call-template name="refresh_action" />
			</span>
		</div>
		<table class="viewtable">
			<tr class="th">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
				</td>
				<td width="5%" class="thcell"></td>
				<td width="20%" class="thcell">id</td>
				<td class="thcell">description</td>
			</tr>
			<xsl:for-each select="view/entry">
				<tr>
					<xsl:variable name="num" select="position()" />
					<xsl:if test="$num mod 2 = 0">
						<xsl:attribute name="bgcolor">#efefef</xsl:attribute>
					</xsl:if>
					<td>
						<input type="checkbox" name="chbox" id="{@docid}">
							<xsl:attribute name="name" select="/request/view/entry/author" />
						</input>
					</td>
					<td style="font-size:0.8em;color:gray">
						<xsl:value-of select="$num" />
					</td>
					<td>
						<a title="{filename}">
							<xsl:attribute name="href">Provider?type=get_query&amp;id=<xsl:value-of select="@id" />&amp;app=<xsl:value-of
								select="app" /></xsl:attribute>
							<xsl:value-of select="@id" />
						</a>
					</td>
					<td class="title" style="font-size:0.8em">
						<xsl:value-of select="description" />
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>

</xsl:stylesheet>
