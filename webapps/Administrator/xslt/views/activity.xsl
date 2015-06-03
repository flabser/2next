<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/view.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:variable name="dbid" select="@dbid" />
	<xsl:variable name="type" select="'activity'" />
	<xsl:variable name="doctype" select="'activity'" />
	<xsl:variable name="viewtype" select="'activity'" />
	<xsl:variable name="currentapp" select="currentview/@app" />
	<xsl:variable name="currentview" select="currentview" />
	<xsl:variable name="currentservice" select="currentview/@service" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="'Administrator - Document activity'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<div class="view">
			<xsl:apply-templates select="/request" mode="content_activity" />
		</div>
	</xsl:template>

	<xsl:template match="/request[@element != 'activity']" mode="content_activity">
		<header class="view-header">
			<h1>
				Activity / Doctype
				<small>
					<xsl:value-of select="document/@doctype | query/@doctype" />
				</small>
				<xsl:text> docID </xsl:text>
				<small>
					<xsl:value-of select="document/@docid | query/@docid" />
				</small>
			</h1>
			<div class="actionbar">
				<xsl:call-template name="showasxml_action" />
				<xsl:call-template name="close_action" />
			</div>
		</header>
		<table class="viewtable">
			<tr>
				<td width="22px" class="th" style="text-align:center">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
				</td>
				<td width="3%" class="th" />
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
						<input type="checkbox" name="chbox" id="chbox{@docid}" value="{@docid}" />
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
						<xsl:value-of select="@eventtime" />
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="@type" />
					</td>
					<td style="border:1px solid #ccc; padding-left:5px">
						<xsl:value-of select="@userid" />
					</td>
				</tr>
				<xsl:if test="changes/entry">
					<tr style="display:none; border-bottom:1px solid #ccc">
						<td />
						<td colspan="6">
							<table width="100%" style="font-size:12px; background:#F4F4F4;">
								<tr style="font-weight:bold; border-bottom:1px solid #ccc; text-align:center">
									<td width="20%">Field name</td>
									<td width="40%">Old value</td>
									<td width="40%">New value</td>
								</tr>
								<xsl:for-each select="changes/entry">
									<tr>
										<xsl:variable name="num" select="position()" />
										<xsl:if test="$num mod 2 = 0">
											<xsl:attribute name="bgcolor">#e5e5e5</xsl:attribute>
										</xsl:if>
										<td>
											<xsl:value-of select="@fieldname" />
										</td>
										<td>
											<xsl:value-of select="oldvalue" />
										</td>
										<td>
											<xsl:value-of select="newvalue" />
										</td>
									</tr>
								</xsl:for-each>
							</table>
						</td>
					</tr>
				</xsl:if>
			</xsl:for-each>
		</table>
	</xsl:template>

	<!-- System activity -->
	<xsl:template match="/request[@element = 'activity']" mode="content_activity">
		<header class="view-header">
			<h1>System activity</h1>
			<div class="actionbar">
				<xsl:call-template name="refresh_action" />
			</div>
			<xsl:call-template name="viewstat" />
		</header>
		<section class="view-content">
			<table class="viewtable">
				<thead>
					<tr>
						<td class="col-select">
							<input type="checkbox" onClick="checkAll(this);" />
						</td>
						<td class="col-icon"></td>
						<td class="col-datetime">Time</td>
						<td width="20%">Type</td>
						<td width="25%">User</td>
						<td>DocID</td>
						<td>DocType</td>
					</tr>
				</thead>
				<xsl:for-each select="query/entry">
					<tr>
						<td class="col-select">
							<input type="checkbox" name="chbox" value="{@docid}" id="chbox{@docid}" />
						</td>
						<td class="col-icon">
							<xsl:if test="./changes/entry">
								<img src="/SharedResources/img/classic/plus.gif">
									<xsl:attribute name="onclick">javascript:openActivityProperties(this)</xsl:attribute>
									<xsl:attribute name="style">cursor:pointer</xsl:attribute>
								</img>
							</xsl:if>
						</td>
						<td class="col-datetime">
							<xsl:value-of select="@eventtime" />
						</td>
						<td>
							<xsl:value-of select="@type" />
						</td>
						<td>
							<xsl:value-of select="@userid" />
							<xsl:if test="@ip != 'null' and string-length(normalize-space(@ip)) != 0">
								<b>
									<xsl:value-of select="concat(' ( ',@ip,')')" />
								</b>
							</xsl:if>
						</td>
						<td>
							<xsl:variable name='linkdoctype'>
								<xsl:choose>
									<xsl:when test="@doctype = 896">
										<xsl:value-of select="'get_maindoc'" />
									</xsl:when>
									<xsl:when test="@doctype = 897">
										<xsl:value-of select="'get_task'" />
									</xsl:when>
									<xsl:when test="@doctype = 898">
										<xsl:value-of select="'get_execution'" />
									</xsl:when>
									<xsl:when test="@doctype = 899">
										<xsl:value-of select="'get_project'" />
									</xsl:when>
								</xsl:choose>
							</xsl:variable>
							<a href='' class="doclink">
								<xsl:attribute name='href'>Provider?type=<xsl:value-of select="$linkdoctype" />&amp;docid=<xsl:value-of
									select="@docid" />&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
								<xsl:value-of select="@docid" />
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
										<td>New value</td>
									</tr>
									<xsl:for-each select="changes/entry">
										<tr>
											<xsl:variable name="num" select="position()" />
											<xsl:if test="$num mod 2 = 0">
												<xsl:attribute name="bgcolor">#e5e5e5</xsl:attribute>
											</xsl:if>
											<td>
												<xsl:value-of select="@fieldname" />
											</td>
											<td>
												<xsl:value-of select="oldvalue" />
											</td>
											<td>
												<xsl:value-of select="newvalue" />
											</td>
										</tr>
									</xsl:for-each>
								</table>
							</td>
						</tr>
					</xsl:if>
				</xsl:for-each>
			</table>
		</section>
	</xsl:template>

	<!-- User activity -->
	<xsl:template match="/request[@element='user_activity']" mode="content_activity">
		<header>
			<h4 class="view-header-title">Activity UserID</h4>
			<div class="actionbar">
				<table width="100%">
					<tr>
						<td width="50%">
							<xsl:call-template name="showasxml_action" />
						</td>
						<td style="text-align:right">
							<xsl:call-template name="close_action" />
						</td>
					</tr>
				</table>
			</div>
		</header>
		<section>
			<table class="viewtable">
				<tr>
					<td width="22px" class="th">
						<input type="checkbox" id="allchbox" onClick="checkAll(this);" />
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
						<xsl:variable name="num" select="position()" />
						<xsl:if test="$num mod 2 = 0">
							<xsl:attribute name="bgcolor">#efefef</xsl:attribute>
						</xsl:if>
						<td width="3%" style='text-align:center'>
							<input type="checkbox" name="chbox" value="{@docid}">
								<xsl:attribute name="id" select="concat('chbox',@docid)" />
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
							<xsl:value-of select="@eventtime" />
						</td>
						<td>
							<xsl:value-of select="@type" />
						</td>
						<td>
							<xsl:variable name='linkdoctype'>
								<xsl:choose>
									<xsl:when test="@doctype = 896">
										<xsl:value-of select="'get_maindoc'" />
									</xsl:when>
									<xsl:when test="@doctype = 897">
										<xsl:value-of select="'get_task'" />
									</xsl:when>
									<xsl:when test="@doctype = 898">
										<xsl:value-of select="'get_execution'" />
									</xsl:when>
									<xsl:when test="@doctype = 899">
										<xsl:value-of select="'get_project'" />
									</xsl:when>
								</xsl:choose>
							</xsl:variable>
							<a href=''>
								<xsl:attribute name='href'>Provider?type=<xsl:value-of select="$linkdoctype" />&amp;docid=<xsl:value-of
									select="@docid" />&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
								<xsl:value-of select="@docid" />
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
											<xsl:variable name="num" select="position()" />
											<xsl:if test="$num mod 2 = 0">
												<xsl:attribute name="bgcolor">#e5e5e5</xsl:attribute>
											</xsl:if>
											<td>
												<xsl:value-of select="@fieldname" />
											</td>
											<td>
												<xsl:value-of select="oldvalue" />
											</td>
											<td>
												<xsl:value-of select="newvalue" />
											</td>
										</tr>
									</xsl:for-each>
								</table>
							</td>
						</tr>
					</xsl:if>
				</xsl:for-each>
			</table>
		</section>
	</xsl:template>

</xsl:stylesheet>
