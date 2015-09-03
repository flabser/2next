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
		<section class="tn-apps">
			<div class="container">
				<xsl:apply-templates select="entry" mode="app" />
			</div>
		</section>
	</xsl:template>

	<xsl:template match="entry" mode="app">
		<div class="tn-app">
			<xsl:if test="status != 'ON_LINE'">
				<xsl:attribute name="class" select="'tn-app off'" />
			</xsl:if>
			<a class="tn-app-link" href="/{apptype}/{appid}">
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
			<button class="tn-app-description js-app-open-description">
				<i class="fa fa-info-circle" />
			</button>
			<xsl:if test="status = 'ON_LINE'">
				<button class="tn-app-edit js-app-edit">
					<i class="fa fa-gear" />
				</button>
				<div class="tn-app-settings">
					<button class="tn-app-remove js-app-remove" data-app-id="{appid}">
						<i class="fa fa-trash" />
						<xsl:value-of select="//captions/remove/@caption" />
					</button>
					<button class="tn-app-close-edit js-app-close-edit">
						<i class="fa fa-close" />
					</button>
				</div>
			</xsl:if>
		</div>
	</xsl:template>

	<xsl:template match="page/templates">
		<section class="tn-templates">
			<div class="container">
				<div class="page-header">
					<h1>
						<xsl:value-of select="//captions/templates/@caption" />
						<small>
							<xsl:value-of select="//captions/templates_description/@caption" />
						</small>
					</h1>
				</div>
				<xsl:apply-templates select="entry" mode="template" />
			</div>
		</section>
	</xsl:template>

	<xsl:template match="entry" mode="template">
		<div class="tn-tpl js-app-create" data-app-type="{.}">
			<span class="tn-tpl-logo">
				<img class="tn-tpl-logo" src="/{.}/img/logo.png" alt="logo" />
			</span>
			<span class="tn-tpl-type">
				<xsl:value-of select="." />
			</span>
			<span class="tn-tpl-description">
				<xsl:value-of select="description" />
			</span>
			<button class="tn-tpl-create">
				<i class="fa fa-plus" />
				<xsl:value-of select="//captions/create_app/@caption" />
			</button>
		</div>
	</xsl:template>

</xsl:stylesheet>
