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
						<div class="action-bar">
							<div class="action-bar-container">
								<xsl:apply-templates select="//action[@id='custom'][@mode='ON']" mode="back" />
							</div>
						</div>
					</div>
					<div class="pull-right">
						<xsl:call-template name="page-navigator" />
					</div>
				</div>
			</div>
			<div class="view-table">
				<div class="view-table-body">
					<xsl:apply-templates select="//view_content//query/entry" />
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
				<!-- <div class="vt-inf"> <span class="vt-date-short"> <xsl:value-of select="viewcontent/viewdate" /> </span> </div> -->
			</a>
		</div>
	</xsl:template>

</xsl:stylesheet>
