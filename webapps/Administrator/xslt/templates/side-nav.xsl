<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="outline">
		<xsl:variable name="currentapp" select="document/application | //request/@dbid" />
		<xsl:variable name="currentview" select="//request/@id" />
		<xsl:variable name="currentservice" select="$currentview" />

		<ul class="sidebar-nav sidebar-nav-list">
			<li class="nav-item">
				<xsl:if test="$currentview = 'cfg'">
					<xsl:attribute name="class">nav-item nav-active</xsl:attribute>
				</xsl:if>
				<a class="globalentry">
					<xsl:attribute name="href">Provider?type=view&amp;element=cfg&amp;id=cfg</xsl:attribute>
					Global settings
				</a>
			</li>
			<li class="nav-item">
				<xsl:if test="$currentview = 'logs'">
					<xsl:attribute name="class">nav-item nav-active</xsl:attribute>
				</xsl:if>
				<a class="globalentry">
					<xsl:attribute name="href">Provider?type=view&amp;element=logs</xsl:attribute>
					Logs
				</a>
			</li>
			<li class="nav-item">
				<xsl:if test="$currentview = 'users'">
					<xsl:attribute name="class">nav-item nav-active</xsl:attribute>
				</xsl:if>
				<a class="globalentry">
					<xsl:attribute name="href">Provider?type=view&amp;element=users&amp;id=users</xsl:attribute>
					Users
				</a>
			</li>
			<li class="nav-item">
				<xsl:if test="$currentview = 'scheduler'">
					<xsl:attribute name="class">nav-item nav-active</xsl:attribute>
				</xsl:if>
				<a class="globalentry">
					<xsl:attribute name="href">Provider?type=view&amp;element=scheduler</xsl:attribute>
					Scheduler
				</a>
			</li>
			<li class="nav-item">
				<xsl:if test="$currentview = 'backup'">
					<xsl:attribute name="class">nav-item nav-active</xsl:attribute>
				</xsl:if>
				<a class="globalentry">
					<xsl:attribute name="href">Provider?type=view&amp;element=backup</xsl:attribute>
					Backup
				</a>
			</li>
			<li class="nav-item">
				<xsl:if test="$currentview = 'calendar'">
					<xsl:attribute name="class">nav-item nav-active</xsl:attribute>
				</xsl:if>
				<a class="globalentry">
					<xsl:attribute name="href">Provider?type=view&amp;element=calendar</xsl:attribute>
					Calendar
				</a>
			</li>
		</ul>
		<xsl:for-each select="outline/application[@appid !='Workspace']">
			<xsl:variable name="num" select="position()" />
			<div class="sidebar-nav">
				<a id="outlineEntry{position()}" class="collapse outline-ch">
					<!-- <xsl:attribute name="id">acategory<xsl:value-of select="$num"/></xsl:attribute> -->
					<xsl:attribute name="href">#</xsl:attribute>
					<img src="/SharedResources/img/classic/minus.gif" style="margin-left:6px">
						<xsl:attribute name="id" select="concat('imgcategory',$num)" />
					</img>
					<b style="color:#333;font-size:.9em;margin-left:5px">
						<xsl:value-of select="@appid" />
					</b>
				</a>
				<div class="section expand outlineEntry{position()}">
					<div class="entrys">
						<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif" />
						<span style="display:inline-block; width:180px;">
							<xsl:if test="$currentservice = 'settings' and $currentapp = @appid">
								<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
							</xsl:if>
							<a style="vertical-align:5px">
								<xsl:attribute name="href">Provider?type=view&amp;element=settings&amp;app=<xsl:value-of
									select="@appid" /></xsl:attribute>
								Settings
							</a>
						</span>
					</div>
					<div class="entrys">
						<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif" />
						<span style="display:inline-block; width:200px;">
							<xsl:if test="$currentview = 'activity' and $currentapp = @appid">
								<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
							</xsl:if>
							<a style="vertical-align:5px">
								<xsl:attribute name="href">Provider?type=view&amp;element=activity&amp;app=<xsl:value-of
									select="@appid" />&amp;id=activity&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
								Activity
							</a>
						</span>
					</div>
					<img style="margin-left:1px;" src="/SharedResources/img/classic/tree_tee.gif" />
					<span style=" display:inline-block; vertical-align:5px; width:90%">
						<a>
							<xsl:attribute name="id">acategory<xsl:value-of select="$num" />role</xsl:attribute>
							<xsl:attribute name="href">javascript:colapseOutlineCategory('<xsl:value-of select="$num" />role')</xsl:attribute>
							<img src="/SharedResources/img/classic/minus.gif" style="margin-left:2px">
								<xsl:attribute name="id" select="'imgcategory',$num,'role'" />
							</img>
							<font style="color:#333;font-size:.7em;margin-left:5px;">
								Rule
							</font>
						</a>
					</span>
					<span class="outlineEntry{$num}role">
						<div class="entrys">
							<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif" />
							<img src="/SharedResources/img/classic/tree_tee.gif" />
							<span style="display:inline-block; width:180px;">
								<xsl:if test="$currentservice = 'forms' and $currentapp = @appid">
									<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
								</xsl:if>
								<a style="vertical-align:5px">
									<xsl:attribute name="href">Provider?type=view&amp;element=forms&amp;app=<xsl:value-of
										select="@appid" /></xsl:attribute>
									Forms
								</a>
							</span>
						</div>
						<div class="entrys">
							<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif" />
							<img src="/SharedResources/img/classic/tree_tee.gif" />
							<span style="display:inline-block; width:180px;">
								<xsl:if test="$currentservice = 'pages' and $currentapp = @appid">
									<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
								</xsl:if>
								<a style="vertical-align:5px">
									<xsl:attribute name="href">Provider?type=view&amp;element=pages&amp;app=<xsl:value-of
										select="@appid" /></xsl:attribute>
									Pages
								</a>
							</span>
						</div>
						<div class="entrys">
							<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif" />
							<img src="/SharedResources/img/classic/tree_corner.gif" />
							<span style="display:inline-block; width:180px;">
								<xsl:if test="$currentservice = 'get_handler_list' and $currentapp = @appid">
									<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
								</xsl:if>
								<a style="vertical-align:5px">
									<xsl:attribute name="href">Provider?type=view&amp;element=handlers&amp;app=<xsl:value-of
										select="@appid" /></xsl:attribute>
									Handlers
								</a>
							</span>
						</div>
					</span>
					<xsl:for-each select="database">
						<img style="margin-left:1px" src="/SharedResources/img/classic/tree_corner.gif" />
						<span style="vertical-align:5px; display:inline-block; width:90%">
							<a href="">
								<xsl:attribute name="id">acategory<xsl:value-of select="$num" />database</xsl:attribute>
								<xsl:attribute name="href">javascript:colapseOutlineCategory('<xsl:value-of select="$num" />database')</xsl:attribute>
								<img src="/SharedResources/img/classic/minus.gif" style="margin-left:2px">
									<xsl:attribute name="id" select="concat('imgcategory',$num,'database')" />
								</img>
								<font style="color:#333;font-size:.7em;margin-left:5px">
									Database&#xA0; </font>
							</a>
							<em>
								<small>
									<xsl:value-of select="@dbid" />
								</small>
							</em>
						</span>
						<span class="outlineEntry{$num}database">
							<div class="entrys">
								<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif" />
								<img src="/SharedResources/img/classic/tree_tee.gif" />
								<span style="display:inline-block; width:180px;">
									<xsl:if test="$currentview ='documents' and $currentapp = @appid">
										<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
									</xsl:if>
									<a style="vertical-align:5px">
										<xsl:attribute name="href">Provider?type=view&amp;element=documents&amp;dbid=<xsl:value-of
											select="@dbid" />&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
										Documents
									</a>
								</span>
							</div>
							<div class="entrys">
								<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif" />
								<img src="/SharedResources/img/classic/tree_corner.gif" />
								<span style="display:inline-block; width:180px;">
									<xsl:if test="$currentview ='glossaries' and $currentapp = @appid">
										<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
									</xsl:if>
									<a style="vertical-align:5px">
										<xsl:attribute name="href">Provider?type=view&amp;element=glossaries&amp;dbid=<xsl:value-of
											select="@dbid" />&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
										Glossaries
									</a>
								</span>
							</div>
						</span>
					</xsl:for-each>
				</div>
			</div>
		</xsl:for-each>
	</xsl:template>

</xsl:stylesheet>
