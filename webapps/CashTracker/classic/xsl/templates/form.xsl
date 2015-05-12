<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template match="text()|@*">
		<xsl:value-of select="." />
	</xsl:template>

	<xsl:template name="field">
		<xsl:param name="name" />
		<xsl:param name="node" />

		<xsl:choose>
			<xsl:when test="not($node)">
				<div class="span7" id="{$name}tbl"></div>
				<input type="hidden" name="{$name}" value="" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates mode="list" select="$node" />
				<xsl:apply-templates mode="list-input" select="$node" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="*" mode="list">
		<xsl:choose>
			<xsl:when test="entry">
				<ul class="list list-table span7" id="{name()}tbl">
					<xsl:apply-templates select="entry" />
				</ul>
			</xsl:when>
			<xsl:otherwise>
				<div class="span7" id="{name()}tbl">
					<xsl:apply-templates />
				</div>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="entry">
		<li>
			<xsl:apply-templates />
		</li>
	</xsl:template>

	<xsl:template match="*" mode="list-input">
		<xsl:choose>
			<xsl:when test="entry">
				<xsl:apply-templates select="entry" mode="input-entry" />
			</xsl:when>
			<xsl:when test="@attrval">
				<xsl:apply-templates select="." mode="input-attr" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:apply-templates select="." mode="input" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="*" mode="input-entry">
		<input type="hidden" name="{../name()}" value="{./@attrval}" />
	</xsl:template>

	<xsl:template match="*" mode="input-attr">
		<input type="hidden" name="{./name()}" value="{./@attrval}" />
	</xsl:template>

	<xsl:template match="*" mode="input">
		<input type="hidden" name="{./name()}" value="{.}" />
	</xsl:template>

	<!-- static-glossaries-name -->
	<xsl:template match="skey" mode="static-glossaries-name">
		<xsl:value-of select="../name" />
	</xsl:template>

	<!-- static-glossaries-input -->
	<xsl:template match="entry" mode="static-glossaries-input">
		<xsl:param name="type" />
		<xsl:param name="value" />
		<xsl:param name="required" />
		<xsl:param name="display-mode" /> <!-- inline, block -->

		<label class="form-control">
			<input type="{$type}" name="{../name()}" value="{skey}">
				<xsl:if test="position() = 1 and $required = 'required'">
					<xsl:attribute name="required">required</xsl:attribute>
				</xsl:if>
				<xsl:if test="skey = $value or $value/entry = skey">
					<xsl:attribute name="checked">checked</xsl:attribute>
				</xsl:if>
			</input>
			<span class="input-label">
				<xsl:value-of select="name" />
			</span>
		</label>
	</xsl:template>

	<!-- static-glossaries-select -->
	<xsl:template match="*" mode="static-glossaries-select">
		<xsl:param name="value" />
		<xsl:param name="required" />

		<select name="{name()}">
			<xsl:if test="$required = 'required'">
				<xsl:attribute name="required">required</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates select="entry" mode="static-glossaries-option">
				<xsl:with-param name="value" select="$value" />
			</xsl:apply-templates>
		</select>
	</xsl:template>

	<!-- static-glossaries-option -->
	<xsl:template match="entry" mode="static-glossaries-option">
		<xsl:param name="value" />

		<option>
			<xsl:if test="skey = $value">
				<xsl:attribute name="selected">selected</xsl:attribute>
			</xsl:if>
			<xsl:attribute name="value" select="skey" />
			<xsl:value-of select="name" />
		</option>
	</xsl:template>

	<!-- glossaries-select -->
	<xsl:template match="*" mode="glossaries-select">
		<xsl:param name="value" />
		<xsl:param name="required" />
		<xsl:param name="keyAttr" />

		<select name="{name()}">
			<xsl:if test="$required = 'required'">
				<xsl:attribute name="required">required</xsl:attribute>
			</xsl:if>
			<xsl:apply-templates select="entry" mode="glossaries-option">
				<xsl:with-param name="value" select="$value" />
				<xsl:with-param name="keyAttr" select="$keyAttr" />
			</xsl:apply-templates>
		</select>
	</xsl:template>

	<!-- glossaries-option -->
	<xsl:template match="entry" mode="glossaries-option">
		<xsl:param name="value" />
		<xsl:param name="keyAttr" />

		<option>
			<xsl:if test="@id = $value or @docid = $value">
				<xsl:attribute name="selected">selected</xsl:attribute>
			</xsl:if>
			<xsl:choose>
				<xsl:when test="$keyAttr = 'docid'">
					<xsl:attribute name="value" select="@docid" />
				</xsl:when>
				<xsl:otherwise>
					<xsl:attribute name="value" select="@id" />
				</xsl:otherwise>
			</xsl:choose>
			<xsl:value-of select="@viewtext" />
		</option>
	</xsl:template>

</xsl:stylesheet>
