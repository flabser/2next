<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout" />
	</xsl:template>

	<xsl:template name="_content">
		<section class="ws">
			<xsl:apply-templates />
		</section>
	</xsl:template>

	<xsl:template match="apps">
		<h1>Apps</h1>
		<xsl:apply-templates select="entry" mode="app" />
	</xsl:template>

	<xsl:template match="entry" mode="app">
		<div>
			<a href="/{apptype}/{appid}">
				<xsl:value-of select="status" />
				-
				<xsl:value-of select="appname" />
				<small>
					-
					<xsl:value-of select="concat('(', apptype, ')')" />
				</small>
			</a>
		</div>
	</xsl:template>

	<xsl:template match="templates">
		<h1>Templates</h1>
		<xsl:apply-templates select="entry" mode="template" />
	</xsl:template>

	<xsl:template match="entry" mode="template">
		<div>
			<a href="/{.}">
				<xsl:value-of select="." />
			</a>
		</div>
	</xsl:template>

</xsl:stylesheet>
