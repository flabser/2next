<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:choose>
			<xsl:when test="$isAjaxRequest">
				<xsl:call-template name="_content" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:call-template name="layout" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="_content">
		<div class="view">
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
		<xsl:apply-templates select="//structure/query/entry" mode="struct" />
	</xsl:template>

	<xsl:template match="query/entry" mode="struct">
		<xsl:apply-templates select="responses" mode="struct" />
	</xsl:template>

	<xsl:template match="responses" mode="struct">
		<div class="entries">
			<xsl:apply-templates select="./entry" mode="struct" />
		</div>
	</xsl:template>

	<xsl:template match="responses/entry" mode="struct">
		<div class="entry-wrap">
			<div class="entry-actions">
				<a class="entry-action action-delete" data-ddbid="{@id}" href="#">
					<i class="fa fa-trash" />
				</a>
			</div>
			<div data-ddbid="{@id}" class="entry document js-swipe-entry">
				<label class="entry-select">
					<input type="checkbox" name="docid" value="{@docid}" />
				</label>
				<a href="{@url}" class="entry-link">
					<div class="entry-fields">
						<span class="entry-field">
							<xsl:value-of select="userid" />
						</span>
					</div>
				</a>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
