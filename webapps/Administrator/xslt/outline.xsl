<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="templates/html-head-jscss.xsl"/>

	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:template match="/request">
	<xsl:variable name="currentapp" select="currentview/@app"/>
	<xsl:variable name="currentview" select="currentview"/>
	<xsl:variable name="currentservice" select="currentview/@service"/>
		<html>
			<head>
				<title>
					<xsl:value-of select="currentview/@title"/>
				</title>
				<xsl:call-template name="view-html-head-jscss"/>
				<script>
					function onLoadActions(){
						service = '<xsl:value-of select="currentview/@service"/>';
						id = '<xsl:value-of select="currentview"/>';
						app = '<xsl:value-of select="currentview/@app"/>';
						dbid = '<xsl:value-of select="currentview/@dbid"/>';
						curPage = '<xsl:value-of select="currentview/@page"/>';
						refreshAction();
						refresher();
						lw = $("#loadingpage").width();
						lh = $("#loadingpage").height();
						lt = ($(window).height() - lh )/2;
						ll = ($(window).width() - lw )/2;
						$("#loadingpage").css("top",lt).css("left",ll).css("z-index",1);
					}
				</script>
			</head>
			<body onLoad="javascript:onLoadActions();">
				<div id="blockWindow" style="display:none"/>
				<div id="wrapper">
					<div id='loadingpage' style='position:absolute; display:none'>
						<img src='/SharedResources/img/classic/4(4).gif'/>
					</div>	
					<div id="outline">
						<div id="outline-header">
							<font style="font-size:1.1em; font-weight:bold">
								Administrator
							</font>
						</div>
						<div id="outline-container">
						<table border="0" style=" border-collapse: collapse; width:245px">
								<tr>
									<td>
										<div style="display:inline-block; width:77%;">
											<xsl:if test="currentview/@service = 'console'">
												<xsl:attribute name="style">display:inline-block; width:77%; background:#DDE0EC</xsl:attribute>
											</xsl:if>
											<a class="globalentry" href="">
												<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=console</xsl:attribute>
												Console
											</a>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div style="display:inline-block; width:77%;">
											<xsl:if test="currentview/@service = 'console'">
												<xsl:attribute name="style">display:inline-block; width:77%; background:#DDE0EC</xsl:attribute>
											</xsl:if>
											<a class="globalentry">
												<xsl:attribute name="href">Provider?type=edit&amp;element=cfg&amp;id=cfg</xsl:attribute>
												Global settings
											</a>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div style="display:inline-block; width:77%;">
											<xsl:if test="currentview/@service = 'get_logs_list'">
												<xsl:attribute name="style">display:inline-block; width:77%; background:#DDE0EC</xsl:attribute>
											</xsl:if>
											<a class="globalentry">
												<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_logs_list</xsl:attribute>
												Logs
											</a>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div style="display:inline-block; width:77%;">
											<xsl:if test="currentview = 'users'">
												<xsl:attribute name="style">display:inline-block; width:77%; background:#DDE0EC</xsl:attribute>
											</xsl:if>
											<a class="globalentry">
												<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=view&amp;element=users&amp;id=users</xsl:attribute>
												Users
											</a>
										</div>
									</td>
								</tr>
								<tr>
									<td>
										<div style="display:inline-block; width:77%;">
											<xsl:if test="currentview = 'users'">
												<xsl:attribute name="style">display:inline-block; width:77%; background:#DDE0EC</xsl:attribute>
											</xsl:if>
											<a class="globalentry">
												<xsl:attribute name="href">Provider?type=view&amp;element=scheduler&amp;id=scheduler</xsl:attribute>
												Scheduler
											</a>
										</div>
									</td>
								</tr>
							</table>
							<xsl:for-each select="outline/application">
									<xsl:variable name="num" select="position()"/>
									<div style="height:15px; width:245px; display:inline-block; margin-top:5px;">								
										<a href="">
											<xsl:attribute name="id">acategory<xsl:value-of select="$num"/></xsl:attribute>
											<xsl:attribute name="href">javascript:colapseOutlineCategory(<xsl:value-of select="$num"/>)</xsl:attribute>
											<img src="/SharedResources/img/classic/minus.gif" style="margin-left:4px">
												<xsl:attribute name="id">imgcategory<xsl:value-of select="$num"/></xsl:attribute>
											</img>
										</a>
										<b style="font-family:arial; font-size:0.9em; margin-left:5px">											
											<xsl:value-of select="@appid" />
										</b>
									</div>
									<div style="display:inline; margin-top:2px">
										<xsl:attribute name="class">outlineEntry<xsl:value-of select="position()"/></xsl:attribute>
										<div class="entrys" style="height:15px; padding-bottom:3px">
											<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif"/>
											<span style="display:inline-block; width:180px;">
												<xsl:if test="$currentservice = 'settings' and $currentapp = @appid">
													<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
												</xsl:if>
												<a style="font-size:12px; vertical-align:5px">
													<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=settings&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
													Settings
												</a>
											</span>
										</div>
										<div class="entrys" style="height:15px; padding-bottom:3px">
											<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif"/>
											<span style="display:inline-block; width:180px;">
												<xsl:if test="$currentservice = 'get_scheduler_list' and $currentapp = @appid">
													<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
												</xsl:if>
												<a style="font-size:12px; vertical-align:5px">
													<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_scheduler_list&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
													Schedule
												</a>
											</span>
										</div>
										<div class="entrys" style="height:15px; padding-bottom:3px">
											<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif"/>
											<span style="display:inline-block; width:200px;">
												<xsl:if test="$currentview = 'activity' and $currentapp = @appid">
													<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
												</xsl:if>
												<a style="font-size:12px; vertical-align:5px">
													<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=view&amp;element=activity&amp;app=<xsl:value-of select="@appid" />&amp;id=activity&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
													Activity
												</a>
											</span>
										</div>
										<img style="margin-left:1px;" src="/SharedResources/img/classic/tree_tee.gif"/>
										<span style=" display:inline-block; vertical-align:5px; width:90%">								
											<a href="">
												<xsl:attribute name="id">acategory<xsl:value-of select="$num"/>role</xsl:attribute>
												<xsl:attribute name="href">javascript:colapseOutlineCategory('<xsl:value-of select="$num"/>role')</xsl:attribute>
												<img src="/SharedResources/img/classic/minus.gif" style="margin-left:2px">
													<xsl:attribute name="id">imgcategory<xsl:value-of select="$num"/>role</xsl:attribute>
												</img>
											</a>
											<font style="font-family:arial; font-size:12px; margin-left:5px; width:50%">											
												Rule
											</font>
										</span>
											<span>
												<xsl:attribute name="class">outlineEntry<xsl:value-of select="$num"/>role</xsl:attribute>												
												<div class="entrys" style="height:15px; padding-bottom:3px">
													<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif"/>
													<img src="/SharedResources/img/classic/tree_tee.gif"/>
													<span style="display:inline-block; width:180px;">
														<xsl:if test="$currentservice = 'get_form_list' and $currentapp = @appid">
															<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
														</xsl:if>
														<a style="font-size:12px; vertical-align:5px">
															<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_form_list&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
															Forms
														</a>
													</span>
												</div>
												<div class="entrys" style="height:15px; padding-bottom:3px">
													<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif"/>
													<img src="/SharedResources/img/classic/tree_tee.gif"/>
													<span style="display:inline-block; width:180px;">
														<xsl:if test="$currentservice = 'get_pages_list' and $currentapp = @appid">
															<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
														</xsl:if>
														<a style="font-size:12px; vertical-align:5px">
															<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_pages_list&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
															Pages
														</a>
													</span>
													</div>
													<div class="entrys" style="height:15px; padding-bottom:3px">
														<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif"/>
														<img  src="/SharedResources/img/classic/tree_corner.gif"/>
														<span style="display:inline-block; width:180px;">
															<xsl:if test="$currentservice = 'get_handler_list' and $currentapp = @appid">
																<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
															</xsl:if>
															<a style="font-size:12px; vertical-align:5px">
																<xsl:attribute name="href">Provider?type=get_outline&amp;&amp;subtype=get_handler_list&amp;id=handler&amp;app=<xsl:value-of
																	select="@appid"/></xsl:attribute>
																	Handlers
															</a>
														</span>
													</div>
												</span>
												<xsl:for-each select="database">
													<img style="margin-left:1px" src="/SharedResources/img/classic/tree_corner.gif"/>
													<span style=" vertical-align:5px; display:inline-block; width:90%">								
														<a href="">
															<xsl:attribute name="id">acategory<xsl:value-of select="$num"/>database</xsl:attribute>
															<xsl:attribute name="href">javascript:colapseOutlineCategory('<xsl:value-of select="$num"/>database')</xsl:attribute>
															<img src="/SharedResources/img/classic/minus.gif" style="margin-left:2px">
																<xsl:attribute name="id">imgcategory<xsl:value-of select="$num"/>database</xsl:attribute>
															</img>
														</a>
														<font style="font-family:arial; font-size:12px; margin-left:5px">	
															Database&#xA0;
														</font>
														<font style="font-family:verdana; font-size:0.8em">
															<xsl:value-of select="@dbid"/>
														</font>
													</span>
													<span>
														<xsl:attribute name="class">outlineEntry<xsl:value-of select="$num"/>database</xsl:attribute>
														<div class="entrys" style="height:15px; padding-bottom:3px">
															<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
															<img  src="/SharedResources/img/classic/tree_tee.gif"/>
															<span style="display:inline-block; width:180px;">
																<xsl:if test="$currentservice = 'get_doc_list' and $currentview ='maindoc' and $currentapp = @appid">
																	<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
																</xsl:if>	
																<a style="font-size:12px; vertical-align:5px">
																	<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_doc_list&amp;id=maindoc&amp;dbid=<xsl:value-of
																		select="@dbid"/>&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
																		Documents
																</a>
															</span>
														</div>														
														<div class="entrys" style="height:15px; padding-bottom:3px">
															<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
															<img src="/SharedResources/img/classic/tree_tee.gif"/>
															<span style="display:inline-block; width:180px;">
																<xsl:if test="$currentservice = 'get_doc_list' and $currentview ='structure' and $currentapp = @appid">
																	<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
																</xsl:if>	
																<a style="font-size:12px; vertical-align:5px">
																	<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_doc_list&amp;id=structure&amp;dbid=<xsl:value-of
																		select="@dbid"/>&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
																		Structure
																</a>
															</span>
														</div>
														<div class="entrys" style="height:15px; padding-bottom:3px">
															<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
															<img src="/SharedResources/img/classic/tree_tee.gif"/>
															<span style="display:inline-block; width:180px;">
																<xsl:if test="$currentservice = 'get_doc_list' and $currentview ='glossary' and $currentapp = @appid">
																	<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
																</xsl:if>	
																<a style="font-size:12px; vertical-align:5px">
																	<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_doc_list&amp;id=glossary&amp;dbid=<xsl:value-of
																	select="@dbid"/>&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
																	Glossaries
																</a>
															</span>
														</div>
														<div class="entrys" style="height:15px; padding-bottom:3px">
															<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
															<img src="/SharedResources/img/classic/tree_corner.gif"/>
															<span style="display:inline-block; width:180px;">
																<xsl:if test="$currentservice = 'get_doc_list' and $currentview ='groups' and $currentapp = @appid">
																	<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
																</xsl:if>	
																<a style="font-size:12px; vertical-align:5px">
																	<xsl:attribute name="href">Provider?type=get_outline&amp;subtype=get_doc_list&amp;element=groups&amp;id=groups&amp;dbid=<xsl:value-of
																	select="@dbid"/>&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
																	Groups
																</a>
															</span>
														</div>
													</span>
												</xsl:for-each>
										</div>
								</xsl:for-each>
						</div>
					</div>
				
					<span id="view" class="viewframe{outline/category[entry[@current=1]]/@id}">
					</span>
					
				</div>
				<div id="footer">
					<div style=" padding:2px 10px 0px 10px; color: #444444; width:600px; margin-top:3px; float:left">
						<a target="_parent"  href="Logout" title="{outline/fields/logout/@caption}">
							<img src="img/logout.gif" style="width:15px; height:15px" alt=""/>						
							<font style="margin-left:5px; font-size:11px; vertical-align:3px">logout</font> 
						</a>&#xA0;
					</div>
					<div style=" padding:5px 20px 0px 10px; font-color: #444444; width:300px; float:right">
						<div id="langview" style="font-size:12px; float:right">
							<font style="vertical-align:2px">NextBase © 2012</font>
						</div>
					</div>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>