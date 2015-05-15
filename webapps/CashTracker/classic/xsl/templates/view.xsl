<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="pagination.xsl" />

	<xsl:template name="sortingcell">
		<xsl:param name="namefield" />
		<xsl:param name="sortorder" />
		<xsl:param name="sortmode" />

		<a href="" class="sort">
			<xsl:choose>
				<xsl:when test="$sortorder = 'ASC' and $sortmode = 'ON'">
					<xsl:attribute name="href">javascript:nbApp.viewSortColumn('<xsl:value-of select="/request/@id" />', '<xsl:value-of
						select="$namefield" />', 'desc')</xsl:attribute>
					<i class="fa fa-chevron-up" style="color:green;"></i>
				</xsl:when>
				<xsl:when test="$sortmode = 'OFF' or $sortorder = 'DESC'">
					<xsl:attribute name="href">javascript:nbApp.viewSortColumn('<xsl:value-of select="/request/@id" />', '<xsl:value-of
						select="$namefield" />', 'asc')</xsl:attribute>
					<i class="fa fa-chevron-up" style="color:black;"></i>
				</xsl:when>
				<xsl:when test="$sortmode = 'OFF'">
					<xsl:attribute name="href">javascript:nbApp.viewSortColumn('<xsl:value-of select="/request/@id" />', '<xsl:value-of
						select="$namefield" />', 'asc')</xsl:attribute>
					<i class="fa fa-chevron-up" style="color:black;"></i>
				</xsl:when>
			</xsl:choose>
			<span style="margin:0 2px">
				<xsl:value-of select="//page/captions/*[name() = lower-case($namefield)]/@caption" />
			</span>
			<xsl:choose>
				<xsl:when test="$sortorder = 'DESC' and $sortmode = 'ON'">
					<i class="fa fa-chevron-down" style="color:green;"></i>
				</xsl:when>
				<xsl:when test="$sortmode = 'OFF' or $sortorder = 'ASC'">
					<i class="fa fa-chevron-down" style="color:black;"></i>
				</xsl:when>
			</xsl:choose>
		</a>
	</xsl:template>

	<xsl:template name="page-info">
		<div class="page-info">
			<header>
				<div class="pull-right">
					<xsl:apply-templates select="//saldo/response/content/saldo" />
				</div>

				<xsl:variable name="co_entry" select="//app_menu//current/entry" />
				<xsl:variable name="ro_entry" select="//response/content/outline/entry" />
				<xsl:if test="$co_entry != $ro_entry[@id = $co_entry/@id]/@caption">
					<xsl:value-of select="$ro_entry[@id = $co_entry/@id]/@caption" />
				</xsl:if>
				<xsl:if test="$co_entry = $ro_entry[@id = $co_entry/@id]/@caption or not($ro_entry[@id = $co_entry/@id]/@caption)">
					<xsl:value-of select="$co_entry" />
				</xsl:if>
				<xsl:if test="//view_content//query/@count">
					<sup class="entry-count">
						<small>
							<xsl:value-of select="concat('(', //view_content//query/@count, ')')" />
						</small>
					</sup>
				</xsl:if>
			</header>
			<nav>
				<div class="pull-right">
					<xsl:apply-templates select="//view_content" mode="page-navigator" />
				</div>
				<div class="on-desktop">
					<xsl:apply-templates select="//actionbar" />
				</div>
			</nav>
		</div>
	</xsl:template>

	<!-- attach-icon -->
	<xsl:template match="entry" mode="attach-icon">
		<i class="fa fa-paperclip" title="Вложений в документе: {@hasattach}" />
	</xsl:template>

	<!-- viewCategory -->
	<xsl:template name="viewCategory">
		<xsl:param name="colspan" />

		<xsl:choose>
			<xsl:when test="category[node()] or responses[node()]">
				<a id="a{@docid}" class="expandedentry" style="display:inline;margin-right:4px;">
					<xsl:attribute name="href"
						select="concat('javascript:nbApp.viewThreadCollapse(', @docid, ',', @doctype, ',', position(), ',', $colspan, ')')" />
					<img src="/SharedResources/img/classic/1/minus1.png" alt="-" id="img{@docid}">
						<xsl:attribute name='title' select="concat('свернуть/', viewcontent/viewtext)" />
					</img>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a id="a{@docid}" class="expandedentry" style="display:inline;margin-right:4px;">
					<xsl:attribute name="href"
						select="concat('javascript:nbApp.viewThreadExpand(', @docid, ',', @doctype, ',', position(), ',', $colspan, ')')" />
					<img src="/SharedResources/img/classic/1/plus1.png" alt="+" id="img{@docid}">
						<xsl:attribute name='title' select="concat('развернуть/', viewcontent/viewtext)" />
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
