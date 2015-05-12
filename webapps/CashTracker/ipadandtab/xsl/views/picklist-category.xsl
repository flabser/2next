<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/util-constants.xsl" />
	<xsl:import href="../templates/pagination.xsl" />

	<xsl:output method="html" />

	<xsl:variable name="picklist_gloss_refer_url">
		<xsl:value-of
			select="concat('Provider?type=', /request/@type, '&amp;id=', /request/@id, '&amp;page=', /request/query/@currentpage)" />
	</xsl:variable>
	<xsl:variable name="select-name" select="concat('chbox-', /request/@id)" />

	<xsl:template match="/request">
		<div class="container">
			<xsl:call-template name="page-navigator" />
			<ul class="dialog-list">
				<xsl:for-each select="page/view_content/query/entry">
					<li class="js-parent">
						<xsl:apply-templates select="." mode="label" />
						<xsl:apply-templates select="responses" />
					</li>
				</xsl:for-each>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="label">
		<label class="dialog-list-item" ondblclick="nb.dialog.execute(this)">
			<xsl:if test="viewcontent/viewtext7 = '1'">
				<xsl:attribute name="ondblclick" select="'null'" />
			</xsl:if>
			<input data-type="select" type="radio" name="{$select-name}" value="{@docid}" data-text="{viewcontent/viewtext1}">
				<xsl:if test="viewcontent/viewtext7 = '1'">
					<xsl:attribute name="disabled" select="'disabled'" />
				</xsl:if>
			</input>
			<xsl:if test="hasresponse='true'">
				<xsl:call-template name="glossResponse" />
			</xsl:if>
			<span>
				<xsl:attribute name="class" select="concat('operation-type-icon-', viewcontent/viewtext3)" />
			</span>
			<input data-id="{@docid}" type="hidden" name="viewtext1" value="{viewcontent/viewtext1}" />
			<input data-id="{@docid}" type="hidden" name="viewtext2" value="{viewcontent/viewtext2}" />
			<input data-id="{@docid}" type="hidden" name="viewtext3" value="{viewcontent/viewtext3}" />
			<input data-id="{@docid}" type="hidden" name="viewtext4" value="{viewcontent/viewtext4}" />
			<input data-id="{@docid}" type="hidden" name="viewtext5" value="{viewcontent/viewtext5}" />
			<input data-id="{@docid}" type="hidden" name="viewtext6" value="{viewcontent/viewtext6}" />
			<input data-id="{@docid}" type="hidden" name="viewtext7" value="{viewcontent/viewtext7}" />
			<input data-id="{@docid}" type="hidden" name="viewnumber" value="{viewcontent/viewnumber}" />
			<input data-id="{@docid}" type="hidden" name="viewdate" value="{viewcontent/viewdate}" />
			<xsl:value-of select="viewcontent/viewtext1" />
		</label>
	</xsl:template>

	<xsl:template match="entry" mode="label-response">
		<label class="dialog-list-item" ondblclick="nb.dialog.execute(this)">
			<!-- <xsl:call-template name="graft" /> -->
			<input data-type="select" type="radio" name="{$select-name}" value="{@docid}" data-text="{viewtext}" class="js-response" />
			<xsl:if test="hasresponse='true'">
				<xsl:call-template name="glossResponse" />
			</xsl:if>
			<input data-id="{@docid}" type="hidden" name="viewtext1" value="{viewcontent/viewtext1}" />
			<xsl:value-of select="viewtext" />
		</label>
	</xsl:template>

	<xsl:template name="glossResponse">
		<xsl:param name="colspan" select="0" />

		<xsl:choose>
			<xsl:when test="responses[node()]">
				<a id="a{@docid}" class="button" style="font-weight:bold;">
					<xsl:attribute name="href"
						select="concat($picklist_gloss_refer_url, '&amp;command=collaps`', @docid, '`', @doctype)" />
					-
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a id="a{@docid}" class="button" style="font-weight:bold;">
					<xsl:attribute name="href"
						select="concat($picklist_gloss_refer_url, '&amp;command=expand`', @docid, '`', @doctype)" />
					+
				</a>
			</xsl:otherwise>
		</xsl:choose>
		<span>
			<xsl:value-of select="viewtext" />
		</span>
	</xsl:template>

	<xsl:template match="responses">
		<ul class="dialog-list dialog-list-response">
			<xsl:apply-templates mode="line" />
		</ul>
	</xsl:template>

	<xsl:template match="viewtext" mode="line" />

	<xsl:template match="entry" mode="line">
		<li>
			<xsl:apply-templates select="." mode="label-response" />
		</li>
		<xsl:apply-templates mode="line" />
	</xsl:template>

	<xsl:template name="graft">
		<xsl:apply-templates select="ancestor::entry" mode="tree" />
		<xsl:choose>
			<xsl:when test="following-sibling::entry">
				<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_tee.gif" />
			</xsl:when>
			<xsl:otherwise>
				<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_corner.gif" />
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="responses" mode="tree" />

	<xsl:template match="*" mode="tree">
		<xsl:choose>
			<xsl:when test="following-sibling::entry and entry[@url]">
				<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_bar.gif" />
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="parent::responses">
					<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_spacer.gif" />
				</xsl:if>
				<xsl:if test="parent::entry">
					<img style="vertical-align:top;" src="/SharedResources/img/classic/tree_spacer.gif" />
				</xsl:if>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

</xsl:stylesheet>
