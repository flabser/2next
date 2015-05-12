<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="pagination.xsl" />

	<xsl:template name="sortingcell">
		<xsl:param name="namefield" />
		<xsl:param name="sortorder" />
		<xsl:param name="sortmode" />

		<a href="">
			<xsl:choose>
				<xsl:when test="$sortorder = 'ASC' and $sortmode = 'ON'">
					<xsl:attribute name="href">javascript:sorting('<xsl:value-of select="/request/@id" />', '<xsl:value-of
						select="$namefield" />', 'desc')</xsl:attribute>
					<img src="/SharedResources/img/iconset/br_up_green.png" style="margin-right:7px; height:12px; width:12px" />
				</xsl:when>
				<xsl:when test="$sortmode = 'OFF' or $sortorder = 'DESC'">
					<xsl:attribute name="href">javascript:sorting('<xsl:value-of select="/request/@id" />', '<xsl:value-of
						select="$namefield" />', 'asc')</xsl:attribute>
					<img src="/SharedResources/img/iconset/br_up.png" style="height:12px; width:12px; margin-right:7px" />
				</xsl:when>
				<xsl:when test="$sortmode = 'OFF'">
					<xsl:attribute name="href">javascript:sorting('<xsl:value-of select="/request/@id" />', '<xsl:value-of
						select="$namefield" />', 'asc')</xsl:attribute>
					<img src="/SharedResources/img/iconset/br_up.png" style="height:12px; width:12px; margin-right:7px" />
				</xsl:when>
			</xsl:choose>
			<span style="vertical-align:2px">
				<xsl:value-of select="page/captions/*[name() = lower-case($namefield)]/@caption" />
			</span>
			<xsl:choose>
				<xsl:when test="$sortorder = 'DESC' and $sortmode = 'ON'">
					<img src="/SharedResources/img/iconset/br_down_green.png" style="margin-left:7px; height:12px; width:12px" />
				</xsl:when>
				<xsl:when test="$sortmode = 'OFF' or $sortorder = 'ASC'">
					<img src="/SharedResources/img/iconset/br_down.png" style="margin-left:7px; height:12px; width:12px" />
				</xsl:when>
			</xsl:choose>
		</a>
	</xsl:template>

	<xsl:template match="entry" mode="attach-icon">
		<span class="attach-icon">
			<img src="/SharedResources/img/classic/icons/attach.png" title="Вложений в документе: {@hasattach}" />
		</span>
	</xsl:template>

	<xsl:template name="pageinfo">
		<div class="page-info">
			<h3 class="page-info-title">
				<small class="pull-right">
					<span class="page-info-doc-count">
						<xsl:value-of select="concat(//captions/documents/@caption, ': ', //view_content//query/@count)" />
					</span>
					<xsl:apply-templates select="//saldo/response/content/saldo" />
				</small>
				Операции
			</h3>
		</div>
	</xsl:template>

	<!-- viewCategory -->
	<xsl:template name="viewCategory">
		<xsl:choose>
			<xsl:when test="category[node()] or responses[node()]">
				<a class="expandedentry" style="margin-left:3px;margin-right:3px;cursor:point;">
					<xsl:attribute name='id' select="concat('a', ./@docid, ./@doctype)" />
					<xsl:attribute name="onclick"
						select="concat('closeResponses(', ./@docid, ',', ./@doctype, ',', position(), ',', 3, ')')" />
					<img src="/SharedResources/img/classic/minus.gif" alt="-">
						<xsl:attribute name='id' select="concat('img', ./@docid, ./@doctype)" />
						<xsl:attribute name='title' select="concat('свернуть/', @viewtext)" />
					</img>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a class="expandedentry" style="margin-left:3px;margin-right:3px;cursor:point;">
					<xsl:attribute name='id' select="concat('a', ./@docid, ./@doctype)" />
					<xsl:attribute name="onclick"
						select="concat('openParentDocView(', ./@docid, ',', ./@doctype, ',', position(), ',', 3, ')')" />
					<img src="/SharedResources/img/classic/plus.gif" alt="+">
						<xsl:attribute name='id' select="concat('img', ./@docid, ./@doctype)" />
						<xsl:attribute name='title' select="concat('развернуть/', @viewtext)" />
					</img>
				</a>
			</xsl:otherwise>
		</xsl:choose>
		<span>
			<xsl:value-of select="viewtext" />
		</span>
	</xsl:template>

	<xsl:template name="replace-string">
		<xsl:param name="text" />
		<xsl:param name="replace" />
		<xsl:param name="with" />
		<xsl:choose>
			<xsl:when test="contains($text, $replace)">
				<xsl:value-of select="substring-before($text, $replace)" />
				<xsl:copy-of select="$with" />
				<xsl:call-template name="replace-string">
					<xsl:with-param name="text" select="substring-after($text, $replace)" />
					<xsl:with-param name="replace" select="$replace" />
					<xsl:with-param name="with">
						<xsl:copy-of select="$with" />
					</xsl:with-param>
				</xsl:call-template>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$text" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
