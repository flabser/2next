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
			<div class="btable-cell">
				<xsl:value-of select="//captions/viewtext1/@caption" />
			</div>
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="view-table-body">
		<div data-ddbid="{@id}" class="btable-row document {@docid}" id="{@docid}{@doctype}">
			<div class="btable-cell tcell-checkbox">
				<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
			</div>
			<div class="btable-cell">
				<a href="{@url}" title="{@viewtext}" class="doclink viewtext">
					<xsl:value-of select="viewcontent/viewtext1" />
				</a>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
