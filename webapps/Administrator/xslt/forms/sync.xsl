<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="../layout.xsl" />
	<xsl:import href="../templates/action.xsl" />

	<xsl:template match="/request">
		<xsl:call-template name="layout">
			<xsl:with-param name="w_title" select="concat('Administrator - Synchronizer ', rule/@id)" />
		</xsl:call-template>
	</xsl:template>

	<xsl:template name="_content">
		<table width="100%">
			<tr>
				<td width="50%">
					<a style="margin-left:1em">
						<xsl:attribute name="href">javascript:saveRule()</xsl:attribute>
						<img src="img/save.gif" />
						<font class="button">Save &amp; Close</font>
					</a>
					<xsl:call-template name="showasxml_action" />
				</td>
				<td style="text-align:right">
					<xsl:call-template name="close_action" />
				</td>
			</tr>
		</table>
		<br />
		<font style="font-family:verdana; font-size:20px;">Synchronizer:&#xA0;</font>
		<xsl:value-of select="rule/@id" />
		<hr />
		<form action="Provider" method="post" id="scriptest" enctype="application/x-www-form-urlencoded">
			<table border="0" style="margin-top:10px;width:80%">
				<tr>
					<td class="fc">Description:&#xA0;</td>
					<td>
						<textarea name="description" id="description" rows="2" cols="50">
							<xsl:value-of select="rule/description" />
						</textarea>
					</td>
				</tr>
				<tr>
					<td class="fc">Source :&#xA0;</td>
					<td>
						<select size="1" name="source" style="margin-top:4px">
							<option>nsf</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="fc">Domino host :&#xA0;</td>
					<td>
						<input type="text" name="dominohost" size="15" value="{rule/dominohost}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Domino user :&#xA0;</td>
					<td>
						<input type="text" name="dominouser" size="15" value="{rule/dominouser}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Domino pwd :&#xA0;</td>
					<td>
						<input type="password" name="dominopwd" size="15" value="{rule/dominopwd}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Domino server :&#xA0;</td>
					<td>
						<input type="text" name="dominoserver" size="15" value="{rule/dominoserver}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Domino database :&#xA0;</td>
					<td>
						<input type="text" name="dominodatabase" size="15" value="{rule/dominodatabase}" />
					</td>
				</tr>
				<tr>
					<td />
					<td>Pull</td>
				</tr>
				<tr>
					<td class="fc">View&#xA0;:</td>
					<td>
						<input type="text" name="dominoview" size="15" value="{rule/pull/dominoview}" />
					</td>
				</tr>
				<tr>
					<td class="fc">DocType :&#xA0;</td>
					<td>
						<select size="1" name="doctype" style="margin-top:4px">
							<option>document</option>
							<option>task</option>
							<option>execution</option>
							<option>project</option>
							<option>glossary</option>
						</select>
					</td>
				</tr>
				<tr>
					<td class="fc">Cutt OFF&#xA0;:</td>
					<td>
						<input type="text" name="cutoff" size="20" value="{rule/pull/cutoff}" />
					</td>
				</tr>
				<tr>
					<td class="fc">Fields&#xA0;:</td>
					<td>
						<table>
							<tr style="background-color:#F1F0F0">
								<td width="10%" />
								<td width="15%">type</td>
								<td width="20%">name</td>
								<td width="15%">valuesource</td>
								<td width="15%">valuetype</td>
								<td>value</td>
							</tr>
							<xsl:for-each select="rule/pull/field">
								<tr>
									<td>
										<xsl:value-of select="@mode" />
									</td>
									<td>
										<xsl:value-of select="@type" />
									</td>
									<td>
										<xsl:value-of select="name" />
									</td>
									<td>
										<xsl:value-of select="value/@source" />
									</td>
									<td>
										<xsl:value-of select="value/@type" />
									</td>
									<td>
										<xsl:value-of select="value" />
									</td>
								</tr>
							</xsl:for-each>
						</table>
					</td>
				</tr>
			</table>
			<input type="hidden" name="type" id="type" value="do_sync" />
			<input type="hidden" name="app" id="app" value="{rule/@app}" />
			<input type="hidden" name="id" id="id" value="{rule/@id}" />
		</form>
	</xsl:template>

</xsl:stylesheet>
