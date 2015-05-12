<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" />

	<xsl:template match="responses">
		<tr>
			<xsl:attribute name="name">child</xsl:attribute>
			<xsl:attribute name="class" select="concat('response', ../@docid)" />
			<xsl:attribute name="id" select="concat(entry/@docid, @doctype)" />
			<td> </td>
			<td> </td>
			<td colspan="5"><xsl:apply-templates mode="line"/></td>
		</tr>
	</xsl:template>

	<xsl:template match="*" mode="line">
		<xsl:if test="@docid and @doctype">
			<div class="Node document">
				<xsl:attribute name="id" select="concat(@docid, @doctype)" />
				<xsl:call-template name="graft" />
				<xsl:apply-templates select="." mode="item" />
			</div>
			<xsl:apply-templates mode="line" />
		</xsl:if>
	</xsl:template>

	<xsl:template match="entry" mode="item">
		<a class="link">
			<xsl:attribute name="href" select="@url" />
			<xsl:attribute name="title" select="@viewtext" />
			<xsl:if test="@isread = 0">
				<xsl:attribute name="class">notread</xsl:attribute>
			</xsl:if>
			<xsl:if test="@hasattach > 0">
				<img id="atach" src="/SharedResources/img/classic/attach.gif" align="baseline" alt="@">
					<xsl:attribute name='title' select="concat('Вложений в документе: ', @hasattach)" />
				</img>&#xA0;
			</xsl:if>
			<font class="font">
				<xsl:attribute name="id" select="concat('font', @docid, @doctype)" />
				<xsl:value-of disable-output-escaping="yes" select="substring(@viewtext,1,105)"/>
                <xsl:if test="string-length(@viewtext) > 105"> ... </xsl:if>
			</font>
		</a>
	</xsl:template>

	<xsl:template name="graft">
		<xsl:apply-templates select="ancestor::entry" mode="tree"/>
		<xsl:choose>
			<xsl:when test="following-sibling::*">
				<img src="/SharedResources/img/classic/tree_tee.gif" alt="|--"/>
			</xsl:when>
			<xsl:otherwise>
				<img  src="/SharedResources/img/classic/tree_corner.gif" alt="|_"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="*" mode="tree">
		<xsl:choose>
			<xsl:when test="following-sibling::* and ./entry">
				<img src="/SharedResources/img/classic/tree_bar.gif" alt="|"/>
			</xsl:when>
			<xsl:when test="./entry">
				<img src="/SharedResources/img/classic/tree_spacer.gif" alt="..."/>
			</xsl:when>
			<xsl:otherwise></xsl:otherwise>
		</xsl:choose>
	</xsl:template>
</xsl:stylesheet>