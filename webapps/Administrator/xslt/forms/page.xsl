<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/action.xsl"/>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
	 doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes"/>
	<xsl:variable name="dbid" select="request/@dbid"/>
	<xsl:template match="/request">
		<head>
			<title>
				<xsl:value-of select="concat('Administrator - Query ',rule/@id)"/>
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
			<script type="text/javascript" src="scripts/view.js"/>
		</head>
		<body>
			<table width="100%">
				<tr>
					<td width="50%">
						<xsl:call-template name="showasxml_action"/>
						<a style="margin-left:10px">
							<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
							<img src="img/refresh.gif"/>
							<font class="button">Refresh</font>
						</a>
						<a style="margin-left:10px">
							<xsl:attribute name="href">javascript:saveRule('<xsl:value-of select="rule/@id"/>', '<xsl:value-of select="rule/@app"/>','<xsl:value-of select="@dbid"/>')</xsl:attribute>
							<img src="img/save.gif"/>
							<font class="button">Save</font>
						</a>
					</td>
					<td style="text-align:right">
						<xsl:call-template name="close_action"/>
					</td>
				</tr>
			</table>
			<br/>
			<font style="font-family:verdana; font-size:20px;">Query:&#xA0;</font>
			<xsl:value-of select="rule/@id"/>
			<br/>
			<div style="font-family:verdana; font-size:10px;margin-top:10">
				<xsl:value-of select="concat('hits: ',rule/hits)"/>
				<br/>
				<xsl:value-of select="concat('turbomode: ',rule/turbomode)"/>
			</div>
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
						<td class="fc">Is on:&#xA0;</td>
						<td>
							<input type="radio" name="ison" value="ON">
								<xsl:if test="rule/@ison = 'ON'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								ON
							</input>
							<input type="radio" name="ison" value="OFF">
								<xsl:if test="rule/@ison = 'OFF'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								OFF
							</input>
						</td>
					</tr>
					<tr>
					<input type="hidden" name="cachemode" id="cachemode" value="{rule/cachemode}"/>
						<td class="fc">Cache mode:&#xA0;</td>
						<td>
							<input type="radio" name="cachemode" value="ON">
								<xsl:if test="rule/cachemode = 'ON'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								ON
							</input>
							<input type="radio" name="cachemode" value="OFF">
								<xsl:if test="rule/cachemode = 'OFF'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								OFF
							</input>
						</td>
					</tr>
					<tr>
						<td class="fc">Doctype:&#xA0;</td>
						<td>
							<input type="radio" name="doctype" value="DOCUMENT">
								<xsl:attribute name="onclick">
									javascript:changeTypeQuery("<xsl:value-of select="rule/@id"/>","<xsl:value-of select="rule/@app"/>","<xsl:value-of select="$dbid"/>")
								</xsl:attribute>
								<xsl:if test="rule/doctype = 'DOCUMENT'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								DOCUMENT
							</input>
							<input type="radio" name="doctype" value="TASK">
								<xsl:attribute name="onclick">
									javascript:changeTypeQuery("<xsl:value-of select="rule/@id"/>","<xsl:value-of select="rule/@app"/>","<xsl:value-of select="$dbid"/>")
								</xsl:attribute>
								<xsl:if test="rule/doctype = 'TASK'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								TASK
							</input>
							<input type="radio" name="doctype" value="PROJECT">
								<xsl:attribute name="onclick">
									javascript:changeTypeQuery("<xsl:value-of select="rule/@id"/>","<xsl:value-of select="rule/@app"/>","<xsl:value-of select="$dbid"/>")
								</xsl:attribute>
								<xsl:if test="rule/doctype = 'PROJECT'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								PROJECT
							</input>
							<input type="radio" name="doctype" value="GLOSSARY">
								<xsl:attribute name="onclick">
									javascript:changeTypeQuery("<xsl:value-of select="rule/@id"/>","<xsl:value-of select="rule/@app"/>","<xsl:value-of select="$dbid"/>")
								</xsl:attribute>
								<xsl:if test="rule/doctype = 'GLOSSARY'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								GLOSSARY
							</input>
							<input type="radio" name="doctype" value="STRUCTURE">
								<xsl:attribute name="onclick">
									javascript:changeTypeQuery("<xsl:value-of select="rule/@id"/>","<xsl:value-of select="rule/@app"/>","<xsl:value-of select="$dbid"/>")
								</xsl:attribute>
								<xsl:if test="rule/doctype = 'STRUCTURE'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								STRUCTURE
							</input>
						</td>
					</tr>
					</table>
					<table border="0" style="margin-top:15px;width:80%">
					<tr>
						<td class="fc">Run under user:&#xA0;</td>
						<td>
							<table>
								<tr style="background:#ccc">
									<td>source</td>
									<td>value</td>
									<td>type</td>
								</tr>
								<tr>
									<td>
										<select size="1" name="rununderusersource" style="margin-top:4px">
											<xsl:variable name="rununderusersource" select="rule/rununderuser/source"/>
											<xsl:for-each select="rule/glossaries/rununderusersource/query/entry">
												<option value="{@viewtext}">
													<xsl:if test="$rununderusersource = @viewtext">
														<xsl:attribute name="selected" select="'selected'"/>
													</xsl:if>
													<xsl:value-of select="@viewtext"/>
												</option>
											</xsl:for-each>
										</select>
									</td>
									<td>
										<select size="1" name="rununderuser" style="margin-top:4px">
											<xsl:variable name="rununderuservalue" select="rule/rununderuser/value"/>
											<xsl:for-each select="rule/glossaries/rununderusermacro/query/entry">
												<option value="{@viewtext}">
													<xsl:if test="$rununderuservalue = @viewtext">
														<xsl:attribute name="selected" select="'selected'"/>
													</xsl:if>
													<xsl:value-of select="@viewtext"/>
												</option>
											</xsl:for-each>
												<option>
													<xsl:if test="$rununderuservalue = 'OBSERVER'">
														<xsl:attribute name="selected" select="'selected'"/>
													</xsl:if>
													OBSERVER
												</option>
										</select>
									</td>
									<td>
										<input type="text" name="rununderusertype" size="15" value="{rule/rununderuser/type}"/>
									</td>
								</tr>
							</table>
						</td>
					</tr>
					</table>
					<table border="0" style="margin-top:15px;width:80%">
					<tr>
						<td class="fc">
							<strong>Query:&#xA0;</strong>
						</td>
						<td></td>
					</tr>
					<tr>
						<td class="fc">source:&#xA0;</td>
						<td>
							<select size="1" name="groupby_source" style="margin-top:4px">
								<option>
									<xsl:if test="rule/groupby = 'STATIC'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									STATIC
								</option>
								<option>
									<xsl:if test="rule/groupby = 'MACRO'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									MACRO
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="fc">value:&#xA0;</td>
						<td>
							<textarea name="groupby_value" id="groupby" rows="2" cols="50">
								<xsl:value-of select="rule/groupby"/>
							</textarea>
						</td>
					</tr>
					<tr>
						<td class="fc">
							<strong>Groupby:&#xA0;</strong>
						</td>
						<td></td>
					</tr>
					<tr>
						<td class="fc">source:&#xA0;</td>
						<td>
							<select size="1" name="sortsource" style="margin-top:4px">
								<option>
									<xsl:if test="rule/querysource = 'STATIC'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									STATIC
								</option>
								<option>
									<xsl:if test="rule/querysource = 'MACRO'">
										<xsl:attribute name="checked">checked</xsl:attribute>
									</xsl:if>
									MACRO
								</option>
							</select>
						</td>
					</tr>
					<tr>
						<td class="fc">value:&#xA0;</td>
						<td>
							<textarea name="query" id="query" rows="2" cols="50">
								<xsl:value-of select="rule/query" />
							</textarea>
							<a style="margin-left:10" title="test query..." target="_blank">
								<xsl:attribute name="href">Provider?type=do_query&amp;id=<xsl:value-of
									select="rule/@id" />&amp;app=<xsl:value-of select="rule/@app" /></xsl:attribute>
								<img src="img/force.gif"></img>
							</a>
						</td>
					</tr>
				</table>
				<table border="0" style="margin-top:15px; width:80%">
					<tr>
						<td class="fc">Group by pubformat:&#xA0;</td>
						<td>
							<input type="radio" name="groupbypubformat" value="AS_IS" >
								<xsl:if test="rule/groupbypublicationformat = 'AS_IS'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								AS_IS
							</input>
							<input type="radio" name="groupbypubformat" value="USER">
								<xsl:if test="rule/groupbypublicationformat = 'USER'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								USER
							</input>
							<input type="radio" name="groupbypubformat" value="EMPLOYER">
								<xsl:if test="rule/groupbypublicationformat = 'EMPLOYER'">
									<xsl:attribute name="checked">checked</xsl:attribute>
								</xsl:if>
								EMPLOYER
							</input>
						</td>
					</tr>
					<tr>
						<td class="fc">Fields condition:&#xA0;</td>
						<td>
							<textarea name="fieldscondition" id="fieldscondition" rows="2" cols="50">
								<xsl:value-of select="rule/fieldscondition"/>
							</textarea>
						</td>
					</tr>
					</table>
					<table border="0" style="margin-top:18px;width:80%">
					<tr>
						<td class="fc">Fields:&#xA0;</td>
						<td>
							<table border="1px solid gray" style="width:130%" id="rulefieldstable">
								<tr style="border:1px solid gray;">
									<td colspan="7">
										&#xA0;<a href="javascript:addFieldToRule()">
											Add
										</a>
										&#xA0;
										<a href="javascript:removeRuleFields()">
											Remove
										</a>&#xA0;
										<a href="javascript:editRuleFields()">
											Edit
										</a>
									</td>
								</tr>
								<tr style="background:#ccc; border-top:1px solid gray">
									<td align="center" width="2%"><input type="checkbox" id="allchbox" onClick="checkAll(this);"/></td>
									<td width="15%">Name</td>
									<td width="8%">Value source</td>
									<td width="35%">Value</td>
									<td width="12%">Macro</td>
									<td width="12%">If error</td>
									<td width="12%">Publish as</td>
								</tr>
								<xsl:for-each select="rule/fields/field">
									<tr style="border-bottom:1px solid gray">
										<td align="center">
											<input type="checkbox" name="chbox" id="chbox"/>
										</td>
										<td>
											<xsl:value-of select="name"/>
										</td>
										<td>
											<xsl:value-of select="valuesource"/>
										</td>
										<td>
											<xsl:value-of select="value"/>
										</td>
										<td>
											<xsl:value-of select="macro"/>
										</td>
										<td>
											<xsl:value-of select="iferror"/>
										</td>
										<td>
											<xsl:value-of select="publishas"/>
										</td>
										<input type="hidden" name="field">
											<xsl:attribute name="value">
												<xsl:value-of select="concat(name,'#',publishas,'#',source,'^',value,'^',type)"/>
											</xsl:attribute>
										</input>
									</tr>
								</xsl:for-each>
							</table>
						</td>
					</tr>
				</table>
				<!-- Скрытые поля -->
				<input type="hidden" name="id" id="id" value="{rule/@id}"/>
				<input type="hidden" name="groupby" id="groupby" value="{rule/groupby}"/>
			</form>
		</body>
	</xsl:template>
</xsl:stylesheet>