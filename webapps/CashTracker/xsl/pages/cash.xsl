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
		<div class="view view_accounts">
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
					<span class="vaccount-name">
						<xsl:value-of select="//captions/viewtext1/@caption" />
					</span>
					<span class="vaccount-user">
						<xsl:value-of select="//captions/viewtext2/@caption" />
					</span>
					<span class="vaccount-observers">
						<xsl:value-of select="//captions/viewtext3/@caption" />
					</span>
					<span class="vaccount-amount-control">
						<xsl:value-of select="//captions/viewnumber/@caption" />
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
				<a class="entry-action action-delete" data-ddbid="{@id}" href="#">
					<i class="fa fa-trash" />
				</a>
			</div>
			<div data-ddbid="{@id}" class="entry document js-swipe-entry">
				<label class="entry-select">
					<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
				</label>
				<a href="{@url}" class="entry-link">
					<div class="entry-fields">
						<span class="entry-field vaccount-name">
							<xsl:value-of select="viewcontent/viewtext1" />
						</span>
						<span class="entry-field vaccount-user">
							<xsl:value-of select="viewcontent/viewtext2" />
						</span>
						<span class="entry-field vaccount-observers">
							<span>
								<xsl:value-of select="viewcontent/viewtext3" />
							</span>
						</span>
						<span class="entry-field vaccount-amount-control">
							<xsl:value-of select="viewcontent/viewnumber" />
						</span>
					</div>
				</a>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
