<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl"/>
	<xsl:import href="../templates/action.xsl"/>
	<xsl:variable name="type">get_doc_list</xsl:variable>
	<xsl:variable name="doctype">rule</xsl:variable>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:variable name="currentapp" select="currentview/@app"/>
	<xsl:variable name="currentview" select="currentview"/>
	<xsl:variable name="currentservice" select="currentview/@service"/>

	<xsl:template match="/request">
		<html>
			<head>
				<title>
					<xsl:value-of select="concat('Administrator',@id)"/>
				</title>
				<xsl:call-template name="view-html-head-jscss"/>
			</head>
			<body>
				<div class="wrapper">
					<div id="blockWindow" style="display:none"/>
					<xsl:call-template name="outline"/>
					<div class="layout-content">
						<div class="layout-content-container">
							<h4 class="view-header-title">Schedule</h4>
							<div class="actionbar">
								<span class="action">
									<xsl:call-template name="refresh_action"/>
									<a style="margin-left:10px">
										<xsl:attribute name="href">javascript:resetRules();</xsl:attribute>
										<img src="img/force.gif"/>
										<font class="button">Reload rules</font>
									</a>
									<a style="margin-left:10px">
										<xsl:attribute name="href">javascript:delMaindoc();</xsl:attribute>
										<img src="img/delete.gif"/>
										<font class="button">Delete</font>
									</a>
								</span>
								<xsl:call-template name="viewstat"/>
							</div>
							<table class="viewtable">
								<tr class="th" style="height:35px">
									<td style="width:22px; border:1px solid #DDE0EC" class="thcell">
										<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
									</td>
									<td style="min-width:10%; border:1px solid #DDE0EC" class="thcell">id</td>
									<td style="width:10%; border:1px solid #DDE0EC" class="thcell">type</td>
									<td style="width:3%; border:1px solid #DDE0EC" class="thcell"></td>
									<td style="width:12%; border:1px solid #DDE0EC" class="thcell">last success time</td>
									<td style="width:12%; border:1px solid #DDE0EC" class="thcell">next start time</td>
									<td style="width:200px; border:1px solid #DDE0EC" class="thcell">status</td>
									<td style="width:30px; border:1px solid #DDE0EC" class="thcell"></td>
								</tr>
								<xsl:for-each select="query/entry">
									<tr>
										<xsl:variable name="num" select="position()"/>
										<xsl:attribute name="bgcolor">#ffffff</xsl:attribute>
										<td style="text-align:center; border:1px solid #ccc">
											<input type="checkbox" name="chbox" id="{@docid}">
												<xsl:attribute name="name" select="/request/view/entry/author"/>
											</input>
										</td>
										<td style="border:1px solid #ccc; padding-left:5px; word-wrap:break-word">
											<a class="doclink" title="{filename}">
												<xsl:attribute name="href">Provider?type=edit&amp;element=schedule&amp;id=<xsl:value-of select="id"/></xsl:attribute>
												<xsl:value-of select="id"/>
											</a>
										</td>
										<td class="title" style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="type"/>
										</td>
										<td class="title" style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="successrun"/>
										</td>
										<td class="title" style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="lastsuccess"/>
										</td>
										<td class="title" style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="nexttime"/>
										</td>
										<td class="title" style="border:1px solid #ccc; padding-left:5px">
											<xsl:value-of select="status"/>
										</td>
										<td class="title" style="border:1px solid #ccc; text-align:center">
											<a style="margin-left:10" title="run handler unscheduled...">
												<xsl:attribute name="href">Provider?type=service&amp;operation=do_scheduled_handler&amp;id=<xsl:value-of select="id"/></xsl:attribute>
												<img src="img/force_small.gif"/>
											</a>
										</td>
									</tr>
								</xsl:for-each>
							</table>
						</div>
					</div>
					<xsl:call-template name="footer"/>
				</div>
			</body>
		</html>
	</xsl:template>
</xsl:stylesheet>