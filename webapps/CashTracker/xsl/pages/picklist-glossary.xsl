<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/constants.xsl" />
	<xsl:import href="../templates/pagination.xsl" />

	<xsl:output method="html" />

	<xsl:variable name="picklist_gloss_refer_url">
		<xsl:value-of select="concat('Provider?type=', //request/@type, '&amp;id=', //request/@id, '&amp;page=', query/@currentpage)" />
	</xsl:variable>
	<xsl:variable name="select-name" select="concat('chbox-', /request/@id)" />

	<xsl:template match="/request">
		<div class="container">
			<xsl:call-template name="page-navigator" />
			<ul class="dialog-list">
				<xsl:for-each select="//view_content//query/entry">
					<li>
						<xsl:apply-templates select="." mode="label" />
						<xsl:apply-templates select="responses" />
					</li>
				</xsl:for-each>
			</ul>
		</div>
	</xsl:template>

	<xsl:template match="entry" mode="label">
		<label class="dialog-list-item" ondblclick="nb.dialog.execute(this)">
			<input data-type="select" type="radio" name="{$select-name}" value="{@docid}" data-text="{viewcontent/viewtext1}" />
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
			<xsl:if test="hasresponse='true'">
				<xsl:call-template name="glossResponse" />
			</xsl:if>
		</label>
	</xsl:template>

	<xsl:template match="entry" mode="label-response">
		<input data-type="select" type="radio" name="{$select-name}" value="{@docid}" data-text="{viewtext}" />
		<input data-id="{@docid}" type="hidden" name="viewtext1" value="{viewcontent/viewtext1}" />
		<xsl:value-of select="viewtext" />
		<xsl:if test="hasresponse='true'">
			<xsl:call-template name="glossResponse" />
		</xsl:if>
	</xsl:template>

	<xsl:template name="glossResponse">
		<xsl:param name="colspan" select="0" />

		<xsl:choose>
			<xsl:when test="responses[node()]">
				<a id="a{@docid}" class="expandedentry" style="margin-left:3px;margin-right:3px;cursor:point;">
					<xsl:attribute name="href"
						select="concat($picklist_gloss_refer_url, '&amp;command=collaps`', @docid, '`', @doctype)" />
					<img src="/SharedResources/img/classic/minus.gif" alt="-" id="img{@docid}">
						<xsl:attribute name='title' select="'свернуть'" />
					</img>
				</a>
			</xsl:when>
			<xsl:otherwise>
				<a id="a{@docid}" class="expandedentry" style="margin-left:3px;margin-right:3px;cursor:point;">
					<xsl:attribute name="href"
						select="concat($picklist_gloss_refer_url, '&amp;command=expand`', @docid, '`', @doctype)" />
					<img src="/SharedResources/img/classic/plus.gif" alt="+" id="img{@docid}">
						<xsl:attribute name='title' select="'развернуть'" />
					</img>
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
			<label class="dialog-list-item" ondblclick="nb.dialog.execute(this)">
				<xsl:call-template name="graft" />
				<xsl:apply-templates select="." mode="item" />
			</label>
		</li>
		<xsl:apply-templates mode="line" />
	</xsl:template>

	<xsl:template match="entry" mode="item">
		<xsl:apply-templates select="." mode="label-response" />
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
