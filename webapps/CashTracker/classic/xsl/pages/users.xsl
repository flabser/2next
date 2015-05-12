<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="include">
				<style>
					.view-table a {display:inline;}
					.struct ul, .struct li { list-style: none; }
					.struct li { padding: .4em; }
				</style>
			</xsl:with-param>
		</xsl:call-template>
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
			<xsl:when test="//structure/query/entry">
				<div class="struct">
					<xsl:apply-templates select="//structure/query/entry" mode="struct" />
				</div>
			</xsl:when>
			<xsl:otherwise>
				<div class="view-empty"></div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="query/entry" mode="struct">
		<!-- <ul> -->
		<!-- <li> <input type="checkbox" name="docid" id="{@id}" value="{@doctype}" /> <a href="{@url}"> <xsl:value-of select="viewtext" 
			/> </a> </li> -->
		<xsl:apply-templates select="responses" mode="struct" />
		<!-- </ul> -->
	</xsl:template>

	<xsl:template match="responses" mode="struct">
		<!-- <li> -->
		<ul>
			<xsl:apply-templates select="./entry" mode="struct" />
		</ul>
		<!-- </li> -->
	</xsl:template>

	<xsl:template match="responses/entry" mode="struct">
		<li>
			<a href="{@url}">
				<xsl:value-of select="userid" />
			</a>
		</li>
	</xsl:template>

</xsl:stylesheet>
