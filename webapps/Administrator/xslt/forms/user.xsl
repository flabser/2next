<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/action.xsl" />

	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:template match="/request">
		<head>
			<title>
				<xsl:value-of select="concat('Administrator - User: ', document/userid)" />
			</title>
			<link rel="stylesheet" href="css/main.css" />
			<link rel="stylesheet" href="css/form.css" />
			<link rel="stylesheet" href="css/all.min.css" />
			<link rel="stylesheet" href="css/dialogs.css" />
			<script type="text/javascript" src="scripts/jquery/js/jquery-1.4.2.js" />
			<script type="text/javascript" src="scripts/outline.js" />
			<script type="text/javascript" src="scripts/service.js" />
			<script type="text/javascript" src="scripts/dialogs.js" />
			<script type="text/javascript" src="scripts/form.js" />
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
			<header class="form-header">
				<h3 class="doc-title">
					<xsl:choose>
						<xsl:when test="document/userid">
							<xsl:value-of select="concat('User: ', document/userid)" />
						</xsl:when>
						<xsl:otherwise>
							<xsl:value-of select="'User: new'" />
						</xsl:otherwise>
					</xsl:choose>
				</h3>
				<nav class="action-bar">
					<xsl:call-template name="showasxml_action" />
					<a style="margin-left:10px">
						<xsl:attribute name="href">javascript:saveUser()</xsl:attribute>
						<img src="img/save.gif" />
						<font class="button">Save &amp; Close</font>
					</a>
					<a style="margin-left:10px">
						<xsl:attribute name="href">Provider?type=view&amp;element=user_activity&amp;key=<xsl:value-of
							select="document/userid" />&amp;app=<xsl:value-of select="document/@dbid" /></xsl:attribute>
						<img src="img/history.png" />
						<font class="button">Activity</font>
					</a>
					<xsl:call-template name="close_action" />
				</nav>
			</header>
			<section class="form-content">
				<form action="Provider" method="post" id="scriptest" enctype="application/x-www-form-urlencoded">
					<fieldset name="property" class="fieldset">
						<div class="fieldset-container">
							<div class="control-group">
								<div class="control-label">
									User login
								</div>
								<div class="controls">
									<input type="text" name="userid" value="{document/userid}" class="span6" required="required" />
								</div>
							</div>
							<div class="control-group">
								<div class="control-label">
									User name
								</div>
								<div class="controls">
									<input type="text" name="username" value="{document/username}" class="span6" required="required" />
								</div>
							</div>
							<div class="control-group">
								<div class="control-label">
									E-mail
								</div>
								<div class="controls">
									<input type="email" name="email" value="{document/email}" class="span6" required="required" />
								</div>
							</div>
							<div class="control-group">
								<div class="control-label">
									Password
								</div>
								<div class="controls">
									<input type="password" name="oldpwd" value="" id="pwd1" class="span4" />
								</div>
							</div>
							<div class="control-group">
								<div class="control-label">
									Re-enter password
								</div>
								<div class="controls">
									<input type="password" name="pwd" value="" id="pwd2" class="span4" />
								</div>
							</div>
							<div class="control-group">
								<div class="control-label">
									Is supervisor
								</div>
								<div class="controls">
									<label class="input-wrapper">
										<input type="checkbox" value="1" name="isadmin">
											<xsl:if test="document/isadmin = 'true'">
												<xsl:attribute name="checked">checked</xsl:attribute>
											</xsl:if>
										</input>
									</label>
									<span style="font-size:.9em">*if you enable this role, the server will disable special administrator access</span>
								</div>
							</div>
						</div>
					</fieldset>

					<xsl:for-each select="document/glossaries/apps/entry">
						<input type="hidden" class="enabledappsname" value="{apptype}" />
					</xsl:for-each>
					<input type="hidden" name="key" id="key" value="{document/docid}" />
				</form>
			</section>
		</body>
	</xsl:template>

</xsl:stylesheet>
