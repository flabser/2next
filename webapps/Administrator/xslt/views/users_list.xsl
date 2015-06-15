<?xml version="1.0"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />

	<xsl:variable name="type" select="'get_users_list'" />
	<xsl:variable name="doctype" select="'user'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat('Administrator - ', currentview/@title)" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<div class="view">
			<div class="view-header">
				<header class="page-info">
					<h4 class="view-header-title">Users</h4>
				</header>
				<nav class="action-bar">
					<a>
						<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
						<img src="img/refresh.gif" />
						<font class="button">Refresh</font>
					</a>
					<a style="margin-left:10px">
						<xsl:attribute name="href">Provider?type=edit&amp;element=user&amp;key=</xsl:attribute>
						<img src="img/new.gif" />
						<font class="button">New User</font>
					</a>
					<a style="margin-left:10px">
						<xsl:attribute name="href">javascript:delUser();</xsl:attribute>
						<img src="img/delete.gif" />
						<font class="button">Remove User</font>
					</a>
				</nav>
				<section>
					<xsl:call-template name="viewstat" />
				</section>
			</div>
			<div class="view-content">
				<table class="viewtable">
					<tr class="th">
						<td width="44px">
							<label class="entry-select">
								<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
							</label>
						</td>
						<td width="2%"></td>
						<td width="35%">
							<div>UserID/Login</div>
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
														<xsl:value-of select="query/@keyword" />
													</xsl:when>
													<xsl:otherwise>Enter login to search...</xsl:otherwise>
												</xsl:choose>
											</xsl:attribute>
								<xsl:attribute name="onkeyup">javascript:finduserid(this)</xsl:attribute>
								<xsl:attribute name="onfocus">javascript:removevalinput(this)</xsl:attribute>
								<xsl:attribute name="onblur">javascript:checkvalueinput(this)</xsl:attribute>
							</input>
						</td>
						<td width="35%">e-mail</td>
						<td>User name</td>
					</tr>
					<xsl:for-each select="view/entry">
						<tr class="entrylist">
							<xsl:variable name="num" select="position()" />
							<td>
								<label class="entry-select">
									<input type="checkbox" name="chbox" value="{@docid}" />
								</label>
							</td>
							<td>
								<xsl:if test="isadministrator = 'true'">
									<img src="img/bullet-blue.png" title="Administrator" />
								</xsl:if>
							</td>
							<td>
								<a class="doclink" title="{shortname}">
									<xsl:attribute name="href" select="concat('Provider?type=edit&amp;element=user&amp;key=', @id)" />
									<xsl:value-of select="login" />
								</a>
							</td>
							<td>
								<xsl:value-of select="email" />
							</td>
							<td>
								<xsl:value-of select="username" />
							</td>
						</tr>
					</xsl:for-each>
				</table>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
