<?xml version="1.0" encoding="windows-1251"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:template name="view-html-head-jscss">
		<link rel="stylesheet" href="css/main.css" />
		<link rel="stylesheet" href="css/view.css" />
		<link rel="stylesheet" href="css/dialogs.css" />
		<link rel="stylesheet" href="css/outline.css" />
		<link rel="stylesheet" href="css/layout.css" />
		<link type="text/css" rel="stylesheet" href="/SharedResources/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.css" />
		<link rel="stylesheet" href="css/all.min.css" />

		<script type="text/javascript" src="/SharedResources/jquery/jquery-1.11.3.min.js"></script>
		<script type="text/javascript" src="/SharedResources/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>

		<script type="text/javascript" src="/SharedResources/jquery/cookie/jquery.cookie.js"></script>
		<script type="text/javascript" src="/SharedResources/jquery/jquery.scrollTo.min.js"></script>

		<script type="text/javascript" src="scripts/outline.js" />
		<script type="text/javascript" src="scripts/service.js" />
		<script type="text/javascript" src="scripts/dialogs.js" />
		<script type="text/javascript" src="scripts/form.js" />
		<script type="text/javascript" src="scripts/view.js" />

		<script>
			$(document).ready(function(){
				$(".outline-ch").expander(true);
			});
		</script>
	</xsl:template>

</xsl:stylesheet>
