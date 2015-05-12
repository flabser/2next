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
			<th style="text-align:center;width:30px;">
				<input type="checkbox" data-toggle="docid" class="all" />
			</th>
			<xsl:apply-templates select="//request/page/captions/viewtext1" mode="table-title-cell" />
			<xsl:apply-templates select="//request/page/captions/viewtext3" mode="table-title-cell">
				<xsl:with-param name="style" select="'width:20%;'" />
			</xsl:apply-templates>
			<xsl:apply-templates select="//request/page/captions/viewtext4" mode="table-title-cell">
				<xsl:with-param name="style" select="'width:21%;'" />
			</xsl:apply-templates>
		</tr>
	</xsl:template>

	<xsl:template match="entry" mode="view-table-body">
		<tr class="document {@docid}" id="{@docid}{@doctype}">
			<xsl:if test="@isread = '0'">
				<xsl:attribute name="style">font-weight:bold</xsl:attribute>
			</xsl:if>
			<td style="text-align:center;">
				<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
			</td>
			<td>
				<xsl:if test="hasresponse = 'true'">
					<xsl:call-template name="viewCategory">
						<xsl:with-param name="colspan" select="'3'" />
					</xsl:call-template>
				</xsl:if>
				<a href="{@url}" title="{@viewtext}" class="doclink viewtext">
					<xsl:if test="hasresponse = 'true'">
						<xsl:attribute name="style" select="'display:inline;'" />
					</xsl:if>
					<xsl:if test="/request/@id = 'favdocs'">
						<xsl:value-of select="viewcontent/viewtext" />
					</xsl:if>
					<xsl:if test="/request/@id != 'favdocs'">
						<xsl:value-of select="viewcontent/viewtext1" />
					</xsl:if>
				</a>
			</td>
			<xsl:apply-templates select="//request/page/captions/viewtext3" mode="table-row-cell">
				<xsl:with-param name="style" select="'width:20%;'" />
				<xsl:with-param name="text">
					<span>
						<xsl:attribute name="class" select="concat('operation-type-icon-', viewcontent/viewtext3)" />
					</span>
					<xsl:value-of select="viewcontent/viewtext3" />
				</xsl:with-param>
			</xsl:apply-templates>
			<xsl:apply-templates select="//request/page/captions/viewtext4" mode="table-row-cell">
				<xsl:with-param name="style" select="'width:20%;'" />
				<xsl:with-param name="text" select="viewcontent/viewtext4" />
			</xsl:apply-templates>
			<xsl:if test="../@ruleid = 'favdocs'">
				<td style="text-align:center">
					<img class="favicon" style="cursor:pointer;width:19px;height:19px" src="/SharedResources/img/iconset/star_empty.png">
						<xsl:if test="@favourites = 1">
							<xsl:attribute name="onclick">nbApp.docRemoveFromFav(<xsl:value-of
								select="concat('this', ',', @docid, ',', @doctype)" />)</xsl:attribute>
							<xsl:attribute name="src">/SharedResources/img/iconset/star_full.png</xsl:attribute>
						</xsl:if>
						<xsl:if test="@favourites = 0">
							<xsl:attribute name="onclick">nbApp.docAddToFav(<xsl:value-of select="concat('this', ',', @docid, ',', @doctype)" />)</xsl:attribute>
							<xsl:attribute name="src">/SharedResources/img/iconset/star_empty.png</xsl:attribute>
						</xsl:if>
					</img>
				</td>
			</xsl:if>
		</tr>
		<xsl:apply-templates select="responses" />
	</xsl:template>

	<xsl:template match="*" mode="table-title-cell">
		<xsl:param name="style" select="''" />
		<th style="{$style}">
			<xsl:value-of select="@caption" />
		</th>
	</xsl:template>

	<xsl:template match="*" mode="table-row-cell">
		<xsl:param name="text" />
		<xsl:param name="style" select="''" />
		<td style="{$style}">
			<xsl:copy-of select="$text" />
		</td>
	</xsl:template>

	<xsl:template match="responses">
		<tr class="response response{../@docid}">
			<td />
			<td data-role="docthread">
				<xsl:apply-templates mode="line" />
			</td>
			<script>
				$("td[data-role=docthread]").each(function(){
				$(this).attr("colspan",
				$(this).parent("tr").prev("tr").children("td").length - 1);
				$(this).removeAttr("data-role");
				});
			</script>
		</tr>
	</xsl:template>

	<xsl:template match="viewtext" mode="line" />

	<xsl:template match="entry" mode="line">
		<div class="document" id="{@docid}{@doctype}">
			<xsl:call-template name="graft" />
			<xsl:apply-templates select="." mode="item" />
		</div>
		<xsl:apply-templates mode="line" />
	</xsl:template>

	<xsl:template match="entry" mode="item">
		<a href="{@url}" title="{@viewtext}" class="doclink" style="display:inline;margin-left:4px;padding:4px;">
			<xsl:value-of select="viewtext" />
		</a>
	</xsl:template>

	<xsl:template name="graft">
		<xsl:apply-templates select="ancestor::entry" mode="tree" />
		<xsl:choose>
			<xsl:when test="following-sibling::entry">
				<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_tee.gif" />
			</xsl:when>
			<xsl:otherwise>
				<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_corner.gif" />
			</xsl:otherwise>
		</xsl:choose>
		<span style="width:15px;">
			<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
		</span>
	</xsl:template>

	<xsl:template match="responses" mode="tree" />

	<xsl:template match="*" mode="tree">
		<xsl:choose>
			<xsl:when test="following-sibling::entry and entry[@url]">
				<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_bar.gif" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="parent::responses">
					<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_spacer.gif" />
				</xsl:if>
				<xsl:if test="parent::entry">
					<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_spacer.gif" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
