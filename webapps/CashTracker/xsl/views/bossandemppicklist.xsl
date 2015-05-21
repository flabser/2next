<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="checkbox-name" select="//query/@ruleid" />

	<xsl:template match="/request">
		<xsl:apply-templates select="query/entry" mode="org" />
	</xsl:template>

	<xsl:template match="entry" mode="org">
		<xsl:if test="responses/entry">
			<div class="tree-org highlight-selected">
				<h5 class="doctype-{../@doctype}" style="border-bottom:1px solid #D9D9D9;margin:5px 0 2px;padding:2px;">
					<xsl:value-of select="viewtext" />
				</h5>
				<xsl:apply-templates select="responses" mode="response" />
			</div>
		</xsl:if>
	</xsl:template>

	<xsl:template match="responses" mode="response">
		<div class="tree-response" style="margin-left:12px;">
			<xsl:apply-templates mode="entry" />
		</div>
	</xsl:template>

	<xsl:template match="*" mode="entry">
		<div class="tree-response">
			<xsl:choose>
				<xsl:when test="userid != ''">
					<div class="tree-entry doctype-{@doctype}">
						<label class="dialog-list-item" ondblclick="nb.dialog.execute(this)">
							<input type="checkbox" name="{$checkbox-name}" value="{userid}" data-type="select" data-text="{@viewtext}" />
							<span class="input-label">
								<xsl:value-of select="@viewtext" />
							</span>
							<span style="display:none;">
								<xsl:value-of select="userid" />
							</span>
						</label>
					</div>
				</xsl:when>
				<xsl:otherwise>
					<h5 class="doctype-{../@doctype}" style="border-bottom:1px solid #D9D9D9;margin:8px 0 5px;padding:3px;">
						<xsl:value-of select="viewtext" />
					</h5>
				</xsl:otherwise>
			</xsl:choose>
		</div>
		<xsl:if test="./entry">
			<div class="tree-response" style="margin-left:12px;padding:2px;">
				<xsl:apply-templates select="entry" mode="entry" />
			</div>
		</xsl:if>
	</xsl:template>

</xsl:stylesheet>
