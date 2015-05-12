<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/action.xsl"/>	
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	 doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:template match="/request">
		<head>
			<title>
				 <xsl:value-of select="concat('Administrator - Handler ',rule/@id)"/>
			</title>
			<link rel="stylesheet" href="css/main.css"/>
			<link rel="stylesheet" href="css/form.css"/>
			<link rel="stylesheet" href="css/dialogs.css"/>
			<script type="text/javascript" src="scripts/jquery/js/jquery.min.js"/>
			<script type="text/javascript" src="scripts/jquery/js/jquery.ui.min.js"/>
			<script type="text/javascript" src="scripts/jquery/js/jquery-1.4.2.js"/>
			<script type="text/javascript" src="scripts/outline.js"/>
			<script type="text/javascript" src="scripts/service.js"/>
			<script type="text/javascript" src="scripts/dialogs.js"/>
			<script type="text/javascript" src="scripts/form.js"/>
		</head>
		<body>
			<table width="100%">
				<tr>
					<td width="50%">						
						<a style="margin-left:1em; margin-right:15px">
							<xsl:attribute name="href">javascript:saveHandler()</xsl:attribute>
							<img src="img/save.gif"/>
							<font class="button">Save &amp; Close</font>
						</a>
						<xsl:call-template name="showasxml_action"/>
					</td>
					<td  style="text-align:right">
						<xsl:call-template name="close_action"/>				
					</td>
				</tr>
			</table>		
			<br/>
			<font style="font-family:verdana; font-size:20px;">Обработчик:&#xA0;</font>
			<xsl:value-of select="rule/@id"/>
			<hr/>
			<form action="Provider" method="post" id="scriptest" enctype="application/x-www-form-urlencoded">
				<table border="0" style="margin-top:10px;width:80%">
					<tr>
						<td class="fc">Description:&#xA0;</td>
						<td>
							<textarea name="description" id="description" rows="2" cols="50">
								<xsl:value-of select="rule/description"/>
							</textarea>
						</td>
					</tr>
					<tr>
						<td class="fc">Run on behalf of:&#xA0;</td>
						<td>
							<select size="1" name="rununderuser" style="margin-top:4px">
								<option>static</option>
								<option>script</option>
							</select>
							&#xA0;
							<input type="text" name="rununderuser_source" size="15">
								<xsl:attribute name="value">observer</xsl:attribute>
							</input>
						</td>
					</tr>
					<tr>
						<td class="fc">Trigger:&#xA0;</td>
						<td>
							<select size="1" name="trigger" style="margin-top:4px" onchange="javascript:trigerUpdate(this)">
								<option value="PROVIDER">
									<xsl:if test="rule/trigger = 'PROVIDER'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									PROVIDER
								</option>
								<option value="PATCH">
									<xsl:if test="rule/trigger = 'PATCH'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									PATCH
								</option>
								<option value="MANUALLY">
									<xsl:if test="rule/trigger = 'MANUALLY'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									MANUALLY
								</option>
								<option value="SCHEDULER">
									<xsl:if test="rule/trigger = 'SCHEDULER'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									SCHEDULER
								</option>
							</select>
						</td>
					</tr>
					
						<tr class="sheduler">
							<xsl:if test="rule/trigger != 'SCHEDULER'">
								<xsl:attribute name="style">display:none</xsl:attribute>
							</xsl:if>
							<td class="fc">Start time:</td>
							<td>
								<input type="text" name="starttime" size="15" onchange="javascript:nextStartUpdate(this)">
									<xsl:attribute name="value"><xsl:value-of select="rule/sheduler/starttime"/></xsl:attribute>
								</input>
							</td>
						</tr>
						<tr class="sheduler">
							<xsl:if test="rule/trigger != 'SCHEDULER'">
								<xsl:attribute name="style">display:none</xsl:attribute>
							</xsl:if>
							<td class="fc">Next start:&#xA0;</td>
							<td>
								<div style="width:200px; border:1px solid #dedede; height:22px" id="nextStart">
									&#xA0;<xsl:value-of select="rule/sheduler/nextstart"/>
								</div>
							</td>
						</tr>
						<tr>
							<td class="fc">To handle source:&#xA0;</td>
							<td>
								<select size="1" id="tohandlesource" name="tohandlesource" style="margin-top:4px">
								<xsl:attribute name="onchange">javascript:handlesorceUpdate('<xsl:value-of select="rule/@app"/>')</xsl:attribute>
								<option value="MACRO">
									<xsl:if test="rule/tohandle/@source = 'MACRO'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									MACRO
								</option>
								<option value="QUERY">
									<xsl:if test="rule/tohandle/@source = 'QUERY'">
										<xsl:attribute name="selected">selected</xsl:attribute>
									</xsl:if>
									QUERY
								</option>
							</select>
							</td>
						</tr>
						<tr>
							<td class="fc">To handle value:&#xA0;</td>
							<td>
								<select size="1" id="tohandlevalue" name="tohandlevalue" style="margin-top:4px">
									<xsl:if test="rule/tohandle/@source != 'QUERY' or not(rule/tohandle/@source)  ">
										<option value="ALLTASKS">
											<xsl:if test="rule/tohandle = 'ALLTASKS'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											ALLTASKS
										</option>
										<option value="DOCUMENTS">
											<xsl:if test="rule/tohandle = 'DOCUMENTS'">
												<xsl:attribute name="selected">selected</xsl:attribute>
											</xsl:if>
											DOCUMENTS
										</option>
									</xsl:if>
								</select>
								<xsl:if test="rule/tohandle/@source = 'QUERY'">
									<script>
										$("#tohandlevalue option").remove()
										$.ajax({
											url: 'Provider?type=get_query_list&amp;app=<xsl:value-of select="rule/@app"/>&amp;onlyxml',
											datatype:'xml',
											success: function(data) {
												maxpage = $(data).find("query").attr("maxpage");
												i=1
												k=2
												if(maxpage &gt; i ){
													$(data).find("entry[doctype=queryrule]").each(function(index, element){
														 <![CDATA[ $("#tohandlevalue").append("<option value='"+ $(element).attr("docid")+"'>"+ $(element).attr("docid") +"</option>")]]>
													});
													while( i&lt;maxpage){
														$.ajax({
															url: 'Provider?type=get_query_list&amp;app=<xsl:value-of select="rule/@app"/>&amp;page='+k+'&amp;onlyxml',
															datatype:'xml',
															success: function(data) {
																$(data).find("entry[doctype=queryrule]").each(function(index, element){
																	 <![CDATA[ $("#tohandlevalue").append("<option value='"+ $(element).attr("docid")+"'>"+ $(element).attr("docid") +"</option>")]]>
																});
															}
														});	
														i++;
														k++;
													}
												}else{
													$(data).find("entry[doctype=queryrule]").each(function(index, element){
														 <![CDATA[ $("#tohandlevalue").append("<option value='"+ $(element).attr("docid")+"'>"+ $(element).attr("docid") +"</option>")]]>
													});
												}
											}
										});	
									</script>
								</xsl:if>
							</td>
						</tr>
					
					<tr>
						<td class="fc">Script&#xA0;:</td>
						<td>
							<textarea name="script" id="script" rows="20" cols="100">
								<xsl:value-of select="rule/script"/>
							</textarea>&#xA0;
							<a style="margin-left:10" title="попробовать...">
								<xsl:attribute name="href">javascript:execute('scriptest');</xsl:attribute>
								<img src="img/force.gif"/>
							</a>
						</td>
					</tr>
					<tr>
						<td class="fc">Console &#xA0;:</td>
						<td>
							<div id="console" name="console" style="border:solid 1px gray; height:200px; padding:5px; overflow:auto;"/>
						</td>
					</tr>
					<tr>
						<td class="fc">Result &#xA0;:</td>
						<td>
							<div id="result" name="result" style="border:solid 1px gray; min-height:50px"/>
						</td>
					</tr>
					
					<tr>
						<td class="fc"></td>
						<td>
							<a style="margin-right:10" target="_blank" >
								<xsl:attribute name="href">/html/doc/</xsl:attribute>
								Помощь...
							</a>
						</td>
					</tr>
				</table>
				&#xA0;
							
				<!-- Скрытые поля -->
				<input type="hidden" name="type" id="type" value="save" />
				<input type="hidden" name="element" id="element" value="handler_rule"/>
				<input type="hidden" name="app" id="app" value="{rule/@app}"/>
				<input type="hidden" name="id" id="id" value="{rule/@id}"/>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>