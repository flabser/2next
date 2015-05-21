<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />

	<xsl:variable name="doctype" select="'structure'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Document activity'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<h4 class="view-header-title">SmartDocWebKMG Structure</h4>
		<div style="top:40px; float:left;left: 240px;position:absolute">
			<a href="Provider?type=organization&amp;id=o&amp;key=">
				<img src="img/new.gif" />
				<font class="button">new organization</font>
			</a>
			<a style="margin-left:10px">
				<xsl:attribute name="href">javascript:delOrganization('<xsl:value-of select="@dbid" />');</xsl:attribute>
				<img src="img/delete.gif" />
				<font class="button">Remove</font>
			</a>
		</div>
		<table style=" border-collapse: collapse; margin-top:70px;float:left;margin-left:240px;width:70%;">
			<xsl:for-each select="entry">
				<tr>
					<td>
						<input type="checkbox" name="chbox" value="{@docid}" />
						<a class="doclink" href="{@url}">
							<xsl:value-of select="@viewtext" />
						</a>
					</td>
				</tr>
				<xsl:apply-templates select="responses" />
			</xsl:for-each>
		</table>
	</xsl:template>

	<xsl:template match="responses">
		<tr>
			<td>
				<xsl:apply-templates mode="line" />
			</td>
		</tr>
	</xsl:template>

	<xsl:template match="*" mode="line">
		<xsl:if test="name(.) != 'userid' and @docid and @doctype">
			<tr class="response{entry/@docid}" id="{entry/@docid}{@doctype}" name="child">
				<td>
					<xsl:apply-templates mode="line" />
				</td>
			</tr>
		</xsl:if>
	</xsl:template>

	<xsl:template match="viewtext" mode="line" />

	<xsl:template match="entry" mode="line">
		<div class="Node" id="{@docid}{@doctype}">
			<xsl:call-template name="graft" />
			<xsl:apply-templates select="." mode="item" />
		</div>
		<xsl:apply-templates mode="line" />
	</xsl:template>

	<xsl:template match="entry" mode="item">
		<a href="" class="doclink" style="vertical-align:top;" title="{@viewtext}">
			<xsl:attribute name="href" select="concat(@url,'&','dbid=',@dbid)" />
			<font class="font" style="font-style:arial; font-size:13px; padding-left:3px">
				<xsl:value-of select="@viewtext" />
			</font>
		</a>
	</xsl:template>

	<xsl:template name="graft">
		<xsl:apply-templates select="ancestor::entry" mode="tree" />
		<xsl:choose>
			<xsl:when test="following-sibling::*">
				<img src="/SharedResources/img/classic/tree_tee.gif" />
			</xsl:when>
			<xsl:otherwise>
				<img src="/SharedResources/img/classic/tree_corner.gif" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="*" mode="tree">
		<xsl:choose>
			<xsl:when test="following-sibling::*">
				<xsl:if test="not(following-sibling::*[@doctype=891])">
					<img src="/SharedResources/img/classic/tree_bar.gif" />
				</xsl:if>
			</xsl:when>
			<xsl:otherwise>
				<img src="/SharedResources/img/classic/tree_spacer.gif" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
