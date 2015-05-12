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
			<xsl:apply-templates select="//filter" mode="filter-container" />
			<div class="view-table">
				<xsl:call-template name="view-table" />
			</div>
			<input type="hidden" name="page_id" id="page_id" value="{@id}" />
		</div>
	</xsl:template>

	<xsl:template name="view-table">
		<xsl:choose>
			<xsl:when test="//view_content//query/entry">
				<div class="btable oper-table oper-by-cash-table">
					<xsl:apply-templates select="//view_content" mode="view-table-head" />
					<xsl:apply-templates select="//view_content//query/entry" mode="view-table-body" />
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="btable oper-table oper-by-cash-table">
					<xsl:apply-templates select="//view_content" mode="view-table-head" />
				</div>
				<div class="view-empty"></div>
			</xsl:otherwise>
		</xsl:choose>
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

	<xsl:template match="view_content" mode="view-table-head">
		<div class="btable-head">
			<div class="btable-cell tcell-checkbox">
				<input type="checkbox" data-toggle="docid" class="all" />
			</div>
			<div class="btable-cell tcell-icon"></div>
			<div class="btable-cell tcell-date">
				<xsl:call-template name="sortingcell">
					<xsl:with-param name="namefield">
						<xsl:value-of select="'VIEWDATE'" />
					</xsl:with-param>
					<xsl:with-param name="sortorder" select="//query/columns/viewdate/sorting/@order" />
					<xsl:with-param name="sortmode" select="//query/columns/viewdate/sorting/@mode" />
				</xsl:call-template>
			</div>
			<div class="btable-cell tcell-sum">
				<xsl:call-template name="sortingcell">
					<xsl:with-param name="namefield">
						<xsl:value-of select="'VIEWNUMBER'" />
					</xsl:with-param>
					<xsl:with-param name="sortorder" select="//query/columns/viewnumber/sorting/@order" />
					<xsl:with-param name="sortmode" select="//query/columns/viewnumber/sorting/@mode" />
				</xsl:call-template>
			</div>
			<div class="btable-cell tcell-vt">
				<xsl:value-of select="//captions/viewtext/@caption" />
			</div>
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="view-table-body">
		<div title="" data-ddbid="{@id}" class="btable-row document {@docid} js_saldo_on_date" id="{@docid}{@doctype}">
			<xsl:if test="@isread = '0'">
				<xsl:attribute name="class" select="concat('document ', @docid, ' unread')" />
			</xsl:if>
			<div class="btable-cell tcell-checkbox">
				<input type="checkbox" name="docid" id="{@id}" value="{@id}" />
			</div>
			<div class="btable-cell tcell-icon">
				<xsl:apply-templates select=".[@hasattach &gt; 0]" mode="attach-icon" />
			</div>
			<div class="btable-cell tcell-date">
				<a href="{@url}">
					<xsl:value-of select="substring(viewcontent/viewdate, 0, 11)" />
				</a>
			</div>
			<div class="btable-cell tcell-sum">
				<a href="{@url}">
					<span class="vt-sum">
						<xsl:if test="viewcontent/viewnumber &lt; 0">
							<xsl:attribute name="class">vt-sum vt-sum-minus</xsl:attribute>
						</xsl:if>
						<xsl:value-of select="format-number(viewcontent/viewnumber, '### ###', 'df')" />
					</span>
				</a>
			</div>
			<div class="btable-cell tcell-vt">
				<a href="{@url}" class="viewtext">
					<xsl:call-template name="replace-string">
						<xsl:with-param name="text" select="viewcontent/viewtext" />
						<xsl:with-param name="replace" select="' -&gt; '" />
						<xsl:with-param name="with">
							<xsl:copy-of select="$VIEW_TEXT_ARROW" />
						</xsl:with-param>
					</xsl:call-template>
				</a>
			</div>
		</div>

		<xsl:apply-templates select="responses" />
	</xsl:template>

</xsl:stylesheet>
