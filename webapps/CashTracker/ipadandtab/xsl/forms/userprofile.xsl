<?xml version="1.0" encoding="utf-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../templates/form.xsl" />
	<xsl:import href="../templates/attach.xsl" />
	<xsl:import href="../templates/doc-info.xsl" />
	<xsl:import href="../layout.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:variable name="editmode" select="/request/document/@editmode" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title">
				<xsl:value-of select="concat(document/fields/fullname, '- 4ms workflow')" />
			</xsl:with-param>
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<div class="container">
			<div class="doc-title">
				<h3>
					<xsl:value-of select="document/fields/fullname" />
				</h3>
			</div>
			<nav class="action-bar">
				<div class="action-group">
					<button class="button button-primary" title="{document/captions/saveandclose/@caption}">
						<xsl:attribute name="onclick">saveUserProfile('<xsl:value-of
							select="substring-after(history/entry[@type eq 'outline'][last()],'/Workflow/')" />')</xsl:attribute>
						<img src="/SharedResources/img/iconset/disk.png" />
						<span>
							<xsl:value-of select="document/captions/saveandclose/@caption" />
						</span>
					</button>
				</div>
				<div class="action-group pull-right">
					<xsl:call-template name="cancel" />
				</div>
			</nav>
			<div class="form-group">
				<form action="Provider" name="frm" method="post" id="frm" enctype="application/x-www-form-urlencoded">
					<fieldset name="property" class="fieldset">
						<xsl:if test="document/@editmode != 'edit'">
							<xsl:attribute name="disabled">disabled</xsl:attribute>
						</xsl:if>

						<legend class="legend">
							<xsl:value-of select="document/captions/properties/@caption" />
						</legend>
						<ol class="fieldset-container">
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/department/@caption" />
								</label>
								<span class="controls">
									<xsl:if test="document/fields/department != '0'">
										<xsl:value-of select="document/fields/department" />
									</xsl:if>
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/postid/@caption" />
								</label>
								<span class="controls">
									<xsl:value-of select="document/fields/post" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/shortname/@caption" />
								</label>
								<span class="controls">
									<xsl:value-of select="document/fields/shortname" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/fullname/@caption" />
								</label>
								<span class="controls">
									<xsl:value-of select="document/fields/fullname" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">ID</label>
								<span class="controls">
									<input type="text" name="userid" value="{document/fields/userid}" class="span6" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/oldpassword/@caption" />
								</label>
								<span class="controls">
									<input type="password" id="oldpwd" name="oldpwd" class="span6" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/newpassword/@caption" />
								</label>
								<span class="controls">
									<input type="password" id="newpwd" name="pwd" class="span6" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/repeatnewpassword/@caption" />
								</label>
								<span class="controls">
									<input type="password" id="newpwd2" name="pwd2" class="span6" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									Instant Messenger address
								</label>
								<span class="controls">
									<div class="span6">
										<div style="float:left;width:80%">
											<input type="text" name="instmsgaddress" value="{document/fields/instmsgaddress}" style="width:100%" />
										</div>
										<xsl:choose>
											<xsl:when test="document/fields/instmsgstatus = 'false'">
												<img src="/SharedResources/img/iconset/bullet_red.png" title="Instant Messenger off" />
											</xsl:when>
											<xsl:when test="document/fields/instmsgstatus = 'true'">
												<img src="/SharedResources/img/iconset/bullet_gren.png" title="Instant Messenger on" />
											</xsl:when>
											<xsl:otherwise>
												<img src="/SharedResources/img/iconset/bullet_red.png" title="Instant Messenger off" />
											</xsl:otherwise>
										</xsl:choose>
									</div>
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									E-mail
								</label>
								<span class="controls">
									<input name="email" type="email" value="{document/fields/email}" class="span6" />
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/role/@caption" />
								</label>
								<span class="controls">
									<table>
										<xsl:for-each select="document/fields/role[not(entry)]">
											<xsl:variable name="role" select="." />
											<xsl:if test="/request/document/glossaries/roles/entry[ison='ON'][name = $role]">
												<tr>
													<td class="td_noteditable">
														<xsl:value-of select="." />
														<xsl:if test="/request/document/glossaries/roles/entry[ison='ON'][name = $role]">
															<input type="hidden" name="role" value="{.}" />
														</xsl:if>
													</td>
												</tr>
											</xsl:if>
										</xsl:for-each>
										<xsl:for-each select="document/fields/role/entry">
											<xsl:variable name="role" select="." />
											<xsl:if test="/request/document/glossaries/roles/entry[ison='ON'][name = $role]">
												<tr>
													<td class="td_noteditable">
														<xsl:value-of select="." />
														<xsl:if test="/request/document/glossaries/roles/entry[ison='ON'][name = $role]">
															<input type="hidden" name="role" value="{.}" />
														</xsl:if>
													</td>
												</tr>
											</xsl:if>
										</xsl:for-each>
									</table>
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/group/@caption" />
								</label>
								<span class="controls">
									<xsl:for-each select="document/fields/group/entry">
										<xsl:value-of select="." />
										<br />
									</xsl:for-each>
								</span>
							</li>
						</ol>
					</fieldset>

					<fieldset name="property" class="fieldset">
						<xsl:if test="document/@editmode != 'edit'">
							<xsl:attribute name="disabled">disabled</xsl:attribute>
						</xsl:if>

						<legend class="legend">
							<xsl:value-of select="document/captions/interface/@caption" />
						</legend>
						<ol class="fieldset-container">
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/countdocinview/@caption" />
								</label>
								<span class="controls">
									<select name="pagesize">
										<option value="10">
											<xsl:if test="document/fields/countdocinview = '10'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											10
										</option>
										<option value="20">
											<xsl:if test="document/fields/countdocinview = '20'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											20
										</option>
										<option value="30">
											<xsl:if test="document/fields/countdocinview = '30'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											30
										</option>
										<option value="50">
											<xsl:if test="document/fields/countdocinview = '50'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											50
										</option>
									</select>
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/refreshperiod/@caption" />
								</label>
								<span class="controls">
									<select name="refresh" id="refresh">
										<option value="3">
											<xsl:if test="document/fields/refresh = '3'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="concat('3 ', document/captions/min/@caption, '.')" />
										</option>
										<option value="5">
											<xsl:if test="document/fields/refresh = '5' ">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="concat('5 ', document/captions/min/@caption, '.')" />
										</option>
										<option value="10">
											<xsl:if test="document/fields/refresh = '10' ">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="concat('10 ', document/captions/min/@caption, '.')" />
										</option>
										<option value="15">
											<xsl:if test="document/fields/refresh = '15' ">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="concat('15 ', document/captions/min/@caption, '.')" />
										</option>
										<option value="20">
											<xsl:if test="document/fields/refresh = '20' ">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="concat('20 ', document/captions/min/@caption, '.')" />
										</option>
										<option value="30">
											<xsl:if test="document/fields/refresh = '30' ">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											<xsl:value-of select="concat('30 ', document/captions/min/@caption, '.')" />
										</option>
									</select>
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/lang/@caption" />
								</label>
								<span class="controls">
									<select name="lang" id="lang">
										<xsl:variable name='chinese' select="document/captions/chinese/@caption" />
										<xsl:variable name='currentlang' select="../@lang" />
										<xsl:for-each select="document/glossaries/langs/entry">
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
								</span>
							</li>
							<li class="control-group">
								<label class="control-label">
									<xsl:value-of select="document/captions/skin/@caption" />
								</label>
								<span class="controls">
									<select name="skin" class="span3">
										<xsl:variable name='currentskin' select="document/fields/skin" />
										<xsl:for-each select="document/glossaries/skins/entry">
											<option value="{id}">
												<xsl:if test="$currentskin = id">
													<xsl:attribute name="selected">selected</xsl:attribute>
												</xsl:if>
												<xsl:value-of select="name" />
											</option>
										</xsl:for-each>
									</select>
								</span>
							</li>
						</ol>
					</fieldset>
					<input type="hidden" name="id" value="userprofile" />
				</form>
			</div>
		</div>
	</xsl:template>

</xsl:stylesheet>
