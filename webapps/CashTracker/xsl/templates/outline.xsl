<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:variable name="outline-current-entry" select="//app_menu//current/entry" />

	<xsl:template match="app_menu" mode="outline">
		<xsl:param name="active-entry-id" />

		<aside class="layout_aside nav-app">
			<div class="side">
				<div class="side-container">
					<xsl:apply-templates select="response/content/outline" mode="outline">
						<xsl:with-param name="active-entry-id" select="$active-entry-id" />
					</xsl:apply-templates>
				</div>
			</div>
		</aside>
	</xsl:template>

	<xsl:template match="outline" mode="outline">
		<xsl:param name="active-entry-id" select="''" />

		<div class="side-nav">
			<div class="side-header" data-role="side-tree-toggle">
				<span class="side-tree-toggle"></span>
				<span title="{@hint}">
					<xsl:value-of select="@caption" />
				</span>
			</div>
			<ul class="side-tree" id="side-tree-{@id}">
				<xsl:apply-templates mode="outline">
					<xsl:with-param name="active-entry-id" select="$active-entry-id" />
				</xsl:apply-templates>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="outline">
		<xsl:param name="active-entry-id" select="''" />

		<li class="side-tree-item">
			<a href="{@url}" title="{@hint}">
				<xsl:if
					test="$active-entry-id != '' and (@id = $active-entry-id or @id = $outline-current-entry or @id = $outline-current-entry/@id)">
					<xsl:attribute name="class">side-tree-item-active</xsl:attribute>
				</xsl:if>
				<xsl:choose>
					<xsl:when test="@id = 'users'">
						<i class="fa fa-users"></i>
					</xsl:when>
					<xsl:otherwise>
						<i class="fa fa-file-o"></i>
					</xsl:otherwise>
				</xsl:choose>
				<xsl:value-of select="@caption" />
			</a>
			<xsl:if test="./entry">
				<span class="side-tree-toggle" data-role="side-tree-toggle"></span>
				<ul class="side-tree" id="side-tree-{@id}{position()}">
					<xsl:apply-templates mode="outline">
						<xsl:with-param name="active-entry-id" select="$active-entry-id" />
					</xsl:apply-templates>
				</ul>
			</xsl:if>
		</li>
	</xsl:template>

</xsl:stylesheet>
