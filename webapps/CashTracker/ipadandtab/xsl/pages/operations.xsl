<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<div class="view">
			<div id="view-header">
				<xsl:call-template name="pageinfo" />
				<div class="container">
					<div class="pull-left">
						<div class="action-bar action-bar-dynamic">
							<div class="action-bar-container">
								<xsl:apply-templates select="//action[@id='delete_document'][@mode='ON']" mode="delete_document" />
							</div>
							<div class="action-bar-toggle" data-role="menu-toggle" data-class="expanded">
								<div class="menu-icon-bar" />
								<div class="menu-icon-bar" />
								<div class="menu-icon-bar" />
							</div>
						</div>
					</div>
					<div class="pull-right">
						<xsl:call-template name="page-navigator" />
					</div>
				</div>
			</div>
			<div class="view-table">
				<div class="view-table-head">
					<div class="action-bar">
						<div class="action-bar-container">
							<!-- <xsl:apply-templates select="//filter_operation/response/content" mode="filter-container" /> -->
							<div class="action-group">
								<xsl:call-template name="sortingcell">
									<xsl:with-param name="namefield">
										<xsl:value-of select="'VIEWDATE'" />
									</xsl:with-param>
									<xsl:with-param name="sortorder" select="//query/columns/viewdate/sorting/@order" />
									<xsl:with-param name="sortmode" select="//query/columns/viewdate/sorting/@mode" />
								</xsl:call-template>
							</div>
							<div class="action-group">
								<xsl:call-template name="sortingcell">
									<xsl:with-param name="namefield">
										<xsl:value-of select="'VIEWNUMBER'" />
									</xsl:with-param>
									<xsl:with-param name="sortorder" select="//query/columns/viewnumber/sorting/@order" />
									<xsl:with-param name="sortmode" select="//query/columns/viewnumber/sorting/@mode" />
								</xsl:call-template>
							</div>
							<xsl:if test="//sorting/@mode = 'ON'">
								<button class="button button-default action-group" onclick="resetFilter('alloperations')" style="vertical-align:middle;">
									<img src="/SharedResources/img/iconset/cross.png" />
								</button>
							</xsl:if>
						</div>
					</div>
				</div>
				<div class="view-table-body">
					<xsl:apply-templates select="//view_content//query/entry" />
					<input type="hidden" name="page_id" id="page_id" value="{@id}" />
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template match="//view_content//query/entry">
		<div class="vt-row">
			<div class="vt-checkbox">
				<label class="vt-checkbox-label">
					<input type="checkbox" name="chbox" id="doc-{@id}" value="{@id}" />
				</label>
			</div>
			<div class="vt-inf">
				<div class="vt-sum">
					<xsl:if test="viewcontent/viewnumber &lt; 0">
						<xsl:attribute name="class">vt-sum negative-sum</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="format-number(viewcontent/viewnumber, '### ###.##', 'df')" />
				</div>
				<span class="vt-cell-attach-icon">
					<xsl:apply-templates select="current()[@hasattach &gt; 0]" mode="attach-icon" />
					<span>
						<xsl:attribute name="class" select="concat('operation-type-icon-', viewcontent/viewtext3)" />
					</span>
				</span>
				<div class="vt-favorite">

				</div>
			</div>
			<a href="{@url}" class="doclink vt-view-text">
				<xsl:if test="@isread = '0'">
					<xsl:attribute name="class">doclink vt-view-text unread</xsl:attribute>
				</xsl:if>

				<xsl:call-template name="replace-string">
					<xsl:with-param name="text" select="viewcontent/viewtext" />
					<xsl:with-param name="replace" select="' -&gt; '" />
					<xsl:with-param name="with">
						<span class="vt-arrow"></span>
					</xsl:with-param>
				</xsl:call-template>

				<span class="vt-add">
					<xsl:if test="viewcontent/viewtext4 != '-' and viewcontent/viewtext4 != ''">
						<span class="vt-4">
							<xsl:value-of select="viewcontent/viewtext4" />
						</span>
					</xsl:if>
					<span class="vt-1">
						<xsl:value-of select="viewcontent/viewtext1" />
					</span>
				</span>
			</a>
		</div>
	</xsl:template>

	<!-- filter -->
	<xsl:template match="content" mode="filter-container">
		<div class="filter">
			<xsl:apply-templates select="*" mode="filter" />
		</div>
	</xsl:template>

	<xsl:template match="*" mode="filter">
		<div class="filter-entry">
			<div class="filter-entry-title">
				<xsl:value-of select="@title" />
			</div>
			<div class="filter-entry-list-wrapper">
				<div class="filter-entry-list-title">
					<span class="filter-entry-list-toggle-btn">
						<xsl:attribute name="onclick">toggleFilterList(this)</xsl:attribute>
						<xsl:if test="//query/columns/viewtext2/filter/@mode = 'ON'">
							<xsl:value-of select="./entry[@id = //query/columns/viewtext2/filter[@mode='ON']/@keyword]" />
						</xsl:if>
						<xsl:if test="//query/columns/viewtext2/filter/@mode != 'ON'">
							все
						</xsl:if>
					</span>
				</div>
				<div class="filter-entry-list">
					<ul>
						<xsl:for-each select="./entry">
							<li>
								<xsl:attribute name="onclick">chooseFilter("<xsl:value-of select='@id' />", "viewtext2")</xsl:attribute>
								<xsl:value-of select="." />
							</li>
						</xsl:for-each>
						<li class="filter-reset">
							<xsl:attribute name="onclick">resetCurrentFilter("viewtext2")</xsl:attribute>
							все
						</li>
					</ul>
				</div>
			</div>
		</div>
		<xsl:if test="//filter/@mode = 'ON'">
			<button class="filter-reset-all" onclick="resetFilter()" title="{//captions/removefilter/@caption}">
				<img src="/SharedResources/img/iconset/054-delete.png" />
			</button>
		</xsl:if>
	</xsl:template>
	<!-- /filter -->

</xsl:stylesheet>
