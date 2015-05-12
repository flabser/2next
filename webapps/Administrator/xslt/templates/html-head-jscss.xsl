<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="view-html-head-jscss">
		<link rel="stylesheet" href="css/main.css"/>
		<link rel="stylesheet" href="css/view.css"/>
		<link rel="stylesheet" href="css/dialogs.css"/>
		<link rel="stylesheet" href="css/outline.css"/>
		<link rel="stylesheet" href="/SharedResources/jquery/css/base/jquery-ui-1.8.2.redmont.css" />
		<link rel="stylesheet" href="css/layout.css"/>

		<script type="text/javascript" src="/SharedResources/jquery/js/jquery-1.4.2.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/cookie/jquery.cookie.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.core.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.widget.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.mouse.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.draggable.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.position.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.resizable.js"/>
		<script type="text/javascript" src="/SharedResources/jquery/js/ui/jquery.ui.dialog.js"/>

		<script type="text/javascript" src="scripts/outline.js"/>
		<script type="text/javascript" src="scripts/service.js"/>
		<script type="text/javascript" src="scripts/dialogs.js"/>
		<script type="text/javascript" src="scripts/form.js"/>
		<script type="text/javascript" src="scripts/view.js"/>

		<script>
			$(document).ready(function(){
				$(".outline-ch").expander(true);
			});
		</script>
	</xsl:template>

</xsl:stylesheet>
