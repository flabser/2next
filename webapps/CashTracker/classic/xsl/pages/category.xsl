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
				<div class="btable">
					<xsl:apply-templates select="//view_content" mode="view-table-head" />
					<xsl:apply-templates select="//view_content//query/entry" mode="view-table-body" />
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="btable">
					<xsl:apply-templates select="//view_content" mode="view-table-head" />
				</div>
				<div class="view-empty"></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="view_content" mode="view-table-head">
		<div class="btable-head">
			<div class="btable-cell tcell-checkbox">
				<input type="checkbox" data-toggle="docid" class="all" />
			</div>
			<div class="btable-cell tcell-icon"></div>
			<xsl:apply-templates select="//request/page/captions/viewtext1" mode="table-title-cell" />
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="view-table-body">
		<div data-ddbid="{@id}" class="btable-row document {@docid}" id="{@docid}{@doctype}">
			<div class="btable-cell tcell-checkbox">
				<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
			</div>
			<div class="btable-cell tcell-icon">
				<span>
					<xsl:attribute name="class" select="concat('operation-type-icon-', viewcontent/viewtext3)" />
				</span>
			</div>
			<div class="btable-cell">
				<xsl:if test="hasresponse = 'true'">
					<xsl:call-template name="viewCategory">
						<xsl:with-param name="colspan" select="'3'" />
					</xsl:call-template>
				</xsl:if>
				<a href="{@url}" title="{@viewtext}" class="doclink viewtext">
					<xsl:if test="hasresponse = 'true'">
						<xsl:attribute name="style" select="'display:inline;'" />
					</xsl:if>
					<xsl:value-of select="viewcontent/viewtext1" />
				</a>
			</div>
		</div>
		<xsl:apply-templates select="responses" />
	</xsl:template>

	<xsl:template match="*" mode="table-title-cell">
		<xsl:param name="style" select="''" />
		<div class="btable-cell" style="{$style}">
			<xsl:value-of select="@caption" />
		</div>
	</xsl:template>

	<xsl:template match="*" mode="table-row-cell">
		<xsl:param name="text" />
		<xsl:param name="style" select="''" />
		<div class="btable-cell" style="{$style}">
			<xsl:copy-of select="$text" />
		</div>
	</xsl:template>

	<xsl:template match="responses">
		<div class="btable-row response response{../@docid}">
			<div class="btable-cell"></div>
			<div class="btable-cell tcell-icon"></div>
			<div class="btable-cell" data-role="docthread">
				<xsl:apply-templates mode="line" />
			</div>
			<script>
				$("td[data-role=docthread]").each(function(){
				$(this).attr("colspan",
				$(this).parent("tr").prev("tr").children("td").length - 1);
				$(this).removeAttr("data-role");
				});
			</script>
		</div>
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
