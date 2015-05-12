<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<!-- page-navigator -->
	<!-- Medet. Стилизация описана в page-navigator.css, не пишите стилизацию в attr (style) для этого есть css -->

	<xsl:variable name="PAGE_ARROW_FIRST">
		<xsl:text>&lt;&lt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="PAGE_ARROW_PREV">
		<xsl:text>&lt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="PAGE_ARROW_NEXT">
		<xsl:text>&gt;</xsl:text>
	</xsl:variable>
	<xsl:variable name="PAGE_ARROW_LAST">
		<xsl:text>&gt;&gt;</xsl:text>
	</xsl:variable>

	<xsl:variable name="select_always" select="true()" />
	<xsl:variable name="page_id" select="//request/@id" />
	<xsl:variable name="current_page" select="//view_content//query/@currentpage" />
	<xsl:variable name="pagination_maxpage" select="//view_content//query/@maxpage" />
	<xsl:variable name="pagination_count" select="5" />
	<xsl:variable name="pagination_center" select="2" />
	<xsl:variable name="c_o_e_id" select="//current_outline_entry/response/content/entry/@entryid" />
	<xsl:variable name="c_o_e_title" select="//current_outline_entry/response/content/entry" />
	<xsl:variable name="c_o_e_custom_param" select="//current_outline_entry/response/content/customparam" />

	<xsl:variable name="refer_url">
		<xsl:value-of
			select="concat('Provider?type=', //request/@type, '&amp;id=', $page_id, '&amp;entryid=', $c_o_e_id, '&amp;title=', $c_o_e_title, $c_o_e_custom_param, '&amp;page=')" />
	</xsl:variable>

	<xsl:template name="page-navigator">
		<xsl:if test="$pagination_maxpage > 1">
			<div class="pagination">
				<xsl:variable name="curpage" select="$current_page" />
				<xsl:variable name="prevpage" select="$curpage - 1" />
				<xsl:variable name="beforecurview" select="substring-before(@id, '.')" />
				<xsl:variable name="aftercurview" select="substring-after(@id, '.')" />

				<xsl:if test="$current_page > 1">
					<xsl:call-template name="page-nav-link">
						<xsl:with-param name="_class" select="'page-first'" />
						<xsl:with-param name="_icon" select="$PAGE_ARROW_FIRST" />
					</xsl:call-template>
					<xsl:call-template name="page-nav-link">
						<xsl:with-param name="_page" select="$prevpage" />
						<xsl:with-param name="_class" select="'page-prev'" />
						<xsl:with-param name="_icon" select="$PAGE_ARROW_PREV" />
					</xsl:call-template>
				</xsl:if>
				<xsl:call-template name="pagenavig" />
				<xsl:if test="$select_always or $pagination_maxpage > $pagination_count">
					<xsl:variable name="beforecurview" select="substring-before(@id, '.')" />
					<xsl:variable name="aftercurview" select="substring-after(@id, '.')" />
					<span class="pagination-select">
						<select>
							<xsl:attribute name="onchange">
								<xsl:value-of select="concat('window.location.href = &quot;', $refer_url, '&quot; + this.value')" />
							</xsl:attribute>
							<xsl:call-template name="combobox" />
						</select>
					</span>
				</xsl:if>
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template name="page-nav-link">
		<xsl:param name="_page" select="1" />
		<xsl:param name="_class" select="''" />
		<xsl:param name="_icon" select="''" />

		<a class="{$_class}" href="{$refer_url}{$_page}">
			<xsl:if test="$_page = $current_page">
				<xsl:attribute name="class">
					<xsl:value-of select="concat($_class, ' page-active')" />
				</xsl:attribute>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="$_icon != ''">
					<xsl:value-of select="$_icon" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:value-of select="$_page" />
				</xsl:otherwise>
			</xsl:choose>
		</a>
	</xsl:template>

	<xsl:template name="pagenavig">
		<xsl:param name="i" select="1" />  <!-- счетчик количества страниц отображаемых в навигаторе -->
		<xsl:param name="n" select="$pagination_count" /> <!-- количество страниц отображаемых в навигаторе -->
		<xsl:param name="z" select="$pagination_maxpage - ($pagination_count - 1)" />
		<xsl:param name="f" select="$pagination_count" />
		<xsl:param name="c" select="$current_page" /> <!-- текущая страница в виде -->
		<xsl:param name="startnum" select="$current_page - $pagination_center" />
		<xsl:param name="d" select="$pagination_maxpage - ($pagination_count - 1)" />	<!-- переменная для вычисления начального номера страницы в навигаторе -->
		<xsl:param name="maxpage" select="$pagination_maxpage" />
		<xsl:param name="nextpage" select="$current_page + 1" />
		<xsl:param name="prevpage" select="$current_page - 1" />

		<xsl:choose>
			<xsl:when test="$maxpage > $pagination_count">
				<xsl:choose>
					<xsl:when test="$maxpage - $current_page &lt; $pagination_center">
						<xsl:if test="$i != $n + 1">
							<xsl:if test="$z &lt; $maxpage + 1">
								<xsl:call-template name="page-nav-link">
									<xsl:with-param name="_page" select="$z" />
								</xsl:call-template>
							</xsl:if>
							<xsl:call-template name="pagenavig">
								<xsl:with-param name="i" select="$i + 1" />
								<xsl:with-param name="n" select="$n" />
								<xsl:with-param name="z" select="$z + 1" />
							</xsl:call-template>
						</xsl:if>
						<xsl:if test="$current_page != $maxpage">
							<xsl:if test="$i = $n + 1">
								<xsl:call-template name="page-nav-link">
									<xsl:with-param name="_page" select="$nextpage" />
									<xsl:with-param name="_class" select="'page-next'" />
									<xsl:with-param name="_icon" select="$PAGE_ARROW_NEXT" />
								</xsl:call-template>
								<xsl:call-template name="page-nav-link">
									<xsl:with-param name="_page" select="$maxpage" />
									<xsl:with-param name="_class" select="'page-last'" />
									<xsl:with-param name="_icon" select="$PAGE_ARROW_LAST" />
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="$current_page &lt; $pagination_center">
								<xsl:if test="$i = 1">
									<xsl:if test="$current_page = 1">
										<!-- nothing todo -->
									</xsl:if>
								</xsl:if>
								<xsl:if test="$i != $n + 1">
									<xsl:if test="$i &lt; $maxpage + 1">
										<xsl:call-template name="page-nav-link">
											<xsl:with-param name="_page" select="$i" />
										</xsl:call-template>
									</xsl:if>
									<xsl:call-template name="pagenavig">
										<xsl:with-param name="i" select="$i + 1" />
										<xsl:with-param name="n" select="$n" />
										<xsl:with-param name="c" select="$c + 1" />
									</xsl:call-template>
								</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="$i != $n + 1">
									<xsl:if test="$i &lt; $maxpage + 1">
										<xsl:if test="$startnum != 0">
											<xsl:call-template name="page-nav-link">
												<xsl:with-param name="_page" select="$startnum" />
											</xsl:call-template>
										</xsl:if>
									</xsl:if>
									<xsl:if test="$startnum != 0">
										<xsl:call-template name="pagenavig">
											<xsl:with-param name="i" select="$i + 1" />
											<xsl:with-param name="n" select="$n" />
											<xsl:with-param name="c" select="$c + 1" />
											<xsl:with-param name="startnum" select="$c - ($pagination_center - 1)" />
										</xsl:call-template>
									</xsl:if>
									<xsl:if test="$startnum = 0">
										<xsl:call-template name="pagenavig">
											<xsl:with-param name="i" select="$i" />
											<xsl:with-param name="n" select="$n" />
											<xsl:with-param name="c" select="$c + 1" />
											<xsl:with-param name="startnum" select="$c - ($pagination_center - 1)" />
										</xsl:call-template>
									</xsl:if>
								</xsl:if>
							</xsl:otherwise>
						</xsl:choose>
						<xsl:if test="$current_page != $maxpage">
							<xsl:if test="$i = $n + 1">
								<xsl:call-template name="page-nav-link">
									<xsl:with-param name="_page" select="$nextpage" />
									<xsl:with-param name="_class" select="'page-next'" />
									<xsl:with-param name="_icon" select="$PAGE_ARROW_NEXT" />
								</xsl:call-template>
								<xsl:call-template name="page-nav-link">
									<xsl:with-param name="_page" select="$maxpage" />
									<xsl:with-param name="_class" select="'page-last'" />
									<xsl:with-param name="_icon" select="$PAGE_ARROW_LAST" />
								</xsl:call-template>
							</xsl:if>
						</xsl:if>
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$i = 1">
					<xsl:if test="$current_page = 1">
						<!-- nothing todo -->
					</xsl:if>
				</xsl:if>
				<xsl:if test="$i != $n + 1">
					<xsl:if test="$i &lt; $maxpage + 1">
						<xsl:call-template name="page-nav-link">
							<xsl:with-param name="_page" select="$i" />
						</xsl:call-template>
					</xsl:if>
					<xsl:call-template name="pagenavig">
						<xsl:with-param name="i" select="$i + 1" />
						<xsl:with-param name="n" select="$n" />
						<xsl:with-param name="c" select="$c + 1" />
					</xsl:call-template>
				</xsl:if>
				<xsl:if test="$current_page != $maxpage">
					<xsl:if test="$i = $n + 1">
						<xsl:call-template name="page-nav-link">
							<xsl:with-param name="_page" select="$nextpage" />
							<xsl:with-param name="_class" select="'page-next'" />
							<xsl:with-param name="_icon" select="$PAGE_ARROW_NEXT" />
						</xsl:call-template>
						<xsl:call-template name="page-nav-link">
							<xsl:with-param name="_page" select="$maxpage" />
							<xsl:with-param name="_class" select="'page-last'" />
							<xsl:with-param name="_icon" select="$PAGE_ARROW_LAST" />
						</xsl:call-template>
					</xsl:if>
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template name="combobox">
		<xsl:param name="i" select="1" />
		<xsl:param name="k" select="$current_page" />
		<xsl:param name="n" select="$pagination_maxpage + 1" />
		<xsl:choose>
			<xsl:when test="$n > $i">
				<option>
					<xsl:attribute name="value" select="$i" />
					<xsl:if test="$k = $i">
						<xsl:attribute name="selected">true</xsl:attribute>
					</xsl:if>
					<xsl:value-of select="$i" />
				</option>
				<xsl:call-template name="combobox">
					<xsl:with-param name="i" select="$i + 1" />
					<xsl:with-param name="n" select="$n" />
					<xsl:with-param name="k" select="$current_page" />
				</xsl:call-template>
			</xsl:when>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
