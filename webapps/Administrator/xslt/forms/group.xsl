<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat('Administrator - Group - ', document/id)" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<table width="100%">
			<tr>
				<td width="50%">
					<a style="margin-left:0.5em">
						<xsl:attribute name="href">javascript:saveGroup()</xsl:attribute>
						<img src="img/save.gif" />
						<font class="button">Save &amp; Close</font>
					</a>
					<a style="margin-left:0.5em">
						<xsl:attribute name="href">javascript:addInGroup(<xsl:value-of select="document/id" />)</xsl:attribute>
						<img src="img/new.gif" />
						<font class="button">Add user</font>
					</a>
					<xsl:call-template name="showasxml_action" />
				</td>
				<td style="text-align:right">
					<xsl:if test="document/@docid='0'">
						<font style="font-size:0.7em">new group</font>
					</xsl:if>
				</td>
				<td style="text-align:right">
					<xsl:call-template name="close_action" />
				</td>
			</tr>
		</table>
		<br />
		<font style="font-family:verdana; font-size:20px;">Group:&#xA0;</font>
		<xsl:value-of select="document/id" />
		<hr />
		<form action="Provider" method="post" id="scriptest" enctype="application/x-www-form-urlencoded">
			<table border="0" style="margin-top:10px;width:80%">
				<tr>
					<td class="fc">groupID:&#xA0;</td>
					<td>
						<input type="text" name="userid" size="20" value="{document/userid}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Comment:&#xA0;</td>
					<td>
						<textarea type="text" name="comment" size="20" value="{document/comment}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Userlist:&#xA0;</td>
					<td />
				</tr>
				<xsl:for-each select="document/userlist/user">
					<tr>
						<td class="fc" />
						<td>
							<xsl:value-of select="shortname" />
							&#xA0;
							<a href="">
								<xsl:attribute name="href">javascript:deleteUserInGroup(<xsl:value-of select="@id" />)</xsl:attribute>
							</a>
						</td>
					</tr>
				</xsl:for-each>
			</table>
			<input type="hidden" name="key" id="key" value="{document/@docid}" />
		</form>
	</xsl:template>

</xsl:stylesheet>
