<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	
	<!-- кнопка сохранения  -->
	<xsl:template name="save">
		<a>
			<xsl:attribute name="href">javascript:SaveForm('kr','frm')</xsl:attribute>
			<img src="img/save.gif" style="border:none"/>
			<font class="button">Сохранить и закрыть</font>
		</a>
	</xsl:template>
	
	<xsl:template name="cancel">
		<a  style="margin-left:1em">
			<xsl:attribute name="href">javascript:CancelForm()</xsl:attribute>
			<img src="img/cancel.gif" style="border:0;"/>
			<font class="button">Закрыть</font>					
		</a>	
	</xsl:template>
</xsl:stylesheet>