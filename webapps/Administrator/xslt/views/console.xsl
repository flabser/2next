<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	 doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>

	<xsl:template match="/request">
		<html>
			<head>
				<link rel="stylesheet" href="css/main.css" />
				<meta http-equiv="Content-Type" content="text/html; charset=windows-1251"/>
				<title>Administrator</title>
			</head>
			<body style="background-image: url(img/f.gif);">
				<div style="height:600px;">
					<APPLET CODE="kz.pchelka.console.rmi.client.RemoteConsole" CODEBASE="" ARCHIVE="RemoteConsole-signed.jar" WIDTH="90%" HEIGHT="95%">
						<param name="UID">
							<xsl:attribute name="value" select="console/@sesid"/>
						</param>
						<param name="SERVER" value="NextBase">
							<xsl:attribute name="value" select="console/@server"/>
						</param>
						<param name="PORT" value="1099">
							<xsl:attribute name="value" select="console/@port"/>
						</param>
						<param name="ARCHIVE" value="RemoteConsole-signed.jar"/>
					</APPLET>
				</div>
				<div id="footer">
					<div style=" padding:2px 10px 0px 10px; color: #444444; width:600px; margin-top:3px; float:left">
						<a target="_parent"  href="Logout" title="{outline/fields/logout/@caption}">
							<img src="img/logout.gif" style="width:15px; height:15px"/>						
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