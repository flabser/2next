<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/action.xsl" />
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	 doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:template match="/request">
		<html>
			<head>
				<title>Administrator - document</title>
				<link type="text/css" rel="stylesheet" href="css/main.css"/>
				<link type="text/css" rel="stylesheet" href="css/view.css"/>
				<link type="text/css" rel="stylesheet" href="css/dialogs.css"/>
				<link type="text/css" href="/SharedResources/jquery/css/base/jquery.ui.all.css" rel="stylesheet"/>
				<link type="text/css" href="/SharedResources/jquery/css/base/jquery-ui-1.8.2.redmont.css" rel="stylesheet"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/jquery-1.4.2.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.core.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.widget.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.mouse.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.draggable.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.position.js"/>
				<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.dialog.js"/>
				<script type="text/javascript" src="scripts/outline.js"/>
				<script type="text/javascript" src="scripts/service.js"/>
				<script type="text/javascript" src="scripts/dialogs.js"/>
				<script type="text/javascript" src="scripts/form.js"/>
			</head>
			<body>
				<table width="100%">
					<tr>
						<td width="50%">
							<xsl:call-template name="showasxml_action"/>
							<a style="margin-left:10px">
								<xsl:attribute name="href">javascript:saveDocument('frm','frm')</xsl:attribute>
								<img src="img/save.gif"/>
								<font class="button">Save &amp; Close</font>
							</a>
							<a style="margin-left:10px">
								<xsl:attribute name="href">Provider?type=view&amp;element=document_activity&amp;doctype=<xsl:value-of select="document/@doctype"/>&amp;docid=<xsl:value-of select="document/@docid"/>&amp;app=<xsl:value-of select="document/@dbid"/></xsl:attribute>
								<img src="img/history.png"/>
								<font class="button">Activity</font>
							</a>
						</td>
						<td style="text-align:right">
							<xsl:call-template name="close_action"/>
						</td>
					</tr>
				</table>
				<br/>				
				<font style="font-family:verdana; font-size:20px;">
					<xsl:value-of select="document/docid"/>
				</font>
				<br/>
				<br/>
				<b>System fields</b>
				<table style="font-family:verdana; margin-top:10px; width:90%; font-size:0.8em; margin-left:10px">
					<tr>
						<td width="10%" class="th">name</td>
						<td width="53%" class="th">value</td>
						<td width="12%" class="th">type</td>
					</tr>					
					<tr>
						<td style="text-align:right; font-weight:bold;">docid</td>
						<td>
							&#xA0;
							<xsl:value-of select="document/docid"/>
						</td>
						<td>system</td>
					</tr>					
					<tr>
						<td style="text-align:right; font-weight:bold;">author</td>
						<td>
							&#xA0;<xsl:value-of select="document/author"/>
						</td>
						<td>system</td>
					</tr>
					<tr>
						<td style="text-align:right; font-weight:bold;">regdate</td>
						<td>
							&#xA0;<xsl:value-of select="document/regdate"/>
						</td>
						<td>system</td>
					</tr>
					<tr>
						<td style="text-align:right; font-weight:bold;">lastupdate</td>
						<td>
							&#xA0;<xsl:value-of select="document/lastupdate"/>
						</td>
						<td>system</td>
					</tr>
					<tr>
						<td style="text-align:right ; font-weight:bold;">form</td>
						<td>
							&#xA0;<xsl:value-of select="document/form"/>
						</td>
						<td>system</td>
					</tr>					
					<tr>
						<td style="text-align:right; font-weight:bold;">parentdocid</td>
						<td>
							<a target="_parent">
								<xsl:attribute name="href">Provider?type=edit&amp;element=document&amp;id=<xsl:value-of select="document/@parentdocid" />&amp;dbid=<xsl:value-of select="document/@dbid" /></xsl:attribute>
								&#xA0;
								<xsl:value-of select="document/parentdocid"/>								
							</a>
						</td>
						<td>system</td>
					</tr>					
					
				</table>
				<br/>
				<b>Custom fields</b>	
				<table style="font-family:verdana; margin-top:10px; width:90%; font-size:0.8em; margin-left:10px">
					<tr>
						<td width="10%" class="th">name</td>
						<td width="53%" class="th">value</td>
						<td width="12%" class="th">type</td>
					</tr>
					<xsl:for-each select="document/customfields/node()">
						<tr>
							<td style="text-align:right; font-weight:bold;">
								<xsl:value-of select="name(.)"/>
								<input type="hidden">
									<xsl:attribute name="name" select="name(.)"/>
									<xsl:attribute name="value" select="node()"/>
								</input>
							</td>
							<td>
								&#xA0;&#xA0;<xsl:value-of select="node()"/>
							</td>
							<td>
								<xsl:value-of select="@type"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<br/>
				<b>Viewtext fields</b>	
				<table style="font-family:verdana; margin-top:10px; width:90%; font-size:0.8em; margin-left:10px">
					<tr>
						<td width="10%" class="th">name</td>
						<td width="53%" class="th">value</td>
						<td width="12%" class="th">type</td>
					</tr>
					<xsl:for-each select="document/viewtexts/node()">
						<tr>
							<td style="text-align:right ; font-weight:bold;">
								<xsl:value-of select="name(.)"/>
								<input type="hidden">
									<xsl:attribute name="name" select="name(.)"/>
									<xsl:attribute name="value" select="node()"/>
								</input>
							</td>
							<td>
								&#xA0;&#xA0;<xsl:value-of select="node()"/>
							</td>
							<td>
								<xsl:value-of select="@type"/>
							</td>
						</tr>
					</xsl:for-each>
				</table>
				<br/>
				<b>Editors &amp; Readers</b>
				<table style="font-family:verdana; margin-top:10px; width:90%; font-size:0.8em;  margin-left:10px">
					<tr>
						<td width="10%" class="th">type</td>
						<td width="53%" class="th">userid</td>
						<td width="12%" class="th"></td>
					</tr>
					<form action="Provider" name="frm" method="post" id="frm" enctype="application/x-www-form-urlencoded">
						<xsl:for-each select="document/editors/user">
							<tr>
								<td style="text-align:right">
									<img onclick='removetr(this)' style="cursor:pointer" src="img/closesmall.gif"/>
									&#xA0;editor&#xA0;
								</td>
								<td>
									&#xA0;<xsl:value-of select="."/>
									<input type="hidden" name="editor" value="{.}"/>
								</td>
								<td>
								</td>
							</tr>
						</xsl:for-each>
						<tr class="neweditor">
							<td style="text-align:right">&#xA0;<font>editor</font>&#xA0;</td>
							<td>
								<input type="text" size='25' style="margin-left:5px" onkeyup="javascript:autocomplete(this)">
									<xsl:attribute name="name">editor</xsl:attribute>
								</input>&#xA0;<a id="linktofullstructureeditor" href="javascript:selectIDtoField($('.neweditor input'))" style="font-size:12px">Select</a>
							</td>
							<td></td>
						</tr>
						<xsl:for-each select="document/readers/user">
							<tr>
								<td style="text-align:right">
									<img onclick='removetr(this)' style="cursor:pointer" src="img/closesmall.gif"/>
									&#xA0;reader&#xA0;
								</td>
								<td>
									&#xA0;<xsl:value-of select="."/>
									<input type="hidden" name="editor" value="{.}"/>
								</td>
								<td>
								</td>
							</tr>
						</xsl:for-each>
						<tr class="newreader">
							<td style="text-align:right">&#xA0;<font>reader</font>&#xA0;</td>
							<td>
								<input type="text" size='25' name="reader" style="margin-left:5px" onkeyup="javascript:autocomplete(this)">
								&#xA0;<a id="linktofullstructurereader" href="javascript:selectIDtoField($('.newreader input'))" style="font-size:12px">Select</a>
							</td>
							<td></td>
						</tr>
						<input type="hidden" name="type" value="save"/>
						<input type="hidden" name="element" value="document"/>
						<input type="hidden" name="form" value="{document/@form}"/>
						<input type="hidden" name="dbid" value="{document/@dbid}"/>
						<input type="hidden" name="docid" value="{document/@docid}"/>						
						<input type="hidden" name="parentdocid" value="{document/@parentdocid}"/>						
					</form>
				</table>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
				<br/>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>