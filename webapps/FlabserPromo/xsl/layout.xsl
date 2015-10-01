<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="//captions/promo/@caption" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<head>
				<meta charset="utf-8" />
				<meta http-equiv="X-UA-Compatible" content="IE=edge" />
				<meta name="format-detection" content="telephone=no" />
				<meta name="format-detection" content="email=no" />
				<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
				<link rel="shortcut icon" href="favicon.png" />
				<title>
					<xsl:value-of select="$w_title" />
				</title>
				<link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css" />
				<link rel="stylesheet" href="/SharedResources/vendor/bootstrap/css/bootstrap.min.css" />
				<link rel="stylesheet" href="/SharedResources/css/animate.css" />

				<link rel="stylesheet" href="css/layout.css" />
				<link rel="stylesheet" href="css/navbar.css" />
				<link rel="stylesheet" href="css/heading.css" />
				<link rel="stylesheet" href="css/promo.css" />
				<link rel="stylesheet" href="css/about.css" />
				<link rel="stylesheet" href="css/skills.css" />
				<link rel="stylesheet" href="css/contact.css" />
			</head>
			<body data-spy="scroll" data-target=".navbar" data-offset="51">
				<div class="layout wrapper">
					<xsl:call-template name="_content" />
				</div>
				<!-- JavaScript
				================================================== -->
				<script src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
				<script src="/SharedResources/vendor/bootstrap/js/bootstrap.min.js"></script>
				<script src="/SharedResources/vendor/jquery/jquery.waypoints.min.js"></script>
				
				<!-- <script src="js/contact.js"></script> -->
				<script src="js/app.js"></script>

				<script type="text/javascript">
					<![CDATA[
		            (function() {
		                var link_element = document.createElement("link"),
		                    s = document.getElementsByTagName("script")[0];
		                if (window.location.protocol !== "http:" && window.location.protocol !== "https:") {
		                    link_element.href = "http:";
		                }
		                link_element.href += "//fonts.googleapis.com/css?family=Dosis:200,300,400,500,600,700,800";
		                link_element.rel = "stylesheet";
		                link_element.type = "text/css";
		                s.parentNode.insertBefore(link_element, s);
		            })();]]>
		        </script>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="_content" />

</xsl:stylesheet>
