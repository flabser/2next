<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />

	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="no" />
	<xsl:variable name="type">get_users_list</xsl:variable>
	<xsl:variable name="doctype">user</xsl:variable>

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
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">Users</h4>
							<div class="actionbar">
								<span class="action" style="padding-bottom:5px">
									<a>
										<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
										<img src="img/refresh.gif"/>
										<font class="button">Refresh</font>
									</a>
									<a style="margin-left:10px">
										<xsl:attribute name="href">Provider?type=edit&amp;element=user&amp;key=</xsl:attribute>
										<img src="img/new.gif"/>
										<font class="button">New User</font>
									</a>
									<a style="margin-left:10px">
										<xsl:attribute name="href">javascript:delUser();</xsl:attribute>
										<img src="img/delete.gif" />
										<font class="button">Remove User</font>
									</a>
								</span>
								<xsl:call-template name="viewstat"/>
							</div>
							<table class="viewtable">
								<tr class="th">
									<td width="22px" class="thcell">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td width="2%" class="thcell">
									</td>
									<td width="35%" class="thcell">
										UserID/Login
										<br/>
										<input type="text" name="usersearch" id="usersearch" size="29">
											<xsl:attribute name="style">
												height:19px; font-size:12px; margin-top:3px; margin-bottom:2px; 
												<xsl:if test="query/@keyword ='null'">
													color:#ccc;
												</xsl:if>
											</xsl:attribute>
											<xsl:attribute name="value">
												<xsl:choose>
													<xsl:when test="query/@keyword !='null'">
														<xsl:value-of select="query/@keyword"/>
													</xsl:when>
													<xsl:otherwise>Введите userid для поиска...</xsl:otherwise>
												</xsl:choose>
											</xsl:attribute>
											<xsl:attribute name="onkeyup">javascript:finduserid(this)</xsl:attribute>
											<xsl:attribute name="onfocus">javascript:removevalinput(this)</xsl:attribute>
											<xsl:attribute name="onblur">javascript:checkvalueinput(this)</xsl:attribute>
										</input>
									</td>
									<td width="35%" class="thcell">e-mail</td>
									<td width="26%" class="thcell">JID</td>
									<!-- <td class="thcell">Последнее изменение</td> -->
								</tr>
								<xsl:for-each select="query/entry">
									<tr class="entrylist" onmouseover="javascript:elemBackground(this,'EEEEEE')" onmouseout="elemBackground(this,'FFFFFF')">
										<xsl:variable name="num" select="position()"/>
										<xsl:attribute name="bgcolor">#FFFFFF</xsl:attribute>
										<td style="text-align:center; border:1px solid #ccc">
											<input type="checkbox" name="chbox" value="{@docid}"/>
										</td>
										<td style="border:1px solid #ccc; text-align:center;">
											<xsl:if test="isadministrator = 'true'">
												<img src="img/bullet-blue.png" title="Administrator"/>
											</xsl:if>
										</td>
										<td style="border:1px solid #ccc; padding-left:5px">
											<a class="doclink" title="{shortname}">
												<xsl:attribute name="href">Provider?type=edit&amp;element=user&amp;key=<xsl:value-of select="@docid"/></xsl:attribute>
												<xsl:value-of select="userid"/>
											</a>
										</td>
										<td class="title" style="font-size:0.8em; border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="email"/>
										</td>
										<td class="title" style="font-size:0.8em; border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="jid"/>
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