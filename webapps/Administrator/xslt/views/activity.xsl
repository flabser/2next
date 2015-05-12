<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:variable name="type">activity</xsl:variable>
	<xsl:variable name="doctype">activity</xsl:variable>
	<xsl:variable name="viewtype">activity</xsl:variable>
	<xsl:variable name="currentapp" select="currentview/@app"/>
	<xsl:variable name="currentview" select="currentview"/>
	<xsl:variable name="currentservice" select="currentview/@service"/>

	<xsl:template match="/request[@element != 'activity']">
		<html>
			<head>
				<title>Administrator - Document activity</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper;">
					<div id="blockWindow" style="display:none"/>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">
								<font style="font-size:1.1em;">Activity</font>
								&#xA0;&#xA0;Doctype&#xA0;
								<font style="font-family:verdana; font-size:20px;">
									<xsl:value-of select="document/@doctype | query/@doctype"/>
								</font>
								&#xA0;docID&#xA0;
								<font style="font-family:verdana; font-size:20px;">
									<xsl:value-of select="document/@docid | query/@docid"/>
								</font>
							</h4>
							<div class="actionbar">
								<table width="100%">
									<tr>
										<td width="50%">
											<xsl:call-template name="showasxml_action"/>
										</td>
										<td style="text-align:right">
											<xsl:call-template name="close_action"/>
										</td>
									</tr>
								</table>
							</div>
							<br />
							<table class="viewtable">
								<tr>
									<td width="22px" class="th" style="text-align:center">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td width="3%" class="th"/>
									<td width="25%" class="th">
										Time
									</td>
									<td width="30%" class="th">
										Type
									</td>
									<td class="th">
										User
									</td>
								</tr>
								<xsl:for-each select="query/entry">
									<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
										<td style="border:1px solid #ccc; text-align:center">
											<input type="checkbox" name="chbox" id="chbox{@docid}" value="{@docid}"/>
										</td>
										<td style="border:1px solid #ccc; padding-left:5px">
											<xsl:if test="./changes/entry">
												<img src="SharedResources/img/classic/plus.gif">
													<xsl:attribute name="onclick">javascript:openActivityProperties(this)</xsl:attribute>
													<xsl:attribute name="style">cursor:pointer</xsl:attribute>
												</img>
											</xsl:if>
										</td>
										<td style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@eventtime"/>
										</td>
										<td style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@type"/>
										</td>
										<td style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@userid"/>
										</td>
									</tr>
									<xsl:if test="changes/entry">
										<tr style="display:none; border-bottom:1px solid #ccc">
											<td/>
											<td colspan="6">
												<table width="100%" style="font-size:12px; background:#F4F4F4;">
													<tr style="font-weight:bold; border-bottom:1px solid #ccc; text-align:center">
														<td width="20%">Field name</td>
														<td width="40%">Old value</td>
														<td width="40%">New value</td>
													</tr>
													<xsl:for-each select="changes/entry">
														<tr>
															<xsl:variable name="num" select="position()"/>
															<xsl:if test="$num mod 2 = 0">
																<xsl:attribute name="bgcolor">#e5e5e5</xsl:attribute>
															</xsl:if>
															<td>
																<xsl:value-of select="@fieldname"/>
															</td>
															<td>
																<xsl:value-of select="oldvalue"/>
															</td>
															<td>
																<xsl:value-of select="newvalue"/>
															</td>
														</tr>
													</xsl:for-each>
												</table>
											</td>
										</tr>
									</xsl:if>
								</xsl:for-each>
							</table>
						</div>
					</div>
					<xsl:call-template name="footer" />
				</div>
			</body>
		</html>
	</xsl:template>

	<!-- Activity для системы -->
	<xsl:template match="/request[@element = 'activity']">
		<xsl:variable name="dbid" select="@dbid" />
		<html>
			<head>
				<title>System activity</title>
				<xsl:call-template name="view-html-head-jscss" />
			</head>
			<body>
				<div class="wrapper">
					<div id="blockWindow" style="display:none" />
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">System activity</h4>
							<div class="actionbar">
								<span class="action">
									<xsl:call-template name="refresh_action"/>

									<xsl:call-template name="viewstat"/>
								</span>
							</div>
							<table class="viewtable">
								<tr>
									<td width="22px" class="th">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td width="3%" class="th">
									</td>
									<td width="25%" class="th">
										Time
									</td>
									<td width="20%" class="th">
										Type
									</td>
									<td width="25%" class="th">
										User
									</td>
									<td class="th">
										DocID
									</td>
									<td class="th">
										DocType
									</td>
								</tr>
								<xsl:for-each select="query/entry">
									<tr onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
										<td style="border:1px solid #ccc; text-align:center">
											<input type="checkbox" name="chbox" value="{@docid}">
												<xsl:attribute name="id" select="concat('chbox',@docid)"/>
											</input>
										</td>
										<td style="border: 1px solid #ccc; text-align:center">
											<xsl:if test="./changes/entry">
												<img src="/SharedResources/img/classic/plus.gif">
													<xsl:attribute name="onclick">javascript:openActivityProperties(this)</xsl:attribute>
													<xsl:attribute name="style">cursor:pointer</xsl:attribute>
												</img>
											</xsl:if>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@eventtime"/>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@type"/>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@userid"/>
											<xsl:if test="@ip != 'null' and string-length(normalize-space(@ip)) != 0">
												<b><xsl:value-of select="concat(' ( ',@ip,')')"/></b>
											</xsl:if>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:variable name='linkdoctype'>
												<xsl:choose>
													<xsl:when test="@doctype = 896">
														<xsl:value-of select="'get_maindoc'"/>
													</xsl:when>
													<xsl:when test="@doctype = 897">
														<xsl:value-of select="'get_task'"/>
													</xsl:when>
													<xsl:when test="@doctype = 898">
														<xsl:value-of select="'get_execution'"/>
													</xsl:when>
													<xsl:when test="@doctype = 899">
														<xsl:value-of select="'get_project'"/>
													</xsl:when>
												</xsl:choose>
											</xsl:variable>
											<a href='' class="doclink">
												<xsl:attribute name='href'>Provider?type=<xsl:value-of select="$linkdoctype" />&amp;docid=<xsl:value-of select="@docid"/>&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
												<xsl:value-of select="@docid"/>
											</a>
										</td>
										<td style="border: 1px solid #ccc; padding-left:5px">
											<xsl:value-of select="@doctype"/>
										</td>
									</tr>
									<xsl:if test="changes/entry">
										<tr style=" display:none; border-bottom:1px solid #ccc">
											<td></td>
											<td colspan="6">
												<table width="100%" style="font-size:12px; background:#F4F4F4;">
													<tr style="font-weight:bold; border-bottom:1px solid #ccc; text-align:center">
														<td width="20%">Field name</td>
														<td width="40%">Old value</td>
														<td width="40%">New value</td>
													</tr>
													<xsl:for-each select="changes/entry">
														<tr>
															<xsl:variable name="num" select="position()"/>
															<xsl:if test="$num mod 2 = 0">
																<xsl:attribute name="bgcolor">#e5e5e5</xsl:attribute>
															</xsl:if>
															<td>
																<xsl:value-of select="@fieldname"/>
															</td>
															<td>
																<xsl:value-of select="oldvalue"/>
															</td>
															<td>
																<xsl:value-of select="newvalue"/>
															</td>
														</tr>
													</xsl:for-each>
												</table>
											</td>
										</tr>
									</xsl:if>
								</xsl:for-each>
							</table>
						</div>
					</div>
					<xsl:call-template name="footer"/>
				</div>
			</body>
		</html>
	</xsl:template>

	<!-- Activity для пользователя -->
	<xsl:template match="/request[@element='user_activity']">
		<xsl:variable name="dbid" select="@dbid"/>
		<html>
			<head>
				<title>User activity</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper">
					<div id="blockWindow" style="display:none"/>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">Activity UserID</h4>
							<div class="actionbar">
								<table width="100%">
									<tr>
										<td width="50%">
											<xsl:call-template name="showasxml_action"/>
										</td>
										<td style="text-align:right">
											<xsl:call-template name="close_action"/>
										</td>
									</tr>
								</table>
							</div>
							<table class="viewtable">
								<tr>
									<td width="22px" class="th">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td width="3%" class="th">
									</td>
									<td width="25%" class="th">
										Time
									</td>
									<td width="30%" class="th">
										Type
									</td>
									<td class="th">
										DocID
									</td>
									<td class="th">
										DocType
									</td>
								</tr>
								<xsl:for-each select="query/entry">
									<tr>
										<xsl:variable name="num" select="position()"/>
										<xsl:if test="$num mod 2 = 0">
											<xsl:attribute name="bgcolor">#efefef</xsl:attribute>
										</xsl:if>
										<td width="3%" style='text-align:center'>
											<input type="checkbox" name="chbox" value="{@docid}">
												<xsl:attribute name="id" select="concat('chbox',@docid)"/>
											</input>
										</td>
										<td style="text-align:center">
											<xsl:if test="./changes/entry">
												<img src="SharedResources/img/classic/plus.gif">
													<xsl:attribute name="onclick">javascript:openActivityProperties(this)</xsl:attribute>
													<xsl:attribute name="style">cursor:pointer</xsl:attribute>
												</img>
											</xsl:if>
										</td>
										<td style="text-align:center">
											<xsl:value-of select="@eventtime"/>
										</td>
										<td>
											<xsl:value-of select="@type"/>
										</td>
										<td>
											<xsl:variable name='linkdoctype'>
												<xsl:choose>
													<xsl:when test="@doctype = 896">
														<xsl:value-of select="'get_maindoc'"/>
													</xsl:when>
													<xsl:when test="@doctype = 897">
														<xsl:value-of select="'get_task'"/>
													</xsl:when>
													<xsl:when test="@doctype = 898">
														<xsl:value-of select="'get_execution'"/>
													</xsl:when>
													<xsl:when test="@doctype = 899">
														<xsl:value-of select="'get_project'"/>
													</xsl:when>
												</xsl:choose>
											</xsl:variable>
											<a href=''>
												<xsl:attribute name='href'>Provider?type=<xsl:value-of select="$linkdoctype"/>&amp;docid=<xsl:value-of select="@docid" />&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
												<xsl:value-of select="@docid"/>
											</a>
										</td>
										<td>
											<xsl:value-of select="@doctype" />
										</td>
									</tr>
									<xsl:if test="changes/entry">
										<tr style="display:none; border-bottom:1px solid #ccc">
											<td></td>
											<td colspan="6">
												<table width="100%" style="font-size:12px; background:#F4F4F4;">
													<tr style="font-weight:bold; border-bottom:1px solid #ccc; text-align:center">
														<td width="20%">Field name</td>
														<td width="40%">Old value</td>
														<td width="40%">New value</td>
													</tr>
													<xsl:for-each select="changes/entry">
														<tr>
															<xsl:variable name="num" select="position()"/>
															<xsl:if test="$num mod 2 = 0">
																<xsl:attribute name="bgcolor">#e5e5e5</xsl:attribute>
															</xsl:if>
															<td>
																<xsl:value-of select="@fieldname"/>
															</td>
															<td>
																<xsl:value-of select="oldvalue"/>
															</td>
															<td>
																<xsl:value-of select="newvalue"/>
															</td>
														</tr>
													</xsl:for-each>
												</table>
											</td>
										</tr>
									</xsl:if>
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