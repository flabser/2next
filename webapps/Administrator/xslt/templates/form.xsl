<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="html" encoding="windows-1251" />
	<xsl:template name="author_field">		
		<div style="border-bottom: 1px solid gray; width:98%">	
       		 <div style="float:right;margin-right:2%"><xsl:value-of select="concat('Автор: ',../@user)"/></div>
        </div>
	</xsl:template>
	
	<xsl:template name="id_field">
		<tr>
			<td width="30%" class="fc">ID&#xA0;:</td>
			<td>
				<input type="text" size="15" class="rof" readonly="readonly" value="{@key}"/>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="description_field">
		<tr>
			<td class="fc">Описание&#xA0;:</td>
			<td>
				<textarea name="description" rows="3" cols="50" class="rof">
					<xsl:value-of select="description"/>
				</textarea>
			</td>
		</tr>
	</xsl:template>
	
	<xsl:template name="isvalid_field">
		<tr>
			<td class="fc" style="height:30">Состояние&#xA0;:</td>
				<td>
					<xsl:choose>
						<xsl:when test="isvalid = 'true'">
							<img src="img/isvalid.gif" />
							<font style="margin-left:5;font-size:0.6em">корректно</font>
						</xsl:when>
						<xsl:otherwise>
							<img src="img/isnotvalid.gif" />
							<font style="margin-left:5;font-size:0.6em">не корректно</font>
						</xsl:otherwise>
					</xsl:choose>
					&#xA0;&#xA0;
					<xsl:choose>
						<xsl:when test="ison = 'true'">
							<img src="img/run.gif" />
							<font style="margin-left:5;font-size:0.6em">работает</font>
						</xsl:when>
						<xsl:otherwise>
							<img src="img/stop.gif" />
							<font style="margin-left:5;font-size:0.6em">остановлено</font>
						</xsl:otherwise>
					</xsl:choose>
				</td>
			</tr>
	</xsl:template>
</xsl:stylesheet>