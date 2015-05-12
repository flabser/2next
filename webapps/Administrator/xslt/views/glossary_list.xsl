<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl"/>
	<xsl:import href="../templates/action.xsl"/>
	<xsl:variable name="type">get_doc_list</xsl:variable>
	<xsl:variable name="doctype">glossary</xsl:variable>
	<xsl:variable name="currentapp" select="currentview/@app"/>
	<xsl:variable name="currentview" select="currentview"/>
	<xsl:variable name="currentservice" select="currentview/@service"/>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:template match="/request">
		<html>
			<head>
				<title>Administrator - Glossaries list</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper">
					<div id="blockWindow" style="display:none"/>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">
								<xsl:value-of select="concat(query/@dbid,'Glossaries')"/>
							</h4>
							<div class="actionbar">
								<span class="action">
									<a>
										<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
										<img src="img/refresh.gif"/>
										<font class="button">Refresh</font>
									</a>
									<a style="margin-left:10px">
										<xsl:attribute name="href">javascript:removeDocument("<xsl:value-of select="query/@dbid"/>");</xsl:attribute>
										<img src="img/delete.gif"/>
										<font class="button">Delete document</font>
									</a>
								</span>
								<xsl:call-template name="viewstat"/>
							</div>
							<table class="viewtable">
								<tr class="th">
									<td width="22px" class="thcell">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td width="20%" class="thcell">docID</td>
									<td width="10%" class="thcell">author</td>
									<td width="18%" class="thcell">regdate</td>
									<td class="thcell">viewtext</td>
									<td width="8%" class="thcell">syncstatus</td>
								</tr>
								<xsl:for-each select="query/entry">
									<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
										<xsl:variable name="num" select="position()"/>
										<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
										<td style="border:1px solid #ccc; text-align:center">
											<input type="checkbox" name="chbox" id="chbox{@docid}" class="{@doctype}" value="{@docid}"/>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<a class="doclink" title="{@docid}">
												<xsl:attribute name="href">Provider?type=edit&amp;element=glossary&amp;id=<xsl:value-of select="@docid"/>&amp;dbid=<xsl:value-of select="@dbid"/></xsl:attribute>
												<xsl:value-of select="@docid"/>
											</a>
										</td>
										<td class="title" style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@author"/>
										</td>
										<td class="title" style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@regdate"/>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="viewtext"/>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@syncstatus"/>
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