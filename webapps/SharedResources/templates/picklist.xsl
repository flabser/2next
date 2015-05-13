<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:output method="xml" />
	<xsl:template match="/content">
		<head>
			<script><![CDATA[		
			
				alert(view);	
				view = 'typedoc';
				var pars = 'type=view&id='+view;
				var myAjax = new Ajax.Updater('viewpane', url, {method: 'get', asynchronous: false, parameters: pars});	
				
			]]>
			</script>
			
		</head>
		<div id="viewpane">
        </div>	
	</xsl:template>
</xsl:stylesheet>
