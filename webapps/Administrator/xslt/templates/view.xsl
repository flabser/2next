<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="html-head-jscss.xsl"/>

	<xsl:template name="outline">
		<xsl:variable name="currentapp" select="document/application | //request/@dbid"/>
		<xsl:variable name="currentview" select="//request/@id"/>
		<xsl:variable name="currentservice" select="$currentview"/>

		<div class="layout-header">
			<div class="layout-header-container">
				<h4 class="app-title">Administrator</h4>
			</div>
		</div>
		<div class="layout-sidebar">
			<div class="layout-sidebar-container">
				<ul class="sidebar-nav sidebar-nav-list">
					<!-- <li class="nav-item">
						<xsl:if test="currentview/@service = 'console'">
							<xsl:attribute name="class">nav-active</xsl:attribute>
						</xsl:if>
						<a class="globalentry">
							<xsl:attribute name="href">Provider?type=view&amp;element=console</xsl:attribute>
							Console
						</a>
					</li> -->
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
					<xsl:variable name="num" select="position()"/>
					<div class="sidebar-nav">
						<a id="outlineEntry{position()}" class="collapse outline-ch">
							<!-- <xsl:attribute name="id">acategory<xsl:value-of select="$num"/></xsl:attribute> -->
							<xsl:attribute name="href">#</xsl:attribute>
							<img src="/SharedResources/img/classic/minus.gif" style="margin-left:6px">
								<xsl:attribute name="id" select="concat('imgcategory',$num)"/>
							</img>
							<b style="color:#333;font-size:.9em;margin-left:5px">											
								<xsl:value-of select="@appid"/>
							</b>
						</a>
						<div class="section expand outlineEntry{position()}">
							<div class="entrys">
								<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif"/>
								<span style="display:inline-block; width:180px;">
									<xsl:if test="$currentservice = 'settings' and $currentapp = @appid">
										<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
									</xsl:if>
									<a style="vertical-align:5px">
										<xsl:attribute name="href">Provider?type=view&amp;element=settings&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
										Settings
									</a>
								</span>
							</div>
							<div class="entrys">
								<img style="margin-left:1px" src="/SharedResources/img/classic/tree_tee.gif"/>
								<span style="display:inline-block; width:200px;">
									<xsl:if test="$currentview = 'activity' and $currentapp = @appid">
										<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
									</xsl:if>
									<a style="vertical-align:5px">
										<xsl:attribute name="href">Provider?type=view&amp;element=activity&amp;app=<xsl:value-of select="@appid"/>&amp;id=activity&amp;dbid=<xsl:value-of select="@dbid" /></xsl:attribute>
										Activity
									</a>
								</span>
							</div>
							<img style="margin-left:1px;" src="/SharedResources/img/classic/tree_tee.gif"/>
							<span style=" display:inline-block; vertical-align:5px; width:90%">								
								<a>
									<xsl:attribute name="id">acategory<xsl:value-of select="$num"/>role</xsl:attribute>
									<xsl:attribute name="href">javascript:colapseOutlineCategory('<xsl:value-of select="$num"/>role')</xsl:attribute>
									<img src="/SharedResources/img/classic/minus.gif" style="margin-left:2px">
										<xsl:attribute name="id" select="'imgcategory',$num,'role'"/>
									</img>
									<font style="color:#333;font-size:.7em;margin-left:5px;">											
										Rule
									</font>
								</a>
							</span>
							<span class="outlineEntry{$num}role">
								<div class="entrys">
									<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif"/>
									<img  src="/SharedResources/img/classic/tree_tee.gif"/>
									<span style="display:inline-block; width:180px;">
										<xsl:if test="$currentservice = 'forms' and $currentapp = @appid">
											<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
										</xsl:if>
										<a style="vertical-align:5px">
											<xsl:attribute name="href">Provider?type=view&amp;element=forms&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
											Forms
										</a>
									</span>
								</div>
								<div class="entrys">
									<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif"/>
									<img  src="/SharedResources/img/classic/tree_tee.gif"/>
									<span style="display:inline-block; width:180px;">
										<xsl:if test="$currentservice = 'pages' and $currentapp = @appid">
											<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
										</xsl:if>
										<a style="vertical-align:5px">
											<xsl:attribute name="href">Provider?type=view&amp;element=pages&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
											Pages
										</a>
									</span>
								</div>
								<div class="entrys">
									<img style="margin-left:1px" src="/SharedResources/img/classic/tree_bar.gif"/>
									<img  src="/SharedResources/img/classic/tree_corner.gif"/>
									<span style="display:inline-block; width:180px;">
										<xsl:if test="$currentservice = 'get_handler_list' and $currentapp = @appid">
											<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
										</xsl:if>
										<a style="vertical-align:5px">
											<xsl:attribute name="href">Provider?type=view&amp;element=handlers&amp;app=<xsl:value-of select="@appid"/></xsl:attribute>
											Handlers
										</a>
									</span>
								</div>
							</span>
							<xsl:for-each select="database">
								<img style="margin-left:1px" src="/SharedResources/img/classic/tree_corner.gif"/>
								<span style="vertical-align:5px; display:inline-block; width:90%">								
									<a href="">
										<xsl:attribute name="id">acategory<xsl:value-of select="$num"/>database</xsl:attribute>
										<xsl:attribute name="href">javascript:colapseOutlineCategory('<xsl:value-of select="$num"/>database')</xsl:attribute>
										<img src="/SharedResources/img/classic/minus.gif" style="margin-left:2px">
											<xsl:attribute name="id" select="concat('imgcategory',$num,'database')"/>
										</img>
										<font style="color:#333;font-size:.7em;margin-left:5px">	
											Database&#xA0;
										</font>
									</a>
									<em><small><xsl:value-of select="@dbid"/></small></em>
								</span>
								<span class="outlineEntry{$num}database">
									<div class="entrys">
										<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
										<img src="/SharedResources/img/classic/tree_tee.gif"/>
										<span style="display:inline-block; width:180px;">
											<xsl:if test="$currentview ='documents' and $currentapp = @appid">
												<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
											</xsl:if>	
											<a style="vertical-align:5px">
												<xsl:attribute name="href">Provider?type=view&amp;element=documents&amp;dbid=<xsl:value-of select="@dbid" />&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
												Documents
											</a>
										</span>
									</div>							
									<div class="entrys">
										<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
										<img src="/SharedResources/img/classic/tree_corner.gif"/>
										<span style="display:inline-block; width:180px;">
											<xsl:if test="$currentview ='glossaries' and $currentapp = @appid">
												<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
											</xsl:if>	
											<a style="vertical-align:5px">
												<xsl:attribute name="href">Provider?type=view&amp;element=glossaries&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
												Glossaries
											</a>
										</span>
									</div>
									<!-- <div class="entrys">
										<img style="margin-left:1px" src="/SharedResources/img/classic/tree_spacer.gif"/>
										<img src="/SharedResources/img/classic/tree_corner.gif"/>
										<span style="display:inline-block; width:180px;">
											<xsl:if test="$currentservice = 'get_doc_list' and $currentview ='groups' and $currentapp = @appid">
												<xsl:attribute name="style">display:inline-block; width:180px; background:#DDE0EC</xsl:attribute>
											</xsl:if>	
											<a style="vertical-align:5px">
												<xsl:attribute name="href">Provider?type=view&amp;element=groups&amp;dbid=<xsl:value-of
												select="@dbid" />&amp;app=<xsl:value-of select="@appid" /></xsl:attribute>
												Groups
											</a>
										</span>
									</div> -->
								</span>
							</xsl:for-each>
						</div>
					</div>
				</xsl:for-each>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="footer">	
		<div class="layout-footer">
			<div class="layout-footer-container">
				<a href="Logout" title="{outline/fields/logout/@caption}" class="link-logout" style="font-size:.6em;">
					<img src="img/logout.gif" style="vertical-align:middle;height:15px;width:15px;" alt=""/>
					logout
				</a>
				<div style="float:right;">
					<div class="copy" style="font-size:.6em;">NextBase © 2014</div>
				</div>
			</div>
		</div>
	</xsl:template>

	<xsl:template name="viewstat">
		<div style="margin-top:5px;font-size:.7em">
			<table style="width:100%;">
				<tr>
					<td style="width:15%; font-size:12px">
						<xsl:value-of select="concat('Page: ',query/@currentpage,' of ', query/@maxpage)"/>
					</td>
					<td>
						<xsl:call-template name="prepagelist"/>
					</td>
					<td style="text-align:right;font-size:12px;padding-right:10px;width:15%">
						Total: <xsl:value-of select="query/@count"/>
					</td>
				</tr>
			</table>
		</div>
	</xsl:template>

	<xsl:template name="pagenavigator">
		<xsl:param name="currentpage" select="query/@currentpage"/>
	  	<xsl:param name="maxpage" select="query/@maxpage"/>
	  	<xsl:param name="nextpage" select="$currentpage + 1"/>
	  	<xsl:param name="nextpage10" select="$nextpage + 9"/>
	  	<xsl:param name="prevpage" select="$currentpage - 1"/>
	  	<xsl:param name="prevpage10" select="$prevpage - 9"/>	
	  	<xsl:param name="maxpage10" select="$maxpage - 9"/>
    	<xsl:choose>
 			<xsl:when test="$prevpage10&lt;10">
 				&lt;x10 					
 			</xsl:when>
 			<xsl:otherwise> 				
      			<a>      
      				<xsl:attribute name="title" select="concat('jump to ', $prevpage10)"/>
      				<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="$doctype"/>&amp;page=<xsl:value-of select="$prevpage10"/>&amp;dbid=<xsl:value-of select="query/@dbid" /></xsl:attribute>
      				&lt;x10
      			</a>
      		</xsl:otherwise>
      	</xsl:choose>
      	&#xA0;
      	<xsl:choose>
 			<xsl:when test="$prevpage=0"> 				
 				&lt;
 			</xsl:when>
 			<xsl:otherwise> 			
      			<a>
      				<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="$doctype"/>&amp;page=<xsl:value-of select="$prevpage"/>&amp;dbid=<xsl:value-of select="query/@dbid" /></xsl:attribute>
      				&lt;
      			</a>
   			</xsl:otherwise>
   		</xsl:choose>
      	&#xA0;&#xA0;
   		 <xsl:choose>
			<xsl:when test="$currentpage=$maxpage">
				&gt;
			</xsl:when>
			<xsl:otherwise>
 				<a>
      				<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="$doctype"/>&amp;page=<xsl:value-of select="$nextpage"/>&amp;dbid=<xsl:value-of select="query/@dbid" /></xsl:attribute>
      				&gt;
				</a>
      		</xsl:otherwise>
      	</xsl:choose>
      	&#xA0;
   		<xsl:choose>
 			<xsl:when test="$nextpage10 > $maxpage10">
 				<a>
      				<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="$doctype"/>&amp;page=<xsl:value-of select="$nextpage10"/>&amp;dbid=<xsl:value-of select="query/@dbid" /></xsl:attribute>
      				<xsl:attribute name="title" select="concat('jump to ',$nextpage10)"/>
      				x10&gt;
				</a> 					 					
 			</xsl:when>
 			<xsl:otherwise>
 				x10&gt;
      		</xsl:otherwise>
      	</xsl:choose>
  </xsl:template>
  
  <xsl:template name="prepagelist">
	<xsl:if test="query/@maxpage !=1">
		<table style="margin-top:10px; margin:0 auto;">
			<xsl:variable name="curpage" select="query/@currentpage"/>
			<xsl:variable name="prevpage" select="$curpage -1 "/>
			<xsl:variable name="beforecurview" select="substring-before(@id,'.')"/> 
           	<xsl:variable name="aftercurview" select="substring-after(@id,'.')"/> 
			<tr>
				<xsl:if test="query/@currentpage>1">
					<td>
						<a href="">
							<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@element"/>&amp;page=1&amp;dbid=<xsl:value-of select="query/@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
							<font style="font-size:12px">&lt;&lt;</font>
						</a>&#xA0;
					</td>
					<td>
						<a href="">
							<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@element"/>&amp;page=<xsl:value-of select="$prevpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
							<font style="font-size:12px">&lt;</font>
						</a>&#xA0;
					</td>
				</xsl:if>
				<xsl:call-template name="pagenavig"/>
					<xsl:if test="query/@maxpage > 15">
						<xsl:variable name="beforecurview" select="substring-before(@id,'.')"/> 
                		<xsl:variable name="aftercurview" select="substring-after(@id,'.')"/> 
						<td>
							<select style="margin-left:5px">
								<xsl:attribute name="onChange">javascript:window.location.href='Provider?type=view&amp;element=<xsl:value-of select="$doctype"/>&amp;page=this.value&amp;dbid=<xsl:value-of select="@dbid" />'</xsl:attribute>
			 					<xsl:call-template name="combobox"/>
			 				</select>
			 			</td>
					</xsl:if>
				</tr>
			</table>
		</xsl:if>
	</xsl:template>
	
	<xsl:template name="pagenavig">
 		<xsl:param name="i" select="1"/>  <!-- счетчик количества страниц отображаемых в навигаторе  -->
 		<xsl:param name="n" select="15"/> <!-- количество страниц отображаемых в навигаторе -->
  		<xsl:param name="z" select="query/@maxpage -14"/>
  		<xsl:param name="f" select="15"/>
 		<xsl:param name="c" select="query/@currentpage"/> <!-- текущая страница в виде -->
 		<xsl:param name="startnum" select="query/@currentpage - 7"/> 
  		<xsl:param name="d" select="query/@maxpage - 14"/>	<!-- переменная для вычисления начального номера страницы в навигаторе  -->
  		<xsl:param name="currentpage" select="query/@currentpage"/>
  		<xsl:param name="maxpage" select="query/@maxpage"/>
  		<xsl:param name="nextpage" select="$currentpage + 1"/>
  		<xsl:param name="prevpage" select="$currentpage - 1"/>
  		<xsl:param name="curview" select="@id"/> 
  		<xsl:param name="direction" select="query/@direction"/> 
  		<xsl:param name="app" select="@dbid"/> 
  		<xsl:param name="dbid" select="@dbid"/> 
  		<xsl:param name="type" select="@type"/> 
		<xsl:choose>
			<xsl:when test="$maxpage>15">
				<xsl:choose>
					<xsl:when test="$maxpage - $currentpage &lt; 7">
						<xsl:if test="$i != $n+1">
							<xsl:if test="$z &lt; $maxpage + 1">
								<td>
									<a href="">
   										<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$z"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
   			 								<font style="font-size:12px">
    											<xsl:if test="$z=$currentpage">
    												<xsl:attribute name="style">font-weight:bold;font-size:18px</xsl:attribute>
    											</xsl:if>
    											<xsl:value-of select="$z"/>
    										</font>
   									</a>&#xA0;
								</td>
							</xsl:if>
      						<xsl:call-template name="pagenavig">
	        					<xsl:with-param name="i" select="$i + 1"/>
	        					<xsl:with-param name="n" select="$n"/>
	        					<xsl:with-param name="z" select="$z+1"/>
      						</xsl:call-template>
						</xsl:if>
						<xsl:if test="$currentpage != $maxpage">
							<xsl:if test="$i = $n+1">
		 						<td>
     								<a href="">
     									<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$nextpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
      									<font style="font-size:12px"> > </font>
      								</a>&#xA0;
      							</td>
       							<td>
       								<a href="">
       								<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$maxpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
       									<font style="font-size:12px"> >> </font>
       								</a> &#xA0;
						        </td>
							</xsl:if>
   						</xsl:if>
					</xsl:when>
					<xsl:otherwise>
						<xsl:choose>
							<xsl:when test="$currentpage &lt; 7">
								<xsl:if test="$i=1">
									<xsl:if test="$currentpage = 1">
										<td>
											&#xA0;&#xA0;&#xA0;&#xA0;	
										</td>
										<td>
											&#xA0;&#xA0;&#xA0;
										</td>
									</xsl:if>
								</xsl:if>
								<xsl:if test="$i != $n+1">
									<xsl:if test="$i &lt; $maxpage + 1">
										<td>
											<a href="">
											<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$i"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
												<font style="font-size:12px">
    												<xsl:if test="$i=$currentpage">
    													<xsl:attribute name="style">font-weight:bold;font-size:18px</xsl:attribute>
    												</xsl:if>
    												<xsl:value-of select="$i"/>
    											</font>
						   					</a>&#xA0;
										</td>
									</xsl:if>
      								<xsl:call-template name="pagenavig">
	        							<xsl:with-param name="i" select="$i + 1"/>
	        							<xsl:with-param name="n" select="$n"/>
	        							<xsl:with-param name="c" select="$c+1"/>
      								</xsl:call-template>
      							</xsl:if>
							</xsl:when>
							<xsl:otherwise>
								<xsl:if test="$i != $n+1">
									<xsl:if test="$i &lt; $maxpage + 1">
										<xsl:if test="$startnum !=0">
											<td>
												<a href="">
													<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$startnum"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
													<font style="font-size:12px">
    													<xsl:if test="$startnum=$currentpage">
    														<xsl:attribute name="style">font-weight:bold;font-size:18px</xsl:attribute>
    													</xsl:if>
    													<xsl:value-of select="$startnum"/>
    												</font>
						   						</a>&#xA0;
											</td>
										</xsl:if>
									</xsl:if>
									<xsl:if test="$startnum != 0">
      									<xsl:call-template name="pagenavig">
											<xsl:with-param name="i" select="$i + 1"/>
	        								<xsl:with-param name="n" select="$n"/>
	        								<xsl:with-param name="c" select="$c+1"/>
	        								<xsl:with-param name="startnum" select="$c - 6"/>
      									</xsl:call-template>
      								</xsl:if>
									<xsl:if test="$startnum = 0">
      									<xsl:call-template name="pagenavig">
										<xsl:with-param name="i" select="$i"/>
	        							<xsl:with-param name="n" select="$n"/>
	        							<xsl:with-param name="c" select="$c+1"/>
	        							<xsl:with-param name="startnum" select="$c - 6"/>
      								</xsl:call-template>
      							</xsl:if>
      						</xsl:if>
						</xsl:otherwise>
					</xsl:choose>
						<xsl:if test="$currentpage != $maxpage">
							<xsl:if test="$i = $n+1">
		 						<td>
      								<a href="">
      									<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$nextpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
     									<font style="font-size:12px"> ></font>
     								</a>&#xA0;
							    </td>
       							<td>
       								<a href="">
       									<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$maxpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
       									<font style="font-size:12px"> >></font>
       								</a> 
							    </td>
							</xsl:if>
  						</xsl:if>
						
					</xsl:otherwise>
				</xsl:choose>
			</xsl:when>
			<xsl:otherwise>
				<xsl:if test="$i=1">
					<xsl:if test="$currentpage = 1">
						<td>
							&#xA0;&#xA0;&#xA0;&#xA0;	
						</td>
						<td>
							&#xA0;&#xA0;&#xA0;
						</td>
					</xsl:if>
				</xsl:if>
				<xsl:if test="$i != $n+1">
					<xsl:if test="$i &lt; $maxpage + 1">
						<td>
							<a href="">
								<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@id"/>&amp;page=<xsl:value-of select="$i"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
   			 					<font style="font-size:12px">
    								<xsl:if test="$i=$currentpage">
    									<xsl:attribute name="style">font-weight:bold;font-size:18px</xsl:attribute>
    								</xsl:if>
    								<xsl:value-of select="$i"/>
    							</font>
						    </a>&#xA0;
						</td>
					</xsl:if>
      				<xsl:call-template name="pagenavig">
	        			<xsl:with-param name="i" select="$i + 1"/>
	        			<xsl:with-param name="n" select="$n"/>
	        			<xsl:with-param name="c" select="$c+1"/>
      				</xsl:call-template>
				</xsl:if>
				<xsl:if test="$currentpage != $maxpage">
					<xsl:if test="$i = $n+1">
						<td>
      						<a href="">
      							<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@element"/>&amp;page=<xsl:value-of select="$nextpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
      							<font style="font-size:12px"> > </font>
      						</a>&#xA0;
					    </td>
       					<td>
       						<a href="">
       							<xsl:attribute name="href">Provider?type=view&amp;element=<xsl:value-of select="@element"/>&amp;page=<xsl:value-of select="$maxpage"/>&amp;dbid=<xsl:value-of select="@dbid"/>&amp;app=<xsl:value-of select="query/@app"/></xsl:attribute>
       							<font style="font-size:12px"> >> </font>
       						</a> &#xA0;
						</td>
					</xsl:if>
   				</xsl:if>
   			</xsl:otherwise>
  		 </xsl:choose>
 	 </xsl:template>
 	 
 	 <xsl:template name="combobox">
		<xsl:param name="i" select="1"/>
		<xsl:param name="k" select="query/@currentpage"/>
 		<xsl:param name="n" select="query/@maxpage + 1"/>
		<xsl:choose>
			<xsl:when test="$n > $i">
				<option value="{$i}">
 					<xsl:if test="$k=$i">
 						<xsl:attribute name="selected">true</xsl:attribute>
 					</xsl:if>
 					<xsl:value-of select="$i"/>
 				</option>
				<xsl:call-template name="combobox">
	        		<xsl:with-param name="i" select="$i + 1"/>
	        		<xsl:with-param name="n" select="$n"/>
	        		<xsl:with-param name="k" select="query/@currentpage"/>
	        	</xsl:call-template>
		 	</xsl:when>
 		</xsl:choose>
	 </xsl:template>
</xsl:stylesheet>