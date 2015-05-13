<?xml version="1.0" encoding="windows-1251"?>

<!-- 
SmartDoc v. 1.2
Набор шаблонов связанных с отображением хода исполнения
Copyright F labs's (c) 
Author Kairat 
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<!-- Функция запускающая получение view "хода исполнения" -->
	<xsl:template name="showdocthread">
		<script>
			<xsl:if  test="@status!='new'">
				function onLoadActions(){
					$('docthread').innerHTML = '';		
					var url = 'Provider';
					var pars = 'type=view&amp;id=responses&amp;key=<xsl:value-of select="$threaddocid"/>';
					var myAjax = new Ajax.Updater('docthread', url, {method: 'get', parameters: pars});											
				}
			</xsl:if>						
		</script>		
	</xsl:template>
</xsl:stylesheet>