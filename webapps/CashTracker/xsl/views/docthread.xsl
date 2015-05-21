<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:template match="request/history" />

	<xsl:template match="responses">
		<tr class="response{../@docid}" id="{../@docid}{@doctype}">
			<td />
			<td data-role="docthread">
				<xsl:apply-templates mode="line" />
			</td>
			<script>
			$("td[data-role=docthread]").each(function(){
				$(this).attr("colspan", $(this).parent("tr").prev("tr").children("td").length - 1);
				$(this).removeAttr("data-role");
			});
		</script>
		</tr>
	</xsl:template>

	<xsl:template match="viewtext" mode="line" />

	<xsl:template match="entry" mode="line">
		<div class="document" id="{@docid}{@doctype}">
			<xsl:call-template name="graft" />
			<xsl:apply-templates select="." mode="item" />
		</div>
		<xsl:apply-templates mode="line" />
	</xsl:template>

	<xsl:template match="entry" mode="item">
		<a href="{@url}" title="{@viewtext}" class="doclink" style="display:inline;margin-left:4px;padding:4px;">
			<xsl:if test="@hasattach != 0">
				<img id="atach" src="/SharedResources/img/classic/icons/attach.png" border="0" title="Вложений в документе: {@hasattach}" />
			</xsl:if>
			<xsl:value-of select="." />
		</a>
	</xsl:template>

	<xsl:template name="graft">
		<xsl:apply-templates select="ancestor::entry" mode="tree" />
		<xsl:choose>
			<xsl:when test="following-sibling::entry">
				<img style="vertical-align:top" src="/SharedResources/img/classic/tree_tee.gif" />
			</xsl:when>
			<xsl:otherwise>
				<img style="vertical-align:top" src="/SharedResources/img/classic/tree_corner.gif" />
			</xsl:otherwise>
		</xsl:choose>
		<span style="width:15px;">
			<input type="checkbox" name="docid" id="{@docid}" value="{@doctype}" />
		</span>
	</xsl:template>

	<xsl:template match="*" mode="tree">
		<xsl:choose>
			<xsl:when test="following-sibling::entry and entry[@url]">
				<img style="vertical-align:top" src="/SharedResources/img/classic/tree_bar.gif" />
			</xsl:when>
			<xsl:otherwise>
				<img style="vertical-align:top" src="/SharedResources/img/classic/tree_spacer.gif" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>