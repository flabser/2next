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
		<div class="view view_op">
			<div class="view-header">
				<xsl:call-template name="page-info" />
			</div>
			<xsl:apply-templates select="//filter2" mode="filter-container" />
			<div class="view-content">
				<xsl:call-template name="view-table" />
			</div>
			<input type="hidden" name="page_id" id="page_id" value="{@id}" />
		</div>
	</xsl:template>

	<xsl:template name="view-table">
		<header class="entries-head">
			<div class="head-wrap op-head">
				<label class="entry-select">
					<input type="checkbox" data-toggle="docid" class="all" />
				</label>
				<div class="entry-captions">
					<span class="op-icon-att">
						<i class="fa fa-paperclip" />
					</span>
					<span class="op-date">
						<xsl:call-template name="sortingcell">
							<xsl:with-param name="namefield">
								<xsl:value-of select="'VIEWDATE'" />
							</xsl:with-param>
							<xsl:with-param name="sortorder" select="//query/columns/viewdate/sorting/@order" />
							<xsl:with-param name="sortmode" select="//query/columns/viewdate/sorting/@mode" />
						</xsl:call-template>
					</span>
					<span class="op-amount">
						<xsl:call-template name="sortingcell">
							<xsl:with-param name="namefield">
								<xsl:value-of select="'VIEWNUMBER'" />
							</xsl:with-param>
							<xsl:with-param name="sortorder" select="//query/columns/viewnumber/sorting/@order" />
							<xsl:with-param name="sortmode" select="//query/columns/viewnumber/sorting/@mode" />
						</xsl:call-template>
					</span>
					<span class="entry-field op-costcenter"></span>
				</div>
			</div>
		</header>
		<div class="entries">
			<xsl:apply-templates select="//view_content//query/entry" mode="view-table-body" />
		</div>
	</xsl:template>

	<!-- filter -->
	<xsl:template match="content" mode="filter-container">
		<div class="filter">
			<div class="filter-container">
				<xsl:apply-templates select="*" mode="filter" />
			</div>
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
							<xsl:value-of select="//captions/filter_reset/@caption" />
						</xsl:if>
					</span>
				</div>
				<div class="filter-entry-list">
					<ul>
						<xsl:for-each select="./entry">
							<li>
								<xsl:attribute name="onclick">nbApp.filterChoose("<xsl:value-of select='@id' />", "viewtext2")</xsl:attribute>
								<xsl:value-of select="." />
							</li>
						</xsl:for-each>
						<li class="filter-reset">
							<xsl:attribute name="onclick">nbApp.filterResetCurrent("viewtext2")</xsl:attribute>
							<xsl:value-of select="//captions/filter_reset/@caption" />
						</li>
					</ul>
				</div>
			</div>
		</div>
		<xsl:if test="//filter/@mode = 'ON'">
			<button class="filter-reset-all" onclick="nbApp.filterReset()" title="{//captions/removefilter/@caption}">
				<img src="/SharedResources/img/iconset/054-delete.png" />
			</button>
		</xsl:if>
	</xsl:template>
	<!-- /filter -->

	<xsl:template match="entry" mode="view-table-body">
		<div class="entry-wrap">
			<div class="entry-actions">
				<a class="entry-action action-delete" data-ddbid="{@id}" href="#">
					<i class="fa fa-trash" />
				</a>
			</div>
			<div data-ddbid="{@id}" class="entry document js-swipe-entry js_saldo_on_date">
				<xsl:if test="@isread = '0'">
					<xsl:attribute name="class" select="'document unread'" />
				</xsl:if>
				<label class="entry-select">
					<input type="checkbox" name="docid" id="{@id}" value="{@doctype}" />
				</label>
				<a href="{@url}" class="entry-link">
					<div class="entry-fields">
						<span class="entry-field op-icon-att">
							<xsl:apply-templates select=".[@hasattach &gt; 0]" mode="attach-icon" />
						</span>
						<span class="entry-field op-date">
							<xsl:value-of select="substring(viewcontent/viewdate, 0, 11)" />
						</span>
						<span class="entry-field op-amount">
							<xsl:if test="viewcontent/viewnumber &lt; 0">
								<xsl:attribute name="class">entry-field op-amount op-amount-minus</xsl:attribute>
							</xsl:if>
							<xsl:value-of select="format-number(viewcontent/viewnumber, '### ###', 'df')" />
						</span>
						<span class="entry-field vcategory-icon-type">
							<i>
								<xsl:attribute name="class" select="concat('operation-type-icon-', viewcontent/viewtext3)" />
							</i>
						</span>
						<span class="entry-field op-costcenter">
							<xsl:value-of select="viewcontent/viewtext4" />
						</span>
						<span class="entry-field op-vt">
							<span>
								<xsl:call-template name="replace-string">
									<xsl:with-param name="text" select="viewcontent/viewtext" />
									<xsl:with-param name="replace" select="' -&gt; '" />
									<xsl:with-param name="with">
										<xsl:copy-of select="$VIEW_TEXT_ARROW" />
									</xsl:with-param>
								</xsl:call-template>
							</span>
						</span>
					</div>
				</a>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
