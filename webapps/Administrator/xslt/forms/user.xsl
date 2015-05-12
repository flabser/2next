<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/action.xsl" />
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	 doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:template match="/request">
		<head>
			<title>
				<xsl:value-of select="concat('Administrator - User: ',document/userid)"/>
			</title>
			<link rel="stylesheet" href="css/main.css"/>
			<link rel="stylesheet" href="css/form.css"/>
			<link rel="stylesheet" href="css/dialogs.css"/>
			<script type="text/javascript" src="scripts/jquery/js/jquery-1.4.2.js"/>
			<script type="text/javascript" src="scripts/outline.js"/>
			<script type="text/javascript" src="scripts/service.js"/>
			<script type="text/javascript" src="scripts/dialogs.js"/>
			<script type="text/javascript" src="scripts/form.js"/>
			<script>
				function chck(el, loginmode){
					if(!(el.checked)){
						$("select[id="+loginmode+"]").attr("disabled","disabled");
					}else{
						$("select[id="+loginmode+"]").removeAttr("disabled");
					}
				}
			</script>
		</head>
		<body>		
			<table width="100%">
				<tr>
					<td width="50%">
						<xsl:call-template name="showasxml_action"/>
						<a style="margin-left:10px">
							<xsl:attribute name="href">javascript:saveUser()</xsl:attribute>
							<img src="img/save.gif"/>
							<font class="button">Save &amp; Close</font>
						</a>
						<a style="margin-left:10px">
							<xsl:attribute name="href">Provider?type=view&amp;element=user_activity&amp;key=<xsl:value-of select="document/userid"/>&amp;app=<xsl:value-of select="document/@dbid"/></xsl:attribute>
							<img src="img/history.png"/>
							<font class="button">Activity</font>
						</a>
					</td>
					<td style="text-align:right">
						<xsl:if test="document/@docid='0'">
							<font style="font-size:0.7em">new user</font>
						</xsl:if>
					</td>
					<td style="text-align:right">
						<xsl:call-template name="close_action"/>
					</td>
				</tr>
			</table>
			<br/>
			<font style="font-family:verdana; font-size:20px;">User:&#xA0;</font>
			<xsl:value-of select="document/userid"/>
			<hr/>
			<form action="Provider" method="post" id="scriptest" enctype="application/x-www-form-urlencoded">
				<table border="0" style="margin-top:10px;width:100%">
					<tr>
						<td class="fc">UserID:&#xA0;</td>
						<td>
							<input type="text" name="userid" size="20" value="{document/userid}"/>
						</td>
					</tr>
					<tr>
						<td class="fc">E-mail:&#xA0;</td>
						<td>
							<input type="text" name="email" size="20" value="{document/email}"/>
						</td>
					</tr>
					<tr>
						<td class="fc">JID:&#xA0;</td>
						<td>
							<input type="text" name="instmsgaddress" size="20" value="{document/instmsgaddress}"/>
							<span style="margin-left:5px">
								<xsl:choose>
									<xsl:when test="document/instmsgstatus = 'true'">
										<img src="img/jid_online.gif"/><span style="font-size:0.7em;margin-left:3px">available</span>
									</xsl:when>
									<xsl:otherwise>
										<img src="img/jid_offline.gif"/><span style="font-size:0.7em;margin-left:3px">unavailable</span>
									</xsl:otherwise>
								</xsl:choose>
							</span>
						</td>
					</tr>
					<tr>
						<td class="fc">Password:&#xA0;</td>
						<td>
							<input type="password" name="oldpwd" id="pwd1" size="20" value="" autocomplete="off"/>&#xA0;
						</td>
					</tr>
					<tr>
						<td class="fc">Re-enter password:&#xA0;</td>
						<td>
							<input type="password" name="pwd" id="pwd2" size="20" value="" autocomplete="off"/>&#xA0;
						</td>
					</tr>
					<tr>
						<td class="fc">Is supervisor:&#xA0;</td>
						<td>
							<input type="checkbox" value="1" name="isadmin">
								<xsl:if test="document/isadmin = 'true'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
							</input>
							<spin style="font-size:0.7em">*if you enable this role, the server will disable special administrator access</spin>
						</td>
					</tr>
					</table>
					<table style="width:100%">
					<tr>
						<td class="fc">Enabled applications:&#xA0;</td>
						<td style="padding-top:10px">
							<table>
								<xsl:for-each select="document/glossaries/apps/entry">
									<xsl:variable name="app" select="apptype"/>
									<tr class="QA_{$app}">
										<td style="padding-right:5px">
											<input type="checkbox" value="{apptype}" name="enabledapps" id ="projects_on">
												<xsl:if test="/request/document/enabledapps/entry/appname = $app">
													<xsl:attribute name="checked">checked</xsl:attribute>
												</xsl:if>
												<xsl:attribute name="onclick">javascript:chck(this, 'loginmode_<xsl:value-of select="$app"/>')</xsl:attribute> 
											</input>
											<font style="padding:5px"><xsl:value-of select="apptype"/></font>
										</td>
										<td style="padding-right:5px" class ="loginmode">
											<font style="padding:5px; font-size:12px">Login mode:&#xA0;</font>
											<select name="loginmode" onchange="javascript:selectLoginMode(this,'{$app}')">
												 <xsl:if test="not(/request/document/enabledapps/entry/appname = $app)">
													 <xsl:attribute name="disabled">disabled</xsl:attribute> 
												</xsl:if> 
												<xsl:attribute name="id">loginmode_<xsl:value-of select="$app"/></xsl:attribute>
												<option value="0">
													<xsl:if test="/request/document/enabledapps/entry[appname=$app]/loginmode = 'LOGIN_AND_REDIRECT' or not(/request/document/enabledapps/entry[appname=$app]/loginmode) ">
														<xsl:attribute name="selected" select="'selected'"/>
													</xsl:if>
													Login and redirect
												</option>
												<option value="1">
													<xsl:if test="/request/document/enabledapps/entry[appname=$app]/loginmode = 'LOGIN_AND_QUESTION'">
														<xsl:attribute name="selected" select="'selected'"/>
													</xsl:if>
													Login and question
												</option>
												<option value="2">
													<xsl:if test="/request/document/enabledapps/entry[appname=$app]/loginmode = 'JUST_LOGIN'">
														<xsl:attribute name="selected" select="'selected'"/>
													</xsl:if>
													Login
												</option>
											</select>
										</td>
										<td style="padding-right:5px">
											<font style="font-size:12px">Question:&#xA0;</font>
											<input type="text" name="question_{$app}" size="30" value="{/request/document/enabledapps/entry[appname=$app]/entry[1]/question}">
												<xsl:if test="/request/document/enabledapps/entry[appname eq $app]/loginmode = 'JUST_LOGIN' or 'LOGIN_REDIRECT' or not(/request/document/enabledapps/entry/appname = $app)">
													<xsl:attribute name="readonly" select="'readonly'"/>
													<xsl:attribute name="value"></xsl:attribute>
													<xsl:attribute name="class" select="'readonly'"/>
												</xsl:if>
											</input>
										</td>
										<td style="padding-right:5px">
											<font style=" font-size:12px">Answer:&#xA0;</font>
											<input type="text" name="answer_{$app}" size="30" value="{/request/document/enabledapps/entry[appname=$app]/entry[1]/answer}">
												<xsl:if test="/request/document/enabledapps/entry[appname eq $app]/loginmode = 'JUST_LOGIN' or 'LOGIN_REDIRECT' or not(/request/document/enabledapps/entry/appname = $app)">
													<xsl:attribute name="readonly" select="'readonly'"/>
													<xsl:attribute name="value"></xsl:attribute>
													<xsl:attribute name="class" select="'readonly'"/>
												</xsl:if>
											</input>
											<a href="javascript:$.noop()" onclick="javascript:AddNewQuestAndAnswer(this,'{$app}')">
												add
											</a>
										</td>
									</tr>
									<xsl:for-each select="/request/document/enabledapps/entry[appname=$app]/entry[not(position() = 1)]">
										<tr class="QA_{$app}">
											<td style="padding-right:5px"/>
											<td style="padding-right:5px"/>
											<td style="padding-right:5px">
												<font style="font-size:12px">Question:&#xA0;</font>
												<input type="text" name="question_{$app}" size="30" value="{question}">
													<xsl:if test="/request/document/enabledapps/entry[appname=$app]/loginmode = 'JUST_LOGIN'">
														<xsl:attribute name="readonly" select="'readonly'"/>
														<xsl:attribute name="value"></xsl:attribute>
														<xsl:attribute name="class" select="'readonly'"/>
													</xsl:if>
												</input>
											</td>
											<td style="padding-right:5px">
												<font style=" font-size:12px">Answer:&#xA0;</font>
												<input type="text" name="answer_{$app}" size="30" value="{answer}">
													<xsl:if test="/request/document/enabledapps/entry[appname=$app]/loginmode = 'JUST_LOGIN'">
														<xsl:attribute name="readonly" select="'readonly'"/>
														<xsl:attribute name="value"></xsl:attribute>
														<xsl:attribute name="class" select="'readonly'"/>
													</xsl:if>
												</input>
												<a href="javascript:$.noop()" onclick="javascript:AddNewQuestAndAnswer(this,'{$app}')">
													add
												</a>
											</td>
										</tr>
									</xsl:for-each>
								</xsl:for-each>
							</table>
						</td>
					</tr>
				</table>
				<!-- Скрытые поля -->
				<xsl:for-each select="document/glossaries/apps/entry">
					<input type="hidden" class="enabledappsname" value="{apptype}"/>
				</xsl:for-each>
				<input type="hidden" name="key" id="key" value="{document/docid}"/>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>