<?xml version="1.0" ?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="glossaries" mode="html-copy-select">
		<xsl:copy-of select="select" />
	</xsl:template>

	<xsl:template match="select" mode="html-copy-select">
		<xsl:copy-of select="../select" />
	</xsl:template>

	<xsl:template match="glossaries" mode="html-render-select">
		<xsl:apply-templates select="*" mode="html-select" />
	</xsl:template>

	<xsl:template match="*" mode="html-select">
		<select name="{name()}">
			<xsl:apply-templates select="entry" mode="html-option" />
		</select>
	</xsl:template>

	<xsl:template match="entry" mode="html-option">
		<option value="{@value}">
			<xsl:value-of select="." />
		</option>
	</xsl:template>

	<xsl:template match="select" mode="html-select-to-checkbox">
		<xsl:apply-templates select="option" mode="html-select-to-checkbox">
			<xsl:with-param name="name" select="@name" />
		</xsl:apply-templates>
	</xsl:template>

	<xsl:template match="option" mode="html-select-to-checkbox">
		<xsl:param name="name" />
		<xsl:if test="@value">
			<label class="form-control">
				<input type="checkbox" id="ch-{$name}-{@value}" name="{$name}" value="{@value}" />
				<span class="input-label">
					<xsl:value-of select="." />
				</span>
			</label>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
