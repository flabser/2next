<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<div class="view">
			<div class="view-header">
				<xsl:call-template name="page-info" />
				<xsl:apply-templates select="//actionbar" />
			</div>
			<div class="view-table">
				<xsl:call-template name="view-table" />
			</div>
			<input type="hidden" name="page_id" id="page_id" value="{@id}" />
		</div>
	</xsl:template>

	<xsl:template name="view-table">
		<xsl:choose>
			<xsl:when test="//view_content//query/entry">
				<table>
					<xsl:apply-templates select="//view_content" mode="view-table-head" />
					<xsl:apply-templates select="//view_content//query/entry" mode="view-table-body" />
				</table>
			</xsl:when>
			<xsl:otherwise>
				<table>
					<xsl:apply-templates select="//view_content" mode="view-table-head" />
				</table>
				<div class="view-empty"></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="view_content" mode="view-table-head">
		<tr>
			<th style="text-align:center;height:30px;width:23px;" class="thcell">
				<input type="checkbox" data-toggle="docid" class="all" />
			</th>
			<th></th>
			<th></th>
			<th></th>
		</tr>
	</xsl:template>

	<xsl:template match="entry" mode="view-table-body">
		<tr title="{@viewtext}" class="{@docid}" id="{@docid}{@doctype}">
			<xsl:attribute name="bgcolor">#FFFFFF</xsl:attribute>
			<xsl:if test="dbd &lt; 0 and allcontrol != 0 and /request/@id != 'completetask'">
				<xsl:attribute name="bgcolor">#FFEDED</xsl:attribute>
			</xsl:if>
			<xsl:if test="dbd = 0 and allcontrol != 0 and /request/@id != 'completetask'">
				<xsl:attribute name="bgcolor">#FCFCE5</xsl:attribute>
			</xsl:if>
			<xsl:if test="@isread = '0'">
				<xsl:attribute name="font-weight">bold</xsl:attribute>
			</xsl:if>
			<td style="text-align:center;border:1px solid #ccc;width:23px;">
				<input type="checkbox" name="docid" id="{@id}" autocomplete="off" value="{@doctype}" />
			</td>
			<td style="border:1px solid #ccc; width:20px; padding:3px">
				<xsl:apply-templates select="current()[@hasattach &gt; 0]" mode="attach-icon" />
			</td>
			<td style="border:1px solid #ccc; word-wrap:break-word; padding-left: 5px">
				<div style="overflow:hidden; width:99%;">
					<a href="{@url}" class="doclink viewtext">
						<xsl:if test="@isread = '0'">
							<xsl:attribute name="style">font-weight:bold</xsl:attribute>
						</xsl:if>
						<xsl:call-template name="replace-string">
							<xsl:with-param name="text" select="viewcontent/viewtext" />
							<xsl:with-param name="replace" select="' -&gt; '" />
							<xsl:with-param name="with">
								<xsl:copy-of select="$VIEW_TEXT_ARROW" />
							</xsl:with-param>
						</xsl:call-template>
					</a>
				</div>
			</td>
			<td style="border:1px solid #ccc; border-left:none; width:30px; text-align:center">
				<img class="favicon" style="cursor:pointer; width:18px; height:18px" src="/SharedResources/img/iconset/star_empty.png">
					<xsl:if test="@favourites = 1">
						<xsl:attribute name="onclick">nbApp.docRemoveFromFav(this,<xsl:value-of select="@docid" />,<xsl:value-of
							select="@doctype" />)</xsl:attribute>
						<xsl:attribute name="src">/SharedResources/img/iconset/star_full.png</xsl:attribute>
						<xsl:attribute name="title"><xsl:value-of select="//captions/removefromfav/@caption"></xsl:value-of></xsl:attribute>
					</xsl:if>
					<xsl:if test="@favourites = 0 or not(@fovourites)">
						<xsl:attribute name="onclick">nbApp.docAddToFav(this,<xsl:value-of select="@docid" />,<xsl:value-of
							select="@doctype" />)</xsl:attribute>
						<xsl:attribute name="src">/SharedResources/img/iconset/star_empty.png</xsl:attribute>
						<xsl:attribute name="title"><xsl:value-of select="//captions/addtofav/@caption"></xsl:value-of></xsl:attribute>
					</xsl:if>
				</img>
			</td>
		</tr>

		<xsl:apply-templates select="responses" />
	</xsl:template>

</xsl:stylesheet>
