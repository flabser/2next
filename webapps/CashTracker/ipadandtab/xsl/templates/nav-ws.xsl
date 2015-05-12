<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="availableapps" mode="ws">
		<aside class="layout_aside nav-ws">
			<div class="ws-apps">
				<xsl:apply-templates select="query/entry" mode="ws">
					<xsl:with-param name="current_app" select="../app_name" />
				</xsl:apply-templates>
				<a href="Logout" class="ws-exit">
					<xsl:value-of select="//captions/exit/@caption" />
				</a>
				<a href="/Workspace/Logout" class="ws-logout">
					<xsl:value-of select="//captions/logout/@caption" />
				</a>
			</div>
		</aside>
	</xsl:template>

	<xsl:template match="entry" mode="ws">
		<xsl:param name="current_app" select="''" />

		<a class="ws-app" href="/{viewcontent/viewtext}/{viewcontent/viewtext1}">
			<xsl:if test="$current_app = viewcontent/viewtext">
				<xsl:attribute name="class" select="'ws-app ws-app-current'" />
			</xsl:if>
			<div class="ws-app-logo">
				<img src="/SharedResources/logos/{viewcontent/viewtext2}" />
			</div>
			<div class="ws-app-name">
				<xsl:value-of select="viewcontent/viewtext" />
				<small>
					<xsl:value-of select="viewcontent/viewtext4" />
				</small>
			</div>
			<div class="clearfix"></div>
		</a>
	</xsl:template>

</xsl:stylesheet>
