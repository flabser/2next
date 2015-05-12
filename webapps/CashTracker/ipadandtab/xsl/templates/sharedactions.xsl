<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="action" mode="new_document">
		<xsl:param name="action_id" />
		<xsl:param name="btn_id" />
		<xsl:param name="class" />

		<button class="button button-primary" title="{@hint}">
			<xsl:attribute name="onclick">window.location.href="<xsl:value-of select="@url" />"</xsl:attribute>
			<img src="/SharedResources/img/classic/icons/page_white.png" />
			<xsl:value-of select="@caption" />
		</button>
	</xsl:template>

	<xsl:template match="action" mode="delete_document">
		<xsl:param name="action_id" />
		<xsl:param name="btn_id" />
		<xsl:param name="class" />

		<button class="button button-danger" title="{@hint}" onclick="delDocument()">
			<xsl:value-of select="@caption" />
		</button>
	</xsl:template>

	<xsl:template match="action" mode="back">
		<xsl:param name="action_id" />
		<xsl:param name="btn_id" />
		<xsl:param name="class" />

		<a class="button button-default" href="javascript:window.history.back()">
			<xsl:if test="//history/entry[@type = 'page'][not(contains(., 'search'))][last()]">
				<xsl:attribute name="href">
					<xsl:value-of select="//history/entry[@type = 'page'][not(contains(. ,'search'))][last()]" />
				</xsl:attribute>
			</xsl:if>
			<img src="/SharedResources/img/iconset/back_2.png" />
			<xsl:value-of select="@caption" />
		</a>
	</xsl:template>

	<xsl:template name="save">
		<xsl:if test="document/actionbar/action[@id = 'save_and_close']/@mode = 'ON'">
			<button type="submit" name="form_submit" form="frm" class="button button-primary">
				<img src="/SharedResources/img/classic/icons/disk.png" />
				<xsl:value-of select="document/actionbar/action[@id = 'save_and_close']/@caption" />
			</button>
		</xsl:if>
	</xsl:template>

	<xsl:template name="cancel">
		<a class="button button-default">
			<xsl:attribute name="href">
				<xsl:value-of select="//history/entry[@type = 'page'][not(contains(., 'search'))][last()]" />
			</xsl:attribute>
			<img src="/SharedResources/img/iconset/cross.png" />
			<xsl:value-of select="document/captions/close/@caption" />
		</a>
	</xsl:template>

</xsl:stylesheet>
