<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:import href="../templates/view.xsl" />
	<xsl:variable name="type">get_doc_list</xsl:variable>
	<xsl:variable name="doctype">rule</xsl:variable>
	<xsl:output method="html" encoding="utf-8" doctype-public="-//W3C//DTD XHTML 1.0 Transitional//EN"
		doctype-system="http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd" indent="yes" />

	<xsl:template match="/">
		<div class="actionbar">
			<span class="action">
				<a>
					<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
					<img src="img/refresh.gif" />
					<font class="button">Обновить</font>
				</a>
				<a style="margin-left:10px">
					<xsl:attribute name="href">javascript:delDoc('doctype');</xsl:attribute>
					<img src="img/delete.gif" />
					<font class="button">Удалить документ</font>
				</a>
			</span>
		</div>
		<table class="viewtable">
			<tr class="th">
				<td width="22px" class="thcell">
					<input type="checkbox" id="allchbox" onClick="checkAll(this);"/>
				</td>
				<td width="5%" class="thcell"></td>
				<td width="20%" class="thcell">Имя</td>
				<td width="15%" class="thcell">Тип правила</td>
			</tr>
			<xsl:for-each select="request/view/entry">
				<tr>
					<xsl:variable name="num" select="position()"/>
					<xsl:if test="$num mod 2 = 0">
						<xsl:attribute name="bgcolor">#efefef</xsl:attribute>
					</xsl:if>
					<td>
						<input type="checkbox" name="chbox" id="{@key}"/>
					</td>
					<td style="font-size:0.8em;color:gray">
						<xsl:value-of select="$num"/>
					</td>
					<td>
						<a title="{name}">
							<xsl:attribute name="href">Provider?type=get_xml&amp;xmlname=<xsl:value-of select="name"/>&amp;typerule=<xsl:value-of select="type"/></xsl:attribute>
							&#xA0;
							<xsl:value-of select="name"/>
						</a>
					</td>
					<td class="title" style="font-size:0.8em">
						<xsl:value-of select="type"/>
					</td>
				</tr>
			</xsl:for-each>
		</table>
	</xsl:template>
</xsl:stylesheet>