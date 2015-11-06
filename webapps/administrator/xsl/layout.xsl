<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

	<xsl:import href="templates/constants.xsl" />

	<xsl:output method="html" encoding="utf-8" indent="no" />

	<xsl:template name="layout">
		<xsl:param name="w_title" select="concat(//captions/title/@caption, ' - ', //captions/brand/@caption)" />
		<xsl:param name="include" select="''" />
		<xsl:param name="body_class" select="''" />

		<xsl:call-template name="HTML-DOCTYPE" />
		<html>
			<xsl:call-template name="html_head">
				<xsl:with-param name="w_title" select="$w_title" />
				<xsl:with-param name="include" select="$include" />
			</xsl:call-template>
			<body class="no_transition {$body_class}">
				<div class="layout">
					<xsl:call-template name="main-header" />
					<section class="content">
						<xsl:call-template name="_content" />
					</section>
					<xsl:call-template name="main-footer" />
				</div>
			</body>
		</html>
	</xsl:template>

	<xsl:template name="html_head">
		<xsl:param name="include" select="''" />
		<xsl:param name="w_title" select="''" />
		<head>
			<title>
				<xsl:value-of select="$w_title" />
			</title>
			<link rel="shortcut icon" href="favicon.ico" />
			<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no" />
			<link rel="stylesheet" href="/SharedResources/vendor/font-awesome/css/font-awesome.min.css" />
			<link rel="stylesheet" href="/SharedResources/vendor/bootstrap/css/bootstrap.min.css" />
			<link rel="stylesheet" href="css/layout.css" />
			<link rel="stylesheet" href="css/content.css" />

			<script type="text/javascript" src="/SharedResources/vendor/jquery/jquery-2.1.4.min.js"></script>
			<script type="text/javascript" src="/SharedResources/vendor/bootstrap/js/bootstrap.min.js"></script>
			<script type="text/javascript" src="js/app.js"></script>

			<xsl:copy-of select="$include" />
		</head>
	</xsl:template>

	<xsl:template name="main-header">
		<header class="header navbar navbar-static-top">
			<div class="container">
				<div class="navbar-header">
					<a class="navbar-brand" href="?id=about">
						<img src="img/logo.png" alt="logo" /><xsl:value-of select="//captions/brand/@caption" />
					</a>
				</div>
				
			</div>
		</header>
	</xsl:template>


	<xsl:template name="main-footer">
		<div class="footer-spacer"></div>
		<footer class="footer">
			<div class="container">
				© 2015 <a href="//flabser.com">flabser.com</a>
			</div>
		</footer>
	</xsl:template>

	<xsl:template name="_content" />

</xsl:stylesheet>
