<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />
	<xsl:variable name="doctype">
		settings
	</xsl:variable>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:template match="/request">
		<xsl:variable name="currentapp" select="currentview/@app"/>
		<xsl:variable name="currentview" select="currentview"/>
		<xsl:variable name="currentservice" select="currentview/@service"/>
		<html>
			<head>
				<title>
					 <xsl:value-of select="concat('Administrator - ',currentview/@title)"/>
				</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper">
					<div id="blockWindow" style="display:none"/>
					<div id='loadingpage' style='position:absolute; display:none;'>
						<img src='/SharedResources/img/classic/4(4).gif'/>
					</div>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">
								<xsl:value-of select="query/application"/>
								Settings
							</h4>
							<div class="actionbar">
								<span class="action">
									<a href="javascript:window.location.reload()">
										<img src="img/refresh.gif"/>
										<font class="button">Refresh</font>
									</a>
								</span>
								<span id="result"/>
							</div>
							<div style="padding:1em;">
								<div style="font-size:1.1em;margin:.5em;">Application parameters</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">
											№
										</td>
										<td width="20%" class="thcell">parameter</td>
										<td class="thcell">value</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">1</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/application"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">2</td>
										<td style="padding-left:5px">mode</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/mode"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">3</td>
										<td style="padding-left:5px">description</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/description"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">4</td>
										<td style="padding-left:5px">XSLT file folder</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/xsltfilefolder"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">5</td>
										<td style="padding-left:5px">Entry point</td>
										<td style="padding-left:5px">
											<a class="doclink" href="{query/entrypoint}">
												<xsl:value-of select="query/entrypoint"/>
											</a>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">6</td>
										<td style="padding-left:5px">Outline</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/description"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">7</td>
										<td style="padding-left:5px">Mark delay sec.</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/markdelaysec"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">8</td>
										<td style="padding-left:5px">Default redirect URL</td>
										<td style="padding-left:5px">
											<a class="doclink" href="{query/entrypoint}">
												<xsl:value-of select="query/defaultredirecturl"/>
											</a>
										</td>
									</tr>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Language settings</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="10%" class="thcell">mode</td>
										<td width="10%" class="thcell">id</td>
										<td class="thcell">language</td>
									</tr>
									<xsl:for-each select="query/availablelangs/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="ison"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="id"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="name"/>
											</td>
										</tr>
									</xsl:for-each>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Skins</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="10%" class="thcell">mode</td>
										<td width="10%" class="thcell">id</td>
										<td width="20%" class="thcell">name</td>
										<td class="thcell">path</td>
									</tr>
									<xsl:for-each select="query/skins/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="ison"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="id"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="name"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="path"/>
											</td>
										</tr>
									</xsl:for-each>
								</table>
								<div style="font-size:1.1em;margin:.5em;">XSLT</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="25%" class="thcell">key</td>
										<td width="40%" class="thcell">file</td>
										<td class="thcell"></td>
									</tr>
									<xsl:for-each select="query/xslt/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="key"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="file"/>
											</td>
											<td style="padding-left:5px">
												<a target="_self">
													<xsl:attribute name="href">javascript:resetXSLT("<xsl:value-of select="/request/query/application" />")</xsl:attribute>
													<img src="img/clear.gif"/>
												</a>
											</td>
										</tr>
									</xsl:for-each>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Patches</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="15%" class="thcell">name</td>
										<td width="15%" class="thcell">description</td>
										<td width="10%" class="thcell">hash</td>
										<td width="18%" class="thcell">processed time</td>
										<td width="30%" class="thcell">viewtext</td>
									</tr>
									<xsl:for-each select="query/patches/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="name"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="description"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="hash"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="processedtime"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="viewtext"/>
											</td>
										</tr>
									</xsl:for-each>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Database</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="25%" class="thcell">parameter</td>
										<td width="20%" class="thcell">value</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											1
										</td>
										<td style="padding-left:5px"> database </td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/database/id"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											2
										</td>
										<td style="padding-left:5px"> type </td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/@type"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											3
										</td>
										<td style="padding-left:5px">
											version
										</td>
										<td style="padding-left:5px">
											<font id="currentDBversion">
												<xsl:value-of select="query/database/version"/>
											</font>
											<img style="margin-left:10px; cursor:pointer" src="img/force_small.gif">
												<xsl:attribute name="onclick">javascript:updateDBver('<xsl:value-of select="query/database/version"/>','<xsl:value-of select="query/application"/>')</xsl:attribute>
											</img>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											4
										</td>
										<td style="padding-left:5px">
											autodeploy
										</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/database/autodeploy"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											5
										</td>
										<td style="padding-left:5px">
											active connection
										</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/database/dbpool/active"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											6
										</td>
										<td style="padding-left:5px">
											idle connection
										</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/database/dbpool/idle"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											7
										</td>
										<td style="padding-left:5px">
											max active
										</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/database/dbpool/maxactive"/>
										</td>
									</tr>
									<tr class="gray-border-table">
										<td style="font-size:11px; color:gray; text-align:center">
											8
										</td>
										<td style="padding-left:5px">
											max idle
										</td>
										<td style="padding-left:5px">
											<xsl:value-of select="query/database/dbpool/maxidle"/>
										</td>
									</tr>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Query cache</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="30%" class="thcell">id</td>
										<td width="15%" class="thcell">initiator</td>
										<td width="20%" class="thcell">birth time</td>
										<td class="thcell">hits</td>
										<td class="thcell"></td>
									</tr>
									<xsl:for-each select="query/cache/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="cacheid"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="initiator"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="birthtime"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="hits"/>
											</td>
											<td style="padding-left:5px">
												<a target="_self">
													<xsl:attribute name="href">javascript:resetCache("<xsl:value-of select="cacheid"/>")</xsl:attribute>
													<img src="img/clear.gif"/>
												</a>
											</td>
										</tr>
									</xsl:for-each>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Roles</div>
								<table class="viewtable width7">
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="30%" class="thcell">Name</td>
										<td class="thcell">Description</td>
										<td width="5%" class="thcell">Is on</td>
									</tr>
									<xsl:for-each select="query/roles/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="name"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="description"/>
											</td>
											<td style="padding-left:5px; text-align:center">
												<xsl:value-of select="ison"/>
											</td>
										</tr>
									</xsl:for-each>
								</table>
								<div style="font-size:1.1em;margin:.5em;">Counters</div>
								<table class="viewtable width7">
									<xsl:variable name="dbid" select="query/@dbid"/>
									<tr class="th">
										<td width="3%" class="thcell">№</td>
										<td width="15%" class="thcell">id</td>
										<td width="25%" class="thcell">value</td>
										<td width="20%" class="thcell"/>
										<td class="thcell"/>
										<td class="thcell"/>
									</tr>
									<xsl:for-each select="query/counters/entry">
										<tr class="gray-border-table">
											<td style="font-size:11px; color:gray; text-align:center">
												<xsl:value-of select="position()"/>
											</td>
											<td style="padding-left:5px">
												<xsl:value-of select="keys"/>
											</td>
											<td style="padding-left:5px">
												<a style="text-decoration:none">
													<xsl:if test="lastnum != 0">
														<xsl:attribute name="href">javascript:changeRegNumber('<xsl:value-of select="keys"/>',<xsl:value-of select="lastnum - 1"/>,'<xsl:value-of select="$dbid"/>')</xsl:attribute>
													</xsl:if>
													-
												</a>
												<xsl:value-of select="concat(' ',lastnum,' ')"/>
												<a style="text-decoration:none">
													<xsl:attribute name="href">javascript:changeRegNumber('<xsl:value-of select="keys"/>',<xsl:value-of select="lastnum + 1"/>,'<xsl:value-of select="$dbid"/>')</xsl:attribute>
													+
												</a>
											</td>
											<td style="padding-left:5px"/>
											<td style="padding-left:5px"/>
											<td style="padding-left:5px"/>
										</tr>
									</xsl:for-each>
								</table>
							</div>
						</div>
					</div>
					<xsl:call-template name="footer"/>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>