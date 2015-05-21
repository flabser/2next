<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<div class="view view_category">
			<div class="view-header">
				<xsl:call-template name="page-info" />
			</div>
			<div class="view-content">
				<xsl:call-template name="view-table" />
			</div>
			<input type="hidden" name="page_id" id="page_id" value="{@id}" />
		</div>
	</xsl:template>

	<xsl:template name="view-table">
		<header class="entries-head">
			<div class="head-wrap">
				<label class="entry-select">
					<input type="checkbox" data-toggle="docid" class="all" />
				</label>
				<div class="entry-captions">
					<span class="vcategory-icon-type"></span>
					<span class="vcategory-name">
						<xsl:value-of select="//captions/viewtext1/@caption" />
					</span>
				</div>
			</div>
		</header>
		<div class="entries">
			<xsl:apply-templates select="//view_content//query/entry" mode="view-table-body" />
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="view-table-body">
		<div class="entry-wrap">
			<div class="entry-actions">
				<a class="entry-action action-delete" href="#" onclick="">
					<i class="fa fa-trash" />
				</a>
			</div>
			<div data-ddbid="{@id}" class="entry document js-swipe-entry">
				<label class="entry-select">
					<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
				</label>
				<xsl:if test="hasresponse = 'true'">
					<xsl:call-template name="viewCategory">
						<xsl:with-param name="colspan" select="'3'" />
					</xsl:call-template>
				</xsl:if>
				<a href="{@url}" class="entry-link">
					<div class="entry-fields">
						<span class="entry-field vcategory-icon-type">
							<i>
								<xsl:attribute name="class" select="concat('operation-type-icon-', viewcontent/viewtext3)" />
							</i>
						</span>
						<span class="entry-field vcategory-name">
							<xsl:value-of select="viewcontent/viewtext1" />
						</span>
					</div>
				</a>
			</div>
		</div>
		<xsl:apply-templates select="responses" />
		
		<!-- <div data-ddbid="{@id}" class="btable-row document {@docid}" id="{@docid}{@doctype}">
			<div class="btable-cell cell-checkbox">
				<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
			</div>
			<div class="btable-cell cell-icon">
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
		<xsl:apply-templates select="responses" /> -->
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
