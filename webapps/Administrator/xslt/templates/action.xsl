<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template name="save_action">
		<a>
			<xsl:attribute name="href">javascript:alert('В текущей версии выбранное действие не доступно');</xsl:attribute>
			<img src="img/save.gif"/>
			<font class="button">Сохранить и закрыть</font>
		</a>
	</xsl:template>
	<xsl:template name="close_action">
		<a>
			<xsl:attribute name="href">javascript:window.history.back();</xsl:attribute>
			<img src="img/close.gif" style="margin-left:5px"/>
			<font class="button">Close</font>
		</a>
	</xsl:template>
	<xsl:template name="showasxml_action">
		<a style="margin-left:5">
			<xsl:attribute name="href">javascript:window.location = window.location + '&amp;onlyxml=1'</xsl:attribute>
			<img src="img/xml.gif"/>
			<font class="button">Show as XML</font>
		</a>
	</xsl:template>
	<xsl:template name="refresh_action">
		<a>
			<xsl:attribute name="href">javascript:window.location.reload();</xsl:attribute>
			<img src="img/refresh.gif"/>
			<font class="button">Refresh</font>
		</a>
	</xsl:template>
</xsl:stylesheet>