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
		<h1>3 штуки для наглядности</h1>
		<section class="tn-apps">
			<xsl:apply-templates select="entry" mode="app" />
			<xsl:apply-templates select="entry" mode="app" />
			<xsl:apply-templates select="entry" mode="app" />
		</section>
	</xsl:template>

	<xsl:template match="entry" mode="app">
		<div class="tn-app">
			<xsl:if test="status != 'ON_LINE'">
				<xsl:attribute name="class" select="'tn-app off'" />
			</xsl:if>
			<a href="/{apptype}/{appid}">
				<xsl:if test="status != 'ON_LINE'">
					<xsl:attribute name="href" select="'#'" />
					<xsl:attribute name="onclick" select="'return false;'" />
				</xsl:if>
				<span class="tn-app-logo">
					<img class="tn-app-logo" src="/{apptype}/img/logo.png" alt="logo" />
				</span>
				<span class="tn-app-type">
					<xsl:value-of select="apptype" />
				</span>
				<span class="tn-app-name">
					<xsl:value-of select="appname" />
				</span>
				<span class="tn-app-owner">
					<xsl:value-of select="owner" />
				</span>
			</a>
			<button class="tn-app-edit js-tn-app-edit">
				<i class="fa fa-gear" />
			</button>
			<div class="tn-app-settings">
				<button class="tn-app-remove">
					<i class="fa fa-trash" />
					<xsl:value-of select="//captions/remove/@caption" />
				</button>
				<button class="tn-app-close-edit js-tn-app-close-edit">
					<i class="fa fa-close" />
				</button>
			</div>
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
