<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />
	<xsl:import href="templates/side-nav.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />
	<xsl:decimal-format name="df" grouping-separator=" " />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="concat(//captions/viewnamecaption/@caption, ' - ', $APP_NAME)" />
		<xsl:param name="active_aside_id" select="//app_menu//current/entry/@id" />
		<xsl:param name="include" select="''" />
		<xsl:param name="body_class" select="''" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<xsl:call-template name="html_head">
				<xsl:with-param name="w_title" select="$w_title" />
				<xsl:with-param name="include" select="$include" />
			</xsl:call-template>
			<body class="no_transition layout_fullscreen {$body_class}">
				<div class="layout">
					<div class="content-overlay" id="content-overlay"></div>
					<aside class="layout_aside nav-app">
						<xsl:call-template name="main_side" />
					</aside>
					<section class="layout_content">
						<xsl:call-template name="_content" />
					</section>
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="_content" />

	<xsl:template name="html_head">
		<xsl:param name="include" select="''" />
		<xsl:param name="w_title" select="''" />
		<head>
			<title>
				<xsl:value-of select="$w_title" />
			</title>
			<link rel="shortcut icon" href="favicon.ico" />
			<meta name="format-detection" content="telephone=no" />
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />

			<link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css" />
			<link rel="stylesheet" href="/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.css" />

			<script src="/SharedResources/vendor/jquery/jquery-1.11.3.min.js"></script>
			<script src="/SharedResources/vendor/jquery/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script>

			<script src="/SharedResources/vendor/jquery/jquery.cookie.min.js"></script>
			<script src="/SharedResources/vendor/jquery/jquery.scrollTo.min.js"></script>
			<script src="/SharedResources/vendor/jquery/jquery.number.min.js"></script>

			<link rel="stylesheet" href="css/main.css" />
			<link rel="stylesheet" href="css/form.css" />
			<link rel="stylesheet" href="css/dialogs.css" />
			<link rel="stylesheet" href="css/view.css" />
			<link rel="stylesheet" href="css/all.min.css" />
			<link rel="stylesheet" href="css/outline.css" />
			<link rel="stylesheet" href="css/common.css" />
			<link rel="stylesheet" href="css/layout.css" />
			<link rel="stylesheet" href="css/side-nav-app.css" />

			<script src="js/stdlib/dynamicform.js"></script>
			<script src="js/outline.js"></script>
			<script src="js/service.js"></script>
			<script src="js/dialogs.js"></script>
			<script src="js/form.js"></script>

			<script>
				$(document).ready(function(){
				$(".outline-ch").expander(true);
				});
			</script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main_side">
		<div class="side">
			<header>
				<h1 class="header-admin">Administrator</h1>
			</header>
			<section>
				<xsl:call-template name="outline" />
			</section>
			<footer>
				<a href="Logout" title="{//fields/logout/@caption}" class="logout">
					<img src="img/logout.gif" alt="" />
					logout
				</a>
				<div class="copy">NextBase © 2014</div>
			</footer>
		</div>
	</xsl:template>

</xsl:stylesheet>
