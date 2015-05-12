<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/form.xsl" />
	<xsl:import href="../templates/attach-cert.xsl" />
	<xsl:import href="../layout.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="editmode" select="//document/@editmode" />
	<xsl:variable name="doctype" select="//captions/doctypemultilang/@caption" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat('Профиль: ', //fields/fullname)" />
			<xsl:with-param name="aside_collapse" select="'aside_collapse'" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<header class="form-header">
			<h3 class="doc-title">
				<xsl:value-of select="concat(//fields/title, ' - ', //fields/fullname)" />
			</h3>
			<xsl:apply-templates select="//actionbar">
				<xsl:with-param name="fixed_top" select="''" />
			</xsl:apply-templates>
		</header>
		<section class="form-content">
			<div id="tabs">
				<ul class="ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header">
					<li>
						<a href="#tabs-1">
							<xsl:value-of select="//captions/properties/@caption" />
						</a>
					</li>
					<li>
						<a href="#tabs-2">
							<xsl:value-of select="//captions/interface/@caption" />
						</a>
					</li>
					<!-- <li>
						<a href="#tabs-3">
							<xsl:value-of select="//captions/attachments_cert/@caption" />
						</a>
					</li> -->
				</ul>
				<form action="Provider" name="frm" method="post" id="userProfile" enctype="application/x-www-form-urlencoded">
					<div class="ui-tabs-panel" id="tabs-1">
						<fieldset name="property" class="fieldset">
							<xsl:if test="document/@editmode != 'edit'">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>

							<div class="fieldset-container">
								<!-- <div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/department/@caption" />
									</div>
									<div class="controls">
										<xsl:if test="//fields/department != '0'">
											<xsl:value-of select="//fields/department" />
										</xsl:if>
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/postid/@caption" />
									</div>
									<div class="controls">
										<xsl:value-of select="//fields/post" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/shortname/@caption" />
									</div>
									<div class="controls">
										<xsl:value-of select="//fields/shortname" />
									</div>
								</div> -->
								<div class="control-group">
									<div class="control-label">
										ID
									</div>
									<div class="controls">
										<xsl:value-of select="//fields/userid" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/fullname/@caption" />
									</div>
									<div class="controls">
										<input name="fullname" type="text" class="span4" value="{//fields/fullname}" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/oldpassword/@caption" />
									</div>
									<div class="controls">
										<input name="oldpwd" type="password" class="span4" autocomplete="off" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/newpassword/@caption" />
									</div>
									<div class="controls">
										<input name="pwd" type="password" class="span4" autocomplete="off" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/repeatnewpassword/@caption" />
									</div>
									<div class="controls">
										<input name="pwd2" type="password" class="span4" autocomplete="off" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										Instant Messenger address
									</div>
									<div class="controls">
										<div class="span4">
											<div style="float:left;width:80%">
												<input type="text" name="instmsgaddress" value="{//fields/instmsgaddress}" style="width:100%" />
											</div>
											<xsl:choose>
												<xsl:when test="//fields/instmsgstatus = 'false'">
													<img src="/SharedResources/img/iconset/bullet_red.png" title="Instant Messenger off" />
												</xsl:when>
												<xsl:when test="//fields/instmsgstatus = 'true'">
													<img src="/SharedResources/img/iconset/bullet_gren.png" title="Instant Messenger on" />
												</xsl:when>
												<xsl:otherwise>
													<img src="/SharedResources/img/iconset/bullet_red.png" title="Instant Messenger off" />
												</xsl:otherwise>
											</xsl:choose>
										</div>
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										E-mail
									</div>
									<div class="controls">
										<input name="email" type="text" class="span4" value="{//fields/email}" />
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/role/@caption" />
									</div>
									<div class="controls">
										<table>
											<xsl:for-each select="//fields/role[not(entry)]">
												<xsl:variable name="role" select="." />
												<xsl:if test="//document/glossaries/roles/entry[ison='ON'][name = $role]">
													<tr>
														<td style="width:500px;" class="td_noteditable">
															<xsl:if test="../../../../document/@editmode != 'edit'">
																<xsl:attribute name="class">td_noteditable</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="." />

															<xsl:if test="//document/glossaries/roles/entry[ison='ON'][name = $role]">
																<input type="hidden" name="role" value="{.}" />
															</xsl:if>
														</td>
													</tr>
												</xsl:if>
											</xsl:for-each>
											<xsl:for-each select="//fields/role/entry">
												<xsl:variable name="role" select="." />
												<xsl:if test="//document/glossaries/roles/entry[ison='ON'][name = $role]">
													<tr>
														<td style="width:500px;" class="td_noteditable">
															<xsl:if test="../../../../document/@editmode != 'edit'">
																<xsl:attribute name="class">td_noteditable</xsl:attribute>
															</xsl:if>
															<xsl:value-of select="." />

															<xsl:if test="//document/glossaries/roles/entry[ison='ON'][name = $role]">
																<input type="hidden" name="role" value="{.}" />
															</xsl:if>
														</td>
													</tr>
												</xsl:if>
											</xsl:for-each>
										</table>
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/group/@caption" />
									</div>
									<div class="controls">
										<table>
											<xsl:for-each select="//fields/group/entry">
												<tr>
													<td style="width:500px" class="td_noteditable">
														<xsl:if test="../../../../document/@editmode != 'edit'">
															<xsl:attribute name="class">td_noteditable</xsl:attribute>
														</xsl:if>
														<xsl:value-of select="." />
													</td>
												</tr>
											</xsl:for-each>
										</table>
									</div>
								</div>
							</div>
						</fieldset>
					</div>
					<div id="tabs-2">
						<fieldset name="interface" class="fieldset">
							<xsl:if test="document/@editmode != 'edit'">
								<xsl:attribute name="disabled">disabled</xsl:attribute>
							</xsl:if>

							<div class="fieldset-container">
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/countdocinview/@caption" />
									</div>
									<div class="controls">
										<select name="pagesize" class="span2">
											<option value="10">
												<xsl:if test="//fields/countdocinview = '10'">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												10
											</option>
											<option value="20">
												<xsl:if test="//fields/countdocinview = '20'">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												20
											</option>
											<option value="30">
												<xsl:if test="//fields/countdocinview = '30'">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												30
											</option>
											<option value="50">
												<xsl:if test="//fields/countdocinview = '50'">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												50
											</option>
										</select>
									</div>
								</div>
								<div class="control-group" style="display:none">
									<div class="control-label">
										<xsl:value-of select="//captions/refreshperiod/@caption" />
									</div>
									<div class="controls">
										<select name="refresh" class="span2">
											<option value="3">
												<xsl:value-of select="concat('3 ', //captions/min/@caption, '.')" />
											</option>
											<option value="5">
												<xsl:value-of select="concat('5 ', //captions/min/@caption, '.')" />
											</option>
											<option value="10">
												<xsl:value-of select="concat('10 ', //captions/min/@caption, '.')" />
											</option>
											<option value="15">
												<xsl:value-of select="concat('15 ', //captions/min/@caption, '.')" />
											</option>
											<option value="20">
												<xsl:value-of select="concat('20 ', //captions/min/@caption, '.')" />
											</option>
											<option value="30">
												<xsl:value-of select="concat('30 ', //captions/min/@caption, '.')" />
											</option>
										</select>
									</div>
								</div>
								<div class="control-group">
									<div class="control-label">
										<xsl:value-of select="//captions/lang/@caption" />
									</div>
									<div class="controls">
										<select name="lang" class="span3">
											<xsl:variable name='chinese' select="//captions/chinese/@caption" />
											<xsl:variable name='currentlang' select="../@lang" />
											<xsl:for-each select="//glossaries/langs/entry">
												<option id="{id}" value="{id}">
													<xsl:if test="$currentlang = id">
														<xsl:attribute name="selected">selected</xsl:attribute>
													</xsl:if>
													<xsl:if test="id = 'CHN'">
														<xsl:value-of select="$chinese" />
													</xsl:if>
													<xsl:if test="id != 'CHN'">
														<xsl:value-of select="name" />
													</xsl:if>
												</option>
											</xsl:for-each>
										</select>
									</div>
								</div>
								<div class="control-group" style="display:none">
									<div class="control-label">
										<xsl:value-of select="//captions/skin/@caption" />
									</div>
									<div class="controls">
										<select name="skin" class="span3">
											<xsl:variable name='currentskin' select="//fields/skin" />
											<xsl:for-each select="//glossaries/skins/entry">
												<option value="{id}">
													<xsl:if test="$currentskin = id">
														<xsl:attribute name="selected">selected</xsl:attribute>
													</xsl:if>
													<xsl:value-of select="name" />
												</option>
											</xsl:for-each>
										</select>
									</div>
								</div>
							</div>
						</fieldset>
					</div>
					<input type="hidden" name="id" value="userprofile" />
				</form>
				<!-- <div id="tabs-3">
					<form action="Uploader" name="upload" id="upload" method="post" enctype="multipart/form-data">
						<fieldset class="fieldset" disabled="disabled">
							<input type="hidden" name="type" value="rtfcontent" />
							<input type="hidden" name="formsesid" value="{formsesid}" />
							<xsl:call-template name="attach_cert" />
						</fieldset>
					</form>
				</div> -->
			</div>
		</section>
	</xsl:template>

</xsl:stylesheet>
