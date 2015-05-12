<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />
	<xsl:variable name="doctype">glossary</xsl:variable>
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:template match="/request">
		<html>
			<head>
				<title>Administrator - Document activity</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper">
					<div id="blockWindow" style="display:none"/>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">
								<xsl:value-of select="concat(/request/@app,' ', query/@ruleid ,' Glossary')"/>
							</h4>
							<div class="actionbar">
								<span class="action">
									<xsl:call-template name="refresh_action"/>
								</span>
							</div>
							<table class="viewtable">
								<tr class="th">
									<td width="22px" class="thcell">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td width="5%" class="thcell"></td>
									<td width="20%" class="thcell">id</td>
									<td class="thcell">description</td>
								</tr>
								<xsl:for-each select="view/entry">
									<tr>
										<xsl:variable name="num" select="position()"/>
										<xsl:if test="$num mod 2 = 0">
											<xsl:attribute name="bgcolor">#efefef</xsl:attribute>
										</xsl:if>
										<td>
											<input type="checkbox" name="chbox" id="{@docid}">
												<xsl:attribute name="name" select="/request/view/entry/author"/>
											</input>
										</td>
										<td style="font-size:0.8em;color:gray">
											<xsl:value-of select="$num"/>
										</td>
										<td>
											<a title="{filename}">
												<xsl:attribute name="href">Provider?type=get_query&amp;id=<xsl:value-of select="@id"/>&amp;app=<xsl:value-of select="app"/></xsl:attribute>
												<xsl:value-of select="@id"/>
											</a>
										</td>
										<td class="title" style="font-size:0.8em">
											<xsl:value-of select="description"/>
										</td>
									</tr>
								</xsl:for-each>
							</table>
						</div>
					</div>
					<xsl:call-template name="footer"/>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>